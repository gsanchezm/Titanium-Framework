package org.titanium.utils;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;
import org.titanium.config.Constants;
import org.titanium.config.Log;
import org.titanium.engine.DriverEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelProvider {
    private static XSSFSheet ExcelWSheet;
    private static XSSFWorkbook ExcelWBook;
    private static org.apache.poi.ss.usermodel.Cell Cell;
    private static XSSFRow Row;

    /**
     * This method is to read the test cases from the Excel file
     *
     * @return
     * @throws IOException
     * @throws IOException
     */
    @DataProvider(name = "GetExcelData")
    public Object[][] getTestCaseData() throws Exception {
        String[][] tabArray = null;
        try {
            // Open the excel file
            FileInputStream ExcelFile = new FileInputStream(Constants.RESOURCES_FOLDER + DriverEngine.WorkbookProperty);

            // Access to required sheet
            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.getSheet(Constants.SHEET_TEST_CASES);

            // Variables declaration
            int startRow, startCol, ci, cj;

            // Get total rows
            int totalRows = ExcelWSheet.getLastRowNum();

            // Get total columns
            int totalCols = ExcelWSheet.getRow(0).getLastCellNum();

            // Set the String array size
            tabArray = new String[totalRows][totalCols];
            ci=0;
            // startRow = 1 because the first row contains the titles
            for(startRow = 1; startRow <=totalRows; startRow++, ci++){
                cj= 0;
                // startCol = 0, all columns are needed
                for(startCol = 0; startCol<totalCols; startCol++, cj++){
                    // Fill the object array with data
                    tabArray[ci][cj] = getCellDataDDT(startRow,startCol);
                }
            }
        }catch (FileNotFoundException e){
            throw new DriverException("Class ExcelUtils | Method getTableArray | Exception desc: Excel not found: "+e.getMessage());
        }catch (IOException e){
            throw new DriverException("Class ExcelUtils | Method getTableArray | Exception desc: Could not read the Excel sheet: "+e.getMessage());
        }
        return(tabArray);
    }

    /**
     * This method is to read the test data from the Excel cell, We are going through the parameters like Row num and Col num
     *
     * @param RowNum
     * @param ColNum
     * @return
     * @throws Exception
     */
    public static String getCellDataDDT(int RowNum, int ColNum) throws Exception {
        String data = "";
        try{
            Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
            // Convert the cell data to String
            if(Cell.getCellTypeEnum()== CellType.STRING) {
                data = Cell.getStringCellValue();
            }else if(Cell == null){
                data = "";
            }else if (Cell.getCellTypeEnum()== CellType.NUMERIC){
                data = String.valueOf(Cell.getNumericCellValue());
            }
        }catch (Exception e){
            data = "";
            new Exception("Class ExcelUtils | Method getCellDataDDT | Exception desc: " + e.getMessage());
        }
        return data;
    }

    /**
     * This method is to set the File path and to open the Excel file
     *
     * @throws Exception
     */
    public static void setExcelFile(String file) throws DriverException {
        try {
            FileInputStream ExcelFile = new FileInputStream(Constants.RESOURCES_FOLDER + file);
            ExcelWBook = new XSSFWorkbook(ExcelFile);
        } catch (Exception e) {
           throw new DriverException("Class ExcelUtils | Method setExcelFile | Exception desc: "+e.getMessage());
        }
    }

    /**
     * This method is to read the test data from the Excel cell
     *
     * @param RowNum
     * @param ColNum
     * @param SheetName
     * @return
     * @throws Exception
     */
    public static String getCellData(int RowNum, int ColNum, String SheetName) throws DriverException {
        try {
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
            Cell.setCellType(CellType.STRING);
            if (Cell == null) {
                return "";
            } else {
                String CellData = Cell.getStringCellValue();
                return CellData;
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
              throw new DriverException("Class ExcelUtils | Method getCellData | Exception desc: "+e.getMessage());
            }
            return "";
        }
    }

    /**
     * This method is used to get the row count used of the excel sheet
     *
     * @param SheetName
     * @throws DriverException
     */
    public static int getRowCount(String SheetName) throws DriverException {
        int iNumber = 0;
        try {
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            iNumber = ExcelWSheet.getLastRowNum() + 1;
        } catch (Exception e) {
            if (e.getMessage() != null) {
                throw new DriverException("Class ExcelUtils | Method getRowCount | Exception desc: "+e.getMessage());
            }
        }
        return iNumber;
    }

    /**
     * This method is to set the results of the test steps and test cases
     *
     * @param Result
     * @param RowNum
     * @param ColNum
     * @param SheetName
     * @return
     * @throws IOException
     */
    public static void setCellData(String Result,  int RowNum, int ColNum, String SheetName, String File) throws DriverException, IOException    {
        try{
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            Row  = ExcelWSheet.getRow(RowNum);
            Cell = Row.getCell(ColNum, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (Cell == null) {
                Cell = Row.createCell(ColNum);
                Cell.setCellValue(Result);
            } else {
                Cell.setCellValue(Result);
            }
            FileOutputStream fileOut = new FileOutputStream(Constants.RESOURCES_FOLDER + File);
            ExcelWBook.write(fileOut);
            //
            fileOut.flush();
            fileOut.close();
            ExcelWBook = new XSSFWorkbook(new FileInputStream(Constants.RESOURCES_FOLDER + File));
        }catch(Exception e){
            throw new DriverException("Class ExcelProvider | Method setCellData | Exception desc: "+e.getMessage());
        }
    }

    /**
     * This method is to create hyper links in the results of the test steps
     *
     * @param destFile
     *
     */
    public static void hyperlinkScreenshot(String destFile) {
        try{
            CreationHelper createHelper = ExcelWBook.getCreationHelper();
            CellStyle hlink_style = ExcelWBook.createCellStyle();
            Font hlink_font = ExcelWBook.createFont();
            hlink_font.setUnderline(Font.U_SINGLE);
            hlink_font.setColor(IndexedColors.BLUE.getIndex());
            hlink_style.setFont(hlink_font);
            Hyperlink hp = createHelper.createHyperlink(HyperlinkType.FILE);//createHyperlink(Hyperlink.LINK_FILE);
            destFile=destFile.replace("\\", "/");
            //				destFile = URLEncoder.encode(destFile, "UTF-8");
            hp.setAddress(destFile);
            Cell.setCellValue("FAIL");
            Cell.setHyperlink((org.apache.poi.ss.usermodel.Hyperlink) hp);
            Cell.setCellStyle(hlink_style);
        }catch(Exception e){
            Log.error("Class ExcelProvider | Method hyperlinkScreenshot | Exception desc: Not able to create hyperlink: "+e.getMessage());
            e.printStackTrace();
        }
    }
}
