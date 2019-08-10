package org.titanium.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.titanium.config.BaseClass;
import org.titanium.config.Constants;
import org.titanium.config.Log;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SeleniumUtils {
    //static WebDriver webdriver;
    //public static Properties allProperties;
    //public static ReadObject object = new ReadObject();
    public static String SSDate;
    public static String SSDateTime;
    public static String file;

    /**
     * This function will take screenshot and highlight over element
     *
     * @param webdriver
     * @param snapshotError
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String takeSnapShot(WebDriver webdriver, WebElement element, String snapshotError) {
        SSDate = new SimpleDateFormat("yyyyMMdd_HH").format(Calendar.getInstance().getTime()).toString();
        SSDateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()).toString();
        file = Constants.SCREENSHOT_PATH + SSDate + "\\" + snapshotError + SSDateTime + ".png";
        JavascriptExecutor js = (JavascriptExecutor) webdriver;
        js.executeScript("arguments[0].setAttribute('style','background: yellow')", element);
        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
        try { //Call getScreenshotAs method to create image file
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            //Move image file to new destination
            File DestFile = new File(file);
            //Copy file at destination
            FileUtils.copyFile(SrcFile, DestFile);

            //***********************************************
//			ExcelUtils.hyperlinkScreenshot(file); --- Commented by space error on path Illegal character in path at index 17: C:/Users/gilberto sanchez/
            js.executeScript("arguments[0].setAttribute('style','background:')", element);
        } catch (Exception e) {
            Log.error("Class SeleniumUtils | Method takeSnapShot | Exception desc: " + e.getMessage());
        }
        return file;
    }

    /**
     * This function will take screenshot
     *
     * @param snapshotError
     * @return
     * @throws Exception
     */
    public static String takeSnapShot(String snapshotError) {
        SSDate = new SimpleDateFormat("yyyyMMdd_HH").format(Calendar.getInstance().getTime()).toString();
        SSDateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()).toString();
        file = Constants.SCREENSHOT_PATH + SSDate + "\\" + snapshotError + SSDateTime + ".png";
        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot = ((TakesScreenshot) DriverFactory.getInstance().getDriver());
        try { //Call getScreenshotAs method to create image file
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            //Move image file to new destination
            File DestFile = new File(file);
            //Copy file at destination
            FileUtils.copyFile(SrcFile, DestFile);

            //***********************************************
//			ExcelUtils.hyperlinkScreenshot(file); --- Commented by space error on path Illegal character in path at index 17: C:/Users/gilberto sanchez/
        } catch (Exception e) {
            Log.error("Class SeleniumUtils | Method takeSnapShot | Exception desc: " + e.getMessage());
        }
        return file;
    }

    /**
     * HighLight an element
     *
     * @param driver
     * @param element
     * @throws InterruptedException
     */
    public static void fnHighlightMe(WebDriver driver, WebElement element) throws DriverException {
        //Creating JavaScriptExecuter Interface
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (int iCnt = 0; iCnt < 3; iCnt++) {
            //Execute javascript
            try {
                js.executeScript("arguments[0].setAttribute('style','background: yellow')", element);
                Thread.sleep(50);
                js.executeScript("arguments[0].setAttribute('style','background:')", element);
            } catch (InterruptedException e) {
                throw new DriverException("Class SeleniumUtils | Method fnHighlightMe | Exception desc: Exception", e);
            }
        }
    }

    /**
     * Send email using java
     *
     */

	public static void sendEmail(String from, String pass, String to, String subject, String body, String fileName){
		if(BaseClass.allProperties.getProperty("SendEmail").equalsIgnoreCase("false")){
			return;
			}
		//allProperties = object.getObjectProperties();
		Properties props = System.getProperties();
		String host = BaseClass.allProperties.getProperty("Host");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", BaseClass.allProperties.getProperty("SmtpPort"));
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try{
			//Set from address
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			//Set subject
			message.setSubject(subject);
			message.setText(body);
			BodyPart objMessageBodyPart = new MimeBodyPart();
			objMessageBodyPart.setText(body);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(objMessageBodyPart);
			objMessageBodyPart = new MimeBodyPart();

			//Create datasource to attach the file in mail
			if(!fileName.equals("")||fileName!=null){
				DataSource source = new FileDataSource(fileName);
				objMessageBodyPart.setDataHandler(new DataHandler(source));
				objMessageBodyPart.setFileName(fileName);
				multipart.addBodyPart(objMessageBodyPart);
			}
			
			message.setContent(multipart);
			Transport transport = session.getTransport("smtp");
			transport.connect(host,from,pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		}catch(Exception ex){
			if(ex instanceof AddressException){
				new DriverException("Class SeleniumUtils | Method sendEmail | Exception: " + ex.getMessage());
			}else if(ex instanceof MessagingException){
				new DriverException("Class SeleniumUtils | Method sendEmail | Exception: " + ex.getMessage());
			}else{
				new DriverException("Class SeleniumUtils | Method sendEmail | Exception: " + ex.getMessage());
			}
		}

	}
}
