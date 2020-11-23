package swe_mo.solver.pso;

import java.util.ArrayList;

import swe_mo.solver.SolverConfig;
import swe_mo.solver.SolverManager;
import swe_mo.solver.SolverResult;

public class PSOnsc extends PSOgsc {

	ArrayList<PSOparticle> swarm = new ArrayList<PSOparticle>();
	
	
	public PSOnsc(int dimension, double min, double max, int particleCount, double w, double cc, double cs, double dt, int numIter,  int ffID, int solverID) {
		super(dimension, min, max, particleCount, w, cc, cs, dt, numIter,  ffID, solverID);
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
	
	
	@Override
	public SolverResult solve() {
		// This method is the engine of the solver, that creates the swarm and updates / finds the globalBestPosition
			
			
			int counter = 0;
				
			for(int i=0; i<particleCount; i++) {
				PSOparticle p = new PSOparticle(dimension, max, min, w, cc, cs, dt);
				swarm.add(p);
			}
			
			
			//for(int i=0; i<numIter && SolverManager.checkTerminated(solverID); i++) {
			for(int i=0; i<numIter; i++) {
				for(int j=0; j<particleCount; j++) {
					updateNC(j);
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
	
	public void updateNC(int j) {
		if(swarm.get(j).personalMinimum < swarm.get(j+1).personalMinimum) {
			swarm.get(j).setNC(swarm.get(j+1).personalBestPosition);
		}else if(swarm.get(j).personalMinimum < swarm.get(j-1).personalMinimum) {
			swarm.get(j).setNC(swarm.get(j-1).personalBestPosition);	
		}
	}
}
