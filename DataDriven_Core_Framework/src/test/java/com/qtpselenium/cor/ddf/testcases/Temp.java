package com.qtpselenium.cor.ddf.testcases;

import java.util.Date;

public class Temp {
    // Generating Different Report file using dates 
	public static void main(String[] args) {
		Date d = new Date();
		System.out.println(d.toString().replace(":", "_").replace(" ", "_"));

	}

}
