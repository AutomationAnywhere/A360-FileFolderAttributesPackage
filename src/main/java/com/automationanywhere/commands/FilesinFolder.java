package com.automationanywhere.commands;

import com.automationanywhere.Supplemental.WindowsExplorerStringComparator;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.FileExtension;
import com.automationanywhere.commandsdk.annotations.rules.FileFolder;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
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

import static com.automationanywhere.commandsdk.model.AttributeType.*;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

//BotCommand makes a class eligible for being considered as an action.
@BotCommand

//CommandPks adds required information to be dispalable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "FilesinFolder", label = "[[FilesinFolder.label]]",
        node_label = "[[FilesinFolder.node_label]]", description = "[[FilesinFolder.description]]", icon = "pkg.svg",

        //Return type information. return_type ensures only the right kind of variable is provided on the UI.
        return_label = "[[FilesinFolder.return_label]]", return_type = DataType.LIST, return_required = true)
public class FilesinFolder {
    //Identify the entry point for the action. Returns a Value<String> because the return type is String.
    @Execute
    public Value<List<Value>> action(
            //Input Field for Folder Selection
            @Idx(index = "1", type = FILE)
            //UI labels.
            @Pkg(label = "[[FilesinFolder.inputFolder.label]]")
            @FileFolder
            @Required
            //Ensure that a validation error is thrown when the value is null.
            @NotEmpty
                    String inputFolderPath,
            //Select Dropdown for Sort Type
            @Idx(index = "2", type = SELECT, options = {
                    @Idx.Option(index = "2.1", pkg = @Pkg(label = "By Date - Desc", value = "bydatedesc")),
                    @Idx.Option(index = "2.2", pkg = @Pkg(label = "By Date - Asc", value = "bydateasc")),
                    @Idx.Option(index = "2.3", pkg = @Pkg(label = "By Name - Desc", value = "bynamedesc")),
                    @Idx.Option(index = "2.4", pkg = @Pkg(label = "By Name - Asc", value = "bynameasc")),
                    @Idx.Option(index = "2.5", pkg = @Pkg(label = "By Size - Asc", value = "bysizeasc")),
                    @Idx.Option(index = "2.6", pkg = @Pkg(label = "By Size - Desc", value = "bysizedesc"))
            })
            @NotEmpty
            @Pkg(label = "[[FilesinFolder.sortType.label]]")
                    String sortFolderBy) {
        //Create return value
        ListValue<?> result = new ListValue();
        List<Value> resultList = new ArrayList();

        //Business Logic
        try {
            //set file directories
            File directory = new File(inputFolderPath);
            File[] files = directory.listFiles();
            List<String> strList = new ArrayList<String>();

            switch (sortFolderBy) {
                case "bydateasc":
                    Arrays.sort(files, Comparator.comparingLong(File::lastModified));
                    for (File file : files) {
                        //Only capture file results, not folders
                        if (!file.isDirectory()) {
                            resultList.add(new StringValue(file.toString()));
                        }
                    }
                    break;
                case "bydatedesc":
                    Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
                    for (File file : files) {
                        //Only capture file results, not folders
                        if (!file.isDirectory()) {
                            resultList.add(new StringValue(file.toString()));
                        }
                    }
                    break;
                case "bynameasc":
                    for (File file : files) {
                        strList.add(file.toString());
                    }
                    String[] ascArray = new String[strList.size()];
                    strList.toArray(ascArray);
                    Arrays.sort(ascArray, new WindowsExplorerStringComparator());
                    for (String file : ascArray) {
                        File checkFile = new File(file);
                        //Only capture file results, not folders
                        if (!checkFile.isDirectory()) {
                            resultList.add(new StringValue(checkFile.toString()));
                        }
                    }
                    break;
                case "bynamedesc":
                    for (File file : files) {
                        strList.add(file.toString());
                    }
                    String[] descArray = new String[strList.size()];
                    strList.toArray(descArray);
                    Arrays.sort(descArray, new WindowsExplorerStringComparator());
                    for (int counter = descArray.length - 1; counter >= 0; counter--) {
                        File checkFile = new File(descArray[counter].toString());
                        //Only capture file results, not folders
                        if (!checkFile.isDirectory()) {
                            resultList.add(new StringValue(checkFile.toString()));
                        }
                    }
                    break;
                case "bysizeasc":
                    List<Path> ascpaths = Files.walk(Paths.get(inputFolderPath), 1)
                            .filter(Files::isRegularFile)
                            .sorted((Path a, Path b) -> a.toFile().length() > b.toFile().length() ? 1 : -1)
                            .collect(Collectors.toList());
                    for (Path file : ascpaths) {
                        File checkFile = new File(file.toString());
                        //Only capture file results, not folders
                        if (!checkFile.isDirectory()) {
                            resultList.add(new StringValue(checkFile.toString()));
                        }
                    }
                    break;
                case "bysizedesc":
                    List<Path> descpaths = Files.walk(Paths.get(inputFolderPath), 1)
                            .filter(Files::isRegularFile)
                            .sorted((Path a, Path b) -> a.toFile().length() < b.toFile().length() ? 1 : -1)
                            .collect(Collectors.toList());
                    for (Path file : descpaths) {
                        File checkFile = new File(file.toString());
                        //Only capture file results, not folders
                        if (!checkFile.isDirectory()) {
                            resultList.add(new StringValue(checkFile.toString()));
                        }
                    }
                    break;
            }

        } catch (Exception e) {
            throw new BotCommandException("Error occured while reading directory. Error code: " + e.toString());
        }

        result.set(resultList);
        return result;
    }
}
