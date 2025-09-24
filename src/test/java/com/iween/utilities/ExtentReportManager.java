package com.iween.utilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.iween.testBase.baseClass;

public class ExtentReportManager extends baseClass implements ITestListener {

	 private static ExtentReports extent = ExtentManager.getExtentReports();

//	    @Override
//	    public void onTestStart(ITestResult result) {
//	        ExtentTest test = extent.createTest(result.getTestClass().getRealClass().getSimpleName());
//	        ExtentManager.setTest(test);
//	        
//	    }
	 @Override
	 public void onTestStart(ITestResult result) {
	     ExtentTest test = extent.createTest(result.getTestClass().getRealClass().getSimpleName());
	     ExtentManager.setTest(test);

	     ITestContext context = result.getTestContext();

	     String os = context.getCurrentXmlTest().getParameter("os");
	     String browser = context.getCurrentXmlTest().getParameter("browser");

	     // Set dynamic OS and Browser info in ExtentReports
	     ExtentManager.setSystemInfo("Operating System", os != null ? os : "Not Specified");
	     ExtentManager.setSystemInfo("Browser", browser != null ? browser : "Not Specified");
	 }


	    @Override
	    public void onTestSuccess(ITestResult result) {
	        ExtentManager.getTest().log(Status.PASS, "Test Passed");
	    }

	    @Override
	    public void onTestFailure(ITestResult result) {
	        ExtentTest test = ExtentManager.getTest();
	        test.fail(result.getThrowable());
	        try {
	            WebDriver driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
	         // ScreenshotUtil.logScreenshotToReport(driver, test, "Failure Screenshot");
	          
	        } catch (Exception e) {
	            test.warning("Failed to capture screenshot: " + e.getMessage());
	        }
	    }

	    @Override
	    public void onTestSkipped(ITestResult result) {
	        ExtentManager.getTest().log(Status.SKIP, "Test Skipped");
	    }

	    @Override
	    public void onFinish(ITestContext context) {
	        extent.flush();
	    }
	/*
	 ExtentReports extent = ExtentManager.getExtentReports();
	    ExtentTest test;
ThreadLocal <ExtentTest>extentTest = new ThreadLocal();
	    @Override
	    public void onTestStart(ITestResult result) {
	        test = extent.createTest(result.getTestClass().getRealClass().getSimpleName());
	        extentTest.set(test);//unique thread id and create one map of test method
	    }

	    @Override
	    public void onTestSuccess(ITestResult result) {
	    	extentTest.get().log(Status.PASS, "Test Passed");
	    }

	    @Override
	    public void onTestFailure(ITestResult result) {
	    	 extentTest.get().fail(result.getThrowable());

	         WebDriver driver = null;
	         try {
	             driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
	         } catch (Exception e) {
	             e.printStackTrace();
	         }

	         if (driver != null) {
	             try {
	                 // Use the ThreadLocal test here
	                 ScreenshotUtil.logScreenshotToReport(driver, extentTest.get(), "Test Failed - Screenshot");
	             } catch (Exception e) {
	                 e.printStackTrace();
	             }
	         } else {
	             extentTest.get().warning("Driver instance not found, screenshot skipped.");
	         }
	    	 */
	    	 /*
	         WebDriver driver = null;
	         try {
	             driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
	         } catch (Exception e) {
	             e.printStackTrace();
	         }

	         if (driver != null) {
	             // Log Base64 screenshot embedded into report
	             ScreenshotUtil.logScreenshotToReport(driver, extentTest.get(), "Screenshot on failure");
	         } else {
	             extentTest.get().warning("Driver instance not found, screenshot skipped.");
	         }
	         */
	         
	         
	        // OR, use this if you're using file path + zipped reports folder:
	        // String screenshotPath = ScreenshotUtil.captureScreenshot(result.getMethod().getMethodName(), driver);
	        // test.addScreenCaptureFromPath(screenshotPath);
	        /*
	        String filePath=null;
			try {
				filePath = getScreenshot(result.getMethod().getMethodName(),driver);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				extentTest.get().info("Failed to attach screenshot");
				e.printStackTrace();
			}
			extentTest.get().addScreenCaptureFromPath(filePath, result.getMethod().getMethodName());
	       */
	 //   }

	  /*  
	    @Override
	    public void onTestSkipped(ITestResult result) {
	        test = extent.createTest(result.getMethod().getMethodName());
	        test.log(Status.SKIP, result.getMethod().getMethodName() + " was skipped.");
	        test.skip(result.getThrowable());
	    }

	    @Override
	    public void onFinish(ITestContext context) {
	        extent.flush();
	    }
	*/
	
	/*
    private ExtentSparkReporter sparkReporter;
    private ExtentReports extent;
    private ExtentTest test;
    private String reportName;

    @Override
    public void onStart(ITestContext context) {
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        reportName = "TEST-REPORT-" + timestamp + ".html";

        sparkReporter = new ExtentSparkReporter("./reports/" + reportName);
        sparkReporter.config().setDocumentTitle("TripGain Automation Report");
        sparkReporter.config().setReportName("End-to-End Test Execution");
        sparkReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Application", "TripGain");
        extent.setSystemInfo("Module", "One Way");
        extent.setSystemInfo("SubModule", "Flights");
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("Environment", "QA");

        String os = context.getCurrentXmlTest().getParameter("os");
        extent.setSystemInfo("Operating System", os != null ? os : "Not Specified");

        String browser = context.getCurrentXmlTest().getParameter("browser");
        extent.setSystemInfo("Browser", browser != null ? browser : "Not Specified");

        List<String> groups = context.getCurrentXmlTest().getIncludedGroups();
        if (!groups.isEmpty()) {
            extent.setSystemInfo("Groups", groups.toString());
        }
    }
    @Override
    public void onTestStart(ITestResult result) {
        // Get the class name (e.g., TCH_01_VerifySearchFunctionalityForHotalForTwoDays)
        String className = result.getTestClass().getRealClass().getSimpleName();
        String testName = className;

        

        // Create the test in Extent Report
        test = extent.createTest(testName);
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.INFO, "Test started: " + testName);

        // Store the test instance for later use
        ExtentManager.setTest(test);
    }

  
    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, result.getMethod().getMethodName() + " passed successfully.");
        captureAndAttachScreenshot(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.log(Status.FAIL, result.getMethod().getMethodName() + " failed.");
        test.fail(result.getThrowable());
        captureAndAttachScreenshot(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.SKIP, result.getMethod().getMethodName() + " was skipped.");
        test.skip(result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }

    private void captureAndAttachScreenshot(ITestResult result) {
        Object currentClass = result.getInstance();

        if (currentClass instanceof baseClass) {
            WebDriver driver = ((baseClass) currentClass).driver;
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getMethod().getMethodName());
            if (screenshotPath != null) {
                try {
                    test.addScreenCaptureFromPath(screenshotPath);
                } catch (Exception e) {
                    test.info("Screenshot could not be attached due to exception.");
                }
            } else {
                test.info("Screenshot path is null, capture may have failed.");
            }
        }
    }
    */
}
