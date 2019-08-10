package org.titanium.engine;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.titanium.config.Constants;

import java.net.MalformedURLException;
import java.net.URL;

public class MobileChrome {
    AppiumDriver<MobileElement> driver;

    @BeforeClass
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        // set the capability to execute test in chrome browser
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);

        //capabilities.setCapability(MobileCapabilityType.UDID, "emulator-5554");
        // we need to define platform name
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");

        // Set the device name as well (you can give any name)
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "AndroidOreo27");

        //Set ChromeDriver location
        capabilities.setCapability("chromedriverExecutable",Constants.DRIVER_PATH + "chromedriver34.exe");
        // set the android version as well
        //capabilities.setCapability(MobileCapabilityType.VERSION, "8.1.0");

        // Create object of URL class and specify the appium server address
        URL url = new URL("http://0.0.0.0:4723/wd/hub");
        driver = new AndroidDriver<MobileElement>(url, capabilities);
    }

    @Test
    public void simpleTest() {
        // Open url
        driver.get("http://www.facebook.com");

        // print the title
        System.out.println("Title " + driver.getTitle());

        // enter username
        driver.findElement(By.name("email")).sendKeys("mukesh@gmail.com");

        // enter password
        driver.findElement(By.name("pass")).sendKeys("mukesh_selenium");

        // click on submit button
        driver.findElement(By.id("u_0_5")).click();

        // close the browser
        driver.quit();
    }
}
