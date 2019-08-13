package io.elastic.jdee1.actions;

import io.elastic.api.ExecutionParameters;
import io.elastic.api.Message;
import io.elastic.api.Module;
import io.elastic.jdee1.Utils;
import java.io.IOException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * Action to execute jde function
 */
public class executeFunction implements Module {

  private static final Logger logger = LoggerFactory.getLogger(executeFunction.class);

  /**
   * Executes the actions's logic by sending a request and emitting response to the platform.
   *
   * @param parameters execution parameters
   */
  @Override
  public void execute(final ExecutionParameters parameters) {
    logger.info("About to get functions list");
    // incoming message
    final Message message = parameters.getMessage();
    // body contains the snapshot
    JsonObject snapshot = parameters.getSnapshot();
    // body contains the mapped data
    final JsonObject body = message.getBody();
    // contains action's configuration
    final JsonObject configuration = parameters.getConfiguration();

    Utils jdeinstance = new Utils();
    final JsonObjectBuilder result = Json.createObjectBuilder();
    JsonObject execResult = Json.createObjectBuilder().build();

    try {
      execResult = jdeinstance.jbExecute_actionPerformed(configuration, body, snapshot);
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }

    String user = "";
    if (Utils.getNonNullString(configuration, Utils.CFG_USER).length() != 0) {
      user = configuration.getString(Utils.CFG_USER);
    }
    String password = "";
    if (Utils.getNonNullString(configuration, Utils.CFG_PASSWORD).length() != 0) {
      password = configuration.getString(Utils.CFG_PASSWORD);
    }
    String env = "";
    if (Utils.getNonNullString(configuration, Utils.CFG_ENV).length() != 0) {
      env = configuration.getString(Utils.CFG_ENV);
    }
    String function = "";
    if (Utils.getNonNullString(configuration, Utils.CFG_FUNCTION).length() != 0) {
      function = configuration.getString(Utils.CFG_FUNCTION);
    }
    String server = "";
    if (Utils.getNonNullString(configuration, Utils.CFG_SERVER).length() != 0) {
      server = configuration.getString(Utils.CFG_SERVER);
    }
    String port = "";
    if (Utils.getNonNullString(configuration, Utils.CFG_PORT).length() != 0) {
      port = configuration.getString(Utils.CFG_PORT);
    }
    String session = "";
    if (Utils.getNonNullString(execResult, Utils.SESSION).length() != 0) {
      session = execResult.getString(Utils.SESSION);
    }

    snapshot = Json.createObjectBuilder().add(Utils.CFG_USER, user)
        .add(Utils.CFG_PASSWORD, password)
        .add(Utils.CFG_ENV, env)
        .add(Utils.CFG_FUNCTION, function)
        .add(Utils.CFG_SERVER, server)
        .add(Utils.CFG_PORT, port)
        .add(Utils.SESSION, session).build();

    logger.info("Emitting new snapshot {}", snapshot.toString());
    // emitting the snapshot to the platform
    parameters.getEventEmitter().emitSnapshot(snapshot);

    logger.info("Emitting data {}", execResult);

    if(execResult.error_code!=0)
    {
     throw new IllegalStateException(execResult);
    }
    final Message data
        = new Message.Builder().body(execResult).build();
    // emitting the message to the platform
    parameters.getEventEmitter().emitData(data);
  }
}
