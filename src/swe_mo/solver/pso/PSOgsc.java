package swe_mo.solver.pso;

import swe_mo.solver.*;
import swe_mo.solver.de.CRN;
import java.util.ArrayList;

public class PSOgsc {

	
	//convergence related
	double sumOfDifferencesGlobal;
	Convergence c;
	double convergence;
	boolean printPositionFile;
	FileGenerator g;
	String csv;

	//end convergence related
	
	

	int dimension;
	int numIter;
	int particleCount;
	int solverID;
	int ffID;
	double min;
	double max;
	double w;
	double cc;
	double cs;
	double dt;
	double globalMinimum = Double.MAX_VALUE;
	ArrayList<Double> globalBestPosition = new ArrayList<Double>();
	ArrayList<PSOparticle> swarm;


	

		public PSOgsc(int dimension, double min, double max, int particleCount, double w, double cc, double cs, double dt, int numIter,  int ffID, double convergence, int solverID, boolean printConvergenceFile, boolean printPositionFile) throws Exception {
		// This constructor creates and  initializes a psoGlobal-Solver for classical Particle Swarm Optimization.
			swarm = new ArrayList<PSOparticle>();


			c = new Convergence("PSOgscConvergence", printConvergenceFile, convergence);
			
			if(printPositionFile) {
				String header = "generation;";
				for (int i=0; i<particleCount; i++) {
					header=header+"P"+i+"solution;";
					for (int j=0; j<dimension; j++) {
						header=header+"P"+i+"axis"+j+";";
					}
				}
				
				
				g = new FileGenerator("PSOgsc_Positions_FFID"+ffID, header);
			}
			
			this.printPositionFile=printPositionFile;
			
			this.convergence = convergence;

			this.solverID = solverID;
			this.ffID = ffID;
			
			this.dimension = dimension;
			this.numIter = numIter;
			this.particleCount = particleCount;
			this.min = min;
			this.max = max;
			this.w = w;
			this.cc = cc;
			this.cs = cs;
			this.dt = dt;
			

			this.sumOfDifferencesGlobal=Double.MIN_VALUE;

			if(dimension < 1) {
				throw new Exception("You need at least 1 dimensions");
				}
			if(min >= max) {
				throw new Exception("Ranges are set incorrectly. Maximum must be greater than the minimum");
				}
			if(particleCount < 1) {
				throw new Exception("You need at least 1 Particle");
				}
			if(w < 0 || w > 1) {
				throw new Exception("w has to be between 0 and 1");
				}
			if(cc < 0 || cc > 1) {
				throw new Exception("cc has to be between 0 and 1");
				}
			if(cs < 0 || cs > 1) {
				throw new Exception("cs has to be between 0 and 1");
				}
			if(dt < 0 || dt > 2) {
				throw new Exception("dt has to be between 0 and 2");
				}
			if(numIter < 1) {
				throw new Exception("You need at least 1 Iteration");
				}

		}
		

			public static SolverConfig defaultConfig() {
				//this method sets the default parameters
				//int ffid, int n, int nP, int maxGenerations, double upperBound, double lowerBound, double w, double cc, double cs, double dt, double convergence, <print files>
				return new SolverConfig(1, 30, 100, 5000, 5.12, -5.12, 0.9, 0.5, 0.9, 1, 1.0, false, false);
			}
			
	
			
			public SolverResult solve() throws Exception {
			// This method is the engine of the solver, that creates the swarm and updates / finds the globalBestPosition 
				
				int counter = 0;
					
				for(int i=0; i<particleCount; i++) {
					PSOparticle p = new PSOparticle(dimension, max, min, w, cc, cs, dt);
					swarm.add(p);
				}
				
				
				for(int i=0; i<numIter && !SolverManager.checkTerminated(solverID); i++) {
				//for(int i=0; i<numIter; i++) {
					if(printPositionFile) {
						csv = ""+i+";";
					}
					
					sumOfDifferencesGlobal=0.0;
					
					for(int j=0; j<particleCount; j++) {
						updateGlobalBestPosition(swarm.get(j));
						swarm.get(j).updateVelocity(globalBestPosition);
						
						swarm.get(j).updatePosition();
						swarm.get(j).updatePersonalBestPosition(ffID);
						
						
						//convergence related
						calculateRandomDifference(j);						
						//end convergence related
					
						if(printPositionFile) {
							csv=csv+swarm.get(j).currentMinimum+";";
								for (int p=0; p<dimension; p++) {
									csv=csv+swarm.get(j).position.get(p)+";";
								}
							}
						
						
						counter++;
					}
					if(printPositionFile) {
						g.write(csv);
					}
					
					SolverManager.updateStatus(solverID, (100*((double)i)/((double)numIter)));
					boolean converged = c.update(sumOfDifferencesGlobal, globalMinimum);	
					
					if(converged) {
						c.closeFile();
						if(printPositionFile) {
							g.close();
						}
						return new SolverResult(globalMinimum, globalBestPosition, counter, i);
					}

					
				}
				ArrayList<Double> ret = new ArrayList<Double>();
				double val = (globalMinimum);
				ret.addAll(globalBestPosition);
				
				c.closeFile();
				if(printPositionFile) {
					g.close();
				}
				return new SolverResult(val, ret, counter, numIter);
			}
			
			
			public void updateGlobalBestPosition(PSOparticle particle) {
			// This method updates the globalBestPosition through calculating the corresponding value for a given position
				if(particle.personalMinimum<globalMinimum) {
					globalMinimum = particle.personalMinimum;
					globalBestPosition = new ArrayList<Double>(particle.position);
				}
			}
			
			public PSOparticle calculateRandomDifference(int skip) {
				
				int index1;
				int index2;
				PSOparticle newP;
				
				do {
					index1 = CRN.rInt((int)particleCount-1, 0);
				}
				while (index1 == skip);
				
				do {
					index2 = CRN.rInt((int)particleCount-1, 0);
				}
				while (index2 == skip || index2 == index1);
				
				newP = new PSOparticle(swarm.get(index1));
				newP.substract(swarm.get(index2));


				double sumOfDifferences=0.0;
				
				for (int i = 0; i < newP.position.size(); i++) {
					sumOfDifferences+=Math.abs(newP.position.get(i));
				}
				this.sumOfDifferencesGlobal+=sumOfDifferences;
				
				
				return newP;
				
			}
			
}
