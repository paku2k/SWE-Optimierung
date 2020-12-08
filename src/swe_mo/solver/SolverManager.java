package swe_mo.solver;

import swe_mo.ui.clogger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;




public class SolverManager {
	final static String AUTH = "SM";
	
	private static ArrayList<Solver> runningSolvers = new ArrayList<Solver>();																						
	
	

	
	public static int create(String creator) throws Exception{
		runningSolvers.add(new Solver(runningSolvers.size(), creator));	
		return runningSolvers.size()-1;
	}

	public static int create(String creator, String algorithm) throws Exception {
		runningSolvers.add(new Solver(runningSolvers.size(), creator, algorithm));
		return runningSolvers.size()-1;
	}																						
	
	

	
	public static String cloneSolver(String _auth) throws Exception {
		if(runningSolvers.size()==0)
			throw new Exception("No Solver created yet.");
		
		return cloneSolver(_auth, runningSolvers.size()-1);		
	}
	
	public static String cloneSolver(String _auth, int cloneId) throws Exception {
		if(status(cloneId)<=-3 || status(cloneId)>=104) 
			throw new Exception("Solver with clone id not found.");
		
		create(_auth,runningSolvers.get(cloneId).getAlgorithm());
		cloneConfig(cloneId);		
		
		return "Solver "+cloneId+" cloned";
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
			if(runningSolvers.get(id).getCreator().equals("UiF"))
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
	
	

	
	public static String getConfig(boolean json) throws Exception {
		return getConfig(runningSolvers.size()-1, json);
	}
	
	public static String getConfig(int id, boolean json) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Solver with this ID.");		
		} else if(status(id) == 104) {
			throw new Exception("Solver not found (deleted).");					
		}

		return runningSolvers.get(id).getConfig(json);
	}																						
	
	

	
	public static void resetConfig() throws Exception {
		resetConfig(runningSolvers.size()-1);
	}
	
	public static void resetConfig(int id1, int id2) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningSolvers.size()-1) id2 = runningSolvers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i)<0 && status(i)>-3)
				resetConfig(i);			
		}		
	}
	
	public static void resetConfig(int id) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Solver with this ID.");
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

		runningSolvers.get(id).resetConfig();
	}																						
	
	

	
	public static void cloneConfig(int cloneId) throws Exception {
		cloneConfig(runningSolvers.size()-1, cloneId);
	}
	
	public static void cloneConfig(int id1, int id2, int cloneId) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningSolvers.size()-1) id2 = runningSolvers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i)<0 && status(i)>-3)
				cloneConfig(i, cloneId);	
		}		
	}
	
	public static void cloneConfig(int id, int cloneId) throws Exception {
		if(status(cloneId)<=-3 || status(cloneId)>=104) 
			throw new Exception("Solver with clone id not found.");
				
		if(status(id) == -3) {
			throw new Exception("No Solver with this ID.");
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
		
		ArrayList<String> usedpars = runningSolvers.get(id).getConfig().usedpars;
		ArrayList<String> usedparsClone = runningSolvers.get(cloneId).getConfig().usedpars;
		
		for(int i=0; i < usedpars.size(); i++) {
			if(usedparsClone.contains(usedpars.get(i))) {
				runningSolvers.get(id).configure(usedpars.get(i)+"="+runningSolvers.get(cloneId).getConfig().getValue(usedpars.get(i)));
			}
		}

		if(status(cloneId) > -2)
			runningSolvers.get(id).updateStatus(-1);
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
			if(runningSolvers.get(id).getCreator().equals("UiF"))
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
				if(status(i) >= 0 && status(i) < 104)
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
	
	

	
	public static void clear() throws Exception {
		clear(runningSolvers.size()-1);
	}
	
	public static void clear(int id1, int id2) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		if(id2>runningSolvers.size()-1) id2 = runningSolvers.size()-1;
		
		for(int i=id1; i<=id2; i++) {
			if(status(i) == -3 || status(i) == 104 || (status(i) >= 0 && status(i) < 100)) continue;	
			clear(i);			
		}		
	}
	
	public static void clear(int id) throws Exception {
		if(status(id) == -3) {
			throw new Exception("No Solver with this ID.");	
		} else if(status(id) == 104) {
			throw new Exception("Solver is deleted.");			
		} else if(status(id) >= 0 && status(id) < 100) {
			throw new Exception("Solver is running. Try terminating.");						
		}
		runningSolvers.get(id).clear();
	}
	
	public static void clearAll() {	
		for(int i=0; i < runningSolvers.size(); i++) {
			try {
				clear(i);
			} catch (Exception e) {
				
			}
		}
		clogger.info(AUTH, "clearAll", "Cleared all solvers (if possible).");
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
		clogger.info(AUTH, "deleteAll", "Deleted all solvers.");
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
		
		for(int i=0; i<runningSolvers.size(); i++) {
			//filter
			try {
				if(show_running && !show_notrunning && (runningSolvers.get(i).getStatus() < 0 || runningSolvers.get(i).getStatus() > 100)) continue;
				if(show_notrunning && !show_running && (runningSolvers.get(i).getStatus() >= 0 && runningSolvers.get(i).getStatus() <= 100)) continue;
				if(!type.equals("") && !type.equals(runningSolvers.get(i).getAlgorithm())) continue;
				if(!creator.equals("") && !creator.equals(runningSolvers.get(i).getCreator())) continue;
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
					list += runningSolvers.get(i).getAlgorithm() + "\t\t";
					list += runningSolvers.get(i).getCreator() + "\t\t";
					list += round(runningSolvers.get(i).getStatus(),3);	
				} catch(Exception e) {
					list += "--------- deleted ---------";
				}
				list += "\n";
			} else {
					JSONObject jsonobj = new JSONObject();
					jsonobj.put("id", i);
				try {		
					jsonobj.put("algorithm", runningSolvers.get(i).getAlgorithm());
					jsonobj.put("status", runningSolvers.get(i).getStatus());
					jsonobj.put("creator", runningSolvers.get(i).getCreator());
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
			listjson.put("solvers", jsonarr);
			return listjson.toJSONString();
		}
	}
	
	
	
	
	
	
	

	
	
	public static boolean checkTerminated(int id){
		try {
			if(status(id) == -3) {
				return false; //allows debugging solvers directly
				//throw new Exception("No Solver with this ID.");
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
	
	

	
	private static boolean isValidAlgorithm(String algorithm) {
		try {
			SolverConfig.getDefault(algorithm);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
