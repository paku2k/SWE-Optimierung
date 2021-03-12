package swe_mo.solver;

import java.util.ArrayList;
import java.util.Collections;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import swe_mo.fitnessfunction.FitnessFunctionManager;
import swe_mo.solver.de.Particle_DE;

public class FitnessFunction {
	
	public static int numberOfHardCoded = 27;
	
	public static double solve(int index, Particle_DE vector) throws Exception {
	//the index refers to Cakar's list of functions	
		switch(index) {
			case 0:
				return calculateRastrigin(vector);
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

			case 19:
				return calculatef19(vector.position);
			case 20:
				return calculatef20(vector.position);
			case 21:
				return calculatef21(vector.position);
			case 22:
				return calculatef22(vector.position);
			case 23:
				return calculatef23(vector.position);
			case 24:
				return calculatef24(vector.position);
			case 25:
				return calculatef25(vector.position);
			case 26:
				return calculatef26(vector.position);
			
			default:
				if(FitnessFunctionManager.exists(index)) {
					return FitnessFunctionManager.calculate(index, vector);
				} else {
					throw new Exception("Cannot find fitnessfunction "+index);					
				}
		}
		
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
		 double sum = 0.0;
		 double sum1 = 0.0;
		 for(int i=0; i<vector.position.size(); i++ ) {
			 for(int j=0; j<=i; j++) {
				 sum1 = 0.0;
				 sum1 += vector.position.get(j);
			 }
			 sum += Math.pow(sum1, 2);
		 }
		 return sum;
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
		double sum = 0.0;
		for(int i=0; i<vector.position.size(); i++) {
			sum += (i+1) * Math.pow(vector.position.get(i), 4);
		}
		return sum + Math.random();
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
		 double sum = 0.0;
		 double sum1 = 0.0;
		 double sum2 = 0.0;
		 for(int i=0; i<vector.position.size()-1; i++) {
			 sum1 += Math.pow(vector.position.get(i)-1, 2)*Math.pow(1+10*Math.sin(Math.PI*vector.position.get(i+1)),2);
		 }
		 sum1 += 10*Math.pow(Math.sin(Math.PI*vector.position.get(0)),2) + Math.pow(vector.position.get(vector.position.size()-1)-1,2); 
				 ;
		 for(int i=0; i<vector.position.size(); i++) {
			 if(vector.position.get(i) > 10) {
				 sum2 += 100*Math.pow(vector.position.get(i)-10, 4);				 
			 }else if(vector.position.get(i) >= -10) {
				 sum2 += 0;				 
			 }else {
				 sum2 += 100*Math.pow(-vector.position.get(i)-10, 4);
			 }
		 }
		 sum = Math.PI/(vector.position.size()-1) * sum1 + sum2;
		 return sum;
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
	 
	 static double calculatef16(Particle_DE vector) throws Exception	{
		 if(vector.position.size() != 2) {
			 throw new Exception("Fitness function 16 only allows dimension N=2!");
		 }
		 double x0 = vector.position.get(0);
		 double x1 = vector.position.get(1);
		 double sum = 4*Math.pow(x0, 2) - 2.1*Math.pow(x0, 4) + (1.0/3.0)*Math.pow(x0, 6) + x0*x1 - 4*Math.pow(x1, 2) + 4*Math.pow(x1, 4);
		 return sum;
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
		 }
		 
		 double x0=vector.position.get(0);
		 double x1=vector.position.get(1);

		 
		 return (1+Math.pow((x0+x1+1),2)*(19-14*x0+3*x0*x0-14*x1+6*x0*x1+3*x1*x1))*(30+Math.pow(2*x0-3*x1,2)*(18-32*x0+12*x0*x0+48*x1-36*x0*x1+27*x1*x1));
	 }

	 
	 static double calculatef19(ArrayList<Double> trialSolution) {
		double newFitness = 0;
		double x1 = trialSolution.get(0); 
		double x2 = trialSolution.get(1);
		double a = 1+(x1+x2+1)*(x1+x2+1)*(19-14*x1+3*x1*x1-14*x2+6*x1*x2+3*x2*x2);
		double b = 30+(2*x1-3*x2)*(2*x1-3*x2)*(18-32*x1+12*x1*x1+48*x2-36*x1*x2+27*x2*x2);
		newFitness = a*b;
		return newFitness;
	 }
	 static double calculatef20(ArrayList<Double> trialSolution) {
		 double newFitness = 0;
		 double p[][] = {{0.1312,	0.1696,		0.5569,		0.0124,		0.8283,		0.5886}, 
		 					{0.2329,	0.4135,		0.8307,		0.3736,		0.1004,		0.9991},
		 					{0.2348,	0.1451,		0.3522,		0.2883,		0.3047,		0.6650},
		 					{0.4047,	0.8828,		0.8732,		0.5743,		0.1091,		0.0381}};

		 double a[][] = {{10.0, 	3.0, 	17.0, 	3.5,	1.7,	8.0}, 
		 					{0.05, 	10.0, 	17.0, 	0.1, 	8.0,	14.0},
		 					{3.0, 	3.5, 	1.7, 	10.0, 	17.0, 	8.0},
		 					{17.0,	8.0,	0.05,	10.0,	0.1,	14.0}};

		 double c[] = 	{1.0,	1.2,	3.0,	3.2};

		 for (int i = 0 ; i < 4 ;++i){
		 	double sm = 0;
		 	for (int j = 0 ; j < 6; ++j){	
		 		sm += a[i][j] * (trialSolution.get(j) - p [i][j]) * (trialSolution.get(j) - p [i][j]);
		 	}
		 	newFitness += c[i] * Math.exp(-sm);
		 }
		 newFitness *= -1;
		 return newFitness;
	 }	 
	 static double calculatef21(ArrayList<Double> trialSolution) {
		 double newFitness = 0;
		 for (int i = 1 ; i <= trialSolution.size(); ++i){	
		 	double xi = trialSolution.get(i-1);
		 	
		 	newFitness += Math.sin(xi) * Math.pow(Math.sin((i*xi*xi)/ Math.PI), 2*10);
		 }
		 newFitness *= -1;
		 return newFitness;
	 }	 
	 static double calculatef22(ArrayList<Double> trialSolution) {
		 double newFitness = 0;
		 double d = 1; //{1,2,3,4}
		 double s = 1 - (1 / (2 * Math.sqrt(trialSolution.size() + 20) - 8.2));

		 double mue1 = 2.5;
		 double mue2 = -1 * (Math.sqrt((mue1*mue1 - d) / s));

		 double firstTerm = 0;
		 for (int i = 0; i < trialSolution.size(); ++i)
		 {
		 	firstTerm += Math.pow(trialSolution.get(i) - mue1, 2);
		 }

		 double secondTerm = 0;
		 for (int i = 0; i < trialSolution.size(); ++i)
		 {
		 	secondTerm += Math.pow(trialSolution.get(i) - mue2, 2);
		 }
		 secondTerm *= s;
		 secondTerm += (d*trialSolution.size());

		 double thirdTerm = 0;
		 for (int i = 0; i < trialSolution.size(); ++i)
		 {
		 	thirdTerm += (1 - Math.cos(2 * Math.PI * (trialSolution.get(i) - mue1)));
		 }
		 thirdTerm *= 10;

		 newFitness = Math.min(firstTerm, secondTerm) + thirdTerm;
		 return newFitness;
	 }
	 static double calculatef23(ArrayList<Double> trialSolution) {
		 double newFitness = 0;
		 double s1 = 0;
		 double s2 = 0;

		 for (int i = 0 ; i < trialSolution.size(); ++i){	
		 	s1 += Math.pow(trialSolution.get(i)-1, 2.0);
		 }

		 for (int i = 1 ; i < trialSolution.size(); ++i){	
		 	s2 += (trialSolution.get(i) *  trialSolution.get(i-1));
		 }
		 newFitness =  s1-s2;
		 return newFitness;
	 }
	 static double calculatef24(ArrayList<Double> trialSolution) {
		 double newFitness = 0;
		 for (int i = 1; i < trialSolution.size(); ++i)
		 {
		 	newFitness += trialSolution.get(i) * trialSolution.get(i);
		 }

		 newFitness *= 10e6;
		 newFitness += trialSolution.get(0) * trialSolution.get(0);
		 return newFitness;
	 }
	 static double calculatef25(ArrayList<Double> trialSolution) {
		 double newFitness = 0;
		 double x1 = trialSolution.get(0); double x2 = trialSolution.get(1);
		 double x3 = trialSolution.get(2); double x4 = trialSolution.get(3);

		 newFitness =  100 * Math.pow((x1*x1 - x2),2) + Math.pow(x1-1,2) + 
		 Math.pow(x3-1,2) + 90 * Math.pow((x3*x3 - x4),2) + 10.1 * (Math.pow(x2-1, 2) + Math.pow(x4-1, 2)) + 
		 19.8*(x2-1)*(x4-1);
		 return newFitness;
	 }
	 static double calculatef26(ArrayList<Double> trialSolution) {
		 double newFitness = 0;
		 for (int i = 1 ; i < trialSolution.size(); ++i){	
		 	double xi		= trialSolution.get(i);
		 	double xi_1		= trialSolution.get(i-1);
		 	
		 	newFitness += i*(((2*xi*xi) - xi_1)*((2*xi*xi) - xi_1));      
		 }
		 newFitness += Math.pow(trialSolution.get(0) - 1,2);
		 return newFitness;
	 }
	 
	 

		
	 public static Double getBoundary(String t, int index) throws Exception {
		 switch(index) {
		 	case 0:
		 	case 1:
		 	case 9:
		 		if(t.equals("lower")) return -5.12;
		 		if(t.equals("upper")) return 5.12;
		 	case 2:
		 		if(t.equals("lower")) return -10.0;
		 		if(t.equals("upper")) return 10.0;
		 	case 3:
		 	case 4:
		 	case 6:
		 		if(t.equals("lower")) return -100.0;
		 		if(t.equals("upper")) return 100.0;
		 	case 5:
		 		if(t.equals("lower")) return -30.0;
		 		if(t.equals("upper")) return 30.0;
		 	case 7:
		 		if(t.equals("lower")) return -1.28;
		 		if(t.equals("upper")) return 1.28;
		 	case 8:
		 		if(t.equals("lower")) return -500.0;
		 		if(t.equals("upper")) return 500.0;
		 	case 10:
		 		if(t.equals("lower")) return -32.0;
		 		if(t.equals("upper")) return 32.0;
		 	case 11:
		 		if(t.equals("lower")) return -600.0;
		 		if(t.equals("upper")) return 600.0;
		 	case 12:
		 		if(t.equals("lower")) return -50.0;
		 		if(t.equals("upper")) return 50.0;
		 	case 13:
		 	case 15:
		 	case 16:
		 		if(t.equals("lower")) return -5.0;
		 		if(t.equals("upper")) return 5.0;
		 	case 14:
		 		if(t.equals("lower")) return -65.54;
		 		if(t.equals("upper")) return 65.54;
		 	case 17:
		 		if(t.equals("lower")) return -5.0;
		 		if(t.equals("upper")) return 15.0;
		 	case 18:
		 		if(t.equals("lower")) return -2.0;
		 		if(t.equals("upper")) return 2.0;

		 	case 19:
		 		if(t.equals("lower")) return -2.0;
		 		if(t.equals("upper")) return 2.0;
		 	case 20:
		 		if(t.equals("lower")) return -1.0;
		 		if(t.equals("upper")) return 1.0;
		 	case 21:
		 		if(t.equals("lower")) return -3.14;
		 		if(t.equals("upper")) return 3.14;
		 	case 22:
		 		if(t.equals("lower")) return -5.12;
		 		if(t.equals("upper")) return 5.12;
		 	case 23:
		 	case 24:
		 		if(t.equals("lower")) return -100.0;
		 		if(t.equals("upper")) return 100.0;
		 	case 25:
		 	case 26:
		 		if(t.equals("lower")) return -10.0;
		 		if(t.equals("upper")) return 10.0;
		 	
		 	default:
				if(FitnessFunctionManager.exists(index)) {
					if(t.equals("lower")) return FitnessFunctionManager.getBoundaryLower(index);
					if(t.equals("upper")) return FitnessFunctionManager.getBoundaryUpper(index);
				} 
		 }
		 return null;
	 }
	 
	 @SuppressWarnings("unchecked")
	public static String ffBoundariesJSON() throws Exception {
			JSONArray jarr = new JSONArray();
			
			for(int i = 0; i < numberOfHardCoded; i++) {
				JSONObject j = new JSONObject();		
				
				j.put("ffid", i);
				j.put("lower", FitnessFunction.getBoundary("lower", i));
				j.put("upper", FitnessFunction.getBoundary("upper", i));
								
				jarr.add(j);				
			}

			JSONObject json = new JSONObject();
			json.put("ffBoundaries", jarr);
			
			return json.toJSONString();
	 }
}
