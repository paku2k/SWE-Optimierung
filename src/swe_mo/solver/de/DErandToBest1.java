package swe_mo.solver.de;

public class DErandToBest1 extends DEbest1 {

	public DErandToBest1(int N, int NP, double F, double CR, int maxGenerations, double upperBound, double lowerBound,
			int ffIndex, int solverID) {
		super(N, NP, F, CR, maxGenerations, upperBound, lowerBound, ffIndex, solverID);
		// TODO Auto-generated constructor stub
	}

	public DErandToBest1(int N, int NP, double F, double CR, int maxGenerations, int ffIndex, int solverID) {
		super(N, NP, F, CR, maxGenerations, ffIndex, solverID);
		// TODO Auto-generated constructor stub
	}

}
