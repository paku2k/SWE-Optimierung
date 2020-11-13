
package swe_mo.solver.de;

import swe_mo.solver.FitnessFunction;
import swe_mo.solver.SolverResult;


public class Test_Solver_DE {
	
	public static void main(String[] args) {

		int N = 50;
		int NP = 500;
		double F = 0.7; //Empfehlung F0.5...1
		double CR = 0.3; //Empfehlung CR=0.3
		int maxGenerations = 500;
		double upperBound = 500;
		double lowerBound = -500;
		FitnessFunction fF = new FitnessFunction();
		
		double bestF = F;
		double bestCR =CR;
		double bestRes=Double.MAX_VALUE;
		
		int stepsF = 4;
		int stepsCR = 10;
		
		double minF=0.0;
		double maxF=0.6;
		double minCR=0.0;
		double maxCR=1.0;
		/*
		DErand1 rand1 = new DEbest1(N, NP, F, CR, maxGenerations, upperBound, lowerBound, 8,1);
		rand1.solve();
		System.out.println("Found new best result: "+rand1.best+" with Particle "+rand1.bestParticle);
		System.out.println("Fitness Function Calls: "+rand1.fitCount);
*/

		
		

		for(int i = 0; i<=stepsF; i++) {
			double curF=((double)i/(double)stepsF)*(maxF-minF)+minF;
			System.out.println("NEW PARAMETER F: "+curF);
			for(int j = 0; j<=stepsCR; j++) {
				double curCR=((double)j/(double)stepsCR)*(maxCR-minCR)+minCR;
				for(int p = 0; p<2; p++) {
					System.out.println("NEW PARAMETER CR: "+curCR);
					DErand1 rand1 = new DErand1(N, NP, curF, curCR, maxGenerations, upperBound, lowerBound, 8,1);
					SolverResult result=rand1.solve();
					System.out.println("Result: "+result.toString()+" with parameters F: "+curF+" and CR: "+curCR+" and Particle "+rand1.bestParticle);

					if(result.value<bestRes) {
						bestRes=result.value;
						bestF=curF;
						bestCR=curCR;
						System.out.println("Found new best result: "+bestRes+" with parameters F: "+bestF+" and CR: "+bestCR+" and Particle "+rand1.bestParticle);
	
						
					}
				}
				
			}
			

				
		}
		System.out.println("Best overall parameters f: "+bestF+" and CR: "+bestCR);



		
		}
		

}

