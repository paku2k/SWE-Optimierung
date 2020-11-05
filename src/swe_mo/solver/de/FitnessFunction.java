package swe_mo.solver.de;

public class FitnessFunction {

	 double calculate(Particle_DE vector)	{
		 double sum = 0.0;
		 for(Double d : vector.position)
		 {
			 sum+=d;
		 }
		 return sum;
	 }
	
}
