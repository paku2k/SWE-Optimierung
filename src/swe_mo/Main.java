package swe_mo;

import swe_mo.ui.*;



public class Main {	
	public final static String APPVERSION = "0.0.3.0";
	public final static String DEVELOPERS = "Jonas D�ckmann\nDaniel B�tjer\nElias Niep�tter\nSimon Pauka\nDavid Messow";
	public final static String DATE = "2020-12-08";
	
	public static void main(String[] args) {
		
			clogger.start();
			Settings.factorySettings();
			Settings.load();
			
			UiBackend.start();
			
			Settings.save();
			clogger.stop();
			
    }
	
}
