package swe_mo.solver;

import java.util.ArrayList;
import java.util.Arrays;
import org.json.simple.JSONObject;


public class SolverResult {
	public double value;
	public ArrayList<Double> returnPosition;
	public long ffCounter;
	public long iterations;
	//other messages (number of iterations until result, ...)
	
	Exception e; //bitte stehen lassen, hier speichere ich die Exception, falls ihr in der solve Methode eine werft
	
	
	public SolverResult() {
		
	}
	
	public SolverResult(double value, ArrayList<Double> position, int ffCounter) {
		this.value = value;
		this.returnPosition = position;
		this.ffCounter=ffCounter;
		
	}
	
	public SolverResult(double value, ArrayList<Double> position, long ffCounter, long iterations) {
		this.value = value;
		this.returnPosition = position;
		this.ffCounter=ffCounter;
		this.iterations = iterations;
	}
	
	
	

	@Override
	public String toString() {
		String s = "";
		
		if(e == null) {
			s += "Value: "+value+"\n";
			if(returnPosition != null) {
				s += "Particle: "+Arrays.toString(returnPosition.toArray());
			} else {
				s += "Particle: null";
			}
			s += "\nIterations: "+iterations;
			s += "\nFitness functions run: "+ffCounter;
		} else {
			s += "Exception: "+e.getMessage();
		}
		
		return s;
	}
	
	public String toJSON() {
		JSONObject json = new JSONObject();
		
		if(e == null) {
			json.put("value", value);
			json.put("ffCounter", ffCounter);
			json.put("iterations", iterations);
			if(returnPosition != null) {
				json.put("particle", Arrays.toString(returnPosition.toArray()));
			} else {
				json.put("particle", null);
			}
		} else {
			json.put("exception", e.getMessage());
		}

		return json.toJSONString();
	}
}
