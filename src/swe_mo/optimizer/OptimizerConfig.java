package swe_mo.optimizer;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import swe_mo.ui.clogger;



public class OptimizerConfig {

	public int ffid;
	
	
	//list of used parameters
	public ArrayList<String> usedpars = new ArrayList<String>();
	
	
	

	public OptimizerConfig() {};
	
	//for cloning
	public OptimizerConfig(OptimizerConfig s) {
		this.ffid = s.ffid;
	};
	
	
	//basic
	public OptimizerConfig(int ffid) {
		this.ffid = ffid;	
		
		usedpars.add("ffid");
	}
	
	
	
	public void set(String param, String value) throws Exception {
		clogger.dbg("OC", "set", param+" = "+value);
		
		switch(param) {
			case "ffid":
				ffid = Integer.parseInt(value);
				return;
		}
		throw new Exception("No such hyperparameter ("+param+").");
	}

	public void set(String param, String min, String max) throws Exception {
		clogger.dbg("OC", "set", param+" = "+min+" / "+max);
		
		switch(param) {
			case "ffid":
				ffid = Integer.parseInt(min);
				return;
		}
		throw new Exception("No such hyperparameter ("+param+").");
	}

	
	public Object getValue(String param) {
		switch(param) {
			case "ffid": 
				return ffid;
		}
		return "nd";
	}
	
	

	
	
	@Override
	public String toString() {
		String s = "Configuration\n";
		
		for(String p : usedpars) {
			s += "\n"+p+" = "+getValue(p);
		}
		
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public String toJSON() {
		JSONArray jarr = new JSONArray();
		
		for(String p : usedpars) {
			JSONObject json = new JSONObject();
			json.put("key", p);
			json.put("value", getValue(p));
			jarr.add(json);
		}
		

		JSONObject json = new JSONObject();
		json.put("cfg", jarr);
		
		return json.toJSONString();
	}
	
	
	
	
	
	public static OptimizerConfig getDefault(String algorithm) throws Exception {
		switch(algorithm) {
			case "DeepRand":
				return new OptimizerConfig(8);
				//return DeepRandomSearch.defaultConfig();
				
		}	
		throw new Exception("Algorithm not specified.");
	}
	
	public static OptimizerResult solveMethod(String algorithm, int id, OptimizerConfig config) throws Exception {

		switch(algorithm) {
			case "DeepRand":
				for(double i=0; i<Math.pow(10, 10); i++) {
					OptimizerManager.updateStatus(id, i/Math.pow(10, 10)*100);
					if(OptimizerManager.checkTerminated(id))
						break;
				};
				return new OptimizerResult();	
				//return new DeepRandomSearch(config).solve();
		}
		throw new Exception("Algorithm not specified or no optimizer method.");
	}
	
	@SuppressWarnings("unchecked")
	public static String getAlgorithmList(boolean json, boolean pars, String algo) throws Exception {
		ArrayList<String> algorithms = new ArrayList<String>();
		
		algorithms.add("DeepRand");
		
		
		if(!pars) {
			if(!json) {
				String l = "List of implemented algorithms:\n";
				
				for(String a : algorithms) {
					try {
						l += "\n"+a;
					} catch (Exception e) {
						l += "\n"+a;
					}
				}
				
				return l;
			} else {
				JSONArray algoarray = new JSONArray();
				for(int i=0; i < algorithms.size(); i++) {					
					algoarray.add((JSONObject) new JSONParser().parse(getAlgorithmList(true,true,algorithms.get(i))));
				}
				
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("algorithms", algoarray);
				return jsonobj.toJSONString();
			}
			
		} else {
			OptimizerConfig defaultConfig = getDefault(algo);
			ArrayList<String> up = defaultConfig.usedpars;
			
			if(!json) {
				String p = "Parameters for "+algo+"\n";
				
				for(String u : up) {
					p += "\n"+u+" \t(default="+defaultConfig.getValue(u)+")";
				}
				
				return p;
			} else {
				JSONArray jsonarr = new JSONArray();
				
				for(String u : up) {
					JSONObject jsonobj = new JSONObject();
					jsonobj.put("name", u);
					jsonobj.put("default", defaultConfig.getValue(u));
					
					jsonarr.add(jsonobj);
				}		
				
				
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("algorithm", algo);
				jsonobj.put("parameters", jsonarr);
				return jsonobj.toJSONString();				
			}
		}
	}
}
