package swe_mo.solver;

import swe_mo.ui.clogger;




public class Solver {
	final String AUTH = "SLV";
	
	private Thread solverThread;
	
	private int id;
	private String algorithm;
	private double status;
	private boolean terminated;
	private int result; 																							//result class
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
																													//determine which method to use, initialize and start
		
		solverThread = new Thread(new Runnable_DE());
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
	
	
	public int getResult() throws InterruptedException {															//return result class
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
	
	

	
	
	
	
	
	public class Runnable_DE implements Runnable{	
		@Override
		public void run() {
			try {
				result = new test_david(id,config).calc();															//DEsolve
				if(status<=100) status = 101;
			} catch(Exception e) {
				clogger.err(AUTH, "Runnable_DE", e);
				status = 103;
			}
		}
	}
	
	
	public class Runnable_PSO implements Runnable{	
		@Override
		public void run() {
			try {
				result = new test_david(id,config).calc();															//PSOsolve
				if(status<=100) status = 101;
			} catch(Exception e) {
				clogger.err(AUTH, "Runnable_PSO", e);
				status = 103;
			}
		}
	}
	
}
