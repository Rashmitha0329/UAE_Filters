package com.iween.utilities;

import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;
public class Log {
	
	/*
	private ExtentTest test;

    public Log() {
        this.test = ExtentManager.getTest();  // Get the current test instance
    }

    public void ReportEvent(String status, String message) {
        if (test == null) {
            System.out.println("[⚠️ TEST NULL] " + message);
            return;
        }

        switch (status.toUpperCase()) {
            case "PASS":
                test.pass(message);
                break;
            case "FAIL":
                test.fail(message);
                break;
            case "INFO":
                test.info(message);
                break;
            default:
                test.info("[UNKNOWN STATUS] " + message);
        }

        System.out.println("[" + status + "] " + message);
    }
*/
}

	/*
	 private WebDriver driver;
	    private ExtentTest test;

	    public Log(WebDriver driver) {
	        this.driver = driver;
	        this.test = ExtentManager.getTest();  // gets thread-safe ExtentTest
	    }
	   


	    public void ReportEvent(String status, String message) {
	        switch (status.toUpperCase()) {
	            case "PASS":
	                test.pass(message);
	                break;
	            case "FAIL":
	                test.fail(message);
	                break;
	            case "INFO":
	                test.info(message);
	                break;
	            default:
	                test.info("[UNKNOWN STATUS] " + message);
	        }
	        System.out.println("[" + status + "] " + message);
	    }
}
*/
	/*
	 private WebDriver driver;

	    public Log(WebDriver driver) {
	        this.driver = driver;
	    }

	    private ExtentTest getTest() {
	        return ExtentManager.getTest();
	    }

	    public void ReportEvent(String status, String message) {
	        ExtentTest test = getTest();
	        if (test == null) {
	            System.err.println("ExtentTest is NULL! Log message not recorded in report.");
	            return;
	        }

	        switch (status.toUpperCase()) {
	            case "PASS": test.pass(message); break;
	            case "FAIL": test.fail(message); break;
	            case "INFO": test.info(message); break;
	            default: test.info("[UNKNOWN STATUS] " + message); break;
	        }
	        System.out.println("[" + status + "] " + message);
	    }
}
*/
/*
public class Log {

    private WebDriver driver;
    private ExtentTest test;

    public Log(WebDriver driver) {
        this.driver = driver;
        this.test = ExtentManager.getTest();
    }


    public void ReportEvent(String status, String message) {
        switch (status.toUpperCase()) {
            case "PASS":
                test.pass(message);
                break;
            case "FAIL":
                test.fail(message);
                break;
            case "INFO":
                test.info(message);
                break;
            default:
                test.info("[UNKNOWN STATUS] " + message);
        }
        System.out.println("[" + status + "] " + message);
    }
}
*/