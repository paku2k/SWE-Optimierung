package swe_mo.solver.pso;

import swe_mo.solver.*;
import java.util.ArrayList;

public class PSOgsc {

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

	
		public PSOgsc(int dimension, double min, double max, int particleCount, double w, double cc, double cs, double dt, int numIter,  int ffID, int solverID) {
		// This constructor creates and  initializes a psoGlobal-Solver for classical Particle Swarm Optimization.
			
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
		}
		
			public static SolverConfig defaultConfig() {
				//int ffid, int n, int nP, int maxGenerations, double upperBound, double lowerBound, double w, double cc, double cs, double dt
				return new SolverConfig(1, 1, 10, 100, 5, -5, 0.9, 0.5, 0.5, 1);
			}
			
	
			
			public SolverResult solve() {
			// This method is the engine of the solver, that creates the swarm and updates / finds the globalBestPosition
				
				ArrayList<PSOparticle> swarm = new ArrayList<PSOparticle>();
				int counter = 0;
					
				for(int i=0; i<particleCount; i++) {
					PSOparticle p = new PSOparticle(dimension, max, min, w, cc, cs, dt);
					swarm.add(p);
				}
				
				
				//for(int i=0; i<numIter && SolverManager.checkTerminated(solverID); i++) {
				for(int i=0; i<numIter; i++) {
					for(int j=0; j<particleCount; j++) {
						updateGlobalBestPosition(swarm.get(j));
						swarm.get(j).updateVelocity(globalBestPosition);
						swarm.get(j).updatePosition();
						swarm.get(j).updatePersonalBestPosition(ffID);
						counter++;
					}
					SolverManager.updateStatus(solverID, (100*((double)i)/((double)numIter)));
				}
				ArrayList<Double> ret = new ArrayList<Double>();
				double val = (globalMinimum);
				ret.addAll(globalBestPosition);
				return new SolverResult(val, ret, counter);
			}
			
			
			public void updateGlobalBestPosition(PSOparticle particle) {
			// This method updates the globalBestPosition through calculating the corresponding value for a given position
				
				//double minimum = FitnessFunction.solve(ffID, particle);
				if(particle.personalMinimum<globalMinimum) {
					globalMinimum = particle.personalMinimum;
					globalBestPosition = new ArrayList<Double>(particle.position);
					
				}
			}
			
}
