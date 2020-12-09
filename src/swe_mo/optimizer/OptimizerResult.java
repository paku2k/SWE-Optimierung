package swe_mo.optimizer;

import org.json.simple.JSONObject;


public class OptimizerResult {
	public String bestParameterset;
	public double minimum;

	Exception e;
	
	
	public OptimizerResult() {
		
	}
	
	public OptimizerResult(String bestParameterset, double minimum) {
		this.bestParameterset = bestParameterset;
		this.minimum = minimum;
		
	}
	
	
	

	@Override
	public String toString() {
		String s = "";
		
		if(e == null) {
			s += "Best Parameterset: "+bestParameterset+"\n";
			s += "Minimum: "+minimum+"\n";
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
			json.put("minimum", minimum);
		} else {
			json.put("exception", e.getMessage());
		}

		return json.toJSONString();
	}
}
