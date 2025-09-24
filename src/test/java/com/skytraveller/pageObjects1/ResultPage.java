package com.skytraveller.pageObjects1;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class ResultPage extends BasePage{

	// Constructor of loginPage calls the BasePage constructor with driver
	public ResultPage(WebDriver driver) {
		super(driver);// calls BasePage constructor
	}
	
	// Method to verify Default Price Range in Price Slider
			 public double[] verifyDefaultPriceRangeinPriceSlider(ExtentTest test) {
			     try {
			         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	 
			         // Wait for the min and max slider input elements
			         WebElement minSliderInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
			                 By.xpath("//*[@class='thumb thumb-0 ' and @aria-valuenow]")));
			         WebElement maxSliderInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
			                 By.xpath("//*[@class='thumb thumb-1 ' and @aria-valuenow]")));
	 
			         // Get min and max values from sliders
			         double minValue = Double.parseDouble(minSliderInput.getAttribute("aria-valuenow"));
			         double maxValue = Double.parseDouble(maxSliderInput.getAttribute("aria-valuemax"));
	 
			         System.out.println("Min value: " + minValue);
			         System.out.println("Max value: " + maxValue);
	 
			         // Get all price elements
			         List<WebElement> priceElements = driver.findElements(
			                 By.xpath("//span[contains(@class,'flight-totalfq')]"));
	 
			         if (minValue >= 0 && maxValue > minValue) {
			             String message = "PASS: Default price range displayed is: Min = " + minValue + ", Max = " + maxValue;
			             test.log(Status.PASS, message);
	 
			             // Loop through each price element
			             for (WebElement price : priceElements) {
			                 String priceText = price.getText().replaceAll("[^\\d.]", ""); // Remove currency symbols
			                 try {
			                     double priceValue = Double.parseDouble(priceText);
			                     if (priceValue >= minValue && priceValue <= maxValue) {
			                         System.out.println("✅ Price within range: " + priceValue);
			                         test.log(Status.INFO, "Price within range: " + priceValue);
			                     } else {
			                         System.out.println("❌ Price out of range: " + priceValue);
			                         test.log(Status.WARNING, "Price out of range: " + priceValue);
			                     }
			                 } catch (NumberFormatException ex) {
			                     System.out.println("⚠️ Skipping invalid price format: " + priceText);
			                     test.log(Status.WARNING, "Skipping invalid price format: " + priceText);
			                 }
			             }
			         } else {
			             String message = "FAIL: Default price range is invalid. Min = " + minValue + ", Max = " + maxValue;
			             test.log(Status.FAIL, message);
			             ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "", "");
			             Assert.fail(message);
			         }
	 
			         return new double[]{minValue, maxValue};
	 
			     } catch (Exception e) {
			         e.printStackTrace();
			         test.log(Status.FAIL, "Exception during price slider validation: " + e.getMessage());
			         ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "", "");
			         Assert.fail("Exception occurred during price range validation");
			         return new double[0]; // fallback
			     }
			 }
			 /*
			 public double[] adjustMinimumSliderToValue(WebDriver driver, double targetValue) {
				    double minValue = -1;
				    double maxValue = -1;
	 
				    try {
				        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	 
				        WebElement minSlider = wait.until(ExpectedConditions.visibilityOfElementLocated(
				                By.xpath("//div[@class='thumb thumb-0 ']")));
				        WebElement maxSlider = wait.until(ExpectedConditions.visibilityOfElementLocated(
				                By.xpath("//div[@class='thumb thumb-1 ']")));
	 
				        minValue = Double.parseDouble(minSlider.getAttribute("aria-valuenow"));
				        maxValue = Double.parseDouble(maxSlider.getAttribute("aria-valuenow"));
	 
				        double sliderMin = Double.parseDouble(minSlider.getAttribute("aria-valuemin"));
				        double sliderMax = Double.parseDouble(maxSlider.getAttribute("aria-valuemax"));
	 
				        // Clamp the target value
				        double clampedValue = Math.max(sliderMin, Math.min(sliderMax, targetValue));
				        double percentage = (clampedValue - sliderMin) / (sliderMax - sliderMin);
	 
				        WebElement sliderContainer = driver.findElement(By.xpath("//div[@class='slider']"));
				        int sliderWidth = sliderContainer.getSize().getWidth();
				        int targetOffset = (int) (sliderWidth * percentage);
	 
				        int sliderX = sliderContainer.getLocation().getX();
				        int thumbX = minSlider.getLocation().getX();
				        int currentOffset = thumbX - sliderX;
				        int moveBy = targetOffset - currentOffset;
	 
				        Actions action = new Actions(driver);
				        action.moveToElement(minSlider)
				              .clickAndHold()
				              .moveByOffset(moveBy, 0)
				              .pause(Duration.ofMillis(200))
				              .release()
				              .perform();
	 
				        Thread.sleep(1000); // Optional wait for UI update
	 
				        String updatedMin = minSlider.getAttribute("aria-valuenow");
				        System.out.println("✅ Updated Min Value: " + updatedMin);
	 
				    } catch (Exception e) {
				        System.err.println("❌ Error adjusting min slider: " + e.getMessage());
				        e.printStackTrace();
				    }
	 
				    return new double[]{minValue, maxValue};
				}
				*/
			 /*
			 public double[] adjustMinimumSliderToValue(WebDriver driver, double targetValue) {
				    double minValue = -1;
				    double maxValue = -1;

				    try {
				        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

				        WebElement sliderTrack = wait.until(ExpectedConditions.visibilityOfElementLocated(
				                By.xpath("//div[@class='slider']")));

				        WebElement minSlider = wait.until(ExpectedConditions.visibilityOfElementLocated(
				                By.xpath("//div[@class='thumb thumb-0 ']")));
				        WebElement maxSlider = wait.until(ExpectedConditions.visibilityOfElementLocated(
				                By.xpath("//div[@class='thumb thumb-1 ']")));

				        double sliderMin = Double.parseDouble(minSlider.getAttribute("aria-valuemin"));
				        double sliderMax = Double.parseDouble(maxSlider.getAttribute("aria-valuemax"));

				        minValue = Double.parseDouble(minSlider.getAttribute("aria-valuenow"));
				        maxValue = Double.parseDouble(maxSlider.getAttribute("aria-valuenow"));

				        double clampedValue = Math.max(sliderMin, Math.min(sliderMax, targetValue));
				        System.out.println("🎯 Clamped Target: " + clampedValue);

				        if (clampedValue == minValue) {
				            System.out.println("ℹ️ No movement needed.");
				            return new double[]{minValue, maxValue};
				        }

				        // Scroll into view
				        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", minSlider);
				        Thread.sleep(300);

				        // Calculate more precise moveBy
				        int sliderWidth = sliderTrack.getSize().getWidth();
				        double valueRange = sliderMax - sliderMin;
				        double pixelPerUnit = sliderWidth / valueRange;

				        int moveBy = (int) ((clampedValue - minValue) * pixelPerUnit);
				        System.out.println("📐 Calculated offset: " + moveBy + "px");

				        // Perform move
				        Actions action = new Actions(driver);
				        action.clickAndHold(minSlider)
				              .moveByOffset(moveBy, 0)
				              .pause(Duration.ofMillis(200))
				              .release()
				              .perform();

				        Thread.sleep(1000); // wait for UI update

				        // Re-locate slider after potential DOM update
				        WebElement updatedMinSlider = wait.until(ExpectedConditions.visibilityOfElementLocated(
				                By.xpath("//div[@class='thumb thumb-0 ']")));
				        String updatedMin = updatedMinSlider.getAttribute("aria-valuenow");
				        System.out.println("✅ Updated Min Value: " + updatedMin);

				    } catch (Exception e) {
				        System.err.println("❌ Error adjusting min slider: " + e.getMessage());
				        e.printStackTrace();
				    }

				    return new double[]{minValue, maxValue};
				}
*/
			 
			 public double[] adjustMinimumSliderToValue(WebDriver driver, double targetValue) {
				    double minValue = -1;
				    double maxValue = -1;

				    try {
				        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

				        // Get slider container
				        WebElement sliderTrack = wait.until(ExpectedConditions.visibilityOfElementLocated(
				                By.xpath("//div[@class='slider']")));

				        // Get min and max slider thumbs
				        WebElement minSlider = wait.until(ExpectedConditions.visibilityOfElementLocated(
				                By.xpath("//div[@class='thumb thumb-0 ']")));
				        WebElement maxSlider = wait.until(ExpectedConditions.visibilityOfElementLocated(
				                By.xpath("//div[@class='thumb thumb-1 ']")));

				        // Read initial values
				        double sliderMin = Double.parseDouble(minSlider.getAttribute("aria-valuemin"));
				        double sliderMax = Double.parseDouble(maxSlider.getAttribute("aria-valuemax"));

				        minValue = Double.parseDouble(minSlider.getAttribute("aria-valuenow"));
				        maxValue = Double.parseDouble(maxSlider.getAttribute("aria-valuenow"));

				        // Clamp the target value to slider range
				        double clampedValue = Math.max(sliderMin, Math.min(sliderMax, targetValue));
				        System.out.println("🎯 Clamped Target: " + clampedValue);

				        // If already at target, no need to move
				        if (clampedValue == minValue) {
				            System.out.println("ℹ️ Slider is already at the target value.");
				            return new double[]{minValue, maxValue};
				        }

				        // Scroll slider into view (in case it's off screen)
				        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", minSlider);
				        Thread.sleep(300);

				        // Calculate how many pixels to move
				        int sliderWidth = sliderTrack.getSize().getWidth();
				        double valueRange = sliderMax - sliderMin;
				        double pixelPerUnit = sliderWidth / valueRange;

				        int moveBy = (int) ((clampedValue - minValue) * pixelPerUnit);
				        System.out.println("📐 Calculated offset: " + moveBy + "px");

				        // Drag the slider
				        Actions action = new Actions(driver);
				        action.clickAndHold(minSlider)
				              .moveByOffset(moveBy, 0)
				              .pause(Duration.ofMillis(200))
				              .release()
				              .perform();

				        // Wait for UI update
				        Thread.sleep(1000);

				        // Re-locate the slider after move (DOM may have changed)
				        WebElement updatedMinSlider = wait.until(ExpectedConditions.visibilityOfElementLocated(
				                By.xpath("//div[@class='thumb thumb-0 ']")));
				        String updatedMin = updatedMinSlider.getAttribute("aria-valuenow");

				        System.out.println("✅ Updated Min Value: " + updatedMin);

				    } catch (Exception e) {
				        System.err.println("❌ Error adjusting min slider: " + e.getMessage());
				        e.printStackTrace();
				    }

				    return new double[]{minValue, maxValue};
				}


			 public double[] adjustMaximumSliderToValue(WebDriver driver, double targetValue) {
				    double minValue = -1;
				    double maxValue = -1;
	 
				    try {
				        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	 
				        WebElement minSlider = wait.until(ExpectedConditions.visibilityOfElementLocated(
				                By.xpath("//div[@class='thumb thumb-0 ']")));
				        WebElement maxSlider = wait.until(ExpectedConditions.visibilityOfElementLocated(
				                By.xpath("//div[@class='thumb thumb-1 ']")));
	 
				        minValue = Double.parseDouble(minSlider.getAttribute("aria-valuenow"));
				        maxValue = Double.parseDouble(maxSlider.getAttribute("aria-valuenow"));
	 
				        double sliderMin = Double.parseDouble(minSlider.getAttribute("aria-valuemin"));
				        double sliderMax = Double.parseDouble(maxSlider.getAttribute("aria-valuemax"));
	 
				        // Clamp the target value
				        double clampedValue = Math.max(sliderMin, Math.min(sliderMax, targetValue));
				        double percentage = (clampedValue - sliderMin) / (sliderMax - sliderMin);
	 
				        WebElement sliderContainer = driver.findElement(By.xpath("//div[@class='slider']"));
				        int sliderWidth = sliderContainer.getSize().getWidth();
				        int targetOffset = (int) (sliderWidth * percentage);
	 
				        int sliderX = sliderContainer.getLocation().getX();
				        int thumbX = maxSlider.getLocation().getX();
				        int currentOffset = thumbX - sliderX;
				        int moveBy = targetOffset - currentOffset;
	 
				        Actions action = new Actions(driver);
				        action.moveToElement(maxSlider)
				              .clickAndHold()
				              .moveByOffset(moveBy, 0)
				              .pause(Duration.ofMillis(200))
				              .release()
				              .perform();
	 
				        Thread.sleep(1000); // Optional wait for UI update
	 
				        String updatedMax = maxSlider.getAttribute("aria-valuenow");
				        System.out.println("✅ Updated Max Value: " + updatedMax);
	 
				    } catch (Exception e) {
				        System.err.println("❌ Error adjusting max slider: " + e.getMessage());
				        e.printStackTrace();
				    }
	 
				    return new double[]{minValue, maxValue};
				}
			 public void waitForProgressToComplete() {
				    
				    int timeoutInSeconds = 120;
				    int pollIntervalInMillis = 500; // Poll every 500ms
				    int elapsedTime = 0;
	 
				    while (elapsedTime < timeoutInSeconds * 1000) {
				        try {
				            List<WebElement> progressElements = driver.findElements(By.cssSelector("div[role='progressbar']"));
	 
				            // If progress element is no longer in the DOM, assume it is complete
				            if (progressElements.isEmpty()) {
				                break;
				            }
	 
				            WebElement progress = progressElements.get(0);
				            String valueStr = progress.getAttribute("aria-valuenow");
				            
				            if (valueStr != null) {
				                try {
				                    int value = Integer.parseInt(valueStr);
				                    if (value >= 100) {
				                        // Wait a bit to ensure DOM update (if element disappears after 100%)
				                        Thread.sleep(300);
				                        if (driver.findElements(By.cssSelector("div[role='progressbar']")).isEmpty()) {
				                            break;
				                        }
				                    }
				                } catch (NumberFormatException e) {
				                    // Not a number, continue polling
				                }
				            }
	 
				        } catch (StaleElementReferenceException e) {
				            // Element was removed after access - treat as done
				            break;
				        } catch (InterruptedException e) {
				            Thread.currentThread().interrupt();
				            break;
				        }
	 
				        try {
				            Thread.sleep(pollIntervalInMillis);
				        } catch (InterruptedException e) {
				            Thread.currentThread().interrupt();
				            break;
				        }
	 
				        elapsedTime += pollIntervalInMillis;
				    }
				    System.out.println("Progress bar is completed");
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
			 
	 
}
