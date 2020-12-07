package swe_mo.solver.pso;

import java.io.IOException;
import java.util.ArrayList;

import swe_mo.solver.Convergence;
import swe_mo.solver.SolverConfig;
import swe_mo.solver.SolverManager;
import swe_mo.solver.SolverResult;

public class PSOnsc extends PSOgsc {

	int neighbors;
	

	public PSOnsc(int dimension, double min, double max, int particleCount, double w, double cc, double cs, double dt, int numIter,  int ffID, int solverID, int neighbors, double convergence) throws Exception {

		super(dimension, min, max, particleCount, w, cc, cs, dt, numIter,  ffID, convergence, solverID);
		c = new Convergence("PSOnsc");

		this.neighbors=neighbors;
		
		if(neighbors < 1) {
			throw new Exception("You need at least 1 neighbor");
			}
		if(neighbors > (particleCount-1)) {
			throw new Exception("You cannot have more neighbors than particles");
			}
	}
	
	
	public static SolverConfig defaultConfig() {
		//int ffid, int n, int nP, int maxGenerations, double upperBound, double lowerBound, double w, double cc, double cs, double dt, neighbors, convergence
		return new SolverConfig(1, 30, 100, 5000, 5.12, -5.12, 0.9, 0.5, 0.9, 1, 20, 1.0);
	}
	
	
	@Override
	public SolverResult solve() throws Exception {
		// This method is the engine of the solver, that creates the swarm and updates / finds the globalBestPosition
			
			
			int counter = 0;
				
			for(int i=0; i<particleCount; i++) {
				PSOparticle p = new PSOparticle(dimension, max, min, w, cc, cs, dt, particleCount, neighbors);
				swarm.add(p);
			}
			
			
			for(int i=0; i<numIter && SolverManager.checkTerminated(solverID); i++) {
				for(int j=0; j<particleCount; j++) {
					updateGlobalBestPosition(swarm.get(j));
					updateNC(swarm.get(j));
					swarm.get(j).updateVelocityNSC();
					swarm.get(j).updatePosition();
					swarm.get(j).updatePersonalBestPosition(ffID);
					
					//convergence related
					calculateRandomDifference(j);						
					//end convergence related
					
					counter++;
				}
				SolverManager.updateStatus(solverID, (100*((double)i)/((double)numIter)));
				
				boolean converged = c.update(sumOfDifferencesGlobal, globalMinimum);	
				
				if(converged&&convergence!=0.0) {
					c.file.close();
					return new SolverResult(globalMinimum, globalBestPosition, counter, i);
				}
				
			}
			ArrayList<Double> ret = new ArrayList<Double>();
			double val = (globalMinimum);
			ret.addAll(globalBestPosition);
			
			c.file.close();
			return new SolverResult(val, ret, counter, numIter);
		}
	
	public void updateNC(PSOparticle particle) {
		for(int i=0; i < (neighbors-1); i++) {
			int x = particle.neighborhood.get(i);
			if(particle.ncMinimum > swarm.get(x).personalMinimum) {
				particle.setNC(swarm.get(x).position);
				particle.ncMinimum=swarm.get(x).personalMinimum;
			}
			else if(particle.ncMinimum > particle.personalMinimum) {
				particle.setNC(particle.personalBestPosition);
				particle.ncMinimum=particle.personalMinimum;
			}
		}
	}
}
