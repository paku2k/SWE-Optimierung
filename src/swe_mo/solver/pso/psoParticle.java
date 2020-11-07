
package swe_mo.solver.pso;

import java.util.ArrayList;

import swe_mo.solver.de.CRN;
import swe_mo.solver.de.Particle_DE;

public class psoParticle extends Particle_DE {
	
	
	ArrayList<Double> velocity = new ArrayList<Double>();
	ArrayList<Double> personalBestPosition = new ArrayList<Double>();
	double personalMinimum;
	double w, cc, cs;  								//wie werden sie inizialisiert? setter/getter-Methoden?	
	
	
	public psoParticle(int dimension, double max, double min) {
			//This constructor creates a particle with the given dimension 
			//and initializes all dimensions with a random number within the given bounds
		super(dimension, max, min);
		initializesVelocity();
		initializesPersonalBestPosition();
	}
		
	
	public psoParticle(int dimension) {
		//This constructor creates a particle with the given dimension and initializes all dimensions with zero
		super(dimension);
		initializesVelocity();
		initializesPersonalBestPosition();
	}
	
	
	public void initializesVelocity(){
		for(int i=0;i<this.position.size();i++) {
			this.velocity.add(this.position.get(i));
		}
		
	}
	
	public void initializesPersonalBestPosition() {
		for(int i=0;i<this.position.size();i++) {
			this.personalBestPosition.add(this.position.get(i));
		}
	}
	
	public void updateVelocity(psoParticle globalBestPosition) {
		double rc=Math.random();
		double rs=Math.random();
		for(int i=0; i<this.velocity.size(); i++) {
			this.velocity.set(i, w*this.velocity.get(i)  +  cc*rc*(this.personalBestPosition.get(i)-this.position.get(i))  +  cs*rs*(globalBestPosition.position.get(i)-this.position.get(i)));
		}
	}
		
	
	public void updatePersonalBestPosition() {
		this.personalBestPosition=this.position;
	}
	
	
	public void updatePosition() {
		for(int i=0; i<this.position.size(); i++) {
			this.position.set(i, this.position.get(i)+this.velocity.get(i));
		}
	}
	
}