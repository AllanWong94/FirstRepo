package hw2;                       
import java.util.Random;


import edu.princeton.cs.introcs.Stopwatch;


public class PercolationStats {
    public int n=10;          
    public int t=100000;
    public double[] fractions;
	
    public PercolationStats(int N, int T) {
    	if (N<=0||T<=0){
    		throw new java.lang.IllegalArgumentException();
    	}
    	n=N;
    	t=T;
    	fractions=new double[t];
    }
    
    public void display(){
    	Stopwatch sw=new Stopwatch();
        for (int i=0;i<t;i++){
        	Percolation p=new Percolation(n);
			while(!p.percolates()){
				randomOpen(p);
			}
			double f=(double)p.numberOfOpenSites()/(double)(n*n);
			fractions[i]=f;
        }
        System.out.println("Sample mean: "+mean(fractions));
        System.out.println("Sample standard deviation: "+stddev(fractions));
        System.out.println("Sample confidence low: "+confidenceHigh(fractions));
        System.out.println("Sample confidence high: "+confidenceLow(fractions));
        System.out.println(sw.elapsedTime());
    }
    
	public static void main(String[] args){
		PercolationStats ps=new PercolationStats(10, 100);
		ps.display();
        
	}
	 // low  endpoint of 95% confidence interval
	public double confidenceLow(double[] a){
		double res=0;
		res=mean(a)-1.96*stddev(a)/Math.sqrt(a.length);
		return res;
	}
	
	// high endpoint of 95% confidence interval
	public double confidenceHigh(double[] a){
		double res=0;
		res=mean(a)+1.96*stddev(a)/Math.sqrt(a.length);
		return res;
		
	}
	
	public static double stddev(double[] a){
		double u=mean(a);
		double res=0;
		for(int i=0;i<a.length;i++){
			res+=(a[i]-u)*(a[i]-u);
		}
		res/=(a.length-1);
		return res;
	}
	
	public static double mean(double[] a){
		double sum=0;
		for(int i=0;i<a.length;i++){
			sum+=a[i];
		}
		return sum/a.length;
	}
	
	public static void randomOpen(Percolation p){
        int max=9;
		Random random = new Random();
		int row=random.nextInt(max+1);
		int col=random.nextInt(max+1);
		p.open(row, col);
	}
}                       
