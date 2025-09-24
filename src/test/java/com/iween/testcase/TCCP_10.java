package com.iween.testcase;

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

public class TCCP_10 extends baseClass {

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
    		  
    		  test.log(Status.INFO, "Depart From: " + departFrom +",Going To: "+ goingTo+",Selected Class: "+Class);
  	        test.log(Status.INFO, "AdultsCounts: " + adultsCounts + ", ChildrenCount: " + childCount + ", InfantsCount: " + infantsCount);
        		 
    		  
    		  String adultDOB = excelTestData.get("adultDOB");
    		  String issueAdultDOB = excelTestData.get("issueAdultDOB");
    		  String passportExpiryDateForAdult = excelTestData.get("passportExpiryDateForAdult");
    		  String AdultName = excelTestData.get("AdultName");
    		  
    		  List<String>travellerInfo=Arrays.asList(
    				  AdultName  // adult
    				  
    		  );
    		  
    		  List<String> dobList = Arrays.asList(
    				  adultDOB  // adult
    				  
    		  );
    		  List<String> issueDobList = Arrays.asList(
    				  issueAdultDOB  // adult
    				  
    		  );
    		  
    		  List<String> expiryOfPassport = Arrays.asList(
    				  passportExpiryDateForAdult  // adult
    				 
    		  );
    		
    		 
    		
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
           
           
              
          SearchPage.validateHomePageIsDisplayed(test);
  		SearchPage.ValidateDefaultSecter(test);
           
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

         //  resultPage.selectFlightBasedOnIndex(FlightCardBasedOnIndex,test);
         //  Thread.sleep(5000);
           
           resultPage.selectAirline("Air Arabia (G9)");
           Thread.sleep(2000);
          String totalPrice = resultPage.selectFlightBasedOnIndexAndValidateAmenities(1,test);  
          
           
          
           
           bookingPage.clickOnContinueButton();
           bookingPage.validateTravellerDetailsIsDisplayed(test);
        String mobileNumber  = bookingPage.generateRandomMobileNumber();
        String email=  bookingPage.generateRandomEmail();
           
           
           bookingPage.addMobileCode("91");
         //  bookingPage.addMobileNumber(mobileNumber);
        //   bookingPage.addEmail(email);
         
           bookingPage.addMobileNumber("9393382211");
           bookingPage.addEmail("tarun@tripgain.com");
           Thread.sleep(3000);
           bookingPage.selectTitle();
           Thread.sleep(2000);
         
           bookingPage.firstNameForAllFields(travellerInfo);
           Thread.sleep(2000);
           
           bookingPage.lastNameForAllFields();
           Thread.sleep(2000);
           
           bookingPage.selectDateofBirth(dobList);
           Thread.sleep(2000);
           
           bookingPage.enterPassportNumber();
           Thread.sleep(2000);
           
           bookingPage.passportIssueDateofBirth(issueDobList);
           Thread.sleep(2000);
           
           bookingPage.passportDateOfExpiry(expiryOfPassport);
           Thread.sleep(2000);
           
           bookingPage.passportPlaceOfIssue();
           Thread.sleep(2000);
           
           bookingPage.clickOnContinueButton();
           
           List<String>seatNumber  = bookingPage.clickOnSelectSeat(test,1);
           List<String>seatInfo  = bookingPage.getSelectSeatTravelInfoList();
           System.out.println(seatInfo.get(0));
          double seatPrice =bookingPage.validateAndSumPrices(travellerInfo,seatNumber,seatInfo,test);
          System.out.println(seatPrice);
       String seatAmount   =bookingPage.validateSeatPrice(seatPrice,test);
       
       
      List<String> meal= bookingPage.clickOnAddMeals(test,1);//this returning seleected meal name
    double TotalmealAmount= bookingPage.getTotalMealPrice();// price of selected meal
   String mealPrice= bookingPage.validateMealtPrice(TotalmealAmount, test);
   
   
   List<String> baggage= bookingPage.clickOnAddBaggage(test,1);
   double TotalBaggageAmount= bookingPage.getTotalBaggagePrice();
   String baggagePrice= bookingPage.validateBaggagePrice(TotalBaggageAmount, test);
   
   Thread.sleep(3000);
   List<String> specialSSR= bookingPage.clickOnSpecialSSR(test,1);
       
          
       //   bookingPage.clickOnAddMeals();
          bookingPage.clickOnEyeIcon();
          bookingPage.validateTaxGrid(test);
          
          //to validate total amount
          List<String> totaloptionadd = Arrays.asList(seatAmount,mealPrice,baggagePrice);
        double totalprice=  bookingPage.validateTotalPrice(totalPrice, totaloptionadd, test);
        
        ArrayList<String> travellerDetails    = bookingPage.travelDetails();
          bookingPage.clickOnPayment(" CreditCard");
          bookingPage.clickOnReadAndAccept();
          Thread.sleep(3000);
          bookingPage.validateConfirmationPage(travellerDetails,totalprice,test);
          
          paymentGateWayPage.validatePayMentPage(travellerDetails, totalprice, test);
          
          
          

           
           
           
           
   //this case to check 
   		
            logger.info("******** TestCase1: testLogin completed successfully ********");

        } catch (Exception e) {
            logger.error("Test failed due to: ", e);
            test.fail("Test failed with exception: " + e.getMessage());
            throw e;  // Re-throw to ensure Retry works properly
        }
    }
    
    
    

}
