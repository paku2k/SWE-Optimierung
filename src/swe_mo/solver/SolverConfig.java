package swe_mo.solver;

import swe_mo.solver.de.DErand1;
import swe_mo.solver.pso.PSOgsc;
import swe_mo.solver.pso.PSOgscDecay;
import swe_mo.solver.de.DEbest1;

import org.json.simple.JSONObject;




public class SolverConfig {

	//test
	public int ffid;
	public int N; //dimension
	public int NP; //particleCount
	public double F;
	public double CR;
	public int maxGenerations; //numIter
	public double upperBound; //max
	public double lowerBound; //min
	//PSOgsc atributes
	public double w;
	public double cc;
	public double cs;
	public double dt;
	//PSOgscDecay atributes
	public double decayStart;
	public double decayEnd;
	

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
	};
	
	public SolverConfig(int ffid, int n, int nP, double f, double cR, int maxGenerations, double upperBound, double lowerBound) {
		super();
		this.ffid = ffid;
		N = n;
		NP = nP;
		F = f;
		CR = cR;
		this.maxGenerations = maxGenerations;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
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
		}
		throw new Exception("No such hyperparameter ("+param+").");
	}
	

	
	
	@Override
	public String toString() {
		return "ERR: not implemented (N="+N+")";
	}
	
	@SuppressWarnings("unchecked")
	public String toJSON() {
		JSONObject json = new JSONObject();

		json.put("error", "not implemented");
		json.put("ffid", ffid);
		json.put("N", N);
		json.put("NP", NP);
		json.put("F", F);
		json.put("CR", CR);
		json.put("maxGenerations", maxGenerations);
		json.put("lowerBound", lowerBound);
		json.put("upperBound", upperBound);
		
		return json.toJSONString();
	}
	
	
	
	
	
	public static SolverConfig getDefault(String algorithm) throws Exception {
		switch(algorithm) {
			case "default":
				return test_david.defaultConfig();
			case "DErand1":
				return DErand1.defaultConfig();
			case "DEbest1":
				return DEbest1.defaultConfig();
			case "PSOgsc":
				return PSOgsc.defaultConfig();
			case "PSOgscDecay":
				return PSOgscDecay.defaultConfig();
		}	
		throw new Exception("Algorithm not specified.");
	}
	
	public static SolverResult solveMethod(String algorithm, int id, SolverConfig config) throws Exception {

		switch(algorithm) {
			case "default":
				return new test_david(id,
									  config.N).calc();		
			case "DErand1":
				return new DErand1(config.N,
										 config.NP,
										 config.F,
										 config.CR,
										 config.maxGenerations,
										 config.upperBound,
										 config.lowerBound,
										 config.ffid, id).solve();	
				
			case "DEbest1":
				return new DEbest1(config.N,
										 config.NP,
										 config.F,
										 config.CR,
										 config.maxGenerations,
										 config.upperBound,
										 config.lowerBound,
										 config.ffid, id).solve();	
					
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
				
			case "PSOgscDecay":
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
}
