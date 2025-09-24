package com.skytraveller.pageObjects;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class BookingPageRoundTrip extends BasePage  {
	public BookingPageRoundTrip(WebDriver driver) {
		super(driver);// calls BasePage constructor
	}

	public List<String> clickOnSelectSeat(ExtentTest test, int count) throws InterruptedException {
	    try {
	        WebElement seatSelection = driver.findElement(By.xpath("//div[contains(normalize-space(text()), 'Seat Selection')]"));
	        if (seatSelection.isDisplayed()) {
	            seatSelection.click();
	            test.log(Status.INFO, "Clicked On SelectSeat");
	            return validateSelectSeatSectionIsDisplayed(test, count);
	        } else {
	            test.log(Status.FAIL, "Seat Selection element is present but not visible");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                    "Seat Selection not visible in Booking Page", "SelectSeatNotVisible");
	            Assert.fail();
	        }
	    } catch (NoSuchElementException e) {
	        test.log(Status.FAIL, "Seat Selection element not found on the page");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "Seat Selection missing in Booking Page", "SelectSeatMissing");
	        Assert.fail();
	    }
	    return null;
	}

	public List<String> validateSelectSeatSectionIsDisplayed(ExtentTest test, int count) throws InterruptedException {
	    try {
	        WebElement travellerInfo = driver.findElement(By.xpath("//div[@class='traveller-name current-traveller-seat-selection']"));
	        WebElement seat = driver.findElement(By.xpath("//div[contains(@class,'flight-seat-map_container')]"));
	        
	        if (travellerInfo.isDisplayed() && seat.isDisplayed()) {
	            test.log(Status.INFO, "Select Seat section is displayed for this sector");
	            //return selectSeat(count, test);
	        } else {
	            test.log(Status.INFO, "Select Seat section is not displayed for this sector");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO,
	                "Seat Selection missing in Booking Page", "SelectSeatMissing");
	            Assert.fail();
	        }
	    } catch (NoSuchElementException e) {
	        test.log(Status.FAIL, "Select Seat section elements not found on the page");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "Seat Selection elements missing in Booking Page", "SelectSeatElementsMissing");
	        Assert.fail();
	    }
	    return null;
	}
	
	
	 // 1. Select available seats for a single flight
    public List<String> selectSeat(int count, ExtentTest test) throws InterruptedException {
        Thread.sleep(3000);
        List<WebElement> availableSeats = driver.findElements(By.xpath(
                "//div[contains(@class, 'flight-seat-map_seat') and not(contains(@class, 'flight-seat-map_booked-seat'))]/div"));
        List<String> selectedSeats = new ArrayList<>();

        if (availableSeats.isEmpty()) {
            test.log(Status.FAIL, "No available seats found to select.");
            return selectedSeats;
        }

        int seatsToSelect = Math.min(count, availableSeats.size());

        for (int i = 0; i < seatsToSelect; i++) {
            WebElement seat = availableSeats.get(i);
            try {
                String seatName = seat.getText();
                selectedSeats.add(seatName);
                Thread.sleep(2000);
                seat.click();
                Thread.sleep(2000);
                test.log(Status.INFO, "Selected seat: " + seatName);
                test.log(Status.INFO, "Return flight tab clicked.");
            } catch (Exception e) {
                test.log(Status.FAIL, "Failed to select seat at index " + i + ": " + e.getMessage());
            }
        }

        if (seatsToSelect < count) {
            test.log(Status.WARNING, "Requested " + count + " seats but only " + seatsToSelect + " were available.");
        }

        Thread.sleep(2000);
        return selectedSeats;
    }

    // 2. Extract selected seat info and price
    public Map<String, Object> getSelectSeatTravelInfoList() {
        List<String> travellerDetails = new ArrayList<>();
        double totalPrice = 0.0;

        try {
            Thread.sleep(3000);
            List<WebElement> travellerNames = driver.findElements(By.xpath("//div[contains(@class,'travellers-info')]//span"));
            List<WebElement> seatInfos = driver.findElements(By.xpath("//div[contains(@class,'travellers-info')]//small[contains(@class,'traveller-name')]"));

            int size = Math.min(travellerNames.size(), seatInfos.size());

            for (int i = 0; i < size; i++) {
                String name = travellerNames.get(i).getText().trim();
                String seatInfo = seatInfos.get(i).getText().trim();

                String seatNumber = "";
                String priceText = "";
                String[] parts = seatInfo.split("•");
                if (parts.length >= 2) {
                    seatNumber = parts[0].trim();
                    priceText = parts[1].trim();

                    String numericPrice = priceText.replaceAll("[^0-9.]", "");
                    try {
                        totalPrice += Double.parseDouble(numericPrice);
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Failed to parse price: " + seatInfo);
                    }
                }

                travellerDetails.add(name + " - " + seatNumber + " - " + priceText);
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to extract seat/traveller info: " + e.getMessage());
            e.printStackTrace();
        }
        /*  finally {
            try {
                driver.findElement(By.xpath("//button[text()='Done']")).click();
            } catch (Exception e) {
                System.out.println("⚠️ Failed to click 'Done' button: " + e.getMessage());
            }
            
        }
*/
        Map<String, Object> result = new HashMap<>();
        result.put("seatDetails", travellerDetails);
        result.put("totalPrice", totalPrice);
        return result;
    }

    /*
    public Map<String, Object> handleSeatSelectionForTrip(int adultCount, boolean isRoundTrip, ExtentTest test) throws InterruptedException {
        test.log(Status.INFO, "Selecting seat(s) for onward flight...");
        selectSeat(adultCount, test);

        if (isRoundTrip) {
            try {
                test.log(Status.INFO, "Clicking on return flight tab...");
                Thread.sleep(3000);
                // Click the second seat-sector (return flight)
                // driver.findElement(By.xpath("(//div[contains(@class,'seat-sector')])[2]")).click();
                Thread.sleep(3000);
                test.log(Status.INFO, "Return flight tab clicked.");
            } catch (Exception e) {
                test.log(Status.FAIL, "Failed to click return flight tab: " + e.getMessage());
            }

            test.log(Status.INFO, "Selecting seat(s) for return flight...");
            selectSeat(adultCount, test);
        }

        List<WebElement> stops = driver.findElements(By.xpath("//*[@class='seat-selection-section']/div[1]/div"));

        Map<String, Object> combinedSeatInfo = new HashMap<>();

        for (int i = 0; i < stops.size(); i++) {
            try {
                stops.get(i).click();
                Thread.sleep(1000); // Optional: Wait for UI to respond
                Map<String, Object> seatInfo = getSelectSeatTravelInfoList();

                // Option 1: Merge into a single map (if keys don't conflict)
                combinedSeatInfo.putAll(seatInfo);

                // Option 2: Alternatively, use a List<Map<String, Object>> if you expect multiple separate seatInfo objects
                // seatInfoList.add(seatInfo);

            } catch (Exception e) {
                test.log(Status.WARNING, "Failed to process stop " + i + ": " + e.getMessage());
            }
        }
        driver.findElement(By.xpath("//button[text()='Done']")).click();
        return combinedSeatInfo;
    }
*/
    public Map<String, Object> handleSeatSelectionForTrip(int adultCount, boolean isRoundTrip, ExtentTest test) throws InterruptedException {
        test.log(Status.INFO, "Selecting seat(s) for onward flight...");
        selectSeat(adultCount, test);

        if (isRoundTrip) {
            try {
                test.log(Status.INFO, "Clicking on return flight tab...");
                Thread.sleep(3000);
                // driver.findElement(By.xpath("(//div[contains(@class,'seat-sector')])[2]")).click(); // Uncomment if required
                Thread.sleep(3000);
                test.log(Status.INFO, "Return flight tab clicked.");
            } catch (Exception e) {
                test.log(Status.FAIL, "Failed to click return flight tab: " + e.getMessage());
                Assert.fail();
            }

            test.log(Status.INFO, "Selecting seat(s) for return flight...");
            selectSeat(adultCount, test);
        }

        List<WebElement> stops = driver.findElements(By.xpath("//*[@class='seat-selection-section']/div[1]/div"));

        List<Map<String, Object>> seatInfoList = new ArrayList<>();
        List<Double> totalAmounts = new ArrayList<>();
        double grandTotal = 0.0;

        for (int i = 0; i < stops.size(); i++) {
            try {
                stops.get(i).click();
                Thread.sleep(1000); // Optional: Wait for UI to respond

                Map<String, Object> seatInfo = getSelectSeatTravelInfoList();
                seatInfoList.add(seatInfo);

                // Extract totalAmount from the seatInfo
                if (seatInfo.containsKey("totalPrice")) {
                    try {
                        double amount = Double.parseDouble(seatInfo.get("totalPrice").toString());
                        totalAmounts.add(amount);
                        grandTotal += amount;
                    } catch (NumberFormatException e) {
                        test.log(Status.WARNING, "Invalid totalAmount format in stop " + i);
                        Assert.fail();
                    }
                } else {
                    test.log(Status.WARNING, "totalAmount not found in stop " + i);
                    Assert.fail();
                }

            } catch (Exception e) {
                test.log(Status.WARNING, "Failed to process stop " + i + ": " + e.getMessage());
                Assert.fail();
            }
        }

        driver.findElement(By.xpath("//button[text()='Done']")).click();

        // Final combined result
        Map<String, Object> result = new HashMap<>();
        result.put("seatDetails", seatInfoList);
        result.put("individualAmounts", totalAmounts);
        result.put("grandTotal", grandTotal);

        return result;
    }
    public String validateSeatPrice(double seatPrice, ExtentTest test) {
	    try {
	     
	        String totalAmountText = driver.findElement(By.xpath("//span[text()='Seats total']/following-sibling::span")).getText();

	        
	        totalAmountText = totalAmountText.replace("AED", "").trim(); // Result: "45.00"
	        double actualAmount = Double.parseDouble(totalAmountText); // Converts "45.00" to 45.00

	       
	        if (actualAmount == seatPrice) {
	            System.out.println("Seat price is valid: " + actualAmount);
	            test.log(Status.INFO, "Seat price is valid: " + actualAmount);
	            return totalAmountText;
	        } else {
	            System.out.println("Seat price mismatch! Expected: " + seatPrice + ", but got: " + actualAmount);
	            test.log(Status.INFO, "Seat price mismatch! Expected: " + seatPrice + ", but got: " + actualAmount);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "missMatchInseattotalprice", "missMatchInseattotalprice");
	            Assert.fail("Seat price does not match.");
	        }
	    } catch (Exception e) {
	        
	        System.out.println("Exception occurred during seat price validation: " + e.getMessage());
	        test.log(Status.FAIL, "Exception during seat price validation: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "exceptionInSeatPriceValidation", "Exception in seat price validation");
	        Assert.fail("Test failed due to exception: " + e.getMessage());
	    }
		return null;
	}
    
    public double validateTotalPrice(String expectedPrice, List<String> seatAmounts, ExtentTest test) {
        try {
            double basePrice = parsePrice(expectedPrice);

            double totalSeatAmount = 0.0;
            for (String seat : seatAmounts) {
                totalSeatAmount += parsePrice(seat);
            }

            double expectedTotal = basePrice + totalSeatAmount;

            WebElement totalFareElement = driver.findElement(By.xpath("//span[text()='Total Fare']/following-sibling::span/span"));
            String actualPriceText = totalFareElement.getText().trim();
            double actualPrice = parsePrice(actualPriceText);

            // Round to 2 decimal places to avoid floating point precision issues
            expectedTotal = Math.round(expectedTotal * 100.0) / 100.0;
            actualPrice = Math.round(actualPrice * 100.0) / 100.0;

            test.log(Status.INFO, "Expected total calculated: " + expectedTotal);
            test.log(Status.INFO, "Actual total fetched: " + actualPrice);

            if (Double.compare(actualPrice, expectedTotal) == 0) {
                test.log(Status.PASS, "Total price matches expected value: " + expectedTotal);
                return actualPrice;
            } else {
                test.log(Status.FAIL, "Price mismatch. Expected: " + expectedTotal + ", but found: " + actualPrice);
                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Price mismatch", "");
                return -1;
            }
        } catch (NoSuchElementException e) {
            test.log(Status.FAIL, "Total Fare element not found on the page.");
            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Element not found", "");
        } catch (Exception e) {
            test.log(Status.FAIL, "Unexpected error during price validation: " + e.getMessage());
            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Unexpected error", "");
        }
        return -1;
    }
 
 private double parsePrice(String priceStr) {
        // Remove everything except digits and decimal point
        priceStr = priceStr.replaceAll("[^\\d.]", "");
        if (priceStr.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(priceStr);
    }
 
 public List<String> clickOnAddMeals(ExtentTest test, int count) {
	    try {
	        WebElement addMealsButton = driver.findElement(By.xpath("//button/div[text()='Add Meals']"));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addMealsButton);

	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        wait.until(ExpectedConditions.elementToBeClickable(addMealsButton));

	        addMealsButton.click();
	        System.out.println("Clicked on 'Add Meals' button successfully.");

	        // Wait for meal selection page to load - example: wait for the meal grid element
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[contains(@class,'travellers-info')]/parent::div//div)[4]")));

	       // return verifyMealsPageIsDisplayed(test, count);

	    } catch (Exception e) {
	        System.out.println("Exception occurred while clicking on 'Add Meals' button: " + e.getMessage());
	        Assert.fail("Failed to click on 'Add Meals' button: " + e.getMessage());
	    }
	    return Collections.emptyList();  // safer than null
	}
	public List<String> verifyMealsPageIsDisplayed(ExtentTest test, int count) throws InterruptedException {
	    List<String> selectedMeals = new ArrayList<>();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    try {
	        WebElement mealSelectGrid = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("(//div[contains(@class,'travellers-info')]/parent::div//div)[4]")));

	        List<WebElement> availableMeals = driver.findElements(By.xpath("//p[contains(@class,'light-booking-ssr-meal-amount')]"));

	        if (availableMeals.isEmpty()) {
	            test.log(Status.FAIL, "No available meal found to select.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "No available meal in Booking Page", "NoAvailableMeal");
	            return selectedMeals;
	        }

	        int mealsToSelect = Math.min(count, availableMeals.size());

	        for (int i = 0; i < mealsToSelect; i++) {
	            WebElement meal = availableMeals.get(i);
	            try {
	                String mealName = meal.getText();
	                wait.until(ExpectedConditions.elementToBeClickable(meal));
	                meal.findElement(By.xpath("./following-sibling::button[text()='Add']")).click();
	                selectedMeals.add(mealName);
	                test.log(Status.INFO, "Selected meal: " + mealName);
	            } catch (Exception e) {
	                test.log(Status.FAIL, "Failed to select meal at index " + i + " due to: " + e.getMessage());
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                    "Meal selection failed", "MealSelectionFailed_Index_" + i);
	            }
	        }

	        if (mealsToSelect < count) {
	            test.log(Status.WARNING, "Requested " + count + " meals but only " + mealsToSelect + " were available.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.WARNING,
	                "Fewer meals available than requested", "FewerMealsAvailable");
	        }

	    } catch (TimeoutException | NoSuchElementException e) {
	        test.log(Status.FAIL, "Meal selection grid element not found or not visible.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "Meal grid not found or not visible", "MealGridNotFoundOrNotVisible");
	    }

	    return selectedMeals;
	}
	public String validateMealtPrice(double seatPrice, ExtentTest test) {
	    try {
	     
	        String totalAmountText = driver.findElement(By.xpath("//span[text()='Meal total']/following-sibling::span")).getText();

	        
	        totalAmountText = totalAmountText.replace("AED", "").trim(); // Result: "45.00"
	        double actualAmount = Double.parseDouble(totalAmountText); // Converts "45.00" to 45.00

	       
	        if (actualAmount == seatPrice) {
	            System.out.println(" meal is valid: " + actualAmount);
	            test.log(Status.INFO, " meal is valid: " + actualAmount);
	            return totalAmountText;
	        } else {
	            System.out.println(" mealprice mismatch! Expected: " + seatPrice + ", but got: " + actualAmount);
	            test.log(Status.INFO, "meal price mismatch! Expected: " + seatPrice + ", but got: " + actualAmount);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "missMatchInMealtotalprice", "missMatchInMealtotalprice");
	            Assert.fail("Meal price does not match.");
	        }
	    } catch (Exception e) {
	        
	        System.out.println("Exception occurred during meal price validation: " + e.getMessage());
	        test.log(Status.FAIL, "Exception during meal price validation: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "exceptionInMealPriceValidation", "Exception in meal price validation");
	        Assert.fail("Test failed due to exception: " + e.getMessage());
	    }
		return null;
	}
	public Map<String, Object> handleMealtSelectionForTrip(int count, boolean isRoundTrip, ExtentTest test) throws InterruptedException {
	    test.log(Status.INFO, "Selecting meal(s) for onward flight...");
	    verifyMealsPageIsDisplayed(test, count);

	    if (isRoundTrip) {
	        try {
	            test.log(Status.INFO, "Clicking on return flight tab...");
	            Thread.sleep(3000);
	            // driver.findElement(By.xpath("(//div[contains(@class,'seat-sector')])[2]")).click();
	            Thread.sleep(3000);
	            test.log(Status.INFO, "Return flight tab clicked.");
	        } catch (Exception e) {
	            test.log(Status.FAIL, "Failed to click return flight tab: " + e.getMessage());
	            Assert.fail();
	        }

	        test.log(Status.INFO, "Selecting meal(s) for return flight...");
	        verifyMealsPageIsDisplayed(test, count);
	    }

	    List<WebElement> stops = driver.findElements(By.xpath("//*[@class='seat-selection-section']/div[1]/div"));

	    List<Map<String, Object>> mealInfoList = new ArrayList<>();
	    List<Double> totalAmounts = new ArrayList<>();
	    double grandTotal = 0.0;

	    for (int i = 0; i < stops.size(); i++) {
	        try {
	            stops.get(i).click();
	            Thread.sleep(1000);

	            // You should select meals again here for each stop
	            List<String> selectedMeals = verifyMealsPageIsDisplayed(test, count);
	            double totalMealPrice = getTotalMealPrice();

	            // Prepare map for this stop
	            Map<String, Object> mealInfo = new HashMap<>();
	            mealInfo.put("stop", i + 1);
	            mealInfo.put("selectedMeals", selectedMeals);
	            mealInfo.put("totalPrice", totalMealPrice);

	            mealInfoList.add(mealInfo);
	            totalAmounts.add(totalMealPrice);
	            grandTotal += totalMealPrice;

	        } catch (Exception e) {
	            test.log(Status.WARNING, "Failed to process stop " + i + ": " + e.getMessage());
	            Assert.fail("Error during meal processing for stop " + i);
	        }
	    }

	    try {
	        driver.findElement(By.xpath("//button[text()='Done']")).click();
	    } catch (Exception e) {
	        System.out.println("⚠️ Failed to click 'Done' button: " + e.getMessage());
	    }

	    Map<String, Object> result = new HashMap<>();
	    result.put("mealDetails", mealInfoList);
	    result.put("individualAmounts", totalAmounts);
	    result.put("grandTotal", grandTotal);

	    return result;
	}
	public double getTotalMealPrice() {
	    double totalPrice = 0.0;

	    try {
	        Thread.sleep(3000);

	        List<WebElement> mealInfos = driver.findElements(By.xpath("//ul[@class='selected-meals']//span"));

	        Pattern pricePattern = Pattern.compile("AED\\s*([\\d,.]+)");

	        for (WebElement mealInfo : mealInfos) {
	            String text = mealInfo.getText().trim();
	            Matcher matcher = pricePattern.matcher(text);
	            if (matcher.find()) {
	                String priceStr = matcher.group(1).replace(",", ""); // Remove commas if any
	                try {
	                    double price = Double.parseDouble(priceStr);
	                    totalPrice += price;
	                } catch (NumberFormatException e) {
	                    System.out.println("⚠️ Failed to parse price: " + priceStr);
	                }
	            }
	        }
          Thread.sleep(2000);
	        //driver.findElement(By.xpath("//button[text()='Done']")).click();
	    } catch (Exception e) {
	        System.out.println("❌ Failed to extract seat/traveller info: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        try {
	            driver.findElement(By.xpath("//button[text()='Done']")).click();
	        } catch (Exception e) {
	            System.out.println("⚠️ Failed to click 'Done' button: " + e.getMessage());
	        }
	    }

	    return totalPrice;
	}
	
}
