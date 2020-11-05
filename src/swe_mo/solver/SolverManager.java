package swe_mo.solver;

import java.util.ArrayList;

import swe_mo.ui.clogger;




public class SolverManager {
	final static String AUTH = "SM";
	
	private static ArrayList<Solver> runningSolvers = new ArrayList<Solver>();
	
	

	public static int newSolver(String algorithm) {
		if(!algorithm.isEmpty())
			runningSolvers.add(new Solver(runningSolvers.size(), algorithm));
		else
			runningSolvers.add(new Solver(runningSolvers.size()));
		
		return runningSolvers.size()-1;
	}
	
	public static void configure(int id) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Solver with this ID.");
		} else if(status(id) == -1) {
			clogger.warn(AUTH, "start", "Overwriting existing configuration.");		
		} else if(status(id) == 100) {
			throw new Exception("Solving finished, preparing Results.");	
		} else if(status(id) == 101) {
			throw new Exception("Solving finished, Results already available.");	
		} else if(status(id) == 102) {
			throw new Exception("Solving terminated with failure.");				
		} else if(status(id) == 103) {
			throw new Exception("Solver not found (deleted).");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Solver already running.");						
		}
		runningSolvers.get(id).configure("max",2000000000);		
	}
	
	
	public static void start(int id) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Solver with this ID.");
		} else if(status(id) == -2) {
			clogger.warn(AUTH, "start", "Solver not configured, using default values.");		
		} else if(status(id) == 100) {
			throw new Exception("Solving finished, preparing Results.");	
		} else if(status(id) == 101) {
			throw new Exception("Solving finished, Results already available.");	
		} else if(status(id) == 102) {
			throw new Exception("Solving terminated with failure.");				
		} else if(status(id) == 103) {
			throw new Exception("Solver not found (deleted).");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Solver already running.");						
		}
		runningSolvers.get(id).start();
	}
	
	
	public static int result(int id) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Solver with this ID.");
		} else if(status(id) == -2) {
			throw new Exception("Solver not configured");	
		} else if(status(id) == -1) {
			throw new Exception("Solver not started");	
		} else if(status(id) == 100) {
			throw new Exception("Solving finished, preparing Results.");	
		} else if(status(id) == 102) {
			throw new Exception("Solving terminated with failure.");				
		} else if(status(id) == 103) {
			throw new Exception("Solver not found (deleted).");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Solver running.");						
		}
		return runningSolvers.get(id).getResult();
	}
	
	public static void delete(int id) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Solver with this ID.");	
		} else if(status(id) == 103) {
			throw new Exception("Solver already deleted.");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Solver is running. Try terminating.");						
		}
		runningSolvers.set(id, null);
	}
	
	
	
	public static double status(int id) {
		//-3		solver not created
		//-2 		solver initialized
		//-1		solver configured
		//[0,100] 	started, progress [%]
		//101 		ended, result ready
		//102 		error
		//103 		deleted
		if(id >= runningSolvers.size()) {
			return -3;			
		} else {
			try {
				return runningSolvers.get(id).status();
			} catch(Exception e) {
				return 103;	//Solver Object not accessible anymore -> deleted		
			}
		}
	}
	
	public static boolean statusChng(int id) {
		try {
			return runningSolvers.get(id).statusChng();
		} catch(Exception e) {
			return false;
		}
	}
	
	
	
	public static void updateStatus(int id, double status) {
		if(status < 0) 
			status = 0;
		if(status > 100)
			status = 100;
		
		if(status(id) >= 0 && status(id) <= 100)
			runningSolvers.get(id).updateStatus(status);
	}
}
