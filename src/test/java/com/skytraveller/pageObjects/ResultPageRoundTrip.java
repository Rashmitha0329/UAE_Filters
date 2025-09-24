package com.skytraveller.pageObjects;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class ResultPageRoundTrip extends BasePage{

	public ResultPageRoundTrip(WebDriver driver) {
		super(driver);
	}

	
	//it will return multiple fare cards and this will check for all flightcard
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

		     //   List<WebElement> flightCards = driver.findElements(By.xpath("//div[contains(@class,'one-way-new-result-card')]"));
		        List<WebElement> flightCards = driver.findElements(By.xpath("//section[contains(@class,'one-way-new-result-card')]"));

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

	        List<WebElement> flightCards = driver.findElements(By.xpath("//section[contains(@class,'one-way-new-result-card')]"));

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
	           // List<WebElement> fares = driver.findElements(By.xpath("//*[contains(@class,'fare-component-watermark')]"));
	            List<WebElement> fares = driver.findElements(By.xpath("//*[contains(@class,'fare-components-list')]/div"));

	            if (!fares.isEmpty()) {
	                WebElement firstFare = fares.get(0);

	                // Look for the Book now button within the selected fare block
	                WebElement bookNowBtn = firstFare.findElement(By.xpath(".//*[text()='Book now']"));

	                // Find Amenities link (relative to the firstFare, not globally)
	                List<WebElement> amenities = firstFare.findElements(By.xpath(".//a[text()='Amenities']"));
	                if (!amenities.isEmpty()) {
	                	Thread.sleep(2000);
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

	
	
	
public Map<String, String> getFlightCardDetailsRoundTripOnWardLocation(int cardIndex) {
		
		int index=cardIndex+1;
		
	    Map<String, String> details = new LinkedHashMap<>();
 
	    // Base XPath for the flight card at the given index (1-based for XPath)
	    WebElement flightCard = driver.findElement(By.xpath("(//*[contains(@class,'one-way-new-result-card')]/parent::section//*[contains(@class,'flight-info_flight-details')][1])['"+index+"']"));
 
	    
	    
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
 
	    return details;
	}


public Map<String, String> getFlightCardDetailsRoundTripReturnLocation(int cardIndex) {
	
	int index=cardIndex+1;
	
    Map<String, String> details = new LinkedHashMap<>();

    // Base XPath for the flight card at the given index (1-based for XPath)
    WebElement flightCard2 = driver.findElement(By.xpath("(//*[contains(@class,'one-way-new-result-card')]/parent::section//*[contains(@class,'flight-info_flight-details')][2])['"+index+"']"));

    
    
    // Flight Numbers (split and joined by commas)
    String flightNumber =flightCard2.findElement(By.xpath(".//*[contains(@class, 'flight-number')]")).getText();
    String[] flightNumberSplit = flightNumber.split(",");
    List<String> flightNo = new ArrayList<>();
    for(String fn : flightNumberSplit) {
        flightNo.add(fn.trim());
    }
    details.put("flightNumbers", String.join(",", flightNo));

    // Airline Name
    String airLineName = flightCard2.findElement(By.xpath(".//*[contains(@class,'flight-company')]")).getText().trim().toLowerCase();
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
    String flightEquipment = flightCard2.findElement(By.xpath(".//*[contains(@class, 'flight-equipment')]")).getText();
    String flightEquipmentClean = flightEquipment.replace("Equipment: ", "").trim();
    String[] flightEquipmentSplit = flightEquipmentClean.split(",");
    List<String> flightEq = new ArrayList<>();
    for (String eq : flightEquipmentSplit) {
        flightEq.add(eq.trim());
    }
    details.put("flightEquipment", String.join(",", flightEq));

    // Flight Operated By
    String flightOperatedBy = flightCard2.findElement(By.xpath(".//*[contains(@class,'flight-operated-by')]")).getText();
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
    String flightDeptTimeArrival = flightCard2.findElement(By.xpath(".//span[contains(@class,'flight-deptime')]")).getText().trim();
    details.put("departureTime", flightDeptTimeArrival);

    // Arrival Time (remove +1 if present)
    String flightArrivalTimeRaw = flightCard2.findElement(By.xpath(".//span[contains(@class,'flight-arrtime')]")).getText().trim();
    String flightArrivalTime = flightArrivalTimeRaw.replace("+1", "");
    details.put("arrivalTime", flightArrivalTime);

    // Departure Location (only first part before hyphen)
    String flightDeptLocation = flightCard2.findElement(By.xpath(".//span[contains(@class,'flight-origin')]")).getText();
    String flightDeptLocationClean = flightDeptLocation.split("-")[0].trim();
    details.put("departureLocation", flightDeptLocationClean);

    // Arrival Location (only first part before hyphen)
    String flightArrivalLocation =flightCard2.findElement(By.xpath(".//span[contains(@class,'flight-destination')]")).getText();
    String flightArrivalLocationClean = flightArrivalLocation.split("-")[0].trim();
    details.put("arrivalLocation", flightArrivalLocationClean);

    // Departure Date
    String flightDeptDate = flightCard2.findElement(By.xpath(".//span[@class='flight-depdate']")).getText();
    details.put("departureDate", flightDeptDate);

    // Arrival Date
    /*
    String flightArrivalDate = flightCard2.findElement(By.xpath("(.//span[@class='flight-arrdate'])[last()]")).getText();
    details.put("arrivalDate", flightArrivalDate);
*/
 // Extract the last arrival date text from flightCard2
    String flightArrivalDate = flightCard2.findElement(By.xpath("(.//span[@class='flight-arrdate'])[last()]")).getText();

    // Store it in the details map with key "arrivalDate"
    details.put("arrivalDate", flightArrivalDate);

//    // Flight Price
//    String flightPrice =flightCard2.findElement(By.xpath(".//span[contains(@class,'flight-totalfq')]")).getText();
//    details.put("price", flightPrice);

    // Total Duration
    String totalDuration = flightCard2.findElement(By.xpath(".//span[contains(@class,'flight-totaljourneyduration')]")).getText();
    details.put("duration", totalDuration);

    // Stops (extract number before "stop")
    String stopText =  flightCard2.findElement(By.xpath(".//p[@class='stop-seperator']/following-sibling::span")).getText();
    String stops = stopText.split("stop")[0].trim();
    details.put("stops", stops);

    return details;
}
}
