package com.skytraveller.pageObjects;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class FlightDetailsRoundTrip extends BasePage  {
	public FlightDetailsRoundTrip(WebDriver driver) {
		super(driver);// calls BasePage constructor
	}
	/*
	public void validateFlightDetailsBasedOnFlightCard(String[] userEnteredData, ExtentTest test) {
	    try {
	        // Extract user-entered details
	        String onwardLocation = userEnteredData[0];           // e.g., BLR
	        String returnLocation = userEnteredData[1];           // e.g., SHJ
	        String userEnteredOnwardDate = userEnteredData[2];    // e.g., 24 Aug, 2025
	        String userEnteredReturnDate = userEnteredData[3];    // e.g., 27 Aug, 2025
	        String userEnteredClass = userEnteredData[userEnteredData.length - 1]; // Last item assumed to be travel class

	        // === 1. Extract flight card airport codes ===
	        List<WebElement> airPortCode = driver.findElements(By.xpath("//*[@class='sector-span']"));

	        String actualOnwardFrom = "", actualOnwardTo = "";
	        String actualReturnFrom = "", actualReturnTo = "";

	        if (airPortCode.size() >= 2) {
	            // Onward flight details
	            String onwardText = airPortCode.get(0).getText();  // e.g., Bengaluru(BLR) -> Sharjah(SHJ)
	            String[] onwardParts = onwardText.split("\\(");
	            actualOnwardFrom = onwardParts[1].split("\\)")[0].trim();  // BLR
	            actualOnwardTo = onwardParts[2].split("\\)")[0].trim();    // SHJ

	            // Return flight details
	            String returnText = airPortCode.get(1).getText();  // e.g., Sharjah(SHJ) -> Bengaluru(BLR)
	            String[] returnParts = returnText.split("\\(");
	            actualReturnFrom = returnParts[1].split("\\)")[0].trim();  // SHJ
	            actualReturnTo = returnParts[2].split("\\)")[0].trim();    // BLR
	        } else {
	            test.log(Status.FAIL, "Could not find both onward and return flight details.");
	            Assert.fail();
	            return;
	        }

	        // === 2. Extract flight card dates ===
	        List<WebElement> dateElements = driver.findElements(By.xpath("//*[@class='date-span']"));
	        String actualOnwardDate = "", actualReturnDate = "";

	        if (dateElements.size() >= 2) {
	            // Onward Date
	            String onwardDateText = dateElements.get(0).getText(); // e.g., "Sun, 24 Aug, 2025"
	            String[] onwardDateParts = onwardDateText.split(",");
	            actualOnwardDate = onwardDateParts[1].trim() + ", " + onwardDateParts[2].trim();

	            // Return Date
	            String returnDateText = dateElements.get(1).getText(); // e.g., "Wed, 27 Aug, 2025"
	            String[] returnDateParts = returnDateText.split(",");
	            actualReturnDate = returnDateParts[1].trim() + ", " + returnDateParts[2].trim();
	        } else {
	            test.log(Status.FAIL, "Could not find both onward and return flight dates.");
	            Assert.fail();
	            return;
	        }

	        // === 3. Extract travel class ===
	        List<WebElement> classElements = driver.findElements(By.xpath("//*[@class='d-flex gap-sm-2 flex-sm-row flex-column flex-wrap gap-1']/span"));
	        String actualTravelClass = "";
	        for (WebElement el : classElements) {
	            String text = el.getText().trim();
	            if (text.equalsIgnoreCase("ECONOMY") || text.equalsIgnoreCase("BUSINESS") || text.equalsIgnoreCase("FIRST")) {
	                actualTravelClass = text;
	                break;
	            }
	        }

	        if (actualTravelClass.isEmpty()) {
	            test.log(Status.FAIL, "Could not find travel class on flight card.");
	            Assert.fail();
	            return;
	        }

	        // === 4. Perform validation ===
	        boolean matches = true;

	        // Onward flight validations
	        if (!actualOnwardFrom.equalsIgnoreCase(onwardLocation)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Onward FROM location: Expected " + onwardLocation + ", Found " + actualOnwardFrom);
	        }
	        if (!actualOnwardTo.equalsIgnoreCase(returnLocation)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Onward TO location: Expected " + returnLocation + ", Found " + actualOnwardTo);
	        }
	        if (!actualOnwardDate.equals(userEnteredOnwardDate)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Onward Date: Expected " + userEnteredOnwardDate + ", Found " + actualOnwardDate);
	        }

	        // Return flight validations (reverse of onward)
	        if (!actualReturnFrom.equalsIgnoreCase(returnLocation)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Return FROM location: Expected " + returnLocation + ", Found " + actualReturnFrom);
	        }
	        if (!actualReturnTo.equalsIgnoreCase(onwardLocation)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Return TO location: Expected " + onwardLocation + ", Found " + actualReturnTo);
	        }
	        if (!actualReturnDate.equals(userEnteredReturnDate)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Return Date: Expected " + userEnteredReturnDate + ", Found " + actualReturnDate);
	        }

	        // Travel class validation
	        if (!actualTravelClass.equalsIgnoreCase(userEnteredClass)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Travel Class: Expected " + userEnteredClass + ", Found " + actualTravelClass);
	        }

	        // Final result
	        if (matches) {
	            test.log(Status.PASS, "Flight details matched successfully.");
	            test.log(Status.INFO, "From: " + actualOnwardFrom + ", To: " + actualOnwardTo +
	                                  ", Onward Date: " + actualOnwardDate +
	                                  ", Return Date: " + actualReturnDate +
	                                  ", Class: " + actualTravelClass);
	        } else {
	            test.log(Status.FAIL, "Flight details mismatch.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Detail Mismatch", "Mismatch in flight card details");
	            Assert.fail();
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception while validating flight details: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception", "Error during validation");
	        Assert.fail();
	    }
	}
*/
	/*
	public void validateFlightDetailsBasedOnFlightCard(String[] userEnteredData, ExtentTest test) {
	    try {
	        // Extract user-entered details
	        String onwardLocation = userEnteredData[0];           // e.g., SHJ
	        String returnLocation = userEnteredData[1];           // e.g., DEL
	        String userEnteredOnwardDate = userEnteredData[2];    // e.g., 26 Aug, 2025
	        String userEnteredReturnDate = userEnteredData[3];    // e.g., 29 Aug, 2025
	        String userEnteredClass = userEnteredData[userEnteredData.length - 1]; // ECONOMY, BUSINESS, FIRST

	        // === 1. Extract airport codes from flight card ===
	        List<WebElement> airPortCode = driver.findElements(By.xpath("//*[@class='sector-span']"));

	        String actualOnwardFrom = "", actualOnwardTo = "";
	        String actualReturnFrom = "", actualReturnTo = "";

	        if (airPortCode.size() >= 2) {
	            // Onward segment
	            String onwardText = airPortCode.get(0).getText();  // e.g., Sharjah(SHJ) -> Delhi(DEL)
	            String[] onwardParts = onwardText.split("\\(");
	            actualOnwardFrom = onwardParts[1].split("\\)")[0].trim();  // SHJ
	            actualOnwardTo = onwardParts[2].split("\\)")[0].trim();    // DEL

	            // Return segment
	            String returnText = airPortCode.get(1).getText();  // e.g., Delhi(DEL) -> Sharjah(SHJ)
	            String[] returnParts = returnText.split("\\(");
	            actualReturnFrom = returnParts[1].split("\\)")[0].trim();  // DEL
	            actualReturnTo = returnParts[2].split("\\)")[0].trim();    // SHJ
	        } else {
	            test.log(Status.FAIL, "Could not find both onward and return flight details.");
	            Assert.fail();
	            return;
	        }

	        // === 2. Extract dates from flight card ===
	        List<WebElement> dateElements = driver.findElements(By.xpath("//*[@class='date-span']"));
	        String actualOnwardDate = "", actualReturnDate = "";

	        if (dateElements.size() >= 2) {
	            // Onward Date
	            String onwardDateText = dateElements.get(0).getText(); // e.g., "Tue, 26 Aug, 2025"
	            String[] onwardDateParts = onwardDateText.split(",");
	            actualOnwardDate = onwardDateParts[1].trim() + ", " + onwardDateParts[2].trim();

	            // Return Date
	            String returnDateText = dateElements.get(1).getText(); // e.g., "Fri, 29 Aug, 2025"
	            String[] returnDateParts = returnDateText.split(",");
	            actualReturnDate = returnDateParts[1].trim() + ", " + returnDateParts[2].trim();
	        } else {
	            test.log(Status.FAIL, "Could not find both onward and return dates.");
	            Assert.fail();
	            return;
	        }

	        // === 3. Extract travel class ===
	        List<WebElement> classElements = driver.findElements(By.xpath("//*[@class='d-flex gap-sm-2 flex-sm-row flex-column flex-wrap gap-1']/span"));
	        String actualTravelClass = "";
	        for (WebElement el : classElements) {
	            String text = el.getText().trim();
	            if (text.equalsIgnoreCase("ECONOMY") || text.equalsIgnoreCase("BUSINESS") || text.equalsIgnoreCase("FIRST")) {
	                actualTravelClass = text;
	                break;
	            }
	        }

	        if (actualTravelClass.isEmpty()) {
	            test.log(Status.FAIL, "Could not find travel class on flight card.");
	            Assert.fail();
	            return;
	        }

	        // === 4. Validate all flight details ===
	        boolean matches = true;

	        // Onward flight checks
	        if (!actualOnwardFrom.equalsIgnoreCase(onwardLocation)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Onward FROM location: Expected " + onwardLocation + ", Found " + actualOnwardFrom);
	        }

	        if (!actualOnwardTo.equalsIgnoreCase(returnLocation)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Onward TO location: Expected " + returnLocation + ", Found " + actualOnwardTo);
	        }

	        if (!actualOnwardDate.equals(userEnteredOnwardDate)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Onward Date: Expected " + userEnteredOnwardDate + ", Found " + actualOnwardDate);
	        }

	        // Return flight checks (from = TO of onward, to = FROM of onward)
	        if (!actualReturnFrom.equalsIgnoreCase(returnLocation)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Return FROM location: Expected " + returnLocation + ", Found " + actualReturnFrom);
	        }

	        if (!actualReturnTo.equalsIgnoreCase(onwardLocation)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Return TO location: Expected " + onwardLocation + ", Found " + actualReturnTo);
	        }

	        if (!actualReturnDate.equals(userEnteredReturnDate)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Return Date: Expected " + userEnteredReturnDate + ", Found " + actualReturnDate);
	        }

	        // Travel class
	        if (!actualTravelClass.equalsIgnoreCase(userEnteredClass)) {
	            matches = false;
	            test.log(Status.INFO, "Mismatch in Travel Class: Expected " + userEnteredClass + ", Found " + actualTravelClass);
	        }

	        // === 5. Final log and assertion ===
	        if (matches) {
	            test.log(Status.PASS, "Flight details matched successfully.");
	            test.log(Status.INFO,
	                "✔ Onward Flight => From: " + actualOnwardFrom + ", To: " + actualOnwardTo + ", Date: " + actualOnwardDate + "\n" +
	                "✔ Return Flight => From: " + actualReturnFrom + ", To: " + actualReturnTo + ", Date: " + actualReturnDate + "\n" +
	                "✔ Class: " + actualTravelClass
	            );
	        } else {
	            test.log(Status.FAIL, "Flight details mismatch.");
	            test.log(Status.INFO,
	                "❌ Expected:\n" +
	                "   Onward => From: " + onwardLocation + ", To: " + returnLocation + ", Date: " + userEnteredOnwardDate + "\n" +
	                "   Return => From: " + returnLocation + ", To: " + onwardLocation + ", Date: " + userEnteredReturnDate + "\n" +
	                "   Class: " + userEnteredClass + "\n" +
	                "❌ Actual:\n" +
	                "   Onward => From: " + actualOnwardFrom + ", To: " + actualOnwardTo + ", Date: " + actualOnwardDate + "\n" +
	                "   Return => From: " + actualReturnFrom + ", To: " + actualReturnTo + ", Date: " + actualReturnDate + "\n" +
	                "   Class: " + actualTravelClass
	            );
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Detail Mismatch", "Mismatch in flight card details");
	            Assert.fail();
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception while validating flight details: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception", "Error during validation");
	        Assert.fail();
	    }
	}
*/
	public void validateFlightDetailsBasedOnFlightCard(String[] userEnteredData, ExtentTest test) {
	    try {
	        // Extract user-entered details
	        String onwardLocation = userEnteredData[0];           // e.g., SHJ
	        String returnLocation = userEnteredData[1];           // e.g., DEL
	        String userEnteredOnwardDate = userEnteredData[2];    // e.g., 26 Aug, 2025
	        String userEnteredReturnDate = userEnteredData[3];    // e.g., 29 Aug, 2025
	        String userEnteredClass = userEnteredData[userEnteredData.length - 1]; // ECONOMY, BUSINESS, FIRST

	        // === 1. Extract airport codes from flight card ===
	        List<WebElement> airPortCode = driver.findElements(By.xpath("//*[@class='sector-span']"));
	        String actualOnwardFrom = "", actualOnwardTo = "", actualReturnFrom = "", actualReturnTo = "";

	        if (airPortCode.size() >= 2) {
	            String onwardText = airPortCode.get(0).getText();
	            String[] onwardParts = onwardText.split("\\(");
	            actualOnwardFrom = onwardParts[1].split("\\)")[0].trim();
	            actualOnwardTo = onwardParts[2].split("\\)")[0].trim();

	            String returnText = airPortCode.get(1).getText();
	            String[] returnParts = returnText.split("\\(");
	            actualReturnFrom = returnParts[1].split("\\)")[0].trim();
	            actualReturnTo = returnParts[2].split("\\)")[0].trim();
	        } else {
	            test.log(Status.FAIL, "Could not find both onward and return flight details.");
	            Assert.fail();
	            return;
	        }

	        // === 2. Extract dates from flight card ===
	        List<WebElement> dateElements = driver.findElements(By.xpath("//*[@class='date-span']"));
	        String actualOnwardDate = "", actualReturnDate = "";

	        if (dateElements.size() >= 2) {
	            String[] onwardDateParts = dateElements.get(0).getText().split(",");
	            actualOnwardDate = onwardDateParts[1].trim() + ", " + onwardDateParts[2].trim();

	            String[] returnDateParts = dateElements.get(1).getText().split(",");
	            actualReturnDate = returnDateParts[1].trim() + ", " + returnDateParts[2].trim();
	        } else {
	            test.log(Status.FAIL, "Could not find both onward and return dates.");
	            Assert.fail();
	            return;
	        }

	        // === 3. Extract travel class ===
	        List<WebElement> classElements = driver.findElements(By.xpath("//*[@class='d-flex gap-sm-2 flex-sm-row flex-column flex-wrap gap-1']/span"));
	        String actualTravelClass = "";
	        for (WebElement el : classElements) {
	            String text = el.getText().trim();
	            if (text.equalsIgnoreCase("ECONOMY") || text.equalsIgnoreCase("BUSINESS") || text.equalsIgnoreCase("FIRST")) {
	                actualTravelClass = text;
	                break;
	            }
	        }

	        if (actualTravelClass.isEmpty()) {
	            test.log(Status.FAIL, "Could not find travel class on flight card.");
	            Assert.fail();
	            return;
	        }

	        // === 4. Compare fields and store in table ===
	        List<String[]> table = new ArrayList<>();
	        boolean allMatch = true;

	        allMatch &= addComparisonRow(table, "Onward From", actualOnwardFrom, onwardLocation);
	        allMatch &= addComparisonRow(table, "Onward To", actualOnwardTo, returnLocation);
	        allMatch &= addComparisonRow(table, "Onward Date", actualOnwardDate, userEnteredOnwardDate);
	        allMatch &= addComparisonRow(table, "Return From", actualReturnFrom, returnLocation);
	        allMatch &= addComparisonRow(table, "Return To", actualReturnTo, onwardLocation);
	        allMatch &= addComparisonRow(table, "Return Date", actualReturnDate, userEnteredReturnDate);
	        allMatch &= addComparisonRow(table, "Travel Class", actualTravelClass, userEnteredClass);

	        // === 5. Build HTML Table for Logs ===
	        StringBuilder html = new StringBuilder();
	        html.append("<table border='1' style='border-collapse: collapse; font-family: monospace;'>")
	            .append("<tr style='background-color:#f2f2f2;'><th>Field</th><th>Card Value</th><th>Detail Value</th><th>Status</th></tr>");
	        for (String[] row : table) {
	            String bgColor = row[3].equals("PASS") ? "#d4edda" : "#f8d7da";
	            html.append("<tr style='background-color:").append(bgColor).append(";'>");
	            for (String col : row) {
	                html.append("<td style='padding: 5px;'>").append(col).append("</td>");
	            }
	            html.append("</tr>");
	        }
	        html.append("</table>");

	        // === 6. Final Log ===
	        if (allMatch) {
	            test.log(Status.PASS, "All flight details matched successfully.");
	        } else {
	            test.log(Status.FAIL, "Mismatch found in one or more flight details.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Detail Mismatch", "Mismatch in flight card details");
	        }

	        test.log(Status.INFO, html.toString());

	        if (!allMatch) Assert.fail();

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception while validating flight details: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception", "Error during validation");
	        Assert.fail();
	    }
	}

	// Helper method to compare values and build row
	private boolean addComparisonRow(List<String[]> table, String field, String actual, String expected) {
	    boolean match = actual.trim().equalsIgnoreCase(expected.trim());
	    String status = match ? "PASS" : "FAIL";
	    table.add(new String[]{field, actual, expected, status});
	    return match;
	}

	
	
	
	public String[] getFlightDetailsOnward() throws InterruptedException {
		Thread.sleep(2000);
		
		// Locate the element
		WebElement flightDetails = driver.findElement(By.xpath("(//a[text()='Flight Details'])[1]"));
	 
		// Scroll to the element using JavaScriptExecutor
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", flightDetails);
	 
		// Optionally, wait a bit to ensure scroll animation completes
		Thread.sleep(500);
	 
		// Click the element
//		flightDetails.click();
		
	    WebElement sector = driver.findElement(By.xpath("(//div[contains(@class,'selected-flight-details__trip-details')])[1]//span[1]"));
	    String[] sectors = sector.getText().split("->");
	 
	    String[] sector1Parts = sectors[0].trim().split("\\(");
	    String city1 = sector1Parts[0].trim();
	    String code1 = sector1Parts[1].replace(")", "").trim();
	 
	    String[] sector2Parts = sectors[1].trim().split("\\(");
	    String city2 = sector2Parts[0].trim();
	    String code2 = sector2Parts[1].replace(")", "").trim();
	    
	    Thread.sleep(2000);
	    
	    WebElement departDate = driver.findElement(By.xpath("(//div[contains(@class,'selected-flight-details__trip-details')])[1]//span[2]"));
	    String[] departDate1 = departDate.getText().split(",");
	    String departDate2 = departDate1[1].trim();
	    String departDate3 = departDate1[2].trim();
	    String departDateConcat = departDate2 + ", " + departDate3;
	    
		WebElement onword = driver.findElement(By.xpath("(//span[contains(text(),'Onward')]/parent::div/parent::div/following-sibling::div)[1]"));
	 
	    
	   
	 
	    List<WebElement> Airlines = onword.findElements(By.xpath(".//span[@class='primary-color flight-company']"));
	    ArrayList<String> airlines = new ArrayList<>();
	    for (WebElement Airline : Airlines) {
	        airlines.add(Airline.getText().trim().toLowerCase());
	    }
	 
	    List<WebElement> flightNumber = onword.findElements(By.xpath(".//span[@class='flight-number']"));
	    ArrayList<String> flightNumbers = new ArrayList<>();
	    for (WebElement flightNumber1 : flightNumber) {
	        flightNumbers.add(flightNumber1.getText());
	    }
	 
	    List<WebElement> FlightEquipment = onword.findElements(By.xpath(".//span[@class='flight-equipment']"));
	    ArrayList<String> FlightEquipments = new ArrayList<>();
	    for (WebElement FlightEquipment1 : FlightEquipment) {
	        String text = FlightEquipment1.getText().trim().replace("AIRCRAFT TYPE:", "").trim();
	        FlightEquipments.add(text);
	    }
	 
	    List<WebElement> OperatorName = onword.findElements(By.xpath(".//span[@class='flight-operated-by']"));
	    ArrayList<String> OperatorNames = new ArrayList<>();
	    for (WebElement OperatorName1 : OperatorName) {
	        String text = OperatorName1.getText().trim().replace("OPERATED BY:", "").trim().toLowerCase();
	        OperatorNames.add(text);
	    }
	 
	    List<WebElement> departTimeGet = onword.findElements(By.xpath(".//span[@class='flight-deptime']"));
	    String DepartTimeText = departTimeGet.get(0).getText();
	 
	    List<WebElement> arrivalTimeGet = onword.findElements(By.xpath(".//span[@class='flight-arrtime']"));
	    String ArrivalTimeTextt = arrivalTimeGet.get(arrivalTimeGet.size() - 1).getText();
	 
	    
	WebElement firstLegDepartDate = onword.findElement(By.xpath(".//span[@class='flight-depdate']"));
	String[] Date = firstLegDepartDate.getText().split(",");
	String Date1 = Date[1].trim();
	String year = Date[2].trim();
	String dateYear = Date1 +" "+year;
	String dateYear1 =  dateYear.replace(")", "");
	System.out.println(dateYear1);
	 
	List<WebElement> firstLegDepartLocation = onword.findElements(By.xpath(".//span[@class='flight-origin_name']"));
	WebElement firstLegDepartLocationFirst = firstLegDepartLocation.getFirst();
	String[] firstLegDepartLocation1 = firstLegDepartLocationFirst.getText().split("-");
	String firstLegDepartLocation2 = firstLegDepartLocation1[0].trim();
	System.out.println(firstLegDepartLocation2);
	 
	 
	  List<WebElement> firstLegArrivalLocation = onword.findElements(By.xpath(".//span[@class='flight-origin_name']"));
	  WebElement firstLegArrivalLocation1 = firstLegArrivalLocation.getLast();
	  String[] firstLegArrivalLocation2 = firstLegArrivalLocation1.getText().split("-");
	  String firstLegArrivalLocation3 = firstLegArrivalLocation2[0].trim();
	  System.out.println(firstLegArrivalLocation3);
	 
	 
	  List<WebElement> fareTypes = onword.findElements(By.xpath(".//div[@class='flight-fare-type']"));
	  ArrayList<String> Fare = new ArrayList<>();
	for(WebElement fareType:fareTypes)
	{
		 String fareTypeText = fareType.getText().trim();
		 Fare.add(fareTypeText);
		
	}
	 
	System.out.println(Fare);
	 
	 
	  List<WebElement> bookingClass = onword.findElements(By.xpath(".//span[@class='flight-booking-class']"));
	  ArrayList<String> bookingcls = new ArrayList<>();
	 
	for(WebElement bookingClas : bookingClass)
	{
		 String bookingClas1 = bookingClas.getText().replace("BOOKING CLASS:", "").trim();
		 bookingcls.add(bookingClas1);
	 
		 System.out.println(bookingClas1);
	}
	System.out.println(bookingcls);
	 
	/*
	List<WebElement> baggage = driver.findElements(By.xpath("//span[@class='flight-baggage']"));
	ArrayList<String> checkInbaggage = new ArrayList<>();
	 
	for (WebElement baggages1 : baggage) {
	     String baggages = baggages1.getText().trim();
	     checkInbaggage.add(baggages);
	 
	     System.out.println(baggages);
	}
	 
	// Optional: print the whole list
	System.out.println("Final baggage list: " + checkInbaggage);
	 
	Thread.sleep(2000);
	List<WebElement> cabin = driver.findElements(By.xpath("//span[@class='flight-baggage']/following-sibling::span"));
	ArrayList<String> cabinBaggage = new ArrayList<>();
	 
	for (WebElement cabin1 : cabin) {
	     String cabinbaggages = cabin1.getText().trim();
	     checkInbaggage.add(cabinbaggages);
	 
	     System.out.println(cabinbaggages);
	}
	 
	// Optional: print the whole list
	System.out.println("Final baggage list: " + cabinBaggage);
	*/
	List<WebElement> baggage = onword.findElements(By.xpath(".//span[@class='flight-baggage']"));
	ArrayList<String> checkInbaggage = new ArrayList<>();
	 
	for (WebElement baggages1 : baggage) {
	     String baggages = baggages1.getText().trim();
	     checkInbaggage.add(baggages);
	 
	     System.out.println(baggages);
	}
	 
	System.out.println("Final Check-in baggage list: " + checkInbaggage);
	 
	Thread.sleep(2000);
	 
	List<WebElement> cabin = onword.findElements(By.xpath(".//span[@class='flight-baggage']/following-sibling::span"));
	ArrayList<String> cabinBaggage = new ArrayList<>();
	 
	for (WebElement cabin1 : cabin) {
	     String cabinbaggages = cabin1.getText().trim();
	     cabinBaggage.add(cabinbaggages);  // ✅ fixed here
	 
	     System.out.println(cabinbaggages);
	}
	 
	System.out.println("Final Cabin baggage list: " + cabinBaggage);  // ✅ fixed here
	 
	// WebElement dateElement = onword.findElement(By.xpath("(//span[@class='flight-arrdate'])[last()]"));
	 List<WebElement> dateElement = onword.findElements(By.xpath(".//span[@class='flight-arrdate']"));
	 WebElement fullDate = dateElement.getLast();
	String fullDate1 = fullDate.getText();
	String arrivalDate = fullDate1.replaceAll("^\\(\\w+,\\s*", "").replaceAll("\\)$", "");
	 
	System.out.println(arrivalDate);  // Output: 24 Aug, 2025
	 
	 
	 
	    // Convert lists to strings
	    String airlinesStr = String.join(",", airlines);
	    String flightNumbersStr = String.join(",", flightNumbers);
	    String flightEquipmentsStr = String.join(",", FlightEquipments);
	    String operatorNamesStr = String.join(",", OperatorNames);
	    String FareStr = String.join(",", Fare);
	    String bookingclsStr = String.join(",", bookingcls);
	    String checkInbaggage1 = String.join(",", checkInbaggage);
	    String cabinBaggage1 = String.join(",", cabinBaggage);
	 
	    
	    
	    return new String[]{
	        city1, code1, city2, code2, departDateConcat,
	        airlinesStr, flightNumbersStr, flightEquipmentsStr, operatorNamesStr,
	        DepartTimeText, ArrivalTimeTextt ,FareStr,bookingclsStr,checkInbaggage1,cabinBaggage1,arrivalDate
	    };
	}
	
	
	 
	public String[] getFlightDetailsReturn() throws InterruptedException {
		Thread.sleep(2000);
		
		// Locate the element
		//WebElement flightDetails = driver.findElement(By.xpath("(//a[text()='Flight Details'])[1]"));
	 
		// Scroll to the element using JavaScriptExecutor
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//		js.executeScript("arguments[0].scrollIntoView(true);", flightDetails);
	//
//		// Optionally, wait a bit to ensure scroll animation completes
//		Thread.sleep(500);
	//
//		// Click the element
//		flightDetails.click();
		
	    WebElement sector = driver.findElement(By.xpath("(//div[contains(@class,'selected-flight-details__trip-details')])[2]//span[1]"));
	    String[] sectors = sector.getText().split("->");
	 
	    String[] sector1Parts = sectors[0].trim().split("\\(");
	    String city1 = sector1Parts[0].trim();
	    String code1 = sector1Parts[1].replace(")", "").trim();
	 
	    String[] sector2Parts = sectors[1].trim().split("\\(");
	    String city2 = sector2Parts[0].trim();
	    String code2 = sector2Parts[1].replace(")", "").trim();
	    
	    Thread.sleep(2000);
	    
	    WebElement departDate = driver.findElement(By.xpath("(//div[contains(@class,'selected-flight-details__trip-details')])[2]//span[2]"));
	    String[] departDate1 = departDate.getText().split(",");
	    String departDate2 = departDate1[1].trim();
	    String departDate3 = departDate1[2].trim();
	    String departDateConcat = departDate2 + ", " + departDate3;
	    
		WebElement Return = driver.findElement(By.xpath("(//span[contains(text(),'Return')]/parent::div/parent::div/following-sibling::div)[1]"));
	 
	    
	   
	 
	    List<WebElement> Airlines = Return.findElements(By.xpath(".//span[@class='primary-color flight-company']"));
	    ArrayList<String> airlines = new ArrayList<>();
	    for (WebElement Airline : Airlines) {
	        airlines.add(Airline.getText().trim().toLowerCase());
	    }
	 
	    List<WebElement> flightNumber = Return.findElements(By.xpath(".//span[@class='flight-number']"));
	    ArrayList<String> flightNumbers = new ArrayList<>();
	    for (WebElement flightNumber1 : flightNumber) {
	        flightNumbers.add(flightNumber1.getText());
	    }
	 
	    List<WebElement> FlightEquipment = Return.findElements(By.xpath(".//span[@class='flight-equipment']"));
	    ArrayList<String> FlightEquipments = new ArrayList<>();
	    for (WebElement FlightEquipment1 : FlightEquipment) {
	        String text = FlightEquipment1.getText().trim().replace("AIRCRAFT TYPE:", "").trim();
	        FlightEquipments.add(text);
	    }
	 
	    List<WebElement> OperatorName = Return.findElements(By.xpath(".//span[@class='flight-operated-by']"));
	    ArrayList<String> OperatorNames = new ArrayList<>();
	    for (WebElement OperatorName1 : OperatorName) {
	        String text = OperatorName1.getText().trim().replace("OPERATED BY:", "").trim().toLowerCase();
	        OperatorNames.add(text);
	    }
	 
	    List<WebElement> departTimeGet = Return.findElements(By.xpath(".//span[@class='flight-deptime']"));
	    String DepartTimeText = departTimeGet.get(0).getText();
	 
	    List<WebElement> arrivalTimeGet = Return.findElements(By.xpath(".//span[@class='flight-arrtime']"));
	    String ArrivalTimeTextt = arrivalTimeGet.get(arrivalTimeGet.size() - 1).getText();
	 
	    
	WebElement firstLegDepartDate = Return.findElement(By.xpath(".//span[@class='flight-depdate']"));
	String[] Date = firstLegDepartDate.getText().split(",");
	String Date1 = Date[1].trim();
	String year = Date[2].trim();
	String dateYear = Date1 +" "+year;
	String dateYear1 =  dateYear.replace(")", "");
	System.out.println(dateYear1);
	 
	List<WebElement> firstLegDepartLocation = Return.findElements(By.xpath(".//span[@class='flight-origin_name']"));
	WebElement firstLegDepartLocationFirst = firstLegDepartLocation.getFirst();
	String[] firstLegDepartLocation1 = firstLegDepartLocationFirst.getText().split("-");
	String firstLegDepartLocation2 = firstLegDepartLocation1[0].trim();
	System.out.println(firstLegDepartLocation2);
	 
	 
	  List<WebElement> firstLegArrivalLocation = Return.findElements(By.xpath(".//span[@class='flight-origin_name']"));
	  WebElement firstLegArrivalLocation1 = firstLegArrivalLocation.getLast();
	  String[] firstLegArrivalLocation2 = firstLegArrivalLocation1.getText().split("-");
	  String firstLegArrivalLocation3 = firstLegArrivalLocation2[0].trim();
	  System.out.println(firstLegArrivalLocation3);
	 
	 
	  List<WebElement> fareTypes = Return.findElements(By.xpath(".//div[@class='flight-fare-type']"));
	  ArrayList<String> Fare = new ArrayList<>();
	for(WebElement fareType:fareTypes)
	{
		 String fareTypeText = fareType.getText().trim();
		 Fare.add(fareTypeText);
		
	}
	 
	System.out.println(Fare);
	 
	 
	  List<WebElement> bookingClass = Return.findElements(By.xpath(".//span[@class='flight-booking-class']"));
	  ArrayList<String> bookingcls = new ArrayList<>();
	 
	for(WebElement bookingClas : bookingClass)
	{
		 String bookingClas1 = bookingClas.getText().replace("BOOKING CLASS:", "").trim();
		 bookingcls.add(bookingClas1);
	 
		 System.out.println(bookingClas1);
	}
	System.out.println(bookingcls);
	 
	/*
	List<WebElement> baggage = driver.findElements(By.xpath("//span[@class='flight-baggage']"));
	ArrayList<String> checkInbaggage = new ArrayList<>();
	 
	for (WebElement baggages1 : baggage) {
	     String baggages = baggages1.getText().trim();
	     checkInbaggage.add(baggages);
	 
	     System.out.println(baggages);
	}
	 
	// Optional: print the whole list
	System.out.println("Final baggage list: " + checkInbaggage);
	 
	Thread.sleep(2000);
	List<WebElement> cabin = driver.findElements(By.xpath("//span[@class='flight-baggage']/following-sibling::span"));
	ArrayList<String> cabinBaggage = new ArrayList<>();
	 
	for (WebElement cabin1 : cabin) {
	     String cabinbaggages = cabin1.getText().trim();
	     checkInbaggage.add(cabinbaggages);
	 
	     System.out.println(cabinbaggages);
	}
	 
	// Optional: print the whole list
	System.out.println("Final baggage list: " + cabinBaggage);
	*/
	List<WebElement> baggage = Return.findElements(By.xpath(".//span[@class='flight-baggage']"));
	ArrayList<String> checkInbaggage = new ArrayList<>();
	 
	for (WebElement baggages1 : baggage) {
	     String baggages = baggages1.getText().trim();
	     checkInbaggage.add(baggages);
	 
	     System.out.println(baggages);
	}
	 
	System.out.println("Final Check-in baggage list: " + checkInbaggage);
	 
	Thread.sleep(2000);
	 
	List<WebElement> cabin = Return.findElements(By.xpath(".//span[@class='flight-baggage']/following-sibling::span"));
	ArrayList<String> cabinBaggage = new ArrayList<>();
	 
	for (WebElement cabin1 : cabin) {
	     String cabinbaggages = cabin1.getText().trim();
	     cabinBaggage.add(cabinbaggages);  // ✅ fixed here
	 
	     System.out.println(cabinbaggages);
	}
	 
	System.out.println("Final Cabin baggage list: " + cabinBaggage);  // ✅ fixed here
	 
	// WebElement dateElement = onword.findElement(By.xpath("(//span[@class='flight-arrdate'])[last()]"));
	WebElement dateElement = Return.findElement(By.xpath("(.//span[@class='flight-arrdate'])[last()]"));
	 
	String fullDate = dateElement.getText();
	String arrivalDate = fullDate.replaceAll("^\\(\\w+,\\s*", "").replaceAll("\\)$", "");
	 
	System.out.println(arrivalDate);  // Output: 24 Aug, 2025
	 
	 
	 
	    // Convert lists to strings
	    String airlinesStr = String.join(",", airlines);
	    String flightNumbersStr = String.join(",", flightNumbers);
	    String flightEquipmentsStr = String.join(",", FlightEquipments);
	    String operatorNamesStr = String.join(",", OperatorNames);
	    String FareStr = String.join(",", Fare);
	    String bookingclsStr = String.join(",", bookingcls);
	    String checkInbaggage1 = String.join(",", checkInbaggage);
	    String cabinBaggage1 = String.join(",", cabinBaggage);
	 
	    
	    
	    return new String[]{
	        city1, code1, city2, code2, departDateConcat,
	        airlinesStr, flightNumbersStr, flightEquipmentsStr, operatorNamesStr,
	        DepartTimeText, ArrivalTimeTextt ,FareStr,bookingclsStr,checkInbaggage1,cabinBaggage1,arrivalDate
	    };
	}
	/*
	
	public String formatFlightDetailsAsHtmlTable(String[] flightDetails) {
	    String[] headers = {
	        "From City", "From Code", "To City", "To Code", "Departure Date",
	        "Airlines", "Flight Numbers", "Aircraft", "Operator",
	        "Departure Time", "Arrival Time", "Fare Types",
	        "Booking Classes", "Check-in Baggage", "Cabin Baggage", "Arrival Date"
	    };
	    
	    StringBuilder sb = new StringBuilder();

	    sb.append("<br><b>✈️ Flight Details:</b><br>");
	    sb.append("<table border='1' cellspacing='0' cellpadding='5' style='border-collapse: collapse; font-family: Arial, sans-serif;'>");

	    // Header row
	    sb.append("<tr style='background-color:#f2f2f2;'>");
	    for (String header : headers) {
	        sb.append("<th>").append(header).append("</th>");
	    }
	    sb.append("</tr>");

	    // Data row
	    sb.append("<tr style='text-align:center;'>");
	    for (String detail : flightDetails) {
	        sb.append("<td>").append(detail != null ? detail : "").append("</td>");
	    }
	    sb.append("</tr>");

	    sb.append("</table><br>");

	    return sb.toString();
	}

*/
	/*
	public String formatFlightDetailsAsHtmlTable(String[] flightDetails) {
	    StringBuilder sb = new StringBuilder();

	    sb.append("From City: ").append(flightDetails[0]).append("\n");
	    sb.append("From Code: ").append(flightDetails[1]).append("\n");
	    sb.append("To City: ").append(flightDetails[2]).append("\n");
	    sb.append("To Code: ").append(flightDetails[3]).append("\n");
	    sb.append("Departure Date: ").append(flightDetails[4]).append("\n");
	    sb.append("Airlines: ").append(flightDetails[5]).append("\n");
	    sb.append("Flight Numbers: ").append(flightDetails[6]).append("\n");
	    sb.append("Aircraft: ").append(flightDetails[7]).append("\n");
	    sb.append("Operator: ").append(flightDetails[8]).append("\n");
	    sb.append("Departure Time: ").append(flightDetails[9]).append("\n");
	    sb.append("Arrival Time: ").append(flightDetails[10]).append("\n");
	    sb.append("Fare Types: ").append(flightDetails[11]).append("\n");
	    sb.append("Booking Classes: ").append(flightDetails[12]).append("\n");
	    sb.append("Check-in Baggage: ").append(flightDetails[13]).append("\n");
	    sb.append("Cabin Baggage: ").append(flightDetails[14]).append("\n");
	    sb.append("Arrival Date: ").append(flightDetails[15]).append("\n");

	    return sb.toString();
	}
*/
	public String formatFlightDetailsAsTextTable(String[] flightDetails, String flightType, ExtentTest test) {
	    StringBuilder sb = new StringBuilder();

	    sb.append("✈️ ").append(flightType).append(" Flight Details:\n");
	    sb.append(String.format("%-20s : %s%n", "From City", flightDetails[0]));
	    sb.append(String.format("%-20s : %s%n", "From Code", flightDetails[1]));
	    sb.append(String.format("%-20s : %s%n", "To City", flightDetails[2]));
	    sb.append(String.format("%-20s : %s%n", "To Code", flightDetails[3]));
	    sb.append(String.format("%-20s : %s%n", "Departure Date", flightDetails[4]));
	    sb.append(String.format("%-20s : %s%n", "Airlines", flightDetails[5]));
	    sb.append(String.format("%-20s : %s%n", "Flight Numbers", flightDetails[6]));
	    sb.append(String.format("%-20s : %s%n", "Aircraft", flightDetails[7]));
	    sb.append(String.format("%-20s : %s%n", "Operator", flightDetails[8]));
	    sb.append(String.format("%-20s : %s%n", "Departure Time", flightDetails[9]));
	    sb.append(String.format("%-20s : %s%n", "Arrival Time", flightDetails[10]));
	    sb.append(String.format("%-20s : %s%n", "Fare Types", flightDetails[11]));
	    sb.append(String.format("%-20s : %s%n", "Booking Classes", flightDetails[12]));
	    sb.append(String.format("%-20s : %s%n", "Check-in Baggage", flightDetails[13]));
	    sb.append(String.format("%-20s : %s%n", "Cabin Baggage", flightDetails[14]));
	    sb.append(String.format("%-20s : %s%n", "Arrival Date", flightDetails[15]));

	    sb.append("\n"); // Blank line after table

	    test.log(Status.INFO, "<pre>" + sb.toString() + "</pre>");

	    return sb.toString();
	}


	 
	public void printFlightDetails(String title, String[] flightDetails) {
	    System.out.println("\n========== " + title + " ==========");
	    String[] fields = {
	        "Departure City",
	        "Departure Code",
	        "Arrival City",
	        "Arrival Code",
	        "Departure Date",
	        "Airline",
	        "Flight Number",
	        "Flight Equipment",
	        "Operated By",
	        "Departure Time",
	        "Arrival Time",
	        "Fare Type",
	        "Class Type",
	        "Check-In Baggage",
	        "Cabin Baggage",
	        "Arrival Date"
	    };

	    for (int i = 0; i < fields.length; i++) {
	        System.out.printf("%-20s : %s\n", fields[i], flightDetails[i]);
	    }
	}

	
	
	
	
	public void validateFlightCardAndFlightDetails(
		    List<String> fareTypeDetails,
		    Map<String, String> onwardFlightCard,
		    Map<String, String> returnFlightCard,
		    String[] onwardFlightDetails,
		    String[] returnFlightDetails
		) {
		    // === Fare Type Details ===
		    String expectedCheckInBaggage = fareTypeDetails.get(0);
		    System.out.println(expectedCheckInBaggage);
		    
		    String expectedCabinBaggage = fareTypeDetails.get(1);
		    System.out.println(expectedCabinBaggage);
		    
		    String expectedFareType = fareTypeDetails.get(2);
		    String expectedClassType = fareTypeDetails.get(3);

		    // === Onward Flight Card Details ===
		    String onwardCardFlightNumbers     = onwardFlightCard.get("flightNumbers");
		    String onwardCardAirlineName       = onwardFlightCard.get("airlineName");
		    String onwardCardFlightEquipment   = onwardFlightCard.get("flightEquipment");
		    String onwardCardOperatedBy        = onwardFlightCard.get("operatedBy");
		    String onwardCardDepartureTime     = onwardFlightCard.get("departureTime");
		    String onwardCardArrivalTime       = onwardFlightCard.get("arrivalTime");
		    String onwardCardDepartureLocation = onwardFlightCard.get("departureLocation");
		    String onwardCardArrivalLocation   = onwardFlightCard.get("arrivalLocation");
		    String onwardCardDepartureDate     = onwardFlightCard.get("departureDate");
		    String onwardCardArrivalDate       = onwardFlightCard.get("arrivalDate");
		    String onwardCardFlightPrice       = onwardFlightCard.get("price");
		    String onwardCardDuration          = onwardFlightCard.get("duration");
		    String onwardCardStops             = onwardFlightCard.get("stops");

		    // === Return Flight Card Details ===
		    String returnCardFlightNumbers     = returnFlightCard.get("flightNumbers");
		    String returnCardAirlineName       = returnFlightCard.get("airlineName");
		    String returnCardFlightEquipment   = returnFlightCard.get("flightEquipment");
		    String returnCardOperatedBy        = returnFlightCard.get("operatedBy");
		    String returnCardDepartureTime     = returnFlightCard.get("departureTime");
		    String returnCardArrivalTime       = returnFlightCard.get("arrivalTime");
		    String returnCardDepartureLocation = returnFlightCard.get("departureLocation");
		    String returnCardArrivalLocation   = returnFlightCard.get("arrivalLocation");
		    String returnCardDepartureDate     = returnFlightCard.get("departureDate");
		    String returnCardArrivalDate       = returnFlightCard.get("arrivalDate");
		    String returnCardFlightPrice       = returnFlightCard.get("price");
		    String returnCardDuration          = returnFlightCard.get("duration");
		    String returnCardStops             = returnFlightCard.get("stops");

		    // === Onward Modal Details ===
		    String onwardModalDepartureCity     = onwardFlightDetails[0];
		    String onwardModalDepartureCode     = onwardFlightDetails[1];
		    String onwardModalArrivalCity       = onwardFlightDetails[2];
		    String onwardModalArrivalCode       = onwardFlightDetails[3];
		    String onwardModalDepartureDate     = onwardFlightDetails[4];
		    String onwardModalAirlineNames      = onwardFlightDetails[5];
		    String onwardModalFlightNumbers     = onwardFlightDetails[6];
		    String onwardModalFlightEquipments  = onwardFlightDetails[7];
		    String onwardModalOperatedBy        = onwardFlightDetails[8];
		    String onwardModalDepartureTime     = onwardFlightDetails[9];
		    String onwardModalArrivalTime       = onwardFlightDetails[10];
		    String onwardModalFareType          = onwardFlightDetails[11];
		    String onwardModalClassType         = onwardFlightDetails[12];
		    String onwardModalCheckInBaggage    = onwardFlightDetails[13];
		    String onwardModalCabinBaggage      = onwardFlightDetails[14];
		    String onwardModalArrivalDate       = onwardFlightDetails[15];

		    // === Return Modal Details ===
		    String returnModalDepartureCity     = returnFlightDetails[0];
		    String returnModalDepartureCode     = returnFlightDetails[1];
		    String returnModalArrivalCity       = returnFlightDetails[2];
		    String returnModalArrivalCode       = returnFlightDetails[3];
		    String returnModalDepartureDate     = returnFlightDetails[4];
		    String returnModalAirlineNames      = returnFlightDetails[5];
		    String returnModalFlightNumbers     = returnFlightDetails[6];
		    String returnModalFlightEquipments  = returnFlightDetails[7];
		    String returnModalOperatedBy        = returnFlightDetails[8];
		    String returnModalDepartureTime     = returnFlightDetails[9];
		    String returnModalArrivalTime       = returnFlightDetails[10];
		    String returnModalFareType          = returnFlightDetails[11];
		    String returnModalClassType         = returnFlightDetails[12];
		    String returnModalCheckInBaggage    = returnFlightDetails[13];
		    String returnModalCabinBaggage      = returnFlightDetails[14];
		    String returnModalArrivalDate       = returnFlightDetails[15];
		}
	
	public void validateSegmentDetails(
		    String segmentLabel,
		    Map<String, String> cardDetails,
		    String[] modalDetails,
		    List<String> fareTypeDetails,
		    ExtentTest test,
		    WebDriver driver
		) throws InterruptedException {

		    boolean hasMismatch = false;
		    StringBuilder detailedComparison = new StringBuilder();

		    // Fare Details
		    String checkin = fareTypeDetails.get(0);
		    String cabin = fareTypeDetails.get(1);
		    String fareTypeName = fareTypeDetails.get(2);
		    String fareClass = fareTypeDetails.get(3);

		    // Map values
		    String flightNoStr = cardDetails.get("flightNumbers");
		    String airLineName = cardDetails.get("airlineName");
		    String flightEqStr = cardDetails.get("flightEquipment");
		    String cleanAirlinesStr = cardDetails.get("operatedBy");
		    String flightDeptTime = cardDetails.get("departureTime");
		    String flightArrivalTime = cardDetails.get("arrivalTime");
		    System.out.println(flightArrivalTime);
		    String flightDeptLocation2 = cardDetails.get("departureLocation");
		    String flightArrivalLocation2 = cardDetails.get("arrivalLocation");
		    String flightDeptDate = cardDetails.get("departureDate");
		    String flightArrivalDate = cardDetails.get("arrivalDate");
		    String flightPrice = cardDetails.get("price");
		    String totalDuration = cardDetails.get("duration");
		    String stop2 = cardDetails.get("stops");

		    // Modal values
		    String city1 = modalDetails[0];
		    String code1 = modalDetails[1];
		    String city2 = modalDetails[2];
		    String code2 = modalDetails[3];
		    String departDateConcat = modalDetails[4];
		    String airlinesStr = modalDetails[5];
		    String flightNumbersStr = modalDetails[6];
		    String flightEquipmentsStr = modalDetails[7];
		    String operatorNamesStr = modalDetails[8];
		    String DepartTimeText = modalDetails[9];
		    String ArrivalTimeTextt = modalDetails[10];
		    System.out.println(ArrivalTimeTextt);
		    String fareType = modalDetails[11];
		    String classType = modalDetails[12];
		    String checkIn = modalDetails[13];
		    String cabinbaggage = modalDetails[14];
		    String arrivalDate = modalDetails[15];

		    test.log(Status.INFO, "🔍 Validating " + segmentLabel + "...");
		    detailedComparison.append("<table border='1' style='border-collapse: collapse;'>");
		    detailedComparison.append("<tr><th>Field</th><th>Flight Result Card Value</th><th>Flight Details NavBar Value</th><th>Status</th></tr>");
		    hasMismatch |= logCompare("Airline", airLineName, airlinesStr, detailedComparison);
		    hasMismatch |= logCompare("Flight Equipment", flightEqStr, flightEquipmentsStr, detailedComparison);
		    hasMismatch |= logCompare("Operated By", cleanAirlinesStr, operatorNamesStr, detailedComparison);
		    hasMismatch |= logCompare("Flight Numbers", flightNoStr, flightNumbersStr, detailedComparison);
		    hasMismatch |= logCompare("Departure Time", flightDeptTime, DepartTimeText, detailedComparison);
		    hasMismatch |= logCompare("Arrival Time", flightArrivalTime, ArrivalTimeTextt, detailedComparison);
		    hasMismatch |= logCompare("Departure Location", flightDeptLocation2, code1, detailedComparison);
		    hasMismatch |= logCompare("Arrival Location", flightArrivalLocation2, code2, detailedComparison);
		    hasMismatch |= logCompare("Departure Date", flightDeptDate, departDateConcat, detailedComparison);
		    hasMismatch |= logCompare("Arrival Date", flightArrivalDate, arrivalDate, detailedComparison);
		    hasMismatch |= logCompare("Fare Type", fareTypeName, fareType, detailedComparison); 
		    hasMismatch |= logCompare("Class Type", fareClass, classType, detailedComparison);
		    hasMismatch |= logCompare("Check-In", checkin, checkIn, detailedComparison);
		    hasMismatch |= logCompare("Cabbin", cabin, cabinbaggage, detailedComparison);

		    detailedComparison.append("</table><br>");
		    detailedComparison.append("<b>Fare Details:</b><br>");
		    detailedComparison.append("Fare Type: ").append(fareTypeName)
		            .append(", Cabin: ").append(cabin)
		            .append(", Check-in: ").append(checkin)
		            .append(", Booking Class: ").append(fareClass)
		            .append("<br>");

		    if (hasMismatch) {
		        test.log(Status.FAIL, "❌ " + segmentLabel + " detail comparison failed:<br>" + detailedComparison.toString());
		        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, segmentLabel + " Mismatch", "Mismatch in " + segmentLabel.toLowerCase());
		        Assert.fail(segmentLabel + " details mismatch. See ExtentReport for breakdown.");
		    } else {
		        test.log(Status.PASS, "✅ All " + segmentLabel + " details matched successfully:<br>" + detailedComparison.toString());
		    }
		}
	/*
	private boolean logCompare(String label, String expected, String actual, StringBuilder log) {
	    boolean match;

	    switch (label.toLowerCase()) {
	        case "airline":
	            match = actual.toLowerCase().contains(expected.toLowerCase());
	            break;

	        case "check-in":
	        case "cabbin":
	            List<String> actualWeights = Arrays.stream(actual.split(","))
	                .map(val -> val.replaceAll("[^0-9]", "").trim())
	                .filter(val -> !val.isEmpty())
	                .collect(Collectors.toList());

	            String expectedWeight = expected.replaceAll("[^0-9]", "").trim();
	            match = actualWeights.stream().allMatch(w -> w.equals(expectedWeight));
	            break;

	        case "fare type":
	            String[] fareTypes = actual.split(",");
	            match = Arrays.stream(fareTypes)
	                .allMatch(f -> f.trim().equalsIgnoreCase(expected.trim()));
	            break;

	        default:
	            match = expected.trim().equalsIgnoreCase(actual.trim());
	            break;
	    }

	    log.append("<tr>")
	       .append("<td>").append(label).append("</td>")
	       .append("<td>").append(expected).append("</td>")
	       .append("<td>").append(actual).append("</td>")
	       .append("<td style='color:").append(match ? "green" : "red").append(";'>")
	       .append(match ? "PASS" : "FAIL")
	       .append("</td></tr>");

	    return !match;
	}
*/
	/*
	private boolean logCompare(String label, String expected, String actual, StringBuilder log) {
	    boolean match;

	    switch (label.toLowerCase()) {
	        case "airline":
	        case "flight equipment":
	        case "operated by":
	        case "flight numbers":
	            // Partial match: check if expected value exists in the actual (which might be comma-separated)
	            List<String> actualList = Arrays.stream(actual.split(","))
	                    .map(String::trim)
	                    .map(String::toLowerCase)
	                    .collect(Collectors.toList());

	            match = actualList.contains(expected.trim().toLowerCase());
	            break;

	        case "fare type":
	            // All values in actual must match expected
	            String[] fareTypes = actual.split(",");
	            match = Arrays.stream(fareTypes)
	                    .allMatch(f -> f.trim().equalsIgnoreCase(expected.trim()));
	            break;

	        case "check-in":
	        case "cabbin":
	            // Extract numbers (e.g., "25KG" → "25") and compare
	            List<String> actualWeights = Arrays.stream(actual.split(","))
	                    .map(val -> val.replaceAll("[^0-9]", "").trim())
	                    .filter(val -> !val.isEmpty())
	                    .collect(Collectors.toList());

	            String expectedWeight = expected.replaceAll("[^0-9]", "").trim();
	            match = actualWeights.stream().allMatch(w -> w.equals(expectedWeight));
	            break;

	        default:
	            // Simple exact match
	            match = expected.trim().equalsIgnoreCase(actual.trim());
	            break;
	    }

	    log.append("<tr>")
	       .append("<td>").append(label).append("</td>")
	       .append("<td>").append(expected).append("</td>")
	       .append("<td>").append(actual).append("</td>")
	       .append("<td style='color:").append(match ? "green" : "red").append(";'>")
	       .append(match ? "PASS" : "FAIL")
	       .append("</td></tr>");

	    return !match;
	}
*/
//	private boolean logCompare(String label, String expected, String actual, StringBuilder log) {
//	    boolean match;
//
//	    switch (label.toLowerCase()) {
//	        case "airline":
//	        case "flight equipment":
//	        case "operated by":
//	        case "flight numbers":
//	            // Compare lists ignoring order and case
//	            Set<String> expectedSet = Arrays.stream(expected.split(","))
//	                    .map(String::trim)
//	                    .map(String::toLowerCase)
//	                    .collect(Collectors.toSet());
//
//	            Set<String> actualSet = Arrays.stream(actual.split(","))
//	                    .map(String::trim)
//	                    .map(String::toLowerCase)
//	                    .collect(Collectors.toSet());
//
//	            match = expectedSet.equals(actualSet);
//	            break;
//
//	        case "fare type":
//	            // All values in actual must match expected
//	            String[] fareTypes = actual.split(",");
//	            match = Arrays.stream(fareTypes)
//	                    .allMatch(f -> f.trim().equalsIgnoreCase(expected.trim()));
//	            break;
//
//	        case "check-in":
//	        case "cabbin":
//	            // Extract numeric weights and compare
//	            List<String> actualWeights = Arrays.stream(actual.split(","))
//	                    .map(val -> val.replaceAll("[^0-9]", "").trim())
//	                    .filter(val -> !val.isEmpty())
//	                    .collect(Collectors.toList());
//
//	            String expectedWeight = expected.replaceAll("[^0-9]", "").trim();
//	            match = actualWeights.stream().allMatch(w -> w.equals(expectedWeight));
//	            break;
//
//	        default:
//	            match = expected.trim().equalsIgnoreCase(actual.trim());
//	            break;
//	    }
//
//	    log.append("<tr>")
//	       .append("<td>").append(label).append("</td>")
//	       .append("<td>").append(expected).append("</td>")
//	       .append("<td>").append(actual).append("</td>")
//	       .append("<td style='color:").append(match ? "green" : "red").append(";'>")
//	       .append(match ? "PASS" : "FAIL")
//	       .append("</td></tr>");
//
//	    return !match;
//	}
	private boolean logCompare(String label, String expected, String actual, StringBuilder log) {
	    boolean match;
 
	    switch (label.toLowerCase()) {
	        case "airline":
	        case "flight equipment":
	        case "operated by":
	        case "flight numbers":
	            // Compare lists ignoring order and case
	            Set<String> expectedSet = Arrays.stream(expected.split(","))
	                    .map(String::trim)
	                    .map(String::toLowerCase)
	                    .collect(Collectors.toSet());
 
	            Set<String> actualSet = Arrays.stream(actual.split(","))
	                    .map(String::trim)
	                    .map(String::toLowerCase)
	                    .collect(Collectors.toSet());
 
	            match = expectedSet.equals(actualSet);
	            break;
 
	        case "fare type":
	            // All values in actual must match expected
	            String[] fareTypes = actual.split(",");
	            match = Arrays.stream(fareTypes)
	                    .allMatch(f -> f.trim().equalsIgnoreCase(expected.trim()));
	            break;
 
	        case "class type":
	        case "class":
	            // Compare sets – PASS if either contains all of the other
	            Set<String> expectedClasses = Arrays.stream(expected.split(","))
	                    .map(String::trim)
	                    .map(String::toUpperCase)
	                    .collect(Collectors.toSet());
 
	            Set<String> actualClasses = Arrays.stream(actual.split(","))
	                    .map(String::trim)
	                    .map(String::toUpperCase)
	                    .collect(Collectors.toSet());
 
	            match = actualClasses.containsAll(expectedClasses) || expectedClasses.containsAll(actualClasses);
	            break;
 
	        case "check-in":
	        case "cabbin":
	            // Extract numeric weights and compare
	            List<String> actualWeights = Arrays.stream(actual.split(","))
	                    .map(val -> val.replaceAll("[^0-9]", "").trim())
	                    .filter(val -> !val.isEmpty())
	                    .collect(Collectors.toList());
 
	            String expectedWeight = expected.replaceAll("[^0-9]", "").trim();
	            match = actualWeights.stream().allMatch(w -> w.equals(expectedWeight));
	            break;
 
	        default:
	            // Default: direct string comparison ignoring case and whitespace
	            match = expected.trim().equalsIgnoreCase(actual.trim());
	            break;
	    }
 
	    // Append result to HTML log
	    log.append("<tr>")
	       .append("<td>").append(label).append("</td>")
	       .append("<td>").append(expected).append("</td>")
	       .append("<td>").append(actual).append("</td>")
	       .append("<td style='color:").append(match ? "green" : "red").append(";'>")
	       .append(match ? "PASS" : "FAIL")
	       .append("</td></tr>");
 
	    // Return true if values did NOT match
	    return !match;
	}

	public void validateRoundTripDetails(
	        Map<String, String> onwardFlightCard,
	        String[] onwardFlightDetails,
	        Map<String, String> returnFlightCard,
	        String[] returnFlightDetails,
	        List<String> onwardFareTypeDetails,
	        List<String> returnFareTypeDetails,
	        ExtentTest test,
	        WebDriver driver) throws InterruptedException {

	    // Validate onward segment
	    validateSegmentDetails("Onward Flight", onwardFlightCard, onwardFlightDetails, onwardFareTypeDetails, test, driver);

	    // Validate return segment
	    validateSegmentDetails("Return Flight", returnFlightCard, returnFlightDetails, returnFareTypeDetails, test, driver);
	}

	public void clickOnFareBreakUp( ExtentTest test) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        WebElement fareSummaryButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Fare Breakup']")));
	        fareSummaryButton.click();
	        test.log(Status.PASS, "✅ Clicked on 'Fare BreakUp' button successfully.");
	    } catch (TimeoutException e) {
	        test.log(Status.FAIL, "❌ 'Fare BreakUp' button was not clickable within timeout.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Fare BreakUp Click Failure", "Button not clickable or not found.");
	        Assert.fail("Failed to click on 'Fare BreakUp' button.");
	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Unexpected error clicking 'Fare BreakUp': " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Unexpected Click Error", e.getMessage());
	        Assert.fail("Unexpected error during Fare BreakUp click.");
	    }
	}

}
