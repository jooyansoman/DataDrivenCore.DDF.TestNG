package com.qtpselenium.zoho.project.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
//import org.testng.asserts.SoftAssert;

import com.qtpselenium.zoho.project.util.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class BaseTest {
	public WebDriver driver;
	public Properties prop;
	public Properties envProp;
	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest test;

	public void init(){
		//init the prop file
		if(prop==null){
			prop=new Properties();
			envProp=new Properties();
			try {
				FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"/src/test/resources/ProjectConfig.properties");
				prop.load(fs);
				String env=prop.getProperty("env");
				fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//"+env+".properties");
				envProp.load(fs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void openBrowser(String bType){
		test.log(LogStatus.INFO, "Opening browser "+ bType);
		if(bType.equals("Mozilla"))
			driver=new FirefoxDriver();
		else if(bType.equals("Chrome")){
			System.setProperty("webdriver.chrome.driver", prop.getProperty("chromedriver"));
			driver=new ChromeDriver();
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(20,TimeUnit.SECONDS);
		driver.manage().window().maximize();
		test.log(LogStatus.INFO, "Browser opened successfully"+ bType);
		System.setProperty("ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY", "TRUE");
		
		
	}
	

	public void navigate(String urlKey){
		test.log(LogStatus.INFO, "Navigate to "+ urlKey);
		driver.get(envProp.getProperty(urlKey));
	}
	
	public void click(String locatorKey){
		test.log(LogStatus.INFO, "Clicking on "+locatorKey);
		getElement(locatorKey).click();
		test.log(LogStatus.INFO, "Clicked successfully on "+locatorKey);
	}
	
	public void type(String locatorKey,String data){
		test.log(LogStatus.INFO, "Typing in "+locatorKey+ ". Data - "+data);
		getElement(locatorKey).sendKeys(data);
		test.log(LogStatus.INFO, "Typed successfully in "+locatorKey);
	}
	// finding element and returning it
	public WebElement getElement(String locatorKey){
		WebElement e=null;
		try{
		if(locatorKey.endsWith("_id"))
			e = driver.findElement(By.id(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_name"))
			e = driver.findElement(By.name(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_xpath"))
			e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
		else{
			reportFailure("Locator not correct - " + locatorKey);
			Assert.fail("Locator not correct - " + locatorKey);
		}
		
		}catch(Exception ex){
			// fail the test and report the error
			reportFailure(ex.getMessage());
			ex.printStackTrace();
			Assert.fail("Failed the test - "+ex.getMessage());
		}
		return e;
	}
	/***********************Validations***************************/
	
	public boolean verifyTitle(){
		return false;		
	}
	
	public boolean isElementPresent(String locatorKey){
		List<WebElement> elementList=null;
		if(locatorKey.endsWith("_id"))
			elementList = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_name"))
			elementList = driver.findElements(By.name(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_xpath"))
			elementList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else{
			reportFailure("Locator not correct - " + locatorKey);
			Assert.fail("Locator not correct - " + locatorKey);
		}
		
		if(elementList.size()==0)
			return false;	
		else
			return true;
	}
	
	public boolean verifyText(String locatorKey,String expectedTextKey){
		String actualText=getElement(locatorKey).getText().trim();
		String expectedText=prop.getProperty(expectedTextKey);
		if(actualText.equals(expectedText))
			return true;
		else 
			return false;
		
	}
	
	public void clickAndWait(String locator_clicked,String locator_pres){
		test.log(LogStatus.INFO, "Clicking and waiting on - "+locator_clicked);
		int count=5;
		for(int i=0;i<count;i++){
			getElement(locator_clicked).click();
			wait(2);
			if(isElementPresent(locator_pres))
				break;
		}
	}
	/*****************************Reporting********************************/
	
	public void reportPass(String msg){
		test.log(LogStatus.PASS, msg);
	}
	
	public void reportFailure(String msg){
		test.log(LogStatus.FAIL, msg);
		takeScreenShot();
		Assert.fail(msg);
	}
	
	public void takeScreenShot(){
		// fileName of the screenshot
		Date d=new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ", "_")+".png";
		// store screenshot in that file
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//put screenshot file in reports
		test.log(LogStatus.INFO,"Screenshot-> "+ test.addScreenCapture(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
	}
	
	public void acceptAlert(){
		WebDriverWait wait = new WebDriverWait(driver,5);
		wait.until(ExpectedConditions.alertIsPresent());
		test.log(LogStatus.INFO,"Accepting alert");
		driver.switchTo().alert().accept();
		driver.switchTo().defaultContent();
	}
	

	public void waitForPageToLoad() {
		wait(1);
		JavascriptExecutor js=(JavascriptExecutor)driver;
		String state = (String)js.executeScript("return document.readyState");
		
		while(!state.equals("complete")){
			wait(2);
			state = (String)js.executeScript("return document.readyState");
		}
	}
	
	public void wait(int timeToWaitInSec){
		try {
			Thread.sleep(timeToWaitInSec * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*****************************Application Functions********************************/
	
	// Login function 
	
	public boolean doLogin(String username,String password){
		test.log(LogStatus.INFO, "Trying to login with "+ username+","+password);
		
		wait(1);
		click("loginLink_xpath");
		
		waitForPageToLoad();
		
		type("loginid_xpath", username);
		click("nextButton_xpath");	
		
		wait(1);
		type("password_xpath", password);
		click("signinButton_xpath");
		
		wait(1);
		click("crmlink_xpath");
	
		if(isElementPresent("home_xpath")){
			test.log(LogStatus.INFO, "Login Success");
			return true;
		}
		else{
			test.log(LogStatus.INFO, "Login Failed");
			return false;
			
		}	
	}
	
	public int getLeadRowNum(String leadName){
		test.log(LogStatus.INFO, "Finding the lead "+leadName);
		List<WebElement> leadNames=driver.findElements(By.xpath(prop.getProperty("leadNamesCol_xpath")));
		for(int i=0;i<leadNames.size();i++){
			System.out.println(leadNames.get(i).getText());
			if(leadNames.get(i).getText().trim().equals(leadName)){
				test.log(LogStatus.INFO, "Lead found in row num "+(i+1));
				return (i+1);
			}
		}
		test.log(LogStatus.INFO, "Lead not found ");
		return -1;
	}
	
	public void clickOnLead(String leadName){
		test.log(LogStatus.INFO, "Clicking the lead "+leadName);
		int rNum=getLeadRowNum(leadName);
		if(rNum==-1)
			reportFailure("Lead not found "+leadName );
		driver.findElement(By.xpath(prop.getProperty("leadpart1_xpath")+rNum+prop.getProperty("leadpart2_xpath"))).click();

	}
}

