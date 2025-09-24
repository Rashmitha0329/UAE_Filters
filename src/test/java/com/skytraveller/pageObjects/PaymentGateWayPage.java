package com.skytraveller.pageObjects;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class PaymentGateWayPage extends BasePage {

	// Constructor of loginPage calls the BasePage constructor with driver
	public PaymentGateWayPage(WebDriver driver) {
		super(driver);// calls BasePage constructor
	}
	
	/*
	public void validatePayMentPage(ArrayList<String> details, double expectedPrice, ExtentTest test) {
	    try {
	        // Extract expected details
	        String expectedLocation = details.get(0).trim(); // e.g., "Sharjah (SHJ) -> New Delhi (DEL)"
	        String expectedTravellerName = details.get(2).replaceFirst("^\\d+\\)\\s*", "").trim(); // remove "1) " if exists

	        WebElement paymentPage = driver.findElement(By.xpath("//span[text()='Billing Information']"));

	        if (paymentPage.isDisplayed()) {
	            // Get detail text (e.g., "Flight Booking- SHJ-DEL , MR TARUN KUMAR")
	            String detailText = driver.findElement(By.xpath("//span[@id='mobileno']")).getText().trim();

	            // Get price text (e.g., "AED 315.16")
	            String priceText = driver.findElement(By.xpath("//div[text()='Total Amount']/following-sibling::div")).getText().trim();

	            // ---- Parse the detail text ----
	            // Assuming format: "Flight Booking- SHJ-DEL , MR TARUN KUMAR"
	            String[] parts = detailText.split("[-,]");
	            if (parts.length < 3) {
	                test.log(Status.FAIL, "Unexpected detail format: " + detailText);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Format Error", "Detail text format is invalid.");
	                Assert.fail("Invalid detail text format.");
	                return;
	            }

	            String actualRouteCode = parts[1].trim();      // e.g., "SHJ-DEL"
	            String actualTravellerName = parts[2].trim();  // e.g., "MR TARUN KUMAR"

	            // Normalize route to match location
	            String expectedRouteCode = expectedLocation.replaceAll("[^A-Z\\-]", ""); // Extracts e.g., SHJ-DEL from full location

	            // ---- Parse the price ----
	            double actualPrice = 0.0;
	            try {
	                actualPrice = Double.parseDouble(priceText.replaceAll("[^\\d.]", ""));
	            } catch (NumberFormatException e) {
	                test.log(Status.FAIL, "Failed to parse actual price: " + priceText);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Price Parsing Error", "Invalid price format.");
	                Assert.fail("Price parsing failed.");
	            }

	            // ---- Compare all values ----
	            boolean isRouteMatch = actualRouteCode.equalsIgnoreCase(expectedRouteCode);
	            boolean isNameMatch = actualTravellerName.equalsIgnoreCase(expectedTravellerName);
	            boolean isPriceMatch = Math.abs(actualPrice - expectedPrice) < 0.01;

	            if (isRouteMatch && isNameMatch && isPriceMatch) {
	                test.log(Status.PASS, "Payment page validated successfully." +
	                        "<br><b>Route:</b> " + actualRouteCode +
	                        "<br><b>Traveller:</b> " + actualTravellerName +
	                        "<br><b>Price:</b> " + actualPrice);
	            } else {
	                test.log(Status.FAIL, "Mismatch in payment details." +
	                        "<br><b>Expected:</b> " + expectedRouteCode + ", " + expectedTravellerName + ", " + expectedPrice +
	                        "<br><b>Actual:</b> " + actualRouteCode + ", " + actualTravellerName + ", " + actualPrice);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Mismatch", "Payment details mismatch");
	                Assert.fail("Payment details did not match.");
	            }

	        } else {
	            test.log(Status.FAIL, "Billing Information section not found on payment page.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Page Load Error", "Billing Information not visible.");
	            Assert.fail("Payment page not displayed correctly.");
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception during payment page validation: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception", "Error during validation.");
	        Assert.fail("Exception during payment validation.");
	    }
	}
*/
	/*
	public void validatePayMentPage(ArrayList<String> details, double expectedPrice, ExtentTest test) {
	    try {
	        // Extract expected details
	        String expectedLocation = details.get(0).trim(); // e.g., "Sharjah (SHJ) -> New Delhi (DEL)"
	        String expectedTravellerName = details.get(2).replaceFirst("^\\d+\\)\\s*", "").trim(); // remove "1) "

	        // Extract SHJ and DEL using regex from expectedLocation
	        Pattern pattern = Pattern.compile("\\((.*?)\\)");
	        Matcher matcher = pattern.matcher(expectedLocation);

	        String fromCode = "", toCode = "";
	        if (matcher.find()) fromCode = matcher.group(1); // SHJ
	        if (matcher.find()) toCode = matcher.group(1);   // DEL

	        String expectedRouteCode = fromCode + "-" + toCode; // SHJ-DEL

	        // Check if payment page is displayed
	        WebElement paymentPage = driver.findElement(By.xpath("//span[text()='Billing Information']"));

	        if (paymentPage.isDisplayed()) {
	            // Extract details from UI
	            String detailText = driver.findElement(By.xpath("//span[@id='mobileno']")).getText().trim(); // e.g., "Flight Booking- SHJ-DEL , MR TARUN KUMAR"
	            String priceText = driver.findElement(By.xpath("//div[text()='Total Amount']/following-sibling::div")).getText().trim(); // e.g., "AED 346.83"

	            // Extract actual route and name
	            String actualRouteCode = "";
	            String actualTravellerName = "";
	            String[] parts = detailText.split("[-,]");
	            if (parts.length >= 3) {
	                actualRouteCode = parts[1].trim() + "-" + parts[2].trim(); // e.g., SHJ-DEL
	                actualTravellerName = parts[3].trim(); // e.g., MR TARUN KUMAR
	            } else {
	                test.log(Status.FAIL, "Unexpected detail format: " + detailText);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Format Error", "Detail text format is invalid.");
	                Assert.fail("Invalid detail text format.");
	                return;
	            }

	            // Parse price
	            double actualPrice = 0.0;
	            try {
	                actualPrice = Double.parseDouble(priceText.replaceAll("[^\\d.]", ""));
	            } catch (NumberFormatException e) {
	                test.log(Status.FAIL, "Failed to parse actual price: " + priceText);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Price Parsing Error", "Invalid price format.");
	                Assert.fail("Price parsing failed.");
	            }

	            // Perform comparisons
	            boolean isRouteMatch = actualRouteCode.equalsIgnoreCase(expectedRouteCode);
	            boolean isNameMatch = actualTravellerName.equalsIgnoreCase(expectedTravellerName);
	            boolean isPriceMatch = Math.abs(actualPrice - expectedPrice) < 0.01;

	            if (isRouteMatch && isNameMatch && isPriceMatch) {
	                test.log(Status.PASS, "Payment page validated successfully." +
	                        "<br><b>Route:</b> " + actualRouteCode +
	                        "<br><b>Traveller:</b> " + actualTravellerName +
	                        "<br><b>Price:</b> " + actualPrice);
	            } else {
	                test.log(Status.FAIL, "Mismatch in payment details." +
	                        "<br><b>Expected:</b> " + expectedRouteCode + ", " + expectedTravellerName + ", " + expectedPrice +
	                        "<br><b>Actual:</b> " + actualRouteCode + ", " + actualTravellerName + ", " + actualPrice);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Mismatch", "Payment details mismatch");
	                Assert.fail("Payment details did not match.");
	            }

	        } else {
	            test.log(Status.FAIL, "Billing Information section not found on payment page.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Page Load Error", "Billing Information not visible.");
	            Assert.fail("Payment page not displayed correctly.");
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception during payment page validation: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception", "Error during validation.");
	        Assert.fail("Exception during payment validation.");
	    }
	}
*/
	public void validatePayMentPage(ArrayList<String> details, double expectedPrice, ExtentTest test) {
	    try {
	        // Extract expected details
	        String expectedLocation = details.get(0).trim(); // e.g., "Sharjah (SHJ) -> New Delhi (DEL)"
	        String expectedTravellerName = details.get(2).replaceFirst("^\\d+\\)\\s*", "").trim(); // Remove "1) " if exists

	        // Extract airport codes (e.g., SHJ, DEL) from location string
	        Pattern pattern = Pattern.compile("\\((.*?)\\)");
	        Matcher matcher = pattern.matcher(expectedLocation);

	        String fromCode = "", toCode = "";
	        if (matcher.find()) fromCode = matcher.group(1); // SHJ
	        if (matcher.find()) toCode = matcher.group(1);   // DEL

	        String expectedRouteCode = fromCode + "-" + toCode; // SHJ-DEL

	        // Check if payment page is visible
	        WebElement paymentPage = driver.findElement(By.xpath("//span[text()='Billing Information']"));
	        if (paymentPage.isDisplayed()) {

	            // Read text from the UI
	            String detailText = driver.findElement(By.xpath("//span[@id='mobileno']")).getText().trim(); // e.g., "Flight Booking- SHJ-DEL , MR TARUN"
	            String priceText = driver.findElement(By.xpath("//div[text()='Total Amount']/following-sibling::div")).getText().trim(); // e.g., "AED 346.83"

	            // Extract route and name from detail text
	            String actualRouteCode = "";
	            String actualTravellerName = "";
	            String[] parts = detailText.split("[-,]");
	            if (parts.length >= 4) {
	                actualRouteCode = parts[1].trim() + "-" + parts[2].trim(); // SHJ-DEL
	                actualTravellerName = parts[3].trim();                     // MR TARUN KUMAR
	            } else {
	                test.log(Status.FAIL, "Unexpected format in passenger detail: " + detailText);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Detail Format Error", "Passenger detail text is invalid.");
	                Assert.fail("Detail text format invalid.");
	                return;
	            }

	            // Parse actual price
	            double actualPrice = 0.0;
	            try {
	                actualPrice = Double.parseDouble(priceText.replaceAll("[^\\d.]", "")); // Removes "AED" etc.
	            } catch (NumberFormatException e) {
	                test.log(Status.FAIL, "Failed to parse price: " + priceText);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Price Parse Error", "Could not convert price to double.");
	                Assert.fail("Price parsing failed.");
	            }

	            // Perform all comparisons
	            boolean isRouteMatch = actualRouteCode.equalsIgnoreCase(expectedRouteCode);
	            boolean isNameMatch = actualTravellerName.equalsIgnoreCase(expectedTravellerName);
	            boolean isPriceMatch = Math.abs(actualPrice - expectedPrice) < 0.01;

	            if (isRouteMatch && isNameMatch && isPriceMatch) {
	                test.log(Status.PASS, "Payment page validated successfully." +
	                        "<br><b>Route:</b> " + actualRouteCode +
	                        "<br><b>Traveller:</b> " + actualTravellerName +
	                        "<br><b>Price:</b> " + actualPrice);
	            } else {
	                test.log(Status.FAIL, "Mismatch in payment details." +
	                        "<br><b>Expected:</b> " + expectedRouteCode + ", " + expectedTravellerName + ", " + expectedPrice +
	                        "<br><b>Actual:</b> " + actualRouteCode + ", " + actualTravellerName + ", " + actualPrice);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Validation Failed", "One or more values did not match.");
	                Assert.fail("Validation failed: route/name/price mismatch.");
	            }

	        } else {
	            test.log(Status.FAIL, "Billing Information section not found.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Page Load Error", "Billing info not visible.");
	            Assert.fail("Payment page not shown.");
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception during validation: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception", "Error during validation process.");
	        Assert.fail("Exception in validatePayMentPage.");
	    }
	}


}

