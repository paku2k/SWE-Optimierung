package swe_mo.solver;

import swe_mo.solver.de.DErand1;
import swe_mo.solver.de.DEbest1;


public class SolverConfig {
	int ffid;
	int N;
	int NP;
	double F;
	double CR;
	int maxGenerations;
	double upperBound;
	double lowerBound;


	public SolverConfig() {};
	
	//for cloning
	public SolverConfig(SolverConfig s) {
		this.ffid = s.ffid;
		this.N = s.N;
		this.NP = s.NP;
		this.F = s.F;
		this.CR = s.CR;
		this.maxGenerations = s.maxGenerations;
		this.upperBound = s.upperBound;
		this.lowerBound = s.lowerBound;
	};
	
	public SolverConfig(int ffid, int n, int nP, double f, double cR, int maxGenerations, double upperBound, double lowerBound) {
		super();
		this.ffid = ffid;
		N = n;
		NP = nP;
		F = f;
		CR = cR;
		this.maxGenerations = maxGenerations;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
	}



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
		return "ERR: not implemented (N="+N+")";
	}
	
	public String toJSON() {
		return "{\"error\":\"not implemented\"}";
	}
	
	
	
	
	
	public static SolverConfig getDefault(String algorithm) throws Exception {
		switch(algorithm) {
			case "default":
				return test_david.defaultConfig();
			case "DErand1":
				return DErand1.defaultConfig();
			case "DEbest1":
				return DEbest1.defaultConfig();
				/*
			"DEbest2"
			"DErtb1"
			"PSOgsc"
			"PSOnsc"*/
		}	
		throw new Exception("Algorithm not specified.");
	}
	
	public static SolverResult solveMethod(String algorithm, int id, SolverConfig config) throws Exception {
		SolverResult sr = new SolverResult();

		switch(algorithm) {
			case "default":
				return new test_david(id,
									  config.N).calc();		
			case "DErand1":
				sr = new DErand1(config.N,
										 config.NP,
										 config.F,
										 config.CR,
										 config.maxGenerations,
										 config.upperBound,
										 config.lowerBound,
										 config.ffid, id).solve();	
				return sr;
				
			case "DEbest1":
				sr = new DEbest1(config.N,
										 config.NP,
										 config.F,
										 config.CR,
										 config.maxGenerations,
										 config.upperBound,
										 config.lowerBound,
										 config.ffid, id).solve();	
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
