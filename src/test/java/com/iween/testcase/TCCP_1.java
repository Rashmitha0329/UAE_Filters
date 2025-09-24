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

public class TCCP_1 extends baseClass {

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
    		 String Class = excelTestData.get("TravelClass");
    		 String profileDropDown = excelTestData.get("profileDropDown");
    		 String ImagePath = excelTestData.get("ImagePath");
    		 int flightCardBasedOnIndex = Integer.parseInt(excelTestData.get("FlightCardBasedOnIndex"));
    		
    		
    		 //Method To Get Future Date
            Map<String, Iween_FutureDates.DateResult> dateResults = futureDates.furtherDate();
    		Iween_FutureDates.DateResult date2 = dateResults.get("datePlus2");
    		String fromMonthYear = date2.month + " " + date2.year;
    		Iween_FutureDates.DateResult date4 = dateResults.get("datePlus4");
    		String returnMonthYear = date4.month + " " + date4.year;
            // Login page object
            loginPage loginPage = new loginPage(driver);
            SearchPage SearchPage = new SearchPage(driver);
            ProfilePage profilePage = new ProfilePage(driver);
            ResultPage resultPage = new ResultPage(driver);
            BookingPage bookingPage = new BookingPage(driver);
            
            loginPage.validateLogoInLoginPage(test);
            
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
           SearchPage.profileDropDown(profileDropDown,test);
           Thread.sleep(3000);
           profilePage.chooseLogoImage(ImagePath,test);
           Thread.sleep(3000);
           profilePage.uploadLogo(test);
           Thread.sleep(3000);
           
           Dimension logoSize  = SearchPage.validateLogo();
           
           driver.navigate().refresh();
           driver.navigate().back();
           
        
           String imageXpath = "//img[contains(@src, 'ST33341.gif') and contains(@class, 'logo-width')]";
          int expectedWidth = logoSize.getWidth();
          int expectedHeight = logoSize.getHeight();
          Thread.sleep(3000);
          SearchPage.validateLogoInHomePage(driver,imageXpath,expectedWidth,expectedHeight,test);
           
          Thread.sleep(3000);
          SearchPage.validateHomePageIsDisplayed(test);
          Thread.sleep(3000);
  		SearchPage.ValidateDefaultSecter(test);
  		 Thread.sleep(3000);
           //Method to Search The Flight
           SearchPage.searchFightsOnHomePage(departFrom,goingTo,date2.day,fromMonthYear,adultsCounts,childCount,infantsCount,Class);
           
           
           long startTime1 = System.currentTimeMillis();
           SearchPage.clickOnSearch(test);
           WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(60));
   		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,'one-way-new-result-card')]")));
           long endTime2 = System.currentTimeMillis();
   		long loadTimeInSeconds2 = (endTime - startTime) / 1000;
   		test.log(Status.INFO, "Flight search results loaded in " + loadTimeInSeconds2 + " seconds");
           
   	 Thread.sleep(3000);
   		resultPage.validateLogoInResultPage(driver, imageXpath, expectedWidth, expectedHeight, test);
   		
   	 Thread.sleep(3000);
   		resultPage.validateResultPage(test);
   		
   		long startTime2 = System.currentTimeMillis();
   		resultPage.selectFlightBasedOnIndex(flightCardBasedOnIndex,test);
        WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='flight-booking-page_flight-details']")));
        long endTime3 = System.currentTimeMillis();
		long loadTimeInSeconds3 = (endTime - startTime) / 1000;
		test.log(Status.INFO, "Flight search booking loaded in " + loadTimeInSeconds2 + " seconds");
   		
   		
		bookingPage.validateLogoInBookingPage(driver, imageXpath, expectedWidth, expectedHeight, test);
   		
   		

   		
            logger.info("******** TestCase1: testLogin completed successfully ********");

        } catch (Exception e) {
            logger.error("Test failed due to: ", e);
            test.fail("Test failed with exception: " + e.getMessage());
            throw e;  // Re-throw to ensure Retry works properly
        }
    }
}
