package puzzle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

import antlr.collections.List;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
		private ArrayList<Board> list;
		private MinPQ<Board> fringe; 
		private BoardComparators bc;
		private Board init;
		private LinkedList<Board> solution;
		private int moves;
		
		private class BoardComparators implements Comparator<Board>{
			@Override
			public int compare(Board o1, Board o2) {
				return o1.priority-o2.priority;
			}
			
		}
		/*Constructor which solves the puzzle, computing 
                 everything necessary for moves() and solution() to 
                 not have to solve the problem again. Solves the 
                 puzzle using the A* algorithm. Assumes a solution exists.*/
	    public Solver(Board initial){
	    	list=new ArrayList<>();
	    	bc=new BoardComparators();
	    	fringe=new MinPQ<>(bc);
	    	init=initial;
	    	solution=new LinkedList<>();
	    	moves=0;
	    	solve();
	    }
	    
	    public void solve(){
	    	fringe.insert(init);
	    	list.add(init);
	    	Board v=new Board(init.size());
	    	Board temp;
	    	ArrayList<Board> nb;
	    	while(!v.isGoal()){
	    		v=fringe.delMin();
	    		nb=v.neighbors();
	    		for (int i=0;i<nb.size();i++){
	    			temp=nb.get(i);
	    			if(!marked(temp)){
	    				if(temp.isGoal()){
	    					v=temp;
	    					break;
	    				}
	    				fringe.insert(temp);
	    				list.add(temp);
	    			}
	    		}
	    	}
	    	while(v.prev!=null){
	    		solution.addFirst(v);
	    		v=v.prev;
	    		moves++;
	    	}
	    	solution.addFirst(init);
	    }
	    
	    public boolean marked(Board x){
	    	return list.contains(x);
	    }
	    
	    //Returns the minimum number of moves to solve the initial board
	    public int moves(){
	    	return moves;
	    }
	    //Returns the sequence of Boards from the initial board to the solution.
	    public Iterable<Board> solution(){
	    	return solution;
	    }
		public boolean exists(int hash){
			return list.contains(hash);
		}
		
		
		
	
	
    // DO NOT MODIFY MAIN METHOD
    // Uncomment this method once your Solver and Board classes are ready.
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution()) {
            StdOut.println(board);
       }
    }

}
