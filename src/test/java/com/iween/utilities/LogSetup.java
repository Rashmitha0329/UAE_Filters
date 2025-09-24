package com.iween.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogSetup {
	  public static void initializeLogFile(String testClassName) {
	        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
	        String logFileName = testClassName + "-" + timestamp;
	        System.setProperty("logFilename", logFileName);
	    }
}
