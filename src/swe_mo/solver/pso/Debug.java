package swe_mo.solver.pso;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Debug {

	public static void main(String[] args) throws IOException {

		// Test a new implementation here:
		int dimension = 30;
		double min = -500;
		double max = 500;
		int particleCount = 100;
		double w = 0.9;
		double cc = 0.9;
		double cs = 0.1;
		double dt = 1;
		int numIter = 2000;
		int solverID = 1;
		int ffID = 8;
		double decayStart = 0.8;
		double decayEnd = 0.1;
		
		//PSOgscDecay psoGlobalSolver = new PSOgscDecay(dimension, min, max, particleCount, w, cc, cs, dt, numIter, ffID, solverID, decayStart, decayEnd);
		PSOgsc psoGlobalSolver = new PSOgsc(dimension, min, max, particleCount, w, cc, cs, dt, numIter, ffID, solverID);
		System.out.println("Ergebnis: " + psoGlobalSolver.solve().value + " Position: " + Arrays.toString(psoGlobalSolver.solve().returnPosition.toArray()));
		
		// Run the Optimizer here:
		//double w_upper = 0.9;
		//double w_lower = 0.1;
		//double cc_upper = 0.9;
		//double cc_lower = 0.1;
		//double cs_upper = 0.9;
		//double cs_lower = 0.1;
		//int resolution = 10;
		//String filename = new String("psoOptimizerTest1");

		//psoOptimizer testOpti = new psoOptimizer(filename, dimension, min, max, particleCount, dt, numIter, ffID, solverID);
		//testOpti.psoGlobalLinear3DimensionalGridSearch(resolution, w_upper, w_lower, cc_upper, cc_lower, cs_upper, cs_lower);
		
	}
}
