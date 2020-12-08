package swe_mo.optimizer;

import org.json.simple.JSONObject;


public class OptimizerResult {
	public String bestParameterset;
	//other messages (number of iterations until result, ...)	
	Exception e;
	
	
	public OptimizerResult() {
		
	}
	
	public OptimizerResult(String bestParameterset) {
		this.bestParameterset = bestParameterset;
		
	}
	
	
	

	@Override
	public String toString() {
		String s = "";
		
		if(e == null) {
			s += "Best Parameterset: "+bestParameterset+"\n";
		} else {
			s += "Exception: "+e.getMessage();
		}
		
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public String toJSON() {
		JSONObject json = new JSONObject();
		
		if(e == null) {
			json.put("bestPS", bestParameterset);
		} else {
			json.put("exception", e.getMessage());
		}

		return json.toJSONString();
	}
}
