package swe_mo.optimizer;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



public class OptimizerConfig {
	
	public int ffid;
	public String solvertype;
	
	
	//arrays for solver hyperparameters
	public ArrayList<String> SHP_name = new ArrayList<String>();
	public ArrayList<Double> SHP_min = new ArrayList<Double>();
	public ArrayList<Double> SHP_max = new ArrayList<Double>();
	
	
	//list of used parameters
	public ArrayList<String> usedpars = new ArrayList<String>();
	
	
	
	
	
	//basic
	public OptimizerConfig(int ffid, String solvertype) {
		this.ffid = ffid;	
		this.solvertype = solvertype;		

		usedpars.add("ffid");
		usedpars.add("solvertype");
		usedpars.add("SHP"); //solver hyperparameters
	}
	
	
	
	public void set(String param, String value) throws Exception {		
		switch(param) {
			case "ffid":
				ffid = Integer.parseInt(value);
				return;
			case "solvertype":
				solvertype = value;
				return;
		}
		throw new Exception("No such hyperparameter ("+param+").");
	}

	public void set(String param, String min, String max) throws Exception {
		if(SHP_name.contains(param)) {
			int id = SHP_name.indexOf(param);
			SHP_min.set(id, Double.parseDouble(min));
			SHP_max.set(id, Double.parseDouble(max));
		} else {
			throw new Exception("No such solver hyperparameter ("+param+").");			
		}
	}

	public void addSHP(String param, String min, String max) throws Exception {
		if(SHP_name.contains(param)) {
			int id = SHP_name.indexOf(param);
			SHP_min.set(id, Double.parseDouble(min));
			SHP_max.set(id, Double.parseDouble(max));
		} else {
			SHP_name.add(param);	
			SHP_min.add(Double.parseDouble(min));
			SHP_max.add(Double.parseDouble(max));		
		}
	}

	public void removeSHP(String param) throws Exception {
		if(SHP_name.contains(param)) {
			int id = SHP_name.indexOf(param);
			SHP_name.remove(id);
			SHP_min.remove(id);
			SHP_max.remove(id);
		}
	}

	
	public Object getValue(String param) {
		switch(param) {
			case "ffid": 
				return ffid;
			case "solvertype": 
				return solvertype;
		}
		
		if(SHP_name.contains(param)) {
			int id = SHP_name.indexOf(param);
			return SHP_min.get(id)+"/"+SHP_max.get(id);
		}
		
		return "(not defined)";
	}
	
	
	
	
	
	public String SHPgetString() {
		String s = "";
		
		for(int i=0; i < SHP_name.size(); i++) {
			s += SHP_name.get(i)+"="+SHP_min.get(i)+"/"+SHP_max.get(i)+",";
		}
		
		return s;
	}
	
	

	
	
	@Override
	public String toString() {
		String s = "Configuration\n";
		
		for(String p : usedpars) {
			if(p.equals("SHP")) {
				for(String q : SHP_name) {
					s += "\n"+q+" = "+getValue(q);
				}
			} else {
				s += "\n"+p+" = "+getValue(p);				
			}
		}
		
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public String toJSON() {
		JSONArray jarr = new JSONArray();
		
		for(String p : usedpars) {
			JSONObject json = new JSONObject();
			
			if(p.equals("SHP")) {
				for(String q : SHP_name) {
					json = new JSONObject();	
					
					json.put("key", q);
					Object val = getValue(q);
					
					if(val.getClass().equals(String.class) && ((String) val).contains("/")) {
						String[] s = ((String) val).split("/");
						json.put("min_value", s[0]);
						json.put("max_value", s[1]);						
					} else {
						json.put("value", val);
					}
					
					jarr.add(json);				
					
				}
			} else {				
				json.put("key", p);
				Object val = getValue(p);
				
				if(val.getClass().equals(String.class) && ((String) val).contains("/")) {
					String[] s = ((String) val).split("/");
					json.put("min_value", s[0]);
					json.put("max_value", s[1]);						
				} else {
					json.put("value", val);
				}
				
				jarr.add(json);				
			}
		}		

		JSONObject json = new JSONObject();
		json.put("cfg", jarr);
		
		return json.toJSONString();
	}
	
	
	
	
	
	public static OptimizerConfig getDefault(String algorithm) throws Exception {
		switch(algorithm) {
			case "DeepRand":
				return new OptimizerConfig(8, "DEbest1");
				//return DeepRandomSearch.defaultConfig();
				
		}	
		throw new Exception("Algorithm not specified.");
	}
	
	public static OptimizerResult solveMethod(String algorithm, int id, OptimizerConfig config) throws Exception {

		switch(algorithm) {
			case "DeepRand":
				for(double i=0; i<Math.pow(10, 9); i++) {
					if(i%2==0) 
						OptimizerManager.updateStatus(id, i/Math.pow(10, 9)*100);
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
					Object val = defaultConfig.getValue(u);
					
					if(val.getClass().equals(String.class) && ((String) val).contains("/")) {
						String[] s = ((String) val).split("/");
						jsonobj.put("min_default", s[0]);
						jsonobj.put("max_default", s[1]);						
					} else {
						jsonobj.put("default", val);
					}
					
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
