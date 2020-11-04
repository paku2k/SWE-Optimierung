package swe_mo.solver;

import swe_mo.ui.clogger;





public class Solver {
	final static String AUTH = "SLV";
	
	private String algorithm;
	
	public Solver(String algorithm){
		clogger.dbg(AUTH,"solver","test");
		this.algorithm = algorithm;
	}
	public Solver() {
		this("default");
	}
	
	
	public void configure(String parameter, String value) throws Exception{
		
	}
	
	public void start() throws Exception{
		
	}
	
	public void terminate() throws Exception{
		
	}
	
	public boolean checkResult() {
		return false;
	}
	
	public void getResult() {
		
	}
	
}
