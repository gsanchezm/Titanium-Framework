package org.titanium.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.titanium.config.BrowserType;
import org.titanium.config.Constants;

import java.util.concurrent.TimeUnit;

public class DriverFactory {
    private DriverFactory() {

    }

    private static DriverFactory instance = new DriverFactory();

    public static DriverFactory getInstance() {
        return instance;
    }

    ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>() {
        @Override
        protected WebDriver initialValue() {
            return null;
        }
    };

    public WebDriver getDriver() {
        return driver.get();
    }

    public WebDriver setDriver(BrowserType browser) {
        String getOS = System.getProperty("os.name").toLowerCase();
        String osName = "";
        if (getOS.contains("mac")) {
            osName = "mac";
        } else if (getOS.contains("win")) {
            osName = "windows";
        } else if (getOS.contains("nix") || getOS.contains("nux") || getOS.contains("aix")) {
            osName = "linux";
        }

        switch (browser.toString()) {
            case "CHROME":
                if (osName.equals("windows")) {
                    System.setProperty("webdriver.chrome.driver", Constants.DRIVER_PATH + "chromedriver.exe");
                } else {
                    System.setProperty("webdriver.chrome.driver", Constants.DRIVER_PATH + "chromedriver");
                }
                driver.set(new ChromeDriver());
                break;
            case "IE":
                if (osName.equals("windows")) {
                    System.setProperty("webdriver.ie.driver", Constants.DRIVER_PATH + "IEDriverServer.exe");
                }
                driver.set(new InternetExplorerDriver());
                break;
            case "FIREFOX":
                if (osName.equals("windows")) {
                    System.setProperty("webdriver.gecko.driver", Constants.DRIVER_PATH + "geckodriver.exe");
                } else {
                    System.setProperty("webdriver.gecko.driver", Constants.DRIVER_PATH + "geckodriver");
                }
                driver.set(new FirefoxDriver());
                break;
            case "SAFARI":
                if (osName.equals("mac")) {
                    driver.set(new SafariDriver());
                }
                break;
            case "EDGE":
                if (osName.equals("windows")) {
                    System.setProperty("webdriver.edge.driver", Constants.DRIVER_PATH + "MicrosoftWebDriver.exe");
                    driver.set(new EdgeDriver());
                }
        }
        int i = 10;

        for (int j = 1; j <= i; i++) {
            try {
                driver.get().manage().window().maximize();
                break;
            } catch (WebDriverException we) {
                driver.set(new ChromeDriver());
                driver.get().manage().window().maximize();
            }
            if (i == j) {
                Assert.fail("Failed to maximize window " + j + " times");
            }
        }
        driver.get().manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        return driver.get();
    }


    public void removeDriver() {
        driver.get().quit();
        driver.remove();
    }
}
