package com.iween.utilities;



import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;
public class DataProviders {

	/*
	 @DataProvider (name = "excelData")
	    public static Object[][] provideData(Method method) {
	        String className = method.getDeclaringClass().getSimpleName();  // E.g. TC_01_SearchTest
	        String sheetName = className.split("_")[0] + "_" + className.split("_")[1]; // TC_01

	        List<Map<String, String>> data = ExcelUtilities.readExcelData(sheetName);
	        Object[][] result = new Object[data.size()][1];

	        for (int i = 0; i < data.size(); i++) {
	            result[i][0] = data.get(i);
	        }

	        return result;
	    }
	/*
	 The Method object allows us to dynamically get the name of the test class at runtime using method.getDeclaringClass().getSimpleName(). This class name is then used as the sheet name in the Excel file, assuming that each test class has a corresponding Excel sheet with the same name. The data from that sheet is read and returned as a list of Map<String, String>, where each map represents one row of test data.
	 */
	/*
	 @DataProvider(name = "excelData")
	    public static Object[][] provideSheetData(Method method) {
	        String className = method.getDeclaringClass().getSimpleName();  // e.g. TC_13_TripgainFlightVerifyMaxValueInPriceRangeSlider
	        // Extract prefix like "TC_13"
	        String sheetName = className.split("_")[0] + "_" + className.split("_")[1];

	        List<Map<String, String>> testData = ExcelUtilities.readExcelData(sheetName);
	        Object[][] data = new Object[testData.size()][1];
	        for (int i = 0; i < testData.size(); i++) {
	            data[i][0] = testData.get(i);
	        }
	        return data;
	    }
	    */
	@DataProvider(name = "excelData")
    public static Object[][] provideSheetData(Method method) {
        String className = method.getDeclaringClass().getSimpleName();  // e.g. TC_13_TripgainFlightVerifyMaxValueInPriceRangeSlider
        // Extract prefix like "TC_13"
        String sheetName = className.split("_")[0] + "_" + className.split("_")[1];

        List<Map<String, String>> testData = ExcelUtilities.readExcelData(sheetName);
        Object[][] data = new Object[testData.size()][1];
        for (int i = 0; i < testData.size(); i++) {
            data[i][0] = testData.get(i);
        }
        return data;
    }
	 
}
