package swe_mo.solver.pso;

public class debug {

	public static void main(String[] args) {

		psoGlobal psoSolver = new psoGlobal(10, -5.12, 5.12, 25, 0.8, 0.5, 0.5, 1, 5000);
		psoSolver.solve();
	}
}
