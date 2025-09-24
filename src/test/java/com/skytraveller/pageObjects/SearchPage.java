package com.skytraveller.pageObjects;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
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
	
	@FindBy(xpath = "//img[contains(@src, 'ST33341.gif') and contains(@class, 'logo-width')]")
	WebElement imageLogo;
	
	@FindBy(xpath ="//*[contains(@class,'fs-12 d-mobile-none nav-item dropdown')]")
	WebElement profileDropdown;
	
	@FindBy(id="prefclass")
	WebElement classDropdown;
	
	@FindBy(xpath = "(//div[@class='react-datepicker__input-container'])[2]")
	WebElement datePickerInputReturn;
	
	@FindBy(xpath = "(//div[@class='react-datepicker__current-month'])[1]")
	WebElement dateReturn;

//	public void validateLogoInHomePage(ExtentTest test) {
//	    try {
//	        // Wait for the logo to be visible
//	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//	        wait.until(ExpectedConditions.visibilityOf(imageLogo));
//
//	        // Check if displayed
//	        boolean isDisplayed = imageLogo.isDisplayed();
//	        test.log(Status.INFO, "Logo displayed: " + isDisplayed);
//
//	        // Check if loaded using JS
//	        JavascriptExecutor js = (JavascriptExecutor) driver;
//	        boolean isLoaded = (Boolean) js.executeScript(
//	                "return arguments[0].complete && " +
//	                        "typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0", imageLogo);
//	        test.log(Status.INFO, "Logo loaded: " + isLoaded);
//
//	        // Check HTTP status
//	        String imageUrl = imageLogo.getAttribute("src");
//	        int statusCode = getImageHttpStatus(imageUrl);
//	        test.log(Status.INFO, "HTTP Response Code: " + statusCode);
//
//	        // Check size
//	        Dimension size = imageLogo.getSize();
//	        boolean hasValidSize = size.getHeight() > 0 && size.getWidth() > 0;
//	        test.log(Status.INFO, "Image Size: " + size + " → Valid size: " + hasValidSize);
//
//	        // Final validation
//	        if (isDisplayed && isLoaded && statusCode == 200 && hasValidSize) {
//	            test.log(Status.PASS, "✅ Logo is valid and fully loaded on the Home Page.");
//	        } else {
//	            test.log(Status.FAIL, "❌ Logo failed validation.");
//	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
//	                    "Logo failed one or more checks", "LogoValidationFailed");
//	        }
//
//	    } catch (Exception e) {
//	        test.log(Status.FAIL, "❌ Exception occurred while validating logo: " + e.getMessage());
//	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
//	                "Exception during logo validation", "LogoValidationException");
//	    }
//	}
//	public static int getImageHttpStatus(String imageUrl) {
//	    try {
//	        URL url = new URL(imageUrl);
//	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//	        connection.setRequestMethod("GET");
//	        connection.connect();
//	        return connection.getResponseCode();
//	    } catch (Exception e) {
//	        System.out.println("❌ Error checking image HTTP status: " + e.getMessage());
//	        return -1;
//	    }
//	}


	
	
	
	
	
	
	//Method to enter From Location
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
						Thread.sleep(500);
						date.getText();
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
			     //   JavascriptExecutor js = (JavascriptExecutor) driver;
			     //   js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

			        Select select = new Select(classDropdown);
			        select.selectByVisibleText(travelClass);
			    } catch (Exception e) {
			        System.out.println("Error in selectTravelClass(): " + e.getMessage());
			    }
			}

			public void searchFightsOnHomePage(String from, String to, String day, String MonthandYear, String adult, String child, String infant,String travelClass) {
			    try {
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
			        Thread.sleep(1000);
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
			        WebElement searchBtn = driver.findElement(By.xpath("//button[text()='Search Flights']"));
			        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", searchBtn);
			        Thread.sleep(300);
			        searchBtn.click();
			        System.out.println("✅ Clicked on 'Search Flights' button.");
			        test.log(Status.PASS, "✅ Clicked on 'Search Flights' button.");
			        areYouSurePopUp();
			    } catch (Exception e) {
			        System.out.println("❌ Failed to click 'Search Flights' button: " + e.getMessage());
			        test.log(Status.FAIL, "❌ Failed to click 'Search Flights' button: " + e.getMessage());
			        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Search Click Failure", "SearchButtonClickException");
			        Assert.fail();
			    }
			}

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


			
			/*
			public String validateLogo() {
			    String dimensions = "";
			    try {
			        // 1. Open a new tab with the image URL
			        String imageUrl = "https://uae.skytravellers.com/printlogos/ST33341.gif"; // your updated image URL

			        // Save current tab handle
			        String originalTab = driver.getWindowHandle();

			        // Open new tab via JS and switch to it
			        ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", imageUrl);

			        // Switch to the new tab
			        for (String handle : driver.getWindowHandles()) {
			            if (!handle.equals(originalTab)) {
			                driver.switchTo().window(handle);
			                break;
			            }
			        }

			        // 2. Wait for the page to load and title to be present
			        new WebDriverWait(driver, Duration.ofSeconds(10))
			            .until(ExpectedConditions.titleContains(".gif"));

			        // 3. Capture the title (which contains the filename and dimensions)
			        String title = driver.getTitle();
			        System.out.println("Page Title: " + title);

			        // 4. Extract dimensions from title using regex
			        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\((\\d+×\\d+)\\)");
			        java.util.regex.Matcher matcher = pattern.matcher(title);
			        if (matcher.find()) {
			            dimensions = matcher.group(1);
			        }
			        System.out.println("Extracted dimensions: " + dimensions);

			        // 5. Close the new tab and switch back to original
			        driver.close();
			        driver.switchTo().window(originalTab);

			    } catch (Exception e) {
			        System.out.println("Exception in validateLogo: " + e.getMessage());
			        // Optionally: close tab if still open and switch back
			        try {
			            driver.close();
			            for (String handle : driver.getWindowHandles()) {
			                driver.switchTo().window(handle);
			                break;
			            }
			        } catch (Exception ignore) {}
			    }

			    return dimensions;
			}
			*/
			public Dimension validateLogo() {
			    Dimension dimensions = new Dimension(0, 0); // default if not found
			    try {
			        // 1. Open a new tab with the image URL
			        String imageUrl = "https://uae.skytravellers.com/printlogos/ST33341.gif";

			        // Save current tab handle
			        String originalTab = driver.getWindowHandle();

			        // Open new tab via JS and switch to it
			        ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", imageUrl);

			        // Switch to the new tab
			        for (String handle : driver.getWindowHandles()) {
			            if (!handle.equals(originalTab)) {
			                driver.switchTo().window(handle);
			                break;
			            }
			        }

			        // 2. Wait for the page to load and title to be present
			        new WebDriverWait(driver, Duration.ofSeconds(10))
			            .until(ExpectedConditions.titleContains(".gif"));

			        // 3. Capture the title (which contains the filename and dimensions)
			        String title = driver.getTitle();
			        System.out.println("Page Title: " + title);

			        // 4. Extract dimensions from title using regex: e.g., (1905×994)
			        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\((\\d+)×(\\d+)\\)");
			        java.util.regex.Matcher matcher = pattern.matcher(title);
			        if (matcher.find()) {
			            int width = Integer.parseInt(matcher.group(1));
			            int height = Integer.parseInt(matcher.group(2));
			            dimensions = new Dimension(width, height);
			        }

			        System.out.println("Extracted dimensions: " + dimensions.getWidth() + " × " + dimensions.getHeight());

			        // 5. Close the new tab and switch back to original
			        driver.close();
			        driver.switchTo().window(originalTab);

			    } catch (Exception e) {
			        System.out.println("Exception in validateLogo: " + e.getMessage());
			        try {
			            driver.close();
			            driver.switchTo().window(driver.getWindowHandles().iterator().next());
			        } catch (Exception ignore) {}
			    }

			    return dimensions;
			}
			public void validateLogoInHomePage(WebDriver driver, String imageXpath, int expectedWidth, int expectedHeight, ExtentTest test) {
			    try {
			        WebElement image = driver.findElement(By.xpath(imageXpath));
			        JavascriptExecutor js = (JavascriptExecutor) driver;

			        Long naturalWidth = (Long) js.executeScript("return arguments[0].naturalWidth;", image);
			        Long naturalHeight = (Long) js.executeScript("return arguments[0].naturalHeight;", image);

			        System.out.println("Intrinsic size: " + naturalWidth + " × " + naturalHeight);

			        if (naturalWidth == expectedWidth && naturalHeight == expectedHeight) {
			            System.out.println("✅ Image intrinsic size is valid in Home Page");
			            test.log(Status.PASS, "✅ Image is uploaded and intrinsic size is valid in Home Page: "
			                    + naturalWidth + "×" + naturalHeight);
			        } else {
			            System.out.println("❌ Image intrinsic size is NOT valid In Home Page");
			            test.log(Status.FAIL, "❌ Image is uploaded but intrinsic size is invalid in Home Page. "
			                    + "Expected: " + expectedWidth + "×" + expectedHeight +
			                    ", Found: " + naturalWidth + "×" + naturalHeight);
			            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
			                    "Intrinsic size mismatch in Home Page", "ImageIntrinsicSizeInvalidInHomePage");
			            Assert.fail();
			        }
			    } catch (Exception e) {
			        System.out.println("❌ Exception in validateImageIntrinsicSize: " + e.getMessage());
			        test.log(Status.FAIL, "❌ Exception during intrinsic image validation in Home Page: " + e.getMessage());
			        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
			                "Exception in image validation In HomePage", "ImageIntrinsicSizeExceptionInHomePage");
			        Assert.fail();
			    }
			}


			public void profileDropDown(String option,ExtentTest test) {
			    try {
			        profileDropdown.click();
			        
			        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			        WebElement dropDownMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
			                By.xpath("//*[contains(@class,'dropdown-menu show dropdown-menu-end')]")));

			        if (dropDownMenu.isDisplayed()) {
			            WebElement targetOption = driver.findElement(By.xpath(
			                    "//*[@data-rr-ui-dropdown-item][normalize-space(text())='" + option + "']"));
			            targetOption.click();
			            System.out.println("✅ Clicked on option: " + option);
			        } else {
			            System.out.println("❌ Dropdown menu not visible.");
			            Assert.fail();
			        }

			    } catch (Exception e) {
			        System.out.println("❌ Exception in profileDropDown: " + e.getMessage());
			      
			         ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Dropdown Failure", "DropdownException");
			         Assert.fail();
			    }
			}

			public void validateHomePageIsDisplayed(ExtentTest test) {
			    try {
			        WebElement homePageIcon = driver.findElement(By.xpath("//*[@class='landing-page_search_container_header']"));
			        
			        if (homePageIcon.isDisplayed()) {
			            test.log(Status.PASS, "Home page is displayed.");
			        } else {
			            test.log(Status.FAIL, "Home page is not displayed.");
			            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "HomePage Failure", "HomePageNotVisible");
			            Assert.fail();
			        }
			    } catch (NoSuchElementException e) {
			        test.log(Status.FAIL, "Home page element not found.");
			        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "HomePage Failure", "ElementNotFound");
			        Assert.fail();
			    } catch (Exception e) {
			        test.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
			        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "HomePage Failure", "UnexpectedException");
			        Assert.fail();
			    }
			}
			
			public void ValidateDefaultSecter(ExtentTest test)
			{
			    try {
			        String onwardLocation = driver.findElement(By.xpath("(//*[@class='d-flex flex-column mt-1']/span)[1]")).getText();
			        String onwardSecterCode = driver.findElement(By.xpath("(//*[@class='d-flex flex-column mt-1']/span)[2]")).getText();
			        String returnLocation = driver.findElement(By.xpath("(//*[@class='d-flex flex-column mt-1']/span)[3]")).getText();
			        String returnSecterCode = driver.findElement(By.xpath("(//*[@class='d-flex flex-column mt-1']/span)[4]")).getText();

			        boolean allFieldsSelected = true;

			       
			        if (onwardLocation.isEmpty()) {
			            test.log(Status.INFO, "onwardLocation: [empty]");
			            allFieldsSelected = false;
			        } else {
			            test.log(Status.INFO, "onwardLocation selected by default: " + onwardLocation);
			        }

			        if (onwardSecterCode.isEmpty()) {
			            test.log(Status.INFO, "onwardSecterCode: [empty]");
			            allFieldsSelected = false;
			        } else {
			            test.log(Status.INFO, "onwardSecterCode selected by default: " + onwardSecterCode);
			        }

			        if (returnLocation.isEmpty()) {
			            test.log(Status.INFO, "returnLocation: [empty]");
			            allFieldsSelected = false;
			        } else {
			            test.log(Status.INFO, "returnLocation selected by default: " + returnLocation);
			        }

			        if (returnSecterCode.isEmpty()) {
			            test.log(Status.INFO, "returnSecterCode: [empty]");
			            allFieldsSelected = false;
			        } else {
			            test.log(Status.INFO, "returnSecterCode selected by default: " + returnSecterCode);
			        }

			      
			        if (allFieldsSelected) {
			            test.log(Status.PASS, "All sector fields are selected by default as expected.");
			        } else {
			            test.log(Status.FAIL, "One or more sector fields are empty. They should be selected by default.");
			            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "HomePage Failure", "Empty fields found");
			            Assert.fail("Some sector fields are empty but should be selected by default.");
			        }

			    } catch (Exception e) {
			        test.log(Status.FAIL, "Exception occurred while validating sector fields: " + e.getMessage());
			        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception", "Error during field validation");
			        Assert.fail("Exception in ValidateDefaultSecter: " + e.getMessage());
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
				System.out.println(Date);
				System.out.println(MonthandYear);
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
						Thread.sleep(500);
						 Date = dateReturn.getText(); 
						if(Date.contentEquals(MonthandYear))
						{
							driver.findElement(By.xpath("(//div[@class='react-datepicker__month-container'])[1]//div[text()='"+day+"' and @aria-disabled='false']")).click();
							break;
							//MonthYear
						}
		 
					}
				}
			}
			
			/*
			public void selectDateReturn(String day, String MonthandYear) throws InterruptedException {
			    JavascriptExecutor js = (JavascriptExecutor) driver;
			    js.executeScript("document.body.style.zoom='80%'");

			    datePickerInputReturn.click();

			    String Date = dateReturn.getText();  
			    System.out.println("Initial Date: " + Date);
			    System.out.println("Target Month and Year: " + MonthandYear);

			    if (Date.contentEquals(MonthandYear)) {
			        Thread.sleep(1000);
			        driver.findElement(By.xpath("(//div[@class='react-datepicker__month-container'])[1]//div[text()='" + day + "' and @aria-disabled='false']")).click();
			    } else {
			        int maxTries = 12; // Avoid infinite loop – maximum 12 months ahead
			        int tries = 0;

			        while (!Date.contentEquals(MonthandYear) && tries < maxTries) {
			            Thread.sleep(500);
			            nextMonth.click();
			            Thread.sleep(500);  
			            Date = dateReturn.getText();  
			            System.out.println("Updated Date: " + Date);
			            tries++;
			        }

			        if (Date.contentEquals(MonthandYear)) {
			            driver.findElement(By.xpath("(//div[@class='react-datepicker__month-container'])[1]//div[text()='" + day + "' and @aria-disabled='false']")).click();
			        } else {
			            System.out.println("❌ Target month/year not found after " + maxTries + " tries.");
			        }
			    }
			}
*/
			public void searchFightsOnHomePage(String from, String to, String day, String MonthandYear,String dayReturn,String MonthandYearReturn, String adult, String child, String infant,String travelClass) {
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
					 selectTravelClass(travelClass);
					 Thread.sleep(1000);
					doneButton.click();
		 
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt(); // Best practice to reset the interruption status
					System.out.println("Interrupted while searching flights on home page: " + e.getMessage());
				} catch (Exception e) {
					System.out.println("Error in searchFightsOnHomePage(): " + e.getMessage());
				}
			}
			public void ModifySearch(String from, String to, String day, String MonthandYear,String dayReturn,String MonthandYearReturn, String adult, String child, String infant,String travelClass) {
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
				} catch (Exception e) {
					System.out.println("Error in searchFightsOnHomePage(): " + e.getMessage());
				}
			}
}
