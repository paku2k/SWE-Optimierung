package swe_mo.solver.pso;

import java.util.ArrayList;

import swe_mo.solver.Convergence;
import swe_mo.solver.FileGenerator;
import swe_mo.solver.SolverConfig;
import swe_mo.solver.SolverManager;
import swe_mo.solver.SolverResult;

public class PSOgscDecay extends PSOgsc{
	
	double decayStart;
	double decayEnd;
	

	public PSOgscDecay(int dimension, double min, double max, int particleCount, double w, double cc, double cs, double dt, int numIter,  int ffID, int solverID, double decayStart, double decayEnd, double convergence, boolean printConvergenceFile, boolean printPositionFile) throws Exception {

		//This constructor sets all parameters for the decay PSO algorithm
		super(dimension, min, max, particleCount, w, cc, cs, dt, numIter,  ffID, convergence, solverID, false, false);
		this.printPositionFile=printPositionFile;

		c = new Convergence("PSOgnscDConvergence", printConvergenceFile, convergence);
		if(printPositionFile) {
			String header = "generation;";
			for (int i=0; i<particleCount; i++) {
				header=header+"P"+i+"solution;";
				for (int j=0; j<dimension; j++) {
					header=header+"P"+i+"axis"+j+";";
				}
			}
			
			
			g = new FileGenerator("PSOgscDecay_Positions_FFID"+ffID, header);
		}

		if(decayStart > decayEnd) {
			this.decayStart = decayStart;
			this.decayEnd = decayEnd;
		}else if(decayStart < decayEnd) {
			this.decayStart = decayEnd;
			this.decayEnd = decayStart;
		}
	}

	
	public static SolverConfig defaultConfig() {
		//this method sets the default parameters
		//int ffid, int n, int nP, int maxGenerations, double upperBound, double lowerBound, double w, double cc, double cs, double dt, double decayStart, double decayEnd, double convergence, <print files>
		return new SolverConfig(1, 30, 100, 5000, 5.12, -5.12, 0.9, 0.5, 0.9, 1, 0.9, 0.4, 1.0, false, false);
	}
	
	
	@Override
	public SolverResult solve() throws Exception {
		// This method is the engine of the solver, that creates the swarm and updates / finds the globalBestPosition with the decay PSO algorithm
			
			int counter = 0;
				
			for(int i=0; i<particleCount; i++) {
				PSOparticle p = new PSOparticle(dimension, max, min, w, cc, cs, dt);
				swarm.add(p);
			}
			
			
			for(int i=0; i<numIter && !SolverManager.checkTerminated(solverID); i++) {
				if(printPositionFile) {
					csv = ""+i+";";
				}
				
				sumOfDifferencesGlobal=0.0;
				
				for(int j=0; j<particleCount; j++) {
					updateGlobalBestPosition(swarm.get(j));
					swarm.get(j).updateVelocityDecay(globalBestPosition, numIter, i, decayStart, decayEnd);
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
