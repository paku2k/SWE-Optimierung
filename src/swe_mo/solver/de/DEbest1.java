package swe_mo.solver.de;

import java.io.IOException;

import swe_mo.solver.Convergence;

public class DEbest1 extends DErand1{
	
	
	
	public DEbest1(int N, int NP, double F, double CR, int maxGenerations, double upperBound, double lowerBound,
			int ffIndex, int solverID, double convergence) throws IOException {
		super(N, NP, F, CR, maxGenerations, upperBound, lowerBound, ffIndex, solverID, convergence);
		c= new Convergence("DEbest1");

		
	}
	
	
	public DEbest1(int N, int NP, double F, double CR, int maxGenerations, 
			int ffIndex, int solverID, double convergence) throws IOException {
		super(N, NP, F, CR, maxGenerations,  ffIndex, solverID, convergence);
		c= new Convergence("DEbest");

		
	}
	
	@Override
	public Particle_DE calculateV(int index) {
		//calculates the Vector V for current generation
		Particle_DE p = this.calculateRandomDifference(index);
		
		p.multiply(this.F);
		p.add(bestParticle);

		
		
	
		for (int i = 0; i < p.position.size(); i++) {
			if(p.position.get(i)>this.upperBound) {
				p.position.set(i, this.upperBound);
			}
			if(p.position.get(i)<this.lowerBound) {
				p.position.set(i, this.lowerBound);
			}
		}
		
		//System.out.println("new particle v: "+p);

		//System.out.println("v: "+p);
		//System.out.println("x: "+xPop.get(index));


		
		return p;
	}
	
	
	

}
