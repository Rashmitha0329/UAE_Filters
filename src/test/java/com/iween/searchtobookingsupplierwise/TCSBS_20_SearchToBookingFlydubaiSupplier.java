package com.iween.searchtobookingsupplierwise;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
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
import com.skytraveller.pageObjects.FlightDetails;
import com.skytraveller.pageObjects.PaymentGateWayPage;
import com.skytraveller.pageObjects.ProfilePage;
import com.skytraveller.pageObjects.ResultPage;
import com.skytraveller.pageObjects.SearchPage;
import com.skytraveller.pageObjects.loginPage;

public class TCSBS_20_SearchToBookingFlydubaiSupplier extends baseClass {

    @Test(dataProvider = "excelData", dataProviderClass = DataProviders.class, retryAnalyzer = Retry.class)
    public void myTest(Map<String, String> excelTestData) throws Exception {

        ExtentTest test = ExtentManager.getTest();  // Get the ExtentTest instance from thread-local
        logger.info("******** Starting TestCase1: testLogin ********");
        
        

        try {
        	   // Log the data being used
            System.out.println("Running test with: " + excelTestData);
        	test.log(Status.INFO, "Search To Booking Supplier Wise");
        	//Get The Data From Excel
            String departFrom = excelTestData.get("Depart From");
            String goingTo = excelTestData.get("Going To");
    		String adultsCounts = excelTestData.get("AdultsCounts");
    		String childCount = excelTestData.get("ChildrenCount");
    		String infantsCount = excelTestData.get("InfantsCount");
    		 String Class = excelTestData.get("TravelClass");
    		 String Supplier = excelTestData.get("Suppiler");
    		
    		  int FlightCardBasedOnIndex = Integer.parseInt(excelTestData.get("FlightCardBasedOnIndex"));
    		 
    		  test.log(Status.INFO, "Depart From: " + departFrom +",Going To: "+ goingTo+",Selected Class: "+Class+",Supplier :"+Supplier);
  	        test.log(Status.INFO, "AdultsCounts: " + adultsCounts + ", ChildrenCount: " + childCount + ", InfantsCount: " + infantsCount);
        		 
    		
    		 //Method To Get Future Date
            Map<String, Iween_FutureDates.DateResult> dateResults = futureDates.furtherDate();
    		Iween_FutureDates.DateResult date4 = dateResults.get("datePlus4");
    		String fromMonthYear = date4.month + " " + date4.year;
    		Iween_FutureDates.DateResult date8 = dateResults.get("datePlus8");
    		String modifyfromMonthYear = date8.month + " " + date8.year;
    		
            // Login page object
            loginPage loginPage = new loginPage(driver);
            SearchPage SearchPage = new SearchPage(driver);
            ProfilePage profilePage = new ProfilePage(driver);
            ResultPage resultPage = new ResultPage(driver);
            BookingPage bookingPage = new BookingPage(driver);
            FlightDetails flightDetails = new FlightDetails(driver);
            PaymentGateWayPage paymentGateWayPage = new PaymentGateWayPage(driver);
            
         //   loginPage.validateLogoInLoginPage(test);
            
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
           
    	//	SearchPage.validateHomePageIsDisplayed(test);
           
           
              
          SearchPage.validateHomePageIsDisplayed(test);
  	//	SearchPage.ValidateDefaultSecter(test);
           
           //Method to Search The Flight
           SearchPage.searchFightsOnHomePage(departFrom,goingTo,date4.day,fromMonthYear,adultsCounts,childCount,infantsCount,Class);
           
           /*
           
           long startTime1 = System.currentTimeMillis();
           SearchPage.clickOnSearch(test);
           WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(60));
   		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,'one-way-new-result-card')]")));
           long endTime2 = System.currentTimeMillis();
   		long loadTimeInSeconds2 = (endTime - startTime) / 1000;
   		test.log(Status.INFO, "Flight search results loaded in " + loadTimeInSeconds2 + " seconds");
           
           
   		
   		
   		
   		resultPage.validateResultPage(test);
   		*/
           
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
           resultPage.validateResultPage(test);
           String [] userDetails=resultPage.userEnteredDetails(test);
         //  resultPage.selectFlightBasedOnIndex(FlightCardBasedOnIndex,test);
         //  Thread.sleep(5000);
           
        //   resultPage.selectAirlineSequentially(false);
          /*
          Map<String, String> result = resultPage.checkSupplierAndDuplicateWithReport(driver, test, "flydubai");
          

           if (result != null) {
               int index = Integer.parseInt(result.get("flightIndex"));  // Get the flightIndex from the map
               Map<String, Object>flightcardDetails=  resultPage.selectFlightBasedOnIndexSimple(index);         // Use it wherever needed
              
           } else {
               System.out.println("❌ Supplier not found in any flight card.");
           }
*/
        //   List<Map<String, String>> result = resultPage.checkSupplierAndDuplicateWithReport(driver, test, "amadeus");
          // List<Map<String, String>> result = resultPage.checkSupplierAndDuplicateWithReport(driver, test, "airarabia");
           List<Map<String, String>> result = resultPage.checkSupplierAndDuplicateWithReport(driver, test,Supplier);

           if (result != null && !result.isEmpty()) {
               Map<String, String> firstMatch = result.get(0);  // get first matching fare details if u put get(1) next farecard details in will return  result card index also it will return
               int index = Integer.parseInt(firstMatch.get("flightIndex"));  // Get flightIndex from the map
               int fareIndex = Integer.parseInt(firstMatch.get("fareIndex")); 
               // Optionally print all details from firstMatch
               System.out.println("Flight Index: " + firstMatch.get("flightIndex"));
               System.out.println("Fare Index: " + firstMatch.get("fareIndex"));
               System.out.println("Supplier: " + firstMatch.get("supplier"));
               System.out.println("Fare Type: " + firstMatch.get("fareType"));
               System.out.println("Price: AED " + firstMatch.get("price"));
               System.out.println("Checkin: " + firstMatch.get("checkin"));
               System.out.println("Cabin: " + firstMatch.get("cabin"));
               System.out.println("Refundable: " + firstMatch.get("refundable"));
               System.out.println("Fare Class: " + firstMatch.get("fareClass"));
               
               String checkin=firstMatch.get("checkin");
               String cabin=firstMatch.get("cabin");
               String fareType=firstMatch.get("fareType");
               String fareClass=firstMatch.get("fareClass");
               String farePrice=firstMatch.get("price");
               
               List<String> fareTypeDetails = Arrays.asList(
            		   checkin,cabin,fareType,fareClass
     				 
     		  );
               
               Map<String, String> flightcardDetails = resultPage.getFlightCardDetails(index);  // Use it wherever needed
               System.out.println(flightcardDetails.size());
               for (Map.Entry<String, String> entry : flightcardDetails.entrySet()) {
                   System.out.println(entry.getKey() + " : " + entry.getValue());
               }
               
               Thread.sleep(2000);
               resultPage.clickOnFlightDetails(index,test);
               Thread.sleep(2000);
               
            test.log(Status.INFO, "Validating Flight Details Page Header");
               flightDetails.validateFlightDetailsBasedOnFlightCard(userDetails, test);
               
               String[] GetFlightDetails  = flightDetails.getFlightDetails();
               test.log(Status.INFO, "Validating Flight Details Page");
               ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO, "Flight Details Page", "Flight Details Page");
               Thread.sleep(2000);
              flightDetails.validateFlightCardAndFlightDetails(flightcardDetails,GetFlightDetails, fareTypeDetails,test,driver);
              
              String selectedFlightDetails=flightDetails.getAllFlightDetails();
              
              flightDetails.clickOnFareSummary(test);
              Thread.sleep(2000);
              flightDetails.fareBreakUp(farePrice,test);
              Thread.sleep(2000);
          //    flightDetails.clickOnFareSummary(test);
              Thread.sleep(2000);
         //     flightDetails.validateFareRules(test);
              Thread.sleep(2000);
              flightDetails.closeNavBar(test);
              Thread.sleep(2000);
              resultPage.clickOnFareBookNow(index,fareIndex,test);
              Thread.sleep(5000);
              
            String bookingPageDetails=  bookingPage.getAllFlightDetailsInBookingPage();
            Thread.sleep(3000);
            bookingPage.validateFlightDetailsContentMatch(selectedFlightDetails,bookingPageDetails,test);
            Thread.sleep(2000);
            bookingPage.validateFinalPrice(farePrice,test);
              
           } else {
               System.out.println("❌ Supplier not found in any flight card.");
              // test.log(Status.INFO, "❌ Supplier not found in any flight card.");
           }

           
           
           
   //this case to check 
   		
            logger.info("******** TestCase1: testLogin completed successfully ********");

        } catch (Exception e) {
            logger.error("Test failed due to: ", e);
            test.fail("Test failed with exception: " + e.getMessage());
            throw e;  // Re-throw to ensure Retry works properly
        }
    }
    
    
    
    

}
