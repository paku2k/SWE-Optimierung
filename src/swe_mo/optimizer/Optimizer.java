package swe_mo.optimizer;

import java.util.LinkedList;
import java.util.Queue;

import swe_mo.Settings;
import swe_mo.ui.clogger;

public class Optimizer {
	final String AUTH = "OPT";
	
	String algorithm;
	int id;
	String creator;
	double status;
	boolean terminated;
	private Thread optimizerThread;
	private OptimizerResult result = new OptimizerResult(); 																						
	private OptimizerConfig config; 						

	public Optimizer(int id, String creator, String algorithm) throws Exception{
		this.algorithm = algorithm;
		this.id = id;
		this.creator = creator;
		status = -2;
		terminated = false;
		
		config = OptimizerConfig.getDefault(algorithm);
	}
	public Optimizer(int id, String creator) throws Exception {
		this(id, creator, Settings.get("defaultOptimizer").toString());
	}			
	
	
	public void configure(String configString) throws Exception{
		Queue<String> configQueue = new LinkedList<String>();
		String hyperparameterNotFound = "";

		for(String s : configString.replace(","," ").replace("\t"," ").replace("    "," ").replace("   "," ").replace("  "," ").replace("= ","=").replace(" =","=").replace("|","/").replace("/ ","/").replace(" /","/").split(" ")) {
			if(s != "" && s != null)
				configQueue.offer(s);
		}

		for(String s : configQueue) {
			String[] split = s.split("=");
			try{
				if(split[1].contains("/")) {
					String[] value = split[1].split("/");
					config.set(split[0], value[0], value[1]);					
				} else {
					config.set(split[0], split[1]);	
				}
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
	
	public void configAddSHP(String string) throws Exception {
		Queue<String> configQueue = new LinkedList<String>();
		String notAllRequiredSettings = "";

		for(String s : string.replace(","," ").replace("\t"," ").replace("    "," ").replace("   "," ").replace("  "," ").replace("= ","=").replace(" =","=").replace("|","/").replace("/ ","/").replace(" /","/").split(" ")) {
			if(s != "" && s != null)
				configQueue.offer(s);
		}
		
		for(String s : configQueue) {
			String[] split = s.split("=");
			try{
				if(split[1].contains("/")) {
					String[] value = split[1].split("/");					
					config.addSHP(split[0], value[0], value[1]);	
				} else {
					throw new Exception();			
				}
			} catch(Exception e) {
				if(!notAllRequiredSettings.isEmpty()) notAllRequiredSettings += ", ";
				notAllRequiredSettings += split[0];		
			}
		}	
		
		status = -1;

		if(!notAllRequiredSettings.isEmpty()) {
			if(notAllRequiredSettings.contains(","))
				throw new Exception("Configured without parameters: "+notAllRequiredSettings+" (no valid configuration given)");
			else
				throw new Exception("Configured without parameter: "+notAllRequiredSettings+" (no valid configuration given)");	
		}
	}
	
	public void configRemoveSHP(String string) throws Exception {
		Queue<String> configQueue = new LinkedList<String>();
		
		for(String s : string.replace(","," ").replace("\t"," ").replace("    "," ").replace("   "," ").replace("  "," ").split(" ")) {
			if(s != "" && s != null)
				configQueue.offer(s);
		}
		
		for(String s : configQueue) {
			config.removeSHP(s);	
		}	
	}
	
	public void resetConfig() throws Exception {
		config = OptimizerConfig.getDefault(algorithm);
		status = -2;
	}
	
	public OptimizerConfig getConfig() throws Exception {
		return config;
	}																						
	
	

	
	public void start(){				
		optimizerThread = new Thread(new Runnable() {	
			@Override
			public void run() {
				try {
					status = 0;
					result = OptimizerConfig.solveMethod(algorithm, id, config);
					if(status<=100) status = 101;
				} catch(Exception e) {
					result.e = e;
					clogger.err(AUTH, "OptimizerThread "+id, e);
					status = 103;
				}
			}
		});
		optimizerThread.start();
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
			optimizerThread.join(tmax);
		} catch (InterruptedException e) {}					
	}	
	public void joinThread() {		
		try {
			optimizerThread.join();
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
	
	public OptimizerResult getResult() throws InterruptedException {	
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
