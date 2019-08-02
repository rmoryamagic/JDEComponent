package io.elastic.jdee1;
import io.elastic.api.Message;
import io.elastic.api.CredentialsVerifier;
import io.elastic.api.InvalidCredentialsException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdeCredentialsVerifierAction implements Module {

  private static final Logger logger = LoggerFactory.getLogger(JdeCredentialsVerifier.class);

  /**
     * Executes the actions's logic by sending a request to the verify creden API and emitting response to the platform.
     *
     * @param parameters execution parameters
     */

  @Override
  public void execute(ExecutionParameters parameters) {
    logger.info("About to verify provided JDE E1 Credentials");

    Utils jdeinstance = new Utils();
    final Message message = parameters.getMessage();
    final JsonObject configuration = parameters.getConfiguration();
    final JsonObjectBuilder result = Json.createObjectBuilder();
    final JsonObjectBuilder snapshot = Json.createObjectBuilder();
    final JsonObject body = message.getBody();//Json.createObjectBuilder();
    try {
      result.add("result",
          jdeinstance.getTemplate_actionPerformed(configuration, snapshot.build(), body));
    } catch (Exception e) {
      throw new InvalidCredentialsException("Failed to connect to instance", e);
    } finally {
      if (result != null) {
        logger.info("Closing connection");
      } else {
        logger.error("Failed to close connection");
      }
    }
  }

}