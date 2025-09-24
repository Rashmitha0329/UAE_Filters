package com.skytraveller.pageObjects;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class ResultPage extends BasePage{

	public ResultPage(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(xpath="(//div[contains(@class,'react-select__input-container')]/input)[1]")
	WebElement fromLocation;
	
	@FindBy(xpath="(//div[contains(@class,'react-select__input-container')]/input)[2]")
	WebElement toLocation;
	
	@FindBy(xpath = "(//div[@class='react-datepicker__input-container'])[1]")
	WebElement datePickerInput;
	
	@FindBy(xpath = "(//div[@class='react-datepicker__current-month'])[1]")
	WebElement date;
	
	@FindBy(xpath = "//button[@aria-label='Next Month']")
	WebElement nextMonth;
 
 
	@FindBy(xpath = "(//div[@class='react-datepicker__header ']/child::div)[1]")
	WebElement MonthYear;
	
	@FindBy(xpath="//span[@class='travellers-class_text']")
	WebElement clickOnClassPassangerDropdown;
	
	@FindBy(xpath="//button[text()='Done']")
	WebElement doneButton;
	
	@FindBy(xpath="//button[text()='Search Flights']")
	WebElement searchFlight;
	
	@FindBy(id="prefclass")
	WebElement classDropdown;
	
	
	
	public void validateLogoInResultPage(WebDriver driver, String imageXpath, int expectedWidth, int expectedHeight, ExtentTest test) {
	    try {
	        WebElement image = driver.findElement(By.xpath(imageXpath));
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        Long naturalWidth = (Long) js.executeScript("return arguments[0].naturalWidth;", image);
	        Long naturalHeight = (Long) js.executeScript("return arguments[0].naturalHeight;", image);

	        System.out.println("Intrinsic size: " + naturalWidth + " × " + naturalHeight);

	        if (naturalWidth == expectedWidth && naturalHeight == expectedHeight) {
	            System.out.println("✅ Image intrinsic size is valid in Result Page");
	            test.log(Status.PASS, "✅ Image is uploaded and intrinsic size is valid in Result Page: "
	                    + naturalWidth + "×" + naturalHeight);
	        } else {
	            System.out.println("❌ Image intrinsic size is NOT valid In Result Page");
	            test.log(Status.FAIL, "❌ Image is uploaded but intrinsic size is invalid in Result Page. "
	                    + "Expected: " + expectedWidth + "×" + expectedHeight +
	                    ", Found: " + naturalWidth + "×" + naturalHeight);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                    "Intrinsic size mismatch in Result Page", "ImageIntrinsicSizeInvalidInResultPage");
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Exception in validateImageIntrinsicSize: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Exception during intrinsic image validation in Result Page: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "Exception in image validation In ResultPage", "ImageIntrinsicSizeExceptionInResultPage");
	        Assert.fail();
	    }
	}
	
	public void validateResultPage(ExtentTest test) {
	    try {
	        List<WebElement> flightCards = driver.findElements(By.xpath("(//*[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]"));
	 
	        if (!flightCards.isEmpty() && flightCards.get(0).isDisplayed()) {
	            System.out.println("✅ Flight card is displayed successfully.");
	            test.log(Status.PASS, "✅ Flight card is displayed successfully.");
	        } else {
	            List<WebElement> noFlightMessages = driver.findElements(By.xpath("//div[text()='No Flights Found']"));
	 
	            if (!noFlightMessages.isEmpty() && noFlightMessages.get(0).isDisplayed()) {
	                System.out.println("⚠️ No flight found for this search.");
	                test.log(Status.INFO, "⚠️ No flight found for this search.");
	            } else {
	                System.out.println("❌ Neither flight cards nor 'No Flights Found' message is present.");
	                test.log(Status.FAIL, "❌ Neither flight cards nor 'No Flights Found' message is present.");
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No flight cards or messages", "ResultPageValidationFailure");
	                Assert.fail();
	            }
	        }
	 
	    } catch (Exception e) {
	        System.out.println("❌ Error while validating result page: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Exception while validating result page: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception during result page check", "ResultPageException");
	        Assert.fail();
	    }
	}
	
	
	public boolean validateResultPage1(ExtentTest test) {
	    try {
	        // Try to find at least one flight card
	        List<WebElement> flightCards = driver.findElements(By.xpath("//*[contains(@class,'one-way-new-result-card')]"));

	        if (!flightCards.isEmpty() && flightCards.get(0).isDisplayed()) {
	            System.out.println("✅ Flight card is displayed successfully.");
	            test.log(Status.PASS, "✅ Flight card is displayed successfully.");
	            return true;
	        } else {
	            // Check if 'No Flights Found' message is shown
	            List<WebElement> noFlightMessages = driver.findElements(By.xpath("//div[text()='No Flights Found']"));

	            if (!noFlightMessages.isEmpty() && noFlightMessages.get(0).isDisplayed()) {
	                System.out.println("⚠️ No flight found for this search.");
	                test.log(Status.INFO, "⚠️ No flight found for this search.");
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, " No flight found for this search", " No flight found for this search");
	                Assert.fail();
	                return false;
	               
	            } else {
	                System.out.println("❌ Neither flight cards nor 'No Flights Found' message is present.");
	                test.log(Status.FAIL, "❌ Neither flight cards nor 'No Flights Found' message is present.");
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No flight cards or messages", "ResultPageValidationFailure");
	                Assert.fail();
	                return false;
	            }
	        }

	    } catch (Exception e) {
	        System.out.println("❌ Error while validating result page: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Exception while validating result page: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception during result page check", "ResultPageException");
	        Assert.fail();
	        return false;
	    }
	}

	
	
	public void selectFlightBasedOnIndex(int index, ExtentTest test) {
	    try {
	        
	        String viewPriceXPath = "(//*[contains(@class,'one-way-new-result-card')]//*[text()='View Price'])[" + index + "]";
	        WebElement viewPriceButton = driver.findElement(By.xpath(viewPriceXPath));

	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewPriceButton);
	        Thread.sleep(500);
	        viewPriceButton.click();
	        test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + index);
             System.out.println("✅ Clicked 'View Price' for flight at index: " + index);
	        // Wait for fare component to be visible
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement fareMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='fare-components-list']")));

	        if (fareMenu.isDisplayed()) {
	            List<WebElement> fares = driver.findElements(By.xpath("//*[contains(@class,'fare-component-watermark')]"));

	            if (!fares.isEmpty()) {
	                WebElement firstFare = fares.get(0);
	                WebElement bookNowBtn = firstFare.findElement(By.xpath(".//*[text()='Book now']")); // dot (.) to search relative to fare element
	                bookNowBtn.click();
	                test.log(Status.PASS, "✅ 'Book now' button clicked for selected flight.");
	                areYouSurePopUp();
	            } else {
	                test.log(Status.FAIL, "❌ No fare options available.");
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No fares found", "FareOptionsMissing");
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Exception while selecting flight: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Exception while selecting flight: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Selection Failure", "FlightSelectException");
	    }
	}
	
	public void selectFlightBasedOnIndexForDetails(int index, ExtentTest test) {
	    try {
	       
	        String flightDetailsXPath = "(//*[contains(@class,'one-way-new-result-card')]//*[text()='Flight Details'])[" + index + "]";
	        WebElement flightDetailsButton = driver.findElement(By.xpath(flightDetailsXPath));

	        
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", flightDetailsButton);
	        Thread.sleep(500); 

	      
	        flightDetailsButton.click();

	        // Log success
	        test.log(Status.PASS, "✅ Clicked 'Flight Details' for flight at index: " + index);
	        System.out.println("✅ Clicked 'Flight Details' for flight at index: " + index);

	       

	    } catch (Exception e) {
	        System.out.println("❌ Exception while selecting flight: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Exception while selecting flight at index " + index + ": " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Selection Failure", "FlightSelectException");
	        Assert.fail();
	    }
	}


	/*
	public String[] userEnteredDetails(ExtentTest test) {
	    try {
	        // Get location codes
	        String onwardLocation = driver.findElement(By.xpath("(//*[contains(@class,'react-select__single-value')]//span)[1]")).getText().split(" - ")[1].trim();
	        String returnLocation = driver.findElement(By.xpath("(//*[contains(@class,'react-select__single-value')]//span)[2]")).getText().split(" - ")[1].trim();

	        // Get and format date
	        String date = driver.findElement(By.xpath("(//*[@class='react-datepicker__input-container']/input)[1]")).getAttribute("value");
	        String formattedDate = date.replaceFirst(" (\\d{4})$", ", $1");//20 Aug 2025 ->20 Aug,2025

	        // Get traveller details
	        String[] travellerDetails = driver.findElement(By.xpath("//*[@class='travellers-class_text']")).getText().split(",");

	        // Create final array
	        String[] allDetails = new String[travellerDetails.length + 3];
	        allDetails[0] = onwardLocation;
	        allDetails[1] = returnLocation;
	        allDetails[2] = formattedDate;

	        for (int i = 0; i < travellerDetails.length; i++) {
	            allDetails[i + 3] = travellerDetails[i].trim();
	        }

	        return allDetails;

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Error getting user-entered details: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Traveller Details", "Error during extraction");
	        Assert.fail();
	        return new String[0];
	        
	    }
	    }
	    */
	/*
	public String[] userEnteredDetails(ExtentTest test) {
	    try {
	        // Get location codes
	        String onwardLocation = driver.findElement(By.xpath("(//*[contains(@class,'react-select__single-value')]//span)[1]"))
	                                     .getText().split(" - ")[1].trim();
	        String returnLocation = driver.findElement(By.xpath("(//*[contains(@class,'react-select__single-value')]//span)[2]"))
	                                     .getText().split(" - ")[1].trim();

	        // Get and format date
	        String date = driver.findElement(By.xpath("(//*[@class='react-datepicker__input-container']/input)[1]"))
	                            .getAttribute("value");
	        String formattedDate = date.replaceFirst(" (\\d{4})$", ", $1"); // e.g. "20 Aug 2025" -> "20 Aug, 2025"
	        
	       

	        // Get traveller details
	        String[] travellerDetails = driver.findElement(By.xpath("//*[@class='travellers-class_text']")).getText().split(",");

	        // Create final array
	        String[] allDetails = new String[travellerDetails.length + 3];
	        allDetails[0] = onwardLocation;
	        allDetails[1] = returnLocation;
	        allDetails[2] = formattedDate;
	       

	        // Process traveller details, convert only "First Class" to "FIRST" // because in flight card it is displaying like that
	        for (int i = 0; i < travellerDetails.length; i++) {
	            String detail = travellerDetails[i].trim();

	            if (i == travellerDetails.length - 1 && detail.equalsIgnoreCase("First Class")) {
	                detail = "FIRST";
	            }

	            allDetails[i + 3] = detail;
	        }

	        return allDetails;

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Error getting user-entered details: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Traveller Details", "Error during extraction");
	        Assert.fail();
	        return new String[0];
	    }
	}
	*/
	public String[] userEnteredDetails(ExtentTest test) {
	    try {
	        // Get location codes
	        String onwardLocation = driver.findElement(By.xpath("(//*[contains(@class,'react-select__single-value')]//span)[1]"))
	                                     .getText().split(" - ")[1].trim();

	        String returnLocation = driver.findElement(By.xpath("(//*[contains(@class,'react-select__single-value')]//span)[2]"))
	                                     .getText().split(" - ")[1].trim();

	        // Get and format onward date
	        String date = driver.findElement(By.xpath("(//*[@class='react-datepicker__input-container']/input)[1]"))
	                            .getAttribute("value"); // e.g. "20 Aug 2025"
	        String formattedDate = date.replaceFirst(" (\\d{4})$", ", $1"); // e.g. "20 Aug 2025" -> "20 Aug, 2025"

	        // Get and format return date
	        String returndate = driver.findElement(By.xpath("(//*[@class='react-datepicker__input-container']/input)[2]"))
	                                  .getAttribute("value"); // e.g. "27 Aug 2025"
	        String formattedReturnDate = returndate.replaceFirst(" (\\d{4})$", ", $1"); // -> "27 Aug, 2025"

	        // Get traveller details
	        String[] travellerDetails = driver.findElement(By.xpath("//*[@class='travellers-class_text']"))
	                                          .getText().split(",");

	        // Prepare final array (2 locations + 2 dates + traveller details)
	        String[] allDetails = new String[travellerDetails.length + 4];

	        allDetails[0] = onwardLocation;
	        allDetails[1] = returnLocation;
	        allDetails[2] = formattedDate;
	        allDetails[3] = formattedReturnDate;

	        // Process and add traveller details
	        for (int i = 0; i < travellerDetails.length; i++) {
	            String detail = travellerDetails[i].trim();

	            // Convert "First Class" to "FIRST"
	            if (i == travellerDetails.length - 1 && detail.equalsIgnoreCase("First Class")) {
	                detail = "FIRST";
	            }

	            allDetails[i + 4] = detail;
	        }

	        return allDetails;

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Error getting user-entered details: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Traveller Details", "Error during extraction");
	        Assert.fail(); // Fails the test
	        return new String[0];
	    }
	}


	    /*
	     allDetails = [
    "BLR",             // index 0
    "DEL",             // index 1
    "20 Aug, 2025",    // index 2
    "1A",              // index 3
    "1C",              // index 4
    "1I",              // index 5
    "Premium Economy"  // index 6
]
	   
	     */
	

	
	public void validateFlightCardSearch(String[] userenterdData, ExtentTest test) {
	    String onWardLoaction = userenterdData[0];
	    String returnLoaction = userenterdData[1];
	    System.out.println(returnLoaction);
	    String userEnteredDate = userenterdData[2];

	 //   List<WebElement> onwardLocations = driver.findElements(By.xpath("(//*[contains(@class,'one-way-new-result-card ')]//span)[6]"));
	 //   List<WebElement> returnLocations = driver.findElements(By.xpath("(//*[contains(@class,'one-way-new-result-card ')]//span)[11]"));
	  //  List<WebElement> departDates = driver.findElements(By.xpath("(//*[contains(@class,'one-way-new-result-card ')]//span)[7]"));
	    List<WebElement> onwardLocations = driver.findElements(By.xpath("//span[@class='flight-origin']"));
	    List<WebElement> returnLocations = driver.findElements(By.xpath("//*[contains(@class,'flight-destination')]"));
	    List<WebElement> departDates = driver.findElements(By.xpath("//span[@class='flight-depdate']"));
	    System.out.println(onwardLocations.size());
	    System.out.println(returnLocations.size());
	    System.out.println(departDates.size());
	    try {
	        for (int i = 0; i < onwardLocations.size(); i++) {
	            String onward = onwardLocations.get(i).getText().split("-")[0].trim();
	            if (onward.equalsIgnoreCase(onWardLoaction)) {
	                test.log(Status.PASS, "Flight card " + (i + 1) + ": Onward location matched: " + onward);
	            } else {
	                test.log(Status.FAIL, "Flight card " + (i + 1) + ": Onward location mismatch. Found: " + onward + ", Expected: " + onWardLoaction);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Onward Location Mismatch", "Mismatch in onward location");
	            }
	        }
	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception while validating onward locations: " + e.getMessage());
	    }

	    try {
	        for (int i = 0; i < returnLocations.size(); i++) {
	            String returnLoc = returnLocations.get(i).getText().split("-")[0].trim();
	            System.out.println(returnLoc);
	            if (returnLoc.equalsIgnoreCase(returnLoaction)) {
	                test.log(Status.PASS, "Flight card " + (i + 1) + ": Return location matched: " + returnLoc);
	            } else {
	                test.log(Status.FAIL, "Flight card " + (i + 1) + ": Return location mismatch. Found: " + returnLoc + ", Expected: " + returnLoaction);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Return Location Mismatch", "Mismatch in return location");
	                Assert.fail();
	            }
	        }
	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception while validating return locations: " + e.getMessage());
	        Assert.fail();
	    }

	    try {
	        for (int i = 0; i < departDates.size(); i++) {
	            String depDate = departDates.get(i).getText().trim();
	            if (depDate.equals(userEnteredDate)) {
	                test.log(Status.PASS, "Flight card " + (i + 1) + ": Departure date matched: " + depDate);
	            } else {
	                test.log(Status.FAIL, "Flight card " + (i + 1) + ": Departure date mismatch. Found: " + depDate + ", Expected: " + userEnteredDate);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Departure Date Mismatch", "Mismatch in departure date");
	                Assert.fail();
	            }
	        }
	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception while validating departure dates: " + e.getMessage());
	        Assert.fail();
	    }
	}

	public void validateFlightDetailsBasedOnFlightCard(String[] userEnteredData, ExtentTest test) {
	    try {
	        String onWardLocation = userEnteredData[0];          // e.g., BLR
	        String returnLocation = userEnteredData[1];          // e.g., HAM
	        String userEnteredDate = userEnteredData[2];         // e.g., 20 Aug, 2025
	        String travelClass = userEnteredData[userEnteredData.length - 1]; // Last item assumed to be travel class

	        // Extract airport codes from sector span
	        String sectorText = driver.findElement(By.xpath("//*[@class='sector-span']")).getText(); // e.g., Bengaluru(BLR) -> Hamburg(HAM)
	        String[] airportCodes = sectorText.split("\\(");
	        String fromCode = airportCodes[1].split("\\)")[0].trim();
	        String toCode = airportCodes[2].split("\\)")[0].trim();

	        // Extract and format travel date
	        String dateText = driver.findElement(By.xpath("//*[@class='date-span']")).getText(); // e.g., One Way Wed, 20 Aug, 2025
	        String[] parts = dateText.split(",");
	        String formattedDate = parts[1].trim() + ", " + parts[2].trim(); // e.g., 20 Aug, 2025

	        // Extract travel class from flight card
	        String flightTravelClass = driver.findElement(By.xpath("(//*[@class='d-flex gap-sm-2 flex-sm-row flex-column flex-wrap gap-1']/span)[1]")).getText().trim();

	        // Validation logs n
	        if (fromCode.equalsIgnoreCase(onWardLocation) &&
	            toCode.equalsIgnoreCase(returnLocation) &&
	            formattedDate.equals(userEnteredDate) &&
	            flightTravelClass.equalsIgnoreCase(travelClass)) {
//Note travell class id need to add so we can use loop for that
	            test.log(Status.PASS, "Flight details matched successfully.");
	            test.log(Status.INFO, "From: " + fromCode + ", To: " + toCode + ", Date: " + formattedDate + ", Class: " + flightTravelClass);
	        } else {
	            test.log(Status.FAIL, "Flight details mismatch.");
	            test.log(Status.INFO, "Expected -> From: " + onWardLocation + ", To: " + returnLocation + ", Date: " + userEnteredDate + ", Class: " + travelClass);
	            test.log(Status.INFO, "Actual   -> From: " + fromCode + ", To: " + toCode + ", Date: " + formattedDate + ", Class: " + flightTravelClass);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Detail Mismatch", "Mismatch in flight card details");
	            Assert.fail();
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception while validating flight details: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception", "Error during validation");
	        Assert.fail();
	    }
	}

	public void selectFlightBasedOnIndexForWhatsAppAndEmail(int index, ExtentTest test) {
	    try {
	        
	        String viewPriceXPath = "(//*[contains(@class,'one-way-new-result-card')]//*[text()='View Price'])[" + index + "]";
	        WebElement viewPriceButton = driver.findElement(By.xpath(viewPriceXPath));

	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewPriceButton);
	        Thread.sleep(500);
	        viewPriceButton.click();
	        test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + index);
             System.out.println("✅ Clicked 'View Price' for flight at index: " + index);
	        // Wait for fare component to be visible
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement fareMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='fare-components-list']")));

	        if (fareMenu.isDisplayed()) {
	            List<WebElement> fares = driver.findElements(By.xpath("//*[contains(@class,'fare-component-watermark')]"));

	            if (!fares.isEmpty()) {
	                WebElement firstFare = fares.get(0);
	                WebElement bookNowBtn = firstFare.findElement(By.xpath(".//*[text()='Share via Email or WhatsApp']/parent::div//input")); // dot (.) to search relative to fare element
	                bookNowBtn.click();
	                test.log(Status.PASS, "✅ 'Book now' button clicked for selected flight.");
	            } else {
	                test.log(Status.FAIL, "❌ No fare options available.");
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No fares found", "FareOptionsMissing");
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Exception while selecting flight: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Exception while selecting flight: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Selection Failure", "FlightSelectException");
	    }
	}
	
	public void clickOnEmail() {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        
	        WebElement emailButton = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[.//text()='Email']")));

	        emailButton.click();
	        System.out.println("Clicked on the Email button successfully.");
	        
	    } catch (TimeoutException e) {
	        System.out.println("Timeout: Email button was not clickable within the expected time.");
	    } catch (NoSuchElementException e) {
	        System.out.println("Error: Email button element was not found on the page.");
	    } catch (ElementClickInterceptedException e) {
	        System.out.println("Error: Email button was not clickable (e.g., overlapped by another element).");
	    } catch (Exception e) {
	        System.out.println("An unexpected error occurred while trying to click the Email button: " + e.getMessage());
	    }
	}
	
	public void emailPopup(String fareOption,String whatsAppNumber) throws InterruptedException
	{
		WebElement popup=driver.findElement(By.xpath("//*[@class='modal-content']"));
		if(popup.isDisplayed())
		{
			popup.findElement(By.xpath("//*[text()='"+fareOption+"']/input")).click();
			Thread.sleep(3000);
			popup.findElement(By.xpath("//input[@placeholder='Enter Customer No']")).sendKeys(whatsAppNumber);
			popup.findElement(By.xpath("//button[text()='Share in Whatsapp']")).click();
		}
	}
	
	public void selectStopFilter(String filtername) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	   
	    wait.until(ExpectedConditions.visibilityOfElementLocated(
	        By.xpath("//div[text()='Fare policy & No. of stop']/parent::div//label")));

	    ArrayList<String> stops = new ArrayList<>();

	    List<WebElement> listOfStops = driver.findElements(
	        By.xpath("//div[text()='Fare policy & No. of stop']/parent::div//label"));

	    for (WebElement getListOfStops : listOfStops) {
	        String stop = getListOfStops.getText().trim();
	        stops.add(stop);
	    }

	    System.out.println("Available stops: " + stops);

	   

	    boolean found = false;
	    for (WebElement stopLabel : listOfStops) {
	        String labelText = stopLabel.getText().trim();
	        if (labelText.equalsIgnoreCase(filtername)) {
	            System.out.println("User needed filter found: " + labelText);
	            stopLabel.click();
	            found = true;
	            break;
	        }
	    }

	    // If not found, click on the first available stop option
	    if (!found && !listOfStops.isEmpty()) {
	        System.out.println("Target stop not found. Selecting default: " + listOfStops.get(0).getText().trim());
	        listOfStops.get(0).click();
	    }

	    // Print selected checkboxes
	    for (WebElement label : listOfStops) {
	        WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));
	        if (checkbox.isSelected()) {
	            System.out.println("Selected stop: " + label.getText().trim());
	        }
	    }
	}
	

	
	public void validateDefaultCurrency(ExtentTest test) {
	    List<WebElement> priceElements = driver.findElements(By.xpath("//span[contains(@class,'flight-totalfq')]"));
	    int totalPrices = priceElements.size();

	    test.log(Status.INFO, "🔍 Total Currency found on page: " + totalPrices);

	    if (priceElements.isEmpty()) {
	        test.log(Status.WARNING, "⚠️ No price elements found on the page.");
	        ScreenshotUtil.captureAndAttachScreenshot1(
	            driver,
	            test,
	            Status.FAIL,
	            "Screenshot of non-AED price on the page",
	            "NonAED_Price_" + System.currentTimeMillis()
	        );
	        return;
	    }

	    boolean allPricesInAED = true;

	    for (int i = 0; i < priceElements.size(); i++) {
	        String priceText = priceElements.get(i).getText().trim();

	        if (priceText.contains("AED")) {
	          //  test.log(Status.INFO, "✅ Price " + i + " is in AED: " + priceText);
	            // Or remove this line if you don't want console output
	            System.out.println("✅ Price " + i + " is in AED: " + priceText);
	        } else {
	            test.log(Status.FAIL, "❌ Price " + i + " is NOT in AED: " + priceText);

	            ScreenshotUtil.captureAndAttachScreenshot1(
	                driver,
	                test,
	                Status.FAIL,
	                "Screenshot of non-AED price at index " + i,
	                "NonAED_Price_" + i + "_" + System.currentTimeMillis()
	            );

	            allPricesInAED = false;
	            Assert.fail();
	        }
	    }

	    if (allPricesInAED) {
	        test.log(Status.PASS, "✅ All prices are in AED by default.");
	    } else {
	        test.log(Status.FAIL, "❌ Some prices are not in AED.");

	        ScreenshotUtil.captureAndAttachScreenshot1(
	            driver,
	            test,
	            Status.FAIL,
	            "Screenshot showing prices not in AED.",
	            "Final_NonAED_Summary_" + System.currentTimeMillis()
	        );
	        Assert.fail();
	    }
	}

	public void clickOnEyeIconAndBaseFareValidation(ExtentTest test) throws InterruptedException {
	    WebElement eyeIcon = driver.findElement(By.xpath("//*[text()='Net Fare']/parent::*/parent::button"));
	    if (eyeIcon.isDisplayed()) {
	        test.log(Status.INFO, "Eye icon is clicked");
	        eyeIcon.click();
	        Thread.sleep(2000);  // Consider using explicit wait here

	        List<WebElement> baseFares = driver.findElements(By.xpath("//span[contains(@class,'flight-totalnet')]"));
	        test.log(Status.INFO, "Total base fares found: " + baseFares.size());

	        boolean allBaseFare = true;

	        for (int i = 0; i < baseFares.size(); i++) {
	            String basefare = baseFares.get(i).getText().trim();

	            if (basefare.contains("AED")) {
	               // test.log(Status.INFO, "✅ Base fare " + i + " is in AED: " + basefare);
	                System.out.println("✅ Base fare " + i + " is in AED: " + basefare);
	            } else {
	                test.log(Status.FAIL, "❌ Base fare " + i + " is NOT in AED: " + basefare);

	                ScreenshotUtil.captureAndAttachScreenshot1(
	                    driver,
	                    test,
	                    Status.FAIL,
	                    "Screenshot of non-AED base fare at index " + i,
	                    "NonAED_BaseFare_" + i + "_" + System.currentTimeMillis()
	                );

	                allBaseFare = false;
	                Assert.fail("Base fare not in AED at index: " + i);
	            }
	        }

	        if (allBaseFare) {
	            test.log(Status.PASS, "✅ All base fares are displayed in AED.");
	        } else {
	            test.log(Status.FAIL, "❌ Some base fares are not in AED.");

	            ScreenshotUtil.captureAndAttachScreenshot1(
	                driver,
	                test,
	                Status.FAIL,
	                "Screenshot showing base fares not in AED.",
	                "Final_NonAED_BaseFare_Summary_" + System.currentTimeMillis()
	            );
	            Assert.fail("Some base fares are not in AED.");
	        }

	    } else {
	        test.log(Status.FAIL, "❌ Eye icon is not displayed");
	        ScreenshotUtil.captureAndAttachScreenshot1(
	            driver,
	            test,
	            Status.FAIL,
	            "Screenshot - Eye icon is not displayed",
	            "EyeIcon_Not_Displayed_" + System.currentTimeMillis()
	        );
	        Assert.fail("Eye icon is not displayed.");
	    }
	}
 
	
	
	public void enterFromLocation(String from) {
		try {
			fromLocation.clear();
			fromLocation.sendKeys(from);
			location(from);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 Assert.fail();
		}
	}
	
	
	public void enterToLocation(String to) {
		try {
			toLocation.clear();
			toLocation.sendKeys(to);
			location(to);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 Assert.fail();
		}
	}
	
	//Method to select City.
	public void location(String location) throws TimeoutException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Wait for dropdown container to appear
			wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[@role='listbox']")));

			// Wait until options are loaded
			wait.until(driver -> driver.findElements(By.xpath("//span[@class='airport-option_country-code']")).size() > 0);

			List<WebElement> initialOptions = driver.findElements(By.xpath("//span[@class='airport-option_country-code']"));
			int bestScore = Integer.MAX_VALUE;
			String bestMatchText = null;

			String input = location.trim().toLowerCase();

			for (int i = 0; i < initialOptions.size(); i++) {
				try {
					WebElement option = driver.findElements(By.xpath("//span[@class='airport-option_country-code']")).get(i);
					String suggestion = option.getText().trim().toLowerCase();
					int score = levenshteinDistance(input, suggestion);

					if (score < bestScore) {
						bestScore = score;
						bestMatchText = option.getText().trim();
					}
				} catch (StaleElementReferenceException e) {
					System.out.println("Stale element at index " + i + ", skipping.");
				}
			}

			if (bestMatchText != null) {
				// Retry clicking best match up to 3 times
				int attempts = 0;
				boolean clicked = false;
				while (attempts < 3 && !clicked) {
					try {
						WebElement bestMatch = wait.until(ExpectedConditions.elementToBeClickable(
								By.xpath("//span[@class='airport-option_country-code'][text()='" + bestMatchText + "']")));
						bestMatch.click();
						System.out.println("Selected best match: " + bestMatchText);
						clicked = true;
					} catch (StaleElementReferenceException e) {
						System.out.println("Stale element on click attempt " + (attempts + 1) + ", retrying...");
					}
					attempts++;
				}

				if (!clicked) {
					System.out.println("Failed to click the best match after retries.");
				}

			} else {
				System.out.println("No suitable match found for input: " + location);
			}

		} catch (NoSuchElementException e) {
			System.out.println("Input or dropdown not found: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Unexpected error while selecting city or hotel: " + e.getMessage());
		}
	}
	
	public int levenshteinDistance(String a, String b) {
		int[][] dp = new int[a.length() + 1][b.length() + 1];

		for (int i = 0; i <= a.length(); i++) {
			for (int j = 0; j <= b.length(); j++) {
				if (i == 0) {
					dp[i][j] = j;
				} else if (j == 0) {
					dp[i][j] = i;
				} else {
					int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
					dp[i][j] = Math.min(
							Math.min(dp[i - 1][j] + 1,     // deletion
									dp[i][j - 1] + 1),    // insertion
							dp[i - 1][j - 1] + cost); // substitution
				}
			}
		}
		return dp[a.length()][b.length()];
	}



	public void selectDate(String day, String MonthandYear) throws InterruptedException
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// Method A: Using zoom
		js.executeScript("document.body.style.zoom='80%'");
 
		datePickerInput.click();
		String Date = date.getText();
		//	String Date=driver.findElement(By.xpath("(//h2[@class='react-datepicker__current-month'])[1]")).getText();
		if(Date.contentEquals(MonthandYear))
		{
			Thread.sleep(4000);
			driver.findElement(By.xpath("(//div[@class='react-datepicker__month-container'])[1]//div[text()='"+day+"' and @aria-disabled='false']")).click();
			Thread.sleep(4000);
		}else {
			while(!Date.contentEquals(MonthandYear))
			{
				Thread.sleep(500);
				nextMonth.click();
				if(MonthYear.getText().contentEquals(MonthandYear))
				{
					driver.findElement(By.xpath("(//div[@class='react-datepicker__month-container'])[1]//div[text()='"+day+"' and @aria-disabled='false']")).click();
					break;
				}
 
			}
		}
	}
	
	public void addAdult(String adult) {
	    try {
	        driver.findElement(By.xpath("//span[text()='Adults(12y+)']/parent::div//li[text()='" + adult + "']")).click();
	    } catch (Exception e) {
	        System.out.println("Error in addAdult(): " + e.getMessage());
	        Assert.fail();
	    }
	}

	public void addChild(String child) {
	    try {
	        driver.findElement(By.xpath("//span[text()='Children(2y-12y)']/parent::div//li[text()='" + child + "']")).click();
	    } catch (Exception e) {
	        System.out.println("Error in addChild(): " + e.getMessage());
	        Assert.fail();
	    }
	}

	public void infantCount(String infant) {
	    try {
	        driver.findElement(By.xpath("//span[text()='Infants(<2y)']/parent::div//li[text()='" + infant + "']")).click();
	    } catch (Exception e) {
	        System.out.println("Error in infantCount(): " + e.getMessage());
	        Assert.fail();
	    }
	}
	public void selectTravelClass(String travelClass) {
	    try {
	        classDropdown.click();
	        
	       // JavascriptExecutor js = (JavascriptExecutor) driver;
	      //  js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

	        Select select = new Select(classDropdown);
	        select.selectByVisibleText(travelClass);
	    } catch (Exception e) {
	        System.out.println("Error in selectTravelClass(): " + e.getMessage());
	    }
	}

	public void modifySearch(String from, String to, String day, String MonthandYear, String adult, String child, String infant,String travelClass) {
	    try {
	    	Thread.sleep(2000);
	    	JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("window.scrollTo(0, 0);");
	    	
	    	Thread.sleep(1000);
	    	enterFromLocation(from);
	        Thread.sleep(1000);
	        enterToLocation(to);
	        Thread.sleep(1000);
	        selectDate(day, MonthandYear);
	        Thread.sleep(1000);
	        
	        clickOnClassPassangerDropdown.click();
	        Thread.sleep(1000);
	        addAdult(adult);
	        Thread.sleep(1000);
	        addChild(child);
	        Thread.sleep(1000);
	        infantCount(infant);
	        Thread.sleep(2000);
	        selectTravelClass(travelClass);
	        Thread.sleep(1000);
	        doneButton.click();
	       
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt(); // Best practice to reset the interruption status
	        System.out.println("Interrupted while searching flights on home page: " + e.getMessage());
	        Assert.fail();
	    } catch (Exception e) {
	        System.out.println("Error in searchFightsOnHomePage(): " + e.getMessage());
	        Assert.fail(); 
	    }
	}
	
	public void clickOnSearch(ExtentTest test) {
	    try {
	        WebElement searchBtn = driver.findElement(By.xpath("//button[text()='Search']"));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", searchBtn);
	        Thread.sleep(300);
	        searchBtn.click();
	        System.out.println("✅ Clicked on 'Search Flights' button.");
	        test.log(Status.PASS, "✅ Clicked on 'Search Flights' button.");
	    } catch (Exception e) {
	        System.out.println("❌ Failed to click 'Search Flights' button: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Failed to click 'Search Flights' button: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Search Click Failure", "SearchButtonClickException");
	        Assert.fail();
	    }
	}

	/*
	public String[] modifyUserEnteredDetails(ExtentTest test) {
	    try {
	        // Get location codes
	        String onwardLocation = driver.findElement(By.xpath("(//*[contains(@class,'react-select__single-value')]//span)[1]")).getText().split(" - ")[1].trim();
	        String returnLocation = driver.findElement(By.xpath("(//*[contains(@class,'react-select__single-value')]//span)[2]")).getText().split(" - ")[1].trim();

	        // Get and format date
	        String date = driver.findElement(By.xpath("(//*[@class='react-datepicker__input-container']/input)[1]")).getAttribute("value");
	        String formattedDate = date.replaceFirst(" (\\d{4})$", ", $1");//20 Aug 2025 ->20 Aug,2025

	        // Get traveller details
	        String[] travellerDetails = driver.findElement(By.xpath("//*[@class='travellers-class_text']")).getText().split(",");

	        // Create final array
	        String[] allDetails = new String[travellerDetails.length + 3];
	        allDetails[0] = onwardLocation;
	        allDetails[1] = returnLocation;
	        allDetails[2] = formattedDate;

	        for (int i = 0; i < travellerDetails.length; i++) {
	            allDetails[i + 3] = travellerDetails[i].trim();
	        }

	        return allDetails;

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Error getting user-entered details: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Traveller Details", "Error during extraction");
	        Assert.fail();
	        return new String[0];
	        
	    }
	    }
	    */
	public String[] modifyUserEnteredDetails(ExtentTest test) {
	    try {
	        // Get location codes
	        String onwardLocation = driver.findElement(By.xpath("(//*[contains(@class,'react-select__single-value')]//span)[1]"))
	                                     .getText().split(" - ")[1].trim();
	        String returnLocation = driver.findElement(By.xpath("(//*[contains(@class,'react-select__single-value')]//span)[2]"))
	                                     .getText().split(" - ")[1].trim();

	        // Get and format date
	        String date = driver.findElement(By.xpath("(//*[@class='react-datepicker__input-container']/input)[1]"))
	                            .getAttribute("value");
	        String formattedDate = date.replaceFirst(" (\\d{4})$", ", $1"); // e.g. "20 Aug 2025" -> "20 Aug, 2025"

	        // Get traveller details
	        String[] travellerDetails = driver.findElement(By.xpath("//*[@class='travellers-class_text']")).getText().split(",");

	        // Create final array
	        String[] allDetails = new String[travellerDetails.length + 3];
	        allDetails[0] = onwardLocation;
	        allDetails[1] = returnLocation;
	        allDetails[2] = formattedDate;

	        // Process traveller details, convert only "First Class" to "FIRST" // because in flight card it is displaying like that
	        for (int i = 0; i < travellerDetails.length; i++) {
	            String detail = travellerDetails[i].trim();

	            if (i == travellerDetails.length - 1 && detail.equalsIgnoreCase("First Class")) {
	                detail = "FIRST";
	            }

	            allDetails[i + 3] = detail;
	        }

	        return allDetails;

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Error getting user-entered details: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Traveller Details", "Error during extraction");
	        Assert.fail();
	        return new String[0];
	    }
	}

	    /*
	     allDetails = [
    "BLR",             // index 0
    "DEL",             // index 1
    "20 Aug, 2025",    // index 2
    "1A",              // index 3
    "1C",              // index 4
    "1I",              // index 5
    "Premium Economy"  // index 6
]
	   
	     */
	
	
	public void areYouSurePopUp() {
	    try {
	        List<WebElement> popups = driver.findElements(By.xpath("//div[@class='fade app-modal help-support-modal modal show']//div[text()='Are You Sure?']/parent::div/parent::div//button[text()='Yes, Continue']"));

	        if (!popups.isEmpty() && popups.get(0).isDisplayed()) {
	            popups.get(0).click();
	            System.out.println("✅ 'Are You Sure?' popup found and 'Yes, Continue' clicked.");
	        } else {
	            System.out.println("ℹ️ No 'Are You Sure?' popup found.");
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Exception while handling popup: " + e.getMessage());
	    }
	}

	
	
	
	
	public HashMap<String, String> selectFlightBasedOnIndexFareSelection(int index, ExtentTest test) {
	    try {
	        // Click on the View Price button based on index
	        String viewPriceXPath = "(//*[contains(@class,'one-way-new-result-card')]//*[text()='View Price'])[" + index + "]";
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement viewPriceButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(viewPriceXPath)));

	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewPriceButton);
	        viewPriceButton.click();
	        test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + index);
	        System.out.println("✅ Clicked 'View Price' for flight at index: " + index);

	        // Wait for the fare components list to appear
	        WebElement fareMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//*[@class='fare-components-list']")));

	        if (fareMenu.isDisplayed()) {
	            List<WebElement> fares = driver.findElements(By.xpath("//*[contains(@class,'fare-component-watermark')]"));

	            if (!fares.isEmpty()) {
	                WebElement firstFare = fares.get(0);

	                String fareName = getTextFromChild(firstFare, ".//*[@class='fs-16 fw-600 text-capitalize fare-type-span d-flex text-end']");
	                String checkIn = getTextFromChild(firstFare, ".//div[contains(text(),'Check-in')]/following-sibling::div");
	                String cabin = getTextFromChild(firstFare, ".//div[contains(text(),'Cabin')]/following-sibling::div");
	                String supplierName = getTextFromChild(firstFare, ".//div[contains(text(),'Supplier')]/following-sibling::div");
	                String farePrice = getTextFromChild(firstFare, ".//*[@class='fs-16 fw-600 primary-color']");

	                HashMap<String, String> fareValue = new HashMap<>();
	                fareValue.put("fareName", fareName);
	                fareValue.put("checkIn", checkIn);
	                fareValue.put("cabin", cabin);
	                fareValue.put("supplierName", supplierName);
	                fareValue.put("farePrice", farePrice);

	                return fareValue;
	            } else {
	                test.log(Status.FAIL, "❌ No fare options available.");
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No fares found", "FareOptionsMissing");
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Exception while selecting flight: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Exception while selecting flight: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Selection Failure", "FlightSelectException");
	    }

	    return null;
	}
	private String getTextFromChild(WebElement parent, String relativeXPath) {
	    try {
	        WebElement child = parent.findElement(By.xpath(relativeXPath));
	        return child.getText().trim();
	    } catch (NoSuchElementException e) {
	        return "N/A"; // or return null based on your preference
	    }
	}

	/*
	public void selectFlightBasedOnIndex(int index)
	{
	 
		WebElement flightCard=driver.findElement(By.xpath("(//*[contains(@class,'one-way-new-result-card')])['"+index+"']"));
		String flightName=flightCard.findElement(By.xpath(".//*[contains(@class,'flight-company')]")).getText();
		WebElement flightNumberElement = flightCard.findElement(By.xpath(".//*[contains(@class, 'flight-number')]"));
		String fullFlightText = flightNumberElement.getText(); // "BA-8772, BA-198, BA-972"
		String[] flightCodes = fullFlightText.split("\\s*,\\s*");
		WebElement equipmentCodes = flightCard.findElement(By.xpath(".//*[contains(@class, 'fs-8 fw-300 grey-color')]"));
		String fullText = equipmentCodes.getText(); // e.g., "Equipment: 321, 351, 32N"

		// Remove "Equipment:" label and split by comma
		String[] flightcode = fullText.replace("Equipment:", "").trim().split("\\s*,\\s*");
		
		WebElement flighttype=flightCard.findElement(By.xpath("(//*[contains(@class,'fs-8 fw-300 grey-color')])[2]"));
		String fulltext=flighttype.getText();
		String[] flightType = fullText.replace("Operated By: ", "").trim().split("\\s*,\\s*");
		
		String departTime=flightCard.findElement(By.xpath("(//span[contains(@class,'title fw-600')])[1]")).getText();
		String arrivalTime=flightCard.findElement(By.xpath("(//span[contains(@class,'title fw-600')])[2]")).getText();
		String onwardLocation=flightCard.findElement(By.xpath("(//span[contains(@style,'font-size: 12px; color: rgb(118, 131, 145);')])[1]")).getText();
		String returnLocation=flightCard.findElement(By.xpath("(//span[contains(@style,'font-size: 12px; color: rgb(118, 131, 145);')])[2]")).getText();
		String depatureDate=flightCard.findElement(By.xpath("(//span[contains(@style,'font-size: 10px; color: rgb(118, 131, 145);')])[1]")).getText();
		String arrivalDate=flightCard.findElement(By.xpath("(//span[contains(@style,'font-size: 10px; color: rgb(118, 131, 145);')])[2]")).getText();
		String price=flightCard.findElement(By.xpath("//span[contains(@class,'fs-18 fw-600 secondary-color')]")).getText();
		String baseFare=flightCard.findElement(By.xpath("//span[contains(@class,'fs-10 fw-300 grey-color ')]")).getText();
		
		
		
		
		
		
		
		
		
	}
	
	*/
	public Map<String, Object> selectFlightBasedOnIndexSimple(int index) {
		
		int count =index+1;//it will return 0 in list in ul index count will start from 1
		
	    Map<String, Object> flightInfo = new HashMap<>();

	    WebElement flightCard = driver.findElement(By.xpath("(//*[contains(@class,'one-way-new-result-card')])[" + count + "]"));

	    String flightName = flightCard.findElement(By.xpath(".//*[contains(@class,'flight-company')]")).getText();
	    flightInfo.put("flightName", flightName);

	    String flightCodesFull = flightCard.findElement(By.xpath(".//*[contains(@class, 'flight-number')]")).getText();
	    flightInfo.put("flightCodesFull", flightCodesFull);
	    flightInfo.put("flightCodes", flightCodesFull.split("\\s*,\\s*"));

	    String equipmentCodesFull = flightCard.findElement(By.xpath(".//*[contains(@class, 'flight-equipment')]")).getText()
	        .replace("Equipment:", "").trim();
	    flightInfo.put("equipmentCodesFull", equipmentCodesFull);
	    flightInfo.put("equipmentCodes", equipmentCodesFull.split("\\s*,\\s*"));

	    String flightTypesFull = flightCard.findElement(By.xpath(".//*[contains(@class,'flight-operated-by')]")).getText()
	        .replace("Operated By: ", "").trim();
	    flightInfo.put("flightTypesFull", flightTypesFull);
	    flightInfo.put("flightTypes", flightTypesFull.split("\\s*,\\s*"));

	    String departTime = flightCard.findElement(By.xpath("//span[contains(@class,'flight-deptime')]")).getText();
	    String arrivalTime = flightCard.findElement(By.xpath(".//span[contains(@class,'flight-arrtime')]")).getText();

	    flightInfo.put("departTime", departTime);
	    flightInfo.put("arrivalTime", arrivalTime);
	    
	    
	    String onwardLocation=flightCard.findElement(By.xpath("//span[contains(@class,'flight-origin')]")).getText();
		String returnLocation=flightCard.findElement(By.xpath("//span[contains(@class,'flight-destination')]")).getText();
		String depatureDate=flightCard.findElement(By.xpath("//span[@class='flight-depdate']")).getText();
		String arrivalDate=flightCard.findElement(By.xpath("//span[@class='flight-arrdate']")).getText();
		String price=flightCard.findElement(By.xpath("//span[contains(@class,'flight-totalfq')]")).getText();
		flightInfo.put("onwardLocation", onwardLocation);
		flightInfo.put("returnLocation", returnLocation);
		flightInfo.put("depatureDate", depatureDate);
		flightInfo.put("arrivalDate", arrivalDate);
		flightInfo.put("price", price);
		
		
	    return flightInfo;
	}

	
	
	
	
	
	
	
	public String selectFlightBasedOnIndexAndValidateAmenities(int index, ExtentTest test) {
	    try {
	        // Build XPath for the desired "View Price" button using the index
	        String viewPriceXPath = "(//*[contains(@class,'one-way-new-result-card')]//*[text()='View Price'])[" + index + "]";
	        WebElement viewPriceButton = driver.findElement(By.xpath(viewPriceXPath));

	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewPriceButton);
	        Thread.sleep(500);
	        viewPriceButton.click();
	        test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + index);
	        System.out.println("✅ Clicked 'View Price' for flight at index: " + index);

	        // Wait for the fare components section to be visible
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement fareMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='fare-components-list']")));

	        if (fareMenu.isDisplayed()) {
	            List<WebElement> fares = driver.findElements(By.xpath("//*[contains(@class,'fare-component-watermark')]"));

	            if (!fares.isEmpty()) {
	                WebElement firstFare = fares.get(0);

	                // Look for the Book now button within the selected fare block
	                WebElement bookNowBtn = firstFare.findElement(By.xpath(".//*[text()='Book now']"));

	                // Find Amenities link (relative to the firstFare, not globally)
	                List<WebElement> amenities = firstFare.findElements(By.xpath(".//a[text()='Amenities']"));
	                if (!amenities.isEmpty()) {
	                    WebElement firstAmenities = amenities.get(0);
	                    firstAmenities.click();

	                    // Wait for the amenities popup to be visible
	                    WebElement amenitiesPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                        By.xpath("//div[contains(@class,'modal-content') and contains(.,'Amenities')]")
	                    ));

	                    if (amenitiesPopup.isDisplayed()) {
	                        test.log(Status.PASS, "✅ Amenities popup is displayed for flight index: " + index);

	                        WebElement popupTable = amenitiesPopup.findElement(By.xpath(".//table[contains(@class,'table-bordered')]"));

	                        if (popupTable.isDisplayed()) {
	                            test.log(Status.PASS, "✅ Amenities table is displayed for flight index: " + index);
	                            amenitiesPopup.findElement(By.xpath("//button[@class='btn-close']")).click();
	                            Thread.sleep(2000);
	                            String totalPrice=firstFare.findElement(By.xpath("//*[contains(@class,'fare-totalfq')]")).getText();
	                            
	                            bookNowBtn.click();
	                            return totalPrice;
	                        } else {
	                            test.log(Status.FAIL, "❌ Amenities table not displayed.");
	                            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Amenities Table Missing", "AmenitiesTableFail");
	                        }
	                    } else {
	                        test.log(Status.FAIL, "❌ Amenities popup not displayed.");
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Amenities Popup Missing", "AmenitiesPopupFail");
	                    }
	                } else {
	                    test.log(Status.INFO, "ℹ️ No 'Amenities' link found for this fare.");
	                }

	                test.log(Status.PASS, "✅ 'Book now' button found for selected flight.");
	            } else {
	                test.log(Status.FAIL, "❌ No fare options available.");
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No fares found", "FareOptionsMissing");
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Exception while selecting flight: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Exception while selecting flight: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Selection Failure", "FlightSelectException");
	    }
		return null;
	}

	/*
	public void selectAirline(String airlineNames) throws InterruptedException
	{
		WebElement showMore=driver.findElement(By.xpath("//div[text()='Search By Airlines']/parent::div//a[text()='Show More']"));
		
		if(showMore.isDisplayed())
		{
			showMore.click();
		}
		else
		{
			System.out.println("show more option not available");
		}
		Thread.sleep(30000);
		ArrayList<String> airline = new ArrayList<>();
		
		List<WebElement> listOfAirline = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));
		for(WebElement getlistOfAirline:listOfAirline)
		{
			String airlineName = getlistOfAirline.getText();
			String[] airlineNameSplit1 = airlineName.split("\\(");
			String airlineNameText = airlineNameSplit1[0].trim();
			airline.add(airlineNameText.toLowerCase());
	    	
		}
		System.out.println(airline);
		
		if(airline.contains(airlineNames))
		{
			
		System.out.println("user needed airline found");
		driver.findElement(By.xpath("//div[text()='Search By Airlines']/parent::div//label[text()='"+airlineNames+"']")).click();
		
	}
		else
		{
			listOfAirline.get(0).click();
			
		}
		for (WebElement label : listOfAirline) {
	        WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));
	        if (checkbox.isSelected()) {
	            System.out.println("Selected airline: " + label.getText().trim());
	        }
	    }
	}
	*/
	public void selectAirline(String airlineNameInput) throws InterruptedException {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        // Expand the "Show More" section if available
	        List<WebElement> showMoreButtons = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//a[text()='Show More']"));
	        if (!showMoreButtons.isEmpty() && showMoreButtons.get(0).isDisplayed()) {
	            showMoreButtons.get(0).click();
	            Thread.sleep(2000); // allow list to expand
	        } else {
	            System.out.println("ℹ️ 'Show More' option not available.");
	        }

	        // Wait for airline list to load
	        Thread.sleep(2000); // You can replace this with a more dynamic wait if required

	        List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));
	        ArrayList<String> availableAirlines = new ArrayList<>();

	        boolean airlineFound = false;

	        for (WebElement label : airlineLabels) {
	            String fullText = label.getText();
	            String nameOnly = fullText.split("\\(")[0].trim().toLowerCase();
	            availableAirlines.add(nameOnly);

	            if (nameOnly.equals(airlineNameInput.toLowerCase())) {
	                label.click();
	                System.out.println("✅ Selected airline: " + nameOnly);
	                airlineFound = true;
	                break;
	            }
	        }

	        if (!airlineFound) {
	            System.out.println("❌ Requested airline '" + airlineNameInput + "' not found. Selecting the first available airline instead.");
	            if (!airlineLabels.isEmpty()) {
	                airlineLabels.get(0).click();
	            }
	        }

	        // Print selected airlines (optional)
	        for (WebElement label : airlineLabels) {
	            WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));
	            if (checkbox.isSelected()) {
	                System.out.println("✔ Selected airline: " + label.getText().trim());
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Exception in selectAirline(): " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	////////////////
	private int currentIndex = 0; // Track current selection index (add this in your class)

	public void selectAirlineSequentially(boolean moveToNext) throws InterruptedException {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        // Expand the "Show More" section if available
	        List<WebElement> showMoreButtons = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//a[text()='Show More']"));
	        if (!showMoreButtons.isEmpty() && showMoreButtons.get(0).isDisplayed()) {
	            showMoreButtons.get(0).click();
	            Thread.sleep(2000); // Wait for list to expand
	        }

	        Thread.sleep(2000); // Optional: can replace with dynamic wait

	        List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));

	        // Ensure list is not empty
	        if (airlineLabels.isEmpty()) {
	            System.out.println("❌ No airlines found.");
	            return;
	        }

	        // If moveToNext is true, unselect current and move to next
	        if (moveToNext && currentIndex < airlineLabels.size()) {
	            WebElement previousLabel = airlineLabels.get(currentIndex);
	            WebElement prevCheckbox = previousLabel.findElement(By.xpath(".//input[@type='checkbox']"));
	            if (prevCheckbox.isSelected()) {
	                previousLabel.click(); // Uncheck previous
	                System.out.println("🔁 Unselected previous airline: " + previousLabel.getText().trim());
	            }

	            currentIndex++; // Move to next
	        }

	        // Wrap around if index exceeds list
	        if (currentIndex >= airlineLabels.size()) {
	            currentIndex = 0;
	            System.out.println("🔁 Reached end of list, starting from beginning.");
	        }

	        // Select current index
	        WebElement currentLabel = airlineLabels.get(currentIndex);
	        currentLabel.click();
	        System.out.println("✅ Selected airline: " + currentLabel.getText().trim());

	        // Print selected airlines (optional)
	        for (WebElement label : airlineLabels) {
	            WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));
	            if (checkbox.isSelected()) {
	                System.out.println("✔ Currently selected airline: " + label.getText().trim());
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Exception in selectAirlineSequentially(): " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	public void selectIndexBasedOnSuppilerName()
	{
		List<WebElement>flightCount=driver.findElements(By.xpath("//div[contains(@class,'one-way-new-result-card')]"));
		int size=flightCount.size();
		for(int i=0;i<size;i++)
		{
			WebElement flightcard=flightCount.get(i);
			flightcard.findElement(By.xpath("//a[text()='View Price']")).click();
			List<WebElement>suppiler=flightcard.findElements(By.xpath("//div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')]//span[text()='Supplier']/following-sibling::span"));
			if(!suppiler.isEmpty())
			{
				
			}
		}
	}
	/*
	public void checkSupplierAndDuplicateWithReport(WebDriver driver, ExtentTest test, String targetSupplier) throws InterruptedException {
	    Set<String> suppliersFound = new HashSet<>();
	    boolean supplierMatched = false;

	    List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));

	    for (int i = 0; i < airlineLabels.size(); i++) {
	        WebElement airline = airlineLabels.get(i);
	        String airlineName = airline.getText().trim();

	        airline.click();
	        test.log(Status.INFO, "✅ Selected airline: " + airlineName);
	        Thread.sleep(3000); // Wait for results to load

	        List<WebElement> flightCards = driver.findElements(By.xpath("//div[contains(@class,'one-way-new-result-card')]"));

	        for (int j = 0; j < flightCards.size(); j++) {
	            WebElement flightCard = flightCards.get(j);

	            try {
	                WebElement viewPrice = flightCard.findElement(By.xpath(".//a[text()='View Price']"));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPrice);
	                test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + j);
	                Thread.sleep(4000);

	                List<WebElement> fareCards = flightCard.findElements(By.xpath("./following-sibling::div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')]"));
	                List<String> fareKeys = new ArrayList<>();
	                
	                System.out.println(fareCards.size());

	                for (int k = 0; k < fareCards.size(); k++) {
	                    WebElement fare = fareCards.get(k);
	                    String supplier = getText(fare, ".//span[text()='Supplier']/following-sibling::span");
	                    System.out.println(supplier);
	                    suppliersFound.add(supplier);

	                    if (!supplier.equalsIgnoreCase(targetSupplier)) continue;

	                    supplierMatched = true;

	                    String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
	                    String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
	                    String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
	                    String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
	                    String refundable = getText(fare, ".//span[contains(@class,'fare_refundable')]");
	                    String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

	                    String uniqueKey = fareType + "|" + farePrice + "|" + checkin + "|" + cabin + "|" + refundable + "|" + fareClass;

	                    if (fareKeys.contains(uniqueKey)) {
	                        test.log(Status.FAIL, "⚠️ Duplicate fare card found [Flight: " + j + ", Fare: " + k + "] - " +
	                                "Type: " + fareType + ", Price: AED " + farePrice);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Duplicate Fare", "DuplicateFare_" + j + "_" + k);
	                    } else {
	                        fareKeys.add(uniqueKey);
	                        test.log(Status.PASS, "✅ Unique fare found [Flight: " + j + ", Fare: " + k + "] - " +
	                                "Type: " + fareType + ", Price: AED " + farePrice);
	                    }
	                }

	            } catch (Exception e) {
	                test.log(Status.FAIL, "❌ Error on flight card index " + j + ": " + e.getMessage());
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Card Error", "ErrorCard_" + j);
	            }
	        }

	        // If supplier not matched in this airline, move to next
	        if (!supplierMatched) {
	            test.log(Status.WARNING, "❌ Supplier '" + targetSupplier + "' not found in airline: " + airlineName);
	            airline.click(); // Unselect current
	        } else {
	            break; // Supplier found, no need to check more airlines
	        }
	    }

	    // Final fallback if supplier was never found
	    if (!supplierMatched) {
	        test.log(Status.FAIL, "❌ Supplier '" + targetSupplier + "' NOT available for this route.");
	        test.log(Status.INFO, "📋 Suppliers found instead: " + String.join(", ", suppliersFound));
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO, "Available Suppliers", "SuppliersList");
	    }
	}
*/
	/* ******imp**********1
	public void checkSupplierAndDuplicateWithReport(WebDriver driver, ExtentTest test, String targetSupplier) throws InterruptedException {
	    Set<String> suppliersFound = new HashSet<>();
	    boolean supplierMatched = false;
	    boolean stopProcessing = false;

	    List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));

	    for (int i = 0; i < airlineLabels.size(); i++) {
	        WebElement airline = airlineLabels.get(i);
	        String airlineName = airline.getText().trim();

	        airline.click();
	        test.log(Status.INFO, "✅ Selected airline: " + airlineName);
	        Thread.sleep(3000);

	        List<WebElement> flightCards = driver.findElements(By.xpath("//div[contains(@class,'one-way-new-result-card')]"));

	        for (int j = 0; j < flightCards.size(); j++) {
	            WebElement flightCard = flightCards.get(j);

	            try {
	                WebElement viewPrice = flightCard.findElement(By.xpath(".//a[text()='View Price']"));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPrice);
	                test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + j);
	                Thread.sleep(4000);

	                List<WebElement> fareCards = flightCard.findElements(By.xpath("./following-sibling::div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')]"));
	                List<String> fareKeys = new ArrayList<>();

	                boolean supplierFoundInThisFlightCard = false;

	                for (int k = 0; k < fareCards.size(); k++) {
	                    WebElement fare = fareCards.get(k);
	                    String supplier = getText(fare, ".//span[text()='Supplier']/following-sibling::span");
	                    suppliersFound.add(supplier);

	                    if (!supplier.equalsIgnoreCase(targetSupplier)) continue;

	                    // Supplier matched
	                    supplierMatched = true;
	                    supplierFoundInThisFlightCard = true;

	                    // Now validate all fare cards under this flight card for duplicates
	                    String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
	                    String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
	                    String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
	                    String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
	                    String refundable = getText(fare, ".//span[contains(@class,'fare_refundable')]");
	                    String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

	                    String uniqueKey = fareType + "|" + farePrice + "|" + checkin + "|" + cabin + "|" + refundable + "|" + fareClass;

	                    if (fareKeys.contains(uniqueKey)) {
	                        test.log(Status.FAIL, "⚠️ Duplicate fare card found [Flight: " + j + ", Fare: " + k + "] - " +
	                                "Type: " + fareType + ", Price: AED " + farePrice);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Duplicate Fare", "DuplicateFare_" + j + "_" + k);
	                    } else {
	                        fareKeys.add(uniqueKey);
	                        test.log(Status.PASS, "✅ Unique fare found [Flight: " + j + ", Fare: " + k + "] - " +
	                                "Type: " + fareType + ", Price: AED " + farePrice);
	                    }
	                }

	                // If supplier was found in this flight card, no need to check next flight cards
	                if (supplierFoundInThisFlightCard) {
	                    stopProcessing = true;
	                    break; // exit flight card loop
	                }

	            } catch (Exception e) {
	                test.log(Status.FAIL, "❌ Error on flight card index " + j + ": " + e.getMessage());
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Card Error", "ErrorCard_" + j);
	            }
	        }

	        if (!supplierMatched) {
	            test.log(Status.WARNING, "❌ Supplier '" + targetSupplier + "' not found in airline: " + airlineName);
	            airline.click(); // Unselect current
	        }

	        if (stopProcessing) break; // No need to go to next airline
	    }

	    if (!supplierMatched) {
	        test.log(Status.FAIL, "❌ Supplier '" + targetSupplier + "' NOT available for this route.");
	        test.log(Status.INFO, "📋 Suppliers found instead: " + String.join(", ", suppliersFound));
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO, "Available Suppliers", "SuppliersList");
	    }
	}

	private String getText(WebElement parent, String xpath) {
	    try {
	        return parent.findElement(By.xpath(xpath)).getText().trim();
	    } catch (NoSuchElementException e) {
	        return "";
	    }
	}
*/
	/*
	public void checkSupplierAndDuplicateWithReport(WebDriver driver, ExtentTest test, String targetSupplier) throws InterruptedException {
	    Set<String> suppliersFound = new HashSet<>();
	    boolean supplierMatched = false;
	    boolean stopProcessing = false;

	    List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));

	    for (int i = 0; i < airlineLabels.size(); i++) {
	        WebElement airline = airlineLabels.get(i);
	        String airlineName = airline.getText().trim();

	        airline.click();
	        test.log(Status.INFO, "✅ Selected airline: " + airlineName);
	        Thread.sleep(3000); // Wait for flight results to load

	        List<WebElement> flightCards = driver.findElements(By.xpath("//div[contains(@class,'one-way-new-result-card')]"));

	        for (int j = 0; j < flightCards.size(); j++) {
	            WebElement flightCard = flightCards.get(j);

	            try {
	                WebElement viewPrice = flightCard.findElement(By.xpath(".//a[text()='View Price']"));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPrice);
	                test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + j);
	                Thread.sleep(4000); // Wait for fare cards to appear

	                List<WebElement> fareCards = flightCard.findElements(By.xpath("./following-sibling::div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')]"));
	                List<String> fareKeys = new ArrayList<>();
	                boolean supplierFoundInThisFlightCard = false;

	                for (int k = 0; k < fareCards.size(); k++) {
	                    WebElement fare = fareCards.get(k);
	                    String supplier = getText(fare, ".//span[text()='Supplier']/following-sibling::span").trim();
	                    suppliersFound.add(supplier);

	                    // Handle missing supplier
	                    if (supplier.isEmpty()) {
	                        String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
	                        String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
	                        String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
	                        String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
	                        String refundable = getText(fare, ".//span[contains(@class,'fare_refundable')]");
	                        String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

	                        test.log(Status.WARNING, "⚠️ Missing supplier name in airline: **" + airlineName + "**, Flight index: " + j + ", Fare index: " + k +
	                                "\n➡️ Fare Details: Type = " + fareType + ", Price = AED " + farePrice + ", Check-in = " + checkin +
	                                ", Cabin = " + cabin + ", Refundable = " + refundable + ", Class = " + fareClass);

	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.WARNING, "Missing Supplier", "MissingSupplier_" + j + "_" + k);
	                        continue;
	                    }

	                    // Skip if not the target supplier
	                    if (!supplier.equalsIgnoreCase(targetSupplier)) continue;

	                    supplierMatched = true;
	                    supplierFoundInThisFlightCard = true;

	                    // Validate duplicate
	                    String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
	                    String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
	                    String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
	                    String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
	                    String refundable = getText(fare, ".//span[contains(@class,'fare_refundable')]");
	                    String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

	                    String uniqueKey = fareType + "|" + farePrice + "|" + checkin + "|" + cabin + "|" + refundable + "|" + fareClass;

	                    if (fareKeys.contains(uniqueKey)) {
	                        test.log(Status.FAIL, "⚠️ Duplicate fare card found [Flight: " + j + ", Fare: " + k + "] - " +
	                                "Type: " + fareType + ", Price: AED " + farePrice);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Duplicate Fare", "DuplicateFare_" + j + "_" + k);
	                    } else {
	                        fareKeys.add(uniqueKey);
	                        test.log(Status.PASS, "✅ Unique fare found [Flight: " + j + ", Fare: " + k + "] - " +
	                                "Type: " + fareType + ", Price: AED " + farePrice);
	                    }
	                }

	                if (supplierFoundInThisFlightCard) {
	                    stopProcessing = true;
	                    break; // Stop checking other flight cards
	                }

	            } catch (Exception e) {
	                test.log(Status.FAIL, "❌ Error on flight card index " + j + ": " + e.getMessage());
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Card Error", "ErrorCard_" + j);
	            }
	        }

	        // If supplier not found for this airline
	        if (!supplierMatched) {
	            test.log(Status.WARNING, "❌ Supplier '" + targetSupplier + "' not found in airline: " + airlineName);
	            airline.click(); // Unselect current
	        }

	        if (stopProcessing) break; // Supplier found, stop checking other airlines
	    }

	    // Final check if supplier never matched
	    if (!supplierMatched) {
	        test.log(Status.FAIL, "❌ Supplier '" + targetSupplier + "' NOT available for this route.");
	        test.log(Status.INFO, "📋 Suppliers found instead: " + String.join(", ", suppliersFound));
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO, "Available Suppliers", "SuppliersList");
	    }
	}

	// Helper method to safely get text from element
	private String getText(WebElement parent, String xpath) {
	    try {
	        return parent.findElement(By.xpath(xpath)).getText().trim();
	    } catch (NoSuchElementException e) {
	        return "";
	    }
	}
*/
	//**********imp************2
	/*
	public int checkSupplierAndDuplicateWithReport(WebDriver driver, ExtentTest test, String targetSupplier) throws InterruptedException {
	    Set<String> suppliersFound = new HashSet<>();
	    boolean supplierMatched = false;
	    boolean stopProcessing = false;
	    int matchedFlightIndex = -1; // To return the flight card index if supplier is found

	    List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));

	    for (int i = 0; i < airlineLabels.size(); i++) {
	        WebElement airline = airlineLabels.get(i);
	        String airlineName = airline.getText().trim();

	        airline.click();
	        test.log(Status.INFO, "✅ Selected airline: " + airlineName);
	        Thread.sleep(3000); // Wait for flight results to load

	        List<WebElement> flightCards = driver.findElements(By.xpath("//div[contains(@class,'one-way-new-result-card')]"));

	        for (int j = 0; j < flightCards.size(); j++) {
	            WebElement flightCard = flightCards.get(j);

	            try {
	                WebElement viewPrice = flightCard.findElement(By.xpath(".//a[text()='View Price']"));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPrice);
	                test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + j);
	                Thread.sleep(4000); // Wait for fare cards to load

	                List<WebElement> fareCards = flightCard.findElements(By.xpath("./following-sibling::div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')]"));
	                List<String> fareKeys = new ArrayList<>();
	                boolean supplierFoundInThisFlightCard = false;

	                for (int k = 0; k < fareCards.size(); k++) {
	                    WebElement fare = fareCards.get(k);
	                    String supplier = getText(fare, ".//span[text()='Supplier']/following-sibling::span").trim();
	                    suppliersFound.add(supplier);

	                    // Handle missing supplier name
	                    if (supplier.isEmpty()) {
	                        String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
	                        String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
	                        String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
	                        String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
	                        String refundable = getText(fare, ".//span[contains(@class,'fare_refundable')]");
	                        String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

	                        test.log(Status.WARNING, "⚠️ Missing supplier name in airline: **" + airlineName + "**, Flight index: " + j + ", Fare index: " + k +
	                                "\n➡️ Fare Details: Type = " + fareType + ", Price = AED " + farePrice + ", Check-in = " + checkin +
	                                ", Cabin = " + cabin + ", Refundable = " + refundable + ", Class = " + fareClass);

	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.WARNING, "Missing Supplier", "MissingSupplier_" + j + "_" + k);
	                        continue;
	                    }

	                    // Skip if supplier is not the target
	                    if (!supplier.equalsIgnoreCase(targetSupplier)) continue;

	                    supplierMatched = true;
	                    supplierFoundInThisFlightCard = true;
	                    matchedFlightIndex = j; // Set return value

	                    // Extract other fare details
	                    String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
	                    String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
	                    String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
	                    String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
	                    String refundable = getText(fare, ".//span[contains(@class,'fare_refundable')]");
	                    String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

	                    // Create unique key to detect duplicates
	                    String uniqueKey = fareType + "|" + farePrice + "|" + checkin + "|" + cabin + "|" + refundable + "|" + fareClass;

	                    if (fareKeys.contains(uniqueKey)) {
	                        test.log(Status.FAIL, "⚠️ Duplicate fare card found [Flight: " + j + ", Fare: " + k + "] - " +
	                                "Type: " + fareType + ", Price: AED " + farePrice);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Duplicate Fare", "DuplicateFare_" + j + "_" + k);
	                    } else {
	                        fareKeys.add(uniqueKey);
	                        test.log(Status.PASS, "✅ Unique fare found [Flight: " + j + ", Fare: " + k + "] - " +
	                                "Type: " + fareType + ", Price: AED " + farePrice);
	                    }
	                }

	                if (supplierFoundInThisFlightCard) {
	                    stopProcessing = true;
	                    break; // No need to check more flights
	                }

	            } catch (Exception e) {
	                test.log(Status.FAIL, "❌ Error on flight card index " + j + ": " + e.getMessage());
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Card Error", "ErrorCard_" + j);
	            }
	        }

	        if (!supplierMatched) {
	            test.log(Status.WARNING, "❌ Supplier '" + targetSupplier + "' not found in airline: " + airlineName);
	            airline.click(); // Unselect airline
	        }

	        if (stopProcessing) break; // Exit outer loop too
	    }

	    if (!supplierMatched) {
	        test.log(Status.FAIL, "❌ Supplier '" + targetSupplier + "' NOT available for this route.");
	        test.log(Status.INFO, "📋 Suppliers found instead: " + String.join(", ", suppliersFound));
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO, "Available Suppliers", "SuppliersList");
	    }

	    return matchedFlightIndex;
	}
	private String getText(WebElement parent, String xpath) {
	    try {
	        return parent.findElement(By.xpath(xpath)).getText().trim();
	    } catch (NoSuchElementException e) {
	        return "";
	    }
	}
	*/
	/*//it will return only one fare card 
	public Map<String, String> checkSupplierAndDuplicateWithReport(WebDriver driver, ExtentTest test, String targetSupplier) throws InterruptedException {
	    Set<String> suppliersFound = new HashSet<>();
	    boolean supplierMatched = false;

	    List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));

	    for (int i = 0; i < airlineLabels.size(); i++) {
	        WebElement airline = airlineLabels.get(i);
	        String airlineName = airline.getText().trim();

	        airline.click();
	        test.log(Status.INFO, "✅ Selected airline: " + airlineName);
	        Thread.sleep(3000); // Wait for results

	        List<WebElement> flightCards = driver.findElements(By.xpath("//div[contains(@class,'one-way-new-result-card')]"));

	        for (int j = 0; j < flightCards.size(); j++) {
	            WebElement flightCard = flightCards.get(j);

	            try {
	                WebElement viewPrice = flightCard.findElement(By.xpath(".//a[text()='View Price']"));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPrice);
	                test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + j);
	                Thread.sleep(4000); // Wait for fare cards to load

	                List<WebElement> fareCards = flightCard.findElements(By.xpath("./following-sibling::div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')]"));
	                List<String> fareKeys = new ArrayList<>();

	                for (int k = 0; k < fareCards.size(); k++) {
	                    WebElement fare = fareCards.get(k);
	                    String supplier = getText(fare, ".//span[text()='Supplier']/following-sibling::span").trim();
	                    suppliersFound.add(supplier);

	                    // Log if supplier name is missing
	                    if (supplier.isEmpty()) {
	                        String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
	                        String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
	                        String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
	                        String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
	                        String refundable = getText(fare, ".//span[contains(@class,'fare_refundable')]");
	                        String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

	                        test.log(Status.WARNING, "⚠️ Missing supplier in airline: " + airlineName + ", Flight: " + j + ", Fare: " + k +
	                                "\nFare Type: " + fareType + ", Price: AED " + farePrice + ", Check-in: " + checkin + ", Cabin: " + cabin +
	                                ", Refundable: " + refundable + ", Class: " + fareClass);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.WARNING, "Missing Supplier", "MissingSupplier_" + j + "_" + k);
	                        continue;
	                    }

	                    if (!supplier.equalsIgnoreCase(targetSupplier)) continue;

	                    // Match found
	                    supplierMatched = true;

	                    String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
	                    String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
	                    String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
	                    String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
	                    String refundable = getText(fare, ".//span[contains(@class,'fare_refundable')]");
	                    String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

	                    String uniqueKey = fareType + "|" + farePrice + "|" + checkin + "|" + cabin + "|" + refundable + "|" + fareClass;

	                    if (fareKeys.contains(uniqueKey)) {
	                        test.log(Status.FAIL, "⚠️ Duplicate fare card found [Flight: " + j + ", Fare: " + k + "] - Type: " + fareType + ", Price: AED " + farePrice);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Duplicate Fare", "DuplicateFare_" + j + "_" + k);
	                    } else {
	                        fareKeys.add(uniqueKey);
	                        test.log(Status.PASS, "✅ Unique fare found [Flight: " + j + ", Fare: " + k + "] - Type: " + fareType + ", Price: AED " + farePrice);
	                    }

	                    // Return all details in a map
	                    Map<String, String> result = new LinkedHashMap<>();
	                    result.put("flightIndex", String.valueOf(j));
	                    result.put("fareIndex", String.valueOf(k));
	                    result.put("supplier", supplier);
	                    result.put("fareType", fareType);
	                    result.put("price", farePrice);
	                    result.put("checkin", checkin);
	                    result.put("cabin", cabin);
	                    result.put("refundable", refundable);
	                    result.put("fareClass", fareClass);
	                    return result;
	                }

	            } catch (Exception e) {
	                test.log(Status.FAIL, "❌ Error on flight card index " + j + ": " + e.getMessage());
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Card Error", "ErrorCard_" + j);
	            }
	        }

	        if (!supplierMatched) {
	            test.log(Status.WARNING, "❌ Supplier '" + targetSupplier + "' not found in airline: " + airlineName);
	            airline.click(); // Unselect current
	        } else {
	            break; // Supplier already found
	        }
	    }

	    if (!supplierMatched) {
	        test.log(Status.FAIL, "❌ Supplier '" + targetSupplier + "' NOT available for this route.");
	        test.log(Status.INFO, "📋 Suppliers found instead: " + String.join(", ", suppliersFound));
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO, "Available Suppliers", "SuppliersList");
	    }

	    return null; // Supplier not found
	}

	// Helper to extract text safely
	private String getText(WebElement parent, String xpath) {
	    try {
	        return parent.findElement(By.xpath(xpath)).getText().trim();
	    } catch (NoSuchElementException e) {
	        return "";
	    }
	}
*/
	//it will return multiple fare cards and check for all flightcard
	/*
	public List<Map<String, String>> checkSupplierAndDuplicateWithReport(WebDriver driver, ExtentTest test, String targetSupplier) throws InterruptedException {
	    Set<String> suppliersFound = new HashSet<>();
	    boolean supplierMatched = false;
	    List<Map<String, String>> matchingFares = new ArrayList<>();

	    List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));

	    for (int i = 0; i < airlineLabels.size(); i++) {
	        WebElement airline = airlineLabels.get(i);
	        String airlineName = airline.getText().trim();

	        airline.click();
	        test.log(Status.INFO, "✅ Selected airline: " + airlineName);
	        Thread.sleep(3000);

	        List<WebElement> flightCards = driver.findElements(By.xpath("//div[contains(@class,'one-way-new-result-card')]"));

	        for (int j = 0; j < flightCards.size(); j++) {
	            WebElement flightCard = flightCards.get(j);

	            try {
	                WebElement viewPrice = flightCard.findElement(By.xpath(".//a[text()='View Price']"));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPrice);
	                test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + j);
	                Thread.sleep(4000); // Allow fare cards to load

	                List<WebElement> fareCards = flightCard.findElements(By.xpath("./following-sibling::div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')]"));
	                List<String> fareKeys = new ArrayList<>();

	                for (int k = 0; k < fareCards.size(); k++) {
	                    WebElement fare = fareCards.get(k);
	                    String supplier = getText(fare, ".//span[text()='Supplier']/following-sibling::span").trim();
	                    suppliersFound.add(supplier);

	                    if (supplier.isEmpty()) {
	                        String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
	                        String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
	                        String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
	                        String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
	                        String refundable = getText(fare, ".//span[contains(@class,'fare_refundable')]");
	                        String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

	                        test.log(Status.WARNING, "⚠️ Missing supplier in airline: " + airlineName + ", Flight: " + j + ", Fare: " + k +
	                                "\nFare Type: " + fareType + ", Price: AED " + farePrice + ", Check-in: " + checkin + ", Cabin: " + cabin +
	                                ", Refundable: " + refundable + ", Class: " + fareClass);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.WARNING, "Missing Supplier", "MissingSupplier_" + j + "_" + k);
	                        continue;
	                    }

	                    if (!supplier.equalsIgnoreCase(targetSupplier)) continue;

	                    supplierMatched = true;

	                    String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
	                    String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
	                    String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
	                    String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
	                    String refundable = getText(fare, ".//span[contains(@class,'fare_refundable')]");
	                    String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

	                    String uniqueKey = fareType + "|" + farePrice + "|" + checkin + "|" + cabin + "|" + refundable + "|" + fareClass;

	                    if (fareKeys.contains(uniqueKey)) {
	                        test.log(Status.FAIL, "⚠️ Duplicate fare card found [Flight: " + j + ", Fare: " + k + "] - Type: " + fareType + ", Price: AED " + farePrice);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Duplicate Fare", "DuplicateFare_" + j + "_" + k);
	                    } else {
	                        fareKeys.add(uniqueKey);
	                        test.log(Status.PASS, "✅ Unique fare found [Flight: " + j + ", Fare: " + k + "] - Type: " + fareType + ", Price: AED " + farePrice);
	                    }

	                    // Store matching fare
	                    Map<String, String> result = new LinkedHashMap<>();
	                    result.put("flightIndex", String.valueOf(j));
	                    result.put("fareIndex", String.valueOf(k));
	                    result.put("supplier", supplier);
	                    result.put("fareType", fareType);
	                    result.put("price", farePrice);
	                    result.put("checkin", checkin);
	                    result.put("cabin", cabin);
	                    result.put("refundable", refundable);
	                    result.put("fareClass", fareClass);
	                    matchingFares.add(result);
	                }

	            } catch (Exception e) {
	                test.log(Status.FAIL, "❌ Error on flight card index " + j + ": " + e.getMessage());
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Card Error", "ErrorCard_" + j);
	            }
	        }

	        if (!supplierMatched) {
	            test.log(Status.WARNING, "❌ Supplier '" + targetSupplier + "' not found in airline: " + airlineName);
	            airline.click(); // Unselect current
	        } else {
	            break; // Stop checking more airlines once supplier is found
	        }
	    }

	    if (!supplierMatched) {
	        test.log(Status.FAIL, "❌ Supplier '" + targetSupplier + "' NOT available for this route.");
	        test.log(Status.INFO, "📋 Suppliers found instead: " + String.join(", ", suppliersFound));
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO, "Available Suppliers", "SuppliersList");
	    }

	    return matchingFares;
	}
	// Helper to extract text safely
		private String getText(WebElement parent, String xpath) {
		    try {
		        return parent.findElement(By.xpath(xpath)).getText().trim();
		    } catch (NoSuchElementException e) {
		        return "";
		    }
		}
		*/
	//This will check only 3 resultcard or less
	/*
		public List<Map<String, String>> checkSupplierAndDuplicateWithReport(WebDriver driver, ExtentTest test, String targetSupplier) throws InterruptedException {
		    Set<String> suppliersFound = new HashSet<>();
		    boolean supplierMatched = false;
		    List<Map<String, String>> matchingFares = new ArrayList<>();

		    List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));

		    for (int i = 0; i < airlineLabels.size(); i++) {
		        WebElement airline = airlineLabels.get(i);
		        String airlineName = airline.getText().trim();

		        airline.click();
		        test.log(Status.INFO, "✅ Selected airline: " + airlineName);
		        Thread.sleep(3000);

		        List<WebElement> flightCards = driver.findElements(By.xpath("//*[contains(@class,'one-way-new-result-card')]"));

		        // Limit to first 3 flight cards or less if fewer exist
		        for (int j = 0; j < Math.min(3, flightCards.size()); j++) {
		            WebElement flightCard = flightCards.get(j);

		            try {
		                WebElement viewPrice = flightCard.findElement(By.xpath(".//a[text()='View Price']"));
		                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPrice);
		                test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + j);
		                Thread.sleep(4000); // Allow fare cards to load

		                List<WebElement> fareCards = flightCard.findElements(By.xpath("./following-sibling::div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')]"));
		                List<String> fareKeys = new ArrayList<>();

		                for (int k = 0; k < fareCards.size(); k++) {
		                    WebElement fare = fareCards.get(k);
		                    String supplier = getText(fare, ".//span[text()='Supplier']/following-sibling::span").trim();
		                    suppliersFound.add(supplier);

		                    if (supplier.isEmpty()) {
		                        String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
		                        String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
		                        String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
		                        String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
		                        String refundable = getText(fare, ".//span[contains(@class,'fare_refundable')]");
		                        String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

		                        test.log(Status.WARNING, "⚠️ Missing supplier in airline: " + airlineName + ", Flight: " + j + ", Fare: " + k +
		                                "\nFare Type: " + fareType + ", Price: AED " + farePrice + ", Check-in: " + checkin + ", Cabin: " + cabin +
		                                ", Refundable: " + refundable + ", Class: " + fareClass);
		                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.WARNING, "Missing Supplier", "MissingSupplier_" + j + "_" + k);
		                        continue;
		                    }

		                    if (!supplier.equalsIgnoreCase(targetSupplier)) continue;

		                    supplierMatched = true;

		                    String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
		                    String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
		                    String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
		                    String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
		                    String refundable = getText(fare, ".//span[contains(@class,'fare_refundable')]");
		                    String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

		                    String uniqueKey = fareType + "|" + farePrice + "|" + checkin + "|" + cabin + "|" + refundable + "|" + fareClass;

		                    if (fareKeys.contains(uniqueKey)) {
		                        test.log(Status.FAIL, "⚠️ Duplicate fare card found [Flight: " + j + ", Fare: " + k + "] - Type: " + fareType + ", Price: AED " + farePrice);
		                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Duplicate Fare", "DuplicateFare_" + j + "_" + k);
		                    } else {
		                        fareKeys.add(uniqueKey);
		                        test.log(Status.PASS, "✅ Unique fare found [Flight: " + j + ", Fare: " + k + "] - Type: " + fareType + ", Price: AED " + farePrice);
		                    }

		                    // Store matching fare
		                    Map<String, String> result = new LinkedHashMap<>();
		                    result.put("flightIndex", String.valueOf(j));
		                    result.put("fareIndex", String.valueOf(k));
		                    result.put("supplier", supplier);
		                    result.put("fareType", fareType);
		                    result.put("price", farePrice);
		                    result.put("checkin", checkin);
		                    result.put("cabin", cabin);
		                    result.put("refundable", refundable);
		                    result.put("fareClass", fareClass);
		                    matchingFares.add(result);
		                }

		            } catch (Exception e) {
		                test.log(Status.FAIL, "❌ Error on flight card index " + j + ": " + e.getMessage());
		                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Card Error", "ErrorCard_" + j);
		            }
		        }

		        if (!supplierMatched) {
		            test.log(Status.WARNING, "❌ Supplier '" + targetSupplier + "' not found in airline: " + airlineName);
		            airline.click(); // Unselect current
		        } else {
		            break; // Stop checking more airlines once supplier is found
		        }
		    }

		    if (!supplierMatched) {
		        test.log(Status.FAIL, "❌ Supplier '" + targetSupplier + "' NOT available for this route.");
		        test.log(Status.INFO, "📋 Suppliers found instead: " + String.join(", ", suppliersFound));
		        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO, "Available Suppliers", "SuppliersList");
		    }

		    return matchingFares;
		}

		// Helper to extract text safely
		private String getText(WebElement parent, String xpath) {
		    try {
		        return parent.findElement(By.xpath(xpath)).getText().trim();
		    } catch (NoSuchElementException e) {
		        return "";
		    }
		}
		*/
	/*
	public List<Map<String, String>> checkSupplierAndDuplicateWithReport(WebDriver driver, ExtentTest test, String targetSupplier) throws InterruptedException {
	    Set<String> suppliersFound = new HashSet<>();
	    boolean supplierMatched = false;
	    List<Map<String, String>> matchingFares = new ArrayList<>();

	    List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));

	    for (int i = 0; i < airlineLabels.size(); i++) {
	        WebElement airline = airlineLabels.get(i);
	        String airlineName = airline.getText().trim();

	        airline.click();
	       // test.log(Status.INFO, "✅ Selected airline: " + airlineName);
	        Thread.sleep(3000);

	        List<WebElement> flightCards = driver.findElements(By.xpath("//*[contains(@class,'one-way-new-result-card')]"));

	        for (int j = 0; j < Math.min(3, flightCards.size()); j++) {
	            WebElement flightCard = flightCards.get(j);

	            try {
	                WebElement viewPrice = flightCard.findElement(By.xpath(".//a[text()='View Price']"));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPrice);
	                test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + j);
	                Thread.sleep(4000);

	                List<WebElement> fareCards = flightCard.findElements(By.xpath("./following-sibling::div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')]"));
	                List<String> fareKeys = new ArrayList<>();

	                for (int k = 0; k < fareCards.size(); k++) {
	                    WebElement fare = fareCards.get(k);

	                    // Extract all fields
	                    String supplier = getText(fare, ".//span[text()='Supplier']/following-sibling::span").trim();
	                    String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
	                    String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
	                    String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
	                    String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
	                 //   String refundable = getText(fare, ".//span[contains(@class,'fare_refundable')]");
	                       String refundable = getText(fare, ".//span[@class='fare-component_value']//span[contains(@class,'fare')]");
	                    String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

	                    // Add found supplier to list
	                    suppliersFound.add(supplier);

	                    // 🛑 Validate missing fields
	                    List<String> missingFields = new ArrayList<>();
	                    if (supplier.isEmpty()) missingFields.add("Supplier");
	                    if (fareType.isEmpty()) missingFields.add("Fare Type");
	                    if (farePrice.isEmpty()) missingFields.add("Price");
	                   // if (checkin.isEmpty()) missingFields.add("Check In");
	                //    if (checkin.isEmpty() || checkin.contains("0KG") || checkin.contains("0PC") || checkin.contains("0 Kg") || checkin.contains("0 Pc")) missingFields.add("Check In (Invalid)");
	                    if (checkin.isEmpty() || checkin.contains("0KG") || checkin.contains("0PC") || checkin.contains("0 Kg") || checkin.contains("0 Pc")) {
	                        missingFields.add("Check In (Invalid)");
	                    }

	                    if (cabin.isEmpty()) missingFields.add("Cabin");
	                    
	                    
	                   // if (refundable.isEmpty()) missingFields.add("Refundable");
	                    if (!(refundable.equalsIgnoreCase("Refundable") || refundable.equalsIgnoreCase("Non Refundable"))) {
	                        missingFields.add("Refundable");
	                    }
	                    if (fareClass.isEmpty()) missingFields.add("Class");

	                    if (!missingFields.isEmpty()) {
	                    	 test.log(Status.INFO, "✅ Selected airline: " + airlineName);
	                        String missingDetails = String.join(", ", missingFields);
	                        test.log(Status.FAIL, "❌ Missing fare details [Flight: " + j + ", Fare: " + k + "] ➤ Missing: " + missingDetails +
	                                "<br><b>Fare Type:</b> " + fareType + ", <b>Price:</b> AED " + farePrice +
	                                ", <b>Check-in:</b> " + checkin + ", <b>Cabin:</b> " + cabin +
	                                ", <b>Refundable:</b> " + refundable + ", <b>Class:</b> " + fareClass);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Missing Fare Fields", "MissingFields_" + j + "_" + k);
	                        continue; // skip this fare
	                    }

	                    // ✅ Proceed only if it's the target supplier
	                    if (!supplier.equalsIgnoreCase(targetSupplier)) continue;

	                    supplierMatched = true;

	                    // 🧭 Duplicate check
	                    String uniqueKey = fareType + "|" + farePrice + "|" + checkin + "|" + cabin + "|" + refundable + "|" + fareClass;
	                    if (fareKeys.contains(uniqueKey)) {
	                        test.log(Status.FAIL, "❌ Duplicate fare found [Flight: " + j + ", Fare: " + k + "] ➤ Type: " + fareType + ", Price: AED " + farePrice);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Duplicate Fare", "DuplicateFare_" + j + "_" + k);
	                    } else {
	                        fareKeys.add(uniqueKey);
	                        test.log(Status.PASS, "✅ Unique fare found [Flight: " + j + ", Fare: " + k + "] ➤ Type: " + fareType + ", Price: AED " + farePrice);
	                    }

	                    // ✅ Store valid fare
	                    Map<String, String> result = new LinkedHashMap<>();
	                    result.put("flightIndex", String.valueOf(j));
	                    result.put("fareIndex", String.valueOf(k));
	                    result.put("supplier", supplier);
	                    result.put("fareType", fareType);
	                    result.put("price", farePrice);
	                    result.put("checkin", checkin);
	                    result.put("cabin", cabin);
	                    result.put("refundable", refundable);
	                    result.put("fareClass", fareClass);

	                    matchingFares.add(result);
	                }

	            } catch (Exception e) {
	                test.log(Status.FAIL, "❌ Error processing flight card index " + j + ": " + e.getMessage());
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Card Error", "FlightCardError_" + j);
	            }
	        }

	        if (!supplierMatched) {
	            test.log(Status.INFO, "⚠️ Supplier '" + targetSupplier + "' not found in airline: " + airlineName);
	            airline.click(); // Unselect to allow next
	        } else {
	            break; // Stop if matching supplier fares found
	        }
	    }

	    // ❌ Supplier never found
	    if (!supplierMatched) {
	        test.log(Status.FAIL, "❌ Supplier '" + targetSupplier + "' NOT available for this route.");
	        test.log(Status.INFO, "📋 Suppliers found instead: " + String.join(", ", suppliersFound));
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO, "Available Suppliers", "SuppliersList");
	    }

	    return matchingFares;
	}
	private String getText(WebElement parent, String xpath) {
	    try {
	        return parent.findElement(By.xpath(xpath)).getText().trim();
	    } catch (NoSuchElementException e) {
	        return "";
	    }
	}
*/
	/*
	public List<Map<String, String>> checkSupplierAndDuplicateWithReport(WebDriver driver, ExtentTest test, String targetSupplier) throws InterruptedException {
	    Set<String> suppliersFound = new HashSet<>();
	    boolean supplierMatched = false;
	    List<Map<String, String>> matchingFares = new ArrayList<>();

	    List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));

	    for (int i = 0; i < airlineLabels.size(); i++) {
	        WebElement airline = airlineLabels.get(i);
	        String airlineName = airline.getText().trim();

	        airline.click();
	        Thread.sleep(3000);

	        List<WebElement> flightCards = driver.findElements(By.xpath("//*[contains(@class,'one-way-new-result-card')]"));

	        for (int j = 0; j < Math.min(3, flightCards.size()); j++) {
	            WebElement flightCard = flightCards.get(j);

	            try {
	                WebElement viewPrice = flightCard.findElement(By.xpath(".//a[text()='View Price']"));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPrice);
	                test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + j);
	                Thread.sleep(4000);

	                List<WebElement> fareCards = flightCard.findElements(By.xpath("./following-sibling::div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')]"));
	                List<String> fareKeys = new ArrayList<>();

	                for (int k = 0; k < fareCards.size(); k++) {
	                    WebElement fare = fareCards.get(k);

	                    // Extract all fields
	                    String supplier = getText(fare, ".//span[text()='Supplier']/following-sibling::span").trim();
	                    String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
	                    String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
	                    String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
	                    String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
	                    String refundable = getText(fare, ".//span[@class='fare-component_value']//span[contains(@class,'fare')]");
	                    String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");

	                    // Add found supplier to set
	                    suppliersFound.add(supplier);

	                    // Validate missing/invalid fields
	                    List<String> missingFields = new ArrayList<>();
	                    if (supplier.isEmpty()) missingFields.add("Supplier");
	                    if (fareType.isEmpty()) missingFields.add("Fare Type");
	                    if (farePrice.isEmpty()) missingFields.add("Price");

	                    // ✅ Validate Check-in Baggage (dynamic unit: KG/PC)
	                    String checkinNormalized = checkin.trim().toUpperCase().replaceAll("\\s+", "");
	                    if (checkinNormalized.isEmpty() || checkinNormalized.matches("^0+(KG|PC)$")) {
	                        missingFields.add("Check In (Invalid)");
	                    }

	                    if (cabin.isEmpty()) missingFields.add("Cabin");

	                    if (!(refundable.equalsIgnoreCase("Refundable") || refundable.equalsIgnoreCase("Non Refundable"))) {
	                        missingFields.add("Refundable");
	                    }

	                    if (fareClass.isEmpty()) missingFields.add("Class");

	                    // Report missing/invalid fields
	                    if (!missingFields.isEmpty()) {
	                        test.log(Status.INFO, "✅ Selected airline: " + airlineName);
	                        String missingDetails = String.join(", ", missingFields);
	                        test.log(Status.FAIL, "❌ Missing fare details [Flight: " + j + ", Fare: " + k + "] ➤ Missing: " + missingDetails +
	                                "<br><b>Fare Type:</b> " + fareType + ", <b>Price:</b> AED " + farePrice +
	                                ", <b>Check-in:</b> " + checkin + ", <b>Cabin:</b> " + cabin +
	                                ", <b>Refundable:</b> " + refundable + ", <b>Class:</b> " + fareClass);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Missing Fare Fields", "MissingFields_" + j + "_" + k);
	                        continue; // Skip this fare
	                    }

	                    // Proceed only if it's the target supplier
	                    if (!supplier.equalsIgnoreCase(targetSupplier)) continue;

	                    supplierMatched = true;

	                    // Duplicate check
	                    String uniqueKey = fareType + "|" + farePrice + "|" + checkin + "|" + cabin + "|" + refundable + "|" + fareClass;
	                    if (fareKeys.contains(uniqueKey)) {
	                        test.log(Status.FAIL, "❌ Duplicate fare found [Flight: " + j + ", Fare: " + k + "] ➤ Type: " + fareType + ", Price: AED " + farePrice);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Duplicate Fare", "DuplicateFare_" + j + "_" + k);
	                    } else {
	                        fareKeys.add(uniqueKey);
	                        test.log(Status.PASS, "✅ Unique fare found [Flight: " + j + ", Fare: " + k + "] ➤ Type: " + fareType + ", Price: AED " + farePrice);
	                    }

	                    // Store valid fare
	                    Map<String, String> result = new LinkedHashMap<>();
	                    result.put("flightIndex", String.valueOf(j));
	                    result.put("fareIndex", String.valueOf(k));
	                    result.put("supplier", supplier);
	                    result.put("fareType", fareType);
	                    result.put("price", farePrice);
	                    result.put("checkin", checkin);
	                    result.put("cabin", cabin);
	                    result.put("refundable", refundable);
	                    result.put("fareClass", fareClass);

	                    matchingFares.add(result);
	                }

	            } catch (Exception e) {
	                test.log(Status.FAIL, "❌ Error processing flight card index " + j + ": " + e.getMessage());
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Card Error", "FlightCardError_" + j);
	            }
	        }

	        if (!supplierMatched) {
	            test.log(Status.INFO, "⚠️ Supplier '" + targetSupplier + "' not found in airline: " + airlineName);
	            airline.click(); // Unselect to allow next
	        } else {
	            break; // Stop if matching supplier fares found
	        }
	    }

	    // Supplier never found
	    if (!supplierMatched) {
	        test.log(Status.FAIL, "❌ Supplier '" + targetSupplier + "' NOT available for this route.");
	        test.log(Status.INFO, " Suppliers found instead: " + String.join(", ", suppliersFound));
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO, "Available Suppliers", "SuppliersList");
	    }

	    return matchingFares;
	}

	// Helper to extract text safely
	private String getText(WebElement parent, String xpath) {
	    try {
	        return parent.findElement(By.xpath(xpath)).getText().trim();
	    } catch (NoSuchElementException e) {
	        return "";
	    }
	}
*/
//	public List<Map<String, String>> checkSupplierAndDuplicateWithReport(WebDriver driver, ExtentTest test, String targetSupplier) throws InterruptedException {
//	    Set<String> suppliersFound = new HashSet<>();
//	    boolean supplierMatched = false;
//	    List<Map<String, String>> matchingFares = new ArrayList<>();
//	    
//	    showMoreFare();//show more option
//Thread.sleep(3000);
//	    List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));
//
//	    for (int i = 0; i < airlineLabels.size(); i++) {
//	        WebElement airline = airlineLabels.get(i);
//	        String airlineName = airline.getText().trim();
//
//	        airline.click();
//	        Thread.sleep(3000);
//
//	        List<WebElement> flightCards = driver.findElements(By.xpath("//*[contains(@class,'one-way-new-result-card')]"));
//
//	        for (int j = 0; j < Math.min(3, flightCards.size()); j++) {
//	            WebElement flightCard = flightCards.get(j);
//
//	            try {
//	                WebElement viewPrice = flightCard.findElement(By.xpath(".//a[text()='View Price']"));
//	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPrice);
//	                test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + j);
//	                Thread.sleep(4000);
//
//	                List<WebElement> fareCards = flightCard.findElements(By.xpath("./following-sibling::div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')]"));
//	                List<String> fareKeys = new ArrayList<>();
//
//	                for (int k = 0; k < fareCards.size(); k++) {
//	                    WebElement fare = fareCards.get(k);
//
//	                    // Extract all fields
//	                    String supplier = getText(fare, ".//span[text()='Supplier']/following-sibling::span").trim();
//	                    String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
//	                    String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
//	                    String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
//	                    String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
//	                    String refundable = getText(fare, ".//span[@class='fare-component_value']//span[contains(@class,'fare')]");
//	                    String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");
//
//	                    // Add found supplier to set
//	                    suppliersFound.add(supplier);
//
//	                    // Proceed only if it's the target supplier
//	                    if (!supplier.equalsIgnoreCase(targetSupplier)) continue;
//
//	                    // ✅ Fix: Mark supplierMatched = true even if fare is invalid
//	                    supplierMatched = true;
//
//	                    // Validate missing/invalid fields
//	                    List<String> missingFields = new ArrayList<>();
//	                    if (supplier.isEmpty()) missingFields.add("Supplier");
//	               //     if (fareType.isEmpty()) missingFields.add("Fare Type");
//	                    if (fareType.isEmpty() || fareType.equals("-")) {
//	                        missingFields.add("Fare Type (Invalid)");
//	                    }
//
//	                    if (farePrice.isEmpty()) missingFields.add("Price");
//
//	                    // ✅ Validate Check-in Baggage (dynamic unit: KG/PC)
//	                    String checkinNormalized = checkin.trim().toUpperCase().replaceAll("\\s+", "");
//	                    if (checkinNormalized.isEmpty() || checkinNormalized.matches("^0+(KG|PC)$")) {
//	                        missingFields.add("Check In (Invalid)");
//	                    }
//
//	                    if (cabin.isEmpty()) missingFields.add("Cabin");
//
//	                    if (!(refundable.equalsIgnoreCase("Refundable") || refundable.equalsIgnoreCase("Non Refundable"))) {
//	                        missingFields.add("Refundable");
//	                    }
//
//	                    if (fareClass.isEmpty()) missingFields.add("Class");
//
//	                    // Report missing/invalid fields
//	                    if (!missingFields.isEmpty()) {
//	                        test.log(Status.INFO, "✅ Selected airline: " + airlineName);
//	                        String missingDetails = String.join(", ", missingFields);
//	                        test.log(Status.FAIL, "❌ Missing fare details [Flight: " + j + ", Fare: " + k + "] ➤ Missing: " + missingDetails +
//	                                "<br><b>Fare Type:</b> " + fareType + ", <b>Price:</b> AED " + farePrice +
//	                                ", <b>Check-in:</b> " + checkin + ", <b>Cabin:</b> " + cabin +
//	                                ", <b>Refundable:</b> " + refundable + ", <b>Class:</b> " + fareClass);
//	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Missing Fare Fields", "MissingFields_" + j + "_" + k);
//
//	                        // Optional: Warn supplier was found but data was invalid
//	                        test.log(Status.WARNING, "⚠️ Fare from supplier '" + supplier + "' found but invalid.");
//	                        continue; // Skip this fare
//	                    }
//
//	                    // Duplicate check
//	                    String uniqueKey = fareType + "|" + farePrice + "|" + checkin + "|" + cabin + "|" + refundable + "|" + fareClass;
//	                    if (fareKeys.contains(uniqueKey)) {
//	                        test.log(Status.FAIL, "❌ Duplicate fare found [Flight: " + j + ", Fare: " + k + "] ➤ Type: " + fareType + ", Price: AED " + farePrice);
//	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Duplicate Fare", "DuplicateFare_" + j + "_" + k);
//	                    } else {
//	                        fareKeys.add(uniqueKey);
//	                        test.log(Status.PASS, "✅ Unique fare found [Flight: " + j + ", Fare: " + k + "] ➤ Type: " + fareType + ", Price: AED " + farePrice);
//	                    }
//
//	                    // Store valid fare
//	                    Map<String, String> result = new LinkedHashMap<>();
//	                    result.put("flightIndex", String.valueOf(j));
//	                    result.put("fareIndex", String.valueOf(k));
//	                    result.put("supplier", supplier);
//	                    result.put("fareType", fareType);
//	                    result.put("price", farePrice);
//	                    result.put("checkin", checkin);
//	                    result.put("cabin", cabin);
//	                    result.put("refundable", refundable);
//	                    result.put("fareClass", fareClass);
//
//	                    matchingFares.add(result);
//	                }
//
//	            } catch (Exception e) {
//	                test.log(Status.FAIL, "❌ Error processing flight card index " + j + ": " + e.getMessage());
//	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Card Error", "FlightCardError_" + j);
//	            }
//	        }
//
//	        if (!supplierMatched) {
//	            test.log(Status.INFO, "⚠️ Supplier '" + targetSupplier + "' not found in airline: " + airlineName);
//	            airline.click(); // Unselect to allow next
//	        } else {
//	            break; // Stop if matching supplier fares found
//	        }
//	    }
//
//	    // Supplier never found
//	    if (!supplierMatched) {
//	        test.log(Status.FAIL, "❌ Supplier '" + targetSupplier + "' NOT available for this route.");
//	        test.log(Status.INFO, " Suppliers found instead: " + String.join(", ", suppliersFound));
//	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO, "Available Suppliers", "SuppliersList");
//	    }
//
//	    return matchingFares;
//	}
//
//	// Helper to extract text safely
//	private String getText(WebElement parent, String xpath) {
//	    try {
//	        return parent.findElement(By.xpath(xpath)).getText().trim();
//	    } catch (NoSuchElementException e) {
//	        return "";
//	    }
//	}
//	public List<Map<String, String>> checkSupplierAndDuplicateWithReport(WebDriver driver, ExtentTest test, String targetSupplier) throws InterruptedException {
//	    Set<String> suppliersFound = new HashSet<>();
//	    boolean supplierMatched = false;
//	    List<Map<String, String>> matchingFares = new ArrayList<>();
// 
//	    showMoreFare(); // show more option
//	    Thread.sleep(3000);
// 
//	    List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));
// 
//	    for (int i = 0; i < airlineLabels.size(); i++) {
//	        WebElement airline = airlineLabels.get(i);
//	        String airlineName = airline.getText().trim();
// 
//	        airline.click();
//	        test.log(Status.INFO, "✅ Selected airline: " + airlineName);
//	        
//	        Thread.sleep(3000);
// 
//	        List<WebElement> flightCards = driver.findElements(By.xpath("//*[contains(@class,'one-way-new-result-card')]"));
// 
//	        for (int j = 0; j < Math.min(3, flightCards.size()); j++) {
//	            WebElement flightCard = flightCards.get(j);
// 
//	            try {
//	                WebElement viewPrice = flightCard.findElement(By.xpath(".//a[text()='View Price']"));
//	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPrice);
//	                test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + j);
//	                Thread.sleep(4000);
// 
//	                List<WebElement> fareCards = flightCard.findElements(By.xpath("./following-sibling::div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')]"));
//	                List<String> fareKeys = new ArrayList<>();
// 
//	                for (int k = 0; k < fareCards.size(); k++) {
//	                    WebElement fare = fareCards.get(k);
// 
//	                    // Extract all fields
//	                    String supplier = getText(fare, ".//span[text()='Supplier']/following-sibling::span").trim();
//	                    //String fareType = getText(fare, ".//span[text()='Fare Type']/following-sibling::span");
//	                    String fareType = getText(fare, ".//span[contains(@class,'fare-type-span ')]/span");
//	                    
//	                    String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
//	                    String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
//	                    String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
//	                    String refundable = getText(fare, ".//span[@class='fare-component_value']//span[contains(@class,'fare')]");
//	                    String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");
// 
//	                    // Always track suppliers, even empty
//	                    suppliersFound.add(supplier);
// 
//	                    // ✅ Supplier is mandatory - handle missing supplier separately
//	                    if (supplier.isEmpty()) {
//	                     //   test.log(Status.FAIL, "❌ Supplier field is empty for flight Index: " + j + ", fare Index: " + k);
//	                        test.log(Status.FAIL, "❌ Supplier field is empty ");
//	                        test.log(Status.INFO, "Flight Index" + j + ", fare Index: " + k);
//	                        
//	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Missing Supplier", "MissingSupplier_" + j + "_" + k);
//	                        continue; // Skip this fare
//	                    }
// 
//	                    // Validate missing/invalid fields
//	                    List<String> missingFields = new ArrayList<>();
//	                    if (fareType.isEmpty() || fareType.equals("-")) missingFields.add("Fare Type (Invalid)");
//	                    if (farePrice.isEmpty()) missingFields.add("Price");
// 
//	                    // Validate Check-in Baggage (unit: KG/PC)
//	                    String checkinNormalized = checkin.trim().toUpperCase().replaceAll("\\s+", "");
//	                    if (checkinNormalized.isEmpty() || checkinNormalized.matches("^0+(KG|PC)$")) {
//	                        missingFields.add("Check In (Invalid)");
//	                    }
// 
//	                    if (cabin.isEmpty()) missingFields.add("Cabin");
// 
//	                    if (!(refundable.equalsIgnoreCase("Refundable") || refundable.equalsIgnoreCase("Non Refundable"))) {
//	                        missingFields.add("Refundable");
//	                    }
// 
//	                    if (fareClass.isEmpty()) missingFields.add("Class");
// 
//	                    // Log and skip invalid fares
//	                    if (!missingFields.isEmpty()) {
//	                     //   test.log(Status.INFO, "✅ Selected airline: " + airlineName);
//	                        String missingDetails = String.join(", ", missingFields);
//	                        test.log(Status.FAIL, "❌ Missing fare details [Flight: " + j + ", Fare: " + k + "] ➤ Missing: " + missingDetails +
//	                                "<br><b>Fare Type:</b> " + fareType + ", <b>Price:</b> AED " + farePrice +
//	                                ", <b>Check-in:</b> " + checkin + ", <b>Cabin:</b> " + cabin +
//	                                ", <b>Refundable:</b> " + refundable + ", <b>Class:</b> " + fareClass);
//	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Missing Fare Details", "MissingFields_" + j + "_" + k);
//	                        test.log(Status.INFO, "⚠️ Fare Details  from supplier '" + supplier + "' is  invalid.");
//	                        continue; // Skip this fare
//	                    }
// 
//	                    // Proceed only if it's the target supplier
//	                    if (!supplier.equalsIgnoreCase(targetSupplier)) continue;
// 
//	                    // ✅ Target supplier with valid fare
//	                    supplierMatched = true;
// 
//	                    // Duplicate check
//	                    String uniqueKey = fareType + "|" + farePrice + "|" + checkin + "|" + cabin + "|" + refundable + "|" + fareClass;
//	                    if (fareKeys.contains(uniqueKey)) {
//	                        test.log(Status.FAIL, "❌ Duplicate fare found [Flight: " + j + ", Fare: " + k + "] ➤ Type: " + fareType + ", Price: AED " + farePrice);
//	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Duplicate Fare", "DuplicateFare_" + j + "_" + k);
//	                    } else {
//	                        fareKeys.add(uniqueKey);
//	                        test.log(Status.PASS, "✅ Unique fare found [Flight: " + j + ", Fare: " + k + "] ➤ Type: " + fareType + ", Price: AED " + farePrice);
//	                    }
// 
//	                    // Store valid fare
//	                    Map<String, String> result = new LinkedHashMap<>();
//	                    result.put("flightIndex", String.valueOf(j));
//	                    result.put("fareIndex", String.valueOf(k));
//	                    result.put("supplier", supplier);
//	                    result.put("fareType", fareType);
//	                    result.put("price", farePrice);
//	                    result.put("checkin", checkin);
//	                    result.put("cabin", cabin);
//	                    result.put("refundable", refundable);
//	                    result.put("fareClass", fareClass);
// 
//	                    matchingFares.add(result);
//	                }
// 
//	            } catch (Exception e) {
//	                test.log(Status.FAIL, "❌ Error processing flight card index " + j + ": " + e.getMessage());
//	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Card Error", "FlightCardError_" + j);
//	            }
//	        }
// 
//	        if (!supplierMatched) {
//	            test.log(Status.INFO, "⚠️ Supplier '" + targetSupplier + "' not found in airline: " + airlineName);
//	            airline.click(); // Unselect to allow next
//	        } else {
//	            break; // Stop if matching supplier fares found
//	        }
//	    }
// 
//	    // Supplier never found at all
//	    if (!supplierMatched) {
//	        test.log(Status.FAIL, "❌ Supplier '" + targetSupplier + "' NOT available for this route.");
//	        test.log(Status.INFO, "Suppliers found instead: " + String.join(", ", suppliersFound));
//	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO, "Available Suppliers", "SuppliersList");
//	    }
// 
//	    return matchingFares;
//	}
// 
//	// Helper to extract text safely
//	private String getText(WebElement parent, String xpath) {
//	    try {
//	        return parent.findElement(By.xpath(xpath)).getText().trim();
//	    } catch (NoSuchElementException e) {
//	        return "";
//	    }
//	}
	
	
	
	public List<Map<String, String>> checkSupplierAndDuplicateWithReport(WebDriver driver, ExtentTest test, String targetSupplier) throws InterruptedException {
	    Set<String> suppliersFound = new HashSet<>();
	    boolean supplierMatched = false;
	    boolean supplierPresent = false; // ✅ Track if supplier was present at all
	    List<Map<String, String>> matchingFares = new ArrayList<>();
 
	    showMoreFare(); // show more option
	    Thread.sleep(3000);
 
	    List<WebElement> airlineLabels = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label"));
 
	    for (int i = 0; i < airlineLabels.size(); i++) {
	        WebElement airline = airlineLabels.get(i);
	        String airlineName = airline.getText().trim();
 
	        airline.click();
	        test.log(Status.INFO, "✅ Selected airline: " + airlineName);
	        Thread.sleep(3000);
 
	        List<WebElement> flightCards = driver.findElements(By.xpath("//*[contains(@class,'one-way-new-result-card')]"));
 
	        for (int j = 0; j < Math.min(3, flightCards.size()); j++) {
	            WebElement flightCard = flightCards.get(j);
 
	            try {
	                WebElement viewPrice = flightCard.findElement(By.xpath(".//a[text()='View Price']"));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPrice);
	                test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + j);
	                Thread.sleep(4000);
 
	                List<WebElement> fareCards = flightCard.findElements(By.xpath("./following-sibling::div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')]"));
	                List<String> fareKeys = new ArrayList<>();
 
	                for (int k = 0; k < fareCards.size(); k++) {
	                    WebElement fare = fareCards.get(k);
 
	                    String supplier = getText(fare, ".//span[text()='Supplier']/following-sibling::span").trim();
	                    String fareType = getText(fare, ".//span[contains(@class,'fare-type-span ')]/span");
	                    String farePrice = getText(fare, ".//div[contains(@class,'fare-totalfq')]").replace("AED", "").trim();
	                    String checkin = getText(fare, ".//span[text()='Check In']/following-sibling::span");
	                    String cabin = getText(fare, ".//span[text()='Cabin']/following-sibling::span");
	                    String refundable = getText(fare, ".//span[@class='fare-component_value']//span[contains(@class,'fare')]");
	                    String fareClass = getText(fare, ".//span[text()='Class']/following-sibling::span");
 
	                    suppliersFound.add(supplier);
 
	                    if (supplier.equalsIgnoreCase(targetSupplier)) {
	                        supplierPresent = true; // ✅ Supplier is present
	                    }
 
	                    // ✅ Handle missing supplier separately
	                    if (supplier.isEmpty()) {
	                        test.log(Status.FAIL, "❌ Supplier field is empty");
	                        test.log(Status.INFO, "Flight Index: " + j + ", Fare Index: " + k);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Missing Supplier", "MissingSupplier_" + j + "_" + k);
	                        continue;
	                    }
 
	                    // Validate fields
	                    List<String> missingFields = new ArrayList<>();
	                 //   if (fareType.isEmpty() || fareType.equals("-"))  missingFields.add("Fare Type (Invalid)");
	                    if (fareType.isEmpty() || fareType.equals("-") || fareType.equalsIgnoreCase("Null")) {
	                        missingFields.add("Fare Type (Invalid)");
	                    }
 
	                    if (farePrice.isEmpty()) missingFields.add("Price");
 
	                    /*
	                    String checkinNormalized = checkin.trim().toUpperCase().replaceAll("\\s+", "");
	                    if (checkinNormalized.isEmpty() || checkinNormalized.matches("^0+(KG|PC)$")) {
	                        missingFields.add("Check In (Invalid)");
	                    }
*/
	                    // It will validate if it return empty
	                    String checkinNormalized = checkin.trim().toUpperCase().replaceAll("\\s+", "");
	                    if (checkinNormalized.isEmpty()) {
	                        missingFields.add("Check In (Invalid)");
	                    }
 
	                    
	                    if (cabin.isEmpty()) missingFields.add("Cabin");
	                    if (!(refundable.equalsIgnoreCase("Refundable") || refundable.equalsIgnoreCase("Non Refundable"))) {
	                        missingFields.add("Refundable");
	                    }
 
	                    if (fareClass.isEmpty()) missingFields.add("Class");
 
	                    if (!missingFields.isEmpty()) {
	                        String missingDetails = String.join(", ", missingFields);
	                        test.log(Status.FAIL, "❌ Missing fare details [Flight: " + j + ", Fare: " + k + "] ➤ Missing: " + missingDetails +
	                                "<br><b>Fare Type:</b> " + fareType + ", <b>Price:</b> AED " + farePrice +
	                                ", <b>Check-in:</b> " + checkin + ", <b>Cabin:</b> " + cabin +
	                                ", <b>Refundable:</b> " + refundable + ", <b>Class:</b> " + fareClass);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Missing Fare Details", "MissingFields_" + j + "_" + k);
	                        test.log(Status.INFO, "⚠️ Fare from supplier '" + supplier + "' is invalid.");
	                        continue;
	                    }
 
	                    // ✅ Proceed if it's the target supplier
	                    if (!supplier.equalsIgnoreCase(targetSupplier)) continue;
 
	                    supplierMatched = true; // ✅ Supplier and fare both valid
 
	                    // Check for duplicate
	                    String uniqueKey = fareType + "|" + farePrice + "|" + checkin + "|" + cabin + "|" + refundable + "|" + fareClass;
	                    if (fareKeys.contains(uniqueKey)) {
	                        test.log(Status.FAIL, "❌ Duplicate fare found [Flight: " + j + ", Fare: " + k + "] ➤ Type: " + fareType + ", Price: AED " + farePrice);
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Duplicate Fare", "DuplicateFare_" + j + "_" + k);
	                    } else {
	                        fareKeys.add(uniqueKey);
	                        test.log(Status.PASS, "✅ Unique fare found [Flight: " + j + ", Fare: " + k + "] ➤ Type: " + fareType + ", Price: AED " + farePrice);
	                    }
 
	                    // Store valid fare
	                    Map<String, String> result = new LinkedHashMap<>();
	                    result.put("flightIndex", String.valueOf(j));
	                    result.put("fareIndex", String.valueOf(k));
	                    result.put("supplier", supplier);
	                    result.put("fareType", fareType);
	                    result.put("price", farePrice);
	                    result.put("checkin", checkin);
	                    result.put("cabin", cabin);
	                    result.put("refundable", refundable);
	                    result.put("fareClass", fareClass);
 
	                    matchingFares.add(result);
	                }
 
	            } catch (Exception e) {
	                test.log(Status.FAIL, "❌ Error processing flight card index " + j + ": " + e.getMessage());
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Card Error", "FlightCardError_" + j);
	            }
	        }
 
	        if (!supplierMatched) {
	            test.log(Status.INFO, "⚠️ Supplier '" + targetSupplier + "' not found in airline: " + airlineName);
	            airline.click(); // Unselect to allow next
	        } else {
	            break; // Stop if matching supplier fares found
	        }
	    }
 
	    // ✅ Final evaluation
	    if (!supplierMatched && supplierPresent) {
	        test.log(Status.FAIL, "❌ Supplier '" + targetSupplier + "' is present, but all fares are invalid or incomplete.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Supplier Found But Invalid Fares", "InvalidFares_" + targetSupplier);
	    } else if (!supplierMatched) {
	        test.log(Status.FAIL, "❌ Supplier '" + targetSupplier + "' NOT available for this route.");
	        test.log(Status.INFO, "Suppliers found instead: " + String.join(", ", suppliersFound));
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO, "Available Suppliers", "SuppliersList");
	    }
 
	    return matchingFares;
	}
 
	// ✅ Helper to extract text safely
	private String getText(WebElement parent, String xpath) {
	    try {
	        return parent.findElement(By.xpath(xpath)).getText().trim();
	    } catch (NoSuchElementException e) {
	        return "";
	    }
	}
//------------
		

		
		public Map<String, String> getFlightCardDetails(int cardIndex) {
			
			int index=cardIndex+1;
			
		    Map<String, String> details = new LinkedHashMap<>();

		    // Base XPath for the flight card at the given index (1-based for XPath)
		    WebElement flightCard = driver.findElement(By.xpath("(//*[contains(@class,'one-way-new-result-card')])[" + index + "]"));

		    
		    
		    // Flight Numbers (split and joined by commas)
		    String flightNumber =flightCard.findElement(By.xpath(".//*[contains(@class, 'flight-number')]")).getText();
		    String[] flightNumberSplit = flightNumber.split(",");
		    List<String> flightNo = new ArrayList<>();
		    for(String fn : flightNumberSplit) {
		        flightNo.add(fn.trim());
		    }
		    details.put("flightNumbers", String.join(",", flightNo));

		    // Airline Name
		    String airLineName = flightCard.findElement(By.xpath(".//*[contains(@class,'flight-company')]")).getText().trim().toLowerCase();
		    details.put("airlineName", airLineName);
		 // Assuming 'flightCard' is the parent WebElement containing multiple flights
		    /*
		    List<WebElement> airlineElements = flightCard.findElements(By.xpath(".//*[contains(@class,'flight-company')]"));
		    List<String> airlineNames = new ArrayList<>();

		    for (WebElement airline : airlineElements) {
		        String name = airline.getText().trim().toLowerCase();
		        if (!name.isEmpty()) {
		            airlineNames.add(name);
		        }
		    }

		  
		    details.put("airlineNames", String.join(",", airlineNames));
*/

		    // Flight Equipment
		    String flightEquipment = flightCard.findElement(By.xpath(".//*[contains(@class, 'flight-equipment')]")).getText();
		    String flightEquipmentClean = flightEquipment.replace("Equipment: ", "").trim();
		    String[] flightEquipmentSplit = flightEquipmentClean.split(",");
		    List<String> flightEq = new ArrayList<>();
		    for (String eq : flightEquipmentSplit) {
		        flightEq.add(eq.trim());
		    }
		    details.put("flightEquipment", String.join(",", flightEq));

		    // Flight Operated By
		    String flightOperatedBy = flightCard.findElement(By.xpath(".//*[contains(@class,'flight-operated-by')]")).getText();
		    String[] parts = flightOperatedBy.split(":");
		    List<String> cleanAirlines = new ArrayList<>();
		    if (parts.length > 1) {
		        String[] airlinesWithCode = parts[1].split(",");
		        for (String airline : airlinesWithCode) {
		            cleanAirlines.add(airline.trim().toLowerCase());
		        }
		    }
		    details.put("operatedBy", String.join(",", cleanAirlines));

		    // Departure Time
		    String flightDeptTime = flightCard.findElement(By.xpath("//span[contains(@class,'flight-deptime')]")).getText().trim();
		    details.put("departureTime", flightDeptTime);

		    // Arrival Time (remove +1 if present)
		    String flightArrivalTimeRaw = flightCard.findElement(By.xpath(".//span[contains(@class,'flight-arrtime')]")).getText().trim();
		    String flightArrivalTime = flightArrivalTimeRaw.replace("+1", "");
		    details.put("arrivalTime", flightArrivalTime);

		    // Departure Location (only first part before hyphen)
		    String flightDeptLocation = flightCard.findElement(By.xpath("//span[contains(@class,'flight-origin')]")).getText();
		    String flightDeptLocationClean = flightDeptLocation.split("-")[0].trim();
		    details.put("departureLocation", flightDeptLocationClean);

		    // Arrival Location (only first part before hyphen)
		    String flightArrivalLocation =flightCard.findElement(By.xpath("//span[contains(@class,'flight-destination')]")).getText();
		    String flightArrivalLocationClean = flightArrivalLocation.split("-")[0].trim();
		    details.put("arrivalLocation", flightArrivalLocationClean);

		    // Departure Date
		    String flightDeptDate = flightCard.findElement(By.xpath("//span[@class='flight-depdate']")).getText();
		    details.put("departureDate", flightDeptDate);

		    // Arrival Date
		    String flightArrivalDate = flightCard.findElement(By.xpath("//span[@class='flight-arrdate']")).getText();
		    details.put("arrivalDate", flightArrivalDate);

		    // Flight Price
		    String flightPrice =flightCard.findElement(By.xpath("//span[contains(@class,'flight-totalfq')]")).getText();
		    details.put("price", flightPrice);

		    // Total Duration
		    String totalDuration = flightCard.findElement(By.xpath("//span[contains(@class,'flight-totaljourneyduration')]")).getText();
		    details.put("duration", totalDuration);

		    // Stops (extract number before "stop")
		    String stopText =  flightCard.findElement(By.xpath("//p[@class='stop-seperator']/following-sibling::span")).getText();
		    String stops = stopText.split("stop")[0].trim();
		    details.put("stops", stops);
		    
		    WebElement departTerminal = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-origin']"));
		    String[] departTerminal1 = departTerminal.getText().split("-");
		    String departTerminal2 = departTerminal1[1].trim();
		    details.put("departCard", departTerminal2);
		    System.out.println(departTerminal2);
		 
		    WebElement arrivalTerminal = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-destination']"));
		    String[] arrivalTerminal1 = arrivalTerminal.getText().split("-");
		    String arrivalTerminal2 = arrivalTerminal1[1].trim();
		    details.put("arrivalCard", arrivalTerminal2);
		    System.out.println(arrivalTerminal2);
		    return details;
		}

		public void clickOnFlightDetails(int index, ExtentTest test)
		{
			int cardIndex= index+1;
			System.out.println(cardIndex);
			 try {
			       
			        String flightDetailsXPath = "(//*[contains(@class,'one-way-new-result-card')]//*[text()='Flight Details'])[" + cardIndex + "]";
			        WebElement flightDetailsButton = driver.findElement(By.xpath(flightDetailsXPath));

			        
			        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", flightDetailsButton);
			        Thread.sleep(2000); 

			      
			        flightDetailsButton.click();

			        // Log success
			        test.log(Status.PASS, "✅ Clicked 'Flight Details' for flight at index: " + index);
			        System.out.println("✅ Clicked 'Flight Details' for flight at index: " + index);

			       

			    } catch (Exception e) {
			        System.out.println("❌ Exception while selecting flight: " + e.getMessage());
			        test.log(Status.FAIL, "❌ Exception while selecting flight at index " + index + ": " + e.getMessage());
			        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Selection Failure", "FlightSelectException");
			        Assert.fail();
			    }
			
		}
		
		 public void clickOnFareBookNow(int index, int fareIndex , ExtentTest test ) throws Exception {
		        try {
		        	int flightCardIndex=index+1;
		        	int fareCardIndex=fareIndex+1;
		        	
		            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		            // Locate the specific flight card using correct XPath index (1-based)
		            WebElement flightCard = driver.findElement(By.xpath("(//*[contains(@class,'one-way-new-result-card')])[" + flightCardIndex + "]"));

		            Thread.sleep(2000);
		            // Find the corresponding fare card within that flight
		            WebElement farecard = flightCard.findElement(
		                    By.xpath("(./following-sibling::div[@class='fare-components-list']//div[contains(@class,'fare-component-mobile')])[" + fareCardIndex + "]"));

		            // Wait for the "Book now" button to be clickable
		            WebElement bookNowButton = wait.until(
		                    ExpectedConditions.elementToBeClickable(farecard.findElement(By.xpath(".//button[text()='Book now']"))));

		            // Click the button
		            bookNowButton.click();

		            // Optional: log success
		            test.log(Status.PASS, "✅ Clicked 'Book now' on flight index " + index + ", fare index " + fareIndex);

		        } catch (NoSuchElementException | TimeoutException e) {
		            System.out.println("❌ Element not found or not clickable: " + e.getMessage());

		            // Optional: capture screenshot and log failure
		             ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Click Failed", "Book now button not found or clickable.");
		             test.log(Status.FAIL, "❌ Failed to click 'Book now': " + e.getMessage());
		            throw e; // rethrow or handle as needed
		        } catch (Exception e) {
		            System.out.println("❌ Unexpected error during clickOnFareBookNow: " + e.getMessage());
		            throw e;
		        }
		    }
		
		 public List<String> selectAirline(List<String> airLines, ExtentTest test) throws InterruptedException {
			    try {
			        test.log(Status.INFO, "🔍 Requested airlines: " + airLines);
			        Thread.sleep(3000);
			 
			        // Show More
			        List<WebElement> showMoreButtons = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//a[text()='Show More']"));
			        if (!showMoreButtons.isEmpty() && showMoreButtons.get(0).isDisplayed()) {
			            showMoreButtons.get(0).click();
			            Thread.sleep(2000);
			            test.log(Status.INFO, "📂 Clicked on 'Show More' to expand airline list.");
			        } else {
			            test.log(Status.INFO, "ℹ️ 'Show More' button not found or not visible.");
			        }
			 
			        List<String> selectedAirlines = new ArrayList<>();
			 
			        List<WebElement> listOfAirline = driver.findElements(By.xpath(
			            "//div[text()='Search By Airlines']/parent::div//label[contains(@class,'app-check-box cursor-pointer')]"
			        ));
			 
			        Map<String, WebElement> airlineMap = new HashMap<>();
			        for (WebElement label : listOfAirline) {
			            String labelText = label.getText();
			            String[] parts = labelText.split("\\(");
			            String airlineName = parts[0].trim().toLowerCase();
			            airlineMap.put(airlineName, label);
			        }
			 
			        for (String airline : airLines) {
			            String airlineLower = airline.toLowerCase();
			            if (airlineMap.containsKey(airlineLower)) {
			                WebElement label = airlineMap.get(airlineLower);
			                WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));
			 
			                if (!checkbox.isSelected()) {
			                    label.click();
			                    test.log(Status.PASS, "✅ Selected airline: " + label.getText().trim());
			                } else {
			                    test.log(Status.INFO, "ℹ️ Airline already selected: " + label.getText().trim());
			                }
			 
			                selectedAirlines.add(label.getText().trim());
			            } else {
			                test.log(Status.WARNING, "❌ Airline not found on screen: " + airline);
			            }
			        }
			 
			        // If no requested airline was selected, click the first available one
			        if (selectedAirlines.isEmpty() && !listOfAirline.isEmpty()) {
			            WebElement firstAirline = listOfAirline.get(0);
			            WebElement checkbox = firstAirline.findElement(By.xpath(".//input[@type='checkbox']"));
			 
			            if (!checkbox.isSelected()) {
			                firstAirline.click();
			            }
			 
			            String selected = firstAirline.getText().trim();
			            selectedAirlines.add(selected);
			            test.log(Status.WARNING, "⚠️ None of the requested airlines were found. Selected first available airline instead: " + selected);
			        }
			 
			        if (selectedAirlines.isEmpty()) {
			            test.log(Status.FAIL, "❌ No airlines could be selected.");
			            Assert.fail("No airlines could be selected.");
			        }
			 
			        return selectedAirlines;
			 
			    } catch (Exception e) {
			        test.log(Status.FAIL, "❌ Exception in selectAirline(): " + e.getMessage());
			        e.printStackTrace();
			        Assert.fail("Exception in selectAirline(): " + e.getMessage());
			        return null;
			    }
			}
		 public void validateAirlineFilter(List<String> selectedAirlines,ExtentTest test) throws InterruptedException {
			    try {
			        Thread.sleep(2000);
			        test.log(Status.INFO, "🧪 Validating selected airlines: " + selectedAirlines);
		 
			        List<String> selectedCodes = new ArrayList<>();
			        for (String airline : selectedAirlines) {
			            if (airline.contains("(") && airline.contains(")")) {
			                String code = airline.split("\\(")[1].replace(")", "").trim();
			                selectedCodes.add(code);
			            }
			        }
		 
			        if (selectedCodes.isEmpty()) {
			            test.log(Status.FAIL, "❌ No airline codes extracted for validation.");
			            Assert.fail("No airline codes extracted.");
			        }
		 
			        boolean allMatch = true;
			        List<WebElement> airLineElements = driver.findElements(By.xpath("//div[@class='d-flex flex-column flight-name']"));
			        test.log(Status.INFO, "📦 Number of flights displayed: " + airLineElements.size());
		 
			        for (WebElement element : airLineElements) {
			            String displayedAirline = element.getText().trim(); // e.g., "Air India AI-123"
			            boolean matchFound = false;
		 
			            for (String code : selectedCodes) {
			                if (displayedAirline.contains(code)) {
			                    matchFound = true;
			                    break;
			                }
			            }
		 
			            if (!matchFound) {
			                test.log(Status.FAIL, "❌ Airline mismatch: " + displayedAirline);
			                allMatch = false;
			            } else {
			                test.log(Status.PASS, "✅ Airline matched: " + displayedAirline);
			            }
			        }
		 
			        if (!allMatch) {
			            test.log(Status.FAIL, "❌ Some flights do not match selected airline(s).");
			            Assert.fail("Flight list contains unmatched airlines.");
			        } else {
			            test.log(Status.PASS, "🎉 All flights match the selected airline(s).");
			        }
		 
			    } catch (Exception e) {
			        test.log(Status.FAIL, "❌ Exception in validateAirlineFilter(): " + e.getMessage());
			        e.printStackTrace();
			        Assert.fail("Exception in validateAirlineFilter(): " + e.getMessage());
			    }
			}
		 
		 public void showMoreFare() {
			    try {
			   
			        By showMoreLocator = By.xpath("//div[text()='Search By Airlines']/parent::div//a[text()='Show More']");
			        
			 
			        List<WebElement> showMoreElements = driver.findElements(showMoreLocator);

			        if (!showMoreElements.isEmpty() && showMoreElements.get(0).isDisplayed()) {
			            showMoreElements.get(0).click();
			            System.out.println("✅ Clicked 'Show More' successfully.");
			        } else {
			            System.out.println("ℹ️ 'Show More' option is not visible or not present.");
			        }
			    } catch (Exception e) {
			        System.out.println("❌ Error while clicking 'Show More': " + e.getMessage());
			    }
			}


}


