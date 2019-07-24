package io.elastic.jdee1.actions;

import io.elastic.api.ExecutionParameters;
import io.elastic.api.InvalidCredentialsException;
import io.elastic.api.Message;
import io.elastic.api.Module;
import io.elastic.jdee1.Utils;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    Utils jdeinstance = new Utils();
    final JsonObjectBuilder result = Json.createObjectBuilder();
    final JsonObjectBuilder snapshot = Json.createObjectBuilder();
    final JsonObjectBuilder body = Json.createObjectBuilder();
    final JsonObject configuration = parameters.getConfiguration();
    try {
      result.add("result",
          jdeinstance.getTemplate_actionPerformed(configuration, snapshot.build(), body.build()));
    } catch (Exception e) {
      try {
        throw new InvalidCredentialsException("Failed to connect to instance", e);
      } catch (InvalidCredentialsException e1) {
        e1.printStackTrace();
      }
    } finally {
      if (result != null) {
        logger.info("Closing connection");
      } else {
        logger.error("Failed to closed connection");
      }
    }

    logger.info("Emitting new snapshot {}", snapshot.toString());
    // emitting the snapshot to the platform
    parameters.getEventEmitter().emitSnapshot((JsonObject) snapshot);

    logger.info("Emitting data {}", result);
    final Message data
        = new Message.Builder().body(result.build()).build();
    // emitting the message to the platform
    parameters.getEventEmitter().emitData(data);
  }
}
