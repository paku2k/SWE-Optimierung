package swe_mo.solver;

import swe_mo.ui.clogger;

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
		
		
		
					//assemble the config class
					
					
					Queue<String> config_q = new LinkedList<String>();
					String[] config_arr = configString.replace("    ","").replace("   ","").replace("  ","").replace(" ","").split("/");
					for(String s : config_arr) {
						if(s != "" && s != null)
							config_q.offer(s);
					}
		
					config.N = Integer.parseInt(config_q.poll());
				
					
		status = -1;
	}
	
	public void start(){				
		solverThread = new Thread(new Runnable() {	
			@Override
			public void run() {
				try {
					result = SolverConfig.solveMethod(algorithm, id, config);
					if(status<=100) status = 101;
				} catch(Exception e) {
					clogger.err(AUTH, "Runnable_Solver id="+id, e);
					status = 103;
				}
				try {
					solverThread.join();
				} catch (InterruptedException e) {
					clogger.err(AUTH, "Runnable_Solver id="+id, e);					
				}
			}
		});
		solverThread.start();
		status = 0;
	}
	
	public void terminate() {
		terminated = true;
		status = 102;
	}																					
	
	

	
	public boolean getTerminated() {
		return terminated;
	}	
	
	public String getAlgorithm() {
		return algorithm;
	}	
	
	public SolverResult getResult() throws InterruptedException {														
		return result;
	}
	
	public double getStatus() {
		return status;
	}																					
	
	

	
	public void updateStatus(double s) {
		status = s;
	}
}
