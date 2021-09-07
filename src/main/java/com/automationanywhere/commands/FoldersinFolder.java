package com.automationanywhere.commands;

import com.automationanywhere.Supplemental.WindowsExplorerStringComparator;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.automationanywhere.commandsdk.model.AttributeType.FILE;
import static com.automationanywhere.commandsdk.model.AttributeType.SELECT;

//BotCommand makes a class eligible for being considered as an action.
@BotCommand

//CommandPks adds required information to be dispalable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "FoldersinFolder", label = "[[FoldersinFolder.label]]",
        node_label = "[[FoldersinFolder.node_label]]", description = "[[FoldersinFolder.description]]", icon = "pkg.svg",

        //Return type information. return_type ensures only the right kind of variable is provided on the UI.
        return_label = "[[FoldersinFolder.return_label]]", return_type = DataType.LIST, return_required = true)
public class FoldersinFolder {
    //Identify the entry point for the action. Returns a Value<String> because the return type is String.
    @Execute
    public Value<List<Value>> action(
            //Input Field for Folder Selection
            @Idx(index = "1", type = FILE)
            //UI labels.
            @Pkg(label = "[[FoldersinFolder.inputFolder.label]]")
            @FileFolder
            @Required
            //Ensure that a validation error is thrown when the value is null.
            @NotEmpty
                    String inputFolderPath) {
        //Create return value
        ListValue<?> result = new ListValue();
        List<Value> resultList = new ArrayList();

        //Business Logic
        try {
            //set file directories
            File directory = new File(inputFolderPath);
            File[] files = directory.listFiles();
            List<String> strList = new ArrayList<String>();

            //Loop through collection of files and add to list
            for (File file : files) {
                strList.add(file.toString());
            }
            //Add to String Array to for sorting
            String[] ascArray = new String[strList.size()];
            strList.toArray(ascArray);
            //Sort by windows explorer naming
            Arrays.sort(ascArray, new WindowsExplorerStringComparator());
            //Loop through sorted array to add to list
            for (String file : ascArray) {
                File checkFile = new File(file);
                //Only capture file results, not folders
                if (checkFile.isDirectory()) {
                    resultList.add(new StringValue(checkFile.toString()));
                }
            }

        } catch (Exception e) {
            throw new BotCommandException("Error occured while reading directory. Error code: " + e.toString());
        }

        result.set(resultList);
        return result;
    }
}
