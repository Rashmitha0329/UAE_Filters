package com.iween.RoundTripFilterTestcase1;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
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
import com.skytraveller.pageObjects1.ResultPage;
import com.skytraveller.pageObjects1.SearchPage;
import com.skytraveller.pageObjects1.loginPage;
public class TCCP_1 extends baseClass {
	 
    @Test(dataProvider = "excelData", dataProviderClass = DataProviders.class, retryAnalyzer = Retry.class)
    public void myTest(Map<String, String> excelTestData) throws Exception {
 
        ExtentTest test = ExtentManager.getTest();  // Get the ExtentTest instance from thread-local
        logger.info("******** Starting TestCase1: testLogin ********");
        
		try {
			// Log the data being used
			System.out.println("Running test with: " + excelTestData);
			test.log(Status.INFO, "Validation AirLines Filter");
			//Get The Data From Excel
			String departFrom = excelTestData.get("Depart From");
			String goingTo = excelTestData.get("Going To");
			String adultsCounts = excelTestData.get("AdultsCounts");
			String childCount = excelTestData.get("ChildrenCount");
			String infantsCount = excelTestData.get("InfantsCount");
			String Class = excelTestData.get("Class");
			// String stops = excelTestData.get("Stops");
			test.log(Status.INFO, "Depart From: " + departFrom +",Going To: "+ goingTo+",Selected Class: "+Class);
  	        test.log(Status.INFO, "AdultsCounts: " + adultsCounts + ", ChildrenCount: " + childCount + ", InfantsCount: " + infantsCount);
			String rawFareType = excelTestData.get("FareType"); // e.g. "ECOVALU, FLEXIFARE"
			List<String> fareTypes = Arrays.asList(rawFareType.split("\\s*,\\s*"));

			String rawStops = excelTestData.get("Stops");
			List<String> stopFilters = Arrays.asList(rawStops.split("\\s*,\\s*"));

			String rawAirlineData = excelTestData.get("airLine"); // e.g., "IndiGo, Air India, SpiceJet"
			List<String> AirLine = Arrays.asList(rawAirlineData.split("\\s*,\\s*")); // Split by comma and optional spaces

			String raw = excelTestData.get("RefundableInfo");
			List<String> refundableOptions = Arrays.asList(raw.split("\\s*,\\s*"));

			String arrivalFromExcel = excelTestData.get("ArrivalFilter"); // e.g., "00 -06,12 -18"
			List<String> arrivalFilters = Arrays.asList(arrivalFromExcel.split("\\s*,\\s*"));

			String departureFromExcel = excelTestData.get("DepartureFilter");  // e.g., "00 -06,12 -18"
			List<String> departureFilters = Arrays.asList(departureFromExcel.split("\\s*,\\s*"));

			//Method To Get Future Date
			Map<String, Iween_FutureDates.DateResult> dateResults = futureDates.furtherDate();
    		Iween_FutureDates.DateResult date2 = dateResults.get("datePlus2");
    		String fromMonthYear = date2.month + " " + date2.year;
    		Iween_FutureDates.DateResult date8 = dateResults.get("datePlus8");
    		String ReturnMonthYear = date8.month + " " + date8.year;
  	   	    test.log(Status.INFO, "Flight OnWardDate:" +" "+date2.day+" "+fromMonthYear+ "Flight ReturnDate:"+date8.day+" "+ReturnMonthYear);

			// Login page object
			loginPage loginPage = new loginPage(driver);
			SearchPage SearchPage = new SearchPage(driver);
			 ResultPage resultPage = new ResultPage(driver);

			loginPage.validateLogo(test);

			// Perform login using values from properties file
			loginPage.UserLogin(p.getProperty("username"), p.getProperty("password"));




			long startTime = System.currentTimeMillis();
			loginPage.clickOnSubmitButton();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Search Flights']")));
			long endTime = System.currentTimeMillis();
			long loadTimeInSeconds = (endTime - startTime) / 1000;
			test.log(Status.INFO, "Flight search page  loaded in " + loadTimeInSeconds + " seconds");
			Thread.sleep(5000);
			//SearchPage.searchFightsOnHomePageRoundTrip(departFrom,goingTo,date2.day,fromMonthYear,date4.day,returnMonthYear,adultsCounts,childCount,infantsCount);    
	       // SearchPage.searchFightsOnHomePageRoundTrip(departFrom,goingTo,date4.day,fromMonthYear,adultsCounts,childCount,infantsCount);
	         SearchPage.searchFightsOnHomePageRoundTrip(departFrom,goingTo,date2.day,fromMonthYear,date8.day,ReturnMonthYear,adultsCounts,childCount,infantsCount);


			long startTime1 = System.currentTimeMillis();
	        
	        // Click the Search button
	        SearchPage.clickOnSearch();

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
	        resultPage. waitForProgressToComplete();

			List<String> selectAirlineReturn = SearchPage.selectAirline(AirLine,test);

			//SearchPage.airLineFilterValidationRoundTrip(selectAirlineReturn);
			//SearchPage.validateAirlineFilterResultInRoundTrip(selectAirlineReturn,test);
			SearchPage.validateAirlineRoundTrip(selectAirlineReturn,test);

		
		} catch (Exception e) {
			logger.error("Test failed due to: ", e);
			test.fail("Test failed with exception: " + e.getMessage());
			throw e;  // Re-throw to ensure Retry works properly
		}
	}
}
