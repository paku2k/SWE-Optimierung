package swe_mo.solver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import swe_mo.ui.clogger;




public class SolverManager {
	final static String AUTH = "SM";
	
	private static ArrayList<Solver> runningSolvers = new ArrayList<Solver>();
	
	
	

	public static String create() throws Exception{
		runningSolvers.add(new Solver(runningSolvers.size()));	
		return "Solver created (default) with ID "+(runningSolvers.size()-1);
	}

	public static String create(String algorithm) throws Exception {
		runningSolvers.add(new Solver(runningSolvers.size(), algorithm));
		return "Solver created ("+algorithm+") with ID "+(runningSolvers.size()-1);
	}	
	
	
	

	public static void configure(String config) throws Exception {
		configure(runningSolvers.size()-1, config);
	}
	
	public static void configure(int id1, int id2, String config) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningSolvers.size()-1) id2 = runningSolvers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i)<0 && status(i)>-3)
				configure(i, config);			
		}		
	}
	
	public static void configure(int id, String config) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Solver with this ID.");
		} else if(status(id) == -1) {
			clogger.warn(AUTH, "start", "Overwriting existing configuration.");		
		} else if(status(id) == 100) {
			throw new Exception("Solving finished, preparing Results.");	
		} else if(status(id) == 101) {
			throw new Exception("Solving finished, Results already available.");	
		} else if(status(id) == 102) {
			throw new Exception("Solving terminated.");	
		} else if(status(id) == 103) {
			throw new Exception("Solving terminated with failure.");		
		} else if(status(id) == 104) {
			throw new Exception("Solver not found (deleted).");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Solver already running.");						
		}

		runningSolvers.get(id).configure(config);
	}
	
	
	

	public static void start() throws Exception {
		start(runningSolvers.size()-1);
	}
	
	public static void start(int id1, int id2) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningSolvers.size()-1) id2 = runningSolvers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i)<0 && status(i)>-3)
				start(i);			
		}		
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
			throw new Exception("Solving terminated.");	
		} else if(status(id) == 103) {
			throw new Exception("Solving terminated with failure.");			
		} else if(status(id) == 104) {
			throw new Exception("Solver not found (deleted).");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Solver already running.");						
		}
		runningSolvers.get(id).start();
	}
	
	
	

	public static void terminate() throws Exception{
		terminate(runningSolvers.size()-1);
	}
	
	public static void terminate(int id1, int id2) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningSolvers.size()-1) id2 = runningSolvers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i)>=0 && status(i)<100)
				terminate(i);			
		}		
	}
	
	public static void terminate(int id) throws Exception{
		if(status(id) == -3) {
			throw new Exception("No Solver with this ID.");
		} else if(status(id) == -2) {
			throw new Exception("Solver not configured");	
		} else if(status(id) == -1) {
			throw new Exception("Solver not started");	
		} else if(status(id) == 100) {
			throw new Exception("Solving finished, preparing Results.");	
		} else if(status(id) == 101) {
			throw new Exception("Solving finished, Results already available.");	
		} else if(status(id) == 102) {
			throw new Exception("Solving already terminated. (id="+id+")");	
		} else if(status(id) == 103) {
			throw new Exception("Solving terminated with failure.");				
		} else if(status(id) == 104) {
			throw new Exception("Solver not found (deleted).");	
		}
		runningSolvers.get(id).terminate();
	}
	
	public static void terminateAll() {
		for(int i=0; i < runningSolvers.size(); i++) {
			try {
				terminate(i);
			} catch (Exception e) {
				
			}
		}
		clogger.info(AUTH, "terminateAll", "Terminated all running Solvers.");
	}	
	
	public static void joinAllThreads() {
		clogger.info(AUTH, "joinAllThreads", "Joining all solver threads.");
		
		for(int i=0; i < runningSolvers.size(); i++) {
			try {
				if(status(i) >= 0)
					runningSolvers.get(i).joinThread();
			} catch(Exception e) {
				clogger.err(AUTH, "joinAllThreads", e);
			}
		}	
		
		clogger.info(AUTH, "joinAllThreads", "Joined all solver threads.");	
	}
	
	
	

	public static double status() {
		return status(runningSolvers.size()-1);
	}
	
	public static double status(int id) {
		//-3		solver not created
		//-2 		solver initialized
		//-1		solver configured
		//[0,100] 	started, progress [%]
		//101 		ended, result ready
		//102 		terminated
		//103 		error
		//104 		deleted
		if(id >= runningSolvers.size() || id < 0) {
			return -3;			
		} else {
			try {
				return runningSolvers.get(id).getStatus();
			} catch(Exception e) {
				return 104;	//Solver Object not accessible anymore -> deleted		
			}
		}
	}
	
	
	

	public static SolverResult result() throws Exception {
		return result(runningSolvers.size()-1);
	}
	
	public static SolverResult result(int id) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Solver with this ID.");
		} else if(status(id) == -2) {
			throw new Exception("Solver not configured");	
		} else if(status(id) == -1) {
			throw new Exception("Solver not started");	
		} else if(status(id) == 100) {
			throw new Exception("Solving finished, preparing Results.");	
		} else if(status(id) == 102) {
			throw new Exception("Solving terminated.");	
		} else if(status(id) == 103) {
			clogger.warn(AUTH, "result", "Solving terminated with failure.");	
			return runningSolvers.get(id).getResult();
		} else if(status(id) == 104) {
			throw new Exception("Solver not found (deleted).");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Solver running.");						
		}
		return runningSolvers.get(id).getResult();
	}
	
	
	

	public static void delete() throws Exception {
		delete(runningSolvers.size()-1);
	}
	
	public static void delete(int id1, int id2) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningSolvers.size()-1) id2 = runningSolvers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i) == -3 || status(i) == 104 || (status(i) >= 0 && status(i) < 100)) continue;	
			delete(i);			
		}		
	}
	
	public static void delete(int id) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Solver with this ID.");	
		} else if(status(id) == 104) {
			throw new Exception("Solver already deleted.");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Solver is running. Try terminating.");						
		}
		runningSolvers.set(id, null);
	}
	
	public static void deleteAll() {	
		terminateAll();
		
		for(int i=0; i < runningSolvers.size(); i++) {
			try {
				delete(i);
			} catch (Exception e) {
				
			}
		}
		clogger.info(AUTH, "deleteAll", "Deleted all Solvers.");
	}
	
	
	
	
	
	
	
	public static String list() throws Exception {
		try {
			return list(true, true, "", -5, 105, 0, Integer.MAX_VALUE, false);
			
		}catch(Exception e) {
			clogger.ftl(AUTH, "list", "test");
		}
		return "";
	}
	
	public static String list(boolean show_running, boolean show_notrunning, String type, double status, double status_max, int id, int id_max, boolean asJson) throws Exception {
		if(!type.equals("") && !isValidAlgorithm(type)) throw new Exception("Searching for invalid Algorithm.");
		
		String list = "ID\tAlgorithm\t\tStatus\n\n";
		String json = "{\"solvers\": [";
		boolean json_comma_first = true;
		
		for(int i=0; i<runningSolvers.size(); i++) {
			//filter
			try {
				if(show_running && !show_notrunning && (runningSolvers.get(i).getStatus() < 0 || runningSolvers.get(i).getStatus() > 100)) continue;
				if(show_notrunning && !show_running && (runningSolvers.get(i).getStatus() >= 0 && runningSolvers.get(i).getStatus() <= 100)) continue;
				if(!type.equals("") && !type.equals(runningSolvers.get(i).getAlgorithm())) continue;
				if(runningSolvers.get(i).getStatus() < status || runningSolvers.get(i).getStatus() > status_max) continue;
				if(i < id || i > id_max) continue;
							
			} catch(Exception e) {
				if(!show_notrunning ||
				   status_max < 104 ||
				   !type.equals("")) continue;
			}
				
			//write entry
			if(!asJson) {
				list += i + "\t";
				try {
					list += runningSolvers.get(i).getAlgorithm() + "\t\t\t";
					list += round(runningSolvers.get(i).getStatus(),3);	
				} catch(Exception e) {
					list += "--------- deleted ---------";
				}
				list += "\n";
			} else {
				if(!json_comma_first) {
					json += ",";	
				} else {
					json_comma_first = false;
				}
				json += "{\"id\": "+i+",";
				try {
					json += "\"algorithm\": \""+runningSolvers.get(i).getAlgorithm()+"\","; 
					json += "\"status\": "+runningSolvers.get(i).getStatus(); 					
				} catch(Exception e) {
					json += "\"deleted\": true";					
				}
				json += "}";			
			}
		}
		
		if(!asJson) {
			return list;
		} else {
			json += "]}";
			return json;
		}
	}
	
	private static boolean isValidAlgorithm(String algorithm) {
		try {
			SolverConfig.getDefault(algorithm);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	
	
	
	
	
	

	
	
	public static boolean checkTerminated(int id){
		try {
			if(status(id) == -3) {
				throw new Exception("No Solver with this ID.");
			} else if(status(id) == -2) {
				throw new Exception("Solver not configured");	
			} else if(status(id) == -1) {
				throw new Exception("Solver not started");	
			} else if(status(id) == 100) {
				throw new Exception("Solving finished, preparing Results.");	
			} else if(status(id) == 101) {
				throw new Exception("Solving finished, Results available.");
			} else if(status(id) == 103) {
				throw new Exception("Solving terminated with failure.");				
			} else if(status(id) == 104) {
				throw new Exception("Solver not found (deleted).");						
			} else
				return runningSolvers.get(id).getTerminated();
		} catch(Exception e) {
			clogger.err(AUTH, "checkTerminated", e);
			return true;
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
	
	

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
