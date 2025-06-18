package clay.vehicle.networking;

import clay.vehicle.CommandProcessor;
import clay.vehicle.InvalidInstructionException;
import clay.vehicle.commands.Executable;
import clay.vehicle.commands.RecursionException;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OnReadExecutionCallback implements ServerProcessingCallback {
  private static final Logger logger = LogManager.getLogger(OnReadExecutionCallback.class);
  @Getter CommandProcessor processor;

  public OnReadExecutionCallback() {
    this.processor = new CommandProcessor();
  }

  public void attachCommand(Executable command, String name) {
    processor.attachCommand(command, name);
  }

  @Override
  public Object execute(NetworkMessage message) {
    StringBuilder instructionBuilder = new StringBuilder();
    String returnMessage;

    logger.info("Processing message " + NetworkMessageSerializer.serialize(message));
    if (message.getType() != MessageType.COMMAND) {
      logger.warn("Message is not a command, informing client");
      return NetworkMessage.newBuilder()
          .setType(MessageType.ERROR)
          .setMessage(
              "Server request should be a command: " + NetworkMessageSerializer.serialize(message))
          .setAdress(message.getAddress())
          .build();
    }
    if (message.getCommand().isEmpty()) {
      logger.warn("Message does not specify command, informing client");
      return NetworkMessage.newBuilder()
          .setType(MessageType.ERROR)
          .setMessage("Command is empty")
          .setAdress(message.getAddress())
          .build();
    }

    instructionBuilder.append(message.getCommand());
    if (message.hasArgs()) {
      instructionBuilder.append(" ");
      instructionBuilder.append(String.join(" ", message.getArgs()));
    }

    processor.addInstruction(instructionBuilder.toString());
    try {
      returnMessage = processor.runReturnable();
    } catch (InvalidInstructionException e) {
      logger.warn("Unknown command, informing client");
      return NetworkMessage.newBuilder()
          .setType(MessageType.ERROR)
          .setMessage("Unknown command")
          .setAdress(message.getAddress())
          .build();
    } catch (RecursionException e) {
      logger.warn("Recursion detected, aborting execution and informing client");
      processor.clearQueue();
      return NetworkMessage.newBuilder()
          .setType(MessageType.ERROR)
          .setMessage("Recursion not allowed. Call stack: " + e.getMessage())
          .setAdress(message.getAddress())
          .build();
    }

    if (returnMessage.charAt(0) == '!') {
      logger.warn(
          "Execution from " + message.getAddress() + " resulted in exception: " + returnMessage);
      return NetworkMessage.newBuilder()
          .setType(MessageType.ERROR)
          .setMessage(returnMessage)
          .setAdress(message.getAddress())
          .build();
    } else
      return NetworkMessage.newBuilder()
          .setType(MessageType.RESPONSE)
          .setMessage(returnMessage)
          .setAdress(message.getAddress())
          .build();
  }
}
