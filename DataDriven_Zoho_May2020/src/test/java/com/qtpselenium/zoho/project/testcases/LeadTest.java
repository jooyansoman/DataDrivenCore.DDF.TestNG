package com.qtpselenium.zoho.project.testcases;
import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qtpselenium.zoho.project.base.BaseTest;
import com.qtpselenium.zoho.project.util.DataUtil;
import com.qtpselenium.zoho.project.util.Xls_Reader;
import com.relevantcodes.extentreports.LogStatus;
public class LeadTest extends BaseTest{
	
	Xls_Reader xls;
	SoftAssert softAssert;
	static int testIter=1;
	
	@Test(priority=1,dataProvider="getData")
	public void createLeadTest(Hashtable<String,String> data){
		
		
		test = rep.startTest("CreateLeadTest");
		test.log(LogStatus.INFO, data.toString());
		if(!DataUtil.isRunnable("CreateLeadTest", xls) ||  data.get("Runmode").equals("N")){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		
		openBrowser(data.get("Browser"));
		navigate("appurl");
		doLogin(envProp.getProperty("username"), envProp.getProperty("password"));
		click("leadsTab_xpath");
		click("newLeadButton_xpath");
		type("leadCompany_xpath", data.get("LeadCompany"));
		type("leadLastName_xpath", data.get("LeadLastName"));
		click("leadSaveButton_xpath");
		click("leadsTab_xpath");
		clickAndWait("leadsTab_xpath", "newLeadButton_xpath");
		
		
		

		//validate
		int rNum=getLeadRowNum(data.get("LeadLastName"));
		if(rNum==-1)
			reportFailure("Lead not found in lead table "+ data.get("LeadLastName"));
	
		reportPass("Lead found in lead table "+ data.get("LeadLastName")); 
		takeScreenShot();
		
	}
	
	@Test(priority=2,dataProvider="getData")
	public void convertLeadTest(Hashtable<String,String> data){
	
		
		test = rep.startTest("ConvertLeadTest");
		test.log(LogStatus.INFO, data.toString());
		if(!DataUtil.isRunnable("ConvertLeadTest", xls) ||  data.get("Runmode").equals("N")){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		
		openBrowser(data.get("Browser"));
		navigate("appurl");
		doLogin(envProp.getProperty("username"), envProp.getProperty("password"));
		click("leadsTab_xpath");
		clickOnLead(data.get("LeadLastName"));
		click("convertLead_xpath");
		click("convertLeadandSave_xpath");
		//validate - you
		
	}
	
	
	
	@Test(priority=3,dataProvider="getDataDeleteLead")
	public void deleteLeadAccountTest(Hashtable<String,String> data){
					

		test = rep.startTest("DeleteLeadAccountTest");
		test.log(LogStatus.INFO, data.toString());
		if(!DataUtil.isRunnable("DeleteLeadAccountTest", xls) ||  data.get("Runmode").equals("N")){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		
		openBrowser(data.get("Browser"));
		navigate("appurl");
		doLogin(envProp.getProperty("username"), envProp.getProperty("password"));
		click("leadsTab_xpath");
		clickOnLead(data.get("LeadLastName"));
		
		waitForPageToLoad();
		
		click("newwhitebtn_xpath");
		click("deleteLead_xpath");
		wait(1);
		click("deletebtn_xpath");
		//acceptAlert();
		wait(3);
		click("leadsTab_xpath");
		
		int rNum=getLeadRowNum(data.get("LeadLastName"));
		if(rNum!=-1)
			reportFailure("Could not delete the lead");
		
		reportPass("Lead deleted successfully");
		takeScreenShot();
		
		System.out.println("xx");
		
	}
	

	
	
	@DataProvider(parallel=true)
	public Object[][] getData(){
		super.init();
		xls = new Xls_Reader(prop.getProperty("xlspath"));
		Object[][] data= DataUtil.getTestData(xls, "CreateLeadTest");
		return data;
	}
	
	@DataProvider(parallel=true)
	public Object[][] getDataDeleteLead(){
		super.init();
		xls = new Xls_Reader(prop.getProperty("xlspath"));
		Object[][] data= DataUtil.getTestData(xls, "DeleteLeadAccountTest");
		return data;
		
		
	}
	
	
	@BeforeMethod
	public void init(){
		softAssert = new SoftAssert();
		
	}
	
	@AfterMethod
	public void quit(){
		try{
			softAssert.assertAll();
		}catch(Error e){
			test.log(LogStatus.FAIL, e.getMessage());
		}
		if(rep!=null){
			rep.endTest(test);
			rep.flush();
		}
		if(driver!=null)
			driver.quit();
		
		
	}

}
