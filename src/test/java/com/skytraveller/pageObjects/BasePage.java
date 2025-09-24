package com.skytraveller.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage {

	 // Constructor of BasePage to initialize driver and PageFactory elements
	WebDriver driver;
	
	public BasePage(WebDriver driver)
	{
		this.driver=driver;
		PageFactory.initElements(driver, this);// initialize all @FindBy elements in this class and subclasses
	}
}
