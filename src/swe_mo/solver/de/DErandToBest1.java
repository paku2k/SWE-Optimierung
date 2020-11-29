package swe_mo.solver.de;

import swe_mo.solver.SolverConfig;

public class DErandToBest1 extends DEbest1 {
	
	double lambda;

	
	
	public DErandToBest1(int N, int NP, double F, double CR, double lambda, int maxGenerations, double upperBound, double lowerBound,
			int ffIndex, int solverID, double convergence) throws Exception {
		super(N, NP, F, CR, maxGenerations, upperBound, lowerBound, ffIndex, solverID, convergence);
		this.lambda = lambda;
		
		if(NP < 4) {
			throw new Exception("You need at least 4 particles");
		}
	}

	
	
	public DErandToBest1(int N, int NP, double F, double CR, double lambda, int maxGenerations, int ffIndex, int solverID, double convergence)  throws Exception {
		super(N, NP, F, CR, maxGenerations, ffIndex, solverID, convergence);
		this.lambda = lambda;
		
		if(NP < 4) {
			throw new Exception("You need at least 4 particles");
			}
	}

	
	
	public static SolverConfig defaultConfig() {		
		return new SolverConfig(1,5,50,0.3,0.3,0.3,1000,5.14,-5.14,1);
	}
	
	
	
	@Override
	public Particle_DE calculateV(int index) {
		//calculates the Vector V for current generation
		Particle_DE p=this.calculateRandomDifference(index);

		
		
		p.multiply(this.F);
		Particle_DE differenceToBest = new Particle_DE(bestParticle);
		differenceToBest.substract(xPop.get(index));
		differenceToBest.multiply(this.lambda);
		p.add(differenceToBest);
		p.add(xPop.get(index));

		
	
		for (int i = 0; i < p.position.size(); i++) {
			if(p.position.get(i)>this.upperBound) {
				p.position.set(i, this.upperBound);
			}
			if(p.position.get(i)<this.lowerBound) {
				p.position.set(i, this.lowerBound);
			}
			
		}
		
		
		
		return p;
	}
	
	
	
}
