package com.iween.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;

public class Retry implements IRetryAnalyzer {

    private int retryCount = 0;
   // private static final int maxRetryCount = 2;  // Retry 2 times
    private static final int maxRetryCount = 0;  // Retry 2 times
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            
            // ✅ Log to Extent Report
            try {
                ExtentManager.getTest().log(
                    Status.WARNING,
                    "Retrying test '" + result.getName() + "' (" + retryCount + "/" + maxRetryCount + ")"
                );
            } catch (Exception e) {
                System.out.println("Could not log retry to ExtentReport: " + e.getMessage());
            }
            return true;  // Retry the test
        }
        return false; // Don't retry anymore
    }
}



