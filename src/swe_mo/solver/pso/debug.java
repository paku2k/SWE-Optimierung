package swe_mo.solver.pso;

public class debug {

	public static void main(String[] args) {

		int dimension = 5;
		double min = -5.12;
		double max = 5.12;
		int particleCount = 200;
		double w = 0.7;
		double cc = 0.0;
		double cs = 0.5;
		double dt = 1;
		int numIter = 5000;
		
		psoGlobal psoSolver = new psoGlobal(dimension, min, max, particleCount, w, cc, cs, dt, numIter);
		psoSolver.solve();
	}
}
