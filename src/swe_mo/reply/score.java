package swe_mo.reply;

import java.io.FileNotFoundException;

import swe_mo.solver.SolverConfig;
import swe_mo.solver.de.Particle_DE;
import swe_mo.ui.clogger;

public class score {
	static Einlesen input = new Einlesen();
	
	public static void init(){
		try {
			input.Einlesen("C:\\Users\\david\\Desktop\\data_scenarios_a_example.in");
		} catch(Exception e) {
			clogger.err("RPL", "init", e);
		}
	}

	public static double calculate_score(Particle_DE vector) {		 
		 return 15;		 
	 }
	
	

	public static SolverConfig defaultConfig() {		
		return new SolverConfig(27,input.numberOfAntenna*2,100,0.3,0.3,1000,input.height,input.width, 1.0, false, false);
	}
}
