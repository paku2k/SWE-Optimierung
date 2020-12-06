package swe_mo.optimizer;

import org.json.simple.JSONObject;


public class OptimizerResult {
	public double value;
	//other messages (number of iterations until result, ...)
	
	Exception e;
	
	
	public OptimizerResult() {
		
	}
	
	public OptimizerResult(double value) {
		this.value = value;
		
	}
	
	
	

	@Override
	public String toString() {
		String s = "";
		
		if(e == null) {
			s += "Value: "+value+"\n";
		} else {
			s += "Exception: "+e.getMessage();
		}
		
		return s;
	}
	
	public String toJSON() {
		JSONObject json = new JSONObject();
		
		if(e == null) {
			json.put("value", value);
		} else {
			json.put("exception", e.getMessage());
		}

		return json.toJSONString();
	}
}
