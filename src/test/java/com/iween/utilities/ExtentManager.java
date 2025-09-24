//package com.iween.utilities;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import com.aventstack.extentreports.ExtentReports;
//import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.reporter.ExtentSparkReporter;
//import com.aventstack.extentreports.reporter.configuration.Theme;
//
//public class ExtentManager {
//
//	private static ExtentReports extent;
//    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
//
//    public static ExtentReports getExtentReports() {
//        if (extent == null) {
//            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
//            String reportName = "TEST-REPORT-" + timestamp + ".html";
//            String reportPath = System.getProperty("user.dir") + "/reports/" + reportName;
//
//            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
//            spark.config().setDocumentTitle("Automation Test Report");
//            spark.config().setReportName("Test Execution Report");
//            spark.config().setTheme(Theme.STANDARD);
//
//            extent = new ExtentReports();
//            extent.attachReporter(spark);
//            extent.setSystemInfo("OS", System.getProperty("os.name"));
//            extent.setSystemInfo("Tester", "Tarun");
//            extent.setSystemInfo("User", System.getProperty("user.name"));
//            extent.setSystemInfo("Environment", "QA");
//            
//            
//        }
//        return extent;
//    }
//
//    public static synchronized void setTest(ExtentTest test) {
//        extentTest.set(test);
//    }
//
//    public static synchronized ExtentTest getTest() {
//        return extentTest.get();
//    }
	/*
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentReports getExtentReports() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String reportName = "TEST-REPORT-" + timestamp + ".html";
            String reportPath = System.getProperty("user.dir") + "/reports/" + reportName;

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setDocumentTitle("Automation Test Report");
            spark.config().setReportName("Test Execution Report");
            spark.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Tester", "Tarun");
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Environment", "QA");
        }
        return extent;
    }
*/
    /*
    public static synchronized void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    public static synchronized ExtentTest getTest() {
        return test.get();
    }
    */
	/*
    public synchronized static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    public synchronized static ExtentTest getTest() {
        return test.get();
    }
    */
//}
package com.iween.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.ExtentTest;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static ExtentReports getExtentReports() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String reportName = "TEST-REPORT-" + timestamp + ".html";
            String reportPath = System.getProperty("user.dir") + "/reports/" + reportName;

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setDocumentTitle("Automation Test Report");
            spark.config().setReportName("Test Execution Report");
            spark.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            // Remove OS from system property here; set externally instead
            // extent.setSystemInfo("OS", System.getProperty("os.name")); 


extent.setSystemInfo("Tester", "Tarun");
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Environment", "QA");
        }
        return extent;
    }

    public static void setSystemInfo(String key, String value) {
        if (extent != null && value != null && !value.isEmpty()) {
            extent.setSystemInfo(key, value);
        }
    }

    public static synchronized void setTest(ExtentTest test) {
        extentTest.set(test);
    }

    public static synchronized ExtentTest getTest() {
        return extentTest.get();
    }
}