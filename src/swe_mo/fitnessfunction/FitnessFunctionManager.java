package swe_mo.fitnessfunction;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import swe_mo.solver.FitnessFunction;
import swe_mo.solver.de.Particle_DE;

public class FitnessFunctionManager {	
	private static Map<Integer, FitnessFunctionCustom> ffcList = new HashMap<Integer, FitnessFunctionCustom>();
	private static int counter = 0;
	

	public static int add(String functionString) throws Exception {
		int id = FitnessFunction.numberOfHardCoded + counter;
		if(ffcList.get(id) == null) {
			ffcList.put(id, new FitnessFunctionCustom(functionString));
			counter++;
			return id;
		} else {
			throw new Exception("There is already a custom fitness function with ID "+id+".");
		}
	}
	
	public static void change(String functionString) throws Exception {
		change(FitnessFunction.numberOfHardCoded + counter-1,functionString);
	}
	public static void change(int id, String functionString) throws Exception {
		if(ffcList.get(id) != null) {
			ffcList.put(id, new FitnessFunctionCustom(functionString));
		} else {
			throw new Exception("No custom fitness function with ID "+id+".");
		}
	}

	public static void deleteAll() throws Exception {
		while(ffcList.size() > 0) {
			delete(ffcList.entrySet().iterator().next().getKey());
		}		
	}
	public static void delete(int id1, int id2) throws Exception {
		if(id1>id2) {
			int m = id1;
			id1 = id2;
			id2 = m;
		}
		if(id1<0) id1=0;
		
		for(int i=id1; i<=id2; i++) {
			try {
				delete(i);		
			} catch(Exception e) {}
		}		
	}
	public static void delete(int id) throws Exception {
		if(ffcList.get(id) != null) {
			ffcList.remove(id);
		} else {
			throw new Exception("No custom fitness function with ID "+id+".");
		}
	}

	
	
	
	public static boolean exists(int id) {
		if(ffcList.get(id) != null) {
			return true;
		} else {
			return false;
		}
	}
	

	public static double calculate(int id, Particle_DE vector) throws Exception {
		if(ffcList.get(id) != null) {
			
			VarSpace varspace = new VarSpace();
			varspace.addVector("x", vector.position);
			varspace.addScalar("n", (double)vector.position.size());	
			
			return ffcList.get(id).calculate(varspace);
		} else {
			throw new Exception("No custom fitness function with ID "+id+".");
		}
	}
	
	

	public static int setBoundaryLower(Double b) {
		setBoundaryLower(FitnessFunction.numberOfHardCoded + counter-1, b);
		return FitnessFunction.numberOfHardCoded + counter-1;
	}
	public static void setBoundaryLower(int id, Double b) {
		ffcList.get(id).setLowerBound(b);
	}

	public static int setBoundaryUpper(Double b) {
		setBoundaryUpper(FitnessFunction.numberOfHardCoded + counter-1, b);
		return FitnessFunction.numberOfHardCoded + counter-1;
	}
	public static void setBoundaryUpper(int id, Double b) {
		ffcList.get(id).setUpperBound(b);		
	}
	
	public static Double getBoundaryLower() {
		return getBoundaryLower(FitnessFunction.numberOfHardCoded + counter-1);
	}
	public static Double getBoundaryLower(int id) {
		return ffcList.get(id).getLowerBound();
	}
	
	public static Double getBoundaryUpper() {
		return getBoundaryUpper(FitnessFunction.numberOfHardCoded + counter-1);
	}
	public static Double getBoundaryUpper(int id) {
		return ffcList.get(id).getUpperBound();
	}
	
	
	
	
	public static String print() throws Exception {
		return print(FitnessFunction.numberOfHardCoded + counter-1);
	}
	public static String print(int id) throws Exception {
		if(ffcList.get(id) != null) {			
			return ffcList.get(id).toString();
		} else {
			throw new Exception("No custom fitness function with ID "+id+".");
		}		
	}

	public static String printTex() throws Exception {
		return printTex(FitnessFunction.numberOfHardCoded + counter-1);
	}
	public static String printTex(int id) throws Exception {
		if(ffcList.get(id) != null) {			
			return ffcList.get(id).getFunctionString();
		} else {
			throw new Exception("No custom fitness function with ID "+id+".");
		}		
	}

	public static String printTexStyled() throws Exception {
		return printTexStyled(FitnessFunction.numberOfHardCoded + counter-1);
	}
	public static String printTexStyled(int id) throws Exception {
		if(ffcList.get(id) != null) {			
			return ffcList.get(id).getFunctionStringStyled();
		} else {
			throw new Exception("No custom fitness function with ID "+id+".");
		}		
	}

	public static String printFM() throws Exception {
		return printFM(FitnessFunction.numberOfHardCoded + counter-1);
	}
	public static String printFM(int id) throws Exception {
		if(ffcList.get(id) != null) {			
			return ffcList.get(id).getFunctionMap().toString();
		} else {
			throw new Exception("No custom fitness function with ID "+id+".");
		}		
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public static String list(boolean json) throws Exception {
		if(!json) {
			String out = "ID\tEquation\n\n";
			for(int id : ffcList.keySet()) {
				out += id +":\t"+printTex(id)+"\r";
			}
			return out;
		} else {
			JSONArray jsonarr = new JSONArray();
			for(int id : ffcList.keySet()) {
				JSONObject jobj = new JSONObject();
				jobj.put("id", id);
				jobj.put("functionString", printTexStyled(id));
				jobj.put("bdl", getBoundaryLower(id));
				jobj.put("bdu", getBoundaryUpper(id));
				
				jsonarr.add(jobj);
			}
			
			JSONObject j = new JSONObject();
			j.put("cff", jsonarr);
			return j.toJSONString();			
		}
	}
	
	
}
