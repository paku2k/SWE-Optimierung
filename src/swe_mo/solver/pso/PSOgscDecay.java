package swe_mo.solver.pso;

import java.util.ArrayList;

import swe_mo.solver.SolverConfig;
import swe_mo.solver.SolverManager;
import swe_mo.solver.SolverResult;

public class PSOgscDecay extends PSOgsc{
	
	double decayStart;
	double decayEnd;
	
	public PSOgscDecay(int dimension, double min, double max, int particleCount, double w, double cc, double cs, double dt, int numIter,  int ffID, int solverID, double decayStart, double decayEnd) {
		
		super(dimension, min, max, particleCount, w, cc, cs, dt, numIter,  ffID, solverID);
		this.decayStart = decayStart;
		this.decayEnd = decayEnd;
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
		conf.decayStart = 0.9;
		conf.decayEnd = 0.4;
		
		return conf;
	}
	
	
	@Override
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
					counter++;
					swarm.get(j).updateVelocityDecay(globalBestPosition, numIter, i, decayStart, decayEnd);
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
}
