package com.iween.utilities;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class ReportUtils {

	
	//passing the report path to the  @Aftersuit in base class
    public static String getLatestReportPath(String reportsFolderPath) {
        File reportsDir = new File(reportsFolderPath);

        File[] files = reportsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".html"));
        if (files == null || files.length == 0) {
            return null;
        }

        // Sort files by last modified date descending, so newest first
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        return files[0].getAbsolutePath();
    }
}

