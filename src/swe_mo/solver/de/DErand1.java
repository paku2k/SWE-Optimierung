package swe_mo.solver.de;

import java.util.ArrayList;

public class DErand1 {
	
	int N;
	int NP;
	double F;
	double CR;
	int maxGenerations;
	private int generation;
	double upperBound;
	double lowerBound;
	FitnessFunction fF;
	ArrayList<Particle_DE> xPop;
	
	public DErand1(int N, int NP, double F, double CR, int maxGenerations, double upperBound, double lowerBound, FitnessFunction fF) {
		//With this constructor the population will be created with random set particles within the provided bounds. 
		
		this.N=N;
		this.NP=NP;
		this.F=F;
		this.CR=CR;
		this.upperBound= upperBound;
		this.lowerBound=lowerBound;
		this.maxGenerations=maxGenerations;
		this.fF=fF;
		
				for(int i = NP; i>0; i--) {
					Particle_DE part = new Particle_DE(N, upperBound, lowerBound);
			    	xPop.add(part);
			    }
	
	}
	
	public DErand1(int N, int NP, double F, double CR, int maxGenerations, FitnessFunction fF) {
		//If, for whatever reason, the population should be populated manually, this constructor can be used
		// it will initialize all NP particles with all dimensions to be zero
		
		//No bounds are created
		
		this.N=N;
		this.NP=NP;
		this.F=F;
		this.CR=CR;
		this.fF=fF;

		this.maxGenerations=maxGenerations;
		
				for(int i = NP; i>0; i--) {
					Particle_DE part = new Particle_DE(N);
			    	xPop.add(part);
			    }
	
	}
	
	public void solve() {
		// Solver
	}
	
	public Particle_DE calculateV(Particle_DE vectorX) {
		//calculates the Vector V for current generation
		return new Particle_DE(0);
	}
	
	public Particle_DE crossOver(Particle_DE vectorX, Particle_DE vectorV) {
		//Crosses over vectorX and vectorV to generate vectorU which will may used in the next generation
		return new Particle_DE(0);
	}
	
	public Particle_DE compare(Particle_DE vectorX, Particle_DE vectorU) {
		//Compares vectorX and vectorU and returns the better one. If both give the same result, vectorU is returned
		return vectorU;
	}
	
	public Particle_DE calculateRandomDifference(int skip) {
		//Calculates a random difference between two vectors of the population and returns it as a new vector
		//skip defines, what index to skip (because it belongs to the original vector)
		return new Particle_DE(0);
	}
	

}
