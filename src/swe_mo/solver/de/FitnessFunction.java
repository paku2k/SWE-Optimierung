package swe_mo.solver.de;

public class FitnessFunction {

	 double calculatef2(Particle_DE vector)	{
		 double sum = 0.0;
		 double mult =1.0;
		 for(Double d : vector.position)
		 {
			 sum+=Math.abs(d);
		 }
		 for(Double d : vector.position)

		 {
			 mult*=Math.abs(d);
		 }
		 return sum+mult;
	 }
	 
	 double calculatef8(Particle_DE vector)	{
		 double sum = 0.0;
		 for(Double d : vector.position)
		 {
			 sum += (-d)*Math.sin(Math.sqrt(Math.abs(d)));
		 }
		 
		 return sum;
	 }
	
	 
	 double calculateRastrigin(Particle_DE vector) {
		 double sum = 0.0;
		 double A = 10.0;
		 for(Double d : vector.position)
		 {
			 sum += (Math.pow(d, 2))-A*Math.cos(2*Math.PI*d);
		 }
		 
		 return sum+A*vector.position.size();
		 
	 }
	 
}
