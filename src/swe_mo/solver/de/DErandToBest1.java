package swe_mo.solver.de;

import swe_mo.solver.SolverConfig;

public class DErandToBest1 extends DEbest1 {
	
	double lambda;

	
	
	public DErandToBest1(int N, int NP, double F, double CR, double lambda, int maxGenerations, double upperBound, double lowerBound,
			int ffIndex, int solverID) throws Exception {
		super(N, NP, F, CR, maxGenerations, upperBound, lowerBound, ffIndex, solverID);
		this.lambda = lambda;
		
		if(NP < 4) {
			throw new Exception("You need at least 4 particles");
		}
	}

	
	public DErandToBest1(int N, int NP, double F, double CR, double lambda, int maxGenerations, int ffIndex, int solverID) {
		super(N, NP, F, CR, maxGenerations, ffIndex, solverID);
		
		this.lambda = lambda;
	}

	
	public static SolverConfig defaultConfig() {		
		return new SolverConfig(1,5,50,0.3,0.3,0.3,1000,5.14,-5.14);
	}
	
	
	
}
