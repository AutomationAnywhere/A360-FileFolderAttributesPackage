package com.automationanywhere.Supplemental;

import java.io.File;

public class getFilesCount {
    public static int getFilesCount(File file) {
        File[] files = file.listFiles();
        int count = 0;
        for (File f : files)
            if (f.isDirectory())
                count += getFilesCount(f);
            else
                count++;
        return count;
    }

    public static int getFoldersCount(File file) {
        File[] files = file.listFiles();
        int count = 0;
        for (File f : files)
            if (f.isDirectory()){
                count++;
                count += getFoldersCount(f);
            }
        return count;
    }
}
