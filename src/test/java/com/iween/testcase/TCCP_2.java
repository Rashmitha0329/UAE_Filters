package com.iween.testcase;

import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.testBase.baseClass;
import com.iween.utilities.DataProviders;
import com.iween.utilities.ExtentManager;
import com.iween.utilities.Iween_FutureDates;
import com.iween.utilities.Retry;
import com.skytraveller.pageObjects.BookingPage;
import com.skytraveller.pageObjects.ProfilePage;
import com.skytraveller.pageObjects.ResultPage;
import com.skytraveller.pageObjects.SearchPage;
import com.skytraveller.pageObjects.loginPage;

public class TCCP_2 extends baseClass {

    @Test(dataProvider = "excelData", dataProviderClass = DataProviders.class, retryAnalyzer = Retry.class)
    public void myTest(Map<String, String> excelTestData) throws Exception {

        ExtentTest test = ExtentManager.getTest();  // Get the ExtentTest instance from thread-local
        logger.info("******** Starting TestCase1: testLogin ********");
        
        

        try {
            // Log the data being used
            System.out.println("Running test with: " + excelTestData);
        	test.log(Status.INFO, "Checking Footer Pages in webpage");
        	//Get The Data From Excel
        	   String departFrom = excelTestData.get("Depart From");
               String goingTo = excelTestData.get("Going To");
       		String adultsCounts = excelTestData.get("AdultsCounts");
       		String childCount = excelTestData.get("ChildrenCount");
       		String infantsCount = excelTestData.get("InfantsCount");
       		 String Class = excelTestData.get("Class");
       		 String profileDropDown = excelTestData.get("profileDropDown");
    		
    		
    		 //Method To Get Future Date
            Map<String, Iween_FutureDates.DateResult> dateResults = futureDates.furtherDate();
    		Iween_FutureDates.DateResult date2 = dateResults.get("datePlus2");
    		String fromMonthYear = date2.month + " " + date2.year;

            // Login page object
            loginPage loginPage = new loginPage(driver);
            SearchPage SearchPage = new SearchPage(driver);
            ProfilePage profilePage = new ProfilePage(driver);
            ResultPage resultPage = new ResultPage(driver);
            BookingPage bookingPage = new BookingPage(driver);
            
            
            loginPage.validateLoginPageIsDisplayed(test);
            loginPage.footerDetails(test);
            
            loginPage.validateTermsAndCondition(test);
            driver.navigate().back();
            loginPage.validateAboutUs(test);
            driver.navigate().back();
            loginPage.validateContactUs(test);
            
          
            
           
   		
Thread.sleep(2000);
   		
            logger.info("******** TestCase1: testLogin completed successfully ********");

        } catch (Exception e) {
            logger.error("Test failed due to: ", e);
            test.fail("Test failed with exception: " + e.getMessage());
            throw e;  // Re-throw to ensure Retry works properly
        }
    }
}
