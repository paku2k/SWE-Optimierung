package swe_mo.fitnessfunction;

import swe_mo.solver.de.Particle_DE;

public class DbgST {
	
	public static void main(String[] args) throws Exception {	
		try {
		//FitnessFunctionManager.add("f_{a}(x,n)=20*x_{3/45}*(23*a)+\\cos^2{abccos}+20\\prod_{a=0}^{[(n+1)-1]}((5+x_a-3)+(x_1/2\\cos 3)\\sqrt[(25x)]{2n})+x_3+ rand((a_{x-1}),1)"); 
		//FitnessFunctionManager.add("f_{a}(x,n)=20 / x_{3 /\\pi}"); 
		//FitnessFunctionManager.add("f_{a}(x,n)=(((|((n+1))|)))"); 
		//FitnessFunctionManager.add("f_5(x,n) = \\sum_{i=(n/2)}^{n-2} \\left( 100 \\left(x_{i+1}-x_i^2 \\right)^2 + (x_i - 1)^2 \\right)+ rand((a_{x-1}),1)"); 
		//FitnessFunctionManager.add("f_{tesat({a}c){{}}}^{(test(b))}(x,n)=\\prod_{a=0}^{n-1}((5 x_a-3)+\\sqrt{2n})+x_3");
		//FitnessFunctionManager.add("f_(x,n)=((a-b)c^c-d/e)");
		//FitnessFunctionManager.add(" f_{10}(x,n) = -20\\cdot e^{-0.2 \\sqrt{\\frac{1}{n} \\sum_{i=0}^{n-1} x_i^2}} -  e^{\\frac{1}{n} \\sum_{i=0}^{n-1} \\cos(2 \\pi x_i)} + 20 + e");
		//FitnessFunctionManager.add("f(x,n)=\\sum_{i=1}^{n-1} (x_i*2)");
		//FitnessFunctionManager.add("f_(x,n)=200.*x_1+x_2*2.+2.45*x_3*34.+x_0*3.n");
		//FitnessFunctionManager.add("f_2(x,n) = \\sum_{i=0}^{n-2} |x_{i+1}| + \\prod_{i=0}^{n-1} |x_i|");
		//FitnessFunctionManager.add("f_2(x,n) = 200.klas+xsadf2.+2.45x_asd34.+x3.y");

System.out.println("-------------------");
		
		System.out.println(FitnessFunctionManager.printTex(19));	
		System.out.println(FitnessFunctionManager.print(19));	

		System.out.println(FitnessFunctionManager.calculate(19, new Particle_DE(4,5,5)));
		System.out.println(FitnessFunctionManager.calculate(19, new Particle_DE(5,10,10)));
		System.out.println(FitnessFunctionManager.calculate(19, new Particle_DE(6,10,10)));
		} catch(Exception e) {
			throw e;
		}
	}	
}
