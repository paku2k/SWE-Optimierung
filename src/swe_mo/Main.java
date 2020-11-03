package swe_mo;

import swe_mo.ui.*;



public class Main {	
	public static void main(String[] args) {
			clogger.start();
			UiBackend.start();
			clogger.stop();
    }
}
