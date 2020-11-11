package swe_mo.solver;

public class SolverResult {
	double value;
	//particle
	//other messages (number of iterations until result, ...)
	
	Exception e; //bitte stehen lassen, hier speichere ich die Exception, falls ihr in der solve Methode eine werft
	
	
	
	

	@Override
	public String toString() {
		return ""+value;
	}
	
	public String toJSON() {
		return "{\"value\":"+value+"}";
	}
}
