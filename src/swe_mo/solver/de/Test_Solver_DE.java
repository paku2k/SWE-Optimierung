
package swe_mo.solver.de;

import swe_mo.solver.FitnessFunction;

public class Test_Solver_DE {
	
	public static void main(String[] args) {

		int N = 20;
		int NP = 200;
		double F = 0.8; //Empfehlung F0.5...1
		double CR = 0.3; //Empfehlung CR=0.3
		int maxGenerations = 500000;
		double upperBound = 5.12;
		double lowerBound = -5.12;
		FitnessFunction fF = new FitnessFunction();
		
		DErand1 rand1 = new DErand1(N, NP, F, CR, maxGenerations, upperBound, lowerBound, fF);
		rand1.solve();
		System.out.println("Best Fitness Function value: "+rand1.best);
		Particle_DE p = rand1.bestParicle;
		System.out.println("BEST Particle:  "+p);

		
		}

}

