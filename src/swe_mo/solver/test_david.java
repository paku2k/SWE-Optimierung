package swe_mo.solver;


public class test_david {
	private int result;
	private double status;
	private int id;
	
	private double status_m;
	
	private double MAX;
	

	public static SolverConfig defaultConfig() {
		SolverConfig sc = new SolverConfig();
		sc.N = 2000000000;
		return sc;
	}
	public static SolverConfig defaultDErand1Config() {
		SolverConfig sc = new SolverConfig();
		sc.N = 5;
		sc.NP = 50;
		sc.F = 0.8;
		sc.CR = 0.3;
		sc.maxGenerations = 500000;
		sc.upperBound = 5.12;
		sc.lowerBound = -5.12;
		return sc;
	}
							 
	
	public test_david(int id, Integer config){
		this.id = id;
		this.MAX = (double)config*1000L;
	}
	
	public SolverResult calc() {
		status_m = 0;
		for(double i=0; i<MAX && !SolverManager.checkTerminated(id); i++) {
			result *= 0.1;
			result += i;
			status = i/MAX*100;
			i*=1.000000002;
			if(status > status_m+2) {
				SolverManager.updateStatus(id, status);
				status_m = status;
			}
		}
		SolverResult sr = new SolverResult();
		sr.value = result;
		return sr;
	}
}
