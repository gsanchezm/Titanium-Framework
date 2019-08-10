package org.titanium.config;

public class Constants {

    //System Variables
    private static final String USER_DIR = System.getProperty("user.dir");
    public static final String RESULTS_FOLDER = System.getProperty("user.dir") + "/results/";
    public static final String RESOURCES_FOLDER = System.getProperty("user.dir") + "/src/main/resources/";
    public static final String EXTENT_CONFIG = USER_DIR + "/extent-config.xml";
    public static final String KEYWORD_FAIL = "FAIL";
    public static final String KEYWORD_PASS = "PASS";
    public static final String KEYWORD_BLANK = "";

    //DataEngine
    public static final String SHEET_TEST_STEPS = "Test Steps";
    public static final String SHEET_TEST_CASES = "Test Cases";
    public static final int TEST_CASE_RESULT_COL = 5;

    //Resources directory
    public static final String CONFIG_PATH = RESOURCES_FOLDER + "config.properties";
    public static final String LOG_PROPERTIES_PATH = RESOURCES_FOLDER + "log4j.properties";
    //Drivers Path
    public static final String DRIVER_PATH = RESOURCES_FOLDER + "drivers/";

    //Results directory
    public static final String VIDEO_FOLDER = RESULTS_FOLDER + "video/";
    public static final String SCREENSHOT_PATH = RESULTS_FOLDER + "screenshots/";
    public static final String HTML_REPORT = RESULTS_FOLDER + "report.html";
}