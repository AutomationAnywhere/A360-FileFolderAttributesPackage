# A360-FileFolderAttributesPackage
 Custom Automation 360 Package for File and Folder Attributes

## Files and Folder Deep (AllFileInSubDirs)
This action returns a list of all files found within the provided folder as well as any files found in subfolders of the provided path.

## File Attributes (FileAttribues)
Returns a dictionary of file attributes to include name, extension, size, modified/created dates, owner, read only, hidden, etc

## Files in Folder
Returns a list of all files found directly within the provided folder - sorted by name, size, or modified date. Note that this action will only return files in the existing directory, and will not traverse through sub directories like Files in Folder Deep

## Folder Attributes
Returns attributes of a provided folder to include things like size (in bytes), count of subfiles/folders, owner, hidden, etc.

## Folders In Folder
Returns a list of subfolders for the provided folder. Should you wish to traverse through the subdirectories of a folder, this would be an approach to do so.