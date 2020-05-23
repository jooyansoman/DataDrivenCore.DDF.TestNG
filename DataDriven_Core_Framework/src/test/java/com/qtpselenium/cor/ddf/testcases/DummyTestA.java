package com.qtpselenium.cor.ddf.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qtpselenium.cor.ddf.base.BaseTest;

public class DummyTestA extends BaseTest{
	 
	
	@Test (priority=1)
	public void testA1() {
		Assert.fail();
	}
	
	@Test(priority=2, dependsOnMethods= {"testA1"})
	public void testA2() {
		
	}

	@Test(priority=3, dependsOnMethods= {"testA1","testA2"})
	public void testA3() {
		
	}
}
