package swe_mo.solver.pso;

public class debug {

	public static void main(String[] args) {

		psoGlobal psoSolver = new psoGlobal(5, -5.12, 5.12, 100, 0.7, 0.6, 0.5, 0.9, 5000);
		psoSolver.solve();
	}
}
