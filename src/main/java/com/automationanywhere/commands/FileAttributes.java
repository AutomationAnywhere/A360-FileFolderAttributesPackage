package com.automationanywhere.commands;

import com.automationanywhere.Supplemental.WindowsExplorerStringComparator;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.*;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.FileFolder;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.automationanywhere.commandsdk.model.AttributeType.*;

//BotCommand makes a class eligible for being considered as an action.
@BotCommand

//CommandPks adds required information to be dispalable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "FileAttributes", label = "[[FileAttributes.label]]",
        node_label = "[[FileAttributes.node_label]]", description = "[[FileAttributes.description]]", icon = "pkg.svg",

        //Return type information. return_type ensures only the right kind of variable is provided on the UI.
        return_label = "[[FileAttributes.return_label]]", return_type = DataType.DICTIONARY, return_required = true)
public class FileAttributes {
    //Identify the entry point for the action. Returns a Value<String> because the return type is String.
    @Execute
    public DictionaryValue action(
            //Input Field for Folder Selection
            @Idx(index = "1", type = FILE)
            //UI labels.
            @Pkg(label = "[[FileAttributes.inputFile.label]]")
            //Ensure that a validation error is thrown when the value is null.
            @Required
            @NotEmpty
                    String inputFile) {

//        //Create return value

        Map<String, Value> fileAttributes = new HashMap<>();

        //Business Logic
        Double fileSize = 0.0;
        Path pathofFile;
        File file = new File(inputFile);
        try {
            String fileExtension = FilenameUtils.getExtension(inputFile);
            pathofFile = Paths.get(inputFile);
            BasicFileAttributes attributes = Files.readAttributes(pathofFile, BasicFileAttributes.class);
            //File Name without Extension
            fileAttributes.put("FileName", new StringValue(FilenameUtils.removeExtension(file.getName())));
            fileAttributes.put("ParentDirectory", new StringValue(file.getParent()));
            fileAttributes.put("Size", new NumberValue(Double.valueOf(attributes.size())));
            fileAttributes.put("LastModifiedDate", new StringValue(attributes.lastModifiedTime().toString()));
            fileAttributes.put("LastAccessed", new StringValue(attributes.lastAccessTime().toString()));
            fileAttributes.put("CreatedDate", new StringValue(attributes.creationTime().toString()));
            fileAttributes.put("Extension", new StringValue(FilenameUtils.getExtension(inputFile)));
            fileAttributes.put("Owner", new StringValue(Files.getOwner(file.toPath()).toString()));
            //Check if file is readOnly
            if (!file.canWrite()) {
                fileAttributes.put("readOnly", new BooleanValue(Boolean.TRUE));
            } else {
                fileAttributes.put("readOnly", new BooleanValue(Boolean.FALSE));
            }
            //Check if file is hidden
            if (file.isHidden()) {
                fileAttributes.put("hidden", new BooleanValue(Boolean.TRUE));
            } else {
                fileAttributes.put("hidden", new BooleanValue(Boolean.FALSE));
            }
            //get properties of PDF file
            if(fileExtension.equals("pdf")) {
                try{
                    PDDocument pdf = PDDocument.load(new File(inputFile));
                    PDPage doc = pdf.getPage(0);

                    //Get width and convert from pt to mm
                    double width = doc.getMediaBox().getWidth();
                    width = width * 25.4 / 72;
                    int roundedWidth = (int)Math.round(width);
                    fileAttributes.put("WidthInMM", new NumberValue(roundedWidth));

                    //Get height and convert from pt to mm
                    double height = doc.getMediaBox().getHeight();
                    height = height * 25.4 / 72;
                    int roundedHeight = (int)Math.round(height);
                    fileAttributes.put("HeightInMM", new NumberValue(roundedHeight));

                    //get num pages
                    int numPages = pdf.getNumberOfPages();
                    fileAttributes.put("NumPages", new NumberValue(numPages));
                    pdf.close();
                } catch (Exception e) {
                    //Do nothing - this PDF properties is extra
                }

            }

            //get properties of image files
            if(fileExtension.equals("jpg")||fileExtension.equals("jpeg")||fileExtension.equals("png")||fileExtension.equals("gif")){
                try{
                    //get height
                    File imageFile = new File(inputFile);
                    Image image = ImageIO.read(imageFile);
                    int height = image.getHeight(null);
                    fileAttributes.put("HeightInPixels", new NumberValue(height));

                    //get width
                    int width = image.getWidth(null);
                    fileAttributes.put("WidthInPixels", new NumberValue(width));

                } catch (Exception e) {
                    //do nothing, image properties is extra
                }

            }
        } catch (Exception e) {
            throw new BotCommandException("Error occured while reading file properties. Error code: " + e.toString());
        }
        return new DictionaryValue(fileAttributes);
    }
}
