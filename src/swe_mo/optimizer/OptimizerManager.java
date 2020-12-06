package swe_mo.optimizer;

import swe_mo.ui.clogger;
import swe_mo.solver.SolverManager;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class OptimizerManager {

	final static String AUTH = "OM";
	
	private static ArrayList<Optimizer> runningOptimizers = new ArrayList<Optimizer>();																						
	
	

	
	public static String create(String creator) throws Exception{
		runningOptimizers.add(new Optimizer(runningOptimizers.size(), creator));	
		return "Optimizer created (default) with ID "+(runningOptimizers.size()-1);
	}

	public static String create(String creator, String algorithm) throws Exception {
		runningOptimizers.add(new Optimizer(runningOptimizers.size(), creator, algorithm));
		return "Optimizer created ("+algorithm+") with ID "+(runningOptimizers.size()-1);
	}																						
	
	

	
	public static String cloneOptimizer(String _auth) throws Exception {
		if(runningOptimizers.size()==0)
			throw new Exception("No Optimizer created yet.");
		
		return cloneOptimizer(_auth, runningOptimizers.size()-1);		
	}
	
	public static String cloneOptimizer(String _auth, int cloneId) throws Exception {
		if(status(cloneId)<=-3 || status(cloneId)>=104) 
			throw new Exception("Optimizer with clone id not found.");
		
		create(_auth,runningOptimizers.get(cloneId).getAlgorithm());
		cloneConfig(cloneId);		
		
		return "Optimizer "+cloneId+" cloned";
	}																						
	


	public static void configure(String config) throws Exception {
		configure(runningOptimizers.size()-1, config);
	}
	
	public static void configure(int id1, int id2, String config) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningOptimizers.size()-1) id2 = runningOptimizers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i)<0 && status(i)>-3)
				configure(i, config);			
		}		
	}
	
	public static void configure(int id, String config) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Optimizer with this ID.");
		} else if(status(id) == -1) {
			clogger.warn(AUTH, "start", "Overwriting existing configuration.");		
		} else if(status(id) == 100) {
			throw new Exception("Optimizing finished, preparing Results.");	
		} else if(status(id) == 101) {
			throw new Exception("Optimizing finished, Results already available.");	
		} else if(status(id) == 102) {
			throw new Exception("Optimizing terminated.");	
		} else if(status(id) == 103) {
			throw new Exception("Optimizing terminated with failure.");		
		} else if(status(id) == 104) {
			throw new Exception("Optimizer not found (deleted).");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Optimizer already running.");						
		}

		runningOptimizers.get(id).configure(config);
	}																						
	
	

	
	public static String getConfig(boolean json) throws Exception {
		return getConfig(runningOptimizers.size()-1, json);
	}
	
	public static String getConfig(int id, boolean json) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Optimizer with this ID.");		
		} else if(status(id) == 104) {
			throw new Exception("Optimizer not found (deleted).");					
		}

		return runningOptimizers.get(id).getConfig(json);
	}																						
	
	

	
	public static void resetConfig() throws Exception {
		resetConfig(runningOptimizers.size()-1);
	}
	
	public static void resetConfig(int id1, int id2) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningOptimizers.size()-1) id2 = runningOptimizers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i)<0 && status(i)>-3)
				resetConfig(i);			
		}		
	}
	
	public static void resetConfig(int id) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Optimizer with this ID.");
		} else if(status(id) == 100) {
			throw new Exception("Optimizing finished, preparing Results.");	
		} else if(status(id) == 101) {
			throw new Exception("Optimizing finished, Results already available.");	
		} else if(status(id) == 102) {
			throw new Exception("Optimizing terminated.");	
		} else if(status(id) == 103) {
			throw new Exception("Optimizing terminated with failure.");		
		} else if(status(id) == 104) {
			throw new Exception("Optimizer not found (deleted).");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Optimizer already running.");						
		}

		runningOptimizers.get(id).resetConfig();
	}																						
	
	

	
	public static void cloneConfig(int cloneId) throws Exception {
		cloneConfig(runningOptimizers.size()-1, cloneId);
	}
	
	public static void cloneConfig(int id1, int id2, int cloneId) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningOptimizers.size()-1) id2 = runningOptimizers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i)<0 && status(i)>-3)
				cloneConfig(i, cloneId);	
		}		
	}
	
	public static void cloneConfig(int id, int cloneId) throws Exception {
		if(status(cloneId)<=-3 || status(cloneId)>=104) 
			throw new Exception("Optimizer with clone id not found.");
				
		if(status(id) == -3) {
			throw new Exception("No Optimizer with this ID.");
		} else if(status(id) == 100) {
			throw new Exception("Optimizing finished, preparing Results.");	
		} else if(status(id) == 101) {
			throw new Exception("Optimizing finished, Results already available.");	
		} else if(status(id) == 102) {
			throw new Exception("Optimizing terminated.");	
		} else if(status(id) == 103) {
			throw new Exception("Optimizing terminated with failure.");		
		} else if(status(id) == 104) {
			throw new Exception("Optimizer not found (deleted).");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Optimizer already running.");						
		}
		
		ArrayList<String> usedpars = runningOptimizers.get(id).getConfig().usedpars;
		ArrayList<String> usedparsClone = runningOptimizers.get(cloneId).getConfig().usedpars;
		
		for(int i=0; i < usedpars.size(); i++) {
			if(usedparsClone.contains(usedpars.get(i))) {
				runningOptimizers.get(id).configure(usedpars.get(i)+"="+runningOptimizers.get(cloneId).getConfig().getValue(usedpars.get(i)));
			}
		}

		if(status(cloneId) > -2)
			runningOptimizers.get(id).updateStatus(-1);
	}																						
	
	

	
	public static void start() throws Exception {
		start(runningOptimizers.size()-1);
	}
	
	public static void start(int id1, int id2) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningOptimizers.size()-1) id2 = runningOptimizers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i)<0 && status(i)>-3)
				start(i);			
		}		
	}
	
	public static void start(int id) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Optimizer with this ID.");
		} else if(status(id) == -2) {
			clogger.warn(AUTH, "start", "Optimizer not configured, using default values.");		
		} else if(status(id) == 100) {
			throw new Exception("Optimizing finished, preparing Results.");	
		} else if(status(id) == 101) {
			throw new Exception("Optimizing finished, Results already available.");	
		} else if(status(id) == 102) {
			throw new Exception("Optimizing terminated.");	
		} else if(status(id) == 103) {
			throw new Exception("Optimizing terminated with failure.");			
		} else if(status(id) == 104) {
			throw new Exception("Optimizer not found (deleted).");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Optimizer already running.");						
		}
		runningOptimizers.get(id).start();
	}																						
	
	

	
	public static void terminate() throws Exception{
		terminate(runningOptimizers.size()-1);
	}
	
	public static void terminate(int id1, int id2) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningOptimizers.size()-1) id2 = runningOptimizers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i)>=0 && status(i)<100)
				terminate(i);			
		}		
	}
	
	public static void terminate(int id) throws Exception{
		if(status(id) == -3) {
			throw new Exception("No Optimizer with this ID.");
		} else if(status(id) == -2) {
			throw new Exception("Optimizer not configured");	
		} else if(status(id) == -1) {
			throw new Exception("Optimizer not started");	
		} else if(status(id) == 100) {
			throw new Exception("Optimizing finished, preparing Results.");	
		} else if(status(id) == 101) {
			throw new Exception("Optimizing finished, Results already available.");	
		} else if(status(id) == 102) {
			throw new Exception("Optimizing already terminated. (id="+id+")");	
		} else if(status(id) == 103) {
			throw new Exception("Optimizing terminated with failure.");				
		} else if(status(id) == 104) {
			throw new Exception("Optimizer not found (deleted).");	
		}
		runningOptimizers.get(id).terminate();
	}
	
	public static void terminateAll() {
		for(int i=0; i < runningOptimizers.size(); i++) {
			try {
				terminate(i);
			} catch (Exception e) {
				
			}
		}
		clogger.info(AUTH, "terminateAll", "Terminated all running Optimizers.");
	}	
	
	public static void joinAllThreads() {
		clogger.info(AUTH, "joinAllThreads", "Joining all optimizer threads.");
		
		for(int i=0; i < runningOptimizers.size(); i++) {
			try {
				if(status(i) >= 0 && status(i) < 104)
					runningOptimizers.get(i).joinThread();
			} catch(Exception e) {
				clogger.err(AUTH, "joinAllThreads", e);
			}
		}	
		
		clogger.info(AUTH, "joinAllThreads", "Joined all optimizer threads.");	
	}																						
	
	

	
	public static double status() {
		return status(runningOptimizers.size()-1);
	}
	
	public static double status(int id) {
		//-3		optimizer not created
		//-2 		optimizer initialized
		//-1		optimizer configured
		//[0,100] 	started, progress [%]
		//101 		ended, result ready
		//102 		terminated
		//103 		error
		//104 		deleted
		if(id >= runningOptimizers.size() || id < 0) {
			return -3;			
		} else {
			try {
				return runningOptimizers.get(id).getStatus();
			} catch(Exception e) {
				return 104;	//Optimizer Object not accessible anymore -> deleted		
			}
		}
	}																						
	
	

	
	public static OptimizerResult result() throws Exception {
		return result(runningOptimizers.size()-1);
	}
	
	public static OptimizerResult result(int id) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Optimizer with this ID.");
		} else if(status(id) == -2) {
			throw new Exception("Optimizer not configured");	
		} else if(status(id) == -1) {
			throw new Exception("Optimizer not started");	
		} else if(status(id) == 100) {
			throw new Exception("Optimizing finished, preparing Results.");	
		} else if(status(id) == 102) {
			throw new Exception("Optimizing terminated.");	
		} else if(status(id) == 103) {
			clogger.warn(AUTH, "result", "Optimizing terminated with failure.");	
			return runningOptimizers.get(id).getResult();
		} else if(status(id) == 104) {
			throw new Exception("Optimizer not found (deleted).");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Optimizer running.");						
		}
		return runningOptimizers.get(id).getResult();
	}																						
	
	

	
	public static void clear() throws Exception {
		clear(runningOptimizers.size()-1);
	}
	
	public static void clear(int id1, int id2) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningOptimizers.size()-1) id2 = runningOptimizers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i) == -3 || status(i) == 104 || (status(i) >= 0 && status(i) < 100)) continue;	
			clear(i);			
		}		
	}
	
	public static void clear(int id) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Optimizer with this ID.");	
		} else if(status(id) == 104) {
			throw new Exception("Optimizer is deleted.");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Optimizer is running. Try terminating.");						
		}
		runningOptimizers.get(id).clear();
	}
	
	public static void clearAll() {	
		for(int i=0; i < runningOptimizers.size(); i++) {
			try {
				clear(i);
			} catch (Exception e) {
				
			}
		}
		clogger.info(AUTH, "clearAll", "Cleared all optimizers (if possible).");
	}																									
	
	

	
	
	
	
	
	
	
	
	
	public static void delete() throws Exception {
		delete(runningOptimizers.size()-1);
	}
	
	public static void delete(int id1, int id2) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningOptimizers.size()-1) id2 = runningOptimizers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i) == -3 || status(i) == 104 || (status(i) >= 0 && status(i) < 100)) continue;	
			delete(i);			
		}		
	}
	
	public static void delete(int id) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Optimizer with this ID.");	
		} else if(status(id) == 104) {
			throw new Exception("Optimizer already deleted.");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Optimizer is running. Try terminating.");						
		}
		runningOptimizers.set(id, null);
	}
	
	public static void deleteAll() {	
		terminateAll();
		
		for(int i=0; i < runningOptimizers.size(); i++) {
			try {
				delete(i);
			} catch (Exception e) {
				
			}
		}
		clogger.info(AUTH, "deleteAll", "Deleted all optimizers.");
	}																						
	
	

	
	public static String list() throws Exception {
		try {
			return list(true, true, "", "", -5, 105, 0, Integer.MAX_VALUE, false);
			
		}catch(Exception e) {
			clogger.ftl(AUTH, "list", "test");
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public static String list(boolean show_running, boolean show_notrunning, String type, String creator, double status, double status_max, int id, int id_max, boolean asJson) throws Exception {
		if(!type.equals("") && !isValidAlgorithm(type)) throw new Exception("Searching for invalid Algorithm.");
		
		String list = "ID\tAlgorithm\tby\t\tStatus\n\n";
		JSONArray jsonarr = new JSONArray();	
		
		for(int i=0; i<runningOptimizers.size(); i++) {
			//filter
			try {
				if(show_running && !show_notrunning && (runningOptimizers.get(i).getStatus() < 0 || runningOptimizers.get(i).getStatus() > 100)) continue;
				if(show_notrunning && !show_running && (runningOptimizers.get(i).getStatus() >= 0 && runningOptimizers.get(i).getStatus() <= 100)) continue;
				if(!type.equals("") && !type.equals(runningOptimizers.get(i).getAlgorithm())) continue;
				if(!creator.equals("") && !creator.equals(runningOptimizers.get(i).getCreator())) continue;
				if(runningOptimizers.get(i).getStatus() < status || runningOptimizers.get(i).getStatus() > status_max) continue;
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
					list += runningOptimizers.get(i).getAlgorithm() + "\t";
					list += runningOptimizers.get(i).getCreator() + "\t\t";
					list += SolverManager.round(runningOptimizers.get(i).getStatus(),3);	
				} catch(Exception e) {
					list += "--------- deleted ---------";
				}
				list += "\n";
			} else {
					JSONObject jsonobj = new JSONObject();
					jsonobj.put("id", i);
				try {		
					jsonobj.put("algorithm", runningOptimizers.get(i).getAlgorithm());
					jsonobj.put("status", runningOptimizers.get(i).getStatus());
					jsonobj.put("creator", runningOptimizers.get(i).getCreator());
					jsonobj.put("deleted", false);
				} catch(Exception e) {
					jsonobj.put("deleted", true);				
				}
				jsonarr.add(jsonobj);
			}
		}
		
		if(!asJson) {
			return list;
		} else {
			JSONObject listjson = new JSONObject();
			listjson.put("optimizers", jsonarr);
			return listjson.toJSONString();
		}
	}
	
	
	
	
	
	
	

	
	
	public static boolean checkTerminated(int id){
		try {
			if(status(id) == -3) {
				throw new Exception("No Optimizer with this ID.");
			} else if(status(id) == -2) {
				throw new Exception("Optimizer not configured");	
			} else if(status(id) == -1) {
				throw new Exception("Optimizer not started");	
			} else if(status(id) == 100) {
				throw new Exception("Optimizing finished, preparing Results.");	
			} else if(status(id) == 101) {
				throw new Exception("Optimizing finished, Results available.");
			} else if(status(id) == 103) {
				throw new Exception("Optimizing terminated with failure.");				
			} else if(status(id) == 104) {
				throw new Exception("Optimizer not found (deleted).");						
			} else
				return runningOptimizers.get(id).getTerminated();
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
			runningOptimizers.get(id).updateStatus(status);
	}
	
	

	
	private static boolean isValidAlgorithm(String algorithm) {
		try {
			OptimizerConfig.getDefault(algorithm);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

}
