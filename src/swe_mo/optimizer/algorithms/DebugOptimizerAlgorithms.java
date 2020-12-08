package swe_mo.optimizer.algorithms;

import java.util.ArrayList;

public class DebugOptimizerAlgorithms {

	public static void main(String [] args) {
		
		int ffID = 1;
		int numberIterations = 1000;
		int levels = 3;
		int levelGuesses = 100;
		
		ArrayList<Double> parametersMin = new ArrayList<Double>();
			parametersMin.add(0.1);
			parametersMin.add(0.1);
			parametersMin.add(0.1);
			
		ArrayList<Double> parametersMax = new ArrayList<Double>();
			parametersMax.add(0.9);
			parametersMax.add(0.9);
			parametersMax.add(0.9);
			
		ArrayList<String> parametersName = new ArrayList<String>();
			parametersName.add("w");
			parametersName.add("cc");
			parametersName.add("cs");

		
		/*DeepRandomSearch DRS = new DeepRandomSearch(ffID, numberIterations, parametersMin, parametersMax, parametersName, levels, levelGuesses);
		
		try {
			DRS.optimize();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}
	
}
