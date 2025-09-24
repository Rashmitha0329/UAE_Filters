package com.skytraveller.pageObjects;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class BookingPage extends BasePage  {
	public BookingPage(WebDriver driver) {
		super(driver);// calls BasePage constructor
	}
	
	
	public void validateLogoInBookingPage(WebDriver driver, String imageXpath, int expectedWidth, int expectedHeight, ExtentTest test) {
	    try {
	        WebElement image = driver.findElement(By.xpath(imageXpath));
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        Long naturalWidth = (Long) js.executeScript("return arguments[0].naturalWidth;", image);
	        Long naturalHeight = (Long) js.executeScript("return arguments[0].naturalHeight;", image);

	        System.out.println("Intrinsic size: " + naturalWidth + " × " + naturalHeight);

	        if (naturalWidth == expectedWidth && naturalHeight == expectedHeight) {
	            System.out.println("✅ Image intrinsic size is valid In Booking Page");
	            test.log(Status.PASS, "✅ Image is uploaded and intrinsic size is valid in Booking Page: "
	                    + naturalWidth + "×" + naturalHeight);
	        } else {
	            System.out.println("❌ Image intrinsic size is NOT valid In Booking Page");
	            test.log(Status.FAIL, "❌ Image is uploaded but intrinsic size is invalid in Booking Page. "
	                    + "Expected: " + expectedWidth + "×" + expectedHeight +
	                    ", Found: " + naturalWidth + "×" + naturalHeight);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                    "Intrinsic size mismatch in Booking Page", "ImageIntrinsicSizeInvalidInBookingPage");
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Exception in validateImageIntrinsicSize: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Exception during intrinsic image validation in Booking Page: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "Exception in image validation In BookingPage", "ImageIntrinsicSizeExceptionInBookingPage");
	        Assert.fail();
	    }
	}
	
	public void clickOnContinueButton() {
	    WebElement continueButton = driver.findElement(By.xpath("//button[text()='Continue']"));

	    // Scroll into view
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", continueButton);

	    // Optional: wait briefly to ensure stability
	    try {
	        Thread.sleep(300); // optional, small wait
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt();
	    }

	    // Click the button
	    continueButton.click();
	}
	
	public void validateTravellerDetailsIsDisplayed(ExtentTest test) {
	    try {
	        WebElement travellerDetails = driver.findElement(By.xpath("//div[@class='p-3 pt-0']"));

	        if (travellerDetails.isDisplayed()) {
	            test.log(Status.PASS, "✅ Traveller details section is displayed on the booking page.");
	        } else {
	            test.log(Status.FAIL, "❌ Traveller details section is NOT visible on the booking page.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "Traveller details section not visible", "TravellerDetailsMissing");
	            Assert.fail("Traveller details section not visible.");
	        }
	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Exception occurred while validating traveller details section: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "Exception in traveller details validation", "TravellerDetailsValidationException");
	        Assert.fail("Exception during traveller details validation.");
	    }
	}

	public void addMobileCode(String code) {
	    try {
	        WebElement countryCodeInput = driver.findElement(By.xpath("//label[text()='Country Code']/parent::div//input"));
	        countryCodeInput.sendKeys(code, Keys.ENTER);
	    } catch (Exception e) {
	        System.out.println("❌ Failed to enter mobile code: " + e.getMessage());
	        e.printStackTrace();
	        Assert.fail("Failed to enter mobile code: " + e.getMessage());
	    }
	}

	public void addMobileNumber(String number) {
	    try {
	        WebElement mobileInput = driver.findElement(By.xpath("//label[text()='Mobile Number']/parent::div//input"));
	        mobileInput.sendKeys(number);
	    } catch (Exception e) {
	        System.out.println("❌ Failed to enter mobile number: " + e.getMessage());
	        e.printStackTrace();
	        Assert.fail("Failed to enter mobile number: " + e.getMessage());
	    }
	}

	public void addEmail(String email) {
	    try {
	        WebElement emailInput = driver.findElement(By.xpath("//label[text()='Email ID']/parent::div//input"));
	        emailInput.sendKeys(email);
	    } catch (Exception e) {
	        System.out.println("❌ Failed to enter email: " + e.getMessage());
	        e.printStackTrace();
	        Assert.fail("Failed to enter email: " + e.getMessage());
	    }
	}
	
	public String generateRandomMobileNumber() {
	    Random random = new Random();
	    int firstDigit = random.nextInt(9) + 1; // Ensure it doesn't start with 0
	    long rest = 100000000L + random.nextLong(900000000L); // 9 more digits
	    return String.valueOf(firstDigit) + rest;
	}

	public String generateRandomEmail() {
	    String uuid = UUID.randomUUID().toString().substring(0, 8); // Get random 8-char string
	    return "user" + uuid + "@gmail.com";
	}

	/*
	public void selectTitle() {
	    List<WebElement> selectTitleInputs = driver.findElements(By.xpath("//label[text()='Select Title']/parent::div//input"));

	    for (WebElement input : selectTitleInputs) {
	        try {
	            // Scroll input into view
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);

	            // Wait until clickable
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	            wait.until(ExpectedConditions.elementToBeClickable(input));

	            try {
	                input.click(); // Try normal click
	            } catch (ElementClickInterceptedException e) {
	                // Fallback: JS click if blocked
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
	            }

	            // Wait for the dropdown menu to appear
	            WebElement dropDownMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath("//div[contains(@class,'menu')]")));

	            List<WebElement> options = dropDownMenu.findElements(By.xpath(".//div[@role='option']"));
	            if (!options.isEmpty()) {
	                options.get(0).click(); // Select first title
	            } else {
	                System.out.println("⚠️ No title options available.");
	            }

	        } catch (Exception e) {
	            System.out.println("❌ Failed to select title: " + e.getMessage());
	            e.printStackTrace();
	            Assert.fail("Failed to select title dropdown due to exception.");
	        }
	    }
	}
*/
	public void selectTitle() {
	    By titleInputsLocator = By.xpath("//label[text()='Select Title']/parent::div//input");

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    // Retry up to 3 times in case of stale elements
	    int attempts = 0;
	    while (attempts < 3) {
	        try {
	            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(titleInputsLocator));
	            
	            // Scroll into view
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);
	            
	            // Click safely
	            try {
	                input.click();
	            } catch (ElementClickInterceptedException e) {
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
	            }

	            // Wait for the dropdown to appear
	            WebElement dropDownMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//div[contains(@class,'menu')]")
	            ));

	            List<WebElement> options = dropDownMenu.findElements(By.xpath(".//div[@role='option']"));
	            if (!options.isEmpty()) {
	                options.get(0).click();
	            } else {
	                System.out.println("⚠️ No title options available.");
	            }

	            // Completed without exception. Exit loop.
	            return;
	        } catch (StaleElementReferenceException e) {
	            System.out.println("Attempt " + (attempts + 1) + " failed due to stale element. Retrying...");
	        } catch (Exception e) {
	            System.out.println("❌ Failed to select title: " + e.getMessage());
	            e.printStackTrace();
	            Assert.fail("Failed to select title dropdown due to exception.");
	        }
	        attempts++;
	    }

	    Assert.fail("Failed to select title after " + attempts + " attempts due to stale elements.");
	}

	public void firstNameForAllFields() {
	    List<WebElement> firstNameInputs = driver.findElements(By.xpath("//label[text()='First name']/parent::div//input"));

	    for (WebElement input : firstNameInputs) {
	        try {
	            // Generate a new random name for each input
	            String randomName = generateRandomName(5);  // e.g., "Alric", "Zoeya"

	            // Scroll into view
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);

	            // Wait until clickable
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	            wait.until(ExpectedConditions.elementToBeClickable(input));

	            // Clear and enter name + press TAB to move to next input
	            input.clear();
	            input.sendKeys(randomName + Keys.TAB);

	            System.out.println("Entered first name: " + randomName);

	        } catch (Exception e) {
	            System.out.println("❌ Failed to enter first name: " + e.getMessage());
	            e.printStackTrace();
	            Assert.fail("Failed to enter first name due to exception.");
	        }
	    }
	}

	//this method tccp10 to enter name which is pass from excel
	public void firstNameForAllFields(List<String> dobList) {
	    List<WebElement> firstNameInputs = driver.findElements(By.xpath("//label[text()='First name']/parent::div//input"));

	    int count = Math.min(firstNameInputs.size(), dobList.size());

	    for (int i = 0; i < count; i++) {
	        WebElement input = firstNameInputs.get(i);
	        String nameToEnter = dobList.get(i);

	        try {
	            // Scroll into view
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);

	            // Wait until clickable
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	            wait.until(ExpectedConditions.elementToBeClickable(input));

	            // Clear and enter the name + TAB
	            input.clear();
	            input.sendKeys(nameToEnter + Keys.TAB);

	            System.out.println("Entered first name: " + nameToEnter);

	        } catch (Exception e) {
	            System.out.println("❌ Failed to enter first name: " + e.getMessage());
	            e.printStackTrace();
	            Assert.fail("Failed to enter first name due to exception.");
	        }
	    }
	}

//helper method generate name
	public String generateRandomName(int length) {
	    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	    StringBuilder name = new StringBuilder();
	    Random random = new Random();

	    for (int i = 0; i < length; i++) {
	        name.append(alphabet.charAt(random.nextInt(alphabet.length())));
	    }
	    return name.toString();
	}

	public void lastNameForAllFields() {
	    List<WebElement> firstNameInputs = driver.findElements(By.xpath("//label[text()='Last name']/parent::div//input"));

	    for (WebElement input : firstNameInputs) {
	        try {
	            // Generate a new random name for each input
	            String randomName = generateRandomName(5);  // e.g., "Alric", "Zoeya"

	            // Scroll into view
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);

	            // Wait until clickable
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	            wait.until(ExpectedConditions.elementToBeClickable(input));

	            // Clear and enter name + press TAB to move to next input
	            input.clear();
	            input.sendKeys(randomName + Keys.TAB);

	            System.out.println("Entered Last name: " + randomName);

	        } catch (Exception e) {
	            System.out.println("❌ Failed to enter last name: " + e.getMessage());
	            e.printStackTrace();
	            Assert.fail("Failed to enter last name due to exception.");
	        }
	    }
	}
	
	
	/*
	public void selectDateofBirth()
	{
	    List<WebElement> dob = driver.findElements(By.xpath("//label[text()='Date of Birth']/parent::div//div[@class='react-datepicker__input-container']/input"));
	    
	    for(int i = 0; i < dob.size(); i++)
	    {
	        WebElement dateOfBirth = dob.get(i);
	        dateOfBirth.click();

	       for(int j=0;j< dob.size();j++)
	       {
	    	   WebElement monthDropdown = driver.findElement(By.className("react-datepicker__month-select"));

	    	
	    	Select selectMonth = new Select(monthDropdown);

	    	
	    	selectMonth.selectByVisibleText("August");
	    	
	    	
WebElement yearDropdown = driver.findElement(By.className("react-datepicker__month-select"));

	    	
	    	Select yearMonth = new Select(monthDropdown);

	    	
	    	yearMonth.selectByVisibleText("2000");
	    	
	    	
	    	List<WebElement> allDays = driver.findElements(By.xpath("//div[contains(@class,'react-datepicker__day') and not(contains(@class,'react-datepicker__day--outside-month'))]"));

	        for (WebElement d : allDays) {
	            if (d.getText().equals(day)) {
	                d.click();
	                break;
	            }
	        }
	    	
	    	
	       }
	    }
	}
*/
	/*
	public void selectDateofBirth(List<String> dobList) {
	    List<WebElement> dobInputs = driver.findElements(By.xpath("//label[text()='Date of Birth']/parent::div//div[@class='react-datepicker__input-container']/input"));

	    // Check sizes match
	    if (dobInputs.size() != dobList.size()) {
	        throw new IllegalArgumentException("Mismatch between DOB inputs and DOB data");
	    }

	    for (int i = 0; i < dobInputs.size(); i++) {
	        String[] dobParts = dobList.get(i).split(",");
	        String month = dobParts[0].trim();
	        String year = dobParts[1].trim();
	        String day = dobParts[2].trim();

	        WebElement dobInput = dobInputs.get(i);
	        dobInput.click();

	        
	        Select selectMonth = new Select(driver.findElement(By.className("react-datepicker__month-select")));
	        selectMonth.selectByVisibleText(month);

	     
	        Select selectYear = new Select(driver.findElement(By.className("react-datepicker__year-select")));
	        selectYear.selectByVisibleText(year);

	       
	        List<WebElement> days = driver.findElements(By.xpath("//div[contains(@class,'react-datepicker__day') and not(contains(@class,'react-datepicker__day--outside-month'))]"));
	        for (WebElement d : days) {
	            if (d.getText().equals(day)) {
	                d.click();
	                break;
	            }
	        }

	        // Optional: small wait here if needed before next input
	    }
	}
*/
	public void selectDateofBirth(List<String> dobList) {
	    List<WebElement> dobInputs = driver.findElements(By.xpath("//label[text()='Date of Birth']/parent::div//div[@class='react-datepicker__input-container']/input"));

	    if (dobInputs.size() != dobList.size()) {
	        throw new IllegalArgumentException("Mismatch between DOB inputs and DOB data");
	    }

	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    for (int i = 0; i < dobInputs.size(); i++) {
	        try {
	            String[] dobParts = dobList.get(i).split(",");
	            String month = dobParts[0].trim();
	            String year = dobParts[1].trim();
	            String day = dobParts[2].trim();

	            WebElement dobInput = dobInputs.get(i);

	            
	            js.executeScript("arguments[0].scrollIntoView(true);", dobInput);

	            Thread.sleep(3000);
	            
	            dobInput.click();

	            Select selectMonth = new Select(driver.findElement(By.className("react-datepicker__month-select")));
	            selectMonth.selectByVisibleText(month);

	            Select selectYear = new Select(driver.findElement(By.className("react-datepicker__year-select")));
	            selectYear.selectByVisibleText(year);

	            List<WebElement> days = driver.findElements(By.xpath("//div[contains(@class,'react-datepicker__day') and not(contains(@class,'react-datepicker__day--outside-month'))]"));
	            for (WebElement d : days) {
	                if (d.getText().equals(day)) {
	                    d.click();
	                    break;
	                }
	            }

	            Thread.sleep(2000); // Wait for UI stability if needed

	        } catch (Exception e) {
	            System.err.println("Failed to select DOB for input index " + i + ": " + e.getMessage());
	            
	        }
	    }
	}

	
//	public void selectDateOfBirth(String day, String month, String year) {
//	    try {
//	        // Wait for datepicker to be visible
//	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("react-datepicker")));
//
//	        // Select year
//	        WebElement yearDropdown = driver.findElement(By.className("react-datepicker__year-select"));
//	        Select selectYear = new Select(yearDropdown);
//	        selectYear.selectByVisibleText(year);
//
//	        // Select month (January = 0, February = 1, ..., December = 11)
//	        WebElement monthDropdown = driver.findElement(By.className("react-datepicker__month-select"));
//	        Select selectMonth = new Select(monthDropdown);
//	        selectMonth.selectByVisibleText(month);  // e.g., "May"
//
//	        // Select day
//	        List<WebElement> allDays = driver.findElements(By.xpath("//div[contains(@class,'react-datepicker__day') and not(contains(@class,'outside-month'))]"));
//	        for (WebElement d : allDays) {
//	            if (d.getText().equals(day)) {
//	                d.click();
//	                break;
//	            }
//	        }
//
//	    } catch (Exception e) {
//	        System.out.println("❌ Failed to select DOB: " + e.getMessage());
//	        Assert.fail("Failed to select date of birth.");
//	    }
//	}
	
	public void enterPassportNumber() {
	    List<WebElement> passportInputs = driver.findElements(By.xpath("//label[text()='Passport Number']/parent::div//input"));

	    for (WebElement input : passportInputs) {
	        try {
	            
	            String passportNumber = generateRandomPassportNumber();  // e.g., "X7Q92Z1B3"

	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);

	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	            wait.until(ExpectedConditions.elementToBeClickable(input));

	            
	            input.clear();
	            input.sendKeys(passportNumber + Keys.TAB);

	            System.out.println("Entered passport number: " + passportNumber);

	        } catch (Exception e) {
	            System.out.println("❌ Failed to enter passport number: " + e.getMessage());
	            e.printStackTrace();
	            Assert.fail("Failed to enter passport number due to exception.");
	        }
	    }
	}
	public String generateRandomPassportNumber() {
	    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    int length = 9;
	    SecureRandom random = new SecureRandom();
	    StringBuilder passport = new StringBuilder(length);
	    for (int i = 0; i < length; i++) {
	        passport.append(chars.charAt(random.nextInt(chars.length())));
	    }
	    return passport.toString();
	}

	public void passportIssueDateofBirth(List<String> dobList) {
	    List<WebElement> dobInputs = driver.findElements(By.xpath("//label[text()='Passport Date of Issue']/parent::div//div[@class='react-datepicker__input-container']/input"));

	    if (dobInputs.size() != dobList.size()) {
	        throw new IllegalArgumentException("Mismatch between DOB inputs and DOB data");
	    }

	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    for (int i = 0; i < dobInputs.size(); i++) {
	        try {
	            String[] dobParts = dobList.get(i).split(",");
	            String month = dobParts[0].trim();
	            String year = dobParts[1].trim();
	            String day = dobParts[2].trim();

	            WebElement dobInput = dobInputs.get(i);

	            
	            js.executeScript("arguments[0].scrollIntoView(true);", dobInput);

	            Thread.sleep(3000);
	            
	            dobInput.click();

	            Select selectMonth = new Select(driver.findElement(By.className("react-datepicker__month-select")));
	            selectMonth.selectByVisibleText(month);

	            Select selectYear = new Select(driver.findElement(By.className("react-datepicker__year-select")));
	            selectYear.selectByVisibleText(year);

	            List<WebElement> days = driver.findElements(By.xpath("//div[contains(@class,'react-datepicker__day') and not(contains(@class,'react-datepicker__day--outside-month'))]"));
	            for (WebElement d : days) {
	                if (d.getText().equals(day)) {
	                    d.click();
	                    break;
	                }
	            }

	            Thread.sleep(2000); // Wait for UI stability if needed

	        } catch (Exception e) {
	            System.err.println("Failed to select DOB for input index " + i + ": " + e.getMessage());
	            
	        }
	    }
	}
	public void passportDateOfExpiry(List<String> dobList) {
	    List<WebElement> dobInputs = driver.findElements(By.xpath("//label[text()='Passport Date of Expiry']/parent::div//div[@class='react-datepicker__input-container']/input"));

	    if (dobInputs.size() != dobList.size()) {
	        throw new IllegalArgumentException("Mismatch between DOB inputs and DOB data");
	    }

	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    for (int i = 0; i < dobInputs.size(); i++) {
	        try {
	            String[] dobParts = dobList.get(i).split(",");
	            String month = dobParts[0].trim();
	            String year = dobParts[1].trim();
	            String day = dobParts[2].trim();

	            WebElement dobInput = dobInputs.get(i);

	            
	            js.executeScript("arguments[0].scrollIntoView(true);", dobInput);

	            Thread.sleep(3000);
	            
	            dobInput.click();

	            Select selectMonth = new Select(driver.findElement(By.className("react-datepicker__month-select")));
	            selectMonth.selectByVisibleText(month);

	            Select selectYear = new Select(driver.findElement(By.className("react-datepicker__year-select")));
	            selectYear.selectByVisibleText(year);

	            List<WebElement> days = driver.findElements(By.xpath("//div[contains(@class,'react-datepicker__day') and not(contains(@class,'react-datepicker__day--outside-month'))]"));
	            for (WebElement d : days) {
	                if (d.getText().equals(day)) {
	                    d.click();
	                    break;
	                }
	            }

	            Thread.sleep(2000); // Wait for UI stability if needed

	        } catch (Exception e) {
	            System.err.println("Failed to select DOB for input index " + i + ": " + e.getMessage());
	            
	        }
	    }
	}
	
	public void passportPlaceOfIssue() {
	    List<WebElement> passportInputs = driver.findElements(By.xpath("//label[text()='Passport Place Of Issue']/parent::div//input"));

	    for (WebElement input : passportInputs) {
	        try {
	            String placeOfIssue = generateRandomPlaceOfIssue(); // e.g., "Mumbai"

	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);

	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	            wait.until(ExpectedConditions.elementToBeClickable(input));

	            input.clear();
	            input.sendKeys(placeOfIssue + Keys.TAB);

	            System.out.println("Entered passport place of issue: " + placeOfIssue);

	        } catch (Exception e) {
	            System.out.println("❌ Failed to enter passport place of issue: " + e.getMessage());
	            e.printStackTrace();
	            Assert.fail("Failed to enter passport place of issue due to exception.");
	        }
	    }
	}
	public String generateRandomPlaceOfIssue() {
	    String[] places = {"Delhi", "Mumbai", "New York", "London", "Paris", "Dubai", "Tokyo", "Sydney"};
	    return places[new SecureRandom().nextInt(places.length)];
	}
	
	/*
	public List<String> clickOnselectSeat(ExtentTest test,int index)
	{
		
		try {
		    WebElement seatSelection = driver.findElement(By.xpath("//div[contains(normalize-space(text()), 'Seat Selection')]"));
		    if (seatSelection.isDisplayed()) {
		        seatSelection.click();
		        test.log(Status.INFO, "Clicked On SelectSeat");
		        List<String> SelectedSeat=  validateSelectSeatSectionIsDisplayed(test,index);
		        return SelectedSeat;
		    } else {
		        test.log(Status.FAIL, "Seat Selection element is present but not visible");
		        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
		                "Seat Selection not visible in Booking Page", "SelectSeatNotVisible");
		    }
		} catch (NoSuchElementException e) {
		    test.log(Status.FAIL, "Seat Selection element not found on the page");
		    ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
		            "Seat Selection missing in Booking Page", "SelectSeatMissing");
		}
		return null;

	}
	
	public List<String> validateSelectSeatSectionIsDisplayed(ExtentTest test,int index) {
	    try {
	        WebElement travellerInfo = driver.findElement(By.xpath("//div[@class='traveller-name current-traveller-seat-selection']"));
	        WebElement seat = driver.findElement(By.xpath("//div[contains(@class,'flight-seat-map_container')]"));
	        
	        if(travellerInfo.isDisplayed() && seat.isDisplayed()) {
	            test.log(Status.INFO, "Select Seat section is displayed for this sector");
	            List<String> SelectedSeat = selectSeat(index,test);
	            return SelectedSeat;
	        } else {
	            test.log(Status.INFO, "Select Seat section is not displayed for this sector");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO,
	                "Seat Selection missing in Booking Page", "SelectSeatMissing");
	        }
	    } catch (NoSuchElementException e) {
	        test.log(Status.FAIL, "Select Seat section elements not found on the page");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "Seat Selection elements missing in Booking Page", "SelectSeatElementsMissing");
	    }
		return null;
	}

	public List<String> selectSeat(int count, ExtentTest test) {
	    List<WebElement> availableSeats = driver.findElements(By.xpath(
	        "//div[contains(@class, 'flight-seat-map_seat') and not(contains(@class, 'flight-seat-map_booked-seat'))]/div"));
	    List<String> selectedSeats = new ArrayList<>();

	    if (availableSeats.isEmpty()) {
	        test.log(Status.FAIL, "No available seats found to select.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "No available seats in Booking Page", "NoAvailableSeats");
	        return selectedSeats;  // empty list
	    }

	    int seatsToSelect = Math.min(count, availableSeats.size());

	    for (int i = 0; i < seatsToSelect; i++) {
	        WebElement seat = availableSeats.get(i);
	        try {
	            String seatName = seat.findElement(By.xpath(".//div[@class='seat-text']")).getText();
	            selectedSeats.add(seatName);
	            seat.click();
	            test.log(Status.INFO, "Selected seat: " + seatName);
	        } catch (NoSuchElementException e) {
	            test.log(Status.FAIL, "Seat text element not found for seat index " + i);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "Seat text element missing", "SeatTextMissing_Index_" + i);
	        } catch (Exception e) {
	            test.log(Status.FAIL, "Failed to click/select seat at index " + i + " due to: " + e.getMessage());
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "Seat selection failed", "SeatSelectionFailed_Index_" + i);
	        }
	    }

	    if (seatsToSelect < count) {
	        test.log(Status.WARNING, "Requested " + count + " seats but only " + seatsToSelect + " were available.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.WARNING,
	            "Fewer seats available than requested", "FewerSeatsAvailable");
	    }

	    return selectedSeats;
	}
*/
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
	        }
	    } catch (NoSuchElementException e) {
	        test.log(Status.FAIL, "Seat Selection element not found on the page");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "Seat Selection missing in Booking Page", "SelectSeatMissing");
	    }
	    return null;
	}

	public List<String> validateSelectSeatSectionIsDisplayed(ExtentTest test, int count) throws InterruptedException {
	    try {
	        WebElement travellerInfo = driver.findElement(By.xpath("//div[@class='traveller-name current-traveller-seat-selection']"));
	        WebElement seat = driver.findElement(By.xpath("//div[contains(@class,'flight-seat-map_container')]"));
	        
	        if (travellerInfo.isDisplayed() && seat.isDisplayed()) {
	            test.log(Status.INFO, "Select Seat section is displayed for this sector");
	            return selectSeat(count, test);
	        } else {
	            test.log(Status.INFO, "Select Seat section is not displayed for this sector");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.INFO,
	                "Seat Selection missing in Booking Page", "SelectSeatMissing");
	        }
	    } catch (NoSuchElementException e) {
	        test.log(Status.FAIL, "Select Seat section elements not found on the page");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "Seat Selection elements missing in Booking Page", "SelectSeatElementsMissing");
	    }
	    return null;
	}

	public List<String> selectSeat(int count, ExtentTest test) throws InterruptedException {
		Thread.sleep(3000);
	    List<WebElement> availableSeats = driver.findElements(By.xpath(
	        "//div[contains(@class, 'flight-seat-map_seat') and not(contains(@class, 'flight-seat-map_booked-seat'))]/div"));
	    List<String> selectedSeats = new ArrayList<>();

	    if (availableSeats.isEmpty()) {
	        test.log(Status.FAIL, "No available seats found to select.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "No available seats in Booking Page", "NoAvailableSeats");
	        return selectedSeats;  // empty list
	    }

	    int seatsToSelect = Math.min(count, availableSeats.size());

	    for (int i = 0; i < seatsToSelect; i++) {
	        WebElement seat = availableSeats.get(i);
	        try {
	        //    String seatName = seat.findElement(By.xpath(".//div[@class='seat-text']")).getText();
	        	   String seatName = seat.getText();
	            selectedSeats.add(seatName);
	            Thread.sleep(3000);
	            seat.click();
	            test.log(Status.INFO, "Selected seat: " + seatName);
	        } catch (NoSuchElementException e) {
	            test.log(Status.FAIL, "Seat text element not found for seat index " + i);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "Seat text element missing", "SeatTextMissing_Index_" + i);
	        } catch (Exception e) {
	            test.log(Status.FAIL, "Failed to click/select seat at index " + i + " due to: " + e.getMessage());
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "Seat selection failed", "SeatSelectionFailed_Index_" + i);
	        }
	    }
Thread.sleep(2000);

	    
	    if (seatsToSelect < count) {
	        test.log(Status.WARNING, "Requested " + count + " seats but only " + seatsToSelect + " were available.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.WARNING,
	            "Fewer seats available than requested", "FewerSeatsAvailable");
	    }

	    return selectedSeats;
	}

	
//	public List<String> getSelectSeatTravelInfoList() {
//	    List<WebElement> travellerNames = driver.findElements(By.xpath("//div[contains(@class,'travellers-info')]//span"));
//	    List<WebElement> seatInfos = driver.findElements(By.xpath("//div[contains(@class,'travellers-info')]//div[contains(@class,'seat-info')]"));
//
//	    List<String> travellerDetails = new ArrayList<>();
//	    int size = Math.min(travellerNames.size(), seatInfos.size());
//
//	    for (int i = 0; i < size; i++) {
//	        String name = travellerNames.get(i).getText().trim();       // e.g., "tarun"
//	        String seatInfo = seatInfos.get(i).getText().trim();        // e.g., "3A • AED 45.00 info"
//	        travellerDetails.add(name + " - " + seatInfo);
//	    }
//
//	    driver.findElement(By.xpath("//button[text()='Done']")).click();
//	    return travellerDetails;
//	    
//	    /*
//	     * output
//	     [
//  "tarun - 3A • AED 45.00 info",
//  "rock - 7B • AED 35.00 info",
//  ...
//]
//
//	     */
//	}
	public List<String> getSelectSeatTravelInfoList() {
	    List<String> travellerDetails = new ArrayList<>();

	    try {
	    	Thread.sleep(3000);
	        List<WebElement> travellerNames = driver.findElements(By.xpath("//div[contains(@class,'travellers-info')]//span"));
	        List<WebElement> seatInfos = driver.findElements(By.xpath("//div[contains(@class,'travellers-info')]//small[contains(@class,'traveller-name')]"));
	      //  List<WebElement> seatInfos = driver.findElements(By.xpath("//div[contains(@class,'travellers-info')]//div[contains(@class,'seat-info')]"));

	        int size = Math.min(travellerNames.size(), seatInfos.size());

	        for (int i = 0; i < size; i++) {
	            String name = travellerNames.get(i).getText().trim();       // e.g., "tarun"
	            String seatInfo = seatInfos.get(i).getText().trim();        // e.g., "3A • AED 45.00 info"
	            travellerDetails.add(name + " - " + seatInfo);
	        }

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

	    return travellerDetails;
	}

/*
	public double validateAndSumPrices(List<String> seatnumber, List<String> seatinfo, List<String> dobList, ExtentTest test) {
	    double totalPrice = 0.0;
	    Pattern pricePattern = Pattern.compile("AED\\s*(\\d+(\\.\\d+)?)");

	    for (int i = 0; i < seatnumber.size(); i++) {
	        String seat = seatnumber.get(i);
	        String info = seatinfo.get(i);
	        boolean matchFound = false;

	        for (String dobEntry : dobList) {
	        	System.out.println(dobEntry);
	        	System.out.println(seat);
	        	System.out.println(info);
	            if (dobEntry.contains(seat) && dobEntry.contains(info)) {
	                matchFound = true;
	                break;
	            }
	        }

	        if (matchFound) {
	            test.log(Status.PASS, "✅ Match found for seat: " + seat + " with info: " + info);

	            Matcher matcher = pricePattern.matcher(info);
	            if (matcher.find()) {
	                String priceStr = matcher.group(1);
	                try {
	                    double price = Double.parseDouble(priceStr);
	                    totalPrice += price;
	                    test.log(Status.INFO, "Extracted price AED " + price + " from seat info: " + info);
	                } catch (NumberFormatException e) {
	                    test.log(Status.WARNING, "⚠️ Failed to parse price: " + priceStr);
	                }
	            } else {
	                test.log(Status.WARNING, "⚠️ Price pattern not found in info: " + info);
	            }

	        } else {
	            test.log(Status.FAIL, "❌ No match found for seat: " + seat + " with info: " + info);
	        }
	    }

	    test.log(Status.INFO, "Total summed price: AED " + totalPrice);
	    return totalPrice;
	}
	*/
	/*
	
	public double validateAndSumPrices(List<String> seatnumber, List<String> seatinfo, List<String> dobList, ExtentTest test) {
	    double totalPrice = 0.0;
	    Pattern pricePattern = Pattern.compile("(\\d+(\\.\\d{1,2})?)"); // matches numbers like 45.00

	    for (int i = 0; i < seatnumber.size(); i++) {
	        String seat = seatnumber.get(i);
	        String info = seatinfo.get(i);
	        boolean matchFound = false;
	        String expectedPattern = "";

	        // Extract price from info
	        Matcher matcher = pricePattern.matcher(info);
	        if (matcher.find()) {
	            String priceStr = matcher.group(1);
	            expectedPattern = seat + " • AED " + priceStr;

	            for (String dobEntry : dobList) {
	                if (dobEntry.contains(expectedPattern)) {
	                    matchFound = true;

	                    try {
	                        double price = Double.parseDouble(priceStr);
	                        totalPrice += price;

	                        test.log(Status.PASS, "✅ Match found: [" + dobEntry + "] for pattern: " + expectedPattern);
	                        test.log(Status.INFO, "Added price: AED " + price);

	                    } catch (NumberFormatException e) {
	                        test.log(Status.WARNING, "⚠️ Price format invalid: " + priceStr);
	                    }

	                    break; // Exit inner loop when matched
	                }
	            }

	            if (!matchFound) {
	                test.log(Status.FAIL, "❌ No match in dobList for expected pattern: " + expectedPattern);
	            }

	        } else {
	            test.log(Status.WARNING, "⚠️ Price not found in seatinfo: " + info);
	        }
	    }

	    test.log(Status.INFO, "💰 Total summed price: AED " + totalPrice);
	    return totalPrice;
	}
*/
	/*
	public double validateAndSumPrices(List<String> names, List<String> seatNumbers, List<String> dobList, ExtentTest test) {
	    double totalPrice = 0.0;
	    Pattern pricePattern = Pattern.compile("AED\\s*(\\d+(\\.\\d{1,2})?)");

	    for (int i = 0; i < names.size(); i++) {
	        String name = names.get(i).toLowerCase().trim();
	        String seat = seatNumbers.get(i).trim();
	        boolean matchFound = false;

	        for (String dobEntry : dobList) {
	            String lowerDobEntry = dobEntry.toLowerCase();  // for case-insensitive match

	            if (lowerDobEntry.contains(name) && lowerDobEntry.contains(seat)) {
	                matchFound = true;

	                Matcher matcher = pricePattern.matcher(dobEntry);
	                if (matcher.find()) {
	                    String priceStr = matcher.group(1);  // e.g., "45.00"
	                    try {
	                        double price = Double.parseDouble(priceStr);
	                        totalPrice += price;

	                        test.log(Status.PASS, "✅ Match found for name: " + name + " with seat: " + seat + " → Price: AED " + price);
	                        test.log(Status.INFO, "Added price: AED " + price + " from entry: " + dobEntry);
	                    } catch (NumberFormatException e) {
	                        test.log(Status.WARNING, "⚠️ Unable to parse price: " + priceStr + " for name: " + name);
	                    }
	                } else {
	                    test.log(Status.WARNING, "⚠️ Price not found in entry: " + dobEntry);
	                }

	                break; // break inner loop once matched
	            }
	        }

	        if (!matchFound) {
	            test.log(Status.FAIL, "❌ No match found for name: " + name + " with seat: " + seat);
	        }
	    }

	    test.log(Status.INFO, "💰 Total summed price: AED " + totalPrice);
	    return totalPrice;
	}
*/
	/*
	public double validateAndSumPrices(List<String> names, List<String> seatNumbers, List<String> dobList, ExtentTest test) {
	    double totalPrice = 0.0;
	    Pattern pricePattern = Pattern.compile("AED\\s*(\\d+(\\.\\d+)?)", Pattern.CASE_INSENSITIVE);

	    for (int i = 0; i < names.size(); i++) {
	        String name = names.get(i).toLowerCase().trim();
	        String seat = seatNumbers.get(i).trim();
	        boolean matchFound = false;

	        for (String dobEntry : dobList) {
	            String lowerDobEntry = dobEntry.toLowerCase();

	            test.log(Status.INFO, "Checking entry: '" + dobEntry + "' for name: '" + name + "' and seat: '" + seat + "'");

	            if (lowerDobEntry.contains(name) && lowerDobEntry.contains(seat)) {
	                matchFound = true;
	                test.log(Status.PASS, "✅ Match found for name: '" + name + "' with seat: '" + seat + "' in entry: '" + dobEntry + "'");

	                // Extract price
	                Matcher matcher = pricePattern.matcher(dobEntry);
	                if (matcher.find()) {
	                    String priceStr = matcher.group(1);
	                    try {
	                        double price = Double.parseDouble(priceStr);
	                        totalPrice += price;
	                        test.log(Status.INFO, "Extracted price AED " + price + " from entry: '" + dobEntry + "'");
	                    } catch (NumberFormatException e) {
	                        test.log(Status.WARNING, "⚠️ Failed to parse price: " + priceStr + " in entry: '" + dobEntry + "'");
	                    }
	                } else {
	                    test.log(Status.WARNING, "⚠️ Price pattern not found in entry: '" + dobEntry + "'");
	                }
	                break;  // Found the match for this name and seat, no need to check other dobEntries
	            }
	        }

	        if (!matchFound) {
	            test.log(Status.FAIL, "❌ No match found for name: '" + name + "' with seat: '" + seat + "'");
	        }
	    }

	    test.log(Status.INFO, "Total summed price: AED " + totalPrice);
	    return totalPrice;
	}
	*/
	public double validateAndSumPrices(List<String> names, List<String> seatNumbers, List<String> dobList, ExtentTest test) {
	    double totalPrice = 0.0;
	    Pattern pricePattern = Pattern.compile("AED\\s*(\\d+(\\.\\d+)?)", Pattern.CASE_INSENSITIVE);

	    for (int i = 0; i < names.size(); i++) {
	        String name = names.get(i).toLowerCase().trim();
	        String seat = seatNumbers.get(i).toLowerCase().trim();
	        boolean matchFound = false;

	        for (String dobEntry : dobList) {
	            String lowerDobEntry = dobEntry.toLowerCase();

	            test.log(Status.INFO, "Checking dobEntry: '" + dobEntry + "' for name: '" + name + "' and seat: '" + seat + "'");

	            String[] tokens = lowerDobEntry.split("[\\s•\\-]+");

	            boolean hasName = false, hasSeat = false;
	            for (String token : tokens) {
	                if (token.equals(name)) hasName = true;
	                if (token.equals(seat)) hasSeat = true;
	            }

	            if (hasName && hasSeat) {
	                matchFound = true;
	                test.log(Status.PASS, "✅ Match found for name: '" + name + "' with seat: '" + seat + "' in entry: '" + dobEntry + "'");

	                Matcher matcher = pricePattern.matcher(dobEntry);
	                if (matcher.find()) {
	                    String priceStr = matcher.group(1);
	                    try {
	                        double price = Double.parseDouble(priceStr);
	                        totalPrice += price;
	                        test.log(Status.INFO, "Extracted price AED " + price + " from entry: '" + dobEntry + "'");
	                    } catch (NumberFormatException e) {
	                        test.log(Status.WARNING, "⚠️ Failed to parse price: " + priceStr + " in entry: '" + dobEntry + "'");
	                    }
	                } else {
	                    test.log(Status.WARNING, "⚠️ Price pattern not found in entry: '" + dobEntry + "'");
	                }
	                break;
	            }
	        }

	        if (!matchFound) {
	            test.log(Status.FAIL, "❌ No match found for name: '" + name + "' with seat: '" + seat + "'");
	        }
	    }

	    test.log(Status.INFO, "Total summed price: AED " + totalPrice);
	    return totalPrice;
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
	
	
	public void clickOnEyeIcon() {
	    try {
	        
	        WebElement eyeIconButton = driver.findElement(By.xpath("//span[text()='FARE SUMMARY']/following-sibling::button"));

	        
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", eyeIconButton);

	     
	        Thread.sleep(500); 

	        // Click the eye icon
	        eyeIconButton.click();

	        System.out.println("Clicked on Eye Icon next to 'FARE SUMMARY'.");
	    } catch (Exception e) {
	        System.out.println("Exception occurred while clicking on Eye Icon: " + e.getMessage());

	       

	        Assert.fail("Failed to click on Eye Icon: " + e.getMessage());
	    }
	}

	public void validateTaxGrid(ExtentTest test) {
	    try {
	        WebElement fareBreakUpAndTax = driver.findElement(By.xpath("//span[text()='Total Payable']/following-sibling::span"));
	        
	        if (fareBreakUpAndTax.isDisplayed()) {
	            test.log(Status.INFO, "Fare breakUp and Tax is displayed");
	        } else {
	            test.log(Status.FAIL, "Fare breakUp and Tax is NOT displayed");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Element not displayed", "");
	        }
	    } catch (NoSuchElementException e) {
	        test.log(Status.FAIL, "Fare breakUp and Tax element not found");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Element not found", "");
	    } catch (Exception e) {
	        test.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Unexpected error", "");
	    }
	}

	/*
	public void validateTotalPrice(String expectedPrice, List<String> seatAmounts, ExtentTest test) {
	    try {
	        // Parse expected base price (e.g., "AED 499.12" -> 499.12)
	        double basePrice = parsePrice(expectedPrice);

	        // Sum all seat prices from the ArrayList
	        double totalSeatAmount = 0.0;
	        for (String seat : seatAmounts) {
	            totalSeatAmount += parsePrice(seat);
	        }

	        // Compute expected total
	        double expectedTotal = basePrice + totalSeatAmount;

	        // Get actual total fare from the web page
	        WebElement totalFareElement = driver.findElement(By.xpath("//span[text()='Total Fare']/following-sibling::span/span"));
	        String actualPriceText = totalFareElement.getText().trim();
	        double actualPrice = parsePrice(actualPriceText);

	        // Round both values to 2 decimal places to avoid floating-point issues
	        expectedTotal = Math.round(expectedTotal * 100.0) / 100.0;
	        actualPrice = Math.round(actualPrice * 100.0) / 100.0;

	        // Compare
	        if (Double.compare(actualPrice, expectedTotal) == 0) {
	            test.log(Status.PASS, "Total price matches expected value: " + expectedTotal);
	        } else {
	            test.log(Status.FAIL, "Price mismatch. Expected: " + expectedTotal + ", but found: " + actualPrice);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Price mismatch", "");
	        }
	    } catch (NoSuchElementException e) {
	        test.log(Status.FAIL, "Total Fare element not found on the page.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Element not found", "");
	    } catch (Exception e) {
	        test.log(Status.FAIL, "Unexpected error during price validation: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Unexpected error", "");
	    }
	}

	// Parses a price like "AED 499.12" into 499.12
	private double parsePrice(String priceStr) {
	    priceStr = priceStr.replaceAll("[^\\d.]", ""); // Remove everything except digits and dot
	    if (priceStr.isEmpty()) {
	        return 0.0;
	    }
	    return Double.parseDouble(priceStr);
	}
*/
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
	
	public void clickOnPayment(String option) {
	    try {
	        WebElement element = driver.findElement(By.xpath("//div[text()='" + option + "']"));
	        element.click();
	        System.out.println("Clicked on payment option: " + option);
	    } catch (NoSuchElementException e) {
	        System.err.println("Payment option not found: " + option);
	        Assert.fail();
	    } catch (Exception e) {
	        System.err.println("Error clicking on payment option '" + option + "': " + e.getMessage());
	        Assert.fail();
	    }
	}
	
	public void clickOnReadAndAccept() {
	    try {
	        WebElement checkbox = driver.findElement(By.xpath("//span[text()='I have read and accepted']/parent::div/preceding-sibling::input"));
	        checkbox.click();
	        System.out.println("Clicked on 'I have read and accepted' checkbox.");
	        Thread.sleep(3000);
	        driver.findElement(By.xpath("//button[text()='Confirm Booking']")).click();
	        
	    } catch (NoSuchElementException e) {
	        System.err.println("'I have read and accepted' checkbox not found.");
	        Assert.fail();
	    } catch (Exception e) {
	        System.err.println("Error clicking on 'I have read and accepted' checkbox: " + e.getMessage());
	        Assert.fail();
	    }
	}
	
	public ArrayList<String> travelDetails() {
	    ArrayList<String> details = new ArrayList<>();
	    try {
	        String location = driver.findElement(By.xpath("(//div[contains(@class,'flight-booking-page_flight-details_journey-details')]/div/div)[1]")).getText();
	        String dates = driver.findElement(By.xpath("(//div[contains(@class,'flight-booking-page_flight-details_journey-details')]/div/div)[2]")).getText();
	        String travellerName = driver.findElement(By.xpath("//div[text()='Traveller Details']/div/div")).getText();

	        details.add(location);
	        details.add(dates);
	        details.add(travellerName);
	    } catch (Exception e) {
	        e.printStackTrace(); // optional debug print
	        details.clear(); // clear list in case partial data was added
	        details.add("Error: " + e.getMessage());
	    }
	    return details;
	}
	/*
	public void validateConfirmationPage(ArrayList<String> details, double expectedPrice, ExtentTest test) {
	    try {
	        // Extract details from the list
	        String location = details.get(0);
	        String date = details.get(1);
	        String travellerName = details.get(2);

	        // Locate the confirmation popup
	        WebElement popUp = driver.findElement(By.xpath("//div[@class='fade app-modal booking-confirm-modal modal show']//div[@class='modal-body']"));
	        WebElement reformationPopup = popUp.findElement(By.xpath("//div[text()='Check Details and Reconfirm']"));

	        if (reformationPopup.isDisplayed()) {
	            test.log(Status.INFO, "Confirmation popup is displayed.");

	            // Extract values from the popup
	            String actualLocation = popUp.findElement(By.xpath("(//div[@class='p-2 border-bottom mb-2']/div)[1]")).getText();
	            String actualDate = popUp.findElement(By.xpath("(//div[@class='p-2 border-bottom mb-2']/div)[2]")).getText();
	            String actualTravellerName = popUp.findElement(By.xpath("//td[@rowspan]")).getText();
	            String actualPrice = popUp.findElement(By.xpath("//span[text()='Total Fare']/following-sibling::span")).getText();

	            // Compare values
	            if (
	                actualLocation.equalsIgnoreCase(location) &&
	                actualDate.equalsIgnoreCase(date) &&
	                actualTravellerName.equalsIgnoreCase(travellerName)
	            ) {
	                popUp.findElement(By.xpath("//button[text()='Confirm']")).click();
	                test.log(Status.PASS, "All confirmation details matched and booking confirmed.");
	            } else {
	                test.log(Status.FAIL, "Mismatch in confirmation details." +
	                    "<br>Expected: " + location + ", " + date + ", " + travellerName +
	                    "<br>Actual: " + actualLocation + ", " + actualDate + ", " + actualTravellerName);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Mismatch", "Confirmation details mismatch");
	                Assert.fail();
	            }

	        } else {
	            test.log(Status.FAIL, "Confirmation popup is not displayed.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Popup not shown", "Reconfirmation popup not displayed.");
	            Assert.fail();
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception during confirmation page validation: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception", "Exception in validateConfirmationPage");
	        Assert.fail();
	    }
	}
*/
	/*
	public void validateConfirmationPage(ArrayList<String> details, double expectedPrice, ExtentTest test) {
	    try {
	        // Extract details from the list
	        String location = details.get(0);
	        String date = details.get(1);
	        String travellerName = details.get(2);

	        // Locate the confirmation popup
	        WebElement popUp = driver.findElement(By.xpath("//div[@class='fade app-modal booking-confirm-modal modal show']//div[@class='modal-body']"));
	        WebElement reformationPopup = popUp.findElement(By.xpath("//div[text()='Check Details and Reconfirm']"));

	        if (reformationPopup.isDisplayed()) {
	            test.log(Status.INFO, "Confirmation popup is displayed.");

	            // Extract values from the popup
	            String actualLocation = popUp.findElement(By.xpath("(//div[@class='p-2 border-bottom mb-2']/div)[1]")).getText();
	            String actualDate = popUp.findElement(By.xpath("(//div[@class='p-2 border-bottom mb-2']/div)[2]")).getText();
	            String actualTravellerName = popUp.findElement(By.xpath("//td[@rowspan]")).getText();
	            String actualPriceText = popUp.findElement(By.xpath("//span[text()='Total Fare']/following-sibling::span")).getText();

	            // Parse the price from "AED 304.50" to 304.50
	            double actualPrice = 0.0;
	            try {
	                actualPrice = Double.parseDouble(actualPriceText.replaceAll("[^\\d.]", ""));
	            } catch (NumberFormatException nfe) {
	                test.log(Status.FAIL, "Failed to parse price: " + actualPriceText);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Price Parsing Error", "Invalid price format.");
	                Assert.fail();
	            }

	            // Compare values with small tolerance for floating-point comparison
	            boolean isPriceMatch = Math.abs(actualPrice - expectedPrice) < 0.01;

	            if (
	                actualLocation.equalsIgnoreCase(location) &&
	                actualDate.equalsIgnoreCase(date) &&
	                actualTravellerName.equalsIgnoreCase(travellerName) &&
	                isPriceMatch
	            ) {
	                popUp.findElement(By.xpath("//button[text()='Confirm']")).click();
	                test.log(Status.PASS, "All confirmation details matched and booking confirmed.");
	            } else {
	                test.log(Status.FAIL, "Mismatch in confirmation details." +
	                    "<br>Expected: " + location + ", " + date + ", " + travellerName + ", Price: " + expectedPrice +
	                    "<br>Actual: " + actualLocation + ", " + actualDate + ", " + actualTravellerName + ", Price: " + actualPriceText);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Mismatch", "Confirmation details mismatch");
	                Assert.fail();
	            }

	        } else {
	            test.log(Status.FAIL, "Confirmation popup is not displayed.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Popup not shown", "Reconfirmation popup not displayed.");
	            Assert.fail();
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception during confirmation page validation: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception", "Exception in validateConfirmationPage");
	        Assert.fail();
	    }
	}
*/
	public void validateConfirmationPage(ArrayList<String> details, double expectedPrice, ExtentTest test) {
	    try {
	        // Extract and clean expected details
	        String location = details.get(0);
	        String date = details.get(1);
	        String travellerName = details.get(2).replaceFirst("^\\d+\\)\\s*", "").trim(); // Remove numbering like "1) "

	        // Locate the confirmation popup
	        WebElement popUp = driver.findElement(By.xpath("//div[@class='fade app-modal booking-confirm-modal modal show']//div[@class='modal-body']"));
	        WebElement reformationPopup = popUp.findElement(By.xpath("//div[text()='Check Details and Reconfirm']"));

	        if (reformationPopup.isDisplayed()) {
	            test.log(Status.INFO, "Confirmation popup is displayed.");

	            // Extract actual values from the popup
	            String actualLocation = popUp.findElement(By.xpath("(//div[@class='p-2 border-bottom mb-2']/div)[1]")).getText().trim();
	            String actualDate = popUp.findElement(By.xpath("(//div[@class='p-2 border-bottom mb-2']/div)[2]")).getText().trim();
	            String actualTravellerName = popUp.findElement(By.xpath("//td[@rowspan]")).getText().trim();
	            String actualPriceText = popUp.findElement(By.xpath("//span[text()='Total Fare']/following-sibling::span")).getText().trim();

	            // Clean and parse price (e.g., from "AED 346.83" to 346.83)
	            double actualPrice = 0.0;
	            try {
	                actualPrice = Double.parseDouble(actualPriceText.replaceAll("[^\\d.]", ""));
	            } catch (NumberFormatException nfe) {
	                test.log(Status.FAIL, "Failed to parse actual price: " + actualPriceText);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Price Parsing Error", "Invalid price format.");
	                Assert.fail("Price parsing failed.");
	            }

	            // Compare with small tolerance for floating point
	            boolean isPriceMatch = Math.abs(actualPrice - expectedPrice) < 0.01;

	            if (
	                actualLocation.equalsIgnoreCase(location.trim()) &&
	                actualDate.equalsIgnoreCase(date.trim()) &&
	                actualTravellerName.equalsIgnoreCase(travellerName) &&
	                isPriceMatch
	            ) {
	            	
	            	/*
	                popUp.findElement(By.xpath("//button[text()='Confirm']")).click();
	                test.log(Status.PASS, "All confirmation details matched and booking confirmed.");
	                */
	            	// Store the current window handle
	            	String originalWindow = driver.getWindowHandle();

	            	// Click Confirm
	            	popUp.findElement(By.xpath("//button[text()='Confirm']")).click();
	            	test.log(Status.PASS, "All confirmation details matched and booking confirmed.");

	            	// Wait and switch to the new tab
	            	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	            	wait.until(driver -> driver.getWindowHandles().size() > 1);

	            	// Switch to the new window
	            	for (String windowHandle : driver.getWindowHandles()) {
	            	    if (!windowHandle.equals(originalWindow)) {
	            	        driver.switchTo().window(windowHandle);
	            	        test.log(Status.INFO, "Switched to new tab after confirmation.");
	            	        break;
	            	    }
	            	}

	            } else {
	                test.log(Status.FAIL, "Mismatch in confirmation details." +
	                    "<br><b>Expected:</b> " + location + ", " + date + ", " + travellerName + ", Price: " + expectedPrice +
	                    "<br><b>Actual:</b> " + actualLocation + ", " + actualDate + ", " + actualTravellerName + ", Price: " + actualPriceText);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Mismatch", "Confirmation details mismatch");
	                Assert.fail("Confirmation details did not match.");
	            }

	        } else {
	            test.log(Status.FAIL, "Confirmation popup is not displayed.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Popup not shown", "Reconfirmation popup not displayed.");
	            Assert.fail("Confirmation popup not shown.");
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception during confirmation page validation: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception", "Exception in validateConfirmationPage");
	        Assert.fail("Exception during confirmation validation.");
	    }
	}

	/*
	public List<String> clickOnAddMeals(ExtentTest test, int count) {
	    try {
	       
	        WebElement addMealsButton = driver.findElement(By.xpath("//button/div[text()='Add Meals']"));

	        
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addMealsButton);

	       
	        Thread.sleep(500); 

	        
	        addMealsButton.click();
	        System.out.println("Clicked on 'Add Meals' button successfully.");
	        Thread.sleep(3000);
	        List<String>meal= verifyMealsPageIsDisplayed( test,  count);
	        return meal;

	       
	    } catch (Exception e) {
	        System.out.println("Exception occurred while clicking on 'Add Meals' button: " + e.getMessage());

	       
	        Assert.fail("Failed to click on 'Add Meals' button: " + e.getMessage());
	    }
		return null;
	}

	public List<String> verifyMealsPageIsDisplayed(ExtentTest test, int count) throws InterruptedException {
	    List<String> selectedMeals = new ArrayList<>();
	    
	    try {
	        WebElement mealSelectGrid = driver.findElement(By.xpath("(//div[contains(@class,'travellers-info')]/parent::div//div)[4]"));
	        
	        if (mealSelectGrid.isDisplayed()) {
	            // Find all available meal options (update XPath if needed)
	            List<WebElement> availableMeals = driver.findElements(By.xpath("//p[contains(@class,'light-booking-ssr-meal-amount')]")); 
	            
	            if (availableMeals.isEmpty()) {
	                test.log(Status.FAIL, "No available meal found to select.");
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                    "No available meal in Booking Page", "NoAvailableMeal");
	                return selectedMeals;  // return empty list
	            }
	            
	            int mealsToSelect = Math.min(count, availableMeals.size());

	            for (int i = 0; i < mealsToSelect; i++) {
	                WebElement meal = availableMeals.get(i);
	                try {
	                    String mealName = "";
	                    try {
	                        mealName = meal.getText();
	                    } catch (NoSuchElementException e) {
	                        mealName = meal.getText();
	                    }

	                    selectedMeals.add(mealName);
	                    Thread.sleep(1000);  // smaller sleep, can be replaced with wait
	                    meal.click();
	                    test.log(Status.INFO, "Selected meal: " + mealName);
	                } catch (Exception e) {
	                    test.log(Status.FAIL, "Failed to select meal at index " + i + " due to: " + e.getMessage());
	                    ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                        "Meal selection failed", "MealSelectionFailed_Index_" + i);
	                }
	            }
	            Thread.sleep(2000);

	            if (mealsToSelect < count) {
	                test.log(Status.WARNING, "Requested " + count + " meals but only " + mealsToSelect + " were available.");
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.WARNING,
	                    "Fewer meals available than requested", "FewerMealsAvailable");
	            }

	        } else {
	            test.log(Status.FAIL, "Meal selection grid is not displayed.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "Meal grid not displayed", "MealGridNotDisplayed");
	        }
	    } catch (NoSuchElementException e) {
	        test.log(Status.FAIL, "Meal selection grid element not found.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "Meal grid not found", "MealGridNotFound");
	    }
	    
	    return selectedMeals;
	}
*/
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

	        return verifyMealsPageIsDisplayed(test, count);

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
	/*
	public List<String> getMealTravelInfoList() {
	    List<String> travellerDetails = new ArrayList<>();

	    try {
	        Thread.sleep(3000);

	        List<WebElement> mealInfos = driver.findElements(By.xpath("//ul[@class='selected-meals']//span"));

	        Pattern pricePattern = Pattern.compile("AED\\s*([\\d,.]+)");  // Regex to capture the price after "AED"

	        for (WebElement mealInfo : mealInfos) {
	            String text = mealInfo.getText().trim();
	            Matcher matcher = pricePattern.matcher(text);
	            if (matcher.find()) {
	                // Add just the numeric value (e.g., "3.00")
	                travellerDetails.add(matcher.group(1));
	            } else {
	                // If no price found, add empty string or the whole text (based on requirement)
	                travellerDetails.add("");
	            }
	        }

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

	    return travellerDetails;
	}
*/
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
	
	public List<String> clickOnAddBaggage(ExtentTest test, int count) {
	    try {
	        WebElement addBaggageButton = driver.findElement(By.xpath("//button/div[text()=' Add Baggage']"));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addBaggageButton);

	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        wait.until(ExpectedConditions.elementToBeClickable(addBaggageButton));

	        addBaggageButton.click();
	        System.out.println("Clicked on 'Add Baggage' button successfully.");

	        // Wait for meal selection page to load - example: wait for the meal grid element
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[contains(@class,'travellers-info')]/parent::div//div)[4]")));

	        return verifyBaggagePageIsDisplayed(test, count);

	    } catch (Exception e) {
	        System.out.println("Exception occurred while clicking on 'Add Baggage' button: " + e.getMessage());
	        Assert.fail("Failed to click on 'Add Baggage' button: " + e.getMessage());
	    }
	    return Collections.emptyList();  // safer than null
	}
	
	public List<String> verifyBaggagePageIsDisplayed(ExtentTest test, int count) throws InterruptedException {
	    List<String> selectedBaggage = new ArrayList<>();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
 
	    try {
	        WebElement baggageSelectGrid = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("(//div[contains(@class,'travellers-info')]/parent::div//div)[4]")));
 
	        List<WebElement> availableBaggage = driver.findElements(By.xpath("//p[contains(@class,'flight-booking-ssr-baggage-amount')]"));
 
	        if (availableBaggage.isEmpty()) {
	            test.log(Status.FAIL, "No available baggage found to select.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "No available baggage in Booking Page", "NoAvailablebaggage");
	            return selectedBaggage;
	        }
 
	        int baggageToSelect = Math.min(count, availableBaggage.size());
 
	        for (int i = 0; i < baggageToSelect; i++) {
	            WebElement baggage = availableBaggage.get(i);
	            try {
	                String mealName = baggage.getText();
	                wait.until(ExpectedConditions.elementToBeClickable(baggage));
	                baggage.findElement(By.xpath("./following-sibling::button[text()='Add']")).click();
	                selectedBaggage.add(mealName);
	                test.log(Status.INFO, "Selected baggage: " + mealName);
	            } catch (Exception e) {
	                test.log(Status.FAIL, "Failed to select baggage at index " + i + " due to: " + e.getMessage());
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                    "baggage selection failed", "baggageSelectionFailed_Index_" + i);
	            }
	        }
 
	        if (baggageToSelect < count) {
	            test.log(Status.WARNING, "Requested " + count + " baggage but only " + baggageToSelect + " were available.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.WARNING,
	                "Fewer baggage available than requested", "FewerbaggageAvailable");
	        }
 
	    } catch (TimeoutException | NoSuchElementException e) {
	        test.log(Status.FAIL, "baggage selection grid element not found or not visible.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "baggage grid not found or not visible", "baggageGridNotFoundOrNotVisible");
	    }
 
	    return selectedBaggage;
	}
	public String validateBaggagePrice(double seatPrice, ExtentTest test) {
	    try {
	     
	        String totalAmountText = driver.findElement(By.xpath("//span[text()='Baggage Total']/following-sibling::span")).getText();
 
	        
	        totalAmountText = totalAmountText.replace("AED", "").trim(); // Result: "45.00"
	        double actualAmount = Double.parseDouble(totalAmountText); // Converts "45.00" to 45.00
 
	       
	        if (actualAmount == seatPrice) {
	            System.out.println(" Baggage is valid: " + actualAmount);
	            test.log(Status.INFO, " Baggage is valid: " + actualAmount);
	            return totalAmountText;
	        } else {
	            System.out.println(" Baggageprice mismatch! Expected: " + seatPrice + ", but got: " + actualAmount);
	            test.log(Status.INFO, "Baggage price mismatch! Expected: " + seatPrice + ", but got: " + actualAmount);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "missMatchInBaggagetotalprice", "missMatchInBaggagetotalprice");
	            Assert.fail("Baggage price does not match.");
	        }
	    } catch (Exception e) {
	        
	        System.out.println("Exception occurred during Baggage price validation: " + e.getMessage());
	        test.log(Status.FAIL, "Exception during Baggage price validation: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "exceptionInBaggagePriceValidation", "Exception in Baggage price validation");
	        Assert.fail("Test failed due to exception: " + e.getMessage());
	    }
		return null;
	}
	public double getTotalBaggagePrice() {
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
	
	public List<String> clickOnSpecialSSR(ExtentTest test, int count) {
	    try {
	        WebElement addSSRButton = driver.findElement(By.xpath("//button/div[text()=' Other SSR']"));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",addSSRButton );

	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        wait.until(ExpectedConditions.elementToBeClickable(addSSRButton));

	        addSSRButton.click();
	        System.out.println("Clicked on 'Add Baggage' button successfully.");

	        // Wait for meal selection page to load - example: wait for the meal grid element
	      //  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[contains(@class,'travellers-info')]/parent::div//div)[4]")));

	        return verifySpecialSSRPageIsDisplayed(test, count);

	    } catch (Exception e) {
	        System.out.println("Exception occurred while clicking on 'Add Baggage' button: " + e.getMessage());
	        Assert.fail("Failed to click on 'Add Baggage' button: " + e.getMessage());
	    }
	    return Collections.emptyList();  // safer than null
	}
	
	/*
	public List<String> verifySpecialSSRPageIsDisplayed(ExtentTest test, int count) throws InterruptedException {
	    List<String> selectedBaggage = new ArrayList<>();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
 
	    try {
	     //   WebElement baggageSelectGrid = wait.until(ExpectedConditions.visibilityOfElementLocated(
	          //  By.xpath("(//div[contains(@class,'travellers-info')]/parent::div//div)[4]")));
	    	boolean value=validateSpecialSSPageDisPlayed(test);
	    	 if (value) {
		            test.log(Status.FAIL, "No available baggage found to select.");
		            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
		                "No available baggage in Booking Page", "NoAvailablebaggage");
		            return selectedBaggage;
		        }
	    	
 
	        List<WebElement> availableBaggage = driver.findElements(By.xpath("//p[contains(@class,'flight-booking-ssr-baggage-amount')]"));
 
	        if (availableBaggage.isEmpty()) {
	            test.log(Status.FAIL, "No available baggage found to select.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "No available baggage in Booking Page", "NoAvailablebaggage");
	            return selectedBaggage;
	        }
 
	        int baggageToSelect = Math.min(count, availableBaggage.size());
 
	        for (int i = 0; i < baggageToSelect; i++) {
	            WebElement baggage = availableBaggage.get(i);
	            try {
	                String mealName = baggage.getText();
	                wait.until(ExpectedConditions.elementToBeClickable(baggage));
	                baggage.findElement(By.xpath("./following-sibling::button[text()='Add']")).click();
	                selectedBaggage.add(mealName);
	                test.log(Status.INFO, "Selected baggage: " + mealName);
	            } catch (Exception e) {
	                test.log(Status.FAIL, "Failed to select baggage at index " + i + " due to: " + e.getMessage());
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                    "baggage selection failed", "baggageSelectionFailed_Index_" + i);
	            }
	        }
 
	        if (baggageToSelect < count) {
	            test.log(Status.WARNING, "Requested " + count + " baggage but only " + baggageToSelect + " were available.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.WARNING,
	                "Fewer baggage available than requested", "FewerbaggageAvailable");
	        }
 
	    } catch (TimeoutException | NoSuchElementException e) {
	        test.log(Status.FAIL, "baggage selection grid element not found or not visible.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "baggage grid not found or not visible", "baggageGridNotFoundOrNotVisible");
	    }
 
	    return selectedBaggage;
	}
	public boolean validateSpecialSSPageDisPlayed(ExtentTest test) {
	    boolean ssrFound = false;

	    // Step 1: Check if Special SSR (e.g., meal info) is available in the sidebar
	    try {
	        WebElement sidebarTextElement = driver.findElement(
	            By.xpath("//div[@class='overflow-auto flight-details-sidebar modal-body']//div"));
	        
	        if (sidebarTextElement.isDisplayed()) {
	            String ssrText = sidebarTextElement.getText();
	            test.log(Status.PASS, "✅ Special SSR is available for this flight (Sidebar): " + ssrText);
	            ssrFound = true;
	        }
	    } catch (NoSuchElementException e) {
	        test.log(Status.INFO, "ℹ️ Sidebar SSR element not found.");
	    }

	    // Step 2: If sidebar SSR wasn't found, check for baggage grid
	    if (!ssrFound) {
	        try {
	            WebElement baggageSelectGrid = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("(//div[contains(@class,'travellers-info')]/parent::div//div)[4]")));

	            if (baggageSelectGrid.isDisplayed()) {
	                String baggageText = baggageSelectGrid.getText();
	                test.log(Status.PASS, "✅ Special SSR is available for this flight (Baggage): " + baggageText);
	                ssrFound = true;
	                return ssrFound;
	            }
	        } catch (TimeoutException e) {
	            test.log(Status.INFO, "ℹ️ Baggage section did not become visible in time.");
	        }
	    }

	    // Step 3: If neither SSR nor Baggage info is found
	    if (!ssrFound) {
	        test.log(Status.FAIL, "❌ Special SSR is not available for this flight.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "SSR section not found or not visible", "ssrNotAvailable");
	    }
	    return false;
	}
*/
	public List<String> verifySpecialSSRPageIsDisplayed(ExtentTest test, int count) throws InterruptedException {
	    List<String> selectedBaggage = new ArrayList<>();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    try {
	        // Check if any SSR (sidebar or baggage grid) is available
	        boolean ssrAvailable = validateSpecialSSPageDisPlayed(test, wait);

	        if (ssrAvailable) {
	            test.log(Status.FAIL, "❌ No SSR available to proceed with baggage selection.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "No available SSR in Booking Page", "NoAvailableSSR");
	            Thread.sleep(2000);
	            driver.findElement(By.xpath("//button[text()='Done']")).click();
	            return selectedBaggage;
	        }

	        // Locate all available baggage options
	        List<WebElement> availableBaggage = driver.findElements(By.xpath("//p[contains(@class,'flight-booking-ssr-baggage-amount')]"));

	        if (availableBaggage.isEmpty()) {
	            test.log(Status.FAIL, "❌ No available baggage found to select.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "No available baggage in Booking Page", "NoAvailableBaggage");
	            return selectedBaggage;
	        }

	        int baggageToSelect = Math.min(count, availableBaggage.size());

	        for (int i = 0; i < baggageToSelect; i++) {
	            WebElement baggage = availableBaggage.get(i);
	            try {
	                String baggageName = baggage.getText();
	                wait.until(ExpectedConditions.elementToBeClickable(baggage));
	                baggage.findElement(By.xpath("./following-sibling::button[text()='Add']")).click();
	                selectedBaggage.add(baggageName);
	                test.log(Status.INFO, "✅ Selected baggage: " + baggageName);
	            } catch (Exception e) {
	                test.log(Status.FAIL, "❌ Failed to select baggage at index " + i + " due to: " + e.getMessage());
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                    "Baggage selection failed", "BaggageSelectionFailed_Index_" + i);
	            }
	        }

	        if (baggageToSelect < count) {
	            test.log(Status.WARNING, "⚠️ Requested " + count + " baggage but only " + baggageToSelect + " were available.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.WARNING,
	                "Fewer baggage available than requested", "FewerBaggageAvailable");
	        }

	    } catch (TimeoutException | NoSuchElementException e) {
	        test.log(Status.FAIL, "❌ Baggage selection grid element not found or not visible.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "Baggage grid not found or not visible", "BaggageGridNotFoundOrNotVisible");
	    }

	    return selectedBaggage;
	}

	public boolean validateSpecialSSPageDisPlayed(ExtentTest test, WebDriverWait wait) {
	    boolean ssrFound = false;

	    try {
	        WebElement sidebarTextElement = driver.findElement(
	            By.xpath("//div[@class='overflow-auto flight-details-sidebar modal-body']//div"));

	        if (sidebarTextElement.isDisplayed()) {
	            String ssrText = sidebarTextElement.getText();
	            test.log(Status.PASS, "✅ Special SSR is available (Sidebar): " + ssrText);
	            ssrFound = true;
	        }
	    } catch (NoSuchElementException e) {
	        test.log(Status.INFO, "ℹ️ Sidebar SSR element not found.");
	    }

	    if (!ssrFound) {
	        try {
	            WebElement baggageSelectGrid = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("(//div[contains(@class,'travellers-info')]/parent::div//div)[4]")));

	            if (baggageSelectGrid.isDisplayed()) {
	                String baggageText = baggageSelectGrid.getText();
	                test.log(Status.PASS, "✅ Special SSR is available (Baggage): " + baggageText);
	                ssrFound = true;
	            }
	        } catch (TimeoutException e) {
	            test.log(Status.INFO, "ℹ️ Baggage section did not become visible in time.");
	        }
	    }

	    if (!ssrFound) {
	        test.log(Status.FAIL, "❌ Special SSR is not available for this flight.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "SSR section not found or not visible", "SSRNotAvailable");
	    }

	    return ssrFound;
	}

	public String getAllFlightDetailsInBookingPage() {
	    String allText = "";

	    try {
	        WebElement flightSection = driver.findElement(By.xpath("//section[@class='selected-flight-details']"));
	        allText = flightSection.getText();
	        System.out.println("🛫 All Flight Details:\n" + allText);
	    } catch (NoSuchElementException e) {
	        System.out.println("❌ Flight details section not found.");
	    } catch (Exception e) {
	        System.out.println("❌ Unexpected error while fetching flight details: " + e.getMessage());
	    }

	    return allText;
	}

//	public void validateFlightDetailsContentMatch(String selectedFlightDetails, String bookingPageDetails, ExtentTest test) {
//	    String[] selectedLines = selectedFlightDetails.trim().split("\\r?\\n");
//	    String[] bookingLines = bookingPageDetails.trim().split("\\r?\\n");
//
//	    boolean hasMismatch = false;
//
//	    StringBuilder logBuilder = new StringBuilder();
//	    logBuilder.append("<table border='1' style='border-collapse: collapse;'>")
//	              .append("<tr><th>Line</th><th>Selected Page</th><th>Booking Page</th><th>Status</th></tr>");
//
//	    int maxLength = Math.max(selectedLines.length, bookingLines.length);
//
//	    for (int i = 0; i < maxLength; i++) {
//	        String selectedLine = (i < selectedLines.length) ? selectedLines[i].trim() : "⛔ Missing in Selected Page";
//	        String bookingLine = (i < bookingLines.length) ? bookingLines[i].trim() : "⛔ Missing in Booking Page";
//
//	        boolean match = selectedLine.equalsIgnoreCase(bookingLine);
//
//	        if (!match) hasMismatch = true;
//
//	        logBuilder.append("<tr>")
//	                  .append("<td>").append(i + 1).append("</td>")
//	                  .append("<td>").append(selectedLine).append("</td>")
//	                  .append("<td>").append(bookingLine).append("</td>")
//	                  .append("<td style='color:").append(match ? "green" : "red").append(";'>")
//	                  .append(match ? "PASS" : "FAIL")
//	                  .append("</td></tr>");
//	    }
//
//	    logBuilder.append("</table>");
//
//	    if (hasMismatch) {
//	        test.log(Status.FAIL, "❌ Flight details mismatch detected:<br>" + logBuilder.toString());
//
//	        // Capture screenshot if needed
//	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Detail Mismatch", "Mismatch in flight card and detail modal");
//
//	        Assert.fail("Flight details content mismatch. Check ExtentReport for full comparison.");
//	    } else {
//	        test.log(Status.PASS, "✅ All flight details match:<br>" + logBuilder.toString());
//	    }
//	}
	public void validateFlightDetailsContentMatch(String selectedFlightDetails, String bookingPageDetails, ExtentTest test) {
	    String[] selectedLines = selectedFlightDetails.trim().split("\\r?\\n");
	    String[] bookingLines = bookingPageDetails.trim().split("\\r?\\n");
 
	    boolean hasMismatch = false;
 
	    StringBuilder logBuilder = new StringBuilder();
	    logBuilder.append("<table border='1' style='border-collapse: collapse;'>")
	              .append("<tr><th>Line</th><th>Flight Details Page</th><th>Booking Page Details </th><th>Status</th></tr>");
 
	    int maxLength = Math.max(selectedLines.length, bookingLines.length);
 
	    for (int i = 0; i < maxLength; i++) {
	        String selectedLine = (i < selectedLines.length) ? selectedLines[i].trim() : "⛔ Missing in Selected Page";
	        String bookingLine = (i < bookingLines.length) ? bookingLines[i].trim() : "⛔ Missing in Booking Page";
 
	        boolean match = selectedLine.equalsIgnoreCase(bookingLine);
 
	        if (!match) hasMismatch = true;
 
	        logBuilder.append("<tr>")
	                  .append("<td>").append(i + 1).append("</td>")
	                  .append("<td>").append(selectedLine).append("</td>")
	                  .append("<td>").append(bookingLine).append("</td>")
	                  .append("<td style='color:").append(match ? "green" : "red").append(";'>")
	                  .append(match ? "PASS" : "FAIL")
	                  .append("</td></tr>");
	    }
 
	    logBuilder.append("</table>");
 
	    if (hasMismatch) {
	        test.log(Status.FAIL, "❌ Flight details mismatch detected:<br>" + logBuilder.toString());
 
	        // Capture screenshot if needed
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Detail Mismatch", "Mismatch in flight card and detail modal");
 
	        Assert.fail("Flight details content mismatch. Check ExtentReport for full comparison.");
	    } else {
	        test.log(Status.PASS, "✅ All flight details match:<br>" + logBuilder.toString());
	    }
	}
	public void validateFinalPrice(String expectedPrice, ExtentTest test) {
	    try {
	        // Locate and get the raw price text from the UI
	        String rawPrice = driver.findElement(By.xpath("//span[text()='Total Fare']/following-sibling::span/span")).getText().trim();

	        // Clean up the price text (remove non-breaking spaces, multiple spaces)
	        rawPrice = rawPrice.replaceAll("\u00A0", " ").replaceAll("\\s+", " ").trim();

	        // Extract only the first valid 'AED' price if multiple are present
	        String actualPrice = rawPrice.replaceAll("(AED\\s[\\d,]+\\.\\d{2}).*", "$1");

	        // Log prices for debugging
	        System.out.println("Actual: '" + actualPrice + "', Expected: '" + expectedPrice + "'");

	        // Normalize both prices for comparison if needed (optional)
	        String normalizedActual = normalizePrice(actualPrice);
	        String normalizedExpected = normalizePrice(expectedPrice);

	        // Perform comparison
	        if (!normalizedActual.equals(normalizedExpected)) {
	            // Log failure and attach screenshot
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "Flight Detail Mismatch",
	                String.format("Expected: %s, but found: %s", expectedPrice, actualPrice));

	            // Fail the test
	            Assert.fail("Flight details content mismatch. Check ExtentReport for full comparison.");
	        } else {
	            // Log success
	            test.log(Status.PASS, "Total price validated successfully. Value: " + actualPrice);
	        }
	    } catch (NoSuchElementException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "Element Not Found",
	            "Could not locate the total price element on the page.");
	        Assert.fail("Failed due to missing element: " + e.getMessage());
	    } catch (Exception e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	            "Unexpected Error",
	            "An unexpected error occurred in price validation.");
	        Assert.fail("Unexpected error during validation: " + e.getMessage());
	    }
	}

	// Helper method to normalize prices by removing all non-digit/decimal characters
	private String normalizePrice(String price) {
	    return price.replaceAll("[^\\d.]", ""); // keep only digits and dot
	}

}
