package swe_mo.solver;

public class SolverResult {
	double value;
	//particle
	//other messages (number of iterations until result, ...)
	
	
	
	

	@Override
	public String toString() {
		return ""+value;
	}
	
	public String toJSON() {
		return "{\"value\":"+value+"}";
	}
}
