package com.skytraveller.pageObjects;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class ProfilePage  extends BasePage{

	// Constructor of loginPage calls the BasePage constructor with driver
		public ProfilePage(WebDriver driver) {
			super(driver);// calls BasePage constructor
		}
	
		@FindBy(xpath = "//input[@type='file']")
		WebElement logoInput;

		public void chooseLogoImage(String imgPath,ExtentTest test) {
		    try {
		        // Scroll the logo input into view
		        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", logoInput);
		        Thread.sleep(500); // slight delay to ensure it's in view

		        // Select the file (Note: remove extra quotes from path)
		        logoInput.sendKeys(imgPath);

		        System.out.println("✅ Logo image selected successfully.");
		        test.log(Status.PASS, "✅ Logo image selected successfully.");
		    } catch (Exception e) {
		        System.out.println("❌ Failed to select logo image: " + e.getMessage());
		        test.log(Status.FAIL, "❌ Failed to select logo image: " + e.getMessage());
		        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "File Select Failure", "LogoSelectException");
		        Assert.fail();
		    }
		}

/*
		public void uploadLogo(ExtentTest test) {
		    try {
		        WebElement uploadLogos = driver.findElement(By.xpath("//*[text()='Click Here to view uploaded file']"));

		        if (uploadLogos.isDisplayed()) {
		            uploadLogos.click();
		            System.out.println("✅ Logo uploaded and link clicked successfully.");
		            test.log(Status.PASS, "✅ Logo uploaded and link clicked successfully.");
		        } else {
		            System.out.println("❌ Upload confirmation link is not visible.");
		            test.log(Status.FAIL, "❌ Upload confirmation link is not visible.");
		            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Upload Link Not Visible", "LogoUploadLinkMissing");
		        }
		    } catch (Exception e) {
		        System.out.println("❌ Exception while clicking upload link: " + e.getMessage());
		        test.log(Status.FAIL, "❌ Exception while clicking upload link: " + e.getMessage());
		        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Upload Link Failure", "LogoUploadException");
		    }
		}
*/
		public void uploadLogo(ExtentTest test) {
		    try {
		        WebElement uploadLink = driver.findElement(By.xpath("//*[text()='Click Here to view uploaded file']"));

		        if (uploadLink.isDisplayed()) {
		            // Scroll into view
		            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", uploadLink);
		            Thread.sleep(500); // allow scroll to complete

		            // Use JavaScript to click
		            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", uploadLink);

		            System.out.println("✅ Upload link clicked successfully.");
		            test.log(Status.PASS, "✅ Upload link clicked successfully.");
		        } else {
		            test.log(Status.FAIL, "❌ Upload link not visible.");
		            Assert.fail();
		        }

		    } catch (Exception e) {
		        System.out.println("❌ Exception while clicking upload link: " + e.getMessage());
		        test.log(Status.FAIL, "❌ Exception while clicking upload link: " + e.getMessage());
		        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
		                "Upload Link Click Failed", "UploadLinkClickException");
		        Assert.fail();
		    }
		}

		
		
		public void validateProfilePageIsDisplayed(ExtentTest test) {
		    try {
		        WebElement profilePage = driver.findElement(By.xpath("//*[@class='profile-page__content gap-5 p-2']"));
		        
		        if (profilePage.isDisplayed()) {
		            test.log(Status.PASS, "Profile page is displayed.");
		            
		            
		        } else {
		            test.log(Status.FAIL, "Profile page is not displayed.");
		            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "ProfilePage Failure", "ProfilePageNotVisible");
		            Assert.fail();
		        }
		    } catch (NoSuchElementException e) {
		        test.log(Status.FAIL, "Profile page element not found.");
		        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "ProfilePage Failure", "ElementNotFound");
		        Assert.fail();
		    } catch (Exception e) {
		        test.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
		        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "ProfilePage Failure", "UnexpectedException");
		        Assert.fail();
		    }
		}

		
}
