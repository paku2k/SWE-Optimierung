package swe_mo.solver;


import swe_mo.solver.de.Particle_DE;

import org.json.simple.JSONObject;


public class SolverResult {
	public double value;
	public Particle_DE particle;
	public int ffCounter;
	//other messages (number of iterations until result, ...)
	
	Exception e; //bitte stehen lassen, hier speichere ich die Exception, falls ihr in der solve Methode eine werft
	
	
	public SolverResult() {
		// TODO Auto-generated constructor stub
	}
	
	public SolverResult(double value, Particle_DE particle, int ffCounter) {
		this.value = value;
		this.particle = particle;
		this.ffCounter=ffCounter;
		// TODO Auto-generated constructor stub
	}
	
	
	

	@Override
	public String toString() {
		return "Value: "+value+"\n Particle: "+particle.toString()+"\nFitness functions run: "+ffCounter;
	}
	
	public String toJSON() {
		JSONObject json = new JSONObject();

		json.put("value", value);
		json.put("ffCounter", ffCounter);
		json.put("particle", particle.toString());
		
		return json.toJSONString();
	}
}
