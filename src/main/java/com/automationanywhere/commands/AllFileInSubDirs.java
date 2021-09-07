package com.automationanywhere.commands;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.FileFolder;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.DataType;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.automationanywhere.commandsdk.model.AttributeType.FILE;
import static com.automationanywhere.commandsdk.model.AttributeType.SELECT;

//BotCommand makes a class eligible for being considered as an action.
@BotCommand

//CommandPks adds required information to be dispalable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "AllFileInSubDirs", label = "[[AllFileInSubDirs.label]]",
        node_label = "[[AllFileInSubDirs.node_label]]", description = "[[AllFileInSubDirs.description]]", icon = "pkg.svg",

        //Return type information. return_type ensures only the right kind of variable is provided on the UI.
        return_label = "[[AllFileInSubDirs.return_label]]", return_type = DataType.LIST, return_required = true)
public class AllFileInSubDirs {
    //Identify the entry point for the action. Returns a Value<String> because the return type is String.
    @Execute
    public Value<List<Value>> action(
            //Input Field for Folder Selection
            @Idx(index = "1", type = FILE)
            //UI labels.
            @Pkg(label = "[[AllFileInSubDirs.inputFolder.label]]")
            @FileFolder
            //Ensure that a validation error is thrown when the value is null.
            @Required
            //Ensure that a validation error is thrown when the value is null.
            @NotEmpty
                    String inputFolderPath) {
        //Create return value
        ListValue<?> result = new ListValue();
        List<Value> resultList = new ArrayList();

        //Business Logic
        try {
            //Walk through all files with no limit on depth
            List<Path> descpaths = Files.walk(Paths.get(inputFolderPath))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
            //Loop through returned List of Paths to add to return list
            for (Path path : descpaths) {
                    resultList.add(new StringValue(path.toString()));
            }

        } catch (Exception e) {
            throw new BotCommandException("Error occured while reading directory. Error code: " + e.toString());
        }

        result.set(resultList);
        return result;
    }
}
