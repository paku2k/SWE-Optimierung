package swe_mo.optimizer;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import swe_mo.solver.FitnessFunction;
import swe_mo.optimizer.algorithms.DeepRandomSearch;
import swe_mo.optimizer.algorithms.RandomSearch;


public class OptimizerConfig {

	public int ffid;

	public int levels;
	public int levelGuesses;
	public int guesses;
	
	public String solvertype;
	public int N;
	public int maxGenerations;
	public double lowerBound;
	public double upperBound;
		
	//arrays for solver hyperparameters
	public ArrayList<String> SHP_name = new ArrayList<String>();
	public ArrayList<Double> SHP_min = new ArrayList<Double>();
	public ArrayList<Double> SHP_max = new ArrayList<Double>();
	
	public boolean printfile;
	
	//list of used parameters
	public ArrayList<String> usedpars = new ArrayList<String>();
	
	
	
	
	
	//DeepRandomSearch
	public OptimizerConfig(int ffid, int levels, int levelGuesses, String solvertype, int N, int maxGenerations, double lowerBound, double upperBound, boolean printfile) {
		this.ffid = ffid;	
		this.levels = levels;
		this.levelGuesses = levelGuesses;
		this.solvertype = solvertype;	
		this.N = N;
		this.maxGenerations = maxGenerations;
		this.lowerBound = lowerBound;		
		this.upperBound = upperBound;		
		
		this.printfile = printfile;

		usedpars.add("ffid");
		usedpars.add("levels");
		usedpars.add("levelGuesses");
		usedpars.add("solvertype");
		usedpars.add("N");
		usedpars.add("maxGenerations");
		usedpars.add("lowerBound");
		usedpars.add("upperBound");
		usedpars.add("printfile");
		usedpars.add("SHP"); //solver hyperparameters
	}
	
	//RandomSearch
	public OptimizerConfig(int ffid, int guesses, String solvertype, int N, int maxGenerations, double lowerBound, double upperBound, boolean printfile) {
		this.ffid = ffid;	
		this.guesses = guesses;
		this.solvertype = solvertype;	
		this.N = N;
		this.maxGenerations = maxGenerations;
		this.lowerBound = lowerBound;		
		this.upperBound = upperBound;		
		
		this.printfile = printfile;

		usedpars.add("ffid");
		usedpars.add("guesses");
		usedpars.add("solvertype");
		usedpars.add("N");
		usedpars.add("maxGenerations");
		usedpars.add("lowerBound");
		usedpars.add("upperBound");
		usedpars.add("printfile");
		usedpars.add("SHP"); //solver hyperparameters
	}
	
	public void set(String param, String value) throws Exception {		
		switch(param) {
			case "ffid":
				ffid = (int)Double.parseDouble(value);
				Double bd = FitnessFunction.getBoundary("lower", ffid);
				if(bd != null) {
					lowerBound = bd;
					upperBound = FitnessFunction.getBoundary("upper", ffid);					
				}
				return;
			case "levels":
				levels = (int)Double.parseDouble(value);
				return;
			case "levelGuesses":
				levelGuesses = (int)Double.parseDouble(value);
				return;
			case "solvertype":
				solvertype = value;
				return;
			case "N":
				N = (int)Double.parseDouble(value);
				return;
			case "maxGenerations":
				maxGenerations = (int)Double.parseDouble(value);
				return;
			case "lowerBound":
				lowerBound = Double.parseDouble(value);
				return;
			case "upperBound":
				upperBound = Double.parseDouble(value);
				return;
			case "printfile":
				printfile = Boolean.parseBoolean(value);
				return;
			case "guesses":
				guesses = (int)Double.parseDouble(value);
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
			case "levels": 
				return levels;				
			case "levelGuesses": 
				return levelGuesses;						
			case "solvertype": 
				return solvertype;
			case "N":
				return N;
			case "maxGenerations":
				return maxGenerations;
			case "lowerBound": 
				return lowerBound;
			case "upperBound": 
				return upperBound;	
			case "printfile": 
				return printfile;	
			case "guesses": 
				return guesses;	
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
				return DeepRandomSearch.defaultConfig();
			case "Rand":
				return RandomSearch.defaultConfig();
				
		}	
		throw new Exception("Algorithm not specified.");
	}
	
	public static OptimizerResult optimizeMethod(String algorithm, int id, OptimizerConfig config) throws Exception {

		switch(algorithm) {
			case "DeepRand":
				/*for(double i=0; i<Math.pow(10, 9) && !OptimizerManager.checkTerminated(id); i++) {
					if(i%100==0) 
						OptimizerManager.updateStatus(id, i/Math.pow(10, 9)*100);
				};
				return new OptimizerResult("F=0.1212, CR=4.5");*/
				return new DeepRandomSearch(config.ffid, config.solvertype, config.N, config.maxGenerations, config.lowerBound, config.upperBound, config.SHP_min, config.SHP_max, config.SHP_name, config.levels, config.levelGuesses, config.printfile, id).optimize();
			case "Rand":
				return new RandomSearch(config.ffid, config.solvertype, config.N, config.maxGenerations, config.lowerBound, config.upperBound, config.SHP_min, config.SHP_max, config.SHP_name, config.guesses, config.printfile, id).optimize();
		}
		throw new Exception("Algorithm not specified or no optimizer method.");
	}
	
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public static String getAlgorithmList(boolean json, boolean pars, String algo) throws Exception {
		ArrayList<String> algorithms = new ArrayList<String>();
		
		algorithms.add("DeepRand");
		algorithms.add("Rand");
		
		
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
