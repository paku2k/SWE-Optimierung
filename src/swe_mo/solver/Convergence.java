package swe_mo.solver;
import java.io.IOException;
import java.math.*;
import java.util.ArrayList;

public class Convergence {
	
	
	public FileGenerator file;

	public double lastSum;
	public double sum;
	public int iterations;
	double maxGradient;
	
	public double convergence;
	public boolean print;
	
	public double lastBest;
	public double best;
	double maxGradientBest;
	int bestGradientCount;
	
	ArrayList<Double> floatingGradientBestMean=new ArrayList<Double>();


	public double gradientDecay;
	final double gradientSampleSize = 20.0;
	final int minObserveGenerations=200;
	ArrayList<Double> floatingGradientMean=new ArrayList<Double>();
	
	public Convergence(String ident, boolean print, double convergence) throws IOException {	
		if(convergence!=0.0) {
			this.gradientDecay = 0.001/convergence;
		}
		else {
			this.gradientDecay = 0.001;
		}
		
		this.print=print;
		this.convergence=convergence;
		
		if(print) {
			file=new FileGenerator(ident, "Generation;SumOfDifference;dSumOfDifference;Minimum;dMinimum" );			
		}

		lastSum=Double.MAX_VALUE;
		lastBest=Double.MAX_VALUE;
		
		sum=0.0;
		maxGradient=Double.MIN_VALUE;
		
		maxGradientBest=Double.MIN_VALUE;

		for(int i=0;i<gradientSampleSize;i++) {
			floatingGradientMean.add(i,0.0);
		}

		for(int i=0;i<gradientSampleSize;i++) {
			floatingGradientBestMean.add(i,0.0);
		}
	}
	
	public void closeFile() throws IOException {
		if(print){
			file.close();
		}
	}

	
	public boolean update(double sum, double best) throws IOException {
		if(iterations==0) {
			this.lastBest=best;
			this.lastSum=sum;
			this.iterations+=1;

			return false;
		}
		
		
		this.iterations+=1;
		this.sum=sum;
		this.best=best;
		
		double gradient = Math.abs(lastSum-this.sum);
		double gradientBest = Math.abs(lastBest-this.best);
		

		
		for(int i=floatingGradientMean.size()-2; i>=0; i--) {
			floatingGradientMean.set(i+1, floatingGradientMean.get(i));
		}
		
		for(int i=floatingGradientBestMean.size()-2; i>=0; i--) {
			floatingGradientBestMean.set(i+1, floatingGradientBestMean.get(i));
		}
		
		floatingGradientBestMean.set(0, gradientBest);

		
		floatingGradientMean.set(0, gradient);
		double gradientMean=0.0;
		for(int i=0;i<gradientSampleSize;i++) {
			gradientMean+=floatingGradientMean.get(i);
		}	
		gradientMean/=gradientSampleSize;
		
		
		double gradientMeanBest=0.0;

		for(int i=0;i<gradientSampleSize;i++) {
			gradientMeanBest+=floatingGradientBestMean.get(i);
		}	
		gradientMeanBest/=gradientSampleSize;
		
		
		
		if(gradientMean>maxGradient&&iterations<minObserveGenerations) {
			maxGradient=gradientMean;
		}
		if(gradientMeanBest>maxGradientBest&&iterations<minObserveGenerations) {
			maxGradientBest=gradientMeanBest;
		}
		
		//System.out.println(floatingGradientMean.toString());
		//System.out.println(gradientMean);
		
		if(print) {
			file.write(iterations+";"+sum+";"+gradientMean+";"+best+";"+gradientMeanBest);
		}
		
		if(gradientBest<maxGradientBest*gradientDecay) {
			bestGradientCount+=1;
			
		}
		else {
			bestGradientCount=0;
		}
		
		if (iterations>minObserveGenerations) {
			
			if(gradientMean<maxGradient*gradientDecay&&bestGradientCount>=20&&convergence!=0.0)
			{
				//System.out.println("Gradient converged to: "+gradientMean+" with the criteria being "+(gradientDecay*maxGradient)+" with max Gradient in the first "+minObserveGenerations+" iterations of: "+maxGradient);
				//System.out.println("Gradient of MinValue converged to: "+gradientMeanBest+" with the criteria being "+(gradientDecay*maxGradientBest)+" with max Gradient in the first "+minObserveGenerations+" iterations of: "+maxGradientBest);

				return true;
			}
		}
		
	
		this.lastSum=sum;
		this.lastBest=best;

		
		
		return false;
		
		
		
	}
	
	

}
