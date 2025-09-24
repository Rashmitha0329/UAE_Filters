package com.skytraveller.pageObjects1;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class loginPage extends BasePage{

	// Constructor of loginPage calls the BasePage constructor with driver
	public loginPage(WebDriver driver) {
		super(driver);// calls BasePage constructor
	}

	@FindBy(xpath="//img[@src]")
	WebElement imageLogo;


	@FindBy(xpath="//*[text()='User ID']/following-sibling::input")
	WebElement userName;


	@FindBy(xpath="//*[text()='Password']/following-sibling::input")
	WebElement password;

	@FindBy(xpath="//button[text()='Sign In']")
	WebElement submit;


	public void validateLogo(ExtentTest test) {
		try {
			Thread.sleep(1000);
			if (imageLogo.isDisplayed()) {
				test.log(Status.PASS, "Image is displayed on the Home Page");
			} else {
				test.log(Status.FAIL, "Image is not displayed on the Home Page");
				ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, 
						"Image is not displayed", "LogoImageIsBroken");
			}
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred while validating logo: " + e.getMessage());
			ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, 
					"Exception while validating logo", "LogoValidationException");
		}
	}

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
		}
 
	} 

 public void clickOnSubmitButton()
 {
	 submit.click();
 }



}
