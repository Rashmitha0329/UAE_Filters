package com.skytraveller.pageObjects;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
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

public class FlightDetails extends BasePage  {
	public FlightDetails(WebDriver driver) {
		super(driver);// calls BasePage constructor
	}
	
	/*
	public void validateFlightDetailsBasedOnFlightCard(String[] userEnteredData,ExtentTest test) {
	    try {
	        String onWardLocation = userEnteredData[0];          // e.g., BLR
	        String returnLocation = userEnteredData[1];          // e.g., HAM
	        String userEnteredDate = userEnteredData[2];         // e.g., 20 Aug, 2025
	        String travelClass = userEnteredData[userEnteredData.length - 1]; // Last item assumed to be travel class

	        
	        // Extract airport codes from sector span
	        String sectorText = driver.findElement(By.xpath("//*[@class='sector-span']")).getText(); // e.g., Bengaluru(BLR) -> Hamburg(HAM)
	        String[] airportCodes = sectorText.split("\\(");
	        String fromCode = airportCodes[1].split("\\)")[0].trim();
	        String toCode = airportCodes[2].split("\\)")[0].trim();

	        // Extract and format travel date
	        String dateText = driver.findElement(By.xpath("//*[@class='date-span']")).getText(); // e.g., One Way Wed, 20 Aug, 2025
	        String[] parts = dateText.split(",");
	        String formattedDate = parts[1].trim() + ", " + parts[2].trim(); // e.g., 20 Aug, 2025

	        // Extract travel class from flight card
	        String flightTravelClass = driver.findElement(By.xpath("(//*[@class='d-flex gap-sm-2 flex-sm-row flex-column flex-wrap gap-1']/span)[1]")).getText().trim();

	        // Validation logs n
	        if (fromCode.equalsIgnoreCase(onWardLocation) &&
	            toCode.equalsIgnoreCase(returnLocation) &&
	            formattedDate.equals(userEnteredDate) &&
	            flightTravelClass.equalsIgnoreCase(travelClass)) {
//Note travell class id need to add so we can use loop for that
	            test.log(Status.PASS, "Flight details matched successfully.");
	            test.log(Status.INFO, "From: " + fromCode + ", To: " + toCode + ", Date: " + formattedDate + ", Class: " + flightTravelClass);
	          
	        } else {
	            test.log(Status.FAIL, "Flight details mismatch.");
	            test.log(Status.INFO, "Expected -> From: " + onWardLocation + ", To: " + returnLocation + ", Date: " + userEnteredDate + ", Class: " + travelClass);
	            test.log(Status.INFO, "Actual   -> From: " + fromCode + ", To: " + toCode + ", Date: " + formattedDate + ", Class: " + flightTravelClass);
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
//	public void validateFlightDetailsBasedOnFlightCard(String[] userEnteredData, ExtentTest test) {
//	    try {
//	        String onWardLocation = userEnteredData[0];          // e.g., BLR
//	        String returnLocation = userEnteredData[1];          // e.g., HAM
//	        String userEnteredDate = userEnteredData[2];         // e.g., 20 Aug, 2025
//	        String travelClass = userEnteredData[userEnteredData.length - 1]; // ECONOMY, BUSINESS, FIRST
//
//	        // === 1. Extract airport codes from sector-span ===
//	        String sectorText = driver.findElement(By.xpath("//*[@class='sector-span']")).getText(); // e.g., Bengaluru(BLR) -> Hamburg(HAM)
//	        String[] airportCodes = sectorText.split("\\(");
//	        String fromCode = airportCodes[1].split("\\)")[0].trim(); // BLR
//	        String toCode = airportCodes[2].split("\\)")[0].trim();   // HAM
//
//	        // === 2. Extract and format travel date ===
//	        String dateText = driver.findElement(By.xpath("//*[@class='date-span']")).getText(); // e.g., One Way Wed, 20 Aug, 2025
//	        String[] parts = dateText.split(",");
//	        String formattedDate = parts[1].trim() + ", " + parts[2].trim(); // e.g., 20 Aug, 2025
//
//	        // === 3. Extract travel class ===
//	        List<WebElement> classElements = driver.findElements(By.xpath("//*[@class='d-flex gap-sm-2 flex-sm-row flex-column flex-wrap gap-1']/span"));
//	        String flightTravelClass = "";
//	        for (WebElement el : classElements) {
//	            String text = el.getText().trim();
//	            if (text.equalsIgnoreCase("ECONOMY") || text.equalsIgnoreCase("BUSINESS") || text.equalsIgnoreCase("FIRST")) {
//	                flightTravelClass = text;
//	                break;
//	            }
//	        }
//
//	        if (flightTravelClass.isEmpty()) {
//	            test.log(Status.FAIL, "Travel class not found on flight card.");
//	            Assert.fail();
//	            return;
//	        }
//
//	        // === 4. Compare values and store in table ===
//	        List<String[]> table = new ArrayList<>();
//	        boolean allMatch = true;
//
//	        allMatch &= addComparisonRow(table, "From Location", fromCode, onWardLocation);
//	        allMatch &= addComparisonRow(table, "To Location", toCode, returnLocation);
//	        allMatch &= addComparisonRow(table, "Travel Date", formattedDate, userEnteredDate);
//	        allMatch &= addComparisonRow(table, "Travel Class", flightTravelClass, travelClass);
//
//	        // === 5. Generate HTML table for ExtentReports ===
//	        StringBuilder html = new StringBuilder();
//	        html.append("<table border='1' style='border-collapse: collapse; font-family: monospace;'>")
//	            .append("<tr style='background-color:#f2f2f2;'><th>Field</th><th>Card Value</th><th>Expected Value</th><th>Status</th></tr>");
//
//	        for (String[] row : table) {
//	            String bgColor = row[3].equals("PASS") ? "#d4edda" : "#f8d7da";
//	            html.append("<tr style='background-color:").append(bgColor).append(";'>");
//	            for (String col : row) {
//	                html.append("<td style='padding: 6px;'>").append(col).append("</td>");
//	            }
//	            html.append("</tr>");
//	        }
//	        html.append("</table>");
//
//	        // === 6. Final Log ===
//	        if (allMatch) {
//	            test.log(Status.PASS, "Flight details matched successfully.");
//	        } else {
//	            test.log(Status.FAIL, "Mismatch found in one or more flight details.");
//	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Detail Mismatch", "Mismatch in flight card details");
//	        }
//
//	        test.log(Status.INFO, html.toString());
//
//	        if (!allMatch) Assert.fail();
//
//	    } catch (Exception e) {
//	        test.log(Status.FAIL, "Exception while validating flight details: " + e.getMessage());
//	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception", "Error during validation");
//	        Assert.fail();
//	    }
//	}
//
//	// === Helper Method ===
//	private boolean addComparisonRow(List<String[]> table, String field, String actual, String expected) {
//	    boolean match = actual.trim().equalsIgnoreCase(expected.trim());
//	    String status = match ? "PASS" : "FAIL";
//	    table.add(new String[]{field, actual, expected, status});
//	    return match;
//	}
	public void validateFlightDetailsBasedOnFlightCard(String[] userEnteredData, ExtentTest test) {
	    try {
	        String onWardLocation = userEnteredData[0];          // e.g., BLR
	        String returnLocation = userEnteredData[1];          // e.g., HAM
	        String userEnteredDate = userEnteredData[2];         // e.g., 20 Aug, 2025
	        String travelClass = userEnteredData[userEnteredData.length - 1]; // ECONOMY, BUSINESS, FIRST
 
	        // === 1. Extract airport codes from sector-span ===
	        String sectorText = driver.findElement(By.xpath("//*[@class='sector-span']")).getText(); // e.g., Bengaluru(BLR) -> Hamburg(HAM)
	        String[] airportCodes = sectorText.split("\\(");
	        String fromCode = airportCodes[1].split("\\)")[0].trim(); // BLR
	        String toCode = airportCodes[2].split("\\)")[0].trim();   // HAM
 
	        // === 2. Extract and format travel date ===
	        String dateText = driver.findElement(By.xpath("//*[@class='date-span']")).getText(); // e.g., One Way Wed, 20 Aug, 2025
	        String[] parts = dateText.split(",");
	        String formattedDate = parts[1].trim() + ", " + parts[2].trim(); // e.g., 20 Aug, 2025
 
	        // === 3. Extract travel class ===
	    //    List<WebElement> classElements = driver.findElements(By.xpath("//*[@class='d-flex gap-sm-2 flex-sm-row flex-column flex-wrap gap-1']/span"));
	        List<WebElement> classElements = driver.findElements(By.xpath("//span[@class='flight-perfclass']"));
	        String flightTravelClass = "";
	        for (WebElement el : classElements) {
	            String text = el.getText().trim();
	            if (text.equalsIgnoreCase("ECONOMY") || text.equalsIgnoreCase("BUSINESS") || text.equalsIgnoreCase("FIRST")) {
	                flightTravelClass = text;
	                break;
	            }
	        }
 
	        if (flightTravelClass.isEmpty()) {
	            test.log(Status.FAIL, "Travel class not found on flight card.");
	            Assert.fail();
	            return;
	        }
 
	        // === 4. Compare values and store in table ===
	        List<String[]> table = new ArrayList<>();
	        boolean allMatch = true;
 
	        allMatch &= addComparisonRow(table, "From Location", fromCode, onWardLocation);
	        allMatch &= addComparisonRow(table, "To Location", toCode, returnLocation);
	        allMatch &= addComparisonRow(table, "Travel Date", formattedDate, userEnteredDate);
	        allMatch &= addComparisonRow(table, "Travel Class", flightTravelClass, travelClass);
 
	        // === 5. Generate HTML table for ExtentReports ===
	        StringBuilder html = new StringBuilder();
	        html.append("<table border='1' style='border-collapse: collapse; font-family: monospace;'>")
	           // .append("<tr style='background-color:#f2f2f2;'><th>Field</th><th>Card Value</th><th>Expected Value</th><th>Status</th></tr>");
	        .append("<tr style='background-color:#f2f2f2;'><th>Field</th><th>Flight Details Nav-Bar Value</th><th>UserEnterValue</th><th>Status</th></tr>");
 
 
	        for (String[] row : table) {
	            String bgColor = row[3].equals("PASS") ? "#d4edda" : "#f8d7da";
	            html.append("<tr style='background-color:").append(bgColor).append(";'>");
	            for (String col : row) {
	                html.append("<td style='padding: 6px;'>").append(col).append("</td>");
	            }
	            html.append("</tr>");
	        }
	        html.append("</table>");
 
	        // === 6. Final Log ===
	        if (allMatch) {
	            test.log(Status.PASS, "Flight details matched successfully.");
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
 
	// === Helper Method ===
	private boolean addComparisonRow(List<String[]> table, String field, String actual, String expected) {
	    boolean match = actual.trim().equalsIgnoreCase(expected.trim());
	    String status = match ? "PASS" : "FAIL";
	    table.add(new String[]{field, actual, expected, status});
	    return match;
	}

	public void closeNavBar(ExtentTest test) {
	    try {
	        WebElement closeButton = driver.findElement(By.xpath("//button[@class='btn-close']"));

	        if (closeButton.isDisplayed()) {
	            closeButton.click();
	            System.out.println("✅ Navigation bar closed successfully.");
	            test.log(Status.INFO, "✅ Navigation bar closed successfully.");
	        } else {
	        	System.out.println("⚠️ Close button is not visible.");
	            test.log(Status.WARNING, "⚠️ Close button is not visible.");
	            Assert.fail();
	        }
	    } catch (NoSuchElementException e) {
	        test.log(Status.FAIL, "❌ Close button not found.");
	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Failed to close navigation bar. Error: " + e.getMessage());
	    }
	}

	
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
	 List<WebElement> baggage = driver.findElements(By.xpath("//span[@class='flight-baggage']"));
	 ArrayList<String> checkInbaggage = new ArrayList<>();

	 for (WebElement baggages1 : baggage) {
	     String baggages = baggages1.getText().trim();
	     checkInbaggage.add(baggages);

	     System.out.println(baggages);
	 }

	 System.out.println("Final Check-in baggage list: " + checkInbaggage);

	 Thread.sleep(2000);

	 List<WebElement> cabin = driver.findElements(By.xpath("//span[@class='flight-baggage']/following-sibling::span"));
	 ArrayList<String> cabinBaggage = new ArrayList<>();

	 for (WebElement cabin1 : cabin) {
	     String cabinbaggages = cabin1.getText().trim();
	     cabinBaggage.add(cabinbaggages);  // ✅ fixed here

	     System.out.println(cabinbaggages);
	 }

	 System.out.println("Final Cabin baggage list: " + cabinBaggage);  // ✅ fixed here

	 WebElement dateElement = driver.findElement(By.xpath("(//span[@class='flight-arrdate'])[last()]"));
	 String fullDate = dateElement.getText();
	 String arrivalDate = fullDate.replaceAll("^\\(\\w+,\\s*", "").replaceAll("\\)$", "");

	 System.out.println(arrivalDate);  // Output: 24 Aug, 2025

	 List<WebElement> departTerminalFlightDetail = driver.findElements(By.xpath("//span[@class='flgiht-depterminal']"));
	    WebElement departTerminalFlightDetails = departTerminalFlightDetail.get(0);
	    String[] departTerminalFlightDetails1 = departTerminalFlightDetails.getText().split("-");
	    String departTerminalFlightDetails2 = departTerminalFlightDetails1[1].trim();
	  //  terminalData.put("departDetails", departTerminalFlightDetails2);
	    System.out.println(departTerminalFlightDetails2);
	 
	    List<WebElement> arrivalTerminalFlightDetail = driver.findElements(By.xpath("//span[@class='flgiht-arrterminal']"));
	    WebElement arrivalTerminalFlightDetails = arrivalTerminalFlightDetail.get(arrivalTerminalFlightDetail.size() - 1);
	    String[] arrivalTerminalFlightDetails1 = arrivalTerminalFlightDetails.getText().split("-");
	    String arrivalTerminalFlightDetails2 = arrivalTerminalFlightDetails1[1].trim();
	   // terminalData.put("arrivalDetails", arrivalTerminalFlightDetails2);
	    System.out.println(arrivalTerminalFlightDetails2);
	 
	    List<WebElement> getAllDepartElements = driver.findElements(By.xpath("//span[@class='flgiht-depterminal']"));
	    List<String> getAllDepartTerminal = new ArrayList<>();
	    for (WebElement el : getAllDepartElements) {
	        String[] parts = el.getText().split("-");
	        getAllDepartTerminal.add(parts[1].trim());
	    }
	    String joinedDepart = String.join(", ", getAllDepartTerminal);
	    //terminalData.put("departAll", joinedDepart);
	    System.out.println(joinedDepart);
	 
	    List<WebElement> getAllArrivalElements = driver.findElements(By.xpath("//span[@class='flgiht-arrterminal']"));
	    List<String> getAllArrivalTerminal = new ArrayList<>();
	    for (WebElement el : getAllArrivalElements) {
	        String[] parts = el.getText().split("-");
	        getAllArrivalTerminal.add(parts[1].trim());
	    }
	    String joinedArrival = String.join(", ", getAllArrivalTerminal);
	   // terminalData.put("arrivalAll", joinedArrival);
	    System.out.println(joinedArrival);
	
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
	        DepartTimeText, ArrivalTimeTextt ,FareStr,bookingclsStr,checkInbaggage1,cabinBaggage1,arrivalDate,departTerminalFlightDetails2,arrivalTerminalFlightDetails2,joinedDepart,joinedArrival
	    };
	}
 
	/*
	public void validateFlightCardAndFlightDetails(Map<String, String> GetFlightCardDetailsReturned,String[] getFlightDetailsReturned,List<String> fareTypeDetails) throws InterruptedException
	{
	String	checkin= fareTypeDetails.get(0);
	String cabin	= fareTypeDetails.get(1);
		String fareTypeName= fareTypeDetails.get(2);
		 String fareClass=fareTypeDetails.get(3);
		
		
		String flightNoStr = GetFlightCardDetailsReturned.get("flightNumbers");
		String airLineName = GetFlightCardDetailsReturned.get("airlineName");
		String flightEqStr = GetFlightCardDetailsReturned.get("flightEquipment");
		String cleanAirlinesStr = GetFlightCardDetailsReturned.get("operatedBy");
		String flightDeptTime = GetFlightCardDetailsReturned.get("departureTime");
		String flightArrivalTime = GetFlightCardDetailsReturned.get("arrivalTime");
		String flightDeptLocation2 = GetFlightCardDetailsReturned.get("departureLocation");
		String flightArrivalLocation2 = GetFlightCardDetailsReturned.get("arrivalLocation");
		String flightDeptDate = GetFlightCardDetailsReturned.get("departureDate");
		String flightArrivalDate = GetFlightCardDetailsReturned.get("arrivalDate");
		String flightPrice = GetFlightCardDetailsReturned.get("price");
		String totalDuration = GetFlightCardDetailsReturned.get("duration");
		String stop2 = GetFlightCardDetailsReturned.get("stops");

	//------------------------------------------------------------------------------------------
		
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
	 
		if(flightDeptTime.equals(DepartTimeText) && flightArrivalTime.equals(ArrivalTimeTextt) && flightDeptLocation2.equals(code1) && flightArrivalLocation2.equals(code2) && flightDeptDate.equals(departDateConcat))
		{
			System.out.println("Pass");
		}
		
		if()
	 
	}
 */
	/*
	public void validateFlightCardAndFlightDetails(Map<String, String> GetFlightCardDetailsReturned, String[] getFlightDetailsReturned, List<String> fareTypeDetails) throws InterruptedException {
	    
	    // Extract Fare Type Details
	    String checkin = fareTypeDetails.get(0);
	    String cabin = fareTypeDetails.get(1);
	    String fareTypeName = fareTypeDetails.get(2);
	    String fareClass = fareTypeDetails.get(3);
	    
	    // Extract Flight Card Details
	    String flightNoStr = GetFlightCardDetailsReturned.get("flightNumbers");
	    String airLineName = GetFlightCardDetailsReturned.get("airlineName");
	    String flightEqStr = GetFlightCardDetailsReturned.get("flightEquipment");
	    String cleanAirlinesStr = GetFlightCardDetailsReturned.get("operatedBy");
	    String flightDeptTime = GetFlightCardDetailsReturned.get("departureTime");
	    String flightArrivalTime = GetFlightCardDetailsReturned.get("arrivalTime");
	    String flightDeptLocation2 = GetFlightCardDetailsReturned.get("departureLocation");
	    String flightArrivalLocation2 = GetFlightCardDetailsReturned.get("arrivalLocation");
	    String flightDeptDate = GetFlightCardDetailsReturned.get("departureDate");
	    String flightArrivalDate = GetFlightCardDetailsReturned.get("arrivalDate");
	    String flightPrice = GetFlightCardDetailsReturned.get("price");
	    String totalDuration = GetFlightCardDetailsReturned.get("duration");
	    String stop2 = GetFlightCardDetailsReturned.get("stops");
	    
	    // Extract Modal Flight Details
	    String city1 = getFlightDetailsReturned[0];
	    String code1 = getFlightDetailsReturned[1];
	    String city2 = getFlightDetailsReturned[2];
	    String code2 = getFlightDetailsReturned[3];
	    String departDateConcat = getFlightDetailsReturned[4];
	    String airlinesStr = getFlightDetailsReturned[5];
	    String flightNumbersStr = getFlightDetailsReturned[6];
	    String flightEquipmentsStr = getFlightDetailsReturned[7];
	    String operatorNamesStr = getFlightDetailsReturned[8];
	    String DepartTimeText = getFlightDetailsReturned[9];
	    String ArrivalTimeTextt = getFlightDetailsReturned[10];
	    
	    System.out.println("============= FLIGHT CARD VS MODAL VALIDATION START =============");
	    
	    System.out.println("Airline Name: " + airLineName + " | Expected in: " + airlinesStr);
	    if (airlinesStr.contains(airLineName)) {
	        System.out.println("✅ Airline matched.");
	    } else {
	        System.out.println("❌ Airline mismatch.");
	    }

	    System.out.println("Flight Equipment: " + flightEqStr + " | Expected in: " + flightEquipmentsStr);
	    if (flightEquipmentsStr.contains(flightEqStr)) {
	        System.out.println("✅ Flight Equipment matched.");
	    } else {
	        System.out.println("❌ Flight Equipment mismatch.");
	    }

	    System.out.println("Operated By: " + cleanAirlinesStr + " | Expected in: " + operatorNamesStr);
	    if (operatorNamesStr.contains(cleanAirlinesStr)) {
	        System.out.println("✅ Operated By matched.");
	    } else {
	        System.out.println("❌ Operated By mismatch.");
	    }

	    System.out.println("Flight Number: " + flightNoStr + " | Expected in: " + flightNumbersStr);
	    if (flightNumbersStr.contains(flightNoStr)) {
	        System.out.println("✅ Flight Number matched.");
	    } else {
	        System.out.println("❌ Flight Number mismatch.");
	    }

	    System.out.println("Departure Time: " + flightDeptTime + " | Modal: " + DepartTimeText);
	    System.out.println("Arrival Time: " + flightArrivalTime + " | Modal: " + ArrivalTimeTextt);
	    System.out.println("Departure Location Code: " + flightDeptLocation2 + " | Modal: " + code1);
	    System.out.println("Arrival Location Code: " + flightArrivalLocation2 + " | Modal: " + code2);
	    System.out.println("Departure Date: " + flightDeptDate + " | Modal: " + departDateConcat);

	    if (flightDeptTime.equals(DepartTimeText)) {
	        System.out.println("✅ Departure Time matched.");
	    } else {
	        System.out.println("❌ Departure Time mismatch.");
	    }

	    if (flightArrivalTime.equals(ArrivalTimeTextt)) {
	        System.out.println("✅ Arrival Time matched.");
	    } else {
	        System.out.println("❌ Arrival Time mismatch.");
	    }

	    if (flightDeptLocation2.equals(code1)) {
	        System.out.println("✅ Departure Location matched.");
	    } else {
	        System.out.println("❌ Departure Location mismatch.");
	    }

	    if (flightArrivalLocation2.equals(code2)) {
	        System.out.println("✅ Arrival Location matched.");
	    } else {
	        System.out.println("❌ Arrival Location mismatch.");
	    }

	    if (flightDeptDate.equals(departDateConcat)) {
	        System.out.println("✅ Departure Date matched.");
	    } else {
	        System.out.println("❌ Departure Date mismatch.");
	    }

	    System.out.println("Fare Type: " + fareTypeName);
	    System.out.println("Cabin: " + cabin);
	    System.out.println("Check-in: " + checkin);
	    System.out.println("Booking Class: " + fareClass);

	    System.out.println("============= FLIGHT CARD VS MODAL VALIDATION END =============");
	}
*/
	public void validateFlightCardAndFlightDetails(
		    Map<String, String> GetFlightCardDetailsReturned,
		    String[] getFlightDetailsReturned,
		    List<String> fareTypeDetails,
		    ExtentTest test,
		    WebDriver driver) throws InterruptedException {

		    boolean hasMismatch = false;
		    StringBuilder detailedComparison = new StringBuilder();

		    // Fare Details
		    String checkin = fareTypeDetails.get(0);
		    System.out.println(checkin);
		    String cabin = fareTypeDetails.get(1);
		    System.out.println(cabin);
		    String fareTypeName = fareTypeDetails.get(2);
		    String fareClass = fareTypeDetails.get(3);

		    
		    // Map values
		    String flightNoStr = GetFlightCardDetailsReturned.get("flightNumbers");
		    String airLineName = GetFlightCardDetailsReturned.get("airlineName");
		    String flightEqStr = GetFlightCardDetailsReturned.get("flightEquipment");
		    String cleanAirlinesStr = GetFlightCardDetailsReturned.get("operatedBy");
		    String flightDeptTime = GetFlightCardDetailsReturned.get("departureTime");
		    String flightArrivalTime = GetFlightCardDetailsReturned.get("arrivalTime");
		    String flightDeptLocation2 = GetFlightCardDetailsReturned.get("departureLocation");
		    String flightArrivalLocation2 = GetFlightCardDetailsReturned.get("arrivalLocation");
		    String flightDeptDate = GetFlightCardDetailsReturned.get("departureDate");
		    String flightArrivalDate = GetFlightCardDetailsReturned.get("arrivalDate");
		    String flightPrice = GetFlightCardDetailsReturned.get("price");
		    String totalDuration = GetFlightCardDetailsReturned.get("duration");
		    String stop2 = GetFlightCardDetailsReturned.get("stops");

		    // Modal values
		    String city1 = getFlightDetailsReturned[0];
		    String code1 = getFlightDetailsReturned[1];
		    String city2 = getFlightDetailsReturned[2];
		    String code2 = getFlightDetailsReturned[3];
		    String departDateConcat = getFlightDetailsReturned[4];
		    String airlinesStr = getFlightDetailsReturned[5];
		    String flightNumbersStr = getFlightDetailsReturned[6];
		    String flightEquipmentsStr = getFlightDetailsReturned[7];
		    String operatorNamesStr = getFlightDetailsReturned[8];
		    String DepartTimeText = getFlightDetailsReturned[9];
		    String ArrivalTimeTextt = getFlightDetailsReturned[10];
		    String fareType = getFlightDetailsReturned[11];
		    String classType = getFlightDetailsReturned[12];
		    String checkIn = getFlightDetailsReturned[13];
		    String cabinbaggage = getFlightDetailsReturned[14];
		    String arrivalDate = getFlightDetailsReturned[15];
		   System.out.println(checkIn);
		   System.out.println(cabinbaggage);
		   
		    

		    detailedComparison.append("<table border='1' style='border-collapse: collapse;'>");
		    detailedComparison.append("<tr><th>Field</th><th>Card Value</th><th>Detail Value</th><th>Status</th></tr>");

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
		    // Arrival Date not checked here, but you can add if needed

		    detailedComparison.append("</table><br>");

		    // Log fare details
		    detailedComparison.append("<b>Fare Details:</b><br>");
		    detailedComparison.append("Fare Type: ").append(fareTypeName)
		            .append(", Cabin: ").append(cabin)
		            .append(", Check-in: ").append(checkin)
		            .append(", Booking Class: ").append(fareClass)
		            .append("<br>");

		    if (hasMismatch) {
		        test.log(Status.FAIL, "❌ Flight detail comparison failed:<br>" + detailedComparison.toString());
		        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Detail Mismatch", "Mismatch in flight card and detail modal");
		        Assert.fail("Flight details mismatch. See ExtentReport for breakdown.");
		    } else {
		        test.log(Status.PASS, "✅ All flight details matched successfully:<br>" + detailedComparison.toString());
		    }
		}
	/*
	private boolean logCompare(String label, String expected, String actual, StringBuilder log) {
	    boolean match = expected.trim().equalsIgnoreCase(actual.trim());
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
	        case "check-in":
	        case "cabbin":
	            // Case-insensitive containment check
	            match = actual.toLowerCase().contains(expected.toLowerCase());
	            break;

	        case "fare type":
	            // All values in actual should match the expected
	            String[] fareTypes = actual.split(",");
	            match = Arrays.stream(fareTypes)
	                          .allMatch(f -> f.trim().equalsIgnoreCase(expected.trim()));
	            break;

	        default:
	            // Default full string comparison
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
	            // Check if expected airline is in actual airline list
	            match = actual.toLowerCase().contains(expected.toLowerCase());
	            break;

	        case "check-in":
	        case "cabbin":
	            // Extract numeric weights from actual value
	            List<String> actualWeights = Arrays.stream(actual.split(","))
	                .map(val -> val.replaceAll("[^0-9]", "").trim())
	                .filter(val -> !val.isEmpty())
	                .collect(Collectors.toList());

	            // Extract numeric value from expected
	            String expectedWeight = expected.replaceAll("[^0-9]", "").trim();

	            // Check if all actual weights match expected
	            match = actualWeights.stream()
	                .allMatch(w -> w.equals(expectedWeight));
	            break;

	        case "fare type":
	            // All actual fare types should match expected
	            String[] fareTypes = actual.split(",");
	            match = Arrays.stream(fareTypes)
	                .allMatch(f -> f.trim().equalsIgnoreCase(expected.trim()));
	            break;

	        default:
	            // Default full string comparison
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
	private boolean logCompare(String label, String expected, String actual, StringBuilder log) {
	    boolean match;

	    switch (label.toLowerCase()) {
	        case "airline":
	        case "flight equipment":
	        case "operated by":
	            // Use contains for both Airline and Operated By
	            match = actual.toLowerCase().contains(expected.toLowerCase());
	            break;

	        case "check-in":
	        case "cabbin":
	            List<String> actualWeights = Arrays.stream(actual.split(","))
	                .map(val -> val.replaceAll("[^0-9]", "").trim())
	                .filter(val -> !val.isEmpty())
	                .collect(Collectors.toList());

	            String expectedWeight = expected.replaceAll("[^0-9]", "").trim();

	            match = actualWeights.stream()
	                .allMatch(w -> w.equals(expectedWeight));
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

	
	
	public String getAllFlightDetails() {
	    String allText = "";

	    try {
	        WebElement flightSection = driver.findElement(By.xpath("//section[@class='selected-flight-details']"));
	        allText = flightSection.getText();
	        System.out.println("🛫 All Flight Details:\n" + allText);
	    } catch (NoSuchElementException e) {
	        System.out.println("❌ Flight details section not found.");
	    } catch (Exception e) {
	        System.out.println("❌ Unexpected error while fetching flight details: " + e.getMessage());
	    }

	    return allText;
	}

	
	
	
	
	public void fareBreakUp(String farePrice, ExtentTest test) {
	    double totalCalculated = 0.0;
	    StringBuilder fareLog = new StringBuilder();

	    totalCalculated += getFareSectionTotal("Adult", fareLog);
	    totalCalculated += getFareSectionTotal("Child", fareLog);
	    totalCalculated += getFareSectionTotal("Infant", fareLog);

	    // Clean and parse input fare price
	    double passedFare = parseAmount(farePrice);

	    fareLog.append("<br><b>🧮 Total Calculated:</b> AED ").append(totalCalculated)
	           .append("<br><b>💵 Fare Passed for Validation:</b> ").append(farePrice);

	    if (Math.abs(totalCalculated - passedFare) < 0.01) {
	        test.log(Status.PASS, "✅ Fare matched successfully.<br>" + fareLog.toString());
	    } else {
	        test.log(Status.FAIL, "❌ Fare mismatch!<br>" + fareLog.toString() +
	                 "<br><b>Difference:</b> AED " + Math.abs(totalCalculated - passedFare));
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Fare Mismatch", "Mismatch in fare breakdown and passed fare.");
	        Assert.fail("Fare mismatch detected.");
	    }
	}
	private double getFareSectionTotal(String sectionName, StringBuilder fareLog) {
	    double total = 0.0;

	    try {
	        String baseFareXpath = "//div[contains(text(),'" + sectionName + "')]/following-sibling::div//span[contains(text(),'Base Fare')]/following-sibling::span";
	        String yqFareXpath = "//div[contains(text(),'" + sectionName + "')]/following-sibling::div//span[contains(text(),'YQ')]/following-sibling::span";
	        String otherTaxXpath = "//div[contains(text(),'" + sectionName + "')]/following-sibling::div//span[contains(text(),'Other Tax')]/following-sibling::span";

	        WebElement baseFareElem = driver.findElement(By.xpath(baseFareXpath));
	        WebElement yqFareElem = driver.findElement(By.xpath(yqFareXpath));
	        WebElement otherTaxElem = driver.findElement(By.xpath(otherTaxXpath));

	        double baseFare = parseAmount(baseFareElem.getText());
	        double yqFare = parseAmount(yqFareElem.getText());
	        double otherTax = parseAmount(otherTaxElem.getText());

	        total = baseFare + yqFare + otherTax;

	        fareLog.append("<br><b>").append(sectionName).append(" Fare Breakdown:</b>")
	               .append("<br>Base Fare: AED ").append(baseFare)
	               .append("<br>YQ: AED ").append(yqFare)
	               .append("<br>Other Tax: AED ").append(otherTax)
	               .append("<br><b>Total: AED ").append(total).append("</b><br>");
	    } catch (NoSuchElementException e) {
	        fareLog.append("<br><b>⚠️ No fare section found for: ").append(sectionName).append("</b><br>");
	    }

	    return total;
	}
	private double parseAmount(String amountText) {
	    String cleaned = amountText.replaceAll("[^0-9.]", "").replace(",", "");
	    try {
	        return Double.parseDouble(cleaned);
	    } catch (NumberFormatException e) {
	        System.out.println("❌ Could not parse amount: " + amountText);
	        return 0.0;
	    }
	}

	
	public void clickOnFareSummary( ExtentTest test) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        WebElement fareSummaryButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Fare Summary']")));
	        fareSummaryButton.click();
	        test.log(Status.PASS, "✅ Clicked on 'Fare Summary' button successfully.");
	    } catch (TimeoutException e) {
	        test.log(Status.FAIL, "❌ 'Fare Summary' button was not clickable within timeout.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Fare Summary Click Failure", "Button not clickable or not found.");
	        Assert.fail("Failed to click on 'Fare Summary' button.");
	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Unexpected error clicking 'Fare Summary': " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Unexpected Click Error", e.getMessage());
	        Assert.fail("Unexpected error during Fare Summary click.");
	    }
	}

	public void clickOnFareRule( ExtentTest test) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement fareSummaryButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Fare Rules']")));
	        fareSummaryButton.click();
	        test.log(Status.PASS, "✅ Clicked on 'Fare Rules' button successfully.");
	    } catch (TimeoutException e) {
	        test.log(Status.FAIL, "❌ 'Fare  Rules' button was not clickable within timeout.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Fare Summary Click Failure", "Button not clickable or not found.");
	        Assert.fail("Failed to click on 'Fare Rules' button.");
	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Unexpected error clicking 'Fare Rules': " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Unexpected Click Error", e.getMessage());
	        Assert.fail("Unexpected error during Fare Rules click.");
	    }
	}
	
	public void validateFareRules(ExtentTest test) {
	    try {
	        WebElement fareRules = driver.findElement(By.xpath("//div[@class='fare-rules-tabel']"));

	        if (fareRules.isDisplayed()) {
	            test.log(Status.PASS, "✅ Fare Rules section is displayed.");
	        } else {
	            test.log(Status.FAIL, "❌ Fare Rules section is present but not visible.");
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Fare Rules Not Visible", "Fare Rules div is not visible.");
	            Assert.fail("Fare Rules section is not visible.");
	        }

	    } catch (NoSuchElementException e) {
	        test.log(Status.FAIL, "❌ Fare Rules section not found on the page.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Fare Rules Missing", "Fare Rules div not present in DOM.");
	        Assert.fail("Fare Rules element is missing.");
	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Unexpected error while validating Fare Rules: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Fare Rules Error", e.getMessage());
	        Assert.fail("Unexpected exception while checking Fare Rules.");
	    }
	}

	public void clickOnMiniFareRule( ExtentTest test) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement fareSummaryButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Mini Fare Rules']")));
	        fareSummaryButton.click();
	        test.log(Status.PASS, "✅ Clicked on 'Mini Fare Rules' button successfully.");
	    } catch (TimeoutException e) {
	        test.log(Status.FAIL, "❌ 'Mini Fare Rules' button was not clickable within timeout.");
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Fare Summary Click Failure", "Button not clickable or not found.");
	        Assert.fail("Failed to click on 'Fare Summary' button.");
	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Unexpected error clicking 'Mini Fare Rules': " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Unexpected Click Error", e.getMessage());
	        Assert.fail("Unexpected error during Mini Fare Rules click.");
	    }
	}

	
}
