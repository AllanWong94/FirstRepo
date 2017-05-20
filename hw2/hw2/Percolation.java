package hw2;                       

import java.net.ConnectException;
import java.util.ArrayList;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private int size;
	private int numOfOpenSites;
	private Site[][] sites;
	public WeightedQuickUnionUF uf;
	private int firstTopRowSite;
	private ArrayList<Double> AllEmpties;
	public enum SITE_STATUS{
		SITE_EMPTY_OPEN,
		SITE_BLOCKED,
		SITE_FILLED_OPEN
	}
	private class Site{
		private SITE_STATUS ss;
		private int row;
		private int col;
		private int size;
		public Site(){
			ss=SITE_STATUS.SITE_BLOCKED;
			row=-1;
			col=-1;
		}
		public Site(int Row,int Col,int Size){
			row=Row;
			col=Col;
			size=Size;
			ss=SITE_STATUS.SITE_BLOCKED;
		}
		public Site(SITE_STATUS s){
			ss=s;
		}
		public SITE_STATUS getSiteStatus(){
			return ss;
		}
		public void setSiteStatus(SITE_STATUS s){
			ss=s;
		}
		public boolean open(){
			if(ss==SITE_STATUS.SITE_BLOCKED){
				ss=SITE_STATUS.SITE_EMPTY_OPEN;
				if(row==0)
					ss=SITE_STATUS.SITE_FILLED_OPEN;
				return true;
			}
			return false;
		}
		public void fill(){
			if(ss==SITE_STATUS.SITE_EMPTY_OPEN)
				ss=SITE_STATUS.SITE_FILLED_OPEN;
		}
		public boolean isOpen(){
			return ss==SITE_STATUS.SITE_EMPTY_OPEN||ss==SITE_STATUS.SITE_FILLED_OPEN;
		}
		
	};

	
	// create N-by-N grid, with all sites initially blocked
	public Percolation(int N){
		if (N<=0)
			throw new java.lang.IllegalArgumentException();
		sites=new Site[N][];
		for (int k=0;k<N;k++){
			sites[k]=new Site[N];
		}
		numOfOpenSites=0;
		for (int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				sites[i][j]=new Site(i,j,N);
			}
		}
		size=N;
		uf = new WeightedQuickUnionUF(N*N);//sites[i][j] is represented by uf[i*N+j-1]
		firstTopRowSite=-1;
		AllEmpties=new ArrayList();
	}
		// open the site (row, col) if it is not open already
	   public void open(int row, int col){
		   if (outOfBounds(row, col))
				throw new java.lang.IllegalArgumentException("Row: "+row+"\nColumn: "+col);
		   if(sites[row][col].open())
			   numOfOpenSites++;
		   AllEmpties.add((double)row*size+col);
		   if(row==0){
			   if(firstTopRowSite==-1){
				   firstTopRowSite=col;
			   }
			   addFull(row, col);
		   }
				if (connectedToFull(row, col)){
					addFull(row, col);
				}
	   }
	   // is the site (row, col) open?
	   public boolean isOpen(int row, int col) throws java.lang.IllegalArgumentException{
		   if (outOfBounds(row, col))
				throw new java.lang.IllegalArgumentException();
		   return sites[row][col].isOpen();
	   }
	   // is the site (row, col) full?
	   public boolean isFull(int row, int col) throws java.lang.IllegalArgumentException{
		   if (outOfBounds(row, col))
				throw new java.lang.IllegalArgumentException();
		   return sites[row][col].getSiteStatus()==SITE_STATUS.SITE_FILLED_OPEN;
	   }
	   // number of open sites
	   public int numberOfOpenSites(){
		   return numOfOpenSites;
	   }
	   // does the system percolate?
	   public boolean percolates(){
		   for(int i=0;i<size;i++){
			   if(firstTopRowSite!=-1){
				   if (uf.connected(firstTopRowSite, (size-1)*size+i)){
					   return true;
				   }
			   }
			   
		   }
		   return false;
	   }
	   public boolean outOfBounds(int x,int y){
		   return (x<0||y<0||x>=size||y>=size);
	   }
	   // unit testing (not required)
	   public static void main(String[] args)   {
		   
	   }
	   
	   public void update(){
		   for(int i=0;i<AllEmpties.size();i++){
			   int index = (new Double(AllEmpties.get(i))).intValue();
			   int col=index%size;
			   int row=(index-col)/size;
			   if(connectedToFull(row, col)){
				   addFull(row, col);
			   }
		   }
	   }
	   public void addFull(int row,int col){
		   sites[row][col].fill();
		   AllEmpties.remove(0.0+row*size+col);
		   uf.union(firstTopRowSite, row*size+col);
		   update();
	   }
	   
	   public boolean connectedToFull(int row,int col){
		   boolean[] sur=new boolean[4];
		   for (int i=0;i<4;i++){
			   try {
					   switch (i) {
					case 0:
						sur[i]=isFull(row-1, col);
						break;
					case 1:
						sur[i]=isFull(row+1, col);
						break;
					case 2:
						sur[i]=isFull(row, col-1);
						break;
					case 3:
						sur[i]=isFull(row, col+1);
						break;
					}
			   }
		   	 	catch (java.lang.IllegalArgumentException e) {
			   // TODO: handle exception
		   	 	} 
		   }
		   return sur[0]||sur[1]||sur[2]||sur[3];
	   }
	   
};           
