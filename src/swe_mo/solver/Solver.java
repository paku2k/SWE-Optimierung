package swe_mo.solver;

import swe_mo.ui.clogger;
import swe_mo.solver.de.*;
import swe_mo.solver.pso.*;




public class Solver {
	final String AUTH = "SLV";
	
	private Thread solverThread;
	
	private int id;
	private String algorithm;
	private double status;
	private boolean terminated;
	private double result; 																							//result class
	private int config; 																							//config class

	
	public Solver(int id, String algorithm){
		this.algorithm = algorithm;
		this.id = id;
		status = -2;
		terminated = false;
		
		config = 2000000000;																						//set default config
	}
	public Solver(int id) {
		this(id, "default");
	}
		
	
	
	public void configure(String parameter, Object value) throws Exception{											//use config class?

		config = (int)value;
				
		status = -1;
	}
	
	public void start(){				
		solverThread = new Thread(new Runnable() {	
			@Override
			public void run() {
				try {
					if(algorithm.equals("DErand1")) {
						result = new DErand1(5,50,0.8,0.3,500000,5.12,-5.12,new FitnessFunction()).solve();								
					} else {
						result = new test_david(id,config).calc();															
					}
					if(status<=100) status = 101;
				} catch(Exception e) {
					clogger.err(AUTH, "Runnable_Solver id="+id, e);
					status = 103;
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
	
	
	public double getResult() throws InterruptedException {															//return result class
		if(status>100) {
			solverThread.join();
			return result;
		}
		return 0;
	}
	
	public double status() {
		return status;
	}
	
	
	
	
	public void updateStatus(double s) {
		status = s;
	}
}
