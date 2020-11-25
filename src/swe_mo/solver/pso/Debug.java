package swe_mo.solver.pso;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import swe_mo.solver.SolverResult;

public class Debug {

	public static void main(String[] args) throws Exception {

		// Test a new implementation here:
		int dimension = 30;
		double min = -5.12;
		double max = 5.12;
		int particleCount = 100;
		double w = 0.9;
		double cc = 0.65;
		double cs = 0.65;
		double dt = 0.5;
		int numIter = 50000;
		int solverID = 1;
		int ffID = 1;
		double decayStart = 0.9;
		double decayEnd = 0.4;
		int neighbors = 50;
		//PSOgscDecay psoGlobalSolver = new PSOgscDecay(dimension, min, max, particleCount, w, cc, cs, dt, numIter, ffID, solverID, decayStart, decayEnd);
		SolverResult sr = new PSOnsc(dimension, min, max, particleCount, w, cc, cs, dt, numIter, ffID, solverID, neighbors).solve();
		//SolverResult sr = new PSOgsc(dimension, min, max, particleCount, w, cc, cs, dt, numIter, ffID, solverID).solve();
		
		
		System.out.println(sr.toString());
		
		// Run the Optimizer here:
		/*double w_upper = 0.91;
		double w_lower = 0.8;
		double cc_upper = 0.7;
		double cc_lower = 0.6;
		double cs_upper = 0.2;
		double cs_lower = 0.07;
		int resolution = 10;
		String filename = new String("f8_N30_pc1000_numIter10000_dt0.1_nsc1");

		PSOoptimizer testOpti = new PSOoptimizer(filename, dimension, min, max, particleCount, dt, numIter, ffID, solverID);
		testOpti.psoGlobalLinear3DimensionalGridSearch(resolution, w_upper, w_lower, cc_upper, cc_lower, cs_upper, cs_lower);
	*/
	}
}
