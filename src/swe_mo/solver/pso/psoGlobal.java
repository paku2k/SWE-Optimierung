package swe_mo.solver.pso;

import java.util.ArrayList; 

public class psoGlobal {

	int dimension;
	double globalMinimum;
	int particleCount;
	
	ArrayList<Double> constraints = new ArrayList<Double>();
	double [] globalBestPosition;
	
		public psoGlobal(int dimension, ArrayList<Double> constraints, int particleCount) {
		
			this.dimension = dimension;
			this.constraints = constraints;
			this.particleCount = particleCount;
		}
	
			public void solve() {
				
				ArrayList<psoParticle> swarm = new ArrayList<psoParticle>(particleCount);
				for(int i=0; i<particleCount; i++) {
					// swarm.get(i).initialize(constraints);
					
				}
			}
			
			public void updateGlobalBestPosition() {
				
				
			}
			
}
