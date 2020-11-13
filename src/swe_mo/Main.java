package swe_mo;

import swe_mo.ui.*;



public class Main {	
	public static void main(String[] args) {
			clogger.start();
			Settings.load("/config.json");
			
			UiBackend.start();
			
			Settings.save("/config.json");
			clogger.stop();
			
    }
	
}
