package swe_mo.solver;

import java.util.LinkedList;
import java.util.Queue;

import swe_mo.Settings;
import swe_mo.ui.clogger;



public class Solver {
	final String AUTH = "SLV";

	private int id;
	private String creator;
	private Thread solverThread;	
	private String algorithm;
	private double status;
	private boolean terminated;
	private SolverResult result = new SolverResult(); 																						
	private SolverConfig config; 																						
		
	

	
	public Solver(int id, String creator, String algorithm) throws Exception{
		this.algorithm = algorithm;
		this.id = id;
		this.creator = creator;
		status = -2;
		terminated = false;
		config = SolverConfig.getDefault(algorithm);
	}
	public Solver(int id, String creator) throws Exception {
		this(id, creator, Settings.get("defaultAlgorithm").toString());
	}																						
	
	

	
	public void configure(String configString) throws Exception{
		Queue<String> configQueue = new LinkedList<String>();
		String hyperparameterNotFound = "";
					
		for(String s : configString.replace(","," ").replace("\t"," ").replace("    "," ").replace("   "," ").replace("  "," ").replace("= ","=").replace(" =","=").split(" ")) {
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
					clogger.err(AUTH, "SolverThread "+id, e);
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
	
	public void clear() {
		joinThread(2000);
		terminated = false;
		status = -1;
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
	
	public String getCreator() {
		return creator;
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
		if(s > 0 && s <= 100 && terminated) return;
		status = s;
	}
}
