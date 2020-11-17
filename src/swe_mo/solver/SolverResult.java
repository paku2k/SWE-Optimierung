package swe_mo.solver;

import java.util.ArrayList;
import java.util.Arrays;

import swe_mo.solver.de.Particle_DE;

import org.json.simple.JSONObject;


public class SolverResult {
	public double value;
	public ArrayList<Double> returnPosition;
	public int ffCounter;
	//other messages (number of iterations until result, ...)
	
	Exception e; //bitte stehen lassen, hier speichere ich die Exception, falls ihr in der solve Methode eine werft
	
	
	public SolverResult() {
		
	}
	
	public SolverResult(double value, ArrayList<Double> position, int ffCounter) {
		this.value = value;
		this.returnPosition = position;
		this.ffCounter=ffCounter;
		
	}
	
	
	

	@Override
	public String toString() {
		return "Value: "+value+"\n Particle: "+Arrays.toString(returnPosition.toArray())+"\nFitness functions run: "+ffCounter;
	}
	
	public String toJSON() {
		JSONObject json = new JSONObject();

		json.put("value", value);
		json.put("ffCounter", ffCounter);
		json.put("particle", Arrays.toString(returnPosition.toArray()));
		
		return json.toJSONString();
	}
}
