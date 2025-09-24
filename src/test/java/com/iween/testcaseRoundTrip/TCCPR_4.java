package com.iween.testcaseRoundTrip;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.TimeoutException;
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
import com.iween.utilities.ScreenshotUtil;
import com.skytraveller.pageObjects.AccountsPages;
import com.skytraveller.pageObjects.BookingPage;
import com.skytraveller.pageObjects.BookingPageRoundTrip;
import com.skytraveller.pageObjects.FlightDetails;
import com.skytraveller.pageObjects.FlightDetailsRoundTrip;
import com.skytraveller.pageObjects.ProfilePage;
import com.skytraveller.pageObjects.ResultPage;
import com.skytraveller.pageObjects.ResultPageRoundTrip;
import com.skytraveller.pageObjects.SearchPage;
import com.skytraveller.pageObjects.loginPage;

public class TCCPR_4 extends baseClass {

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
     		
     		  int FlightCardBasedOnIndex = Integer.parseInt(excelTestData.get("FlightCardBasedOnIndex"));
     		  
     		  String modifySearchFromLocation = excelTestData.get("ModifySearchFromLocation");
               String modifySearchToLocation = excelTestData.get("ModifySearchToLocation");
       		String modifySearchAdultsCounts = excelTestData.get("ModifySearchAdultsCounts");
       		String modifySearchChildrenCount = excelTestData.get("ModifySearchChildrenCount");
       		String modifySearchInfantsCount = excelTestData.get("ModifySearchInfantsCount");
       		 String modifyTravelClass = excelTestData.get("ModifyTravelClass");
       		
    		  
       		test.log(Status.INFO, "Depart From: " + departFrom +",Going To: "+ goingTo+",Selected Class: "+Class);
	        test.log(Status.INFO, "AdultsCounts: " + adultsCounts + ", ChildrenCount: " + childCount + ", InfantsCount: " + infantsCount);
      		 
    		
    		
    		 //Method To Get Future Date
            Map<String, Iween_FutureDates.DateResult> dateResults = futureDates.furtherDate();
    		Iween_FutureDates.DateResult date2 = dateResults.get("datePlus2");
    		String fromMonthYear = date2.month + " " + date2.year;
    		Iween_FutureDates.DateResult date5 = dateResults.get("datePlus5");
    		String ReturnMonthYear = date5.month + " " + date5.year;
    		//modify search Date
    		
    		
     		Iween_FutureDates.DateResult date3 = dateResults.get("datePlus3");
     		String modifyfromMonthYear = date3.month + " " + date3.year;
     		Iween_FutureDates.DateResult date_5 = dateResults.get("datePlus5");
     		String modifyReturnMonthYear = date_5.month + " " + date_5.year;
    		
            // Login page object
            loginPage loginPage = new loginPage(driver);
            SearchPage SearchPage = new SearchPage(driver);
            ProfilePage profilePage = new ProfilePage(driver);
            ResultPage resultPage = new ResultPage(driver);
            BookingPage bookingPage = new BookingPage(driver);
            FlightDetails flightDetails = new FlightDetails(driver);
            BookingPageRoundTrip bookingPageRoundTrip = new BookingPageRoundTrip(driver);
            FlightDetailsRoundTrip flightDetailsRoundTrip = new FlightDetailsRoundTrip(driver);
           
            ResultPageRoundTrip resultPageRoundTrip = new ResultPageRoundTrip(driver);
            
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
           
           
              
          
  		SearchPage.ValidateDefaultSecter(test);
           
           //Method to Search The Flight
           SearchPage.searchFightsOnHomePage(departFrom,goingTo,date2.day,fromMonthYear,date5.day,ReturnMonthYear,adultsCounts,childCount,infantsCount,Class);
           
        // Start timing
           long startTime1 = System.currentTimeMillis();

           // Click the Search button
           SearchPage.clickOnSearch(test);

           // Define wait
           WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(60));

           boolean isResultLoaded = false;

           try {
               // Wait for flight result cards (primary indicator)
               wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,'one-way-new-result-card')]")));
               isResultLoaded = true;
           } catch (TimeoutException e) {
               // If primary element not found, optionally use backup check or validation method
               test.log(Status.WARNING, "Flight cards not found within wait time. Trying page validation fallback...");
               isResultLoaded = resultPage.validateResultPage1(test); // This method must return boolean
           }

           // End timing
           long endTime1 = System.currentTimeMillis();
           long loadTimeInSeconds1 = (endTime - startTime) / 1000;

           if (isResultLoaded) {
               test.log(Status.PASS, "✅ Flight search results loaded in " + loadTimeInSeconds + " seconds.");
           } else {
               test.log(Status.FAIL, "❌ Flight search results did not load in time.");
               ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Result Load Timeout", "No results within expected time.");
           }

           String[] userEnteredData =resultPage.userEnteredDetails(test);
           resultPage.selectFlightBasedOnIndexForDetails(FlightCardBasedOnIndex,test);
           Thread.sleep(3000);
           flightDetailsRoundTrip.validateFlightDetailsBasedOnFlightCard(userEnteredData,test); 
           flightDetails.closeNavBar(test);
           
           Thread.sleep(3000);
           
           test.log(Status.INFO, "Modify Search Details");
           test.log(Status.INFO, "Depart From: " + modifySearchFromLocation +",Going To: "+ modifySearchToLocation+",Selected Class: "+modifyTravelClass);
	        test.log(Status.INFO, "AdultsCounts: " + modifySearchAdultsCounts + ", ChildrenCount: " + modifySearchChildrenCount + ", InfantsCount: " + modifySearchInfantsCount);
           
           
           SearchPage.ModifySearch(modifySearchFromLocation,modifySearchToLocation,date3.day,modifyfromMonthYear,date_5.day,modifyReturnMonthYear,modifySearchAdultsCounts,modifySearchChildrenCount,modifySearchInfantsCount,modifyTravelClass);
           Thread.sleep(2000);
     		resultPage.clickOnSearch(test);
     		Thread.sleep(4000);
     	//	resultPage.validateResultPage(test); 
     		Thread.sleep(2000);
     		 String[] userEnteredData1 =resultPage.userEnteredDetails(test);
           
     		 resultPage.selectFlightBasedOnIndexForDetails(FlightCardBasedOnIndex,test);
             Thread.sleep(3000);
             flightDetailsRoundTrip.validateFlightDetailsBasedOnFlightCard(userEnteredData1,test);
   		
            Thread.sleep(2000);
   		
            logger.info("******** TestCase1: testLogin completed successfully ********");

        } catch (Exception e) {
            logger.error("Test failed due to: ", e);
            test.fail("Test failed with exception: " + e.getMessage());
            throw e;  // Re-throw to ensure Retry works properly
        }
    }
}
