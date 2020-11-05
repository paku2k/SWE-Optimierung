package swe_mo.solver.de;

import swe_mo.solver.SolverManager;

public class test_david {
	private int result;
	private double status;
	private int id;
	
	private double status_m;
	
	private double MAX;
	
	public test_david(int id, int config){
		this.id = id;
		this.MAX = config;
	}
	
	public int calc() {
		status_m = 0;
		for(double i=0; i<MAX; i++) {
			result *= 0.1;
			result += i;
			status = i/MAX*100;
			i*=1.000000002;
			if(status > status_m+5) {
				SolverManager.updateStatus(id, status);
				status_m = status;
			}
		}
		SolverManager.updateStatus(id, 100);
		return result;
	}
}
