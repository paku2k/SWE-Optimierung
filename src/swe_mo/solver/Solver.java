package swe_mo.solver;

import java.util.LinkedList;
import java.util.Queue;



public class Solver {
	final String AUTH = "SLV";

	private int id;
	private Thread solverThread;	
	private String algorithm;
	private double status;
	private boolean terminated;
	private SolverResult result; 																						
	private SolverConfig config; 																						
		
	

	
	public Solver(int id, String algorithm) throws Exception{
		this.algorithm = algorithm;
		this.id = id;
		status = -2;
		terminated = false;
		config = SolverConfig.getDefault(algorithm);
	}
	public Solver(int id) throws Exception {
		this(id, "default");
	}																						
	
	

	
	public void configure(String configString) throws Exception{
		Queue<String> configQueue = new LinkedList<String>();
		String hyperparameterNotFound = "";
					
		for(String s : configString.replace(","," ").replace("    "," ").replace("   "," ").replace("  "," ").replace("= ","=").replace(" =","=").split(" ")) {
			if(s != "" && s != null)
				configQueue.offer(s);
		}

		for(String s : configQueue) {
			String[] split = s.split("=");
			try{
				config.set(split[0], split[1]);
			} catch(Exception e) {
				if(!hyperparameterNotFound.isEmpty()) hyperparameterNotFound += ", ";
				hyperparameterNotFound += split[0];
			}
		}			
					
		status = -1;
		
		if(!hyperparameterNotFound.isEmpty()) {
			if(hyperparameterNotFound.contains(","))
				throw new Exception("Configured without parameters: "+hyperparameterNotFound+" (not found or no valid value given)");
			else
				throw new Exception("Configured without parameter: "+hyperparameterNotFound+" (not found or no valid value given)");	
		}
	}
	
	public void resetConfig() throws Exception {
		config = SolverConfig.getDefault(algorithm);
		status = -2;
	}
	
	public void setConfig(SolverConfig sc) throws Exception {
		config = new SolverConfig(sc);
	}
	
	public SolverConfig getConfig() throws Exception {
		return config;
	}																						
	
	

	
	public void start(){				
		solverThread = new Thread(new Runnable() {	
			@Override
			public void run() {
				try {
					status = 0;
					result = SolverConfig.solveMethod(algorithm, id, config);
					if(status<=100) status = 101;
				} catch(Exception e) {
					result.e = e;
					status = 103;
				}
			}
		});
		solverThread.start();
	}
	
	public void terminate() {
		terminated = true;
		status = 102;
	}	
	
	public void joinThread(int tmax) {		
		try {
			solverThread.join(tmax);
		} catch (InterruptedException e) {}					
	}	
	public void joinThread() {		
		try {
			solverThread.join();
		} catch (InterruptedException e) {}					
	}																						
	
	

	
	public boolean getTerminated() {
		return terminated;
	}	
	
	public String getAlgorithm() {
		return algorithm;
	}
	
	public String getConfig(boolean json) {
		if(!json) {
			return config.toString();		
		} else {
			return config.toJSON();
		}
	}	
	
	public SolverResult getResult() throws InterruptedException {	
		joinThread(1000);
		return result;
	}
	
	public double getStatus() {
		return status;
	}																							
	
	

	
	public void updateStatus(double s) {
		status = s;
	}
}
