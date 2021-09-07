package com.automationanywhere.commands;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FilesinFolderCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(FilesinFolderCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    FilesinFolder command = new FilesinFolder();
    HashMap<String, Object> convertedParameters = new HashMap<String, Object>();
    if(parameters.containsKey("inputFolderPath") && parameters.get("inputFolderPath") != null && parameters.get("inputFolderPath").get() != null) {
      convertedParameters.put("inputFolderPath", parameters.get("inputFolderPath").get());
      if(convertedParameters.get("inputFolderPath") !=null && !(convertedParameters.get("inputFolderPath") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","inputFolderPath", "String", parameters.get("inputFolderPath").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("inputFolderPath") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","inputFolderPath"));
    }

    if(parameters.containsKey("sortFolderBy") && parameters.get("sortFolderBy") != null && parameters.get("sortFolderBy").get() != null) {
      convertedParameters.put("sortFolderBy", parameters.get("sortFolderBy").get());
      if(convertedParameters.get("sortFolderBy") !=null && !(convertedParameters.get("sortFolderBy") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","sortFolderBy", "String", parameters.get("sortFolderBy").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("sortFolderBy") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","sortFolderBy"));
    }
    if(convertedParameters.get("sortFolderBy") != null) {
      switch((String)convertedParameters.get("sortFolderBy")) {
        case "bydatedesc" : {

        } break;
        case "bydateasc" : {

        } break;
        case "bynamedesc" : {

        } break;
        case "bynameasc" : {

        } break;
        case "bysizeasc" : {

        } break;
        case "bysizedesc" : {

        } break;
        default : throw new BotCommandException(MESSAGES_GENERIC.getString("generic.InvalidOption","sortFolderBy"));
      }
    }

    try {
      Optional<Value> result =  Optional.ofNullable(command.action((String)convertedParameters.get("inputFolderPath"),(String)convertedParameters.get("sortFolderBy")));
      return logger.traceExit(result);
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","action"));
    }
    catch (BotCommandException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}
