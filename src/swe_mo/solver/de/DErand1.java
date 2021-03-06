package swe_mo.solver.de;
import swe_mo.solver.SolverManager;
import swe_mo.solver.SolverResult;
import swe_mo.solver.SolverConfig;
import swe_mo.solver.Convergence;
import swe_mo.solver.FileGenerator;

import java.io.IOException;


import java.util.ArrayList;

import swe_mo.solver.FitnessFunction;



public class DErand1 {
	
	FileGenerator g;

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
	
	double sumOfDifferencesGlobal;
	boolean printConvergenceFile;
	boolean printPositionFile;

	double convergenceCrit;
	Convergence c;
	
	String csv;

	Particle_DE bestParticle;
	int ffIndex;
	ArrayList<Double> lastResult;
	ArrayList<Particle_DE> xPop;
	int numberOfCalls = 0;
	
	
	
	public DErand1(int N, int NP, double F, double CR, int maxGenerations, double upperBound, double lowerBound, int ffIndex, int solverID, double convergence, boolean printConvergenceFile, boolean printPositionFile) throws IOException {
		//With this constructor the population will be created with random set particles within the provided bounds. 
		
		this(N, NP, F, CR, maxGenerations, ffIndex, solverID, convergence, printConvergenceFile, printPositionFile);
		
		this.upperBound=upperBound;
		this.lowerBound=lowerBound;

		xPop.clear();
		
		for(int i = 0; i<NP; i++) {
			Particle_DE part = new Particle_DE(N, upperBound, lowerBound);

	    	xPop.add(part);
	    }
		this.convergenceCrit=NP*N*(upperBound-lowerBound)*10E-5*convergence;
		this.bestParticle  = new Particle_DE(N, upperBound,lowerBound);
		
	}
	
	public static SolverConfig defaultConfig() {		
		return new SolverConfig(1,5,50,0.3,0.3,1000,5.12,-5.12, 1.0, false, false);
	}

	
	
	public DErand1(int N, int NP, double F, double CR, int maxGenerations, int ffIndex, int solverID, double convergence, boolean printConvergenceFile, boolean printPositionFile) throws IOException {
		//If, for whatever reason, the population should be populated manually, this constructor can be used
		// it will initialize all NP particles with all dimensions to be zero
		
		//Bounds are created to be max.

		String s="";
		if(N<1) {
			s += "N";
		}
		if(NP<3) {
			if(!s.equals("")) {
				s+=", ";
			}
			s+="NP";
		}
		if(F<0) {
			if(!s.equals("")) {
				s+=", ";
			}
			s+="F";
		}
		if(CR<0) {
			if(!s.equals("")) {
				s+=", ";
			}
			s+="CR";
		}
		if(CR>1) {
			if(!s.equals("")) {
				s+=", ";
			}
			s+="CR";
		}
		if(convergence<0) {
			if(!s.equals("")) {
				s+=", ";
			}
			s+="convergence";
		}
		if(!s.equals("")) {
			throw new IOException("Please check your input parameters ("+s+").");
		}

		
		this.printConvergenceFile=printConvergenceFile;
		this.printPositionFile=printPositionFile;
		
		if(printPositionFile) {
			String header = "generation;";
			for (int i=0; i<NP; i++) {
				header=header+"P"+i+"solution;";
				for (int j=0; j<N; j++) {
					header=header+"P"+i+"axis"+j+";";
				}
			}
			
			
			g = new FileGenerator("DErand1_Positions_FFID"+ffIndex, header);
		}		
		c = new Convergence("DErand1Convergence", printConvergenceFile, convergence);


		
		this.upperBound=Double.MAX_VALUE;
		this.lowerBound=-Double.MAX_VALUE;
		
		this.fitCount=0;
		this.solverID=solverID;
		this.sumOfDifferencesGlobal=Double.MIN_VALUE;
		this.convergenceCrit=NP*N*(upperBound-lowerBound)*10E-5*convergence;


		this.bestParticle  = new Particle_DE(N);
		this.best = Double.MAX_VALUE;
		this.N=N;
		this.NP=NP;
		this.F=F;
		this.CR=CR;
		this.ffIndex=ffIndex;
		
		lastResult = new ArrayList<Double>();

		this.xPop = new ArrayList<Particle_DE>();


		this.generation=0;

		this.maxGenerations=maxGenerations;
		

		for(int i = 0; i<NP; i++) {
			Particle_DE part = new Particle_DE(N, upperBound, lowerBound);
	    	xPop.add(part);
	    }
		
		for(int i = 0; i<NP; i++) {
	    	lastResult.add(Double.MAX_VALUE);
	    }
	}
	
	public SolverResult solve() throws Exception  {
		//set the result and status of the solvermanager
		
		SolverManager.updateStatus(solverID, 0.0);
		

		// Solver
		for(this.generation=0; generation<this.maxGenerations; generation++) {
			
			if(g!=null) {
				csv = ""+generation+";";
			}
			SolverManager.updateStatus(solverID, (100*((double)generation)/((double)this.maxGenerations)));
			if(SolverManager.checkTerminated(solverID)) {
				return new SolverResult(best, bestParticle.position, fitCount);

			}
			this.sumOfDifferencesGlobal=0.0;
			
		
			for(int i=0; i<NP; i++) {

				xPop.set(i, compare(i, crossOver(xPop.get(i), calculateV(i))));
				
			}
			
			if(g!=null) {
				g.write(csv);
			}
			
			boolean converged = c.update(sumOfDifferencesGlobal, best);
			if (converged) {
		
				c.closeFile();
				try {
					g.close();
				}
				catch (Exception e) {
					
				}

				return new SolverResult(best, bestParticle.position, fitCount, generation);

			}
		}
		

		c.closeFile();
		try {
			g.close();
		}
		catch (Exception e) {
			
		}
			
		return new SolverResult(best, bestParticle.position, fitCount, generation);

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
	
		return u;
	}
	
	
	public Particle_DE compare(int xIndex, Particle_DE vectorU) throws Exception {
		//Compares vectorX and vectorU and returns the better one. If both give the same result, vectorU is returned
		double xRes;

		if(generation == 0) {

			 xRes=FitnessFunction.solve(ffIndex, xPop.get(xIndex));

			fitCount+=1;
		}
		
		
		else {
			xRes=lastResult.get(xIndex);
		}
		
		Particle_DE x = xPop.get(xIndex);

		if(g!=null) {
		csv=csv+xRes+";";
			for (int j=0; j<N; j++) {
				csv=csv+x.position.get(j)+";";
			}
		}
		
		
		
		
		
		
		double uRes=FitnessFunction.solve(ffIndex, vectorU);
		fitCount+=1;

		
		if(xRes<uRes) {
			if(xRes<this.best) {
				this.best = xRes;
				bestParticle = new Particle_DE(xPop.get(xIndex));

			}

			return new Particle_DE(xPop.get(xIndex));
		}
		else
		{
			if(uRes<this.best) {
				this.best = uRes;
				bestParticle = new Particle_DE(vectorU);
			}

			lastResult.set(xIndex, uRes);
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
			index1 = CRN.rInt((int)NP-1, 0);
		}
		while (index1 == skip);
		
		do {
			index2 = CRN.rInt((int)NP-1, 0);
		}
		while (index2 == skip || index2 == index1);
		
		newP = new Particle_DE(xPop.get(index1));
		newP.substract(xPop.get(index2));


		double sumOfDifferences=0.0;
		
		for (int i = 0; i < newP.position.size(); i++) {
			sumOfDifferences+=Math.pow(Math.abs(newP.position.get(i)),2);

		}

		this.sumOfDifferencesGlobal+=Math.sqrt(sumOfDifferences);

		return newP;
	}
	


}
