package swe_mo.fitnessfunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VarSpace {

	private Map<String, ArrayList<Double>> valuelist = new HashMap<String, ArrayList<Double>>();

	public VarSpace() {}
	public VarSpace(VarSpace v) {
		this.valuelist = new HashMap<String, ArrayList<Double>>(v.valuelist);
	}

	public void addVector(String id, ArrayList<Double> values) {
		this.valuelist.put(id, values);
	}
	public void addScalar(String id, Double value) {
		ArrayList<Double> arrList = new ArrayList<Double>();
		arrList.add(value);
		this.valuelist.put(id, arrList);
	}
	public Double get(String id, int index) throws Exception {
		if(valuelist.get(id) == null) 
			throw new Exception("Cannot find value for variable. ("+id+")");
		if(index >= valuelist.get(id).size())
			throw new Exception("Cannot find index in variable array. ("+index+")");
		return valuelist.get(id).get(index);
	}
}
