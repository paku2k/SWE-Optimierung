package swe_mo.solver.de;
import java.util.ArrayList;
import java.lang.StringBuilder;

public class Particle_DE {

	ArrayList<Double> position = new ArrayList<Double>(); 
	
	
	public Particle_DE(int dimension, double max, double min) {
		//This constructor creates a particle with the given dimension 
		//and initializes all dimensions with a random number within the given bounds

	    for(int i = dimension; i>0; i--) {
	    	position.add(CRN.rn(max, min));
	    }
	  }
	
	public Particle_DE(int dimension) {
		//This constructor creates a particle with the given dimension and initializes all dimensions with zero
	    for(int i = dimension; i>0; i--) {
	    	position.add(0.0);
	    }
	  }
	
	public void add(Particle_DE part2) {
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
		String temp="[";
		for(int i=0; i<this.position.size(); i++) {
			temp=temp+" , "+this.position.get(i).toString();
		}
		temp=temp+"]";
		return temp;
	}
	
}