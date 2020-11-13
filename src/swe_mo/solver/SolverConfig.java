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
	
	
	
	
	public void set(String param, String value) throws Exception {
		switch(param) {
			case "ffid":
				ffid = Integer.parseInt(value);
				return;
			case "N":
				N = Integer.parseInt(value);
				return;
			case "NP":
				NP = Integer.parseInt(value);
				return;
			case "F":
				F = Double.parseDouble(value);
				return;
			case "CR":
				CR = Double.parseDouble(value);
				return;
			case "maxGenerations":
				maxGenerations = Integer.parseInt(value);
				return;
			case "upperBound":
				upperBound = Double.parseDouble(value);
				return;
			case "lowerBound":
				lowerBound = Double.parseDouble(value);
				return;
		}
		throw new Exception("No such hyperparameter ("+param+").");
	}
	

	
	
	@Override
	public String toString() {
		return "ERR: not implemented";
	}
	
	public String toJSON() {
		return "{\"error\":\"not implemented\"}";
	}
	
	
	
	
	
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
