package org.titanium.config;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {
    public Sheet readExcel(String filePath, String fileName, String sheetName) throws IOException {

        //Create a File object  in order to open the excel file
        File file = new File(filePath + "/" + fileName);

        //Create FileInoutStream object in order to read the excel file
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workBook = null;

        //Find the file extension splitting the file name in the substring and get only the extension name
        String fileExtensionName = fileName.substring(fileName.indexOf("."));

        // Check if is an .xlsx file
        if (fileExtensionName.equals(".xlsx")) {
            //if is an .xlsx file, then create an XSSFWorbook object
            workBook = new XSSFWorkbook(inputStream);
            // Check if is an .xls file
        } else if (fileExtensionName.equals(".xls")) {
            //if is an xls file, then create an HSSFWorbook object
            workBook = new HSSFWorkbook(inputStream);
        }

        // Read the sheet inside the woorbook
        Sheet sheet = workBook.getSheet(sheetName);
        return sheet;
    }
}
