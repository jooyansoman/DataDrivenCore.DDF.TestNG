package com.qtpselenium.cor.ddf.testcases;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qtpselenium.cor.ddf.base.BaseTest;
import com.qtpselenium.core.ddf.util.DataUtil;
import com.qtpselenium.core.ddf.util.Xls_Reader;
import com.relevantcodes.extentreports.LogStatus;

public class DummyTestC extends BaseTest{
	
	String testCaseName="TestC";
	Xls_Reader xls;
	
	@Test(dataProvider="getData")
	public void testC(Hashtable<String,String> data){
		test=rep.startTest("DummyTestC");
		
		if(!DataUtil.isRunnable(testCaseName, xls) ||  data.get("Runmode").equals("N")){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		
		test=rep.startTest("DummyTestC");
		test.log(LogStatus.INFO, "Starting the test C");
		test.log(LogStatus.FAIL, "Failed DummyTest C");
		
		//Below for taking screenshot and adding image/screenshot
		test.log(LogStatus.FAIL, "Screenshot->" + test.addScreenCapture("/Users/jooyansoman/MVNProject1/DataDriven_Core_Framework/test-output/failed.png"));
	
		
		
	}
	 
	
	
	@AfterMethod
	public void quit() {
	rep.endTest(test);
	rep.flush();

	}
	

	@DataProvider
	public Object[][] getData(){
		super.init();
		xls = new Xls_Reader(prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls, testCaseName);
		
	}
}