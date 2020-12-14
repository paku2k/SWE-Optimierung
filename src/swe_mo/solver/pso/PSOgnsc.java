package swe_mo.solver.pso;

import java.io.IOException;
import java.util.ArrayList;

import swe_mo.solver.Convergence;
import swe_mo.solver.FileGenerator;
import swe_mo.solver.SolverConfig;
import swe_mo.solver.SolverManager;
import swe_mo.solver.SolverResult;

public class PSOgnsc extends PSOnsc {
	
	public PSOgnsc(int dimension, double min, double max, int particleCount, double w, double cc, double cs, double dt, int numIter,  int ffID, int solverID, int neighbors, double convergence, boolean printConvergenceFile, boolean printPositionFile) throws Exception {
		super(dimension, min, max, particleCount, w, cc, cs, dt, numIter, ffID, solverID, neighbors, convergence, false, false);
		this.printPositionFile=printPositionFile;

		c = new Convergence("PSOgnscConvergence", printConvergenceFile, convergence);
		if(printPositionFile) {
			String header = "generation;";
			for (int i=0; i<particleCount; i++) {
				header=header+"P"+i+"solution;";
				for (int j=0; j<dimension; j++) {
					header=header+"P"+i+"axis"+j+";";
				}
			}
			
			
			g = new FileGenerator("PSOgnsc_Positions_FFID"+ffID, header);
		}

	}
	
	public static SolverConfig defaultConfig() {
		//int ffid, int n, int nP, int maxGenerations, double upperBound, double lowerBound, double w, double cc, double cs, double dt, neighbors, convergence
		return new SolverConfig(1, 30, 100, 5000, 5.12, -5.12, 0.9, 0.5, 0.9, 1, 10, 1.0, false, false);
	}
	
	
	@Override
	public SolverResult solve() throws Exception {
		// This method is the engine of the solver, that creates the swarm and updates / finds the globalBestPosition
			
			
			int counter = 0;
				
			for(int i=0; i<particleCount; i++) {
				PSOparticle p = new PSOparticle(dimension, max, min, w, cc, cs, dt, particleCount, neighbors);
				swarm.add(p);
			}
			
			
			for(int i=0; i<numIter && !SolverManager.checkTerminated(solverID); i++) {
				if(printPositionFile) {
					csv = ""+i+";";
				}
				
				sumOfDifferencesGlobal=0.0;
				
				for(int j=0; j<particleCount; j++) {
					updateGlobalBestPosition(swarm.get(j));
					updateNC(swarm.get(j));
					swarm.get(j).updateVelocityGNSC(globalBestPosition);
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
}
