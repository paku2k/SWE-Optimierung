
package swe_mo.solver.pso;

import java.util.ArrayList;
import java.util.Random;

import swe_mo.solver.FitnessFunction;
import swe_mo.solver.de.CRN;
import swe_mo.solver.de.Particle_DE;

public class PSOparticle extends Particle_DE {
	
	
	ArrayList<Double> velocity = new ArrayList<Double>();
	ArrayList<Double> personalBestPosition = new ArrayList<Double>();
	ArrayList<Double> nc = new ArrayList<Double>();
	ArrayList<Integer> neighborhood = new ArrayList<Integer>();
	public double currentMinimum;
	double personalMinimum= Double.MAX_VALUE /10;
	double w, cc, cs, dt,  min, max;
	double ncMinimum = Double.MAX_VALUE;
	int dimension, particleCount;
	
	
	
						/*This constructor creates a particle with the given dimension, 
						initializes all dimensions with a random number within the given bounds
						and sets all parameters.*/
	public PSOparticle(int dimension, double max, double min, double w, double cc, double cs, double dt) {
		super(dimension, max, min);
		this.dimension=dimension;
		this.w=w;
		this.cc=cc; 
		this.cs=cs;
		this.dt=dt;
		this.min=min;
		this.max=max;
		for(int i = dimension; i>0; i--) {
	    	velocity.add(CRN.rn(max, min));}
		personalBestPosition = new ArrayList<Double>(position);
		nc = new ArrayList<Double>(position);

	}
	

						//clone
	public PSOparticle(PSOparticle clone) {
		super(clone);
	}
	

	
						/*This constructor creates a particle with the given dimension, 
						initializes all dimensions with a random number within the given bounds
						and sets all parameters. Additional neighbors are set for the particle*/
	public PSOparticle(int dimension, double max, double min, double w, double cc, double cs, double dt, int particleCount, int neighbors) { 
		this(dimension, max, min, w, cc, cs, dt);
		this.particleCount=particleCount;
		setNeighbors(neighbors);								
	}	

	
	
						/*This constructor creates a particle with the given 
						dimension and initializes all dimensions with zero*/
	public PSOparticle(int dimension) {
		super(dimension);
		velocity = new ArrayList<Double>(position);
		personalBestPosition = new ArrayList<Double>(position);
	}
	
	
	
						/*This method calculates the velocity with the standard PSO algorithm*/	
	public void updateVelocity(ArrayList<Double> socialComponent) {
		double rc=Math.random();
		double rs=Math.random();
		for(int i=0; i<velocity.size(); i++) {
			velocity.set(i, w*velocity.get(i)  
					+  cc*rc*(personalBestPosition.get(i)
							-position.get(i))  
					+  cs*rs*(socialComponent.get(i)
							-position.get(i)));
		}
	}
	
	
	
						/*This method calculates the velocity with the neighborhood PSO algorithm*/	
	public void updateVelocityNSC() {
		double rc=Math.random();
		double rs=Math.random();
		for(int i=0; i<velocity.size(); i++) {
			velocity.set(i, w*velocity.get(i)  
					+  cc*rc*(personalBestPosition.get(i)
							-position.get(i))  
					+  cs*rs*(nc.get(i)
							-position.get(i)));
		}	
	}
	
	
						/*This method calculates the velocity with a combination 
						of the standard algorithm and the neighborhood algorithm*/
	public void updateVelocityGNSC(ArrayList<Double> socialComponent) {
		double rc=Math.random();
		double rs=Math.random();
		for(int i=0; i<velocity.size(); i++) {
			velocity.set(i, w*velocity.get(i)  
					+  cc*rc*(personalBestPosition.get(i)
							-position.get(i))  
					+  cs*rs*((nc.get(i)
							-position.get(i)) + (socialComponent.get(i) - position.get(i))));
		}
		
	}
	
	
	
						/*This method calculates the velocity with the decay PSO algorithm*/
	public void updateVelocityDecay(ArrayList<Double> socialComponent, int numIter , int Iter, double decayStart, double decayEnd) {
		double rc=Math.random();
		double rs=Math.random();
		w=((decayEnd-decayStart)/numIter)*Iter+decayStart;
		for(int i=0; i<velocity.size(); i++) {
			velocity.set(i, w*velocity.get(i)  
					+  cc*rc*(personalBestPosition.get(i)
							-position.get(i))  
					+  cs*rs*(socialComponent.get(i)
							-position.get(i)));
		}
	}
	
	
	
						/*This method identifies and sets the best personal position*/
	public void updatePersonalBestPosition(int ffID) throws Exception {
		double minimum = FitnessFunction.solve(ffID, this);
		this.currentMinimum=minimum;
		if(minimum < personalMinimum) {
			personalMinimum = minimum;
			personalBestPosition = new ArrayList<Double>(position);
		}
	}
	
	
	
						/*This method calculates the new position*/
	public void updatePosition() {
		for(int i=0; i<position.size(); i++) { 
			if((position.get(i) + velocity.get(i)*dt) >= min  && (position.get(i) + velocity.get(i)*dt) <= max) {			//If the dimensions are not in the limits, they are not changed
			position.set(i, position.get(i) + velocity.get(i)*dt);
			}
		}
	}
		
	
	
						/*This method sets the new neighborhood component*/
	public void setNC(ArrayList<Double> nc) {
		for(int i=0; i < nc.size(); i++) {
			this.nc.set(i, nc.get(i));
		}
	}
	
	
	
						/*This method sets the neighbors of the particle randomly*/
	public void setNeighbors(int neighbors){
		for(int i=0; i<(neighbors-1);i++) {
			Random r = new Random();
			neighborhood.add(r.nextInt(particleCount-1));
		}
	}
}