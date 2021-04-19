package swe_mo;

import swe_mo.ui.*;



public class Main {	
	public final static String APPVERSION = "0.4.0.2";
	public final static String DEVELOPERS = "Jonas Dückmann\nDaniel Bätjer\nElias Niepötter\nSimon Pauka\nDavid Messow";
	public final static String DATE = "2021-04-19";
	
	public static void main(String[] args) {
		
			clogger.start();
			Settings.factorySettings();
			Settings.load();
			
			UiBackend.start();
			
			Settings.save();
			clogger.stop();
			
    }
	
}
