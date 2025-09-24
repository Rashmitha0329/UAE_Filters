package com.skytraveller.pageObjects;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class loginPage extends BasePage{

	// Constructor of loginPage calls the BasePage constructor with driver
	public loginPage(WebDriver driver) {
		super(driver);// calls BasePage constructor
	}

	


	@FindBy(xpath="//*[text()='User ID']/following-sibling::input")
	WebElement userName;


	@FindBy(xpath="//*[text()='Password']/following-sibling::input")
	WebElement password;

	@FindBy(xpath="//button[text()='Sign In']")
	WebElement submit;

	@FindBy(xpath="//img[@src='/images/logos/app_logo.png']")
	WebElement imageLogo;
	
	public void validateLogoInLoginPage(ExtentTest test) {
		try {
			if (imageLogo.isDisplayed()) {
				test.log(Status.PASS, "Image is displayed on the Login Page");
			} else {
				test.log(Status.FAIL, "Image is not displayed on the Login Page");
				ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, 
						"Image is not displayed", "LogoImageIsBroken");
			}
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred while validating logo: " + e.getMessage());
			ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, 
					"Exception while validating logo", "LogoValidationException");
			 Assert.fail();
		}
	}
	
	//*[text()='Welcome']

	public void UserLogin(String uName, String pwd) throws InterruptedException
	{
		try
		{
		System.out.println(uName);
		System.out.println(pwd);
		Thread.sleep(2000);
		userName.sendKeys(uName);
		password.sendKeys(pwd);
		//submit.click();
		}
		catch(Exception e)
		{
			
		  e.printStackTrace();
		  Assert.fail();
		}
 
	} 

 public void clickOnSubmitButton()
 {
	 submit.click();
 }

 public void validateLoginPageIsDisplayed(ExtentTest test) {
	    try {
	        WebElement LoginPage = driver.findElement(By.xpath("//*[text()='Welcome']"));
	        
	        if (LoginPage.isDisplayed()) {
	            test.log(Status.PASS, "Login page is displayed.");
	        } else {
	            test.log(Status.FAIL, "Login page is not displayed.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "LoginPage Failure", "loginPageNotVisible");
	            Assert.fail();
	        }
	    } catch (NoSuchElementException e) {
	        test.log(Status.FAIL, "Login page element not found.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "LoginPage Failure", "ElementNotFound");
	        Assert.fail();
	    } catch (Exception e) {
	        test.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "LoginPage Failure", "UnexpectedException");
	        Assert.fail();
	    }
	}

 public void footerDetails(ExtentTest test) {
     //  Check for footer logo
     try {
         WebElement footerLogo = driver.findElement(By.xpath("//*[text()='© 2024 Sky travellers. All brands are trademarks of their respective owners.']"));
         if (footerLogo.isDisplayed()) {
             test.log(Status.PASS, "Footer copyright text is displayed.");
         } else {
             test.log(Status.FAIL, "Footer copyright text is not displayed.");
         }
     } catch (NoSuchElementException e) {
         test.log(Status.FAIL, "Footer logo element not found.");
     }

     //  Check footer links
     List<String> expectedFooterItems = Arrays.asList(
             "Privacy Policy",
             "Terms & Conditions",
             "About us",
             "Contact us"
     );

     List<WebElement> footerElements = driver.findElements(By.xpath("//*[@class='cursor-pointer']"));

     Set<String> foundItems = new HashSet<>();

     for (WebElement footerElement : footerElements) {
         String text = footerElement.getText().trim();

         
         for (String expected : expectedFooterItems) {
             if (expected.equalsIgnoreCase(text)) {
                 test.log(Status.PASS, expected + " is displayed.");
                 foundItems.add(expected);
                 break;
             }
         }
     }

     // Log missing footer items
     for (String expectedItem : expectedFooterItems) {
         if (!foundItems.contains(expectedItem)) {
             test.log(Status.FAIL, expectedItem + " is NOT displayed.");
         }
     }
 }
 
 public void validateTermsAndCondition(ExtentTest test) {
	    try {
	        WebElement termsLink = driver.findElement(By.xpath("//*[text()='Terms & Conditions']"));

	        // Scroll into view
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", termsLink);
	        termsLink.click();
	        test.log(Status.INFO, "Clicked on 'Terms & Conditions' link.");

	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement termsDetailsPanel = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='panel-body']"))
	        );

	        if (termsDetailsPanel.isDisplayed()) {
	            test.log(Status.PASS, "Terms and Conditions details are displayed.");
	        } else {
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "TermsAndConditions", "Details not visible");
	            test.log(Status.FAIL, "Terms and Conditions details are not visible.");
	            Assert.fail();
	        }

	    } catch (NoSuchElementException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "TermsAndConditions", "Element not found");
	        test.log(Status.FAIL, "Terms & Conditions link or details panel not found.");
	        Assert.fail();
	    } catch (TimeoutException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "TermsAndConditions", "Timeout");
	        test.log(Status.FAIL, "Terms and Conditions details did not appear within timeout.");
	        Assert.fail();
	    } catch (Exception e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "TermsAndConditions", "Unexpected error");
	        test.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
	        Assert.fail();
	    }
	}
 public void validateAboutUs(ExtentTest test) {
	    try {
	        WebElement aboutLink = driver.findElement(By.xpath("//*[text()='About us']"));

	        // Scroll into view
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", aboutLink);
	        aboutLink.click();
	        test.log(Status.INFO, "Clicked on 'About us' link.");

	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement aboutDetailsPanel = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='panel-body']"))
	        );

	        if (aboutDetailsPanel.isDisplayed()) {
	            test.log(Status.PASS, "About us details are displayed.");
	        } else {
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "AboutUs", "Details not visible");
	            test.log(Status.FAIL, "About us details are not visible.");
	            Assert.fail();
	        }

	    } catch (NoSuchElementException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "AboutUs", "Element not found");
	        test.log(Status.FAIL, "About us link or details panel not found.");
	        Assert.fail();
	    } catch (TimeoutException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "AboutUs", "Timeout");
	        test.log(Status.FAIL, "About us details did not appear within timeout.");
	        Assert.fail();
	    } catch (Exception e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "AboutUs", "Unexpected error");
	        test.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
	        Assert.fail();
	    }
	}
 public void validateContactUs(ExtentTest test) {
	    try {
	        WebElement contactLink = driver.findElement(By.xpath("//*[text()='Contact us']"));

	        // Scroll into view
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", contactLink);
	        contactLink.click();
	        test.log(Status.INFO, "Clicked on 'Contact us' link.");

	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement contactDetailsPanel = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='panel-body']"))
	        );

	        if (contactDetailsPanel.isDisplayed()) {
	            test.log(Status.PASS, "Contact us details are displayed.");
	        } else {
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "ContactUs", "Details not visible");
	            test.log(Status.FAIL, "Contact us details are not visible.");
	            Assert.fail();
	        }

	    } catch (NoSuchElementException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "ContactUs", "Element not found");
	        test.log(Status.FAIL, "Contact us link or details panel not found.");
	        Assert.fail();
	    } catch (TimeoutException e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "ContactUs", "Timeout");
	        test.log(Status.FAIL, "Contact us details did not appear within timeout.");
	        Assert.fail();
	    } catch (Exception e) {
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "ContactUs", "Unexpected error");
	        test.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
	        Assert.fail();
	    }
	}





}
