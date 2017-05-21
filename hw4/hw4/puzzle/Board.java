package puzzle;

import java.util.ArrayList;
import java.util.Comparator;


public class Board {
	private int size;
	private int[][] tiles;
	private int movesMade;
	private int[][] goal;
	public int priority;
	public Board prev;
	
	public Board(int s){
		size=s;
		tiles=new int[s][s];
		size=tiles.length;
		prev=null;
		initializeGoal();		
		priority=movesMade+manhattan();
	}
	
	public Board(int[][] tiles, Board b) {
		this.tiles=tiles;
		size=tiles.length;
		prev=b;
		initializeGoal();
		priority=movesMade+manhattan();
	}
	
	//Constructs a board from an N-by-N array of tiles where 
	//tiles[i][j] = tile at row i, column j
 	public Board(int[][] tiles) {
		this.tiles=tiles;
		size=tiles.length;
		prev=null;
		initializeGoal();
		priority=movesMade+manhattan();
	}
	//Returns value of tile at row i, column j (or 0 if blank)
	public int tileAt(int row, int col){
		if(outOfBounds(row, col)){
			throw new java.lang.IndexOutOfBoundsException();
		}
		return tiles[row][col];
	}
	//Returns the board size N
	public int size(){
		return size;
	}
	//Hamming priority function defined above
	public int hamming(){
		int res=0;
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(tiles[i][j]!=0&&tiles[i][j]!=goal[i][j]){
					res++;
				}
			}
		}
		return res;
	}
	//Manhattan priority function defined above
	public int manhattan(){
		int res=0;
		int[] PosInTiles=new int[2];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(goal[i][j]!=0){
					PosInTiles=find(this.tiles, goal[i][j]);
					res+=Math.abs(PosInTiles[0]-i);
					res+=Math.abs(PosInTiles[1]-j);
				}
			}
		}
		return res;
	}
	/** Returns the string representation of the board. 
	 * Uncomment this method. */
	public String toString() {
		StringBuilder s = new StringBuilder();
		int N = size();
		s.append(N + "\n");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				s.append(String.format("%2d ", tileAt(i,j)));
			}
			s.append("\n");
		}
		s.append("\n");
		return s.toString();
	}
	//Returns true if this board's tile values are the same 
	//position as y's
	public boolean equals(Object y){
		if(this.getClass()==y.getClass()){
			if(y.toString().equals(this.toString()))
				return true;
		}
		return false;
	}
	//Returns true if is this board the goal board
	public boolean isGoal(){
		return (SameInt(goal));
	}
	public boolean SameInt(int[][] temptiles){
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(tiles[i][j]!=temptiles[i][j]){
					return false;
				}
			}
		}
		return true;
	}

	public int[] find(int[][] tiles, int k){
		int[] pos=new int[2];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(tiles[i][j]==k){
					pos[0]=i;
					pos[1]=j;
					return pos;
				}
			}
		}
		return new int[]{-1,-1};
	}

	public boolean outOfBounds(int i,int j){
		return(i<0||j<0||i>size-1||j>size-1);
	}


	public int[][] Up(int[][] temptiles){
		int[] pos=find(temptiles, 0);
		if(pos[0]>0){
			swap(temptiles, pos[0], pos[1], pos[0]-1, pos[1]);
		}
		return temptiles;
	}

	public int[][] Down(int[][] temptiles){
		int[] pos=find(temptiles, 0);
		if(pos[0]<size-1){
			swap(temptiles, pos[0], pos[1], pos[0]+1, pos[1]);
		}
		return temptiles;
	}

	public int[][] Left(int[][] temptiles){
		int[] pos=find(temptiles, 0);
		if(pos[1]>0){
			swap(temptiles, pos[0], pos[1], pos[0], pos[1]-1);
		}
		return temptiles;
	}

	public int[][] Right(int[][] temptiles){
		int[] pos=find(temptiles, 0);
		if(pos[1]<size-1){
			swap(temptiles, pos[0], pos[1], pos[0], pos[1]+1);
		}
		return temptiles;
	}

	public int[][] Clone(int[][] orig){
		int[][] copy=new int[size][size];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				copy[i][j]=orig[i][j];
			}
		}
		return copy;
	}
	
	public ArrayList<Board> neighbors(){
		ArrayList<Board> nb=new ArrayList<>();
		int[][] temp0=Clone(tiles);
		int[][] temp1=Clone(tiles);
		int[][] temp2=Clone(tiles);
		int[][] temp3=Clone(tiles);

		temp0=Up(temp0);
		if (!SameInt(temp0)){
			nb.add(new Board(temp0,this));
		}
		temp1=Down(temp1);
		if (!SameInt(temp1)){
			nb.add(new Board(temp1,this));
		}
		temp2=Left(temp2);
		if (!SameInt(temp2)){
			nb.add(new Board(temp2,this));
		}
		temp3=Right(temp3);
		if (!SameInt(temp3)){
			nb.add(new Board(temp3,this));
		}
		return nb;
	}

	
	public void displayNeighbors(){
		ArrayList<Board> neighbors=neighbors();
		System.out.println("Neighbors:  ");
		for (int i=0;i<neighbors.size();i++){
			System.out.println(neighbors.get(i).toString());
		}
	}


	public void swap(int[][] tiles, int row1, int col1, int row2, int col2){
		int temp=tiles[row1][col1];
		tiles[row1][col1]=tiles[row2][col2];
		tiles[row2][col2]=temp;
	}

	public void setMoves(int i){
		movesMade=i;
	}
	
	public static void main(String[] args){
		int[][] tiles={{3,2,1},{6,7,8},{4,5,0}};
		Board b=new Board(tiles);

	}

	public int getmovesMade(){
		return movesMade;
	}
	
	private void initializeGoal(){
		goal=new int[size][size];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				goal[i][j]=i*size+j+1;
			}
		}
		goal[size-1][size-1]=0;
	}

}
