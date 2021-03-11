package swe_mo.solver.de;
import java.util.ArrayList;

public class Particle_DE {

	//position indicates the value of the particle in every dimension
	public ArrayList<Double> position = new ArrayList<Double>(); 
	//ffValueOld indicates the Fitnessfunction ID
	public double ffValueOld;
	
	
	public Particle_DE(int dimension, double max, double min) {
		//This constructor creates a particle with the given dimension 
		//and initializes all dimensions with a random number within the given bounds

	    for(int i = 0; i<dimension; i++) {
	    	if(i%2==0) {
	    		position.add(CRN.rn(max, 0));
	    	} else {
	    		position.add(CRN.rn(min, 0));	    		
	    	}
	    }
	  }
	
	public Particle_DE(Particle_DE clone) {
		this.position=new ArrayList<Double>(clone.position);
		this.ffValueOld=clone.ffValueOld;
	}
	
	public Particle_DE(int dimension) {
		//This constructor creates a particle with the given dimension and initializes all dimensions with zero
	    for(int i = dimension; i>0; i--) {
	    	position.add(0.0);
	    }
	  }
	
	public void add(Particle_DE part2) {
		//Adds new from this particle
		for(int i=0; i<this.position.size(); i++) {
			this.position.set(i, part2.position.get(i)+this.position.get(i));
		}
		
	}
	
	public void substract(Particle_DE part2) {
		//Substracts new from this particle
		for(int i=0; i<this.position.size(); i++) {
			this.position.set(i, this.position.get(i)-part2.position.get(i));
		}
		
	}
	
	public void multiply(double mult) {
		//Multiply particle
		for(int i=0; i<this.position.size(); i++) {
			this.position.set(i, this.position.get(i)*mult);
		}
		
	}
	
	@Override
	public String toString(){
		String temp="[" + this.position.get(0).toString();
		for(int i=1; i<this.position.size(); i++) {
			temp=temp+" , "+this.position.get(i).toString();
		}
		temp=temp+"]";
		return temp;
	}
	
}