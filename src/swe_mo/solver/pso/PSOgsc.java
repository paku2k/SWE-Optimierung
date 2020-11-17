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
				SolverConfig conf = new SolverConfig();
				conf.ffid = 1;
				conf.N = 1;
				conf.NP = 10;
				conf.maxGenerations = 100;
				conf.lowerBound = -5;
				conf.upperBound = 5;
				conf.w = 0.9;
				conf.cc = 0.5;
				conf.cs = 0.5;
				conf.dt = 1;
				
				return conf;
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
