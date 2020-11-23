package swe_mo.solver.pso;

public class PSOnsc extends PSOgsc {

	public PSOnsc(int dimension, double min, double max, int particleCount, double w, double cc, double cs, double dt, int numIter,  int ffID, int solverID) {
		super(dimension, min, max, particleCount, w, cc, cs, dt, numIter,  ffID, solverID);
	}
	
}
