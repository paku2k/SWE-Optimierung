package swe_mo.solver;

import org.apache.poi.ss.formula.eval.NotImplementedException;

import swe_mo.solver.de.Particle_DE;

public class FitnessFunction {
	
	public static double solve(int index, Particle_DE vector) throws Exception {
	//the index refers to Cakar's list of functions	
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
		 double prod = 1.0;
		 for(Double d : vector.position)
		 {
			 sum+=Math.abs(d);
			 prod*=Math.abs(d);
		 }
		 return sum+prod;
	 }	 
	
	 static double calculatef3(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef4(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef5(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 
	 static double calculatef6(Particle_DE vector)	{
		 double sum = 0.0;
		 for(Double d : vector.position) {
			 sum += Math.pow(Math.round(d+0.5), 2);
		 }
		 return sum;
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
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 
	 static double calculatef11(Particle_DE vector)	{
		 double sum = 0.0;
		 double prod = 1.0;		 
		 for(int i=0; i < vector.position.size(); i++) {
			 Double d = vector.position.get(i);
			 
			 sum += Math.pow(d, 2);
			 prod *= Math.cos(d/Math.sqrt(i+1));
		 }		 
		 return (sum/4000.0 - prod + 1);
	 }
	 
	 static double calculatef12(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef13(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef14(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 
	 static double calculatef15(Particle_DE vector) throws Exception	{
		 if(vector.position.size() != 11) {
			 throw new Exception("Fitness function 15 only allows dimension N=11!");
		 }
		 double sum = 0.0;
		 double[] a = {0.1957, 0.1947, 0.1735, 0.1600, 0.0844, 0.0627, 0.0456, 0.0342, 0.0323, 0.0235, 0.0246};
		 double[] b = {4,      2,      1,      0.5,    0.25,   1.0/6,    0.125,  0.1,    1.0/12,   1.0/14,   1.0/16};
		 for(int i=0; i <= 10; i++) {
			 double x0 = vector.position.get(0);
			 double x1 = vector.position.get(1);
			 double x2 = vector.position.get(2);
			 double x3 = vector.position.get(3);			 
			 sum += Math.pow((a[i]-((x0*(Math.pow(b[i], 2) + b[i]*x1)) / (Math.pow(b[i], 2) + b[i]*x2 + x3))),2);			 
		 }		 
		 return sum;
	 }
	 
	 static double calculatef16(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 static double calculatef17(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
	 }
	 
	 static double calculatef18(Particle_DE vector)	{
		 throw new NotImplementedException("\nDiese Fitness Funktion wurde noch nicht geschrieben!\nSei ein Ehrenmann und schreib sie");
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
	 
	 

		
		public static double getBoundary(String t, int index) throws Exception {
			switch(index) {
			case 2:
				if(t.equals("lower")) return -10;
				if(t.equals("upper")) return 10;
			case 3:
			case 4:
			case 6:
				if(t.equals("lower")) return -100;
				if(t.equals("upper")) return 100;
			case 5:
				if(t.equals("lower")) return -30;
				if(t.equals("upper")) return 30;
			case 7:
				if(t.equals("lower")) return -1.28;
				if(t.equals("upper")) return 1.28;
			case 8:
				if(t.equals("lower")) return -500;
				if(t.equals("upper")) return 500;
			case 10:
				if(t.equals("lower")) return -32;
				if(t.equals("upper")) return 32;
			case 11:
				if(t.equals("lower")) return -600;
				if(t.equals("upper")) return 600;
			case 12:
				if(t.equals("lower")) return -50;
				if(t.equals("upper")) return 50;
			case 13:
			case 15:
			case 16:
				if(t.equals("lower")) return -5;
				if(t.equals("upper")) return 5;
			case 14:
				if(t.equals("lower")) return -65.54;
				if(t.equals("upper")) return 65.54;
			case 17:
				if(t.equals("lower")) return -5;
				if(t.equals("upper")) return 15;
			case 18:
				if(t.equals("lower")) return -2;
				if(t.equals("upper")) return 2;
			case 1:
			case 9:
			default:
				if(t.equals("lower")) return -5.12;
				if(t.equals("upper")) return 5.12;
			}
			return 1;
		}
}
