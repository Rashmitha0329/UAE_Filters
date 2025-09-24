package com.iween.utilities;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;

public class ScreenshotUtil {
	
	/*
	 

	// 1. Save screenshot to file (use this if sharing report with screenshots folder)
    public static String captureScreenshot(String testCaseName, WebDriver driver) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);

        String screenshotDir = System.getProperty("user.dir") + "/reports/screenshots/";
        File dir = new File(screenshotDir);
        if (!dir.exists()) dir.mkdirs();

        String relativePath = "screenshots/" + testCaseName + ".png";
        File dest = new File(screenshotDir + testCaseName + ".png");

        try {
            FileUtils.copyFile(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return relativePath;  // Use this in ExtentReports
    }

    // 2. Get screenshot as Base64 string (for embedding directly in report)
    public static String captureScreenshotBase64(WebDriver driver) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        return ts.getScreenshotAs(OutputType.BASE64);
    }

    // 3. Capture and log screenshot directly into ExtentReports
    public static void logScreenshotToReport(WebDriver driver, ExtentTest test, String message) {
        try {
            String base64Screenshot = captureScreenshotBase64(driver);
            test.log(Status.INFO, message,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        } catch (Exception e) {
            test.warning("⚠️ Unable to capture Base64 screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }
*/
	// Capture screenshot and save file, return relative path
    public static String captureScreenshot(String testCaseName, WebDriver driver) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);

        String screenshotDir = System.getProperty("user.dir") + "/reports/screenshots/";
        File dir = new File(screenshotDir);
        if (!dir.exists()) dir.mkdirs();

        String relativePath = "screenshots/" + testCaseName + ".png";
        File dest = new File(screenshotDir + testCaseName + ".png");

        try {
            FileUtils.copyFile(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return relativePath;
    }

    // Capture screenshot as Base64 string for embedding
    public static String captureScreenshotBase64(WebDriver driver) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        return ts.getScreenshotAs(OutputType.BASE64);
    }

    // Log screenshot to ExtentReports with Base64 screenshot
    public static void logScreenshotToReport(WebDriver driver, ExtentTest test, String message) {
        try {
            String base64Screenshot = captureScreenshotBase64(driver);
            test.log(Status.INFO, message,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        } catch (Exception e) {
            test.warning("⚠️ Unable to capture Base64 screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }
   /*
    public static void captureAndAttachScreenshot1(WebDriver driver, ExtentTest test, Status status, String message, String screenshotName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);

            // Folder inside the report directory
            String screenshotFolder = "reports/screenshots/";
            File dir = new File(System.getProperty("user.dir") + "/" + screenshotFolder);
            if (!dir.exists()) dir.mkdirs();

            String fileName = screenshotName + ".png";
            File dest = new File(dir, fileName);
            FileUtils.copyFile(src, dest);

            // Use relative path so report works on any machine
            String relativePath = "screenshots/" + fileName;

            test.log(status, message, MediaEntityBuilder.createScreenCaptureFromPath(relativePath).build());

        } catch (IOException e) {
            test.warning("⚠️ Failed to capture or attach screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }
*/
    /*
    public static void captureAndAttachScreenshot1(WebDriver driver, ExtentTest test, Status status, String message, String screenshotName) {
        try {
            // 1. Take screenshot file and save it
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);

            String screenshotFolder = System.getProperty("user.dir") + "/reports/screenshots/";
            File dir = new File(screenshotFolder);
            if (!dir.exists()) dir.mkdirs();

            File dest = new File(dir, screenshotName + ".png");
            FileUtils.copyFile(src, dest);

            String relativePath = "screenshots/" + screenshotName + ".png";

            // 2. Embed Base64 screenshot for sharing
            String base64 = ts.getScreenshotAs(OutputType.BASE64);

            // 3. Log screenshot in report both ways
            test.log(status, message + " (file saved and embedded)");
            test.log(status, message,
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());

        } catch (IOException e) {
            test.warning("Failed to capture or attach screenshot: " + e.getMessage());
        }
    }
    */
    public static void captureAndAttachScreenshot1(WebDriver driver, ExtentTest test, Status status, String message, String screenshotName) {
        try {
            // 1. Take screenshot file and save it
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);

            String screenshotFolder = System.getProperty("user.dir") + "/reports/screenshots/";
            File dir = new File(screenshotFolder);
            if (!dir.exists()) dir.mkdirs();// if the directories not created then it .Creates the directory and all necessary parent directories (if they don't exist).

            File dest = new File(dir, screenshotName + ".png");
            FileUtils.copyFile(src, dest);

            String relativePath = "screenshots/" + screenshotName + ".png";

            // 2. Embed Base64 screenshot for sharing
            String base64 = ts.getScreenshotAs(OutputType.BASE64);

            // 3. Log screenshot in report both ways
          //  test.log(status, message);
            test.log(status, message,
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());

        } catch (IOException e) {
            test.warning("Failed to capture or attach screenshot: " + e.getMessage());
        }
    }


}
