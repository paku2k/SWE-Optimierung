package swe_mo.solver;

import swe_mo.ui.clogger;





public class Solver {
	final String AUTH = "SLV";
	
	private Thread solverThread;
	
	private int id;
	private String algorithm;
	private double status;
	private double status_old;
	private int result;
	private int config;
	private boolean terminated;

	
	public Solver(int id, String algorithm){
		this.algorithm = algorithm;
		this.id = id;
		status = -2;
		terminated = false;
		
		//set default config
	}
	public Solver(int id) {
		this(id, "default");
	}
		
	
	
	public void configure(String parameter, Object value) throws Exception{

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
	
	public boolean getTerminatedStatus() {
		return terminated;
	}
	
	
	
	public int getResult() throws InterruptedException {
		if(status>100) {
			solverThread.join();
			return result;
		}
		return 0;
	}
	
	public double status() {
		return status;
	}
	
	public boolean statusChng() {
		boolean r = status!=status_old;
		status_old = status;
		return r;
	}
	
	
	
	
	public void updateStatus(double s) {
		status = s;
	}
	
	

	
	
	
	
	
	public class Runnable_DE implements Runnable{	
		test_david t = new test_david(id,config);
		@Override
		public void run() {
			result = t.calc();//DEsolve
			if(status<=100) status = 101;
		}
	}
	
	
	public class Runnable_PSO implements Runnable{	
		@Override
		public void run() {
			result = new test_david(id, config).calc();//PSOsolve
		}
	}
	
}
