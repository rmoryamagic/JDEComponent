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
public class verifyCredentials implements Module {

  private static final Logger logger = LoggerFactory.getLogger(verifyCredentials.class);

  /**
   * Executes the actions's logic by sending a request and emitting response to the platform.
   *
   * @param parameters execution parameters
   */
  @Override
  public void execute(final ExecutionParameters parameters) {
    logger.info("About to verify provided JDE E1 Credentials");
    // incoming message
    final Message message = parameters.getMessage();
    // body contains the snapshot
    JsonObjectBuilder snapshot = snapshot = Json.createObjectBuilder();
    // body contains the mapped data
    final JsonObject body = null;
    // contains action's configuration
    final JsonObject configuration = parameters.getConfiguration();

    Utils jdeinstance = new Utils();
    JsonObject execResult = null;

    try {
      execResult = jdeinstance.jbExecute_actionPerformed(configuration, body, snapshot.build());
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to connect to instance");
    } catch (SAXException e) {
      e.printStackTrace();
    } finally {
      if (execResult != null) {
        logger.info("Closing connection");
      } else {
        logger.error("Failed to close connection");
      }
    }

    final Message data
        = new Message.Builder().body(execResult).build();
    // emitting the message to the platform
    parameters.getEventEmitter().emitData(data);
  }

}
