package com.iween.testBase;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.iween.utilities.ExtentManager;
import com.iween.utilities.Iween_FutureDates;
import com.iween.utilities.ScreenshotUtil;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import org.openqa.selenium.remote.RemoteWebDriver;

import org.testng.annotations.*;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

public class baseClass {

    public static WebDriver driver;
    public static Logger logger;
    public static Properties p;
    public static ExtentTest test;
    public ExtentReports extent;
    public ScreenshotUtil screenShots;
    public Iween_FutureDates futureDates;

    @BeforeMethod
    @Parameters({"os", "browser"})
    public void setup(String os, String browser, Method method) throws Exception {
        logger = LogManager.getLogger(this.getClass());

        p = new Properties();
        FileReader file = new FileReader("./src/test/resources/config.properties");
        if (file == null) {
            logger.error("config.properties file not found at ./src/test/resources/");
            throw new IllegalStateException("config.properties file is missing");
        }
        p.load(file);
        logger.info("Loaded config.properties file successfully.");

        // Use helper method to get required property with clear error if missing
        String env = getRequiredProperty("execution_env").toLowerCase();
        String appUrl = getRequiredProperty("applicationUrl");

        logger.info("Execution environment: " + env + " | OS: " + os + " | Browser: " + browser);

        switch (env) {
            case "remote":
                setupRemoteDriver(os, browser);
                break;
            case "local":
                setupLocalDriver(browser);
                break;
            case "machine":
                setupMachineDriver(browser);
                break;
            default:
                throw new IllegalArgumentException("Invalid execution_env in config.properties: " + env);
        }

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080)); // ensure proper size in headless
        driver.get(appUrl);

        logger.info("Launched URL: " + appUrl);

        extent = ExtentManager.getExtentReports();
        ExtentManager.setTest(test);
        screenShots = new ScreenshotUtil();
        futureDates = new Iween_FutureDates();
    }

    /**
     * Helper method to get required property from Properties object.
     * Throws IllegalStateException if property is missing or empty.
     */
    private String getRequiredProperty(String key) {
        String value = p.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required property: " + key);
        }
        return value.trim();
    }

    private void setupRemoteDriver(String os, String browser) throws Exception {
        String gridUrl = "http://localhost:4444/wd/hub";

        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--window-size=1920,1080");
                chromeOptions.setPlatformName(os.toLowerCase());
                driver = new RemoteWebDriver(new URL(gridUrl), chromeOptions);
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("-headless");
                firefoxOptions.addPreference("browser.startup.page", 0);
                firefoxOptions.addPreference("browser.startup.homepage_override.mstone", "ignore");
                firefoxOptions.setPlatformName(os.toLowerCase());
                driver = new RemoteWebDriver(new URL(gridUrl), firefoxOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
                edgeOptions.setPlatformName(os.toLowerCase());
                driver = new RemoteWebDriver(new URL(gridUrl), edgeOptions);
                break;

            default:
                throw new IllegalArgumentException("Unsupported remote browser: " + browser);
        }
    }

    private void setupLocalDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--window-size=1920,1080");
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("-headless");
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
                driver = new EdgeDriver(edgeOptions);
                break;

            default:
                throw new IllegalArgumentException("Unsupported local browser: " + browser);
        }
    }

    private void setupMachineDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(); // headed
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(); // headed
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver(); // headed
                break;
            default:
                throw new IllegalArgumentException("Unsupported machine browser: " + browser);
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            logger.info("Closing browser.");
            driver.quit();
        }
    }
}
