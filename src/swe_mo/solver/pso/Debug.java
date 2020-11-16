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
		double w = 0.9;
		double cc = 0.83;
		double cs = 0.022;
		double dt = 1.9;
		int numIter = 5000;
		int solverID = 1;
		int ffID = 8;
		double decayStart = 0.9;
		double decayEnd = 0.4;
		
		PSOgscDecay psoGlobalSolver = new PSOgscDecay(dimension, min, max, particleCount, w, cc, cs, dt, numIter, ffID, solverID, decayStart, decayEnd);
		SolverResult sr = new PSOgsc(dimension, min, max, particleCount, w, cc, cs, dt, numIter, ffID, solverID).solve();

		System.out.println(sr.toString());
		
		// Run the Optimizer here:
		double w_upper = 0.9;
		double w_lower = 0.9;
		double cc_upper = 0.85;
		double cc_lower = 0.75;
		double cs_upper = 0.03;
		double cs_lower = 0.015;
		int resolution = 20;
		String filename = new String("f8_N30_pc1000_numIter10000_dt1,9");

		//PSOoptimizer testOpti = new PSOoptimizer(filename, dimension, min, max, particleCount, dt, numIter, ffID, solverID);
		//testOpti.psoGlobalLinear3DimensionalGridSearch(resolution, w_upper, w_lower, cc_upper, cc_lower, cs_upper, cs_lower);
		
	}
}
