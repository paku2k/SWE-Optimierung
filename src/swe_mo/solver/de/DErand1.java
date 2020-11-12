package swe_mo.solver.de;
import swe_mo.solver.SolverManager;

import java.util.ArrayList;

import swe_mo.solver.FitnessFunction;

public class DErand1 {
	
	int N;
	int NP;
	double F;
	double CR;
	int maxGenerations;
	private int generation;
	double upperBound;
	double lowerBound;
	int fitCount;
	int solverID;
	double best;
	Particle_DE bestParicle;
	int ffIndex;
	ArrayList<Particle_DE> xPop;
	
	public DErand1(int N, int NP, double F, double CR, int maxGenerations, double upperBound, double lowerBound, int ffIndex, int solverID) {
		//With this constructor the population will be created with random set particles within the provided bounds. 
		
		this.solverID=solverID;
		
		this.fitCount=0;
		this.bestParicle  = new Particle_DE(N);
		this.best = Double.MAX_VALUE;
		this.N=N;
		this.NP=NP;
		this.F=F;
		this.CR=CR;
		this.upperBound= upperBound;
		this.lowerBound=lowerBound;
		this.maxGenerations=maxGenerations;
		this.ffIndex=ffIndex;

		this.xPop = new ArrayList<Particle_DE>();

		this.generation=0;
		

				for(int i = NP; i>0; i--) {
					Particle_DE part = new Particle_DE(N, upperBound, lowerBound);
			    	xPop.add(part);
			    }
	
	}
	
	public DErand1(int N, int NP, double F, double CR, int maxGenerations, int ffIndex, int solverID) {
		//If, for whatever reason, the population should be populated manually, this constructor can be used
		// it will initialize all NP particles with all dimensions to be zero
		
		//Bounds are created to be max.
		
		this.upperBound=Double.MAX_VALUE;
		this.lowerBound=-Double.MAX_VALUE;
		
		this.fitCount=0;
		this.solverID=solverID;


		this.bestParicle  = new Particle_DE(N);
		this.best = Double.MAX_VALUE;
		this.N=N;
		this.NP=NP;
		this.F=F;
		this.CR=CR;
		this.ffIndex=ffIndex;

		this.xPop = new ArrayList<Particle_DE>();


		this.generation=0;

		this.maxGenerations=maxGenerations;
		
				for(int i = NP; i>0; i--) {
					Particle_DE part = new Particle_DE(N);
			    	xPop.add(part);
			    }
	
	}
	
	public double solve() {
		
		SolverManager.updateStatus(solverID, 0.0);
		

		// Solver
		for(this.generation=0; generation<this.maxGenerations; generation++) {
			//SolverManager.updateStatus(solverID, (((double)generation)/((double)this.maxGenerations)));
			//if(SolverManager.checkTerminated(solverID)) {
			//	break;
			//}


			
			for(int i=0; i<NP; i++) {

				xPop.set(i, compare(xPop.get(i), crossOver(xPop.get(i), calculateV(i))));

				
			}
			//System.out.println("\n\n NEW GENERATION \n\n");

		}

		return best;
	}
	
	public Particle_DE calculateV(int index) {
		//calculates the Vector V for current generation
		Particle_DE p=this.calculateRandomDifference(index);
		p.multiply(this.F);
		p.add(xPop.get(index));

	
		for (int i = 0; i < p.position.size(); i++) {
			if(p.position.get(i)>this.upperBound) {
				p.position.set(i, this.upperBound);
			}
			if(p.position.get(i)<this.lowerBound) {
				p.position.set(i, this.lowerBound);
			}
		}
		
		//System.out.println("v: "+p);
		//System.out.println("x: "+xPop.get(index));


		
		return p;
	}
	
	public Particle_DE crossOver(Particle_DE vectorX, Particle_DE vectorV) {
		//Crosses over vectorX and vectorV to generate vectorU which may be used in the next generation
		int n = CRN.rInt(N, 0);
		int L = 0;
		Particle_DE u = new Particle_DE(N);
		
		do {
			L++;
		}
	
		while( (CRN.rn(1, 0)<this.CR) && (L<N) );
		//System.out.println("L: "+L);
		//System.out.println("n: "+n);

		
		for(int j = 0; j<(vectorV.position.size()*2); j++) {
			
			if(j>=n&&j<=(n+L-1)) {
				u.position.set(j%N, vectorV.position.get(j%N));
			}
			else {
				if(j<N) {
					u.position.set(j, vectorX.position.get(j));
				}
			}
			
		}

		//System.out.println("u: "+u);

		
		return u;
	}
	
	public Particle_DE compare(Particle_DE vectorX, Particle_DE vectorU) {
		//Compares vectorX and vectorU and returns the better one. If both give the same result, vectorU is returned
		double xRes=FitnessFunction.solve(ffIndex, vectorX);
		double uRes=FitnessFunction.solve(ffIndex, vectorU);
		fitCount+=2;
		//System.out.println("current best: "+best);
		//System.out.println("xRes: "+xRes);
		//System.out.println("uRes: "+uRes);

		
		if(xRes<uRes) {
			if(xRes<this.best) {
				this.best = xRes;
				bestParicle = vectorX;
				System.out.println("Best Value:"+ best);
				System.out.println("In generation: "+ generation);


				//System.out.println("BestX: "+vectorX);

			}
			

			return vectorX;
		}
		else
		{
			if(uRes<this.best) {
				this.best = uRes;
				bestParicle = vectorU;
				System.out.println("Best Value:"+ best);
				System.out.println("In generation: "+ generation);

				//System.out.println("BestU: "+vectorU);


			}

			
			return vectorU;
		}
		
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
