package swe_mo.solver.pso;

public class debug {

	public static void main(String[] args) {

		int dimension = 30;
		double min = -500;
		double max = 500;
		int particleCount = 50;
		double w = 0.9;
		double cc = 0.5;
		double cs = 0.5;
		double dt = 1;
		int numIter = 5000;
		
		psoGlobal psoSolver = new psoGlobal(dimension, min, max, particleCount, w, cc, cs, dt, numIter);
		psoSolver.solve();
		
	}
}
