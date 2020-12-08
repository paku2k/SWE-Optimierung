package swe_mo.solver;

import java.util.Collections;

import org.apache.poi.ss.formula.eval.NotImplementedException;

import swe_mo.solver.de.Particle_DE;

public class FitnessFunction {
	
	public static double solve(int index, Particle_DE vector) throws Exception 
	//the index refers to Cakar's list of functions
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
		 double result = Double.MIN_VALUE;
		 result = Math.abs(Collections.min(vector.position));
		 if(Math.abs(Collections.max(vector.position))>result) {
			 result = Math.abs(Collections.max(vector.position));
		 }
		 return result;
	 }
	 
	 static double calculatef5(Particle_DE vector)	{
		 double sum = 0;
		 
		 for (int i = 0; i<vector.position.size()-1; i++) {
			 sum += 100.0*Math.pow((vector.position.get(i+1) - Math.pow(vector.position.get(i), 2)),2) + Math.pow(vector.position.get(i)-1.0 ,2);
		 }
		 
		 return sum;
		 
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
		 double result = 0;
		 for(Double d : vector.position){
			 result = result + (Math.pow(d, 2)-(10*Math.cos(2*Math.PI*d))+10);
		 }
		 return result;
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
	 static double calculatef13(Particle_DE vector)	throws Exception	{
		 if(vector.position.size() < 3) {
			 throw new Exception("Fitness function 13 only allows dimension N>=3!");
		 }
		 double result = 0.0;
		 double sum1 = 0.0;
		 double sum2 = 0.0;
		 int a = 10;
		 int k = 100;
		 int m = 4;
		 double xn = vector.position.get(vector.position.size()-1);
		 double x1 = vector.position.get(1);
		 
		 for(int i=0; i<vector.position.size()-1;i++) {
			 sum1 = sum1 + (Math.pow((vector.position.get(i)-1), 2)*(1+Math.pow(Math.sin(3*Math.PI*vector.position.get(i+1)), 2)));
		 }
		 
		 for(Double xi : vector.position){
			 if(xi<-a) {
				 sum2 = sum2 + k*Math.pow((-xi-a), m);
			 }
			 else if(xi>a) {
				 sum2 = sum2 + k*Math.pow((xi-a), m);
			 }
		 }
		 
		 result = (10*Math.pow(Math.sin(3*Math.PI*x1), 2));
		 result += sum1;
		 result += ((xn-1)*(1+Math.pow(Math.sin(2*Math.PI*xn),2)));
		 result *= 0.1;
		 result += sum2;
		 return result;	 
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
	 static double calculatef17(Particle_DE vector)	throws Exception	{
		 if(vector.position.size() != 2) {
			 throw new Exception("Fitness function 17 only allows dimension N=2!");
		 }
		 double result = Double.MAX_VALUE;
		 double x1 = vector.position.get(1);
		 double x0 = vector.position.get(0);
		 
		 
		 result = Math.pow((x1-(5.1/(4.0*Math.pow(Math.PI, 2)))*Math.pow(x0, 2)+(5.0/Math.PI)*x0-6.0),2);
		 result = result + 10.0*(1.0-(1.0/(8.0*Math.PI)))*Math.cos(x0);
		 result = result + 10.0;
		 
		 return result;
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
