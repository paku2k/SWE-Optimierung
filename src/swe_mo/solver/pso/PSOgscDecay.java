package swe_mo.solver.pso;

import java.util.ArrayList;

public class PSOgscDecay extends PSOgsc{
	
	double decayStart;
	double decayEnd;
	
	public PSOgscDecay(int dimension, double min, double max, int particleCount, double w, double cc, double cs, double dt, int numIter,  int ffID, int solverID, double decayStart, double decayEnd) {
		
		super(dimension, min, max, particleCount, w, cc, cs, dt, numIter,  ffID, solverID);
		this.decayStart = decayStart;
		this.decayEnd = decayEnd;
	}

	@Override
	public ArrayList<Double> solve() {
		// This method is the engine of the solver, that creates the swarm and updates / finds the globalBestPosition
			
			ArrayList<PSOparticle> swarm = new ArrayList<PSOparticle>();
				
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
				}
			}
			ArrayList<Double> ret = new ArrayList<Double>();
			ret.add(globalMinimum);
			ret.addAll(globalBestPosition);
			return ret;
		}
}
