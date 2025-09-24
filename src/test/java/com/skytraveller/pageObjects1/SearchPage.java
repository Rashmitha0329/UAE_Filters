package com.skytraveller.pageObjects1;

import static org.testng.Assert.fail;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class SearchPage extends BasePage{

	// Constructor of loginPage calls the BasePage constructor with driver
	public SearchPage(WebDriver driver) {
		super(driver);// calls BasePage constructor
	}

	@FindBy(xpath="(//div[contains(@class,'react-select__input-container')]/input)[1]")
	WebElement fromLocation;

	@FindBy(xpath="(//div[contains(@class,'react-select__input-container')]/input)[2]")
	WebElement toLocation;

	@FindBy(xpath = "(//div[@class='react-datepicker__input-container'])[1]")
	WebElement datePickerInput;

	@FindBy(xpath = "(//div[@class='react-datepicker__input-container'])[2]")
	WebElement datePickerInputReturn;

	@FindBy(xpath = "(//div[@class='react-datepicker__current-month'])[1]")
	WebElement date;
	@FindBy(xpath = "(//div[@class='react-datepicker__current-month'])[1]")
	WebElement dateReturn;


	@FindBy(xpath = "//button[@aria-label='Next Month']")
	WebElement nextMonth;


	@FindBy(xpath = "(//div[@class='react-datepicker__header ']/child::div)[1]")
	WebElement MonthYear;

	@FindBy(xpath="//span[@class='travellers-class_text']")
	WebElement clickOnClassPassangerDropdown;

	@FindBy(xpath="//button[text()='Done']")
	WebElement doneButton;

	//Method to enter From Location
	public void enterFromLocation(String from) {
		try {
			fromLocation.clear();
			fromLocation.sendKeys(from);
			location(from);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		String Date;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// Method A: Using zoom
		js.executeScript("document.body.style.zoom='80%'");

		datePickerInput.click();
		Date = date.getText();
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
				if(Date.contentEquals(MonthandYear))
				{
					driver.findElement(By.xpath("(//div[@class='react-datepicker__month-container'])[1]//div[text()='"+day+"' and @aria-disabled='false']")).click();
					break;
					//MonthYear
				}

			}
		}
	}
	public void selectDateReturn(String day, String MonthandYear) throws InterruptedException
	{
		String Date;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// Method A: Using zoom
		js.executeScript("document.body.style.zoom='80%'");

		datePickerInputReturn.click();
		Date = dateReturn.getText();
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
				if(Date.contentEquals(MonthandYear))
				{
					driver.findElement(By.xpath("(//div[@class='react-datepicker__month-container'])[1]//div[text()='"+day+"' and @aria-disabled='false']")).click();
					break;
					//MonthYear
				}

			}
		}
	}
	//			public void addAdult(String adult) {
	//			    try {
	//			        driver.findElement(By.xpath("//*[contains(@class,'adult')][text()='" + adult + "']")).click();
	//			    } catch (Exception e) {
	//			        System.out.println("Error in addAdult(): " + e.getMessage());
	//			    }
	//			}
	//
	//			public void addChild(String child) {
	//			    try {
	//			        driver.findElement(By.xpath("//*[contains(@class,'child')][text()='" + child + "']")).click();
	//			    } catch (Exception e) {
	//			        System.out.println("Error in addChild(): " + e.getMessage());
	//			    }
	//			}
	//
	//			public void infantCount(String infant) {
	//			    try {
	//			        driver.findElement(By.xpath("//*[contains(@class,'infant')][text()='" + infant + "']")).click();
	//			    } catch (Exception e) {
	//			        System.out.println("Error in infantCount(): " + e.getMessage());
	//			    }
	//			}
	public void addAdult(String adult) {
		try {
			driver.findElement(By.xpath("//span[text()='Adults(12y+)']/parent::div//li[text()='" + adult + "']")).click();
		} catch (Exception e) {
			System.out.println("Error in addAdult(): " + e.getMessage());
		}
	}

	public void addChild(String child) {
		try {
			driver.findElement(By.xpath("//span[text()='Children(2y-12y)']/parent::div//li[text()='" + child + "']")).click();
		} catch (Exception e) {
			System.out.println("Error in addChild(): " + e.getMessage());
		}
	}

	public void infantCount(String infant) {
		try {
			driver.findElement(By.xpath("//span[text()='Infants(<2y)']/parent::div//li[text()='" + infant + "']")).click();
		} catch (Exception e) {
			System.out.println("Error in infantCount(): " + e.getMessage());
		}
	}

	//Method to click on search button
	public void clickOnSearch()
	{
		driver.findElement(By.xpath("//button[text()='Search Flights']")).click();
	}

	public void searchFightsOnHomePageRoundTrip(String from, String to, String day, String MonthandYear,String dayReturn, String MonthandYearReturn, String adult, String child, String infant) {
		try {
			//String dayReturn, String MonthandYearReturn,
			Thread.sleep(1000);
			enterFromLocation(from);
			Thread.sleep(1000);
			enterToLocation(to);
			Thread.sleep(1000);
			selectDate(day, MonthandYear);
			selectDateReturn(dayReturn, MonthandYearReturn);

			Thread.sleep(1000);

			clickOnClassPassangerDropdown.click();
			Thread.sleep(1000);
			addAdult(adult);
			Thread.sleep(1000);
			addChild(child);
			Thread.sleep(1000);
			infantCount(infant);
			Thread.sleep(1000);
			doneButton.click();

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Best practice to reset the interruption status
			System.out.println("Interrupted while searching flights on home page: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error in searchFightsOnHomePage(): " + e.getMessage());
		}
	}
	public void searchFightsOnHomePage(String from, String to, String day, String MonthandYear, String adult, String child, String infant) {
		try {
			//String dayReturn, String MonthandYearReturn,
			Thread.sleep(1000);
			enterFromLocation(from);
			Thread.sleep(1000);
			enterToLocation(to);
			Thread.sleep(1000);
			selectDate(day, MonthandYear);
			//selectDateReturn(dayReturn, MonthandYearReturn);

			Thread.sleep(1000);

			clickOnClassPassangerDropdown.click();
			Thread.sleep(1000);
			addAdult(adult);
			Thread.sleep(1000);
			addChild(child);
			Thread.sleep(1000);
			infantCount(infant);
			Thread.sleep(1000);
			doneButton.click();

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Best practice to reset the interruption status
			System.out.println("Interrupted while searching flights on home page: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error in searchFightsOnHomePage(): " + e.getMessage());
		}
	}
	//public void validateResultPage()
	//{
	//	try {
	// WebElement flightCard = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]"));	
	// if(flightCard.isDisplayed())
	// {
	//	 System.out.println("flight card is displayed successfully");
	// }
	// else
	// {
	//	WebElement flightNotFound = driver.findElement(By.xpath("//div[text()='No Flights Found']"));
	//	if(flightNotFound.isDisplayed())
	//	{
	//		System.out.println("No flight found for this search");
	//	}
	//	
	// }
	//	}
	// catch (Exception e) {
	//   System.out.println("An error occurred while validating the result page: " + e.getMessage());
	//}
	//}

	//method to validate whether result page is getting displayed
	public void validateResultPage() {
		try {
			Thread.sleep(2000);
			List<WebElement> flightCards = driver.findElements(By.xpath("(//section[@class=' d-flex my-2 one-way-new-result-card '])[1]"));

			if (!flightCards.isEmpty() && flightCards.get(0).isDisplayed()) {
				System.out.println("Flight card is displayed successfully.");
			} else {
				List<WebElement> noFlightMessages = driver.findElements(By.xpath("//div[text()='No Flights Found']"));
				if (!noFlightMessages.isEmpty() && noFlightMessages.get(0).isDisplayed()) {
					System.out.println("No flight found for this search.");
				} else {
					System.out.println("Neither flight cards nor 'No Flights Found' message is present.");
				}
			}
		} catch (Exception e) {
			System.out.println("An error occurred while validating the result page: " + e.getMessage());
		}
	}
	public void extractFareCardsBySupplier(WebDriver driver, String expectedSupplier) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Step 1: Get all fare cards
		List<WebElement> cards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.xpath("//div[@class='d-flex justify-content-between flex-column h-100']")));

		// Step 2: Create list to hold only matching cards
		List<WebElement> matchingCards = new ArrayList<>();

		// Step 3: Filter cards that contain the expected supplier
		for (WebElement card : cards) {
			List<WebElement> elements = card.findElements(By.xpath(".//*"));

			boolean supplierMatched = false;
			for (WebElement el : elements) {
				String text = el.getText().trim();
				if (!text.isEmpty() && text.equalsIgnoreCase(expectedSupplier)) {
					supplierMatched = true;
					break;
				}
			}

			if (supplierMatched) {
				matchingCards.add(card);
			}
		}

		// Step 4: Extract data from each matching card
		List<Map<String, String>> allCardData = new ArrayList<>();

		for (WebElement card : matchingCards) {
			Map<String, String> cardData = new LinkedHashMap<>();

			// Get all child elements
			List<WebElement> elements = card.findElements(By.xpath(".//*"));
			List<String> textElements = new ArrayList<>();

			for (WebElement el : elements) {
				String text = el.getText().trim();
				if (!text.isEmpty()) {
					textElements.add(text);
				}
			}

			// Add first 4 key-value pairs (first 8 items)
			for (int i = 0; i <= 7 && i + 1 < textElements.size(); i += 2) {
				String key = textElements.get(i);
				String value = textElements.get(i + 1);
				cardData.put(key, value);
			}

			// Add remaining items with hardcoded keys
			if (textElements.size() > 8) {
				cardData.put("RefundableInfo", textElements.get(8));
			}
			if (textElements.size() > 9) {
				cardData.put("NoOfSeatAvailable", textElements.get(9));
			}
			if (textElements.size() > 10) {
				cardData.put("Price", textElements.get(10));
			}

			allCardData.add(cardData);
		}

		// Step 5: Print result & detect duplicates
		int cardIndex = 1;
		Set<Map<String, String>> seenCardData = new HashSet<>();

		for (Map<String, String> cardMap : allCardData) {
			System.out.println("=== Fare Card " + cardIndex++ + " ===");
			for (Map.Entry<String, String> entry : cardMap.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}

			// Check for duplicates
			if (!seenCardData.add(cardMap)) {
				System.out.println("❌ Duplicate fare card detected! Data already exists.");
				throw new RuntimeException("Duplicate fare card found with the same data: " + cardMap);
			}

			System.out.println();
		}

		System.out.println("✅ Total matching cards found: " + matchingCards.size());
	}
	//	public void selectAirline() throws InterruptedException
	//	{
	//		Thread.sleep(30000);
	//		ArrayList<String> airline = new ArrayList<>();
	//		
	//		List<WebElement> listOfAirline = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label[@class=' d-flex  fw-500 align-items-center fs-12 app-check-box cursor-pointer']"));
	//		for(WebElement getlistOfAirline:listOfAirline)
	//		{
	//			String airlineName = getlistOfAirline.getText();
	//			String[] airlineNameSplit1 = airlineName.split("\\(");
	//			String airlineNameText = airlineNameSplit1[0].trim();
	//			airline.add(airlineNameText.toLowerCase());
	//	    	
	//		}
	//		System.out.println(airline);
	//		
	//		if(airline.contains("spicejet"))
	//		{
	//			
	//		System.out.println("user needed airline found");
	//		driver.findElement(By.xpath("//div[text()='Search By Airlines']/parent::div//input[@id='Spicejet']")).click();
	//		
	//	}
	//		else
	//		{
	//			listOfAirline.get(0).click();
	//			
	//		}
	//		for (WebElement label : listOfAirline) {
	//	        WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));
	//	        if (checkbox.isSelected()) {
	//	            System.out.println("Selected airline: " + label.getText().trim());
	//	        }
	//	    }
	//	}
//	public String selectAirline(String airLine) throws InterruptedException
//	{
//		System.out.println(airLine);
//		String airLineToLowerCase = airLine.toLowerCase();
//		System.out.println(airLineToLowerCase);
//		Thread.sleep(30000);
//		ArrayList<String> airline = new ArrayList<>();
//
//		List<WebElement> listOfAirline = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//label[@class=' d-flex  fw-500 align-items-center fs-12 app-check-box cursor-pointer']"));
//		for(WebElement getlistOfAirline:listOfAirline)
//		{
//			String airlineName = getlistOfAirline.getText();
//			String[] airlineNameSplit1 = airlineName.split("\\(");
//			String airlineNameText = airlineNameSplit1[0].trim();
//			airline.add(airlineNameText.toLowerCase());
//
//		}
//		System.out.println(airline);
//
//		if(airline.contains(airLineToLowerCase))
//		{
//
//			System.out.println("user needed airline found");
//			driver.findElement(By.xpath("//div[text()='Search By Airlines']/parent::div//input[@id='"+airLine+"']")).click();
//
//		}
//		else
//		{
//			listOfAirline.get(0).click();
//
//		}
//		for (WebElement label : listOfAirline) {
//			WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));
//			if (checkbox.isSelected()) {
//				System.out.println("Selected airline: " + label.getText().trim());
//				String label1 = label.getText().trim();
//				return label1;
//			}
//		}
//		return null;
//	}
	
	public List<String> selectAirline(List<String> airLines, ExtentTest test) throws InterruptedException {
	    try {
	        test.log(Status.INFO, "🔍 Requested airlines: " + airLines);
	        Thread.sleep(3000);
	 
	        // Show More
	        List<WebElement> showMoreButtons = driver.findElements(By.xpath("//div[text()='Search By Airlines']/parent::div//a[text()='Show More']"));
	        if (!showMoreButtons.isEmpty() && showMoreButtons.get(0).isDisplayed()) {
	            showMoreButtons.get(0).click();
	            Thread.sleep(2000);
	            test.log(Status.INFO, " Clicked on 'Show More' to expand airline list.");
	        } else {
	            test.log(Status.INFO, " Show More' button not found or not visible.");
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
	                    test.log(Status.PASS, " Selected airline: " + label.getText().trim());
	                } else {
	                    test.log(Status.INFO, " Airline already selected: " + label.getText().trim());
	                }
	 
	                selectedAirlines.add(label.getText().trim());
	            } else {
	                test.log(Status.PASS, "❌ Airline not found on screen: " + airline);
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
	            test.log(Status.PASS, "None of the requested airlines were found. Selected first available airline instead: " + selected);
	        }
	 
	        if (selectedAirlines.isEmpty()) {
	            test.log(Status.FAIL, "❌ No airlines could be selected.");
                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "❌ No airlines could be selected.", "error occured while selecting the airline");

	            Assert.fail("No airlines could be selected.");

	        }
	 
	        return selectedAirlines;
	 
	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Exception in selectAirline(): " + e.getMessage());
	        e.printStackTrace();
            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "❌ Exception in selectAirline()", "error occured while selecting the airline");

	        Assert.fail("Exception in selectAirline(): " + e.getMessage());

	        return null;
	    }
	}
	 



//	public String selectStopFilter(String stopp) throws InterruptedException
//	{
//		System.out.println(stopp);
//		Thread.sleep(30000);
//		ArrayList<String> stops = new ArrayList<>();
//
//		List<WebElement> listOfStops = driver.findElements(By.xpath("//div[text()='Fare policy & No. of stop']/parent::div//label"));
//		for(WebElement getlistOfStops:listOfStops)
//		{
//			String stop = getlistOfStops.getText();
//			//			
//			stops.add(stop);
//
//		}
//		System.out.println(stops);
//
//		//		if(stops.contains("1 Stop"))
//		//		{
//		//			
//		//		System.out.println("user needed airline found");
//		//		driver.findElement(By.xpath("//div[text()='Fare policy & No. of stop']/parent::div//label[text()='1 Stop']")).click();
//		//		
//		//	}
//		//		else
//		//		{
//		//			listOfStops.get(0).click();
//		//			
//		//		}
//		boolean containsStop = stops.stream()
//				.anyMatch(s -> s.equalsIgnoreCase(stopp));
//
//		if (containsStop) {
//			System.out.println("user needed stop filter found");
//			driver.findElement(By.xpath("//div[text()='Fare policy & No. of stop']/parent::div//label[normalize-space()='"+ stopp +"']")).click();
//		} else {
//			listOfStops.get(0).click();
//		}
//		for (WebElement label : listOfStops) {
//			WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));
//			if (checkbox.isSelected()) {
//				System.out.println("Selected stop: " + label.getText().trim());
//				String label1 = label.getText().trim();
//				return label1;
//			}
//		}
//		return null;
//	}
	public List<String> selectStopFilter(List<String> stopFilters, ExtentTest test) throws InterruptedException {
	    try {
	        test.log(Status.INFO, " Requested stop filters: " + stopFilters);
	        Thread.sleep(3000);
	        WebElement element = driver.findElement(By.xpath("//div[text()='Fare policy & No. of stop']"));
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("arguments[0].scrollIntoView(true);", element);
	        List<String> selectedStops = new ArrayList<>();
	        List<WebElement> listOfStops = driver.findElements(By.xpath("//div[text()='Fare policy & No. of stop']/parent::div//label"));

	        if (listOfStops.isEmpty()) {
	            test.log(Status.FAIL, "❌ No stop filters found on the page.");
	            Assert.fail("Stop filters not found.");
	        }

	        Map<String, WebElement> stopMap = new HashMap<>();
	        for (WebElement label : listOfStops) {
	            String stopText = label.getText().trim().toLowerCase();
	            stopMap.put(stopText, label);
	        }

	        for (String stop : stopFilters) {
	            String stopLower = stop.trim().toLowerCase();
	            if (stopMap.containsKey(stopLower)) {
	                WebElement label = stopMap.get(stopLower);
	                WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));

	                if (!checkbox.isSelected()) {
	                    label.click();
	                    test.log(Status.PASS, " Selected stop filter: " + label.getText().trim());
	                } else {
	                    test.log(Status.INFO, " Stop filter already selected: " + label.getText().trim());
	                }

	                selectedStops.add(label.getText().trim());
	            } else {
	                test.log(Status.INFO, "⚠️ Stop filter not found on screen: " + stop);

	                // 🔁 Fallback: click on the first available stop filter
	                if (selectedStops.isEmpty() && !listOfStops.isEmpty()) {
	                    WebElement fallbackLabel = listOfStops.get(0);
	                    WebElement fallbackCheckbox = fallbackLabel.findElement(By.xpath(".//input[@type='checkbox']"));

	                    if (!fallbackCheckbox.isSelected()) {
	                        fallbackLabel.click();
	                    }

	                    String fallbackText = fallbackLabel.getText().trim();
	                    selectedStops.add(fallbackText);
	                    test.log(Status.INFO, "⚠ No requested stop matched. Selected default: " + fallbackText);
	                }
	            }
	        }

	        if (selectedStops.isEmpty()) {
	            test.log(Status.FAIL, "❌ None of the requested stop filters were found or selected.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "❌ None of the requested stop filters were found or selected.", "error occured while selecting stops filter");

	            Assert.fail("None of the requested stop filters were selected.");
	        }

	        return selectedStops;

	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Exception in selectStopFilter(): " + e.getMessage());
	        e.printStackTrace();
            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "❌ None of the requested stop filters were found or selected.", "error occured while selecting stops filter");

	        Assert.fail("Exception in selectStopFilter(): " + e.getMessage());
	        return null;
	    }
	}

//	public void validateAirlineFilter(String airlineReturned) throws InterruptedException
//	{
//		System.out.println(airlineReturned);
//		String[] airlineReturnedSplit = airlineReturned.split("\\(");
//		String airlineReturned1 = airlineReturnedSplit[1];
//		String airlineReturned1Replaced = airlineReturned1.replace(")", "").trim();
//		System.out.println(airlineReturned1Replaced);
//		Thread.sleep(2000);
//		boolean allMatch = true;
//
//		List<WebElement> airLine = driver.findElements(By.xpath("//div[@class='d-flex flex-column flight-name']"));
//		for(WebElement airLineText : airLine)
//		{
//			String airLineText1 = airLineText.getText();
//			//System.out.println(airLineText1);
//			if(!airLineText1.contains(airlineReturned1Replaced))
//			{
//				allMatch = false;
//			}
//			else
//			{
//				allMatch = true;
//			}
//
//		}
//		if (allMatch) {
//			System.out.println("✅ All flights are SG flights.");
//		} else {
//			System.out.println("❌ Some flights are NOT SG flights.");
//		}
//	}
//	public void validateAirlineFilter(List<String> selectedAirlines,ExtentTest test) throws InterruptedException {
//	    try {
//	        Thread.sleep(2000);
//	        test.log(Status.INFO, " Validating selected airlines: " + selectedAirlines);
//
//	        List<String> selectedCodes = new ArrayList<>();
//	        for (String airline : selectedAirlines) {
//	            if (airline.contains("(") && airline.contains(")")) {
//	                String code = airline.split("\\(")[1].replace(")", "").trim();
//	                selectedCodes.add(code);
//	            }
//	        }
//
//	        if (selectedCodes.isEmpty()) {
//	            test.log(Status.FAIL, "❌ No airline codes extracted for validation.");
//	            Assert.fail("No airline codes extracted.");
//	        }
//
//	        boolean allMatch = true;
//	        List<WebElement> airLineElements = driver.findElements(By.xpath("//div[@class='d-flex flex-column flight-name']"));
//	        test.log(Status.INFO, " Number of flights displayed: " + airLineElements.size());
//
//	        for (WebElement element : airLineElements) {
//	            String displayedAirline = element.getText().trim(); // e.g., "Air India AI-123"
//	            boolean matchFound = false;
//
//	            for (String code : selectedCodes) {
//	                if (displayedAirline.contains(code)) {
//	                    matchFound = true;
//	                    break;
//	                }
//	            }
//
//	            if (!matchFound) {
//	                test.log(Status.FAIL, "❌ Airline mismatch: " + displayedAirline);
//	                allMatch = false;
//	            } else {
//	                test.log(Status.PASS, " Airline matched: " + displayedAirline);
//	            }
//	        }
//
//	        if (!allMatch) {
//	            test.log(Status.FAIL, "❌ Some flights do not match selected airline(s).");
//	            Assert.fail("Flight list contains unmatched airlines.");
//	        } else {
//	            test.log(Status.PASS, "All flights match the selected airline(s).");
//	        }
//
//	    } catch (Exception e) {
//	        test.log(Status.FAIL, "❌ Exception in validateAirlineFilter(): " + e.getMessage());
//	        e.printStackTrace();
//	        Assert.fail("Exception in validateAirlineFilter(): " + e.getMessage());
//	    }
//	}
	public void validateAirlineFilter(List<String> selectedAirlines, ExtentTest test) throws InterruptedException {
	    try {
	        Thread.sleep(2000);
	        test.log(Status.INFO, "Validating selected airlines: " + selectedAirlines);

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
	        test.log(Status.INFO, "Number of flights displayed: " + airLineElements.size());

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
	            }
	            // Removed the success log inside the loop to avoid noise
	        }

	        if (!allMatch) {
	            test.log(Status.FAIL, "❌ Some flights do not match selected airline(s).");
	            Assert.fail("Flight list contains unmatched airlines.");
	        } else {
	            test.log(Status.PASS, "✅ All flights match the selected airline(s).");
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Exception in validateAirlineFilter(): " + e.getMessage());
	        e.printStackTrace();
	        Assert.fail("Exception in validateAirlineFilter(): " + e.getMessage());
	    }
	}


//	public void validateStopsFilter(String stopp) throws InterruptedException {
//		String expectedStop = stopp.toLowerCase();
//		System.out.println("Expected stop (case-insensitive): " + expectedStop);
//		Thread.sleep(2000);
//		boolean allMatch = true;
//
//		List<WebElement> stops = driver.findElements(By.xpath("//span[@class='fs-10 d-inline-block']"));
//		for (WebElement stop : stops) {
//			String stopText = stop.getText().trim();
//			System.out.println("Flight stop info: " + stopText);
//
//			if (!stopText.toLowerCase().contains(expectedStop)) {
//				allMatch = false;
//				System.out.println("❌ Not matched: " + stopText);
//			} else {
//				System.out.println("✅ Matched: " + stopText);
//			}
//		}
//
//		if (allMatch) {
//			System.out.println("✅ All flight stops match: " + stopp);
//		} else {
//			System.out.println("❌ Some flight stops do NOT match: " + stopp);
//		}
//	}
//	public void validateStopsFilter(List<String> expectedStops,ExtentTest test) throws InterruptedException {
//	    try {
//	        Thread.sleep(2000);
//	        List<String> expectedStopsLower = expectedStops.stream()
//	                .map(String::toLowerCase)
//	                .collect(Collectors.toList());
//
//	        test.log(Status.INFO, " Validating stop filters for: " + expectedStopsLower);
//
//	        List<WebElement> stops = driver.findElements(By.xpath("//div[contains(@class,'flight-stopover-info')]//p[@class='stop-seperator']/following-sibling::span"));
//	        boolean allMatch = true;
//
//	        if (stops.isEmpty()) {
//	            test.log(Status.FAIL, "❌ No Flight Found for these sectors");
//                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No Flight Found for these sectors", "FareOptionsMissing");
//
//	            Assert.fail("No Flight Found for these sectors.");
//	        }
//
//	        for (WebElement stop : stops) {
//	            String stopText = stop.getText().trim().toLowerCase();
//	            boolean matched = expectedStopsLower.stream().anyMatch(stopText::contains);
//
//	            if (matched) {
//	                test.log(Status.PASS, "✅ Matched: " + stop.getText().trim());
//	            } else {
//	                test.log(Status.FAIL, "❌ Mismatch: " + stop.getText().trim());
//	                allMatch = false;
//	            }
//	        }
//
//	        if (!allMatch) {
//	            test.log(Status.FAIL, "❌ Some flight stops do NOT match the selected filters.");
//	            Assert.fail("Validation failed: Not all flight stops match selected filters.");
//	        } else {
//	            test.log(Status.PASS, " All flights match the selected stop filters.");
//	        }
//
//	    } catch (Exception e) {
//	        test.log(Status.FAIL, "❌ Exception in validateStopsFilter(): " + e.getMessage());
//	        e.printStackTrace();
//	        Assert.fail("Exception in validateStopsFilter(): " + e.getMessage());
//	    }
//	}
	public void validateStopsFilter(List<String> expectedStops, ExtentTest test) throws InterruptedException {
	    try {
	        Thread.sleep(2000);
	        List<String> expectedStopsLower = expectedStops.stream()
	                .map(String::toLowerCase)
	                .collect(Collectors.toList());

	        test.log(Status.INFO, "Validating stop filters for: " + expectedStopsLower);

	        List<WebElement> stops = driver.findElements(By.xpath("//div[contains(@class,'flight-stopover-info')]//p[@class='stop-seperator']/following-sibling::span"));
	        boolean allMatch = true;

	        if (stops.isEmpty()) {
	            test.log(Status.FAIL, "❌ No Flight Found for these sectors");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No Flight Found for these sectors", "FareOptionsMissing");
	            Assert.fail("No Flight Found for these sectors.");
	        }

	        for (WebElement stop : stops) {
	            String stopText = stop.getText().trim().toLowerCase();
	            boolean matched = expectedStopsLower.stream().anyMatch(stopText::contains);

	            if (!matched) {
	                test.log(Status.FAIL, "❌ Mismatch: " + stop.getText().trim());
	                allMatch = false;
	            }
	            // Removed the success log from here
	        }

	        if (!allMatch) {
	            test.log(Status.FAIL, "❌ Some flight stops do NOT match the selected filters.");
	            Assert.fail("Validation failed: Not all flight stops match selected filters.");
	        } else {
	            test.log(Status.PASS, "✅ All flights match the selected stop filters."); // Only one success log here
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Exception in validateStopsFilter(): " + e.getMessage());
	        e.printStackTrace();
	        Assert.fail("Exception in validateStopsFilter(): " + e.getMessage());
	    }
	}
//public void validateStop(List<String> expectedStops, ExtentTest test)
//{
//	
// List<WebElement> allFlightStopList = driver.findElements(By.xpath("(//div[contains(@class,'flight-info_flight-details')])[1]//p[@class='stop-seperator']/following-sibling::span"));
//for(WebElement allFlightStop:allFlightStopList)
//{
//	String[] allFlightStop1 = allFlightStop.getText().split("at");
//	String allFlightStop2 = allFlightStop1[0].trim();
//		
//	boolean matched = expectedStops.stream().anyMatch(s->s.contains(allFlightStop2));
//	
//}
//}
	public void validateStop(List<String> expectedStops, ExtentTest test) {
	    try {
	    	Thread.sleep(1000);
	        List<WebElement> allFlightStopList = driver.findElements(By.xpath(
	            "(//div[contains(@class,'flight-info_flight-details')])[1]//p[@class='stop-seperator']/following-sibling::span"));

	        if (allFlightStopList.isEmpty()) {
	            test.log(Status.FAIL, "❌ No Flight Found");
	            Assert.fail("No Flight Found.");
	        }

	        boolean allMatched = true;
	        List<String> unexpectedStops = new ArrayList<>();

	        for (WebElement stopElement : allFlightStopList) {
	            String stopText = stopElement.getText(); // e.g., "1 Stop at DOH"
	            String[] splitStop = stopText.split("at");
	            String actualStop = splitStop[0].trim(); // e.g., "1 Stop"

	            boolean matched = expectedStops.stream().anyMatch(expected -> expected.equalsIgnoreCase(actualStop));

	            if (!matched) {
	                unexpectedStops.add(actualStop);
	                allMatched = false;
	            }
	        }

	        if (allMatched) {
	            test.log(Status.PASS, "✅ All stop values matched the expected criteria.");
	        } else {
	            String failMessage = "❌ Unexpected stop values found: " + unexpectedStops;
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, failMessage, "Stop validation failed");
	            test.log(Status.FAIL, failMessage);
	            Assert.fail(failMessage);
	        }

	    } catch (Exception e) {
	        String errorMsg = "❌ Exception in validateStop(): " + e.getMessage();
	        test.log(Status.FAIL, errorMsg);
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, errorMsg, "Exception during stop validation.");
	        e.printStackTrace();
	        Assert.fail(errorMsg);
	    }
	}




//public void ValidateFaretype(List<String> selectedFares)
//{
//	
//	System.out.println(selectedFares);
////List<WebElement> fareTypelist = driver.findElements(By.xpath("//div[text()='Search By Fare types']/parent::div//label");
//    for(int i=0;i<10;i++)
//    {
//    	driver.findElement(By.xpath("(//div[contains(@class,'one-way-new-result-card')]//a[text()='View Price'])[1]")).click();
//    List<WebElement> listOfFare = driver.findElements(By.xpath("//span[text()='Fare Type']/following-sibling::span/span"));
//    if(listOfFare.contains(selectedFares))
//    {
//    	System.out.println("Pass");
//    }
//    }
//}
	public void validateFareType(List<String> selectedFares, ExtentTest test) throws InterruptedException {
		
		((JavascriptExecutor) driver).executeScript(
			    "window.scrollTo({ top: 0, behavior: 'smooth' });"
			);

		
	    System.out.println(" Expected Fare Types: " + selectedFares);
	    
	    boolean hasFailure = false;  // To track if any validation fails

//	    WebElement Button = driver.findElement(
//	        By.xpath("(//div[contains(@class,'one-way-new-result-card')]//a[text()='View Price'])[1]")
//	    );
//	    JavascriptExecutor js = (JavascriptExecutor) driver;
//	    js.executeScript("arguments[0].scrollIntoView(true);", Button);

	    Thread.sleep(2000);
	    List<WebElement> viewPriceButtons = driver.findElements(
	        By.xpath("//div[contains(@class,'one-way-new-result-card')]//a[text()='View Price']")
	    );

	    for (int i = 0; i < viewPriceButtons.size(); i++) {
	        try {
	            // Scroll to the element and click it
	            WebElement button = viewPriceButtons.get(i);
//	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
//	            button.click();
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", button);
	            Thread.sleep(500);
	            button.click();
	            

	            // Wait for fare types to be visible
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	            wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//span[text()='Fare Type']/following-sibling::span/span")
	            ));

	            // Get the fare type texts
	            List<WebElement> fareElements = driver.findElements(
	                By.xpath("//span[text()='Fare Type']/following-sibling::span/span")
	            );
	            List<String> actualFares = fareElements.stream()
	                .map(e -> e.getText().trim().toLowerCase())
	                .collect(Collectors.toList());

	            // Normalize selected fares
	            List<String> expectedFaresLower = selectedFares.stream()
	                .map(s -> s.trim().toLowerCase())
	                .collect(Collectors.toList());

	            System.out.println("Flight " + (i + 1) + " - Displayed Fare Types: " + actualFares);
	            System.out.println(actualFares);
	            System.out.println(expectedFaresLower);

	            if (expectedFaresLower.containsAll(actualFares)) {
	                System.out.println("✅ Flight " + (i + 1) + ": Fare types match");
	                driver.findElement(By.xpath("//a[text()='Hide Price']")).click();
	            } else {
	                System.out.println("❌ Flight " + (i + 1) + ": Fare types do not match expected values");
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Fare types do not match expected values", "FareType Mismatch");

	    	        test.log(Status.FAIL, "❌ Fare Type validation failed for one or more flights."+ "For the Flight Index: "+ i );
                    Assert.fail();
	                hasFailure = true;
	            }

	        } catch (Exception e) {
	            System.out.println(" Exception while validating fare type for flight " + (i + 1) + ": " + e.getMessage());
                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Fare types do not match expected values", "FareType Mismatch");

	            test.log(Status.FAIL, "❌ Exception in validateFareType(): " + e.getMessage());
	            hasFailure = true;
	        }
	    }

	    // Final result after loop
	    if (hasFailure) {
	        test.log(Status.FAIL, "❌ Fare Type validation failed for one or more flights.");
	        Assert.fail("Fare Type validation failed.");
	    } else {
	        test.log(Status.PASS, "✅ Fare Types matched for all flights.");
	    }
	}
public void validateFareTypeRoundTrip(List<String> selectedFares, ExtentTest test) throws InterruptedException {
		
		((JavascriptExecutor) driver).executeScript(
			    "window.scrollTo({ top: 0, behavior: 'smooth' });"
			);

		
	    System.out.println(" Expected Fare Types: " + selectedFares);
	    
	    boolean hasFailure = false;  // To track if any validation fails

//	    WebElement Button = driver.findElement(
//	        By.xpath("(//div[contains(@class,'one-way-new-result-card')]//a[text()='View Price'])[1]")
//	    );
//	    JavascriptExecutor js = (JavascriptExecutor) driver;
//	    js.executeScript("arguments[0].scrollIntoView(true);", Button);

	    Thread.sleep(2000);
	    List<WebElement> viewPriceButtons = driver.findElements(
	        By.xpath("//div[contains(@class,'one-way-new-result-card')]//a[text()='View Price']")
	    );

	    for (int i = 0; i < viewPriceButtons.size(); i++) {
	        try {
	            // Scroll to the element and click it
	            WebElement button = viewPriceButtons.get(i);
//	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
//	            button.click();
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", button);
	            Thread.sleep(500);
	            button.click();
	            

	            // Wait for fare types to be visible
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	            wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//span[text()='Fare Type']/following-sibling::span/span")
	            ));

	            // Get the fare type texts
	            List<WebElement> fareElements = driver.findElements(
	                By.xpath("//span[text()='Fare Type']/following-sibling::span/span")
	            );
	            List<String> actualFares = fareElements.stream()
	                .map(e -> e.getText().trim().toLowerCase())
	                .collect(Collectors.toList());

	            // Normalize selected fares
	            List<String> expectedFaresLower = selectedFares.stream()
	                .map(s -> s.trim().toLowerCase())
	                .collect(Collectors.toList());

	            System.out.println("Flight " + (i + 1) + " - Displayed Fare Types: " + actualFares);
	            System.out.println(actualFares);
	            System.out.println(expectedFaresLower);

	            if (expectedFaresLower.containsAll(actualFares)) {
	                System.out.println("✅ Flight " + (i + 1) + ": Fare types match");
	                driver.findElement(By.xpath("//a[text()='Hide Price']")).click();
	            } else {
	                System.out.println("❌ Flight " + (i + 1) + ": Fare types do not match expected values");
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Fare types do not match expected values", "FareType Mismatch");

	    	        test.log(Status.FAIL, "❌ Fare Type validation failed for one or more flights."+ "For the Flight Index: "+ i );
                    Assert.fail();
	                hasFailure = true;
	            }

	        } catch (Exception e) {
	            System.out.println(" Exception while validating fare type for flight " + (i + 1) + ": " + e.getMessage());
                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Fare types do not match expected values", "FareType Mismatch");

	            test.log(Status.FAIL, "❌ Exception in validateFareType(): " + e.getMessage());
	            hasFailure = true;
	        }
	    }

	    // Final result after loop
	    if (hasFailure) {
	        test.log(Status.FAIL, "❌ Fare Type validation failed for one or more flights.");
	        Assert.fail("Fare Type validation failed.");
	    } else {
	        test.log(Status.PASS, "✅ Fare Types matched for all flights.");
	    }
	}

	public void validateStopsFilterRoundTrip(String stopp) throws InterruptedException {
		String expectedStop = stopp.toLowerCase();
		System.out.println("Expected stop (case-insensitive): " + expectedStop);
		Thread.sleep(2000);
		boolean allMatch = true;

		List<WebElement> stops = driver.findElements(By.xpath("//div[@class=\"spacing-0 flex-1 flight-info_flight-details pb-2 row\"]//p[@class='stop-seperator']/following-sibling::span"));
		for (WebElement stop : stops) {
			String stopText = stop.getText().trim();
			System.out.println("Flight stop info: " + stopText);

			if (!stopText.toLowerCase().contains(expectedStop)) {
				allMatch = false;
				System.out.println("❌ Not matched: " + stopText);
			} else {
				System.out.println("✅ Matched: " + stopText);
			}
		}

		if (allMatch) {
			System.out.println("✅ All flight stops match: " + stopp);
		} else {
			System.out.println("❌ Some flight stops do NOT match: " + stopp);
		}
	}
//	public String fareTypeFilter() throws InterruptedException
//	{
//		String fareText ="ECOVALU";
//		String fareText1  = fareText.toLowerCase();
//		Thread.sleep(30000);
//		ArrayList<String> fare = new ArrayList<>();
//
//		List<WebElement> listOfFares = driver.findElements(By.xpath("//div[text()='Search By Fare types']/parent::div//label[@class=' d-flex  fw-500 align-items-center fs-12 app-check-box cursor-pointer']"));
//		for(WebElement getlistOfFares:listOfFares)
//		{
//			String fareType = getlistOfFares.getText();
//			//			String[] fareTypeSplit1 = fareType.split("\\(");
//			//			String fareTypeText = fareTypeSplit1[0].trim();
//			fare.add(fareType.toLowerCase());
//
//		}
//		System.out.println(fare);
//
//		if(fare.contains(fareText1))
//		{
//
//			System.out.println("user needed fare found");
//			driver.findElement(By.xpath("//div[text()='Search By Fare types']/parent::div//input[@id='ECOVALU']")).click();
//
//		}
//		else
//		{
//			//listOfFares.get(0).click();
//
//		}
//		for (WebElement label : listOfFares) {
//			WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));
//			if (checkbox.isSelected()) {
//				String selectedFare = label.getText().trim();
//				System.out.println("Selected Fare: " + selectedFare);
//				return selectedFare;
//			}
//		}
//		return null;
//
//
//	}
	public List<String> fareTypeFilter(List<String> fareTypeInputs, ExtentTest test) throws InterruptedException {
	    List<String> selectedFares = new ArrayList<>();

	    try {
	        Thread.sleep(30000);
	       // driver.findElement(By.xpath("//div[text()='Search By Fare types']/parent::div//a[text()='Show More']"));
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	     // Wait for the element to be present in the DOM
//	     WebElement showMoreLink = wait.until(ExpectedConditions.presenceOfElementLocated(
//	         By.xpath("//div[text()='Search By Fare types']/parent::div//a[text()='Show More']")
//	     ));
	        /*
	        List<WebElement> showMoreLinks = driver.findElements(
	        	    By.xpath("//div[text()='Search By Fare types']/parent::div//a[text()='Show More']")
	        	);

	        	if (!showMoreLinks.isEmpty()) {
	       	     ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showMoreLinks);
	       	  Thread.sleep(500); 
	        	    showMoreLinks.get(0).click();
	        	}
	        	*/
	        try {
				   
		        By showMoreLocator = By.xpath("//div[text()='Search By Airlines']/parent::div//a[text()='Show More']");
		        
		
		        List<WebElement> showMoreElements = driver.findElements(showMoreLocator);

		        if (!showMoreElements.isEmpty() && showMoreElements.get(0).isDisplayed()) {
		            showMoreElements.get(0).click();
		            System.out.println("✅ Clicked 'Show More' successfully.");
		        } else {
		            System.out.println("Show More' option is not visible or not present.");
		        }
		    } catch (Exception e) {
		        System.out.println("❌ Error while clicking 'Show More': " + e.getMessage());
		    }



//	     // Scroll to the element using JavaScript
//	     ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showMoreLink);
//
//	     // Optional: small wait after scrolling (in case animation happens)
//	     Thread.sleep(500);  // Avoid using sleep in production unless necessary
//
//	     // Click the element
//	     showMoreLink.click();
	     
	        test.log(Status.INFO, " Requested Fare Types: " + fareTypeInputs);

	        ArrayList<String> fare = new ArrayList<>();

	        List<WebElement> listOfFares = driver.findElements(By.xpath(
	            "//div[text()='Search By Fare types']/parent::div//label[contains(@class,'app-check-box cursor-pointer')]"
	        ));

	        if (listOfFares.isEmpty()) {
	            test.log(Status.FAIL, "❌ No Fare Type options found on the page.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "❌ No Fare Type options found on the page.", "error occured while selecting Faretype filter");

	            Assert.fail("No Fare Type filters found.");
	        }

	        for (WebElement getlistOfFares : listOfFares) {
	            String fareType = getlistOfFares.getText();
	            fare.add(fareType.toLowerCase());
	        }

	        System.out.println(fare);
	        test.log(Status.INFO, " Available Fare Types on screen: " + fare);

	        boolean atLeastOneSelected = false;

	        for (String fareText : fareTypeInputs) {
	            String fareText1 = fareText.toLowerCase();
	            System.out.println(fare);
	            System.out.println(fareText1);

	            if (fare.contains(fareText1)) {
	                test.log(Status.PASS, "✅ Fare type found and selected: " + fareText);
	                try {
//	                    driver.findElement(By.xpath(
//	                        "//div[text()='Search By Fare types']/parent::div//input[@id='" + fareText + "']"
//	                    )).click();
	                //	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	                	// Locate the element with dynamic ID (fareText is a String variable)
	                	WebElement fareInput = wait.until(ExpectedConditions.presenceOfElementLocated(
	                	    By.xpath("//div[text()='Search By Fare types']/parent::div//input[@id='" + fareText + "']")
	                	));

	                	// Scroll the element into view
	                	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fareInput);

	                	// Optional: small delay after scrolling
	                	Thread.sleep(1000);

	                	// Click the input
	                	fareInput.click();
	                    atLeastOneSelected = true;
	                } catch (Exception e) {
	                    test.log(Status.FAIL, "❌ Failed to click on fare type checkbox: " + fareText + ". " + e.getMessage());
	    	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "❌ Failed to click on fare type checkbox", "error occured while selecting Faretype filter");

	                    Assert.fail("Click failed for fare type: " + fareText);
	                }
	            } else {
	                test.log(Status.INFO, "Fare type not found: " + fareText);
	            }
	        }

	        // ⏪ Fallback: if none matched, click the first available fare type
	        if (!atLeastOneSelected && !listOfFares.isEmpty()) {
	            WebElement fallbackFare = listOfFares.get(0);
	            //WebElement checkbox = fallbackFare.findElement(By.xpath(".//input[@type='checkbox']"));
	            WebElement checkbox = fallbackFare.findElement(By.xpath(".//input[@type='checkbox']"));

	         // Scroll into center of viewport
	         ((JavascriptExecutor) driver).executeScript(
	             "arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", checkbox
	         );

	         // Optional: wait a bit if needed for animations or visibility
	         Thread.sleep(300); // Or use WebDriverWait for better practice

	         // Click the checkbox
	         checkbox.click();
	         
	            if (!checkbox.isSelected()) {
	                fallbackFare.click();
	                String fallbackText = fallbackFare.getText().trim();
	                selectedFares.add(fallbackText);
	                test.log(Status.INFO, "No matching fare types found. Selected default: " + fallbackText);
	            }
	        }

	        for (WebElement label : listOfFares) {
	            WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));
	            if (checkbox.isSelected()) {
	                String selectedFare = label.getText().trim();
	                selectedFares.add(selectedFare);
	                System.out.println("Selected Fare: " + selectedFare);
	                test.log(Status.INFO, "✔ Selected Fare: " + selectedFare);
	            }
	        }

	        if (selectedFares.isEmpty()) {
	            test.log(Status.FAIL, "❌ No fare types were selected.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No fare types were selected.", "error occured while selecting Faretype filter");

	            Assert.fail("No fare types were selected.");
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Exception in fareTypeFilter(): " + e.getMessage());
	        e.printStackTrace();
            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No fare types were selected.", "error occured while selecting Faretype filter");

	        Assert.fail("Exception in fareTypeFilter(): " + e.getMessage());
	    }

	    return selectedFares;
	}

//	public String refundableinfo() throws InterruptedException
//	{
//		String refunable ="Non-Refundable";
//		String refundableAndNon  = refunable.toLowerCase();
//		//driver.findElement(By.xpath("//div[text()='Refundable Info']//parent::div//label[@class=' d-flex  fw-500 align-items-center fs-12 app-check-box cursor-pointer']"));
//		Thread.sleep(30000);
//		ArrayList<String> refundableList = new ArrayList<>();
//
//		List<WebElement> refunableAndNonRefundable = driver.findElements(By.xpath("//div[text()='Refundable Info']//parent::div//label[@class=' d-flex  fw-500 align-items-center fs-12 app-check-box cursor-pointer']"));
//		for(WebElement getrefunableAndNonRefundable:refunableAndNonRefundable)
//		{
//			String refundable = getrefunableAndNonRefundable.getText();
//			String[] refundableSplit = refundable.split("\\(");
//			String refundableSplitText = refundableSplit[0].trim();
//			refundableList.add(refundableSplitText.toLowerCase());
//
//		}
//		System.out.println(refundableList);
//
//		if(refundableList.contains(refundableAndNon))
//		{
//
//			System.out.println("user needed Option found");
//			driver.findElement(By.xpath("//div[text()='Refundable Info']/parent::div//label[text()='Non-Refundable']//input")).click();
//
//		}
//		else
//		{
//			//refunableAndNonRefundable.get(0).click();
//
//		}
//		for (WebElement label : refunableAndNonRefundable) {
//			WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));
//			if (checkbox.isSelected()) {
//				String refundableInfo = label.getText().trim();
//				System.out.println("Selected airline: " + refundableInfo);
//				return refundableInfo;
//			}
//		}
//		return null;
//	}
	public List<String> refundableinfo(List<String> requiredOptions, ExtentTest test) throws InterruptedException {
		WebElement element = driver.findElement(By.xpath("//div[text()='Refundable Info']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);

	    List<String> selectedOptions = new ArrayList<>();

	    try {
	        Thread.sleep(30000);

	        test.log(Status.INFO, "Requested Refundable Options: " + requiredOptions);

	        List<WebElement> optionLabels = driver.findElements(By.xpath(
	            "//div[text()='Refundable Info']/parent::div//label[contains(@class, 'app-check-box')]"
	        ));

	        if (optionLabels.isEmpty()) {
	            test.log(Status.FAIL, "❌ No refundable options found.");
	            Assert.fail("Refundable filter section is missing.");
	        }

	        List<String> availableOptions = new ArrayList<>();
	        Map<String, WebElement> optionMap = new HashMap<>();

	        for (WebElement label : optionLabels) {
	            String labelText = label.getText().split("\\(")[0].trim().toLowerCase();  // e.g., "non-refundable"
	            availableOptions.add(labelText);
	            optionMap.put(labelText, label);
	        }

	        test.log(Status.INFO, "📋 Available options: " + availableOptions);

	        boolean foundMatch = false;

	        for (String option : requiredOptions) {
	            String optionLower = option.toLowerCase();

	            if (optionMap.containsKey(optionLower)) {
	                WebElement label = optionMap.get(optionLower);
	                WebElement checkbox = label.findElement(By.xpath(".//input[@type='checkbox']"));

	                if (!checkbox.isSelected()) {
	                    label.click();
	                    test.log(Status.PASS, "✅ Selected: " + option);
	                }

	                selectedOptions.add(label.getText().trim());
	                foundMatch = true;
	            } else {
	                test.log(Status.INFO, "⚠️ Option not found on screen: " + option);
	            }
	        }

	        // ✅ Fallback logic: click first index if nothing was selected
	        if (!foundMatch && !optionLabels.isEmpty()) {
	            WebElement firstOption = optionLabels.get(0);
	            WebElement checkbox = firstOption.findElement(By.xpath(".//input[@type='checkbox']"));

	            if (!checkbox.isSelected()) {
	                firstOption.click();
	            }

	            String fallbackSelected = firstOption.getText().trim();
	            selectedOptions.add(fallbackSelected);
	            test.log(Status.INFO, "⚠️ No matching options found. Selected default: " + fallbackSelected);
	        }

	        if (selectedOptions.isEmpty()) {
	            test.log(Status.FAIL, "❌ No refundable options were selected.");
	            Assert.fail("No refundable options matched or were selected.");
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Exception in refundableinfo(): " + e.getMessage());
	        e.printStackTrace();
	        Assert.fail("Exception occurred in refundableinfo(): " + e.getMessage());
	    }

	    return selectedOptions;
	}


//	public void validateRefundableAndRefundable(String refundableInfo) throws InterruptedException
//	{
//		System.out.println(refundableInfo);
//		String refundableInfoToLowerCase = refundableInfo.toLowerCase().replace("-", " ");
//		driver.findElement(By.xpath("(//a[text()='View Price'])[1]")).click();
//		Thread.sleep(3000);
//		List<WebElement> refundableNonRefunableText = driver.findElements(By.xpath("//span[@class='fare_non-refundable']"));
//		System.out.println(refundableNonRefunableText.size());
//		for(WebElement refundableNonRefunable:refundableNonRefunableText)
//		{
//
//			String text = refundableNonRefunable.getText();
//			String refundableText = text.toLowerCase();
//			System.out.println(refundableText);
//			System.out.println(refundableInfoToLowerCase);
//
//			if(refundableText.equals(refundableInfoToLowerCase))
//			{
//				System.out.println("pass");
//			}
//		}
//	}
//	public void validateRefundableAndRefundable(List<String> selectedOptions,ExtentTest test) throws InterruptedException {
//	    try {
//	        driver.findElement(By.xpath("(//a[text()='View Price'])[1]")).click();
//	        Thread.sleep(3000);
//
//	        List<WebElement> refundLabels = driver.findElements(By.xpath("//span[@class='fare_non-refundable']"));
//
//	        if (refundLabels.isEmpty()) {
//	            test.log(Status.FAIL, "❌ No refundable/non-refundable indicators found on the result cards.");
//	            Assert.fail("Refundable info not found on result cards.");
//	        }
//
//	        List<String> selectedNormalized = new ArrayList<>();
//	        for (String opt : selectedOptions) {
//	            selectedNormalized.add(opt.toLowerCase().replace("-", " ").trim());
//	        }
//
//	        test.log(Status.INFO, "🧪 Validating against selected options: " + selectedNormalized);
//
//	        boolean allMatched = true;
//
//	        for (WebElement label : refundLabels) {
//	            String displayedText = label.getText().toLowerCase().replace("-", " ").trim();
//	            System.out.println("Displayed: " + displayedText);
//
//	            if (!selectedNormalized.contains(displayedText)) {
//	                test.log(Status.FAIL, "❌ Mismatch found: " + displayedText);
//	                allMatched = false;
//	            } else {
//	                test.log(Status.PASS, "✅ Match found: " + displayedText);
//	            }
//	        }
//
//	        if (!allMatched) {
//	            Assert.fail("Some flights do not match the selected refundable option(s).");
//	        }
//
//	    } catch (Exception e) {
//	        test.log(Status.FAIL, "❌ Exception in validateRefundableAndRefundable(): " + e.getMessage());
//	        e.printStackTrace();
//	        Assert.fail("Exception occurred during refundable validation: " + e.getMessage());
//	    }
//	}
	public void validateRefundableAndRefundable(List<String> selectedOptions, ExtentTest test) throws InterruptedException {
	    System.out.println("🔍 Expected Refundability Options: " + selectedOptions);

	    boolean hasFailure = false;  // To track if any validation fails
	    WebElement Button = driver.findElement(
		        By.xpath("(//div[contains(@class,'one-way-new-result-card')]//a[text()='View Price'])[1]")
		    );
		    JavascriptExecutor js = (JavascriptExecutor) driver;
		    js.executeScript("arguments[0].scrollIntoView(true);", Button);
	    List<WebElement> viewPriceButtons = driver.findElements(
	        By.xpath("//div[contains(@class,'one-way-new-result-card')]//a[text()='View Price']")
	    );
Thread.sleep(2000);
	    // Normalize selected options
	    List<String> expectedOptionsLower = selectedOptions.stream()
	        .map(s -> s.trim().toLowerCase().replace("-", " "))
	        .collect(Collectors.toList());

	    for (int i = 0; i < viewPriceButtons.size(); i++) {
	        try {
	            WebElement button = viewPriceButtons.get(i);
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
	            button.click();

	            // Wait for refundable/non-refundable labels to load
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	            wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//span[@class='fare-component_value']//span")
	            ));

	            // Get all refundable labels on the card
	            List<WebElement> refundLabels = driver.findElements(
	                By.xpath("//span[@class='fare-component_value']//span")
	            );

	            List<String> actualLabels = refundLabels.stream()
	                .map(e -> e.getText().trim().toLowerCase().replace("-", " "))
	                .collect(Collectors.toList());

	            System.out.println("Flight " + (i + 1) + " - Displayed Refundability: " + actualLabels);
	            test.log(Status.INFO, "✈️ Flight " + (i + 1) + " - Refundability: " + actualLabels);

	            if (expectedOptionsLower.containsAll(actualLabels)) {
	                System.out.println("✅ Flight " + (i + 1) + ": Refundability matches");
	                test.log(Status.PASS, "✅ Refundability matches for flight index: " + i);
	                driver.findElement(By.xpath("//a[text()='Hide Price']")).click();
	            } else {
	                System.out.println("❌ Flight " + (i + 1) + ": Refundability does not match expected values");
	                test.log(Status.FAIL, "❌ Refundability mismatch for flight index: " + i);
	                hasFailure = true;
	            }

	        } catch (Exception e) {
	            System.out.println("⚠️ Exception while validating refundability for flight " + (i + 1) + ": " + e.getMessage());
	            test.log(Status.FAIL, "❌ Exception during refundability check at index " + i + ": " + e.getMessage());
	            hasFailure = true;
	        }
	    }

	    if (hasFailure) {
	        test.log(Status.FAIL, "❌ Refundability validation failed for one or more flights.");
	        Assert.fail("Refundability validation failed.");
	    } else {
	        test.log(Status.PASS, "✅ Refundability matches for all flights.");
	    }
	}

//	public String clickOnArrivalFilter() throws InterruptedException
//	{
//		Thread.sleep(30000);
//		ArrayList<String> Arrival = new ArrayList<>();
//
//		List<WebElement> arrival = driver.findElements(By.xpath("//div[text()='Arrival Time']/parent::div//div[@class='app-chip m-1 text-center flex-1 ']"));
//		for(WebElement getlistOfArrival:arrival)
//		{
//			String arrivalRange = getlistOfArrival.getText();
//
//
//			Arrival.add(arrivalRange.toLowerCase());
//
//		}
//		System.out.println(Arrival);
//
//		if(Arrival.contains("00 -06"))
//		{
//
//			System.out.println("user needed airline found");
//			WebElement element = driver.findElement(By.xpath("//div[text()='Arrival Time']/parent::div//span[text()='00 -06']"));
//			JavascriptExecutor js = (JavascriptExecutor) driver;
//			js.executeScript("arguments[0].scrollIntoView(true);", element);
//			Thread.sleep(500);
//			element.click();
//			String clickedElementText = element.getText();
//			System.out.println("Selected ArrivalTime: " + clickedElementText);
//			return clickedElementText;
//
//		}
//		else
//		{
//			arrival.get(0).click();
//
//		}
//		return null;
//
//	}
//	public List<String> clickOnArrivalFilter(List<String> desiredRanges,ExtentTest test) throws InterruptedException {
//	    List<String> selectedArrivals = new ArrayList<>();
//
//	    try {
//	        Thread.sleep(30000);
//
//	        List<WebElement> arrivalChips = driver.findElements(By.xpath(
//	            "//div[text()='Arrival Time']/parent::div//div[contains(@class,'app-chip')]"
//	        ));
//
//	        if (arrivalChips.isEmpty()) {
//	            test.log(Status.FAIL, "❌ No arrival time options found.");
//	            Assert.fail("Arrival time chips are missing.");
//	        }
//
//	        List<String> allArrivalOptions = new ArrayList<>();
//	        Map<String, WebElement> arrivalMap = new HashMap<>();
//
//	        for (WebElement chip : arrivalChips) {
//	            String text = chip.getText().trim();
//	            allArrivalOptions.add(text.toLowerCase());
//	            arrivalMap.put(text.toLowerCase(), chip);
//	        }
//
//	        test.log(Status.INFO, "📋 Available Arrival Options: " + allArrivalOptions);
//
//	        for (String desired : desiredRanges) {
//	            String desiredLower = desired.toLowerCase();
//	            if (arrivalMap.containsKey(desiredLower)) {
//	                WebElement chip = arrivalMap.get(desiredLower);
//	                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", chip);
//	                Thread.sleep(500);
//	                chip.click();
//	                selectedArrivals.add(desired);
//	                test.log(Status.PASS, "✅ Selected Arrival Time: " + desired);
//	            } else {
//	                test.log(Status.WARNING, "⚠️ Desired arrival range not found on screen: " + desired);
//	            }
//	        }
//
//	        if (selectedArrivals.isEmpty()) {
//	            test.log(Status.FAIL, "❌ No desired arrival filters were selected.");
//	            Assert.fail("Arrival filter not applied.");
//	        }
//
//	    } catch (Exception e) {
//	        test.log(Status.FAIL, "❌ Exception in clickOnArrivalFilter(): " + e.getMessage());
//	        e.printStackTrace();
//	        Assert.fail("Exception in arrival filter selection.");
//	    }
//
//	    return selectedArrivals;
//	}
	public List<String> clickOnArrivalFilter(List<String> desiredRanges, ExtentTest test) throws InterruptedException {
	    List<String> selectedArrivals = new ArrayList<>();

	    try {
	        Thread.sleep(30000);

	        List<WebElement> arrivalChips = driver.findElements(By.xpath(
	            "//div[text()='Arrival Time']/parent::div//div[contains(@class,'app-chip')]"
	        ));

	        if (arrivalChips.isEmpty()) {
	            test.log(Status.FAIL, "❌ No arrival time options found.");
	            Assert.fail("Arrival time chips are missing.");
	        }

	        List<String> allArrivalOptions = new ArrayList<>();
	        Map<String, WebElement> arrivalMap = new HashMap<>();

	        for (WebElement chip : arrivalChips) {
	            String text = chip.getText().trim();
	            allArrivalOptions.add(text.toLowerCase());
	            arrivalMap.put(text.toLowerCase(), chip);
	        }

	        test.log(Status.INFO, "📋 Available Arrival Options: " + allArrivalOptions);

	        boolean foundMatch = false;

	        for (String desired : desiredRanges) {
	            String desiredLower = desired.toLowerCase();
	            if (arrivalMap.containsKey(desiredLower)) {
	                WebElement chip = arrivalMap.get(desiredLower);
	                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", chip);
	                Thread.sleep(500);
	                chip.click();
	                selectedArrivals.add(desired);
	                test.log(Status.PASS, "✅ Selected Arrival Time: " + desired);
	                foundMatch = true;
	            } else {
	                test.log(Status.WARNING, "⚠️ Desired arrival range not found on screen: " + desired);
	            }
	        }

	        // ✅ Fallback: Click first chip if no matches found
	        if (!foundMatch) {
	            WebElement fallbackChip = arrivalChips.get(0);
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fallbackChip);
	            Thread.sleep(500);
	            fallbackChip.click();
	            String fallbackSelected = fallbackChip.getText().trim();
	            selectedArrivals.add(fallbackSelected);
	            test.log(Status.WARNING, "⚠️ No requested filters matched. Selected default: " + fallbackSelected);
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Exception in clickOnArrivalFilter(): " + e.getMessage());
	        e.printStackTrace();
	        Assert.fail("Exception in arrival filter selection.");
	    }

	    return selectedArrivals;
	}

//	public String clickOnDepartureFilter() throws InterruptedException
//	{
//		Thread.sleep(30000);
//		ArrayList<String> Departure = new ArrayList<>();
//
//		List<WebElement> departure = driver.findElements(By.xpath("//div[text()='Departure Time']/parent::div//div[@class='app-chip m-1 text-center flex-1 ']"));
//		for(WebElement getlistOfDeparture:departure)
//		{
//			String departureRange = getlistOfDeparture.getText();
//
//
//			Departure.add(departureRange.toLowerCase());
//
//		}
//		System.out.println(Departure);
//
//		if(Departure.contains("00 -06"))
//		{
//
//			System.out.println("user needed airline found");
//			WebElement element = driver.findElement(By.xpath("//div[text()='Departure Time']/parent::div//span[text()='00 -06']"));
//			JavascriptExecutor js = (JavascriptExecutor) driver;
//			js.executeScript("arguments[0].scrollIntoView(true);", element);
//			Thread.sleep(500);
//			element.click();
//			String clickedElementText = element.getText();
//			System.out.println("Selected ArrivalTime: " + clickedElementText);
//			return clickedElementText;
//
//		}
//		else
//		{
//			departure.get(0).click();
//
//		}
//		return null;
//
//	}
	public List<String> clickOnDepartureFilter(List<String> desiredRanges, ExtentTest test) throws InterruptedException {
	    List<String> selectedDepartures = new ArrayList<>();

	    try {
	        Thread.sleep(30000);

	        List<WebElement> departureChips = driver.findElements(By.xpath(
	            "//div[text()='Departure Time']/parent::div//div[contains(@class,'app-chip')]"
	        ));

	        if (departureChips.isEmpty()) {
	            test.log(Status.FAIL, "❌ No departure time options found.");
	            Assert.fail("Departure time chips are missing.");
	        }

	        List<String> allDepartureOptions = new ArrayList<>();
	        Map<String, WebElement> departureMap = new HashMap<>();

	        for (WebElement chip : departureChips) {
	            String text = chip.getText().trim();
	            allDepartureOptions.add(text.toLowerCase());
	            departureMap.put(text.toLowerCase(), chip);
	        }

	        test.log(Status.INFO, "📋 Available Departure Options: " + allDepartureOptions);

	        boolean foundMatch = false;

	        for (String desired : desiredRanges) {
	            String desiredLower = desired.toLowerCase();
	            if (departureMap.containsKey(desiredLower)) {
	                WebElement chip = departureMap.get(desiredLower);
	                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", chip);
	                Thread.sleep(500);
	                chip.click();
	                selectedDepartures.add(desired);
	                test.log(Status.PASS, "✅ Selected Departure Time: " + desired);
	                foundMatch = true;
	            } else {
	                test.log(Status.WARNING, "⚠️ Desired departure range not found on screen: " + desired);
	            }
	        }

	        // ✅ Fallback: Click first chip if no matches found
	        if (!foundMatch) {
	            WebElement fallbackChip = departureChips.get(0);
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fallbackChip);
	            Thread.sleep(500);
	            fallbackChip.click();
	            String fallbackSelected = fallbackChip.getText().trim();
	            selectedDepartures.add(fallbackSelected);
	            test.log(Status.WARNING, "⚠️ No requested filters matched. Selected default: " + fallbackSelected);
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Exception in clickOnDepartureFilter(): " + e.getMessage());
	        e.printStackTrace();
	        Assert.fail("Exception in departure filter selection.");
	    }

	    return selectedDepartures;
	}

	//public void ValidateArrivalTime(String clickedElementText)
	//{
	//	System.out.println(clickedElementText);
	//	if(clickedElementText.contains("00"))
	//	{
	//		 clickedElementText = clickedElementText.replace("00", "24");
	//		System.out.println(clickedElementText);
	//		String[] startSplit = clickedElementText.split("-");
	//		String Start = startSplit[0].trim();
	//		System.out.println(Start);
	//		String end = startSplit[1].trim();
	//		System.out.println(end);
	//
	//
	//		List<WebElement> arrivalTimeList = driver.findElements(By.xpath("//span[contains(@class, 'flight-arrtime')]"));
	//		for(WebElement arrivalTime:arrivalTimeList)
	//		{
	//			String arrivalTimeText = arrivalTime.getText();
	//			String[] arrivalTimeTextSplit = arrivalTimeText.split(":");
	//			String arrivalTimeTextSplit1 = arrivalTimeTextSplit[0].trim();
	//			System.out.println(arrivalTimeTextSplit1);
	//			int arrivalTime1 = Integer.parseInt(arrivalTimeTextSplit1);
	//			int start1 = Integer.parseInt(Start);
	//			int end1 = Integer.parseInt(end);
	//
	//			if(arrivalTime1 > start1 && arrivalTime1 < end1)
	//			{
	//				
	//			}
	//			
	//			
	//		}
	//	}
	//
	//
	//
	//}
	//public void ValidateArrivalTime(String clickedElementText) throws InterruptedException {
	//	Thread.sleep(2000);
	//    System.out.println("Clicked Range: " + clickedElementText);
	//
	//    if (clickedElementText.contains("00")) {
	//        clickedElementText = clickedElementText.replace("00", "24");
	//    }
	//
	//    String[] startSplit = clickedElementText.split("-");
	//    String Start = startSplit[0].trim();
	//    String end = startSplit[1].trim();
	//
	//    System.out.println("Start: " + Start);
	//    System.out.println("End: " + end);
	//
	//    int start1 = Integer.parseInt(Start);
	//    int end1 = Integer.parseInt(end);
	//
	//    List<WebElement> arrivalTimeList = driver.findElements(By.xpath("//span[contains(@class, 'flight-arrtime')]"));
	//
	//    for (WebElement arrivalTime : arrivalTimeList) {
	//        String arrivalTimeText = arrivalTime.getText(); // e.g., "00:45"
	//        String[] arrivalTimeTextSplit = arrivalTimeText.split(":");
	//        String arrivalTimeTextSplit1 = arrivalTimeTextSplit[0].trim();
	//
	//        // Convert "00" to "24" for consistency
	//        if (arrivalTimeTextSplit1.equals("00")) {
	//            arrivalTimeTextSplit1 = "24";
	//        }
	//
	//        int arrivalTime1 = Integer.parseInt(arrivalTimeTextSplit1);
	//        System.out.println("Checking arrival hour: " + arrivalTime1);
	//
	//        boolean isInRange;
	//        if (start1 <= end1) {
	//            // Non-wrapped range, e.g. 10 - 18
	//            isInRange = (arrivalTime1 >= start1 && arrivalTime1 <= end1);
	//        } else {
	//            // Wrapped range across midnight, e.g. 24 - 6
	//            isInRange = (arrivalTime1 >= start1 || arrivalTime1 <= end1);
	//        }
	//
	//        if (isInRange) {
	//            System.out.println("✅ Arrival time " + arrivalTime1 + " is in range.");
	//        } else {
	//            System.out.println("❌ Arrival time " + arrivalTime1 + " is NOT in range.");
	//        }
	//    }
	//}
//	public void ValidateArrivalTime(String clickedElementText) throws InterruptedException {
//		Thread.sleep(2000);
//		System.out.println("Clicked Range: " + clickedElementText);
//
//		if (clickedElementText.contains("00")) {
//			clickedElementText = clickedElementText.replace("00", "24");
//		}
//
//		String[] startSplit = clickedElementText.split("-");
//		String Start = startSplit[0].trim();
//		String end = startSplit[1].trim();
//
//		System.out.println("Start: " + Start);
//		System.out.println("End: " + end);
//
//		int start1 = Integer.parseInt(Start);
//		int end1 = Integer.parseInt(end);
//
//		List<WebElement> arrivalTimeList = driver.findElements(By.xpath("//span[contains(@class, 'flight-arrtime')]"));
//
//		boolean allInRange = true;
//		List<Integer> outOfRangeTimes = new ArrayList<>();
//
//		for (WebElement arrivalTime : arrivalTimeList) {
//			String arrivalTimeText = arrivalTime.getText(); // e.g., "00:45"
//			String[] arrivalTimeTextSplit = arrivalTimeText.split(":");
//			String arrivalTimeTextSplit1 = arrivalTimeTextSplit[0].trim();
//
//			// Convert "00" to "24" for consistency
//			if (arrivalTimeTextSplit1.equals("00")) {
//				arrivalTimeTextSplit1 = "24";
//			}
//
//			int arrivalTime1 = Integer.parseInt(arrivalTimeTextSplit1);
//			System.out.println("Checking arrival hour: " + arrivalTime1);
//
//			boolean isInRange;
//			if (start1 <= end1) {
//				// Non-wrapped range, e.g. 10 - 18
//				isInRange = (arrivalTime1 >= start1 && arrivalTime1 <= end1);
//			} else {
//				// Wrapped range across midnight, e.g. 24 - 6
//				isInRange = (arrivalTime1 >= start1 || arrivalTime1 <= end1);
//			}
//
//			if (isInRange) {
//				System.out.println("✅ Arrival time " + arrivalTime1 + " is in range.");
//			} else {
//				System.out.println("❌ Arrival time " + arrivalTime1 + " is NOT in range.");
//				allInRange = false;
//				outOfRangeTimes.add(arrivalTime1);
//			}
//		}
//
//		if (allInRange) {
//			System.out.println("✅ All arrival times are in range.");
//		} else {
//			System.out.println("❌ Some arrival times are NOT in range. Out of range: " + outOfRangeTimes);
//			// Optional: throw an exception or assert
//			// throw new AssertionError("Arrival times out of range: " + outOfRangeTimes);
//		}
//	}
//	public void ValidateArrivalTime(List<String> selectedRanges,ExtentTest test) throws InterruptedException {
//	    try {
//	    	((JavascriptExecutor) driver).executeScript(
//				    "window.scrollTo({ top: 0, behavior: 'smooth' });"
//				);
//	    	
//	        Thread.sleep(2000);
//	        boolean allValid = true;
//	        List<Integer> outOfRangeHours = new ArrayList<>();
//
//	        // Normalize selected ranges
//	        List<int[]> parsedRanges = new ArrayList<>();
//	        for (String range : selectedRanges) {
//	            String normalized = range.replace("00", "24");
//	            String[] parts = normalized.split("-");
//	            int start = Integer.parseInt(parts[0].trim());
//	            int end = Integer.parseInt(parts[1].trim());
//	            parsedRanges.add(new int[]{start, end});
//	        }
//
//	        List<WebElement> arrivalTimes = driver.findElements(By.xpath("//span[contains(@class, 'flight-arrtime')]"));
//
//	        if (arrivalTimes.isEmpty()) {
//	            test.log(Status.FAIL, "❌ No arrival time elements found in results.");
//	            Assert.fail("Arrival time not displayed in results.");
//	        }
//
//	        for (WebElement element : arrivalTimes) {
//	            String text = element.getText().trim(); // e.g., "00:45"
//	            String hourPart = text.split(":")[0].trim();
//
//	            if (hourPart.equals("00")) hourPart = "24";
//	            int arrivalHour = Integer.parseInt(hourPart);
//
//	            boolean inAnyRange = false;
//	            for (int[] range : parsedRanges) {
//	                int start = range[0];
//	                int end = range[1];
//
//	                if ((start <= end && arrivalHour >= start && arrivalHour <= end) ||
//	                    (start > end && (arrivalHour >= start || arrivalHour <= end))) {
//	                    inAnyRange = true;
//	                    break;
//	                }
//	            }
//
//	            if (inAnyRange) {
//	                test.log(Status.PASS, "✅ Arrival " + arrivalHour + ":00 is within selected range.");
//	            } else {
//	                test.log(Status.FAIL, "❌ Arrival " + arrivalHour + ":00 is outside selected range.");
//	                outOfRangeHours.add(arrivalHour);
//	                allValid = false;
//	            }
//	        }
//
//	        if (!allValid) {
//	            Assert.fail("Some flights have arrival times out of selected range: " + outOfRangeHours);
//	        }
//
//	    } catch (Exception e) {
//	        test.log(Status.FAIL, "❌ Exception in ValidateArrivalTime(): " + e.getMessage());
//	        e.printStackTrace();
//	        Assert.fail("Exception during arrival time validation.");
//	    }
//	}
//	public void ValidateArrivalTime(List<String> selectedRanges, ExtentTest test) throws InterruptedException {
//	    try {
//	        ((JavascriptExecutor) driver).executeScript("window.scrollTo({ top: 0, behavior: 'smooth' });");
//	        Thread.sleep(2000);
//
//	        boolean allValid = true;
//	        List<Integer> outOfRangeHours = new ArrayList<>();
//
//	        // Normalize selected ranges
//	        List<int[]> parsedRanges = new ArrayList<>();
//	        for (String range : selectedRanges) {
//	            String normalized = range.replace("00", "24");
//	            String[] parts = normalized.split("-");
//	            int start = Integer.parseInt(parts[0].trim());
//	            int end = Integer.parseInt(parts[1].trim());
//	            parsedRanges.add(new int[]{start, end});
//	        }
//
//	        List<WebElement> arrivalTimes = driver.findElements(By.xpath("//span[contains(@class, 'flight-arrtime')]"));
//
//	        if (arrivalTimes.isEmpty()) {
//	            test.log(Status.FAIL, "❌ No arrival time elements found in results.");
//	              ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "❌ No arrival time elements found in results.", "Arrival Time filter not found");
//
//	            Assert.fail("Arrival time not displayed in results.");
//	        }
//
//	        for (WebElement element : arrivalTimes) {
//	            String text = element.getText().trim(); // e.g., "00:45"
//	            String hourPart = text.split(":")[0].trim();
//
//	            if (hourPart.equals("00")) hourPart = "24";
//	            int arrivalHour = Integer.parseInt(hourPart);
//
//	            boolean inAnyRange = false;
//	            for (int[] range : parsedRanges) {
//	                int start = range[0];
//	                int end = range[1];
//
//	                if ((start <= end && arrivalHour >= start && arrivalHour <= end) ||
//	                    (start > end && (arrivalHour >= start || arrivalHour <= end))) {
//	                    inAnyRange = true;
//	                    break;
//	                }
//	            }
//
//	            if (!inAnyRange) {
//		              ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "", "");
//
//	                test.log(Status.FAIL, "❌ Arrival " + arrivalHour + ":00 is outside selected range.");
//	                outOfRangeHours.add(arrivalHour);
//	                allValid = false;
//	            }
//	        }
//
//	        if (!allValid) {
//	              ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "", "");
//
//	            Assert.fail("Some flights have arrival times out of selected range: " + outOfRangeHours);
//	        }
//
//	    } catch (Exception e) {
//	        test.log(Status.FAIL, "❌ Exception in ValidateArrivalTime(): " + e.getMessage());
//	        e.printStackTrace();
//            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "", "");
//
//	        Assert.fail("Exception during arrival time validation.");
//	    }
//	}
	public void ValidateArrivalTime(List<String> selectedRanges, ExtentTest test) throws InterruptedException {
	    try {
	        ((JavascriptExecutor) driver).executeScript("window.scrollTo({ top: 0, behavior: 'smooth' });");
	        Thread.sleep(2000);

	        boolean allValid = true;
	        List<Integer> outOfRangeHours = new ArrayList<>();

	        // Normalize selected ranges
	        List<int[]> parsedRanges = new ArrayList<>();
	        for (String range : selectedRanges) {
	            String normalized = range.replace("00", "24");
	            String[] parts = normalized.split("-");
	            int start = Integer.parseInt(parts[0].trim());
	            int end = Integer.parseInt(parts[1].trim());
	            parsedRanges.add(new int[]{start, end});
	        }

	        List<WebElement> arrivalTimes = driver.findElements(By.xpath("//span[contains(@class, 'flight-arrtime')]"));

	        if (arrivalTimes.isEmpty()) {
	            String message = "❌ No arrival time elements found in results.";
	            test.log(Status.FAIL, message);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, message, "Arrival Time filter not found");
	            Assert.fail("Arrival time not displayed in results.");
	        }

	        for (WebElement element : arrivalTimes) {
	            String text = element.getText().trim(); // e.g., "00:45"
	            String hourPart = text.split(":")[0].trim();

	            if (hourPart.equals("00")) hourPart = "24";
	            int arrivalHour = Integer.parseInt(hourPart);

	            boolean inAnyRange = false;
	            for (int[] range : parsedRanges) {
	                int start = range[0];
	                int end = range[1];

	                if ((start <= end && arrivalHour >= start && arrivalHour <= end) ||
	                    (start > end && (arrivalHour >= start || arrivalHour <= end))) {
	                    inAnyRange = true;
	                    break;
	                }
	            }

	            if (!inAnyRange) {
	                test.log(Status.FAIL, "❌ Arrival " + arrivalHour + ":00 is outside selected range.");
	                outOfRangeHours.add(arrivalHour);
	                allValid = false;
	            }
	        }

	        if (!allValid) {
	            String message = "❌ Some flights have arrival times out of selected range: " + outOfRangeHours;
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, message, "Arrival time validation failed");
	            Assert.fail(message);
	        } else {
	            test.log(Status.INFO, "✅ All arrival times are within the selected range.");
	        }

	    } catch (Exception e) {
	        String errorMsg = "❌ Exception in ValidateArrivalTime(): " + e.getMessage();
	        test.log(Status.FAIL, errorMsg);
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, errorMsg, "Exception occurred");
	        e.printStackTrace();
	        Assert.fail("Exception during arrival time validation.");
	    }
	}


	//public void ValidateDepartureTime(String clickedElementText) throws InterruptedException {
	//	Thread.sleep(2000);
	//    System.out.println("Clicked Range: " + clickedElementText);
	//
	//    if (clickedElementText.contains("00")) {
	//        clickedElementText = clickedElementText.replace("00", "24");
	//    }
	//
	//    String[] startSplit = clickedElementText.split("-");
	//    String Start = startSplit[0].trim();
	//    String end = startSplit[1].trim();
	//
	//    System.out.println("Start: " + Start);
	//    System.out.println("End: " + end);
	//
	//    int start1 = Integer.parseInt(Start);
	//    int end1 = Integer.parseInt(end);
	//
	//    List<WebElement> departureTimeList = driver.findElements(By.xpath("//span[contains(@class, 'flight-arrtime')]"));
	//
	//    for (WebElement departureTime : departureTimeList) {
	//        String departureTimeText = departureTime.getText(); // e.g., "00:45"
	//        String[] departureTimeTextSplit = departureTimeText.split(":");
	//        String departureTimeTextSplit1 = departureTimeTextSplit[0].trim();
	//
	//        // Convert "00" to "24" for consistency
	//        if (departureTimeTextSplit1.equals("00")) {
	//        	departureTimeTextSplit1 = "24";
	//        }
	//
	//        int departureTime1 = Integer.parseInt(departureTimeTextSplit1);
	//        System.out.println("Checking departure hour: " + departureTime1);
	//
	//        boolean isInRange;
	//        if (start1 <= end1) {
	//            // Non-wrapped range, e.g. 10 - 18
	//            isInRange = (departureTime1 >= start1 && departureTime1 <= end1);
	//        } else {
	//            // Wrapped range across midnight, e.g. 24 - 6
	//            isInRange = (departureTime1 >= start1 || departureTime1 <= end1);
	//        }
	//
	//        if (isInRange) {
	//            System.out.println("✅ Departure time " + departureTime1 + " is in range.");
	//        } else {
	//            System.out.println("❌ Departure time " + departureTime1 + " is NOT in range.");
	//        }
	//    }
	//}
//	public void ValidateDepartureTime(String clickedElementText) throws InterruptedException {
//		Thread.sleep(2000);
//		System.out.println("Clicked Range: " + clickedElementText);
//
//		if (clickedElementText.contains("00")) {
//			clickedElementText = clickedElementText.replace("00", "24");
//		}
//
//		String[] startSplit = clickedElementText.split("-");
//		String Start = startSplit[0].trim();
//		String end = startSplit[1].trim();
//
//		System.out.println("Start: " + Start);
//		System.out.println("End: " + end);
//
//		int start1 = Integer.parseInt(Start);
//		int end1 = Integer.parseInt(end);
//
//		List<WebElement> departureTimeList = driver.findElements(By.xpath("//span[contains(@class, 'flight-deptime')]"));
//
//		boolean allInRange = true;  // Add this flag
//		List<Integer> outOfRangeTimes = new ArrayList<>(); // For reporting
//
//		for (WebElement departureTime : departureTimeList) {
//			String departureTimeText = departureTime.getText(); // e.g., "00:45"
//			String[] departureTimeTextSplit = departureTimeText.split(":");
//			String departureTimeTextSplit1 = departureTimeTextSplit[0].trim();
//
//			if (departureTimeTextSplit1.equals("00")) {
//				departureTimeTextSplit1 = "24";
//			}
//
//			int departureTime1 = Integer.parseInt(departureTimeTextSplit1);
//			System.out.println("Checking departure hour: " + departureTime1);
//
//			boolean isInRange;
//			if (start1 <= end1) {
//				isInRange = (departureTime1 >= start1 && departureTime1 <= end1);
//			} else {
//				isInRange = (departureTime1 >= start1 || departureTime1 <= end1);
//			}
//
//			if (isInRange) {
//				System.out.println("✅ Departure time " + departureTime1 + " is in range.");
//			} else {
//				System.out.println("❌ Departure time " + departureTime1 + " is NOT in range.");
//				allInRange = false;
//				outOfRangeTimes.add(departureTime1);
//			}
//		}
//
//		if (allInRange) {
//			System.out.println("✅ All departure times are in range.");
//		} else {
//			System.out.println("❌ Some departure times are NOT in range. Offenders: " + outOfRangeTimes);
//			// Optional: throw an exception or fail a test assertion here
//			// throw new AssertionError("Departure times out of range: " + outOfRangeTimes);
//		}
//	}
//	public void ValidateDepartureTime(List<String> selectedRanges, ExtentTest test) throws InterruptedException {
//	    try {
//	    	((JavascriptExecutor) driver).executeScript(
//				    "window.scrollTo({ top: 0, behavior: 'smooth' });"
//				);
//	        Thread.sleep(2000);
//	        boolean allValid = true;
//	        List<Integer> outOfRangeHours = new ArrayList<>();
//
//	        // Normalize selected ranges
//	        List<int[]> parsedRanges = new ArrayList<>();
//	        for (String range : selectedRanges) {
//	            String normalized = range.replace("00", "24");
//	            String[] parts = normalized.split("-");
//	            int start = Integer.parseInt(parts[0].trim());
//	            int end = Integer.parseInt(parts[1].trim());
//	            parsedRanges.add(new int[]{start, end});
//	        }
//
//	        List<WebElement> departureTimes = driver.findElements(By.xpath("//span[contains(@class, 'flight-deptime')]"));
//
//	        if (departureTimes.isEmpty()) {
//	            test.log(Status.FAIL, "❌ No departure time elements found in results.");
//	            Assert.fail("Departure time not displayed in results.");
//	        }
//
//	        for (WebElement element : departureTimes) {
//	            String text = element.getText().trim(); // e.g., "00:45"
//	            String hourPart = text.split(":")[0].trim();
//
//	            if (hourPart.equals("00")) hourPart = "24";
//	            int departureHour = Integer.parseInt(hourPart);
//
//	            boolean inAnyRange = false;
//	            for (int[] range : parsedRanges) {
//	                int start = range[0];
//	                int end = range[1];
//
//	                if ((start <= end && departureHour >= start && departureHour <= end) ||
//	                    (start > end && (departureHour >= start || departureHour <= end))) {
//	                    inAnyRange = true;
//	                    break;
//	                }
//	            }
//
//	            if (inAnyRange) {
//	                test.log(Status.PASS, "✅ Departure " + departureHour + ":00 is within selected range.");
//	            } else {
//	                test.log(Status.FAIL, "❌ Departure " + departureHour + ":00 is outside selected range.");
//	                outOfRangeHours.add(departureHour);
//	                allValid = false;
//	            }
//	        }
//
//	        if (!allValid) {
//	            Assert.fail("Some flights have departure times out of selected range: " + outOfRangeHours);
//	        }
//
//	    } catch (Exception e) {
//	        test.log(Status.FAIL, "❌ Exception in ValidateDepartureTime(): " + e.getMessage());
//	        e.printStackTrace();
//	        Assert.fail("Exception during departure time validation.");
//	    }
//	}
//	public void ValidateDepartureTime(List<String> selectedRanges, ExtentTest test) throws InterruptedException {
//	    try {
//	        ((JavascriptExecutor) driver).executeScript(
//	            "window.scrollTo({ top: 0, behavior: 'smooth' });"
//	        );
//	        Thread.sleep(2000);
//
//	        boolean allValid = true;
//	        List<Integer> outOfRangeHours = new ArrayList<>();
//
//	        // Normalize selected ranges
//	        List<int[]> parsedRanges = new ArrayList<>();
//	        for (String range : selectedRanges) {
//	            String normalized = range.replace("00", "24");
//	            String[] parts = normalized.split("-");
//	            int start = Integer.parseInt(parts[0].trim());
//	            int end = Integer.parseInt(parts[1].trim());
//	            parsedRanges.add(new int[]{start, end});
//	        }
//
//	        List<WebElement> departureTimes = driver.findElements(By.xpath("//span[contains(@class, 'flight-deptime')]"));
//
//	        if (departureTimes.isEmpty()) {
//	            test.log(Status.FAIL, "❌ No departure time elements found in results.");
//	            Assert.fail("Departure time not displayed in results.");
//	        }
//
//	        for (WebElement element : departureTimes) {
//	            String text = element.getText().trim(); // e.g., "00:45"
//	            String hourPart = text.split(":")[0].trim();
//
//	            if (hourPart.equals("00")) hourPart = "24";
//	            int departureHour = Integer.parseInt(hourPart);
//
//	            boolean inAnyRange = false;
//	            for (int[] range : parsedRanges) {
//	                int start = range[0];
//	                int end = range[1];
//
//	                if ((start <= end && departureHour >= start && departureHour <= end) ||
//	                    (start > end && (departureHour >= start || departureHour <= end))) {
//	                    inAnyRange = true;
//	                    break;
//	                }
//	            }
//
//	            if (!inAnyRange) {
//	                test.log(Status.FAIL, "❌ Departure " + departureHour + ":00 is outside selected range.");
//	                outOfRangeHours.add(departureHour);
//	                allValid = false;
//	            }
//	        }
//
//	        if (allValid) {
//	            test.log(Status.INFO, "✅ All departure times are within the selected range.");
//	        } else {
//	            Assert.fail("Some flights have departure times out of selected range: " + outOfRangeHours);
//	        }
//
//	    } catch (Exception e) {
//	        test.log(Status.FAIL, "❌ Exception in ValidateDepartureTime(): " + e.getMessage());
//	        e.printStackTrace();
//	        Assert.fail("Exception during departure time validation.");
//	    }
//	}

	public void ValidateDepartureTime(List<String> selectedRanges, ExtentTest test) throws InterruptedException {
	    try {
	        ((JavascriptExecutor) driver).executeScript(
	            "window.scrollTo({ top: 0, behavior: 'smooth' });"
	        );
	        Thread.sleep(2000);

	        boolean allValid = true;
	        List<Integer> outOfRangeHours = new ArrayList<>();

	        // Normalize selected ranges
	        List<int[]> parsedRanges = new ArrayList<>();
	        for (String range : selectedRanges) {
	            String normalized = range.replace("00", "24");
	            String[] parts = normalized.split("-");
	            int start = Integer.parseInt(parts[0].trim());
	            int end = Integer.parseInt(parts[1].trim());
	            parsedRanges.add(new int[]{start, end});
	        }

	        List<WebElement> departureTimes = driver.findElements(By.xpath("//span[contains(@class, 'flight-deptime')]"));

	        if (departureTimes.isEmpty()) {
	            String message = "❌ No departure time elements found in results.";
	            test.log(Status.FAIL, message);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, message, "Departure time elements not found.");
	            Assert.fail("Departure time not displayed in results.");
	        }

	        for (WebElement element : departureTimes) {
	            String text = element.getText().trim(); // e.g., "00:45"
	            String hourPart = text.split(":")[0].trim();

	            if (hourPart.equals("00")) hourPart = "24";
	            int departureHour = Integer.parseInt(hourPart);

	            boolean inAnyRange = false;
	            for (int[] range : parsedRanges) {
	                int start = range[0];
	                int end = range[1];

	                if ((start <= end && departureHour >= start && departureHour <= end) ||
	                    (start > end && (departureHour >= start || departureHour <= end))) {
	                    inAnyRange = true;
	                    break;
	                }
	            }

	            if (!inAnyRange) {
	                String failMsg = "❌ Departure " + departureHour + ":00 is outside selected range.";
	                test.log(Status.FAIL, failMsg);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, failMsg, "Departure time validation failed");
	                outOfRangeHours.add(departureHour);
	                allValid = false;
	            }
	        }

	        if (allValid) {
	            test.log(Status.INFO, "✅ All departure times are within the selected range.");
	        } else {
	            String summaryFailMsg = "❌ Some flights have departure times out of selected range: " + outOfRangeHours;
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, summaryFailMsg, "Multiple invalid departure times found.");
	            Assert.fail(summaryFailMsg);
	        }

	    } catch (Exception e) {
	        String errorMsg = "❌ Exception in ValidateDepartureTime(): " + e.getMessage();
	        test.log(Status.FAIL, errorMsg);
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, errorMsg, "Exception occurred during departure time validation.");
	        e.printStackTrace();
	        Assert.fail("Exception during departure time validation.");
	    }
	}

	//public void ValidateDepartureTimeRoundTrip(String clickedElementText) throws InterruptedException {
	//    boolean isInRange = false;
	//    int departureTime1 = 0;
	//
	//	Thread.sleep(2000);
	//    System.out.println("Clicked Range: " + clickedElementText);
	//
	//    if (clickedElementText.contains("00")) {
	//        clickedElementText = clickedElementText.replace("00", "24");
	//    }
	//
	//    String[] startSplit = clickedElementText.split("-");
	//    String Start = startSplit[0].trim();
	//    String end = startSplit[1].trim();
	//
	//    System.out.println("Start: " + Start);
	//    System.out.println("End: " + end);
	//
	//    int start1 = Integer.parseInt(Start);
	//    int end1 = Integer.parseInt(end);
	//
	//    List<WebElement> departureTimeList = driver.findElements(By.xpath("//div[@class='spacing-0 flex-1 flight-info_flight-details pb-2 row']//span[contains(@class,'flight-arrtime')]"));
	//
	//    for (WebElement departureTime : departureTimeList) {
	//        String departureTimeText = departureTime.getText(); // e.g., "00:45"
	//        String[] departureTimeTextSplit = departureTimeText.split(":");
	//        String departureTimeTextSplit1 = departureTimeTextSplit[0].trim();
	//
	//        // Convert "00" to "24" for consistency
	//        if (departureTimeTextSplit1.equals("00")) {
	//        	departureTimeTextSplit1 = "24";
	//        }
	//
	//        departureTime1 = Integer.parseInt(departureTimeTextSplit1);
	//        System.out.println("Checking departure hour: " + departureTime1);
	//
	// //     boolean isInRange;
	//        if (start1 <= end1) {
	//            // Non-wrapped range, e.g. 10 - 18
	//            isInRange = (departureTime1 >= start1 && departureTime1 <= end1);
	//        } else {
	//            // Wrapped range across midnight, e.g. 24 - 6
	//            isInRange = (departureTime1 >= start1 || departureTime1 <= end1);
	//        }
	//
	//        
	//    }
	//    if (isInRange) {
	//        System.out.println("✅ Departure time is in range.");
	//    } else {
	//        System.out.println("❌ Departure time is NOT in range.");
	//    }
	//}
//	public void ValidateDepartureTimeRoundTrip(List<String> selectedDepart) throws InterruptedException {
//		boolean allInRange = true; // Track overall validity
//		int departureTime1 = 0;
//
//		Thread.sleep(2000);
//		System.out.println("Clicked Range: " + selectedDepart);
//
//		if (selectedDepart.contains("00")) {
//			selectedDepart = selectedDepart.replace("00", "24");
//		}
//
//		String[] startSplit = selectedDepart.split("-");
//		String Start = startSplit[0].trim();
//		String end = startSplit[1].trim();
//
//		System.out.println("Start: " + Start);
//		System.out.println("End: " + end);
//
//		int start1 = Integer.parseInt(Start);
//		int end1 = Integer.parseInt(end);
//
//		List<WebElement> departureTimeList = driver.findElements(By.xpath("//div[@class='spacing-0 flex-1 flight-info_flight-details pb-2 row']//span[contains(@class,'flight-deptime')]"));
//
//		for (WebElement departureTime : departureTimeList) {
//			String departureTimeText = departureTime.getText(); // e.g., "00:45"
//			String[] departureTimeTextSplit = departureTimeText.split(":");
//			String departureTimeTextSplit1 = departureTimeTextSplit[0].trim();
//
//			// Convert "00" to "24" for consistency
//			if (departureTimeTextSplit1.equals("00")) {
//				departureTimeTextSplit1 = "24";
//			}
//
//			departureTime1 = Integer.parseInt(departureTimeTextSplit1);
//			System.out.println("Checking departure hour: " + departureTime1);
//
//			boolean isInRange;
//			if (start1 <= end1) {
//				// Normal range
//				isInRange = (departureTime1 >= start1 && departureTime1 <= end1);
//			} else {
//				// Wrapped range
//				isInRange = (departureTime1 >= start1 || departureTime1 <= end1);
//			}
//
//			if (!isInRange) {
//				allInRange = false; // As soon as one is out of range
//				System.out.println("❌ Departure hour out of range: " + departureTime1);
//			}
//		}
//
//		if (allInRange) {
//			System.out.println("✅ All departure times are in range.");
//		} else {
//			System.out.println("❌ Some departure times are NOT in range.");
//		}
//	}
//
//	public void ValidateArrivalTimeRoundTrip(String clickedElementText) throws InterruptedException {
//		boolean isInRange = false;
//		int arrivalTime1 = 0;
//
//		Thread.sleep(2000);
//		System.out.println("Clicked Range: " + clickedElementText);
//
//		if (clickedElementText.contains("00")) {
//			clickedElementText = clickedElementText.replace("00", "24");
//		}
//
//		String[] startSplit = clickedElementText.split("-");
//		String Start = startSplit[0].trim();
//		String end = startSplit[1].trim();
//
//		System.out.println("Start: " + Start);
//		System.out.println("End: " + end);
//
//		int start1 = Integer.parseInt(Start);
//		int end1 = Integer.parseInt(end);
//
//		List<WebElement> arrivalTimeList = driver.findElements(By.xpath("//div[@class='spacing-0 flex-1 flight-info_flight-details pb-2 row']//span[contains(@class,'flight-arrtime')]"));
//
//		for (WebElement arrivalTime : arrivalTimeList) {
//			String arrivalTimeText = arrivalTime.getText(); // e.g., "00:45"
//			String[] arrivalTimeTextSplit = arrivalTimeText.split(":");
//			String arrivalTimeTextSplit1 = arrivalTimeTextSplit[0].trim();
//
//			// Convert "00" to "24" for consistency
//			if (arrivalTimeTextSplit1.equals("00")) {
//				arrivalTimeTextSplit1 = "24";
//			}
//
//			arrivalTime1 = Integer.parseInt(arrivalTimeTextSplit1);
//			System.out.println("Checking arrival hour: " + arrivalTime1);
//
//			//     boolean isInRange;
//			if (start1 <= end1) {
//				// Non-wrapped range, e.g. 10 - 18
//				isInRange = (arrivalTime1 >= start1 && arrivalTime1 <= end1);
//			} else {
//				// Wrapped range across midnight, e.g. 24 - 6
//				isInRange = (arrivalTime1 >= start1 || arrivalTime1 <= end1);
//			}
//
//
//		}
//		if (isInRange) {
//			System.out.println("✅ Arrival time is in range.");
//		} else {
//			System.out.println("❌ Arrival time is NOT in range.");
//		}
//	}
	public void ValidateArrivalTimeRoundTrip(List<String> selectedRanges, ExtentTest test) throws InterruptedException {
	    try {
	        ((JavascriptExecutor) driver).executeScript("window.scrollTo({ top: 0, behavior: 'smooth' });");
	        Thread.sleep(2000);

	        boolean allValid = true;
	        List<Integer> outOfRangeHours = new ArrayList<>();

	        // Normalize and parse selected ranges
	        List<int[]> parsedRanges = new ArrayList<>();
	        for (String range : selectedRanges) {
	            String normalized = range.replace("00", "24");
	            String[] parts = normalized.split("-");
	            int start = Integer.parseInt(parts[0].trim());
	            int end = Integer.parseInt(parts[1].trim());
	            parsedRanges.add(new int[]{start, end});
	        }

	        // Find arrival time elements
	        List<WebElement> arrivalTimes = driver.findElements(By.xpath("//div[@class='spacing-0 flex-1 flight-info_flight-details pb-2 row']//span[contains(@class,'flight-arrtime')]"));

	        if (arrivalTimes.isEmpty()) {
	            String message = "❌ No arrival time elements found in round-trip results.";
	            test.log(Status.FAIL, message);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, message, "Arrival time elements not found.");
	            Assert.fail("Arrival time not displayed in round-trip results.");
	        }

	        // Validate each arrival time against the parsed ranges
	        for (WebElement element : arrivalTimes) {
	            String text = element.getText().trim(); // e.g., "00:45"
	            String hourPart = text.split(":")[0].trim();

	            if (hourPart.equals("00")) hourPart = "24";

	            int arrivalHour = Integer.parseInt(hourPart);
	            System.out.println("Checking arrival hour: " + arrivalHour);

	            boolean inAnyRange = false;
	            for (int[] range : parsedRanges) {
	                int start = range[0];
	                int end = range[1];

	                if ((start <= end && arrivalHour >= start && arrivalHour <= end) ||
	                    (start > end && (arrivalHour >= start || arrivalHour <= end))) {
	                    inAnyRange = true;
	                    break;
	                }
	            }

	            if (!inAnyRange) {
	                String failMsg = "❌ Arrival " + arrivalHour + ":00 is outside selected range.";
	                test.log(Status.FAIL, failMsg);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, failMsg, "Arrival time validation failed.");
	                outOfRangeHours.add(arrivalHour);
	                allValid = false;
	            }
	        }

	        if (allValid) {
	            test.log(Status.INFO, "✅ All round-trip arrival times are within the selected range.");
	        } else {
	            String summaryFailMsg = "❌ Some round-trip flights have arrival times out of selected range: " + outOfRangeHours;
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, summaryFailMsg, "Multiple invalid round-trip arrival times found.");
	            Assert.fail(summaryFailMsg);
	        }

	    } catch (Exception e) {
	        String errorMsg = "❌ Exception in ValidateArrivalTimeRoundTrip(): " + e.getMessage();
	        test.log(Status.FAIL, errorMsg);
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, errorMsg, "Exception occurred during round-trip arrival time validation.");
	        e.printStackTrace();
	        Assert.fail("Exception during round-trip arrival time validation.");
	    }
	}

	public void ValidateDepartureTimeRoundTrip(List<String> selectedRanges, ExtentTest test) throws InterruptedException {
	    try {
	        ((JavascriptExecutor) driver).executeScript("window.scrollTo({ top: 0, behavior: 'smooth' });");
	        Thread.sleep(2000);

	        boolean allValid = true;
	        List<Integer> outOfRangeHours = new ArrayList<>();

	        // Normalize and parse selected ranges
	        List<int[]> parsedRanges = new ArrayList<>();
	        for (String range : selectedRanges) {
	            String normalized = range.replace("00", "24");
	            String[] parts = normalized.split("-");
	            int start = Integer.parseInt(parts[0].trim());
	            int end = Integer.parseInt(parts[1].trim());
	            parsedRanges.add(new int[]{start, end});
	        }

	        List<WebElement> departureTimes = driver.findElements(By.xpath("//div[@class='spacing-0 flex-1 flight-info_flight-details pb-2 row']//span[contains(@class,'flight-deptime')]"));

	        if (departureTimes.isEmpty()) {
	            String message = "❌ No departure time elements found in round-trip results.";
	            test.log(Status.FAIL, message);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, message, "Departure time elements not found.");
	            Assert.fail("Departure time not displayed in round-trip results.");
	        }

	        for (WebElement element : departureTimes) {
	            String text = element.getText().trim(); // e.g., "00:45"
	            String hourPart = text.split(":")[0].trim();

	            if (hourPart.equals("00")) hourPart = "24";

	            int departureHour = Integer.parseInt(hourPart);
	            System.out.println("Checking departure hour: " + departureHour);

	            boolean inAnyRange = false;
	            for (int[] range : parsedRanges) {
	                int start = range[0];
	                int end = range[1];

	                // Check if departureHour is within this range
	                if ((start <= end && departureHour >= start && departureHour <= end) ||
	                    (start > end && (departureHour >= start || departureHour <= end))) {
	                    inAnyRange = true;
	                    break;
	                }
	            }

	            if (!inAnyRange) {
	                String failMsg = "❌ Departure " + departureHour + ":00 is outside selected range.";
	                test.log(Status.FAIL, failMsg);
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, failMsg, "Departure time validation failed.");
	                outOfRangeHours.add(departureHour);
	                allValid = false;
	            }
	        }

	        if (allValid) {
	            test.log(Status.INFO, "✅ All round-trip departure times are within the selected range.");
	        } else {
	            String summaryFailMsg = "❌ Some round-trip flights have departure times out of selected range: " + outOfRangeHours;
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, summaryFailMsg, "Multiple invalid round-trip departure times found.");
	            Assert.fail(summaryFailMsg);
	        }

	    } catch (Exception e) {
	        String errorMsg = "❌ Exception in ValidateDepartureTimeRoundTrip(): " + e.getMessage();
	        test.log(Status.FAIL, errorMsg);
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, errorMsg, "Exception occurred during round-trip departure time validation.");
	        e.printStackTrace();
	        Assert.fail("Exception during round-trip departure time validation.");
	    }
	}

	//public void searchByFlightNumber()
	//{
	//	
	//		try
	//		{
	//	}
	//WebElement flightNumber = driver.findElement(By.xpath("(//div[@class='d-flex w-100 flex-sm-row flex-column'])[1]//span[@class='fs-10 flight-number']"));
	//String flightNumberText = flightNumber.getText();
	// String[] flightNo = flightNumberText.split(",");
	// String flightNoText = flightNo[0];
	// System.out.println(flightNoText);
	//}
	//catch(Exception e)
	//{
	//	
	//}
	// 
	//}
//	public String searchByFlightNumber() {
//		try {
//			// Locate the flight number element
//			WebElement flightNumber = driver.findElement(By.xpath(
//					"(//div[@class='d-flex w-100 flex-sm-row flex-column'])[1]//span[@class='fs-10 flight-number']"
//					));
//
//			// Get the text of the element
//			String flightNumberText = flightNumber.getText(); // e.g., "AI123, AI124"
//			System.out.println("Raw flight number text: " + flightNumberText);
//
//			// Split by comma and get the first flight number
//			String[] flightNo = flightNumberText.split(",");
//			String flightNoText = flightNo[0].trim(); // trim to remove any spaces
//
//			// Print the first flight number
//			System.out.println("First flight number: " + flightNoText);
//			return flightNoText;
//
//		} catch (Exception e) {
//			//        System.out.println("❌ Error while extracting flight number: " + e.getMessage());
//			//        e.printStackTrace();
//			WebElement flightNumber = driver.findElement(By.xpath(
//					"(//div[@class='d-flex w-100 flex-sm-row flex-column'])[1]//span[@class='fs-10 flight-number']"
//					));
//			String flightNoText = flightNumber.getText().trim();
//			System.out.println(flightNoText);
//			return flightNoText;
//		}
//
//	}
	public String searchByFlightNumber(ExtentTest test) {
	    try {
	        WebElement flightNumber = driver.findElement(By.xpath(
	            "(//div[@class='d-flex w-100 flex-sm-row flex-column'])[1]//span[@class='fs-10 flight-number']"
	        ));

	        String flightNumberText = flightNumber.getText(); // e.g., "AI123, AI124"
	        System.out.println("Raw flight number text: " + flightNumberText);

	        String[] flightNo = flightNumberText.split(",");
	        String flightNoText = flightNo[0].trim();

	        System.out.println("First flight number: " + flightNoText);
	        test.log(Status.PASS, "✅ Flight number fetched successfully: " + flightNoText);
	        return flightNoText;

	    } catch (Exception e) {
	        test.log(Status.WARNING, "⚠️ Primary locator failed. Retrying fallback...");

	        try {
	            WebElement flightNumber = driver.findElement(By.xpath(
	                "(//div[@class='d-flex w-100 flex-sm-row flex-column'])[1]//span[@class='fs-10 flight-number']"
	            ));
	            String flightNoText = flightNumber.getText().trim();
	            System.out.println(flightNoText);
	            test.log(Status.PASS, "✅ Flight number fetched using fallback: " + flightNoText);
	            return flightNoText;

	        } catch (Exception ex) {
	            test.log(Status.FAIL, "❌ Failed to fetch flight number: " + ex.getMessage());
	            ex.printStackTrace();
	            Assert.fail("Exception while fetching flight number: " + ex.getMessage());
	        }
	    }

	    return null;
	}
	public String searchByFlightAirCraftType(ExtentTest test) {
	    try {
	        WebElement flightAirCraftType = driver.findElement(By.xpath(
	            "(//div[@class='d-flex w-100 flex-sm-row flex-column'])[1]//span[contains(@class,'flight-equipment')]"
	        ));

	        String flightAirCraftTypeText = flightAirCraftType.getText(); // e.g., "AI123, AI124"
	        System.out.println("Raw flight AircraftType text: " + flightAirCraftTypeText);

	        String[] flightAirCraftType1 = flightAirCraftTypeText.split(":");
	        String flightAirCraftTypeText1 = flightAirCraftType1[1].trim();

	        System.out.println("First flight AircraftType: " + flightAirCraftTypeText1);
	        test.log(Status.PASS, "✅ Flight AircraftType fetched successfully: " + flightAirCraftTypeText1);
	        return flightAirCraftTypeText1;

	    } catch (Exception e) {
	        test.log(Status.WARNING, "⚠️ Primary locator failed. Retrying fallback...");

	        try {
	            WebElement flightAircraftType = driver.findElement(By.xpath(
	                "(//div[@class='spacing-0 flex-1 flight-info_flight-details pb-2 row'])[1]//span[contains(@class,'flight-number')]"
	            ));
	            String flightAircraftTypeText = flightAircraftType.getText().trim();
	            System.out.println(flightAircraftTypeText);
	            test.log(Status.PASS, "✅ Flight AircraftType fetched using fallback: " + flightAircraftTypeText);
	            return flightAircraftTypeText;

	        } catch (Exception ex) {
	            test.log(Status.FAIL, "❌ Failed to fetch flight AircraftType: " + ex.getMessage());
	            ex.printStackTrace();
	            Assert.fail("Exception while fetching flight AircraftType: " + ex.getMessage());
	        }
	    }

	    return null;
	}
//	public String searchByFlightNumberRoundTrip() {
//		try {
//			// Locate the flight number element
//			WebElement flightNumber = driver.findElement(By.xpath(
//					"(//div[@class='spacing-0 flex-1 flight-info_flight-details pb-2 row'])[1]//span[contains(@class,'flight-number')]"
//					));
//
//			// Get the text of the element
//			String flightNumberText = flightNumber.getText(); // e.g., "AI123, AI124"
//			System.out.println("Raw flight number text: " + flightNumberText);
//
//			// Split by comma and get the first flight number
//			String[] flightNo = flightNumberText.split(",");
//			String flightNoText = flightNo[0].trim(); // trim to remove any spaces
//
//			// Print the first flight number
//			System.out.println("First flight number: " + flightNoText);
//			return flightNoText;
//
//		} catch (Exception e) {
//			//        System.out.println("❌ Error while extracting flight number: " + e.getMessage());
//			//        e.printStackTrace();
//			WebElement flightNumber = driver.findElement(By.xpath(
//					"(//div[@class='d-flex w-100 flex-sm-row flex-column'])[1]//span[@class='fs-10 flight-number']"
//					));
//			String flightNoText = flightNumber.getText().trim();
//			System.out.println(flightNoText);
//			return flightNoText;
//		}
//
//	}
	public String searchByFlightNumberRoundTrip(ExtentTest test) {
	    try {
	        // Attempt primary locator
	        WebElement flightNumber = driver.findElement(By.xpath(
	            "(//div[@class='spacing-0 flex-1 flight-info_flight-details pb-2 row'])[1]//span[contains(@class,'flight-number')]"
	        ));

	        String flightNumberText = flightNumber.getText(); // e.g., "AI123, AI124"
	        System.out.println("Raw flight number text: " + flightNumberText);

	        String[] flightNo = flightNumberText.split(",");
	        String flightNoText = flightNo[0].trim(); // First flight number

	        System.out.println("First flight number: " + flightNoText);
	        test.log(Status.PASS, "✅ Round-trip flight number fetched successfully: " + flightNoText);
	        return flightNoText;

	    } catch (Exception e) {
	        test.log(Status.WARNING, "⚠️ Primary locator failed for round-trip flight number. Retrying fallback...");

	        try {
	            // Fallback locator
	            WebElement flightNumber = driver.findElement(By.xpath(
	                "(//div[@class='d-flex w-100 flex-sm-row flex-column'])[1]//span[@class='fs-10 flight-number']"
	            ));

	            String flightNoText = flightNumber.getText().trim();
	            System.out.println("Fallback flight number: " + flightNoText);
	            test.log(Status.PASS, "✅ Fallback round-trip flight number fetched successfully: " + flightNoText);
	            return flightNoText;

	        } catch (Exception ex) {
	            String errorMsg = "❌ Failed to fetch round-trip flight number: " + ex.getMessage();
	            test.log(Status.FAIL, errorMsg);
	            ex.printStackTrace();
	            Assert.fail(errorMsg);
	        }
	    }

	    return null;
	}

	//public String searchByFlightEquipmentRoundTrip() {
	//    try {
	//    	
	//        // Locate the flight number element
	//        WebElement flightEquipment = driver.findElement(By.xpath(
	//            "(//span[contains(@class,'flight-equipment')])[1]"
	//        ));
	//
	//        // Get the text of the element
	//        String flightEquipmentText = flightEquipment.getText(); // e.g., "AI123, AI124"
	//        System.out.println("Raw flight number text: " + flightEquipmentText);
	//        String[] flightEquipmentTxt1 = flightEquipmentText.split(":");
	//        String flightEquipmentTxt2 = flightEquipmentTxt1[1].trim();
	//        System.out.println(flightEquipmentTxt2);
	//        // Split by comma and get the first flight number
	//        String[] flightEq = flightEquipmentTxt2.split(",");
	//        String flightEqText = flightEq[0].trim(); // trim to remove any spaces
	//
	//        // Print the first flight number
	//        System.out.println("First flight number: " +flightEqText);
	//        return flightEqText;
	//
	//    } catch (Exception e) {
	////        System.out.println("❌ Error while extracting flight number: " + e.getMessage());
	////        e.printStackTrace();
	//    	 WebElement flightEquipment = driver.findElement(By.xpath(
	//    	            "(//span[contains(@class,'flight-equipment')])[1]"
	//    	        ));
	//    	 String flightEqText = flightEquipment.getText().trim();
	//    	 System.out.println(flightEqText);
	//    	 return flightEqText;
	//    }
	//	
	//}
//	public String searchByFlightEquipmentRoundTrip() {
//		try {
//			WebElement flightEquipment = driver.findElement(By.xpath("(//span[contains(@class,'flight-equipment')])[1]"));
//			String flightEquipmentText = flightEquipment.getText(); // e.g., "Flight Equipment: AI123, AI124"
//			System.out.println("Raw flight number text: " + flightEquipmentText);
//
//			if (!flightEquipmentText.contains(":")) {
//				System.out.println("❌ Flight equipment text format unexpected.");
//				return null;
//			}
//
//			String[] flightEquipmentTxt1 = flightEquipmentText.split(":");
//			String flightEquipmentTxt2 = flightEquipmentTxt1[1].trim();
//			String[] flightEq = flightEquipmentTxt2.split(",");
//			String flightEqText = flightEq[0].trim();
//
//			System.out.println("First flight number: " + flightEqText);
//			return flightEqText;
//
//		} catch (Exception e) {
//			System.out.println("❌ Error while extracting flight number: " + e.getMessage());
//			e.printStackTrace();
//			return null;
//		}
//	}
	public String searchByFlightEquipmentRoundTrip(ExtentTest test) {
	    try {
	        // Locate the flight equipment element
	        WebElement flightEquipment = driver.findElement(By.xpath("(//span[contains(@class,'flight-equipment')])[1]"));
	        String flightEquipmentText = flightEquipment.getText(); // e.g., "Flight Equipment: AI123, AI124"
	        System.out.println("Raw flight equipment text: " + flightEquipmentText);

	        // Validate format
	        if (!flightEquipmentText.contains(":")) {
	            String errorMsg = "❌ Flight equipment text format unexpected: " + flightEquipmentText;
	            System.out.println(errorMsg);
	            test.log(Status.FAIL, errorMsg);
	            return null;
	        }

	        // Parse flight equipment
	        String[] flightEquipmentTxt1 = flightEquipmentText.split(":");
	        String flightEquipmentTxt2 = flightEquipmentTxt1[1].trim();
	        String[] flightEq = flightEquipmentTxt2.split(",");
	        String flightEqText = flightEq[0].trim();

	        System.out.println("First flight equipment: " + flightEqText);
	        test.log(Status.PASS, "✅ Flight equipment fetched successfully: " + flightEqText);
	        return flightEqText;

	    } catch (Exception e) {
	        String errorMsg = "❌ Exception while extracting flight equipment: " + e.getMessage();
	        System.out.println(errorMsg);
	        e.printStackTrace();
	        test.log(Status.FAIL, errorMsg);
	        Assert.fail(errorMsg);
	        return null;
	    }
	}

//	public void flightNumberSearchField(String flightNoText)
//	{
//		System.out.println(flightNoText);
//		driver.findElement(By.xpath("//input[@placeholder='Eg: QP-571']")).sendKeys(flightNoText);
//
//		// Locate the flight number element
//		WebElement flightNumberAfterSearch = driver.findElement(By.xpath(
//				"(//div[@class='spacing-0 flex-1 flight-info_flight-details pb-2 row'])[1]//span[contains(@class,'flight-number')]"
//				));
//
//		// Get the text of the element
//		String flightNumberTextAfterSearch = flightNumberAfterSearch.getText(); // e.g., "AI123, AI124"
//		System.out.println("Raw flight number text: " + flightNumberTextAfterSearch);
//
//		// Split by comma and get the first flight number
//		String[] flightNoAfterSearch = flightNumberTextAfterSearch.split(",");
//		String flightNoTextAfterSearch = flightNoAfterSearch[0].trim(); // trim to remove any spaces
//
//		// Print the first flight number
//		System.out.println("First flight number: " + flightNoTextAfterSearch);
//
//		if(flightNoText.equals(flightNoTextAfterSearch))
//		{
//			System.out.println("FlightNumber search filed validation success");
//		}
//
//	}
//	public void flightNumberSearchField(String flightNoText, ExtentTest test) {
//	    try {
//	        System.out.println(flightNoText);
//	        driver.findElement(By.xpath("//input[@placeholder='Eg: QP-571']")).sendKeys(flightNoText);
//
//	        WebElement flightNumberAfterSearch = driver.findElement(By.xpath(
//	            "(//div[contains(@class,'flight-info_flight-details')])[1]//span[contains(@class,'flight-number')]"
//	        ));
//
//	        String flightNumberTextAfterSearch = flightNumberAfterSearch.getText(); // e.g., "AI123, AI124"
//	        System.out.println("Raw flight number text: " + flightNumberTextAfterSearch);
//
//	        String[] flightNoAfterSearch = flightNumberTextAfterSearch.split(",");
//	        String flightNoTextAfterSearch = flightNoAfterSearch[0].trim();
//
//	        System.out.println("First flight number: " + flightNoTextAfterSearch);
//
//	        if (flightNoText.equals(flightNoTextAfterSearch)) {
//	            System.out.println("FlightNumber search field validation success");
//	            test.log(Status.PASS, "✅ Flight number matched after search: " + flightNoTextAfterSearch);
//	        } else {
//	            System.out.println("❌ Flight number mismatch after search");
//	            test.log(Status.FAIL, "❌ Flight number mismatch. Expected: " + flightNoText + ", Found: " + flightNoTextAfterSearch);
//	            Assert.fail("Flight number validation failed.");
//	        }
//
//	    } catch (Exception e) {
//	        test.log(Status.FAIL, "❌ Exception during flight number search validation: " + e.getMessage());
//	        e.printStackTrace();
//	        Assert.fail("Exception during flight number search validation: " + e.getMessage());
//	    }
//	}
	public void flightNumberSearchField(String flightNoText, ExtentTest test) {
	    try {
	        System.out.println(flightNoText);
	        driver.findElement(By.xpath("//input[@placeholder='Eg: QP-571']")).sendKeys(flightNoText);

	        WebElement flightNumberAfterSearch = driver.findElement(By.xpath(
	            "(//div[contains(@class,'flight-info_flight-details')])[1]//span[contains(@class,'flight-number')]"
	        ));

	        String flightNumberTextAfterSearch = flightNumberAfterSearch.getText(); // e.g., "AI123, AI124"
	        System.out.println("Raw flight number text: " + flightNumberTextAfterSearch);

	        String[] flightNoAfterSearch = flightNumberTextAfterSearch.split(",");
	        String flightNoTextAfterSearch = flightNoAfterSearch[0].trim();

	        System.out.println("First flight number: " + flightNoTextAfterSearch);

	        if (flightNoText.equals(flightNoTextAfterSearch)) {
	            System.out.println("FlightNumber search field validation success");
	            test.log(Status.PASS, "✅ Flight number matched after search: " + flightNoTextAfterSearch);
	        } else {
	            System.out.println("❌ Flight number mismatch after search");
	            test.log(Status.FAIL, "❌ Flight number mismatch. Expected: " + flightNoText + ", Found: " + flightNoTextAfterSearch);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight number mismatch after search", 
	                                                      "Expected: " + flightNoText + ", Found: " + flightNoTextAfterSearch);
	            Assert.fail("Flight number validation failed.");
	        }

	    } catch (Exception e) {
	        String errorMsg = "❌ Exception during flight number search validation: " + e.getMessage();
	        test.log(Status.FAIL, errorMsg);
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, errorMsg, "Exception occurred");
	        e.printStackTrace();
	        Assert.fail(errorMsg);
	    }
	}

//	public void aircraftNameSearchField(String aircraftNameText, ExtentTest test) {
//	    try {
//	        System.out.println(aircraftNameText);
//	        driver.findElement(By.xpath("//input[@placeholder='Eg: 380']")).sendKeys(aircraftNameText);
//
//	        WebElement aircraftNameAfterSearch = driver.findElement(By.xpath(
//	            "(//div[@class='d-flex w-100 flex-sm-row flex-column'])[1]//span[contains(@class,'flight-equipment')]"
//	        ));
//
//	        String aircraftNameTextAfterSearch = aircraftNameAfterSearch.getText(); // e.g., "AI123, AI124"
//	        System.out.println("Raw aircraft name text: " + aircraftNameTextAfterSearch);
//
//	        String[] aircraftNameSplit = aircraftNameTextAfterSearch.split(":");
//	        String firstAircraftName = aircraftNameSplit[1].trim();
//
//	        System.out.println("First aircraft name: " + firstAircraftName);
//
//	        if (aircraftNameText.equals(firstAircraftName)) {
//	            System.out.println("AircraftName search field validation success");
//	            test.log(Status.PASS, "✅ Aircraft name matched after search: " + firstAircraftName);
//	        } else {
//	            System.out.println("❌ Aircraft name mismatch after search");
//	            test.log(Status.FAIL, "❌ Aircraft name mismatch. Expected: " + aircraftNameText + ", Found: " + firstAircraftName);
//	            Assert.fail("Aircraft name validation failed.");
//	        }
//
//	    } catch (Exception e) {
//	        test.log(Status.FAIL, "❌ Exception during aircraft name search validation: " + e.getMessage());
//	        e.printStackTrace();
//	        Assert.fail("Exception during aircraft name search validation: " + e.getMessage());
//	    }
//	}
	public void aircraftNameSearchField(String aircraftNameText, ExtentTest test) {
	    try {
	        System.out.println(aircraftNameText);
	        driver.findElement(By.xpath("//input[@placeholder='Eg: 380']")).sendKeys(aircraftNameText);

	        WebElement aircraftNameAfterSearch = driver.findElement(By.xpath(
	            "(//div[@class='d-flex w-100 flex-sm-row flex-column'])[1]//span[contains(@class,'flight-equipment')]"
	        ));

	        String aircraftNameTextAfterSearch = aircraftNameAfterSearch.getText(); // e.g., "Equipment: 380"
	        System.out.println("Raw aircraft name text: " + aircraftNameTextAfterSearch);

	        String[] aircraftNameSplit = aircraftNameTextAfterSearch.split(":");
	        String firstAircraftName = aircraftNameSplit[1].trim();

	        System.out.println("First aircraft name: " + firstAircraftName);

	        if (aircraftNameText.equals(firstAircraftName)) {
	            System.out.println("AircraftName search field validation success");
	            test.log(Status.PASS, "✅ Aircraft name matched after search: " + firstAircraftName);
	        } else {
	            System.out.println("❌ Aircraft name mismatch after search");
	            test.log(Status.FAIL, "❌ Aircraft name mismatch. Expected: " + aircraftNameText + ", Found: " + firstAircraftName);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, 
	                "Aircraft name mismatch after search", 
	                "Expected: " + aircraftNameText + ", Found: " + firstAircraftName);
	            Assert.fail("Aircraft name validation failed.");
	        }

	    } catch (Exception e) {
	        String errorMsg = "❌ Exception during aircraft name search validation: " + e.getMessage();
	        test.log(Status.FAIL, errorMsg);
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, errorMsg, "Exception occurred");
	        e.printStackTrace();
	        Assert.fail(errorMsg);
	    }
	}


	//public void flightEquipmentSearchField(String flightEqText)
	//{
	//	System.out.println(flightEqText);
	//	driver.findElement(By.xpath("//input[@placeholder='Eg: 380']")).sendKeys(flightEqText);
	//	
	//	 // Locate the flight number element
	//    WebElement flightEquipmentAfterSearch = driver.findElement(By.xpath(
	//        "(//span[contains(@class,'flight-equipment')])[1]"
	//    ));
	//    // Get the text of the element
	//    String flightEquipmentTextAfterSearch = flightEquipmentAfterSearch.getText(); // e.g., "AI123, AI124"
	//    System.out.println("Raw flight number text: " + flightEquipmentTextAfterSearch);
	//    
	//    String[] flightEquipmentTextAfterSearch1 = flightEquipmentTextAfterSearch.split(":");
	//    String flightEquipmentTextAfterSearch2 = flightEquipmentTextAfterSearch1[1];
	//    // Split by comma and get the first flight number
	//    String[] flightEqAfterSearch = flightEquipmentTextAfterSearch2.split(",");
	//    String flightEqTextAfterSearch = flightEqAfterSearch[0].trim(); // trim to remove any spaces
	//
	//    // Print the first flight number
	//    System.out.println("First flight number: " + flightEqTextAfterSearch);
	//    
	//    if(flightEqText.equals(flightEqTextAfterSearch))
	//    {
	//    	System.out.println("FlightEquipment search filed validation success");
	//    }
	//    
	//}
//	public void flightEquipmentSearchField(String flightEqText, ExtentTest test) throws InterruptedException {
//		if (flightEqText == null || flightEqText.isEmpty()) {
//			System.out.println("❌ Invalid flight number provided for search.");
//			return;
//		}
//
//		System.out.println("Searching for flight: " + flightEqText);
//		WebElement inputField = driver.findElement(By.xpath("//input[@placeholder='Eg: 380']"));
//		inputField.clear();
//		inputField.sendKeys(flightEqText);
//		Thread.sleep(2000); // Wait for results to load (replace with WebDriverWait in real test)
//
//		WebElement flightEquipmentAfterSearch = driver.findElement(By.xpath("(//span[contains(@class,'flight-equipment')])[1]"));
//		String flightEquipmentTextAfterSearch = flightEquipmentAfterSearch.getText(); // e.g., "Flight Equipment: AI123, AI124"
//		System.out.println("Flight result text after search: " + flightEquipmentTextAfterSearch);
//
//		if (!flightEquipmentTextAfterSearch.contains(":")) {
//			System.out.println("❌ Unexpected format in search result.");
//			return;
//		}
//
//		String[] flightEquipmentTextAfterSearch1 = flightEquipmentTextAfterSearch.split(":");
//		String flightEquipmentTextAfterSearch2 = flightEquipmentTextAfterSearch1[1];
//		String[] flightEqAfterSearch = flightEquipmentTextAfterSearch2.split(",");
//		String flightEqTextAfterSearch = flightEqAfterSearch[0].trim();
//
//		System.out.println("First flight number after search: " + flightEqTextAfterSearch);
//
//		if (flightEqText.equals(flightEqTextAfterSearch)) {
//			System.out.println("✅ FlightEquipment search field validation success");
//		} else {
//			System.out.println("❌ FlightEquipment search field validation FAILED");
//			System.out.println("Expected: " + flightEqText + " | Found: " + flightEqTextAfterSearch);
//		}
//	}
	public void flightEquipmentSearchField(String flightEqText, ExtentTest test) throws InterruptedException {
	    if (flightEqText == null || flightEqText.isEmpty()) {
	        String msg = "❌ Invalid flight equipment provided for search.";
	        System.out.println(msg);
	        test.log(Status.FAIL, msg);
	        Assert.fail("Flight equipment input is null or empty.");
	        return;
	    }

	    try {
	        System.out.println("Searching for flight equipment: " + flightEqText);
	        WebElement inputField = driver.findElement(By.xpath("//input[@placeholder='Eg: 380']"));
	        inputField.clear();
	        inputField.sendKeys(flightEqText);
	        Thread.sleep(2000); // Ideally use WebDriverWait

	        WebElement flightEquipmentAfterSearch = driver.findElement(By.xpath("(//span[contains(@class,'flight-equipment')])[1]"));
	        String flightEquipmentTextAfterSearch = flightEquipmentAfterSearch.getText(); // e.g., "Flight Equipment: AI123, AI124"
	        System.out.println("Flight equipment text after search: " + flightEquipmentTextAfterSearch);

	        if (!flightEquipmentTextAfterSearch.contains(":")) {
	            String msg = "❌ Unexpected format in search result: " + flightEquipmentTextAfterSearch;
	            System.out.println(msg);
	            test.log(Status.FAIL, msg);
	            Assert.fail(msg);
	            return;
	        }

	        String[] parts = flightEquipmentTextAfterSearch.split(":");
	        String flightList = parts[1];
	        String[] flightEqAfterSearch = flightList.split(",");
	        String flightEqTextAfterSearch = flightEqAfterSearch[0].trim();

	        System.out.println("First flight equipment after search: " + flightEqTextAfterSearch);

	        if (flightEqText.equals(flightEqTextAfterSearch)) {
	            String passMsg = "✅ Flight equipment matched after search: " + flightEqTextAfterSearch;
	            System.out.println(passMsg);
	            test.log(Status.PASS, passMsg);
	        } else {
	            String failMsg = "❌ Flight equipment mismatch. Expected: " + flightEqText + " | Found: " + flightEqTextAfterSearch;
	            System.out.println(failMsg);
	            test.log(Status.FAIL, failMsg);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, 
	                "Flight equipment mismatch after search", failMsg);
	            Assert.fail("Flight equipment validation failed.");
	        }

	    } catch (Exception e) {
	        String errorMsg = "❌ Exception during flight equipment search validation: " + e.getMessage();
	        System.out.println(errorMsg);
	        test.log(Status.FAIL, errorMsg);
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, errorMsg, "Exception occurred");
	        e.printStackTrace();
	        Assert.fail(errorMsg);
	    }
	}

	public void cheapestFlightFilter()
	{
		ArrayList<String> Price = new ArrayList<>();
		List<WebElement> flightPrice = driver.findElements(By.xpath("//span[contains(@class,'flight-totalfq')]"));
		for(WebElement flightPrice1:flightPrice)
		{
			String flightPriceText = flightPrice1.getText();	
			String flightPriceTrimmedText = flightPriceText.replace(",", "").replace(".00", "").replace("AED ", "");
			//System.out.println(flightPriceTrimmedText);
			Price.add(flightPriceTrimmedText);
			System.out.println(Price);
		}
	}
	//public void airLineFilterValidationRoundTrip(String airlineReturned)
	//{
	//	System.out.println(airlineReturned);
	//	String[] airlineReturnedSplit = airlineReturned.split("\\(");
	//	String airlineReturnedSplit2 = airlineReturnedSplit[0].trim();
	//	System.out.println(airlineReturnedSplit2);
	//	List<WebElement> airlineFound = driver.findElements(By.xpath("(//section[@class=' d-flex my-2 one-way-new-result-card '])[1]//span[@class='flight-company']"));
	//	for(WebElement airlineFound1 :airlineFound)
	//	{
	//		String airlineFound1Gettext = airlineFound1.getText(); 
	//    if(airlineFound1Gettext.equalsIgnoreCase(airlineReturnedSplit2))
	//    {
	//    	System.out.println("Airline filter successfully validated based on flight index");
	//    }
	//	}
	//}
	public void stopsFilterValidationRoundTrip(String selectStopFilterReturn)
	{
		System.out.println(selectStopFilterReturn);
		String[] airlineReturnedSplit = selectStopFilterReturn.split("at");
		String airlineReturnedSplit2 = airlineReturnedSplit[0].trim();
		System.out.println(airlineReturnedSplit2);
		List<WebElement> stopFound = driver.findElements(By.xpath("(//*[contains(@class,' one-way-new-result-card')])[1]//p[@class='stop-seperator']/following-sibling::span"));
		System.out.println(stopFound.size());
		for(WebElement stopFound1 :stopFound)
		{
			String stopFound1GetText = stopFound1.getText(); 
			System.out.println(stopFound1GetText);
			System.out.println(airlineReturnedSplit2);

			if(stopFound1GetText.equalsIgnoreCase(airlineReturnedSplit2))
			{
				System.out.println("Airline filter successfully validated based on flight index");
				break;
			}
		}
	}
//	public void airLineFilterValidationRoundTrip(List<String> selectAirlineReturn) throws InterruptedException
//	{
//		System.out.println(selectAirlineReturn);
//		String[] airlineReturnedSplit = selectAirlineReturn.split("\\(");
//		String airlineReturnedSplit2 = airlineReturnedSplit[0].trim();
//		System.out.println(airlineReturnedSplit2);
//		driver.findElement(By.xpath("(//a[text()='Flight Details'])[1]")).click();
//		Thread.sleep(2000);
//		List<WebElement> airlineFound = driver.findElements(By.xpath("(//div[@class='p-0 p-lg-3'])[1]//span[@class='primary-color flight-company']"));
//		System.out.println(airlineFound.size());
//
//
//		for(WebElement airlineFound1 :airlineFound)
//		{
//			String airlineFound1Gettext = airlineFound1.getText(); 
//			if(airlineFound1Gettext.equalsIgnoreCase(airlineReturnedSplit2))
//			{
//				System.out.println("Airline filter successfully validated based on flight index");
//				break;
//			}
//		}
//	}
	public void airLineFilterValidationRoundTrip(List<String> selectedAirlines) throws InterruptedException {
	    System.out.println("Selected airlines: " + selectedAirlines);

	    // Click on first flight to get its airline details
	    driver.findElement(By.xpath("(//a[text()='Flight Details'])[1]")).click();
	    Thread.sleep(2000);

	    List<WebElement> airlineElements = driver.findElements(
	        By.xpath("(//div[@class='p-0 p-lg-3'])[1]//span[@class='primary-color flight-company']")
	    );

	    System.out.println("Airlines found in flight details: " + airlineElements.size());

	    // Normalize the selected airline names (before comparing)
	    List<String> normalizedSelectedAirlines = selectedAirlines.stream()
	        .map(name -> name.split("\\(")[0].trim().toLowerCase())
	        .toList();

	    boolean matched = false;

	    for (WebElement airlineElement : airlineElements) {
	        String airlineOnUI = airlineElement.getText().trim().toLowerCase();
	        System.out.println("Found airline in UI: " + airlineOnUI);

	        if (normalizedSelectedAirlines.contains(airlineOnUI)) {
	            System.out.println("✅ Airline filter successfully validated based on flight details");
	            matched = true;
	            break;
	        }
	    }

	    if (!matched) {
	        System.out.println("❌ Airline filter validation failed: None of the expected airlines matched.");
	        // Optionally throw exception or log failure
	    }
	}
//	public void ValidateAirlineFilterResultInRoundTrip(List<String> selectedAirlines)
//	{
//	    System.out.println("Selected airlines: " + selectedAirlines);
//       // String selectedAirlines1 = String.join(", ", selectedAirlines);
//
//        List<WebElement> listOfAirlines = driver.findElements(By.xpath("(//section[contains(@class,'one-way-new-result-card')])"));
//        int i = 1;
//        boolean match = false;
//       for(WebElement airline : listOfAirlines)
//       {
//    	 List<WebElement> operatedBy = airline.findElements(By.xpath(".//span[contains(@class,'flight-operated-by')]"));
//    	 System.out.println(operatedBy.size()); 
//    	 for(WebElement operatedBy1 : operatedBy)
//    	 {
//    		 String operatedBy1Text = operatedBy1.getText();
//    		 String[] operatedBy1Text1 = operatedBy1Text.split("-");
//    		 String operatedBy1Text2 = operatedBy1Text1[1].trim();
//    		 System.out.println(operatedBy1Text2);
//
//    		 System.out.println(selectedAirlines);
//
//    		 boolean matched = selectedAirlines.stream().anyMatch(selAirline->operatedBy1Text.equalsIgnoreCase(selAirline.trim()));
//
//    	 }
//    	 if(match)
//    	 {
//    		System.out.println("Airline name matched");
//    		match= false;
//    		
//    	 }
//    	 if(!match)
//    	 {
//    		System.out.println("Airline name not matched");
//    	
//    	 }
//    	 
//    	   i++;
//    	   
//       }
//		
//	}
//	public void validateAirlineFilterResultInRoundTrip(List<String> selectedAirlines) throws InterruptedException {
//	    System.out.println("Selected airlines: " + selectedAirlines);
//		Thread.sleep(2000);
//
//	    // Find all flight result cards
//	    List<WebElement> flightCards = driver.findElements(By.xpath("//section[contains(@class,'one-way-new-result-card')]"));
//
//	    int cardIndex = 1;
//
//	    for (WebElement card : flightCards) {
//	        System.out.println("🔍 Checking flight card #" + cardIndex);
//
//	        List<WebElement> airlineElements = card.findElements(By.xpath(".//span[contains(@class,'flight-operated-by')]"));
//
//	        boolean matched = false;  // ✅ Track if any match was found
//
//	        for (WebElement airlineElement : airlineElements) {
//	            String airlineText = airlineElement.getText().trim();
//
//	            String[] airlineTextParts = airlineText.split("-");
//	            if (airlineTextParts.length < 2) {
//	                System.out.println("⚠ Unexpected airline format: " + airlineText);
//	                continue;
//	            }
//
//	            String airlineText2 = airlineTextParts[1].trim(); // The clean airline name
//	            System.out.println("✈ Found airline: " + airlineText2);
//
//	            matched = selectedAirlines.stream()
//	                .anyMatch(selAirline -> airlineText2.equalsIgnoreCase(selAirline.split("\\(")[0].trim()));
//
//	            if (matched) {
//	                System.out.println("✅ Airline matched: " + airlineText2);
//	                break; // ✅ Stop checking further airlines in this card
//	            } else {
//	                System.out.println("❌ Airline not matched: " + airlineText2);
//	                
//	            }
//	        }
//
//	        if (matched) {
//	            System.out.println("✔ At least one airline in flight card #" + cardIndex + " matches the selected airlines.");
//	        } else {
//	            System.out.println("✘ No matching airline found in flight card #" + cardIndex + ".");
//	            // You can assert/fail here if this is a test
//	            // Assert.fail("Airline mismatch in card #" + cardIndex);
//	        }
//
//	        cardIndex++;
//	    }
//	}
//	public void validateAirlineFilterResultInRoundTrip(List<String> selectedAirlines,ExtentTest test) throws InterruptedException {
//	    System.out.println("Selected airlines: " + selectedAirlines);
//	    Thread.sleep(2000); // Optional wait if DOM is loading late
//
//	    // Find all flight result cards
//	    List<WebElement> flightCards = driver.findElements(By.xpath("//section[contains(@class,'one-way-new-result-card')]"));
//
//	    int cardIndex = 1;
//
//	    for (WebElement card : flightCards) {
//	        System.out.println("🔍 Checking flight card #" + cardIndex);
//	        test.log(Status.INFO, "🔍 Checking flight card #" + cardIndex);
//
//	        List<WebElement> airlineElements = card.findElements(By.xpath(".//span[contains(@class,'flight-operated-by')]"));
//
//	        boolean matched = false;
//
//	        for (WebElement airlineElement : airlineElements) {
//	            String airlineText = airlineElement.getText().trim();
//
//	            String[] airlineTextParts = airlineText.split("-");
//	            if (airlineTextParts.length < 2) {
//	                System.out.println("⚠ Unexpected airline format: " + airlineText);
//	                test.log(Status.WARNING, "⚠ Unexpected airline format: " + airlineText);
//	                continue;
//	            }
//
//	            String airlineText2 = airlineTextParts[1].trim();
//	            System.out.println("✈ Found airline: " + airlineText2);
//	            test.log(Status.INFO, "✈ Found airline: " + airlineText2);
//
//	            matched = selectedAirlines.stream()
//	                .anyMatch(selAirline -> airlineText2.equalsIgnoreCase(selAirline.split("\\(")[0].trim()));
//
//	            if (matched) {
//	                System.out.println("✅ Airline matched: " + airlineText2);
//	                test.log(Status.PASS, "✅ Airline matched: " + airlineText2);
//	                break;
//	            } else {
//	                System.out.println("❌ Airline not matched: " + airlineText2);
//	                test.log(Status.INFO, "❌ Airline not matched: " + airlineText2);
//	            }
//	        }
//
//	        if (matched) {
//	            String successMessage = "✔ At least one airline in flight card #" + cardIndex + " matches the selected airlines.";
//	            System.out.println(successMessage);
//	            test.log(Status.PASS, successMessage);
//	        } else {
//	            String failureMessage = "✘ No matching airline found in flight card #" + cardIndex + ".";
//	            System.out.println(failureMessage);
//	            test.log(Status.FAIL, failureMessage);
//
//	            // Capture screenshot on failure
//	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No fares found", "FareOptionsMissing");
//
//	            // Fail the test (use org.testng.Assert or junit)
//	            Assert.fail("Airline mismatch in card #" + cardIndex);
//	        }
//
//	        cardIndex++;
//	    }
//	}
	public void validateAirlineFilterResultInRoundTrip(List<String> selectedAirlines, ExtentTest test) throws InterruptedException {
	    System.out.println("Selected airlines: " + selectedAirlines);
	    Thread.sleep(2000); // Optional wait if DOM is loading late

	    // Find all flight result cards
	    List<WebElement> flightCards = driver.findElements(By.xpath("//section[contains(@class,'one-way-new-result-card')]"));

	    for (int cardIndex = 1; cardIndex <= flightCards.size(); cardIndex++) {
	        WebElement card = flightCards.get(cardIndex - 1);

	        System.out.println("🔍 Checking flight card #" + cardIndex);
	       // test.log(Status.INFO, "🔍 Checking flight card #" + cardIndex);

	        List<WebElement> airlineElements = card.findElements(By.xpath(".//span[contains(@class,'flight-operated-by')]"));
	        boolean matched = false;

	        for (WebElement airlineElement : airlineElements) {
	            String airlineText = airlineElement.getText().trim();
	            String[] airlineTextParts = airlineText.split("-");

	            if (airlineTextParts.length < 2) {
	                System.out.println("⚠ Unexpected airline format: " + airlineText);
	                test.log(Status.WARNING, "⚠ Unexpected airline format: " + airlineText);
	                continue;
	            }

	            String airlineText2 = airlineTextParts[1].trim(); // Extracted airline name
	            System.out.println("✈ Found airline: " + airlineText2);

	            matched = selectedAirlines.stream()
	                .anyMatch(selAirline -> airlineText2.equalsIgnoreCase(selAirline.split("\\(")[0].trim()));

	            if (matched) {
	                System.out.println("✅ Airline matched: " + airlineText2);
	                break; // We can stop checking airlines in this card
	            } else {
	                System.out.println("❌	 Airline not matched: " + airlineText2);
	            }
	        }

	        // If not matched, fail immediately
	        if (!matched) {
	            String failureMessage = "✘ No matching airline found in flight card #" + cardIndex + ".";
	            System.out.println(failureMessage);
	            test.log(Status.FAIL, failureMessage);

	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No fares found", "FareOptionsMissing");

	            Assert.fail("Airline mismatch in card #" + cardIndex); // Stop test execution
	            return; // Optional: since Assert.fail throws, return is just for clarity
	        }
	    }

	    // If we reach here, all flight cards matched
	    String successMessage = "✅ All flight cards matched the selected airlines.";
	    System.out.println(successMessage);
	    test.log(Status.PASS, successMessage);
	}
//	public void validateAirlineRoundTrip(List<String> selectedAirlines, ExtentTest test)
//	{
//	    System.out.println("Selected airlines: " + selectedAirlines);
//
//		List<WebElement> flightCard = driver.findElements(By.xpath("//section[contains(@class,'one-way-new-result-card')]"));
//		for(WebElement flightCard1:flightCard)
//		{
//			flightCard1.findElement(By.xpath("//a[text()='Flight Details']")).click();
//			List<WebElement> airLine = flightCard1.findElements(By.xpath("(//section[@class='selected-flight-details'])[1]//span[contains(@class,'flight-company')]"));
//			if(airLine.contains(selectedAirlines))
//			{
//			System.out.println("Airline matched");	
//				
//		
//			}
//			flightCard1.findElement(By.xpath("//button[@class='btn-close']")).click();
//
//		}
//		
//		
//	}
//	public void validateAirlineRoundTrip(List<String> selectedAirlines, ExtentTest test) {
//	    try {
//	        System.out.println("Selected airlines: " + selectedAirlines);
//	        List<WebElement> flightCards = driver.findElements(By.xpath("//section[contains(@class,'one-way-new-result-card')]"));
//
//	        if (flightCards.isEmpty()) {
//	            test.log(Status.FAIL, "❌ No flight cards found on the round-trip results page.");
//	            Assert.fail("No flight cards found.");
//	        }
//
//	        boolean allMatched = true;
//	        List<String> unmatchedAirlines = new ArrayList<>();
//
//	        for (WebElement flightCard : flightCards) {
//	            // Click "Flight Details"
////	            WebElement flightDetailsLink = flightCard.findElement(By.xpath(".//a[text()='Flight Details']"));
////	            flightDetailsLink.click();
//	            JavascriptExecutor js = (JavascriptExecutor) driver;
//
//	            try {
//	                // Scroll element into view (top)
//	            	WebElement element = flightCard.findElement(By.xpath(".//a[text()='Flight Details']"));
//	                js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
//	                Thread.sleep(500); // Optional: Wait for smooth scroll animation
//
//	                // Click after scrolling
//	                element.click();
//	            } catch (Exception e) {
//	                System.out.println("❌ Failed to scroll and click: " + e.getMessage());
//	                e.printStackTrace();
//	                throw new RuntimeException("scrollToCenterAndClick failed", e);
//	            }
//	            Thread.sleep(1000); // Better to use WebDriverWait in real tests
//
//	            // Get airline elements inside this card
//	            List<WebElement> airlineElements = driver.findElements(By.xpath(".//section[contains(@class,'selected-flight-details')]//span[contains(@class,'flight-company')]"));
//
//	            for (WebElement airlineEl : airlineElements) {
//	                String airlineName = airlineEl.getText().trim();
//	                System.out.println("Airline found: " + airlineName);
//
//	              //  boolean matched = selectedAirlines.stream().anyMatch(selected -> selected.equalsIgnoreCase(airlineName));
//	                boolean matched = selectedAirlines.stream()
//	                	    .peek(selected -> System.out.println("🔍 Comparing: selected = " + selected + ", airlineName = " + airlineName))
//	                	    .anyMatch(selected -> selected.split("\\(")[0].trim().equalsIgnoreCase(airlineName));
//
//	                if (!matched) {
//	                    unmatchedAirlines.add(airlineName);
//	                    allMatched = false;
//	                }
//	            }
//
//	            // Close the flight details modal
//	            WebElement closeButton = driver.findElement(By.xpath("//button[@class='btn-close']"));
//	            closeButton.click();
//	            Thread.sleep(500);
//	        }
//
//	        if (allMatched) {
//	            test.log(Status.PASS, "✅ All airlines in round-trip results match the selected filters: " + selectedAirlines);
//	        } else {
//	            String failMsg = "❌ Some airlines do not match the selected filters. Unexpected: " + unmatchedAirlines;
//	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, failMsg, "Airline mismatch found");
//	            test.log(Status.FAIL, failMsg);
//	            Assert.fail(failMsg);
//	        }
//
//	    } catch (Exception e) {
//	        String errorMsg = "❌ Exception in validateAirlineRoundTrip(): " + e.getMessage();
//	        test.log(Status.FAIL, errorMsg);
//	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, errorMsg, "Exception during airline validation");
//	        e.printStackTrace();
//	        Assert.fail(errorMsg);
//	    }
//	}
//	public void validateAirlineRoundTrip(List<String> selectedAirlines, ExtentTest test) {
//	    try {
//	        System.out.println("Selected airlines: " + selectedAirlines);
//	        List<WebElement> flightCards = driver.findElements(By.xpath("//section[contains(@class,'one-way-new-result-card')]"));
//
//	        if (flightCards.isEmpty()) {
//	            test.log(Status.FAIL, "❌ No flight cards found on the round-trip results page.");
//	            Assert.fail("No flight cards found.");
//	        }
//
//	        boolean allMatched = true;
//	        List<String> unmatchedAirlines = new ArrayList<>();
//
//	        for (WebElement flightCard : flightCards) {
//	            JavascriptExecutor js = (JavascriptExecutor) driver;
//
//	            try {
//	                // Scroll element into view (center)
//	                WebElement element = flightCard.findElement(By.xpath(".//a[text()='Flight Details']"));
//	                js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
//	                Thread.sleep(500); // Optional: Wait for smooth scroll animation
//
//	                // Click after scrolling
//	                element.click();
//	            } catch (Exception e) {
//	                System.out.println("❌ Failed to scroll and click: " + e.getMessage());
//	                e.printStackTrace();
//	                throw new RuntimeException("scrollToCenterAndClick failed", e);
//	            }
//
//	            Thread.sleep(1000); // Prefer WebDriverWait in real tests
//
//	            // Get airline elements inside this card's flight details
//	            List<WebElement> airlineElements = driver.findElements(By.xpath(".//section[contains(@class,'selected-flight-details')]//span[contains(@class,'flight-company')]"));
//
//	            // Collect all airlines for this flight card
//	            List<String> airlinesInFlight = new ArrayList<>();
//	            for (WebElement airlineEl : airlineElements) {
//	                String airlineName = airlineEl.getText().trim();
//	                System.out.println("Airline found: " + airlineName);
//	                airlinesInFlight.add(airlineName);
//	            }
//
//	            // Check if any airline in this flight card matches selected airlines
//	            boolean anyMatched = airlinesInFlight.stream().anyMatch(airlineName ->
//	                selectedAirlines.stream()
//	                    .peek(selected -> System.out.println("🔍 Comparing: selected = " + selected + ", airlineName = " + airlineName))
//	                    .anyMatch(selected -> selected.split("\\(")[0].trim().equalsIgnoreCase(airlineName))
//	            );
//
//	            if (!anyMatched) {
//	                unmatchedAirlines.addAll(airlinesInFlight);
//	                allMatched = false;
//	            }
//
//	            // Close the flight details modal
//	            WebElement closeButton = driver.findElement(By.xpath("//button[@class='btn-close']"));
//	            closeButton.click();
//	            Thread.sleep(500);
//	        }
//
//	        if (allMatched) {
//	            test.log(Status.PASS, "✅ All airlines in round-trip results match the selected filters: " + selectedAirlines);
//	        } else {
//	            String failMsg = "❌ Some airlines do not match the selected filters. Unexpected: " + unmatchedAirlines;
//	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, failMsg, "Airline mismatch found");
//	            test.log(Status.FAIL, failMsg);
//	            Assert.fail(failMsg);
//	        }
//
//	    } catch (Exception e) {
//	        String errorMsg = "❌ Exception in validateAirlineRoundTrip(): " + e.getMessage();
//	        test.log(Status.FAIL, errorMsg);
//	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, errorMsg, "Exception during airline validation");
//	        e.printStackTrace();
//	        Assert.fail(errorMsg);
//	    }
//	}
	public void validateAirlineRoundTrip(List<String> selectedAirlines, ExtentTest test) {
	    try {
	    	Thread.sleep(2000);
	        System.out.println("Selected airlines: " + selectedAirlines);
	        List<WebElement> flightCards = driver.findElements(By.xpath("//section[contains(@class,'one-way-new-result-card')]"));

	        if (flightCards.isEmpty()) {
	            test.log(Status.FAIL, "❌ No flight cards found on the round-trip results page.");
	            Assert.fail("No flight cards found.");
	        }

	        boolean allMatched = true;
	        List<String> unmatchedAirlines = new ArrayList<>();

	        for (WebElement flightCard : flightCards) {
	            JavascriptExecutor js = (JavascriptExecutor) driver;

	            try {
	            	Thread.sleep(1000);
	                WebElement element = flightCard.findElement(By.xpath(".//a[text()='Flight Details']"));
	                js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
	                Thread.sleep(1000); // optional
	                element.click();
	            } catch (Exception e) {
	                System.out.println("❌ Failed to scroll and click: " + e.getMessage());
	                e.printStackTrace();
	                throw new RuntimeException("scrollToCenterAndClick failed", e);
	            }

	            Thread.sleep(1000); // Consider using WebDriverWait instead

	            // ✅ Only get onward journey airlines from the first "selected-flight-details" section
	            WebElement onwardSection = driver.findElement(By.xpath("(//section[@class='selected-flight-details'])[1]"));

	            List<WebElement> airlineElements = onwardSection.findElements(By.xpath(".//span[contains(@class,'flight-company')]"));

	            // Collect onward airline names
	            List<String> onwardAirlines = new ArrayList<>();
	            for (WebElement airlineEl : airlineElements) {
	                String airlineName = airlineEl.getText().trim();
	                System.out.println("🛫 Onward Airline Found: " + airlineName);
	                onwardAirlines.add(airlineName);
	            }

	            // Check if any onward airline matches selected airlines
	            boolean anyMatched = onwardAirlines.stream().anyMatch(airlineName ->
	                selectedAirlines.stream()
	                    .peek(selected -> System.out.println("🔍 Comparing: selected = " + selected + ", airlineName = " + airlineName))
	                    .anyMatch(selected -> selected.split("\\(")[0].trim().equalsIgnoreCase(airlineName))
	            );

	            if (!anyMatched) {
	                unmatchedAirlines.addAll(onwardAirlines);
	                allMatched = false;
	            }

	            // Close the flight details modal
	            WebElement closeButton = driver.findElement(By.xpath("//button[@class='btn-close']"));
	            closeButton.click();
	            Thread.sleep(500);
	        }

	        if (allMatched) {
	            test.log(Status.PASS, "✅ All onward airlines in round-trip results match the selected filters: " + selectedAirlines);
	        } else {
	            String failMsg = "❌ Some onward airlines do not match the selected filters. Unexpected: " + unmatchedAirlines;
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, failMsg, "Onward airline mismatch found");
	            test.log(Status.FAIL, failMsg);
	            Assert.fail(failMsg);
	        }

	    } catch (Exception e) {
	        String errorMsg = "❌ Exception in validateAirlineRoundTrip(): " + e.getMessage();
	        test.log(Status.FAIL, errorMsg);
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, errorMsg, "Exception during airline validation");
	        e.printStackTrace();
	        Assert.fail(errorMsg);
	    }
	}
	


//	public void validateAirlineRoundTrip(List<String> selectedAirlines, ExtentTest test) {
//	    try {
//	        System.out.println("Selected airlines: " + selectedAirlines);
//	        List<WebElement> flightCards = driver.findElements(By.xpath("//section[contains(@class,'one-way-new-result-card')]"));
//
//	        if (flightCards.isEmpty()) {
//	            test.log(Status.FAIL, "❌ No flight cards found on the round-trip results page.");
//	            Assert.fail("No flight cards found.");
//	        }
//
//	        boolean allMatched = true;
//	        List<String> unmatchedAirlines = new ArrayList<>();
//
//	        for (WebElement flightCard : flightCards) {
//	            JavascriptExecutor js = (JavascriptExecutor) driver;
//
//	            try {
//	                // Scroll "Flight Details" link into view and click
//	                WebElement element = flightCard.findElement(By.xpath(".//a[text()='Flight Details']"));
//	                js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
//	                Thread.sleep(500); // Optional smooth scroll wait
//
//	                element.click();
//	            } catch (Exception e) {
//	                System.out.println("❌ Failed to scroll and click: " + e.getMessage());
//	                e.printStackTrace();
//	                throw new RuntimeException("scrollToCenterAndClick failed", e);
//	            }
//
//	            Thread.sleep(1000); // Ideally use WebDriverWait instead
//
//	            // Locate the flight details modal
//	            WebElement flightDetailsModal = driver.findElement(By.xpath("//section[contains(@class,'selected-flight-details')]"));
//
//	            // Locate onward flight section (assuming it's the first flight segment inside modal)
//	            WebElement onwardFlightSection = flightDetailsModal.findElement(By.xpath(".//div[contains(@class,'flight-segment')][1]"));
//
//	            // Get airline elements only from onward flight section
//	            List<WebElement> airlineElements = onwardFlightSection.findElements(By.xpath(".//span[contains(@class,'flight-company')]"));
//
//	            // Collect onward flight airlines
//	            List<String> airlinesInFlight = new ArrayList<>();
//	            for (WebElement airlineEl : airlineElements) {
//	                String airlineName = airlineEl.getText().trim();
//	                System.out.println("Onward airline found: " + airlineName);
//	                airlinesInFlight.add(airlineName);
//	            }
//
//	            // Check if any onward airline matches selected airlines
//	            boolean anyMatched = airlinesInFlight.stream().anyMatch(airlineName ->
//	                selectedAirlines.stream()
//	                    .peek(selected -> System.out.println("🔍 Comparing: selected = " + selected + ", airlineName = " + airlineName))
//	                    .anyMatch(selected -> selected.split("\\(")[0].trim().equalsIgnoreCase(airlineName))
//	            );
//
//	            if (!anyMatched) {
//	                unmatchedAirlines.addAll(airlinesInFlight);
//	                allMatched = false;
//	            }
//
//	            // Close the flight details modal
//	            WebElement closeButton = driver.findElement(By.xpath("//button[@class='btn-close']"));
//	            closeButton.click();
//	            Thread.sleep(500);
//	        }
//
//	        if (allMatched) {
//	            test.log(Status.PASS, "✅ All onward airlines in round-trip results match the selected filters: " + selectedAirlines);
//	        } else {
//	            String failMsg = "❌ Some onward airlines do not match the selected filters. Unexpected: " + unmatchedAirlines;
//	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, failMsg, "Onward airline mismatch found");
//	            test.log(Status.FAIL, failMsg);
//	            Assert.fail(failMsg);
//	        }
//
//	    } catch (Exception e) {
//	        String errorMsg = "❌ Exception in validateAirlineRoundTrip(): " + e.getMessage();
//	        test.log(Status.FAIL, errorMsg);
//	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, errorMsg, "Exception during airline validation");
//	        e.printStackTrace();
//	        Assert.fail(errorMsg);
//	    }
//	}

	public void validateStopsFilterResultInRoundTrip(List<String> selectedStops, ExtentTest test) throws InterruptedException {
	    System.out.println("Selected Stops: " + selectedStops);
	    Thread.sleep(2000); // Optional wait if DOM is loading late

	    // Find all flight result cards
	    List<WebElement> flightCards = driver.findElements(By.xpath("//section[contains(@class,'one-way-new-result-card')]"));

	    for (int cardIndex = 1; cardIndex <= flightCards.size(); cardIndex++) {
	        WebElement card = flightCards.get(cardIndex - 1);

	        System.out.println("🔍 Checking flight card #" + cardIndex);
	       // test.log(Status.INFO, "🔍 Checking flight card #" + cardIndex);

	        List<WebElement> stopsElements = card.findElements(By.xpath(".//p[@class='stop-seperator']/following-sibling::span"));
	        boolean matched = false;

	        for (WebElement stopElements : stopsElements) {
	            String stopText = stopElements.getText().trim();
	            String[] stopTextParts = stopText.split("at");

	            if (stopTextParts.length < 2) {
	                System.out.println("⚠ Unexpected stops format: " + stopText.trim());
	                test.log(Status.WARNING, "⚠ Unexpected stops format: " + stopText.trim());
	                continue;
	            }

	            String stopsText2 = stopTextParts[0].trim(); // Extracted airline name
	            System.out.println("✈ Found airline: " + stopsText2);

	            matched = selectedStops.stream()
	                .anyMatch(selstop -> stopsText2.equalsIgnoreCase(selstop.trim()));
	            //split("\\(")[0]

	            if (matched) {
	                System.out.println("✅ Stops matched: " + stopsText2);
	                break; // We can stop checking airlines in this card
	            } else {
	                System.out.println("❌	 Stops not matched: " + stopsText2);
	            }
	        }

	        // If not matched, fail immediately
	        if (!matched) {
	            String failureMessage = "✘ No matching Stops found in flight card #" + cardIndex + ".";
	            System.out.println(failureMessage);
	            test.log(Status.FAIL, failureMessage);

	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No matching Stops found in flight card", "StopsNotMatched");

	            Assert.fail("Stop mismatch in card #" + cardIndex); // Stop test execution
	            return; // Optional: since Assert.fail throws, return is just for clarity
	        }
	    }

	    // If we reach here, all flight cards matched
	    String successMessage = "✅ All flight cards matched the selected stops.";
	    System.out.println(successMessage);
	    test.log(Status.PASS, successMessage);
	}


//	public String[] getFlightCardDetails()
//	{
//		ArrayList<String> flightNo = new ArrayList<>();
//		String flightNumber = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='fs-10 flight-number']")).getText();
//		System.out.println(flightNumber);
//		String[] flightNumberSplit = flightNumber.split(",");
//		Collections.addAll(flightNo, flightNumberSplit);
//		System.out.println(flightNo);
//
//
//
//		String airLineName = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-company']")).getText().trim();
//		System.out.println(airLineName);
//
//		String flightEquipment = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='fs-8 fw-300 grey-color flight-equipment']")).getText();
//		System.out.println(flightEquipment);
//		ArrayList<String> flightEq = new ArrayList<>();
//		String flightEquipment1 = flightEquipment.replace("Equipment: ", "");
//		String[] flightEquipment2 = flightEquipment1.split(",");
//		Collections.addAll(flightEq,flightEquipment2);
//		System.out.println(flightEq);
//
//		String flightOperatedBy = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='fs-8 fw-300 grey-color flight-operated-by']")).getText();
//		System.out.println(flightOperatedBy);
//		//		ArrayList<String> flightOperated = new ArrayList<>();
//		//		String flightOperatedBy1 = flightOperatedBy.replace("Operated By: ", "");
//		//		String[] flightOperatedBy2 = flightOperatedBy1.split(",");
//		//		Collections.addAll(flightOperated,flightOperatedBy2);
//		//		System.out.println(flightOperated);
//		// Step 1: Remove "Operated By:"
//		String[] parts = flightOperatedBy.split(":");
//
//		// Step 2: Take the second part (after colon), and split by comma
//		String[] airlinesWithCode = parts[1].split(",");
//
//		// Step 3: Loop through each airline, split by dash, and take only the name part
//		List<String> cleanAirlines = new ArrayList<>();
//		for (String airline : airlinesWithCode) {
//			String[] codeAndName = airline.trim().split("-");
//			if (codeAndName.length > 1) {
//				cleanAirlines.add(codeAndName[1].trim());
//			}
//		}
//		System.out.println(cleanAirlines);
//
//		String flightDeptTime = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-origin']")).getText().trim();
//		System.out.println(flightDeptTime);
//
//
//		String flightArrivalTime = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='title fw-600 flight-arrtime']")).getText().trim();
//		System.out.println(flightArrivalTime);
//
//		String flightDeptLocation = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-origin']")).getText();
//		System.out.println(flightDeptLocation);
//		String[] flightDeptLocation1 = flightDeptLocation.split("-");
//		String flightDeptLocation2 = flightDeptLocation1[0].trim();
//		System.out.println(flightDeptLocation2);
//
//
//		String flightArrivalLocation = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-destination']")).getText();
//		System.out.println(flightArrivalLocation);
//		String[] flightArrivalLocation1 = flightArrivalLocation.split("-");
//		String flightArrivalLocation2 = flightArrivalLocation1[0].trim();
//		System.out.println(flightArrivalLocation2);
//
//		String flightDeptDate = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-depdate']")).getText();
//		System.out.println(flightDeptDate);
//
//		String flightArrivalDate = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-arrdate']")).getText();
//		System.out.println(flightArrivalDate);
//
//		String flightPrice = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='fs-18 fw-600 secondary-color flight-totalfq']")).getText();
//		System.out.println(flightPrice);
//
//		String totalDuration = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='fs-12 fw-500 grey-color flight-totaljourneyduration']")).getText();
//		System.out.println(totalDuration);
//
//		String stop = driver.findElement(By.xpath("//div[@class=' d-flex flex-column mb-2 one-way-new-result-card ']//p[@class='stop-seperator']/following-sibling::span")).getText();
//		System.out.println(stop);
//		String[] stop1 = stop.split("stop");
//		String stop2 = stop1[0].trim();
//		System.out.println(stop2);
//		return new String[] {flightNo,airLineName,flightEq,cleanAirlines,flightDeptTime,flightArrivalTime,flightDeptLocation2,flightArrivalLocation2,flightDeptDate,flightArrivalDate,flightPrice,totalDuration,stop2};
//
//
//
//
//
//
//	}
	public String[] getFlightCardDetails() {
	    ArrayList<String> flightNo = new ArrayList<>();
	    String flightNumber = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='fs-10 flight-number']")).getText();
	    System.out.println(flightNumber);
	    String[] flightNumberSplit = flightNumber.split(",");
	    for(String flightNumberSplits : flightNumberSplit)
	    {
	    	String flightNumberSplits1 = flightNumberSplits.trim();
	    	flightNo.add(flightNumberSplits1);

	    }
	    //Collections.add(flightNo, flightNumberSplits1);
	    System.out.println(flightNo);
	    

	    String airLineName = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-company']")).getText().trim().toLowerCase();
	    System.out.println(airLineName);

//	    String flightEquipment = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='fs-8 fw-300 grey-color flight-equipment']")).getText();
//	    System.out.println(flightEquipment);
//	    ArrayList<String> flightEq = new ArrayList<>();
//	    String flightEquipment1 = flightEquipment.replace("Equipment: ", "").trim();
//	    String[] flightEquipment2 = flightEquipment1.split(",");
//	    for(String flightEquipment3:flightEquipment2)
//	    {
//	    	System.out.println(flightEquipment3);
//	    	String flightEquipment4 = flightEquipment3.trim();
//	    	flightEq.add(flightEquipment4);
//	    }
//	   // Collections.addAll(flightEq,flightEquipment2);
//	    System.out.println(flightEq);
	    
	    String flightEquipment = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='fs-8 fw-300 grey-color flight-equipment']")).getText();
	    
	    ArrayList<String> flightEq = new ArrayList<>();
	    String flightEquipment1 = flightEquipment.replace("Equipment: ", "").trim();
	    String[] flightEquipment2 = flightEquipment1.split(",");
	     
	    for (String flightEquipment3 : flightEquipment2) {
	        String flightEquipment4 = flightEquipment3.trim();  // Remove leading/trailing spaces
	        flightEq.add(flightEquipment4);
	    }
	     

//	    String result = String.join(",", flightEq);
//	    System.out.println(result);

	    String flightOperatedBy = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='fs-8 fw-300 grey-color flight-operated-by']")).getText();
	    System.out.println(flightOperatedBy);

	    String[] parts = flightOperatedBy.split(":");
	    String[] airlinesWithCode = parts[1].split(",");

	    List<String> cleanAirlines = new ArrayList<>();
	    for (String airline : airlinesWithCode) {
//	        String[] codeAndName = airline.trim().split("-");
//	        if (codeAndName.length > 1) {
//	            cleanAirlines.add(codeAndName[1].trim());
//	        }
	    	String airline1 = airline.trim().toLowerCase();
	    	cleanAirlines.add(airline1);
	    }
	    System.out.println(cleanAirlines);

	    String flightDeptTime = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='title fw-600 flight-deptime']")).getText().trim();
	    System.out.println(flightDeptTime);

	    String flightArrivalTime1 = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='title fw-600 flight-arrtime']")).getText().trim();
	    System.out.println(flightArrivalTime1);
	    String flightArrivalTime = flightArrivalTime1.replace("+1", "");
	    System.out.println(flightArrivalTime);

	    String flightDeptLocation = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-origin']")).getText();
	    System.out.println(flightDeptLocation);
	    String[] flightDeptLocation1 = flightDeptLocation.split("-");
	    String flightDeptLocation2 = flightDeptLocation1[0].trim();
	    System.out.println(flightDeptLocation2);

	    String flightArrivalLocation = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-destination']")).getText();
	    System.out.println(flightArrivalLocation);
	    String[] flightArrivalLocation1 = flightArrivalLocation.split("-");
	    String flightArrivalLocation2 = flightArrivalLocation1[0].trim();
	    System.out.println(flightArrivalLocation2);

	    String flightDeptDate = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-depdate']")).getText();
	    System.out.println(flightDeptDate);

	    String flightArrivalDate = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-arrdate']")).getText();
	    System.out.println(flightArrivalDate);

	    String flightPrice = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='fs-18 fw-600 secondary-color flight-totalfq']")).getText();
	    System.out.println(flightPrice);

	    String totalDuration = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='fs-12 fw-500 grey-color flight-totaljourneyduration']")).getText();
	    System.out.println(totalDuration);

	    String stop = driver.findElement(By.xpath("//div[@class=' d-flex flex-column mb-2 one-way-new-result-card ']//p[@class='stop-seperator']/following-sibling::span")).getText();
	    System.out.println(stop);
	    String[] stop1 = stop.split("stop");
	    String stop2 = stop1[0].trim();
	    System.out.println(stop2);

	    // Convert lists to comma-separated strings
	    String flightNoStr = String.join(",", flightNo);
	    String flightEqStr = String.join(",", flightEq);
	    String cleanAirlinesStr = String.join(",", cleanAirlines);

	    return new String[] {
	        flightNoStr,
	        airLineName,
	        flightEqStr,
	        cleanAirlinesStr,
	        flightDeptTime,
	        flightArrivalTime,
	        flightDeptLocation2,
	        flightArrivalLocation2,
	        flightDeptDate,
	        flightArrivalDate,
	        flightPrice,
	        totalDuration,
	        stop2
	    };
	}
//public void getFlightDetails()
//{
//	WebElement sectors = driver.findElement(By.xpath("//span[@class='sector-span']"));
//	String[] sectorsSplit = sectors.getText().split("->");
//	for(String sectorsSplit1 :sectorsSplit)
//	{
//		String[] sectorsSplit2 = sectorsSplit1.split("\\(");
//		String Sector1 = sectorsSplit2[0].trim();
//		String Sector2= sectorsSplit2[1].replace(")", "").trim();
//		System.out.println(Sector1);
//		System.out.println(Sector2);
//
//	}
//	
//	
//
//
//}
//	public String[] getFlightDetails() throws InterruptedException {
//	    WebElement sector = driver.findElement(By.xpath("//span[@class='sector-span']"));
//	 // Step 1: Split by "->" to get each sector
//	    String[] sectors = sector.getText().split("->");
//
//	    // Step 2: For each sector, extract city name and code
//	    String[] sector1Parts = sectors[0].trim().split("\\(");
//	    String city1 = sector1Parts[0].trim();                // "Dubai"
//	    String code1 = sector1Parts[1].replace(")", "").trim();  // "DXB"
//
//	    String[] sector2Parts = sectors[1].trim().split("\\(");
//	    String city2 = sector2Parts[0].trim();                // "Hamburg"
//	    String code2 = sector2Parts[1].replace(")", "").trim();  // "HAM"
//
//	    // Print values to verify
//	    System.out.println("City 1: " + city1);
//	    System.out.println("Code 1: " + code1);
//	    System.out.println("City 2: " + city2);
//	    System.out.println("Code 2: " + code2);
//	    
//	    Thread.sleep(2000);
//	   WebElement departDate = driver.findElement(By.xpath("//span[@class='date-span']"));
//	   String[] departDate1 = departDate.getText().split(",");
//	   String departDate2 = departDate1[1].trim();
//	   System.out.println(departDate2);
//	   String departDate3 = departDate1[2].trim();
//	   System.out.println(departDate3);
//	   String departDateConcat = departDate2+", "+departDate3;
//	   System.out.println(departDateConcat);
//	   
//	   
//	   List<WebElement> Airlines = driver.findElements(By.xpath("//span[@class='primary-color flight-company']"));
//	   ArrayList<String> airlines = new ArrayList<>();
//	  for(WebElement Airline:Airlines)
//	  {
//		  String airlineGetText = Airline.getText();
//		  airlines.add(airlineGetText);
//		  System.out.println(airlines);
//	  }
//	  
//	  List<WebElement> flightNumber = driver.findElements(By.xpath("//span[@class='flight-number']"));
//	  ArrayList<String> flightNumbers = new ArrayList<>();
//	  for(WebElement flightNumber1:flightNumber)
//	  {
//		  String flightNumber1Text = flightNumber1.getText();
//		  flightNumbers.add(flightNumber1Text);
//		  System.out.println(flightNumbers);
//	  }
//	  
//	  List<WebElement> FlightEquipment = driver.findElements(By.xpath("//span[@class='flight-equipment']"));
//	
//	  ArrayList<String> FlightEquipments = new ArrayList<>();
//	  for(WebElement FlightEquipment1:FlightEquipment)
//	  {
//		  String FlightEquipment1Text = FlightEquipment1.getText().trim();
//		  System.out.println(FlightEquipment1Text);
//		  String FlightEquipment1Text1 = FlightEquipment1Text.replace("AIRCRAFT TYPE:","").trim();
//		  
//		  FlightEquipments.add(FlightEquipment1Text1);
//		  System.out.println(FlightEquipments);
//	  }
//	  
//	  
//	  
//	  List<WebElement> OperatorName = driver.findElements(By.xpath("//span[@class='flight-operated-by']"));
//		
//	  ArrayList<String> OperatorNames = new ArrayList<>();
//	  for(WebElement OperatorName1:OperatorName)
//	  {
//		  String OperatorName1Text = OperatorName1.getText().trim();
//		  System.out.println(OperatorName1Text);
//		  String OperatorName1Text1 = OperatorName1Text.replace("OPERATED BY:","").trim();
//		  
//		  OperatorNames.add(OperatorName1Text1);
//		  System.out.println(OperatorNames);
//	  }
//	  
//	  
//	List<WebElement> departTimeGet = driver.findElements(By.xpath("//span[@class='flight-deptime']"));
//	String DepartTimeText = departTimeGet.get(0).getText();
//	System.out.println(DepartTimeText);
//	
//	List<WebElement> arrivalTimeGet = driver.findElements(By.xpath("//span[@class='flight-arrtime']"));
//	  String ArrivalTimeTextt = arrivalTimeGet.getLast().getText();
//	  System.out.println(ArrivalTimeTextt);
//	  
//	  return new String[] {city1,code1,city2,code2,departDateConcat,airlines,flightNumbers,FlightEquipments,OperatorNames,DepartTimeText,ArrivalTimeTextt};
//	} 
	public String[] getFlightDetails() throws InterruptedException {
		Thread.sleep(2000);
	    WebElement sector = driver.findElement(By.xpath("//span[@class='sector-span']"));
	    String[] sectors = sector.getText().split("->");

	    String[] sector1Parts = sectors[0].trim().split("\\(");
	    String city1 = sector1Parts[0].trim();
	    String code1 = sector1Parts[1].replace(")", "").trim();

	    String[] sector2Parts = sectors[1].trim().split("\\(");
	    String city2 = sector2Parts[0].trim();
	    String code2 = sector2Parts[1].replace(")", "").trim();

	    Thread.sleep(2000);
	    WebElement departDate = driver.findElement(By.xpath("//span[@class='date-span']"));
	    String[] departDate1 = departDate.getText().split(",");
	    String departDate2 = departDate1[1].trim();
	    String departDate3 = departDate1[2].trim();
	    String departDateConcat = departDate2 + ", " + departDate3;

	    List<WebElement> Airlines = driver.findElements(By.xpath("//span[@class='primary-color flight-company']"));
	    ArrayList<String> airlines = new ArrayList<>();
	    for (WebElement Airline : Airlines) {
	        airlines.add(Airline.getText().trim().toLowerCase());
	    }

	    List<WebElement> flightNumber = driver.findElements(By.xpath("//span[@class='flight-number']"));
	    ArrayList<String> flightNumbers = new ArrayList<>();
	    for (WebElement flightNumber1 : flightNumber) {
	        flightNumbers.add(flightNumber1.getText());
	    }

	    List<WebElement> FlightEquipment = driver.findElements(By.xpath("//span[@class='flight-equipment']"));
	    ArrayList<String> FlightEquipments = new ArrayList<>();
	    for (WebElement FlightEquipment1 : FlightEquipment) {
	        String text = FlightEquipment1.getText().trim().replace("AIRCRAFT TYPE:", "").trim();
	        FlightEquipments.add(text);
	    }

	    List<WebElement> OperatorName = driver.findElements(By.xpath("//span[@class='flight-operated-by']"));
	    ArrayList<String> OperatorNames = new ArrayList<>();
	    for (WebElement OperatorName1 : OperatorName) {
	        String text = OperatorName1.getText().trim().replace("OPERATED BY:", "").trim().toLowerCase();
	        OperatorNames.add(text);
	    }

	    List<WebElement> departTimeGet = driver.findElements(By.xpath("//span[@class='flight-deptime']"));
	    String DepartTimeText = departTimeGet.get(0).getText();

	    List<WebElement> arrivalTimeGet = driver.findElements(By.xpath("//span[@class='flight-arrtime']"));
	    String ArrivalTimeTextt = arrivalTimeGet.get(arrivalTimeGet.size() - 1).getText();

	    
	    
	 WebElement firstLegDepartDate = driver.findElement(By.xpath("//div[@class='modal-dialog slide-from-right-animation modal-fullscreen']//span[@class='flight-depdate']"));
	 String[] Date = firstLegDepartDate.getText().split(",");
	 String Date1 = Date[1].trim();
	 String year = Date[2].trim();
	 String dateYear = Date1 +" "+year;
	 String dateYear1 =  dateYear.replace(")", "");
	 System.out.println(dateYear1);
	 
	 List<WebElement> firstLegDepartLocation = driver.findElements(By.xpath("//span[@class='flight-origin_name']"));
	 WebElement firstLegDepartLocationFirst = firstLegDepartLocation.getFirst();
	 String[] firstLegDepartLocation1 = firstLegDepartLocationFirst.getText().split("-");
	 String firstLegDepartLocation2 = firstLegDepartLocation1[0].trim();
	 System.out.println(firstLegDepartLocation2);
	 
	 
	  List<WebElement> firstLegArrivalLocation = driver.findElements(By.xpath("//span[@class='flight-origin_name']"));
	  WebElement firstLegArrivalLocation1 = firstLegArrivalLocation.getLast();
      String[] firstLegArrivalLocation2 = firstLegArrivalLocation1.getText().split("-");
	  String firstLegArrivalLocation3 = firstLegArrivalLocation2[0].trim();
	  System.out.println(firstLegArrivalLocation3);
	 
	 
	  List<WebElement> fareTypes = driver.findElements(By.xpath("//div[@class='flight-fare-type']"));
	  ArrayList<String> Fare = new ArrayList<>();
	 for(WebElement fareType:fareTypes)
	 {
		 String fareTypeText = fareType.getText().trim();
		 Fare.add(fareTypeText);
	 }
	 System.out.println(Fare);
 
	 
	  List<WebElement> bookingClass = driver.findElements(By.xpath("//span[@class='flight-booking-class']"));
	  ArrayList<String> bookingcls = new ArrayList<>();

	 for(WebElement bookingClas : bookingClass)
	 {
		 String bookingClas1 = bookingClas.getText().replace("BOOKING CLASS:", "").trim();
		 bookingcls.add(bookingClas1);

		 System.out.println(bookingClas1);
	 }
	 System.out.println(bookingcls);
 
//	  List<WebElement> LayoverStops = driver.findElements(By.xpath("//span[@class='selected-flight-details__flight-leg_layover__location ms-1']"));
//	  int LayoverStopsSize = LayoverStops.size();
//	  if(LayoverStopsSize==0)
//	  {
//		  int Stop = 0;
//		  return Stop;
//      }
	 
	 
	    // Convert lists to strings
	    String airlinesStr = String.join(",", airlines);
	    String flightNumbersStr = String.join(",", flightNumbers);
	    String flightEquipmentsStr = String.join(",", FlightEquipments);
	    String operatorNamesStr = String.join(",", OperatorNames);
	    String FareStr = String.join(",", Fare);
	    String bookingclsStr = String.join(",", bookingcls);

	    
	    
	    return new String[]{
	        city1, code1, city2, code2, departDateConcat,
	        airlinesStr, flightNumbersStr, flightEquipmentsStr, operatorNamesStr,
	        DepartTimeText, ArrivalTimeTextt ,FareStr,bookingclsStr
	    };
	}
	
	
	public int ValidateStopsInFlightDetailsCard(String[] GetFlightCardDetails) {
		String[] GetFlightCardDetailsReturned = getFlightCardDetails();
		String stop2 = GetFlightCardDetailsReturned[12];
		System.out.println(stop2);
	    int stops = driver.findElements(By.xpath("//span[@class='selected-flight-details__flight-leg_layover__location ms-1']")).size();
	 
	    switch (stops) {
	        case 0:
	            return 0;
	        case 1:
	            return 1;
	        case 2:
	            return 2;
	        default:
	            return 2; // or throw an exception or log a warning if >2 is unexpected
	    }
	    
	    
	}
public void validateFlightCardAndFlightDetails(String[] GetFlightCardDetails,String[] GetFlightDetails) throws InterruptedException
{
	
	String[] GetFlightCardDetailsReturned = getFlightCardDetails();
	String flightNoStr = GetFlightCardDetailsReturned[0];
	System.out.println(flightNoStr);
	String airLineName = GetFlightCardDetailsReturned[1];
	System.out.println(airLineName);
	String flightEqStr = GetFlightCardDetailsReturned[2];
	System.out.println(flightEqStr);
	String cleanAirlinesStr = GetFlightCardDetailsReturned[3];
	System.out.println(cleanAirlinesStr);
	String flightDeptTime = GetFlightCardDetailsReturned[4];
	System.out.println(flightDeptTime);
	String flightArrivalTime = GetFlightCardDetailsReturned[5];
	System.out.println(flightArrivalTime);
	String flightDeptLocation2 = GetFlightCardDetailsReturned[6];
	System.out.println(flightDeptLocation2);
	String flightArrivalLocation2 = GetFlightCardDetailsReturned[7];
	System.out.println(flightArrivalLocation2);
	String flightDeptDate = GetFlightCardDetailsReturned[8];
	System.out.println(flightDeptDate);
	String flightArrivalDate = GetFlightCardDetailsReturned[9];
	System.out.println(flightArrivalDate);
	String flightPrice = GetFlightCardDetailsReturned[10];
	System.out.println(flightPrice);
	String totalDuration = GetFlightCardDetailsReturned[11];
	System.out.println(totalDuration);
	String stop2 = GetFlightCardDetailsReturned[12];
	System.out.println(stop2);
//------------------------------------------------------------------------------------------
	String[] getFlightDetailsReturned = getFlightDetails();
	String city1 = getFlightDetailsReturned[0];
	System.out.println(city1);
	String code1 = getFlightDetailsReturned[1];
	System.out.println(code1);
	String city2 = getFlightDetailsReturned[2];
	System.out.println(city2);
	String code2 = getFlightDetailsReturned[3];
	System.out.println(code2);
	String departDateConcat = getFlightDetailsReturned[4];
	System.out.println(departDateConcat);
	String airlinesStr = getFlightDetailsReturned[5];
	System.out.println(airlinesStr);
	String flightNumbersStr = getFlightDetailsReturned[6];
	System.out.println(flightNumbersStr);
	String flightEquipmentsStr = getFlightDetailsReturned[7];
	System.out.println(flightEquipmentsStr);
	String operatorNamesStr = getFlightDetailsReturned[8];
	System.out.println(operatorNamesStr);
	String DepartTimeText = getFlightDetailsReturned[9];
	System.out.println(DepartTimeText);
	String ArrivalTimeTextt = getFlightDetailsReturned[10];
	System.out.println(ArrivalTimeTextt);
	
	
	
//	System.out.println(flightNoStr);
//	System.out.println(flightNumbersStr);

//	if(flightNoStr.equals(flightNumbersStr) && airLineName.equals(airlinesStr) && flightEqStr.equals(flightEquipmentsStr) && flightDeptTime.equals(DepartTimeText) && flightArrivalTime.equals(ArrivalTimeTextt) && flightDeptLocation2.equals(code1) && flightArrivalLocation2.equals(code2) && flightDeptDate.equals(departDateConcat))
//	{
//		System.out.println("Pass");
//	}
	System.out.println(airLineName);
	System.out.println(airlinesStr);
	System.out.println(flightEquipmentsStr);
	System.out.println(flightEqStr);
	System.out.println(operatorNamesStr);
	System.out.println(cleanAirlinesStr);
	System.out.println(flightNoStr);
	System.out.println(flightNumbersStr);
	//-------------------------------------------------
	if(airlinesStr.contains(airLineName)&& flightEquipmentsStr.contains(flightEqStr) && operatorNamesStr.contains(cleanAirlinesStr) && flightNoStr.contains(flightNumbersStr))
	{
		//&& flightNoStr.contains(flightNumbersStr)
		System.out.println("Pass");
	}
	//-------------------------------------------------------
//	System.out.println(flightEqStr);
//	System.out.println(flightEquipmentsStr);
//	if(flightEquipmentsStr.contains(flightEqStr))
//	{
//		System.out.println("Pass");
//	}
//	System.out.println(cleanAirlinesStr);
//	System.out.println(operatorNamesStr);
//	if(operatorNamesStr.contains(cleanAirlinesStr))
//	{
//		System.out.println("Pass");
//	}
//	System.out.println(flightDeptTime);
//	System.out.println(DepartTimeText);
	if(flightDeptTime.equals(DepartTimeText) && flightArrivalTime.equals(ArrivalTimeTextt) && flightDeptLocation2.equals(code1) && flightArrivalLocation2.equals(code2) && flightDeptDate.equals(departDateConcat))
	{
		System.out.println("Pass");
	}
//	System.out.println(flightArrivalTime);
//	System.out.println(ArrivalTimeTextt);
//	if(flightArrivalTime.equals(ArrivalTimeTextt))
//	{
//		System.out.println("Pass");
//	}
//	System.out.println(flightDeptLocation2);
//	System.out.println(code1);
//	if(flightDeptLocation2.equals(code1))
//	{
//		System.out.println("Pass");
//	}
//	System.out.println(flightArrivalLocation2);
//	System.out.println(code2);
//	if(flightArrivalLocation2.equals(code2))
//	{
//		System.out.println("Pass");
//	}
//	System.out.println(flightDeptDate);
//	System.out.println(departDateConcat);
//	if(flightDeptDate.equals(departDateConcat))
//	{
//		System.out.println("Pass");
//	}
}

public void clickOnViewFlight()
{
	driver.findElement(By.xpath("(//a[text()='Flight Details'])[1]")).click();
	

}
//public String[] validateTerminal() throws InterruptedException
//{
//	Thread.sleep(2000);
//WebElement departTerminal = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-origin']"));	
//String[] departTerminal1 = departTerminal.getText().split("-");
//String departTerminal2 = departTerminal1[1].trim();
//System.out.println(departTerminal2);
//
//WebElement arrivalTerminal = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-destination']"));	
//String[] arrivalTerminal1 = arrivalTerminal.getText().split("-");
//String arrivalTerminal2 = arrivalTerminal1[1].trim();
//System.out.println(arrivalTerminal2);
//
//clickOnViewFlight(); 
//Thread.sleep(1000);
// List<WebElement> departTerminalFlightDetail = driver.findElements(By.xpath("//span[@class='flgiht-depterminal']"));
// WebElement departTerminalFlightDetails = departTerminalFlightDetail.getFirst();
//String[] departTerminalFlightDetails1 = departTerminalFlightDetails.getText().split("-");
//String departTerminalFlightDetails2 = departTerminalFlightDetails1[1].trim();
//System.out.println(departTerminalFlightDetails2);
//
// List<WebElement> arrivalTerminalFlightDetail = driver.findElements(By.xpath("//span[@class='flgiht-arrterminal']"));
// WebElement arrivalTerminalFlightDetails = arrivalTerminalFlightDetail.getLast();
//String[] arrivalTerminalFlightDetails1 = arrivalTerminalFlightDetails.getText().split("-");
//String arrivalTerminalFlightDetails2 = arrivalTerminalFlightDetails1[1].trim();
//System.out.println(arrivalTerminalFlightDetails2);
//
// List<WebElement> getAllTheDepartTerminalInFlightDetails = driver.findElements(By.xpath("//span[@class='flgiht-depterminal']"));
// ArrayList<String> getAllDepartTerminal = new ArrayList<>();
// for(WebElement getAllTheDepartTerminalInFlightDetail:getAllTheDepartTerminalInFlightDetails)
// {
//	 String terminal = getAllTheDepartTerminalInFlightDetail.getText();
//	 String[] terminal1 = terminal.split("-");
//	 String terminal2 = terminal1[1].trim();
////span[@class="flgiht-depterminal"]
////span[@class="flgiht-arrterminal"]
//System.out.println(terminal2);
//	 getAllDepartTerminal.add(terminal2);
//	 
//}
// System.out.println(getAllDepartTerminal);
// List<WebElement> ArrivalTerminalInFlightDetails = driver.findElements(By.xpath("//span[@class='flgiht-arrterminal']"));
// ArrayList<String> getAllArrivalTerminal = new ArrayList<>();
// for(WebElement getAllTheArrivalTerminalInFlightDetail:ArrivalTerminalInFlightDetails)
// {
// 	 String terminal = getAllTheArrivalTerminalInFlightDetail.getText();
// 	 String[] terminal1 = terminal.split("-");
// 	 String terminal2 = terminal1[1].trim();
// //span[@class="flgiht-depterminal"]
// //span[@class="flgiht-arrterminal"]
// System.out.println(terminal2);
// 	 getAllArrivalTerminal.add(terminal2);
// 	 
// }
// String getAllDepartTerminal1 = String.join(", ", getAllDepartTerminal);
// String getAllArrivalTerminal1 = String.join(", ", getAllArrivalTerminal);
// System.out.println(getAllArrivalTerminal);
////button[@class="btn-close"]
// Thread.sleep(2000);
// driver.findElement(By.xpath("//button[@class='btn-close']")).click();
// return new  String[] {departTerminal2,arrivalTerminal2,departTerminalFlightDetails2,arrivalTerminalFlightDetails2,getAllDepartTerminal1,getAllArrivalTerminal1};
//
//}
public Map<String, String> validateTerminal() throws InterruptedException {
    Map<String, String> terminalData = new HashMap<>();

    Thread.sleep(2000);
    WebElement departTerminal = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-origin']"));
    String[] departTerminal1 = departTerminal.getText().split("-");
    String departTerminal2 = departTerminal1[1].trim();
    terminalData.put("departCard", departTerminal2);
    System.out.println(departTerminal2);

    WebElement arrivalTerminal = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-destination']"));
    String[] arrivalTerminal1 = arrivalTerminal.getText().split("-");
    String arrivalTerminal2 = arrivalTerminal1[1].trim();
    terminalData.put("arrivalCard", arrivalTerminal2);
    System.out.println(arrivalTerminal2);

    clickOnViewFlight();
    Thread.sleep(1000);

    List<WebElement> departTerminalFlightDetail = driver.findElements(By.xpath("//span[@class='flgiht-depterminal']"));
    WebElement departTerminalFlightDetails = departTerminalFlightDetail.get(0);
    String[] departTerminalFlightDetails1 = departTerminalFlightDetails.getText().split("-");
    String departTerminalFlightDetails2 = departTerminalFlightDetails1[1].trim();
    terminalData.put("departDetails", departTerminalFlightDetails2);
    System.out.println(departTerminalFlightDetails2);

    List<WebElement> arrivalTerminalFlightDetail = driver.findElements(By.xpath("//span[@class='flgiht-arrterminal']"));
    WebElement arrivalTerminalFlightDetails = arrivalTerminalFlightDetail.get(arrivalTerminalFlightDetail.size() - 1);
    String[] arrivalTerminalFlightDetails1 = arrivalTerminalFlightDetails.getText().split("-");
    String arrivalTerminalFlightDetails2 = arrivalTerminalFlightDetails1[1].trim();
    terminalData.put("arrivalDetails", arrivalTerminalFlightDetails2);
    System.out.println(arrivalTerminalFlightDetails2);

    List<WebElement> getAllDepartElements = driver.findElements(By.xpath("//span[@class='flgiht-depterminal']"));
    List<String> getAllDepartTerminal = new ArrayList<>();
    for (WebElement el : getAllDepartElements) {
        String[] parts = el.getText().split("-");
        getAllDepartTerminal.add(parts[1].trim());
    }
    String joinedDepart = String.join(", ", getAllDepartTerminal);
    terminalData.put("departAll", joinedDepart);
    System.out.println(joinedDepart);

    List<WebElement> getAllArrivalElements = driver.findElements(By.xpath("//span[@class='flgiht-arrterminal']"));
    List<String> getAllArrivalTerminal = new ArrayList<>();
    for (WebElement el : getAllArrivalElements) {
        String[] parts = el.getText().split("-");
        getAllArrivalTerminal.add(parts[1].trim());
    }
    String joinedArrival = String.join(", ", getAllArrivalTerminal);
    terminalData.put("arrivalAll", joinedArrival);
    System.out.println(joinedArrival);

    Thread.sleep(2000);
    driver.findElement(By.xpath("//button[@class='btn-close']")).click();

    return terminalData;
}

//public String[] validateTerminalInBookingPage() throws InterruptedException
//{
////	Thread.sleep(2000);
////WebElement departTerminal = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-origin']"));	
////String[] departTerminal1 = departTerminal.getText().split("-");
////String departTerminal2 = departTerminal1[1].trim();
////System.out.println(departTerminal2);
////
////WebElement arrivalTerminal = driver.findElement(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]//span[@class='flight-destination']"));	
////String[] arrivalTerminal1 = arrivalTerminal.getText().split("-");
////String arrivalTerminal2 = arrivalTerminal1[1].trim();
////System.out.println(arrivalTerminal2);
////
////clickOnViewFlight(); 
//Thread.sleep(1000);
// List<WebElement> departTerminalFlightDetailInBookingPage = driver.findElements(By.xpath("//span[@class='flgiht-depterminal']"));
// WebElement departTerminalFlightDetailsInBookingPage = departTerminalFlightDetailInBookingPage.getFirst();
//String[] departTerminalFlightDetails1InBookingPage = departTerminalFlightDetailsInBookingPage.getText().split("-");
//String departTerminalFlightDetails2InBookingPage = departTerminalFlightDetails1InBookingPage[1].trim();
//System.out.println(departTerminalFlightDetails2InBookingPage);
//
// List<WebElement> arrivalTerminalFlightDetailInBookingPage = driver.findElements(By.xpath("//span[@class='flgiht-arrterminal']"));
// WebElement arrivalTerminalFlightDetailsInBookingPage = arrivalTerminalFlightDetailInBookingPage.getLast();
//String[] arrivalTerminalFlightDetails1InBookingPage = arrivalTerminalFlightDetailsInBookingPage.getText().split("-");
//String arrivalTerminalFlightDetails2InBookingPage = arrivalTerminalFlightDetails1InBookingPage[1].trim();
//System.out.println(arrivalTerminalFlightDetails2InBookingPage);
//
// List<WebElement> getAllTheDepartTerminalInFlightDetailsInBookingPage = driver.findElements(By.xpath("//span[@class='flgiht-depterminal']"));
// ArrayList<String> getAllDepartTerminalInBookingPage = new ArrayList<>();
// for(WebElement getAllTheDepartTerminalInFlightDetailInBookingPage:getAllTheDepartTerminalInFlightDetailsInBookingPage)
// {
//	 String terminalInBookingPage = getAllTheDepartTerminalInFlightDetailInBookingPage.getText();
//	 String[] terminal1InBookingPage = terminalInBookingPage.split("-");
//	 String terminal2InBookingPage = terminal1InBookingPage[1].trim();
////span[@class="flgiht-depterminal"]
////span[@class="flgiht-arrterminal"]
//System.out.println(terminal2InBookingPage);
//	 getAllDepartTerminalInBookingPage.add(terminal2InBookingPage);
//	 
//}
// System.out.println(getAllDepartTerminalInBookingPage);
// List<WebElement> ArrivalTerminalInFlightDetailsInBookingPage = driver.findElements(By.xpath("//span[@class='flgiht-arrterminal']"));
// ArrayList<String> getAllArrivalTerminalInBookingPage = new ArrayList<>();
// for(WebElement getAllTheArrivalTerminalInFlightDetailInBookingPage:ArrivalTerminalInFlightDetailsInBookingPage)
// {
// 	 String terminalInBookingPage = getAllTheArrivalTerminalInFlightDetailInBookingPage.getText();
// 	 String[] terminal1InBookingPage = terminalInBookingPage.split("-");
// 	 String terminal2InBookingPage = terminal1InBookingPage[1].trim();
// //span[@class="flgiht-depterminal"]
// //span[@class="flgiht-arrterminal"]
// System.out.println(terminal2InBookingPage);
// 	 getAllArrivalTerminalInBookingPage.add(terminal2InBookingPage);
// 	 
// }
// String getAllDepartTerminal1InBookingPage = String.join(", ", getAllDepartTerminalInBookingPage);
// String getAllArrivalTerminal1InBookingPage = String.join(", ", getAllArrivalTerminalInBookingPage);
// System.out.println(getAllArrivalTerminalInBookingPage);
////button[@class="btn-close"]
// Thread.sleep(2000);
// //driver.findElement(By.xpath("//button[@class='btn-close']")).click();
// return new  String[] {departTerminalFlightDetails2InBookingPage,arrivalTerminalFlightDetails2InBookingPage,getAllDepartTerminal1InBookingPage,getAllArrivalTerminal1InBookingPage};
//
//}
public Map<String, String> validateTerminalInBookingPage() throws InterruptedException {
    Map<String, String> bookingData = new HashMap<>();

    Thread.sleep(1000);

    List<WebElement> departList = driver.findElements(By.xpath("//span[@class='flgiht-depterminal']"));
    WebElement departFirst = departList.get(0);
    String[] depSplit = departFirst.getText().split("-");
    String depTerminal = depSplit[1].trim();
    bookingData.put("departDetails", depTerminal);
    System.out.println(depTerminal);

    List<WebElement> arrivalList = driver.findElements(By.xpath("//span[@class='flgiht-arrterminal']"));
    WebElement arrivalLast = arrivalList.get(arrivalList.size() - 1);
    String[] arrSplit = arrivalLast.getText().split("-");
    String arrTerminal = arrSplit[1].trim();
    bookingData.put("arrivalDetails", arrTerminal);
    System.out.println(arrTerminal);

    List<String> allDepTerms = new ArrayList<>();
    for (WebElement el : departList) {
        String[] parts = el.getText().split("-");
        allDepTerms.add(parts[1].trim());
    }
    String depAll = String.join(", ", allDepTerms);
    bookingData.put("departAll", depAll);
    System.out.println(depAll);

    List<String> allArrTerms = new ArrayList<>();
    for (WebElement el : arrivalList) {
        String[] parts = el.getText().split("-");
        allArrTerms.add(parts[1].trim());
    }
    String arrAll = String.join(", ", allArrTerms);
    bookingData.put("arrivalAll", arrAll);
    System.out.println(arrAll);

    return bookingData;
}

//public void validateTerminalInResultPageAndBookingPage( String[] validateTerminalText,String[] validateTerminalText1)
//{
//	String getAllDepartTerminal1 = validateTerminalText[4];
//	System.out.println(getAllDepartTerminal1);
//	String getAllArrivalTerminal1 = validateTerminalText[5];
//	System.out.println(getAllArrivalTerminal1);
//
//	String getAllDepartTerminal1InBookingPage = validateTerminalText1[2];
//	System.out.println(getAllDepartTerminal1InBookingPage);
//	String getAllArrivalTerminal1InBookingPage = validateTerminalText1[3];
//	System.out.println(getAllArrivalTerminal1InBookingPage);
//System.out.println(getAllDepartTerminal1);
//System.out.println(getAllDepartTerminal1InBookingPage);
//System.out.println(getAllArrivalTerminal1);
//System.out.println(getAllArrivalTerminal1InBookingPage);
//
//	if(getAllDepartTerminal1.equals(getAllDepartTerminal1InBookingPage) && getAllArrivalTerminal1.equals(getAllArrivalTerminal1InBookingPage))
//	{
//	System.out.println("pass");	
//		
//		
//	}
//}
public void validateTerminalInResultPageAndBookingPage(Map<String, String> resultPageData, Map<String, String> bookingPageData, ExtentTest test) {
    String resultDepartAll = resultPageData.get("departAll");
    String resultArrivalAll = resultPageData.get("arrivalAll");
    String bookingDepartAll = bookingPageData.get("departAll");
    String bookingArrivalAll = bookingPageData.get("arrivalAll");

    test.log(Status.INFO, "Result Page Depart Terminals: " + resultDepartAll);
    test.log(Status.INFO, "Booking Page Depart Terminals: " + bookingDepartAll);
    test.log(Status.INFO, "Result Page Arrival Terminals: " + resultArrivalAll);
    test.log(Status.INFO, "Booking Page Arrival Terminals: " + bookingArrivalAll);

    try {
        Assert.assertEquals(resultDepartAll, bookingDepartAll, "Departure terminals mismatch between result and booking pages.");
        Assert.assertEquals(resultArrivalAll, bookingArrivalAll, "Arrival terminals mismatch between result and booking pages.");
        test.log(Status.PASS, "✅ Terminal validation passed between result and booking pages.");
    } catch (AssertionError e) {
        test.log(Status.FAIL, "❌ Terminal mismatch: " + e.getMessage());
        throw e;
    }
}


//public void validateTerminalInFlightCardAndFlightDetails( String[] validateTerminalText) throws InterruptedException
//{
//	Thread.sleep(2000);
//	String departTerminal2 = validateTerminalText[0];
//	System.out.println(departTerminal2);
//	String arrivalTerminal2 = validateTerminalText[1];
//	System.out.println(arrivalTerminal2);
//	String departTerminalFlightDetails2 = validateTerminalText[2];
//	System.out.println(departTerminalFlightDetails2);
//	String arrivalTerminalFlightDetails2 = validateTerminalText[3];
//	System.out.println(arrivalTerminalFlightDetails2);
//	String getAllDepartTerminal1 = validateTerminalText[4];
//	System.out.println(getAllDepartTerminal1);
//	String getAllArrivalTerminal1 = validateTerminalText[5];
//	System.out.println(getAllArrivalTerminal1);
//	
//	if(departTerminal2.equals(departTerminalFlightDetails2) && arrivalTerminal2.equals(arrivalTerminalFlightDetails2))
//	{
//		System.out.println("Pass");
//	}
//	
//}

public void validateTerminalInFlightCardAndFlightDetails(Map<String, String> terminalData, ExtentTest test) throws InterruptedException {
    Thread.sleep(2000);

    String departCard = terminalData.get("departCard");
    String arrivalCard = terminalData.get("arrivalCard");
    String departDetails = terminalData.get("departDetails");
    String arrivalDetails = terminalData.get("arrivalDetails");

    test.log(Status.INFO, "Depart Terminal (Card): " + departCard);
    test.log(Status.INFO, "Arrival Terminal (Card): " + arrivalCard);
    test.log(Status.INFO, "Depart Terminal (Details): " + departDetails);
    test.log(Status.INFO, "Arrival Terminal (Details): " + arrivalDetails);

    try {
        Assert.assertEquals(departCard, departDetails, "Departure terminal mismatch between card and flight details.");
        Assert.assertEquals(arrivalCard, arrivalDetails, "Arrival terminal mismatch between card and flight details.");
        test.log(Status.PASS, "✅ Terminal validation passed between flight card and flight details.");
    } catch (AssertionError e) {
        test.log(Status.FAIL, "❌ Terminal mismatch: " + e.getMessage());
        throw e;
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
               // areYouSurePopUp();
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
}



