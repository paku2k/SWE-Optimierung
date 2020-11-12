package swe_mo.solver.pso;

import swe_mo.solver.*;
import java.util.ArrayList;
import java.util.Arrays;

public class psoGlobal {

	int dimension;
	int numIter;
	int particleCount;
	double min;
	double max;
	double w;
	double cc;
	double cs;
	double dt;
	double globalMinimum = Double.MAX_VALUE;
	ArrayList<Double> globalBestPosition = new ArrayList<Double>();
	FitnessFunction debugFitter = new FitnessFunction();

	
		public psoGlobal(int dimension, double min, double max, int particleCount, double w, double cc, double cs, double dt, int numIter) {
		// This constructor creates and  initializes a psoGlobal-Solver for classical Particle Swarm Optimization.
			
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
	
		
			public void solve() {
			// This method is the engine of the solver, that creates the swarm and updates / finds the globalBestPosition
				
				ArrayList<psoParticle> swarm = new ArrayList<psoParticle>();
					
				for(int i=0; i<particleCount; i++) {
					psoParticle p = new psoParticle(dimension, max, min, w, cc, cs, dt);
					swarm.add(p);
				}
				
				
				for(int i=0; i<numIter; i++) {
					for(int j=0; j<particleCount; j++) {
						updateGlobalBestPosition(swarm.get(j));
						swarm.get(j).updateVelocity(globalBestPosition);
						swarm.get(j).updatePosition();
						swarm.get(j).updatePersonalBestPosition();
					}
					System.out.println("Minimum in Iteration "+i+": "+globalMinimum);
					System.out.println("Beste Position in Iteration "+i+": "+Arrays.toString(swarm.toArray()));
				}
			}
			
			
			public void updateGlobalBestPosition(psoParticle particle) {
			// This method updates the globalBestPosition through calculating the corresponding value for a given position
				
				double minimum = debugFitter.calculatef8(particle);
				if(minimum<globalMinimum) {
					globalMinimum = minimum;
					globalBestPosition = new ArrayList<Double>(particle.position);
				}
			}
			
}
