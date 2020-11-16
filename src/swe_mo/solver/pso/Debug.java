package swe_mo.solver.pso;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import swe_mo.solver.SolverResult;

public class Debug {

	public static void main(String[] args) throws IOException {

		// Test a new implementation here:
		int dimension = 30;
		double min = -500;
		double max = 500;
		int particleCount = 2000;
		int particleCount = 500;
		double w = 0.9;
		double cc = 0.82;
		double cs = 0.025;
		double dt = 1.9;
		int numIter = 5000;
		int solverID = 1;
		int ffID = 8;
		double decayStart = 0.9;
		double decayEnd = 0.4;
		
		//PSOgscDecay psoGlobalSolver = new PSOgscDecay(dimension, min, max, particleCount, w, cc, cs, dt, numIter, ffID, solverID, decayStart, decayEnd);
		SolverResult sr = new PSOgsc(dimension, min, max, particleCount, w, cc, cs, dt, numIter, ffID, solverID).solve();

		System.out.println(sr.toString());
		
		// Run the Optimizer here:
		/*double w_upper = 0.9;
		double w_lower = 0.1;
		double cc_upper = 0.9;
		double cc_lower = 0.1;
		double cs_upper = 0.9;
		double cs_lower = 0.1;
		int resolution = 10;
		String filename = new String("psoOptimizerTest1");

		PSOoptimizer testOpti = new PSOoptimizer(filename, dimension, min, max, particleCount, dt, numIter, ffID, solverID);
		testOpti.psoGlobalLinear3DimensionalGridSearch(resolution, w_upper, w_lower, cc_upper, cc_lower, cs_upper, cs_lower);
		*/
	}
}
