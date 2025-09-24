package com.iween.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtilities {
	/*
	public static List<Map<String, String>> readExcelData(String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
  
        try (FileInputStream fis = new FileInputStream("src/test/resources/testdata/testdata 1.xlsx");
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<String, String> rowData = new HashMap<>();

                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                 //   String key = headerRow.getCell(j).toString().trim();
                //	DataFormatter formatter = new DataFormatter();
                //	String key = formatter.formatCellValue(headerRow.getCell(j));
                	
                	String key="";
                	 if (row.getCell(j) != null) {
                         key = row.getCell(j).toString().trim();
                     }

// DataFormatter handles:

//null cells (returns "")

//different data types (dates, numbers, text)

//consistent string output


 
                    String value = "";
                    if (row.getCell(j) != null) {
                        value = row.getCell(j).toString().trim();
                    }

                    rowData.put(key, value);
                }

                if (!rowData.containsValue("SKIP")) {
                    data.add(rowData);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
	*/
	/*
	public static List<Map<String, String>> readExcelData(String sheetName) {
	    List<Map<String, String>> data = new ArrayList<>();
	    DataFormatter formatter = new DataFormatter(); // use once, reuse for consistent formatting

	    String path="src/test/resources/testdata/testdata 1.xlsx";
	    String path2="src\\test\\resources\\testData\\TBO_AI.xlsx";
	    String path3="src\\test\\resources\\testData\\SkyTravellerUAE.xlsx";
	    String path4="src\\test\\resources\\testData\\SearchToBookingSupplierWise.xlsx";
	    String path5="src\\test\\resources\\testData\\SkyTravellerUAE1.xlsx";

	    try (FileInputStream fis = new FileInputStream(path5);
	         Workbook workbook = new XSSFWorkbook(fis)) {

	        Sheet sheet = workbook.getSheet(sheetName);
	        Row headerRow = sheet.getRow(0);

	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            Map<String, String> rowData = new HashMap<>();

	            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
	                String key = formatter.formatCellValue(headerRow.getCell(j)); // key from header
	                String value = formatter.formatCellValue(row.getCell(j)); // value from row cell

	                rowData.put(key, value);
	            }

	            boolean skip = false;
	            for (String val : rowData.values()) {
	                if ("SKIP".equalsIgnoreCase(val.trim())) {
	                    skip = true;
	                    break;
	                }
	            }
	            if (!skip) {
	                data.add(rowData);
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
*/
	
	public static List<Map<String, String>> readExcelData(String sheetName) {
	    List<Map<String, String>> data = new ArrayList<>();
	    DataFormatter formatter = new DataFormatter();

	    String path = "src\\test\\resources\\testData\\SkyTravellerUAE1.xlsx";  // your actual file path

	    try (FileInputStream fis = new FileInputStream(path);
	         Workbook workbook = new XSSFWorkbook(fis)) {

	        Sheet sheet = workbook.getSheet(sheetName);
	        Row headerRow = sheet.getRow(0);

	        if (headerRow == null) {
	            System.out.println("Header row is missing in sheet: " + sheetName);
	            return data;
	        }

	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            Map<String, String> rowData = new HashMap<>();
	            boolean isRowEmpty = true;

	            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
	                Cell cell = row.getCell(j);
	                String key = formatter.formatCellValue(headerRow.getCell(j));
	                String value = formatter.formatCellValue(cell);

	                if (!value.trim().isEmpty()) {
	                    isRowEmpty = false;
	                }

	                rowData.put(key, value);
	            }

	            // Skip row if it's completely empty
	            if (isRowEmpty) {
	                continue;
	            }

	            // Skip row if any cell is SKIP (case-insensitive)
	            boolean skip = rowData.values().stream()
	                    .anyMatch(val -> "SKIP".equalsIgnoreCase(val.trim()));

	            if (!skip) {
	                data.add(rowData);
	            } else {
	                System.out.println("Skipping row " + i + " due to SKIP flag.");
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

	/*
	public static List<Map<String, String>> readExcelData(String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        File classpathRoot = new File(System.getProperty("user.dir"));
      
           File app = new File(classpathRoot, "src\\test\\resources\\testdata\\testdata 1.xlsx");
       
        String fileName = app.toString();

        try (FileInputStream fis = new FileInputStream(fileName);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row is missing in sheet: " + sheetName);
            }

            int totalCols = headerRow.getLastCellNum();
            DataFormatter formatter = new DataFormatter();

            // Find the index of "TestRun" column
            int testRunColIndex = -1;
            for (int i = 0; i < totalCols; i++) {
                String header = formatter.formatCellValue(headerRow.getCell(i));
                if ("TestRun".equalsIgnoreCase(header)) {
                    testRunColIndex = i;
                    break;
                }
            }

            if (testRunColIndex == -1) {
                throw new RuntimeException("\"TestRun\" column not found in sheet: " + sheetName);
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Check if TestRun column value is "SKIP"
                String testRunValue = formatter.formatCellValue(row.getCell(testRunColIndex));
                if ("SKIP".equalsIgnoreCase(testRunValue.trim())) {
                    continue; // Skip this row
                }

                Map<String, String> rowMap = new HashMap<>();
                boolean hasData = false;

                for (int j = 0; j < totalCols; j++) {
                    Cell headerCell = headerRow.getCell(j);
                    if (headerCell == null) continue;

                    String key = formatter.formatCellValue(headerCell);
                    String value = formatter.formatCellValue(row.getCell(j));

                    if (value != null && !value.trim().isEmpty()) {
                        hasData = true;
                    }

                    rowMap.put(key, value);
                }

                if (hasData) {
                    dataList.add(rowMap);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }
*/
	/*
	 public static List<Map<String, String>> getExcelDataFromSheet(String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            // Get Excel file path (you can change the file here)
            File file = new File(System.getProperty("user.dir") + "/src/test/resources/testdata/AutomationTestData.xlsx");
            FileInputStream fis = new FileInputStream(file);

            // Load workbook
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                System.out.println("❌ Sheet not found: " + sheetName);
                workbook.close();
                return dataList;
            }

            Row headerRow = sheet.getRow(0);  // First row = column headers
            if (headerRow == null) {
                System.out.println("❌ Header row is missing.");
                workbook.close();
                return dataList;
            }

            int totalCols = headerRow.getLastCellNum();
            DataFormatter formatter = new DataFormatter();

            // Loop through each data row (start from row 1)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowMap = new HashMap<>();
                boolean skipRow = false;
                boolean hasData = false;

                for (int j = 0; j < totalCols; j++) {
                    Cell headerCell = headerRow.getCell(j);
                    String key = (headerCell != null) ? formatter.formatCellValue(headerCell).trim() : "";

                    Cell cell = row.getCell(j);
                    String value = (cell != null) ? formatter.formatCellValue(cell).trim() : "";

                    // Skip the row if any cell contains "TestSkip"
                    if ("TestSkip".equalsIgnoreCase(value)) {
                        skipRow = true;
                        break;
                    }

                    if (!value.isEmpty()) {
                        hasData = true;
                    }

                    rowMap.put(key, value);
                }

                if (!skipRow && hasData) {
                    dataList.add(rowMap);
                }
            }

            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }
	 */
}
