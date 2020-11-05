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

		this.xPop = new ArrayList<Particle_DE>();

		this.generation=0;
		

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

		this.xPop = new ArrayList<Particle_DE>();


		this.generation=0;

		this.maxGenerations=maxGenerations;
		
				for(int i = NP; i>0; i--) {
					Particle_DE part = new Particle_DE(N);
			    	xPop.add(part);
			    }
	
	}
	
	public void solve() {
		// Solver
	}
	
	public Particle_DE calculateV(int index) {
		//calculates the Vector V for current generation
		Particle_DE p=this.calculateRandomDifference(index);
		p.multiply(this.F);
		p.add(xPop.get(index));
		
		
		return p;
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
		int index1;
		int index2;
		Particle_DE newP = new Particle_DE(this.N);
		
		do {
			index1 = CRN.rInt(0, (int)NP-1);
		}
		while (index1 == skip);
		
		do {
			index2 = CRN.rInt(0, (int)NP-1);
		}
		while (index2 == skip || index2 == index1);
		
		newP = xPop.get(index1);
		newP.substract(xPop.get(index2));
		
		
		return newP;
	}
	

}
