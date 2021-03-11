package swe_mo.reply;

import java.io.FileNotFoundException;

import swe_mo.solver.de.Particle_DE;
import swe_mo.ui.clogger;

public class score {
	static Einlesen input;
	
	public static void init(){
		try {
			input.Einlesen("");
		} catch(Exception e) {
			clogger.err("RPL", "init", e);
		}
	}

	public static double calculate_score(Particle_DE vector) {		 
		 return 15;		 
	 }
}
