package swe_mo.solver.pso;

import java.util.ArrayList;
import java.util.Arrays; 
//import swe_mo.solver.pso.*;

public class psoGlobal {

	int dimension;
	double globalMinimum;
	int particleCount;
	double min;
	double max;
	ArrayList<Double> globalBestPosition = new ArrayList<Double>();
	double w;
	double cc;
	double cs;
	double dt;
	int numIter;
	debugFitness debugFitter = new debugFitness();

	
		public psoGlobal(int dimension, double min, double max, int particleCount, double w, double cc, double cs, double dt, int numIter) {
		
			this.dimension = dimension;
			this.min = min;
			this.max = max;
			this.particleCount = particleCount;
			this.w = w;
			this.cc = cc;
			this.cs = cs;
			this.dt = dt;
			this.numIter = numIter;
			globalMinimum = 100000;
		}
	
			public void solve() {
				
				ArrayList<psoParticle> swarm = new ArrayList<psoParticle>();
					
				for(int i=0; i<particleCount; i++) {
					psoParticle p = new psoParticle(dimension, max, min, w, cc, cs, dt);
					swarm.add(p);
				}
				
				for(int i=0; i<numIter; i++) {
					for(int j=0; j<particleCount; j++) {
						swarm.get(j).updateVelocity(globalBestPosition);
						swarm.get(j).updatePosition();
						swarm.get(j).updatePersonalBestPosition();
						updateGlobalBestPosition(dimension, swarm.get(j).position);
					}
					System.out.printf("Minimum in Iteration %i: %d", i, globalMinimum);
					System.out.printf("Beste Position in Iteration %i: %s", i, Arrays.toString(swarm.toArray()));
				}
			}
			
			
			public void updateGlobalBestPosition(int dimension, ArrayList<Double> position) {
				double minimum = debugFitter.calcSpehreFunction(dimension, position);
				if(minimum<globalMinimum) {
					globalMinimum = minimum;
				}
			}
			
}
