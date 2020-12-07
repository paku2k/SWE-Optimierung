package swe_mo.solver.pso;

import java.io.IOException;
import java.util.ArrayList;

import swe_mo.solver.Convergence;
import swe_mo.solver.SolverConfig;
import swe_mo.solver.SolverManager;
import swe_mo.solver.SolverResult;

public class PSOgscDecay extends PSOgsc{
	
	double decayStart;
	double decayEnd;
	

	public PSOgscDecay(int dimension, double min, double max, int particleCount, double w, double cc, double cs, double dt, int numIter,  int ffID, int solverID, double decayStart, double decayEnd, double convergence) throws Exception {

		
		super(dimension, min, max, particleCount, w, cc, cs, dt, numIter,  ffID, convergence, solverID);
		c = new Convergence("PSOgnscD");

		if(decayStart > decayEnd) {
			this.decayStart = decayStart;
			this.decayEnd = decayEnd;
		}else if(decayStart < decayEnd) {
			this.decayStart = decayEnd;
			this.decayEnd = decayStart;
		}
	}

	public static SolverConfig defaultConfig() {
		//int ffid, int n, int nP, int maxGenerations, double upperBound, double lowerBound, double w, double cc, double cs, double dt, double decayStart, double decayEnd, convergence
		return new SolverConfig(1, 30, 100, 5000, 5.12, -5.12, 0.9, 0.5, 0.9, 1, 0.9, 0.4, 1.0);
	}
	
	
	@Override
	public SolverResult solve() throws Exception {
		// This method is the engine of the solver, that creates the swarm and updates / finds the globalBestPosition
			
			int counter = 0;
				
			for(int i=0; i<particleCount; i++) {
				PSOparticle p = new PSOparticle(dimension, max, min, w, cc, cs, dt);
				swarm.add(p);
			}
			
			
			//for(int i=0; i<numIter && SolverManager.checkTerminated(solverID); i++) {
			for(int i=0; i<numIter; i++) {
				for(int j=0; j<particleCount; j++) {
					updateGlobalBestPosition(swarm.get(j));
					swarm.get(j).updateVelocityDecay(globalBestPosition, numIter, i, decayStart, decayEnd);
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
}
