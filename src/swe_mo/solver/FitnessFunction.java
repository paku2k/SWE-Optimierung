package swe_mo.solver;

import org.apache.poi.ss.formula.eval.NotImplementedException;

import swe_mo.solver.de.Particle_DE;

public class FitnessFunction {
	
	public static double solve(int index, Particle_DE vector) 
	//the index refers to Papa Cakar's list of functions
	{
		switch(index) {
		case 1:
			return calculatef1(vector);
		case 2:
			return calculatef2(vector);
		case 3:
			return calculatef3(vector);
		case 4:
			return calculatef4(vector);
		case 5:
			return calculatef5(vector);
		case 6:
			return calculatef6(vector);
		case 7:
			return calculatef7(vector);
		case 8:
			return calculatef8(vector);
		case 9:
			return calculatef9(vector);
		case 10:
			return calculatef10(vector);
		case 11:
			return calculatef11(vector);
		case 12:
			return calculatef12(vector);
		case 13:
			return calculatef13(vector);
		case 14:
			return calculatef14(vector);
		case 15:
			return calculatef15(vector);
		case 16:
			return calculatef16(vector);
		case 17:
			return calculatef17(vector);
		case 18:
			return calculatef18(vector);
		default:
			return calculateRastrigin(vector);
		}
		
	}

	

	 
	 static double calculatef1(Particle_DE vector)	{
		 double sum = 0.0;
		 for(Double d : vector.position)
		 {
			 sum += Math.pow(d,2);
		 }
		 
		 return sum;
	 }
	 
	 static double calculatef2(Particle_DE vector)	{
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
	 
	
	 static double calculatef3(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef4(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef5(Particle_DE vector)	{
		 double sum = 0;
		 
		 for (int i = 0; i<vector.position.size()-1; i++) {
			 sum += 100.0*Math.pow((vector.position.get(i+1) - Math.pow(vector.position.get(i), 2)),2) + Math.pow(vector.position.get(i)-1.0 ,2);
		 }
		 
		 return sum;
		 
	 }
	 static double calculatef6(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef7(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef8(Particle_DE vector)	{
		 double sum = 0.0;
		 for(Double d : vector.position)
		 {
			 sum += (-d)*Math.sin(Math.sqrt(Math.abs(d)));
		 }
		 
		 return sum;
	 }
	 static double calculatef9(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef10(Particle_DE vector)	{
		 double sum1 = 0.0;
		 for (double f : vector.position) {
			 sum1+=Math.pow(f,2);
		 }
		 
		 double sum2=0.0;
		 for (double f : vector.position) {
			 sum2+=Math.cos(2.0*Math.PI*f);
		 }
		 
		 
		 double d = -20.0*Math.exp(-0.2*Math.sqrt((1.0/vector.position.size())*sum1))-Math.exp((1.0/vector.position.size())*sum2)+20.0+Math.exp(1);
		 return d;
	 
	 }
	 static double calculatef11(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef12(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef13(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef14(Particle_DE vector)	{
		 
		 double a[][] = {{-32,-16,0,16,32,-32,-16,0,16,32,-32,-16,0,16,32,-32,-16,0,16,32,-32,-16,0,16,32}, {-32,-32,-32,-32,-32,-16,-16,-16,-16,-16,0,0,0,0,0,16,16,16,16,16,32,32,32,32,32}};
		 
		 double sum = 0.0;
		 for(int j = 0; j<25; j++) {
			 
			 double sum2 = 0.0;
			 for (int i = 0; i<2; i++) {
				 sum2+=Math.pow((vector.position.get(i))-a[i][j], 6);
			 }
			 
			 
			 sum+=Math.pow(j+1.0+sum2, -1);
			 
		 } 
		 
		 return Math.pow(((1.0/500.0)+sum), -1);
		 
	 }
	 static double calculatef15(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef16(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef17(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 
	 static double calculatef18(Particle_DE vector)	{
		 if (vector.position.size()>2) {
			// throw new Exception("Too many dimensions for this function");
		 }
		 
		 double x0=vector.position.get(0);
		 double x1=vector.position.get(1);

		 
		 return (1+Math.pow((x0+x1+1),2)*(19-14*x0+3*x0*x0-14*x1+6*x0*x1+3*x1*x1))*(30+Math.pow(2*x0-3*x1,2)*(18-32*x0+12*x0*x0+48*x1-36*x0*x1+27*x1*x1));
	 }
	 
	 
	 
	 
	 static double calculateRastrigin(Particle_DE vector) {
		 double sum = 0.0;
		 double A = 10.0;
		 for(Double d : vector.position)
		 {
			 sum += (Math.pow(d, 2))-A*Math.cos(2*Math.PI*d);
		 }
		 
		 return sum+A*vector.position.size();
		 
	 }
	 
}
