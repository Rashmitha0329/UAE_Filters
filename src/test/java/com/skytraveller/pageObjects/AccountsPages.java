package com.skytraveller.pageObjects;

import java.time.Duration;
import java.util.Set;

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

public class AccountsPages extends BasePage {

	public AccountsPages(WebDriver driver) {
		super(driver);// calls BasePage constructor
	}
	
	public void accountsPageOption(String options, ExtentTest test) {
	    try {
	        
	        WebElement optionElement = driver.findElement(
	            By.xpath("//*[contains(@class,'accounts-page_horizontal-bar_option ')][text()='" + options + "']")
	        );

	      
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", optionElement);

	       
	        optionElement.click();

	        
	        test.log(Status.INFO, "Clicked on account page option: '" + options + "'");

	    } catch (Exception e) {
	       
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "AccountsPageOption", "Failed to click option: " + options);
	        test.log(Status.FAIL, "Failed to click on account page option: '" + options + "'. Exception: " + e.getMessage());
	        Assert.fail();
	    }
	}
	
	public void balanceUploadPageIsDisplayed(ExtentTest test) {
	    try {
	        WebElement balanceUploadPage = driver.findElement(By.xpath("//*[@class='balance-upload-page pb-4']"));

	        if (balanceUploadPage.isDisplayed()) {
	            test.log(Status.PASS, "Balance Upload Page is displayed successfully.");
	        } else {
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "BalanceUploadPage", "Page not visible");
	            test.log(Status.FAIL, "Balance Upload Page is not visible.");
	            Assert.fail("Balance Upload Page is not visible.");
	        }

	    } catch (NoSuchElementException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "BalanceUploadPage", "Element not found");
	        test.log(Status.FAIL, "Balance Upload Page element not found.");
	        Assert.fail("Balance Upload Page element not found.");
	    } catch (Exception e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "BalanceUploadPage", "Unexpected error");
	        test.log(Status.FAIL, "Unexpected error occurred: " + e.getMessage());
	        Assert.fail("Unexpected error occurred: " + e.getMessage());
	    }
	}

	public void selectPayment(String paymentOption, ExtentTest test) {
	    try {
	      
	        WebElement creditCardDropdown = driver.findElement(By.xpath("//*[text()='Credit Card']/parent::div"));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", creditCardDropdown);
	        creditCardDropdown.click();
	        test.log(Status.INFO, "Clicked on 'Credit Card' dropdown.");

	        
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement paymentOptionMenu = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@role='listbox']"))
	        );

	        if (paymentOptionMenu.isDisplayed()) {
	            WebElement optionToSelect = driver.findElement(By.xpath("//*[@role='option'][text()='" + paymentOption + "']"));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", optionToSelect);
	            optionToSelect.click();
	            test.log(Status.PASS, "Selected payment option: '" + paymentOption + "'.");
	        } else {
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "PaymentDropdown", "Payment dropdown not visible");
	            test.log(Status.FAIL, "Payment dropdown menu is not visible.");
	            Assert.fail("Payment dropdown menu is not visible.");
	        }

	    } catch (NoSuchElementException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "PaymentOption", "Element not found");
	        test.log(Status.FAIL, "Payment option element not found: '" + paymentOption + "'.");
	        Assert.fail("Payment option element not found: '" + paymentOption + "'.");
	    } catch (TimeoutException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "PaymentDropdown", "Dropdown timeout");
	        test.log(Status.FAIL, "Payment dropdown did not appear in time.");
	        Assert.fail("Payment dropdown did not appear in time.");
	    } catch (Exception e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "PaymentOption", "Unexpected error");
	        test.log(Status.FAIL, "Unexpected error occurred while selecting payment option: " + e.getMessage());
	        Assert.fail("Unexpected error occurred while selecting payment option: " + e.getMessage());
	    }
	}

	public void enterDepositAmount(String amount, ExtentTest test) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement depositAmountInput = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//*[text()='Deposit Amount']/following-sibling::input")
	            )
	        );

	        
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", depositAmountInput);

	       
	        depositAmountInput.clear();
	        depositAmountInput.sendKeys(amount);

	        test.log(Status.PASS, "Entered deposit amount: " + amount);

	    } catch (NoSuchElementException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "DepositAmount", "Element not found");
	        test.log(Status.FAIL, "Deposit amount input field not found.");
	        Assert.fail("Deposit amount input field not found.");
	    } catch (TimeoutException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "DepositAmount", "Input field timeout");
	        test.log(Status.FAIL, "Deposit amount input field did not appear in time.");
	        Assert.fail("Deposit amount input field did not appear in time.");
	    } catch (Exception e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "DepositAmount", "Unexpected error");
	        test.log(Status.FAIL, "Unexpected error occurred while entering deposit amount: " + e.getMessage());
	        Assert.fail("Unexpected error occurred while entering deposit amount: " + e.getMessage());
	    }
	}

	public void clickOnDepositeSubmitButton(ExtentTest test) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement submitButton = wait.until(
	            ExpectedConditions.elementToBeClickable(By.xpath("//*[text()='Submit']"))
	        );

	       

	        // Click the button
	        submitButton.click();
	        test.log(Status.PASS, "Clicked on the 'Submit' button.");

	    } catch (NoSuchElementException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "DepositSubmit", "Submit button not found");
	        test.log(Status.FAIL, "Submit button not found.");
	        Assert.fail("Submit button not found.");
	    } catch (TimeoutException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "DepositSubmit", "Submit button not clickable in time");
	        test.log(Status.FAIL, "Submit button did not become clickable in time.");
	        Assert.fail("Submit button did not become clickable in time.");
	    } catch (Exception e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "DepositSubmit", "Unexpected error");
	        test.log(Status.FAIL, "Unexpected error occurred while clicking 'Submit': " + e.getMessage());
	        Assert.fail("Unexpected error occurred while clicking 'Submit': " + e.getMessage());
	    }
	}

	public void validateDepositPopupAndPayMentGateWay(String amountEntered, ExtentTest test) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        // Wait for the deposit popup to be visible
	        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//*[@class='modal-content']")));

	        if (popup.isDisplayed()) {
	            test.log(Status.INFO, "Deposit popup appeared successfully.");

	            // Get the displayed amount text
	            String amountText = popup.findElement(By.xpath("//*[@class='fs-16 fw-600']")).getText();

	            // Normalize amounts (strip currency symbols and spaces)
	            String normalizedAmountEntered = amountEntered.replaceAll("[^0-9.]", "");
	            String normalizedAmountText = amountText.replaceAll("[^0-9.]", "");

	            if (normalizedAmountText.contains(normalizedAmountEntered)) {
	                test.log(Status.PASS, "Entered amount '" + amountEntered + "' is correctly displayed in deposit popup.");

	                // Scroll and click on "Proceed" button
	                WebElement proceedBtn = popup.findElement(By.xpath("//*[text()='Proceed']"));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", proceedBtn);
	                wait.until(ExpectedConditions.elementToBeClickable(proceedBtn)).click();
	                test.log(Status.INFO, "Clicked on 'Proceed' button in the deposit popup.");

	                // Save the current window handle
	                String originalWindow = driver.getWindowHandle();

	                // Wait for a new tab to open
	                wait.until(driver -> driver.getWindowHandles().size() > 1);

	                // Switch to the new tab
	                Set<String> allWindows = driver.getWindowHandles();
	                for (String windowHandle : allWindows) {
	                    if (!windowHandle.equals(originalWindow)) {
	                        driver.switchTo().window(windowHandle);
	                        break;
	                    }
	                }

	                // Wait for Payment Gateway Page to be visible
	                WebElement paymentGatewayPage = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath("//*[text()='Payment Information']")));

	                if (paymentGatewayPage.isDisplayed()) {
	                    String payAbleAmount = driver.findElement(
	                        By.xpath("//*[@class='orderTotal highlight-text']")).getText();

	                    String normalizedPayableAmount = payAbleAmount.replaceAll("[^0-9.]", "");

	                    if (normalizedPayableAmount.contains(normalizedAmountEntered)) {
	                        test.log(Status.PASS, "Payment gateway page is displayed and the amount is correct: " + amountEntered);
	                    } else {
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "PaymentGateway", "Amount mismatch");
	                        test.log(Status.FAIL, "Payment amount mismatch. Expected: '" + amountEntered + "', but got: '" + payAbleAmount + "'");
	                        Assert.fail("Payment amount mismatch.");
	                    }
	                }

	                // Optional: Close new tab and switch back to original
	                driver.close();
	                driver.switchTo().window(originalWindow);

	            } else {
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "DepositPopup", "Amount mismatch");
	                test.log(Status.FAIL, "Displayed amount does not match entered amount. Expected to contain: '" + amountEntered + "', but got: '" + amountText + "'");
	                Assert.fail("Deposit amount mismatch.");
	            }
	        }
	    } catch (TimeoutException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "DepositPopup", "Popup timeout");
	        test.log(Status.FAIL, "Deposit popup did not appear within the expected time.");
	        Assert.fail("Deposit popup timeout.");
	    } catch (NoSuchElementException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "DepositPopup", "Element not found");
	        test.log(Status.FAIL, "Expected element not found in deposit popup.");
	        Assert.fail("Element not found in deposit popup.");
	    } catch (Exception e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "DepositPopup", "Unexpected error");
	        test.log(Status.FAIL, "Unexpected error in depositPopup(): " + e.getMessage());
	        Assert.fail("Unexpected error in depositPopup(): " + e.getMessage());
	    }
	}


	
}
