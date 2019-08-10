package org.titanium.engine;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.titanium.config.BaseClass;
import org.titanium.config.Constants;
import org.titanium.config.ExcelReader;
import org.titanium.utils.ExcelProvider;

import java.lang.reflect.InvocationTargetException;

public class DriverEngine extends BaseClass {

    private static int startStepsRow;
    private static int startStepsCol;
    private static int totalDataRows = 2;
    public static boolean testCaseResult;
    public static boolean testStepResult;
    private static ExcelReader file;
    private static Row row;
    private static String sParam;
    public static boolean breaking = false;

    @Test(dataProviderClass = ExcelProvider.class, dataProvider = "GetExcelData")
    public static void DriverScript(String... provider) throws Exception {
        testStepResult = true;
        testCaseResult = true;
        file = new ExcelReader();
        testCase.setRunMode(provider[3]);
        testCase.setTCKey(provider[0]);
        if (testCase.getRunMode().equalsIgnoreCase("yes")) {
            startTestCase(provider[1], provider[2]);
            if (!provider[4].equals("")) {
                ExcelProvider.setExcelFile(WorkbookProperty);
                totalDataRows = ExcelProvider.getRowCount(provider[4]);
            }

            //Data driven For
            for (int ddt = 1; ddt < totalDataRows; ddt++) {
                testCaseResult = true;
                int ddtTotalCols = 0;
                Sheet stepsSheet = file.readExcel(Constants.RESOURCES_FOLDER, WorkbookProperty, Constants.SHEET_TEST_STEPS);
                int totalRows = stepsSheet.getLastRowNum() - stepsSheet.getFirstRowNum();
                int totalCols = stepsSheet.getRow(0).getLastCellNum();
                //Test Steps For
                for (startStepsRow = 0; startStepsRow < totalRows; startStepsRow++) {
                    row = stepsSheet.getRow(startStepsRow + 1);
                    for (startStepsCol = 0; startStepsCol < totalCols; startStepsCol++) {
                        if (startStepsCol == 0 && !provider[1].equals(row.getCell(startStepsCol).toString())) {
                            breaking = true;
                            break;
                        }
                        switch (startStepsCol) {
                            case 1:
                                testStep.setId(row.getCell(startStepsCol).toString());
                                break;
                            case 2:
                                testStep.setDescription(row.getCell(startStepsCol).toString());
                                break;
                            case 3:
                                testStep.setPageName(row.getCell(startStepsCol).toString());
                                break;
                            case 4:
                                testStep.setPageObject(row.getCell(startStepsCol).toString());
                                break;
                            case 5:
                                testStep.setActionKeyword(row.getCell(startStepsCol).toString());
                                break;
                            case 6:
                                testStep.setData(row.getCell(startStepsCol).toString());
                                sParam = "";
                                if (!testStep.getData().equals("") && !provider[4].equals("")) {
                                    Sheet dataDrivenSheet = file.readExcel(Constants.RESOURCES_FOLDER, WorkbookProperty, provider[4]);
                                    ddtTotalCols = dataDrivenSheet.getRow(0).getLastCellNum();
                                    for (int startDataCol = 0; startDataCol < ddtTotalCols; startDataCol++) {
                                        sParam = ExcelProvider.getCellData(0, startDataCol, provider[4]);
                                        if (testStep.getData().equals(sParam)) {
                                            sParam = ExcelProvider.getCellData(ddt, startDataCol, provider[4]);
                                            testStep.setData(sParam);
                                            break;
                                        }
                                    }
                                }
                                break;
                        }
                    }
                    if (!breaking == true) {
                        execute_Actions();
                        if ((testStepResult == true)) {
                            logPassStep(testStep.getDescription());
                            ExcelProvider.setCellData(Constants.KEYWORD_PASS, startStepsRow+1, totalCols - 1, Constants.SHEET_TEST_STEPS, WorkbookProperty);
                            if(!sParam.equals("")){
                                //dataDrivenResult = true;
                                ExcelProvider.setCellData(Constants.KEYWORD_PASS, ddt, ddtTotalCols, provider[4], WorkbookProperty);

                            }
                        } else {
                            testCaseResult = false;
                            logFailStep(testStep.getDescription());
                            if (ddt>1) {
                                ExcelProvider.setCellData(Constants.KEYWORD_FAIL, ddt, ddtTotalCols, provider[4], WorkbookProperty);
                            }
                            ExcelProvider.setCellData(Constants.KEYWORD_FAIL, startStepsRow+1, totalCols - 1, Constants.SHEET_TEST_STEPS, WorkbookProperty);
                            ExcelProvider.setCellData(Constants.KEYWORD_FAIL, testCase.getTCKey(), Constants.TEST_CASE_RESULT_COL, Constants.SHEET_TEST_CASES, WorkbookProperty);
                            Assert.fail();
                        }
                    }
                    breaking = false;
                }
            }
            ExcelProvider.setCellData(Constants.KEYWORD_PASS, testCase.getTCKey(), Constants.TEST_CASE_RESULT_COL, Constants.SHEET_TEST_CASES, WorkbookProperty);
        }
    }


    /**
     * This method contains the code to perform some action
     * As it is completely different set of logic, which revolves around the action only,
     * It makes sense to keep it separate from the main driver script
     * This is to execute test step (Action)
     */
    private static void execute_Actions() throws InvocationTargetException, IllegalAccessException {
        for (int i = 0; i < method.length; i++) {
            if (method[i].getName().equals(testStep.getActionKeyword())) {
                 method[i].invoke(actionKeywords, allObjects, testStep.getPageObject(), testStep.getData(), testStep.getLink());
                break;
            }
        }
    }
}
