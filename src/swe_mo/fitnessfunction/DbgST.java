package swe_mo.fitnessfunction;

import swe_mo.solver.de.Particle_DE;

public class DbgST {
	
	public static void main(String[] args) throws Exception {	
		try {
		//FitnessFunctionManager.add("f_{a}(x,n)=20x_{3\\pi}(23a)+\\cos^2{abccos}+20\\prod_{a=0}^{[(n+1)-1]}((5+x_a-3)+(x_1/2\\cos 3)\\sqrt[(25x)]{2n})+x_3"); 
		//FitnessFunctionManager.add("f_{a}(x,n)=(((|((a+1))|)))"); 
		//FitnessFunctionManager.add("f_5(x,n) = \\sum_{i=(n/2)}^{n-2} \\left( 100 \\left(x_{i+1}-x_i^2 \\right)^2 + (x_i - 1)^2 \\right)+ rand((a_{x-1}),1)"); 
		//FitnessFunctionManager.add("f_{tesat({a}c){{}}}^{(test(b))}(x,n)=\\prod_{a=0}^{n-1}((5 x_a-3)+\\sqrt{2n})+x_3");
		//FitnessFunctionManager.add("f_(x,n)=((a-b)c^c-d/e)");
		FitnessFunctionManager.add("f_(x,n)=\\frac{22}{23}");
		//FitnessFunctionManager.add("f_(x,n)=(\\log_{1}[1])");
		//FitnessFunctionManager.add("f_(x,n)=200.klas+xsadf2.+2.45x_asd34.+x3.y");
		//FitnessFunctionManager.add("f_2(x,n) = \\sum_{i=0}^{n-2} |x_{i+1}| + \\prod_{i=0}^{n-1} |x_i|");

System.out.println("-----");
		
		System.out.println(FitnessFunctionManager.printTex(19));	
		System.out.println(FitnessFunctionManager.print(19));	
		/*
		System.out.println(FitnessFunctionManager.calculate(19, new Particle_DE(4,10,10)));
		System.out.println(FitnessFunctionManager.calculate(19, new Particle_DE(5,10,10)));
		System.out.println(FitnessFunctionManager.calculate(19, new Particle_DE(6,10,10)));*/
		} catch(Exception e) {
			throw e;
		}
	}	
}
