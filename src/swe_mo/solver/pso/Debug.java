package swe_mo.solver.pso;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import swe_mo.solver.SolverResult;

public class Debug {

	public static void main(String[] args) throws IOException {

		// Test a new implementation here:
		int dimension = 30;
		double min = -5.12;
		double max = 5.12;
		int particleCount = 200;
		double w = 0.9;
		double cc = 0.65;
		double cs = 0.65;
		double dt = 0.5;
		int numIter = 2500;
		int solverID = 1;
		int ffID = 1;
		double decayStart = 0.9;
		double decayEnd = 0.4;
		
		PSOgsc psoGlobalSolver = new PSOgsc(dimension, min, max, particleCount, w, cc, cs, dt, numIter, ffID, solverID);
		SolverResult sr = new PSOgsc(dimension, min, max, particleCount, w, cc, cs, dt, numIter, ffID, solverID).solve();

		System.out.println(sr.toString());
		
		// Run the Optimizer here:
		/*double w_upper = 0.9;
		double w_lower = 0.9;
		double cc_upper = 0.85;
		double cc_lower = 0.75;
		double cs_upper = 0.03;
		double cs_lower = 0.015;
		int resolution = 20;
		String filename = new String("f8_N30_pc1000_numIter10000_dt1,9");*/

		//PSOoptimizer testOpti = new PSOoptimizer(filename, dimension, min, max, particleCount, dt, numIter, ffID, solverID);
		//testOpti.psoGlobalLinear3DimensionalGridSearch(resolution, w_upper, w_lower, cc_upper, cc_lower, cs_upper, cs_lower);
	}
}
