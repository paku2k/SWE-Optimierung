package swe_mo;

import swe_mo.reply.score;
import swe_mo.ui.*;



public class Main {	
	public final static String APPVERSION = "0.4.0.1reply";
	public final static String DEVELOPERS = "Jonas Dückmann\nDaniel Bätjer\nElias Niepötter\nSimon Pauka\nDavid Messow";
	public final static String DATE = "2020-12-22";
	
	public static void main(String[] args) {
		
			clogger.start();
			Settings.factorySettings();
			Settings.load();
			
			score.init();
			
			UiBackend.start();
			
			score.result();
			
			Settings.save();
			clogger.stop();
			
    }
	
}
