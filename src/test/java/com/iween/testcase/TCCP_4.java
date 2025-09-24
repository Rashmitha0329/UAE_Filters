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
import com.skytraveller.pageObjects.AccountsPages;
import com.skytraveller.pageObjects.BookingPage;
import com.skytraveller.pageObjects.ProfilePage;
import com.skytraveller.pageObjects.ResultPage;
import com.skytraveller.pageObjects.SearchPage;
import com.skytraveller.pageObjects.loginPage;

public class TCCP_4 extends baseClass {

    @Test(dataProvider = "excelData", dataProviderClass = DataProviders.class, retryAnalyzer = Retry.class)
    public void myTest(Map<String, String> excelTestData) throws Exception {

        ExtentTest test = ExtentManager.getTest();  // Get the ExtentTest instance from thread-local
        logger.info("******** Starting TestCase1: testLogin ********");
        
        

        try {
            // Log the data being used
            System.out.println("Running test with: " + excelTestData);
        	test.log(Status.INFO, "Checking Logo in webpage");
        	//Get The Data From Excel
        	   String departFrom = excelTestData.get("Depart From");
               String goingTo = excelTestData.get("Going To");
       		String adultsCounts = excelTestData.get("AdultsCounts");
       		String childCount = excelTestData.get("ChildrenCount");
       		String infantsCount = excelTestData.get("InfantsCount");
       		 String Class = excelTestData.get("Class");
       		 String profileDropDown = excelTestData.get("profileDropDown");
       		 String accountsPageOption = excelTestData.get("accountsPageOption");
       		 String selectPayment = excelTestData.get("selectPayment");
       		 String depositAmount = excelTestData.get("enterDepositAmount");
    		
    		
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
            AccountsPages accountsPages = new AccountsPages(driver);
            
            
            loginPage.validateLoginPageIsDisplayed(test);
          
            // Perform login using values from properties file
            loginPage.UserLogin(p.getProperty("username"), p.getProperty("password"));
            
            
            
            long startTime = System.currentTimeMillis();
            loginPage.clickOnSubmitButton();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Search Flights']")));
            long endTime = System.currentTimeMillis();
    		long loadTimeInSeconds = (endTime - startTime) / 1000;
    		test.log(Status.INFO, "Flight Home page  loaded in " + loadTimeInSeconds + " seconds");
           
    		SearchPage.validateHomePageIsDisplayed(test);
           Thread.sleep(3000);
           
           
           
           long startTime1 = System.currentTimeMillis();
           SearchPage.profileDropDown(profileDropDown,test);
           WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(60));
   	   	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='my-bookings-page']")));
           long endTime1 = System.currentTimeMillis();
   		long loadTimeInSeconds1 = (endTime - startTime) / 1000;
   		test.log(Status.INFO, "Booking  page  loaded in " + loadTimeInSeconds + " seconds");
          
   		accountsPages.accountsPageOption(accountsPageOption,test);
   		accountsPages.balanceUploadPageIsDisplayed(test);
   		accountsPages.selectPayment(selectPayment,test);
   		accountsPages.enterDepositAmount(depositAmount,test);
   		accountsPages.clickOnDepositeSubmitButton(test);
   		
   		accountsPages.validateDepositPopupAndPayMentGateWay(depositAmount,test);
   		
   		
           
        
           
           
   		
Thread.sleep(2000);
   		
            logger.info("******** TestCase1: testLogin completed successfully ********");

        } catch (Exception e) {
            logger.error("Test failed due to: ", e);
            test.fail("Test failed with exception: " + e.getMessage());
            throw e;  // Re-throw to ensure Retry works properly
        }
    }
}
