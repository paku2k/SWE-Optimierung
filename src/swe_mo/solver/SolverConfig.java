package swe_mo.solver;

import swe_mo.solver.de.DErand1;

public class SolverConfig {
	int ffid;
	int N;
	int NP;
	double F;
	double CR;
	int maxGenerations;
	double upperBound;
	double lowerBound;
	
	
	
	
	
	
	public static SolverConfig getDefault(String algorithm) throws Exception {
		switch(algorithm) {
			case "default":
				return test_david.defaultConfig();
			case "DErand1":
				return test_david.defaultDErand1Config(); //replace
			/*"DEbest1"
			"DEbest2"
			"DErtb1"
			"PSOgsc"
			"PSOnsc"*/
		}	
		throw new Exception("Algorithm not specified.");
	}
	
	public static SolverResult solveMethod(String algorithm, int id, SolverConfig config) throws Exception {
		switch(algorithm) {
			case "default":
				return new test_david(id,
									  config.N).calc();		
			case "DErand1":
				SolverResult sr = new SolverResult();
				sr.value = new DErand1(config.N,
										 config.NP,
										 config.F,
										 config.CR,
										 config.maxGenerations,
										 config.upperBound,
										 config.lowerBound,
										 new FitnessFunction()).solve();	//later just the ff_id
				return sr;
			/*"DEbest1"
			"DEbest2"
			"DErtb1"
			"PSOgsc"
			"PSOnsc"*/
		}
		throw new Exception("Algorithm not specified or no solver method.");
	}
}
