package org.titanium.config;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.PropertyConfigurator;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.titanium.properties.ExtentRep;
import org.titanium.properties.General;
import org.titanium.properties.TestCase;
import org.titanium.properties.TestStep;
import org.titanium.utils.ActionKeywords;
import org.titanium.utils.ExcelProvider;
import org.titanium.utils.JyperionListener;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import static org.titanium.utils.SeleniumUtils.sendEmail;
import static org.titanium.utils.SeleniumUtils.takeSnapShot;

public class BaseClass {
    protected static ExtentRep exProp = new ExtentRep();
    protected String extentReportFile;
    private ReadObject object;
    public static Properties allProperties;
    private String BrowserProperty;
    public static String WorkbookProperty;
    public static General objProp;
    protected ExcelProvider excelProvider;
    protected static VideoRecorder videoRec;
    protected static Method[] method;
    protected static ActionKeywords actionKeywords;
    protected static TestStep testStep;
    protected static TestCase testCase;
    protected static Properties allObjects;
    private static String fromProperty;
    private static String passwordProperty;
    private static String toProperty;

    public BaseClass() {
        object = new ReadObject();
        objProp = new General();
        excelProvider = new ExcelProvider();
        actionKeywords = new ActionKeywords();
        testStep = new TestStep();
        testCase = new TestCase();
        videoRec = new VideoRecorder();

        allProperties = object.getObjectProperties();
        BrowserProperty = allProperties.getProperty("Browser");
        method = actionKeywords.getClass().getMethods();
        allObjects = object.getObjectRepository();
        WorkbookProperty = allProperties.getProperty("DataEngine");
        objProp.setVideoPath(Constants.VIDEO_FOLDER);
        fromProperty = BaseClass.allProperties.getProperty("From");
        passwordProperty = BaseClass.allProperties.getProperty("Password");
        toProperty = BaseClass.allProperties.getProperty("To");
    }

    @AfterMethod
    public void getResult(ITestResult result) {
       if(testCase.getRunMode().equalsIgnoreCase("yes")) {
           switch (result.getStatus()) {
               case ITestResult.FAILURE:
                   logTestFail(result);
                   break;
               case ITestResult.SUCCESS:
                   logTestPass(result);
                   break;
               case ITestResult.SKIP:
                   logTestSkip(result);
                   break;
           }
       }
    }

    @BeforeTest
    @Parameters("browser")
    public void setUpTest(String browser) throws Exception {
        extentReportFile = Constants.HTML_REPORT;
        objProp.setBrowserDriver(browser.toLowerCase());

        // Create object of extent report and specify the report file path.
        exProp.setExtent(new ExtentReports(extentReportFile, true));

        exProp.getExtent().loadConfig(new File(Constants.EXTENT_CONFIG));
        // Start the test using the ExtentTest class object.
        actionKeywords.openBrowser(null, Constants.KEYWORD_BLANK, objProp.getBrowserDriver(), Constants.KEYWORD_BLANK);
        videoRec.startRecording(objProp.getVideoPath());
    }

    @AfterTest
    public void tearDown() throws Exception {
        actionKeywords.closeBrowser(null, Constants.KEYWORD_BLANK, Constants.KEYWORD_BLANK, Constants.KEYWORD_BLANK);
        Log.endTestCase();
        exProp.getExtentTest().log(LogStatus.INFO, "Browser closed");
        videoRec.stopRecording();

        // close report.
        exProp.getExtent().endTest(exProp.getExtentTest());

        // writing everything to document.
        exProp.getExtent().flush();
    }

    @BeforeClass
    public void frameworkSetup() {
        PropertyConfigurator.configure(Constants.LOG_PROPERTIES_PATH);
    }

    @AfterSuite
    public void openReports() throws IOException {
        if (allProperties.getProperty("Open_Report").toLowerCase().equals("true")) {
            Desktop.getDesktop().open(new File(Constants.HTML_REPORT));
        }
        if (allProperties.getProperty("Open_DataEngine").toLowerCase().equals("true")) {
            Desktop.getDesktop().open(new File(Constants.RESOURCES_FOLDER + WorkbookProperty));
        }
        if (allProperties.getProperty("Open_Logs").toLowerCase().equals("true")) {
            Desktop.getDesktop().open(new File(Constants.RESULTS_FOLDER + "Logs.log"));
        }
        if (allProperties.getProperty("Open_Video").toLowerCase().equals("true")) {
            Desktop.getDesktop().open(new File(objProp.getVideoPath() + objProp.getVideo()));
        }
    }

    public static void startTestCase(String testName, String desc) {
        exProp.setExtentTest(exProp.getExtent().startTest(testName, desc));
        Log.startTestCase(testName);
    }

    private void logTestPass(ITestResult result) {
        exProp.getExtentTest().log(LogStatus.PASS, result.getName() + " Passed");
    }

    private void logTestFail(ITestResult result) {
        // In case you want to attach screenshot then use below method
        // We used a random image but you've to take screenshot at run-time
        // and specify the error image path.
        objProp.setScreenShot(takeSnapShot("error_"));
        exProp.getExtentTest().log(LogStatus.FAIL, result.getName() + " Failed due to below issue : " +
                exProp.getExtentTest().addScreenCapture(objProp.getScreenShot()));
        errorInfo(objProp.getErrorException(),objProp.getScreenShot());
    }

    private void logTestSkip(ITestResult result) {
        exProp.getExtentTest().log(LogStatus.SKIP, result.getName() + " Skipped");
    }

    public static void logPassStep(String details) {
        exProp.getExtentTest().log(LogStatus.PASS, details);
    }

    public static void logFailStep(String details) {
        exProp.getExtentTest().log(LogStatus.FAIL, details);
    }

    private void errorInfo(String message, String fileName){
        if(allProperties.getProperty("SendEmail").equalsIgnoreCase("true")){
            sendEmail(fromProperty, passwordProperty, toProperty, message, message, fileName);
            Log.error("Email sent to " + toProperty);
        }
    }
}
