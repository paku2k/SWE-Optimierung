
package swe_mo.solver.de;

public class Test_Solver_DE {
	
	public static void main(String[] args) {

		int N = 10;
		int NP = 100;
		double F = 0.7;
		double CR = 0.7;
		int maxGenerations = 10;
		double upperBound = 10;
		double lowerBound = 2;
		FitnessFunction fF = new FitnessFunction();
		
		DErand1 rand1 = new DErand1(N, NP, F, CR, maxGenerations, upperBound, lowerBound, fF);
		
		}

}

