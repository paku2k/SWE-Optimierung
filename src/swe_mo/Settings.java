package swe_mo;

import swe_mo.ui.clogger;



public class Settings {
	final static String AUTH = "SET";
	
	
	
	
	

	public static void load(String file) {
		try {
			
			clogger.info(AUTH, "load", "Settings loaded ("+file+")");
		} catch(Exception e) {
			clogger.err(AUTH, "load", e);
		}
	}
	
	
	public static void save(String file) {
		try {
			
			clogger.info(AUTH, "load", "Settings saved ("+file+")");
		} catch(Exception e) {
			clogger.err(AUTH, "load", e);
		}
	}
	
	
}
