package com.automationanywhere.commands;

import com.automationanywhere.Supplemental.WindowsExplorerStringComparator;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.BooleanValue;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.NumberValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.FileFolder;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.DataType;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;

import static com.automationanywhere.Supplemental.getFilesCount.getFilesCount;
import static com.automationanywhere.Supplemental.getFilesCount.getFoldersCount;
import static com.automationanywhere.commandsdk.model.AttributeType.FILE;

//BotCommand makes a class eligible for being considered as an action.
@BotCommand

//CommandPks adds required information to be dispalable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "FolderAttributes", label = "[[FolderAttributes.label]]",
        node_label = "[[FolderAttributes.node_label]]", description = "[[FolderAttributes.description]]", icon = "pkg.svg",

        //Return type information. return_type ensures only the right kind of variable is provided on the UI.
        return_label = "[[FolderAttributes.return_label]]", return_type = DataType.DICTIONARY, return_required = true)
public class FolderAttributes {
    //Identify the entry point for the action. Returns a Value<String> because the return type is String.
    @Execute
    public DictionaryValue action(
            //Input Field for Folder Selection
            @Idx(index = "1", type = FILE)
            //UI labels.
            @Pkg(label = "[[FolderAttributes.inputFolder.label]]")
            //Ensure that a validation error is thrown when the value is null.
            @FileFolder
            @Required
            @NotEmpty
                    String inputFolder) {

//        //Create return value

        Map<String, Value> folderAttributes = new HashMap<>();

        //Business Logic
        Double fileSize = 0.0;
        Path pathofFile;
        File folder = new File(inputFolder);
        try {
            //Get total size of folder
            folderAttributes.put("size", new NumberValue(FileUtils.sizeOfDirectory(folder)));
            //Get count of files
            folderAttributes.put("totalFileCount", new NumberValue(getFilesCount(folder)));
            folderAttributes.put("totalFoldersCount", new NumberValue(getFoldersCount(folder)));
            folderAttributes.put("immediateFileCount", new NumberValue(folder.listFiles().length));
            folderAttributes.put("owner", new StringValue(Files.getOwner(folder.toPath()).toString()));
            //Check if folder is read only
            if (!folder.canWrite()) {
                folderAttributes.put("readOnly", new BooleanValue(Boolean.TRUE));
            } else {
                folderAttributes.put("readOnly", new BooleanValue(Boolean.FALSE));
            }
            //Check if folder is hidden
            if (folder.isHidden()) {
                folderAttributes.put("hidden", new BooleanValue(Boolean.TRUE));
            } else {
                folderAttributes.put("hidden", new BooleanValue(Boolean.FALSE));
            }

            //Get count of immediate sub-folders (meaning not including subfolders of subfolders)
            //set file directories
            File directory = new File(inputFolder);
            File[] files = directory.listFiles();
            List<String> strList = new ArrayList<String>();
            int folderCount = 0;
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
                    folderCount++;
                }
            }
            folderAttributes.put("immediateFolderCount", new NumberValue(folderCount));
        } catch (Exception e) {
            throw new BotCommandException("Error occured while reading file properties. Error code: " + e.toString());
        }
        return new DictionaryValue(folderAttributes);
    }

}

