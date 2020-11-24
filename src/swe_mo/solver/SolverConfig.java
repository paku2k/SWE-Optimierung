package swe_mo.solver;

import swe_mo.solver.de.DErand1;
import swe_mo.solver.pso.PSOgsc;
import swe_mo.solver.pso.PSOgscDecay;
import swe_mo.solver.pso.PSOnsc;
import swe_mo.solver.de.DEbest1;
import swe_mo.solver.de.DEbest2;
import swe_mo.solver.de.DErandToBest1;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;




public class SolverConfig {

	public int ffid;
	
	public int N; //dimension
	public int NP; //particleCount
	public int maxGenerations; //numIter
	public double upperBound; //max
	public double lowerBound; //min
	//DE attributes
	public double F;
	public double CR;
	//DErtb1 attributes
	public double lambda;
	public double convergence;
	//PSOgsc attributes
	public double w;
	public double cc;
	public double cs;
	public double dt;
	//PSOgscDecay attributes
	public double decayStart;
	public double decayEnd;
	
	
	//list of used parameters
	public ArrayList<String> usedpars = new ArrayList<String>();
	
	
	

	public SolverConfig() {};
	
	//for cloning
	public SolverConfig(SolverConfig s) {
		this.ffid = s.ffid;
		this.N = s.N;
		this.NP = s.NP;
		this.F = s.F;
		this.CR = s.CR;
		this.maxGenerations = s.maxGenerations;
		this.upperBound = s.upperBound;
		this.lowerBound = s.lowerBound;
		this.lambda = s.lambda;
		this.w = s.w;
		this.cc = s.cc;
		this.cs = s.cs;
		this.dt = s.dt;
		this.decayStart = s.decayStart;
		this.decayEnd = s.decayEnd;
		
		this.usedpars = s.usedpars;
	};
	
	
	//basic
	public SolverConfig(int ffid, int n, int nP, int maxGenerations, double upperBound, double lowerBound) {
		this.ffid = ffid;
		this.N = n;
		this.NP = nP;
		this.maxGenerations = maxGenerations;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
	
		
		usedpars.add("ffid");
		usedpars.add("N");
		usedpars.add("NP");
		usedpars.add("maxGenerations");
		usedpars.add("upperBound");
		usedpars.add("lowerBound");		
	}
	
	//with convergence
	public SolverConfig(int ffid, int n, int nP, int maxGenerations, double upperBound, double lowerBound, double convergence) {
		this(ffid, n, nP, maxGenerations, upperBound, lowerBound);
		
		this.convergence=convergence;
		usedpars.add("convergence");

	}
	
	
	//DErand1, DEbest1, DEbest2
	public SolverConfig(int ffid, int n, int nP, double f, double cR, int maxGenerations, double upperBound, double lowerBound, double convergence) {
		this(ffid, n, nP, maxGenerations, upperBound, lowerBound, convergence);
		this.F = f;
		this.CR = cR;

		usedpars.add("F");
		usedpars.add("CR");
	}
	
	//DErtb1
	public SolverConfig(int ffid, int n, int nP, double f, double cR, double lambda, int maxGenerations, double upperBound, double lowerBound, double convergence) {
		this(ffid, n, nP, f, cR, maxGenerations, upperBound, lowerBound, convergence);
		
		this.lambda = lambda;

		usedpars.add("lambda");
	}
	
	//PSOgsc
	public SolverConfig(int ffid, int n, int nP, int maxGenerations, double upperBound, double lowerBound, double w, double cc, double cs, double dt) {
		this(ffid, n, nP, maxGenerations, upperBound, lowerBound);		

		this.w = w;
		this.cc = cc;
		this.cs = cs;
		this.dt = dt;

		usedpars.add("w");
		usedpars.add("cc");
		usedpars.add("cs");
		usedpars.add("dt");
	}
	
	//PSOgscDecay
	public SolverConfig(int ffid, int n, int nP, int maxGenerations, double upperBound, double lowerBound, double w, double cc, double cs, double dt, double decayStart, double decayEnd) {
		this(ffid, n, nP, maxGenerations, upperBound, lowerBound, w, cc, cs, dt);		

		this.decayStart = decayStart;
		this.decayEnd = decayEnd;

		usedpars.add("decayStart");
		usedpars.add("decayEnd");
	}
	


	public void set(String param, String value) throws Exception {
		switch(param) {
			case "ffid":
				ffid = Integer.parseInt(value);
				return;
			case "N":
				N = Integer.parseInt(value);
				return;
			case "NP":
				NP = Integer.parseInt(value);
				return;
			case "F":
				F = Double.parseDouble(value);
				return;
			case "CR":
				CR = Double.parseDouble(value);
				return;
			case "maxGenerations":
				maxGenerations = Integer.parseInt(value);
				return;
			case "upperBound":
				upperBound = Double.parseDouble(value);
				return;
			case "lowerBound":
				lowerBound = Double.parseDouble(value);
				return;
			case "w":
				w = Double.parseDouble(value);
				return;
			case "cc":
				cc = Double.parseDouble(value);
				return;
			case "cs":
				cs = Double.parseDouble(value);
				return;
			case "dt":
				dt = Double.parseDouble(value);
				return;
			case "decayStart":
				decayStart = Double.parseDouble(value);
				return;
			case "decayEnd":
				decayEnd = Double.parseDouble(value);
				return;
			case "lambda":
				lambda = Double.parseDouble(value);
				return;
			case "convergence":
				convergence = Double.parseDouble(value);
				return;
		}
		throw new Exception("No such hyperparameter ("+param+").");
	}

	
	public Object getValue(String param) {
		switch(param) {
			case "ffid": 
				return ffid;
			case "N":  
				return N;
			case "NP": 
				return NP;
			case "maxGenerations": 
				return maxGenerations;
			case "upperBound": 
				return upperBound;
			case "lowerBound": 
				return lowerBound;
			case "F": 
				return F;
			case "CR": 
				return CR;
			case "lambda": 
				return lambda;
			case "w": 
				return w;
			case "cc": 
				return cc;
			case "cs": 
				return cs;
			case "dt": 
				return dt;
			case "decayStart": 
				return decayStart;
			case "decayEnd": 
				return decayEnd;
			case "convergence": 
				return convergence;
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
		JSONObject json = new JSONObject();
		
		for(String p : usedpars) {
			json.put(p, getValue(p));
		}
		
		return json.toJSONString();
	}
	
	
	
	
	
	public static SolverConfig getDefault(String algorithm) throws Exception {
		switch(algorithm) {
			case "DErand1":
				return DErand1.defaultConfig();
			case "DEbest1":
				return DEbest1.defaultConfig();
			case "DEbest2":
				return DEbest2.defaultConfig();
			case "DErtb1":
				return DErandToBest1.defaultConfig();
			case "PSOgsc":
				return PSOgsc.defaultConfig();
			case "PSOnsc":
				return PSOnsc.defaultConfig();
			case "PSOgscD":
				return PSOgscDecay.defaultConfig();
				
		}	
		throw new Exception("Algorithm not specified.");
	}
	
	public static SolverResult solveMethod(String algorithm, int id, SolverConfig config) throws Exception {

		switch(algorithm) {
			case "DErand1":
				return new DErand1(config.N,
										 config.NP,
										 config.F,
										 config.CR,
										 config.maxGenerations,
										 config.upperBound,
										 config.lowerBound,
										 config.ffid, id, config.convergence).solve();	
				
			case "DEbest1":
				return new DEbest1(config.N,
										 config.NP,
										 config.F,
										 config.CR,
										 config.maxGenerations,
										 config.upperBound,
										 config.lowerBound,
										 config.ffid, id,config.convergence).solve();	
			case "DEbest2":
				return new DEbest2(config.N,
										 config.NP,
										 config.F,
										 config.CR,
										 config.maxGenerations,
										 config.upperBound,
										 config.lowerBound,
										 config.ffid, id,config.convergence).solve();
			case "DErtb1":
				return new DErandToBest1(config.N,
										 config.NP,
										 config.F,
										 config.CR,
										 config.lambda,
										 config.maxGenerations,
										 config.upperBound,
										 config.lowerBound,
										 config.ffid, id,config.convergence).solve();
					
			case "PSOgsc":
				return new PSOgsc(config.N,
										 config.lowerBound,
										 config.upperBound,
										 config.NP,
										 config.w,
										 config.cc,
										 config.cs,
										 config.dt,
										 config.maxGenerations,
										 config.ffid, id).solve();	
				
			case "PSOnsc":
				return new PSOnsc(config.N,
										 config.lowerBound,
										 config.upperBound,
										 config.NP,
										 config.w,
										 config.cc,
										 config.cs,
										 config.dt,
										 config.maxGenerations,
										 config.ffid, id).solve();
				
			case "PSOgscD":
				return new PSOgscDecay(config.N,
										 config.lowerBound,
										 config.upperBound,
										 config.NP,
										 config.w,
										 config.cc,
										 config.cs,
										 config.dt,
										 config.maxGenerations,
										 config.ffid, 
										 id,
										 config.decayStart,
										 config.decayEnd).solve();	

		}
		throw new Exception("Algorithm not specified or no solver method.");
	}
	
	public static String getAlgorithmList(boolean json, boolean pars, String algo) throws Exception {
		ArrayList<String> algorithms = new ArrayList<String>();
		
		algorithms.add("DErand1");
		algorithms.add("DEbest1");
		algorithms.add("DEbest2");
		algorithms.add("DErtb1");
		algorithms.add("PSOgsc");
		algorithms.add("PSOnsc");
		algorithms.add("PSOgscD");
		
		
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
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("algorithms", algorithms);
				return jsonobj.toJSONString();
			}
			
		} else {
			SolverConfig defaultConfig = getDefault(algo);
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
