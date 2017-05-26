import java.util.Observable;

import edu.princeton.cs.algs4.Queue;
/**
 *  @author Josh Hug
 */

public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze; 
    private Queue<Integer> fringe;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;     
        fringe=new Queue<>();
    }

    /** Conducts a breadth first search of the maze starting at vertex x. */
    private void bfs(int s) {
        fringe.enqueue(s);
    	marked[s]=true;
    	announce();
    	while(!fringe.isEmpty()&& !targetFound){
    		int v=fringe.dequeue();
    		for(int w:maze.adj(v)){
    			if(!marked[w]){
    				fringe.enqueue(w);
    				marked[w]=true;
    				distTo[w]=distTo[v]+1;
    				announce();
    				edgeTo[w]=v;
    				if(w==t){
    					targetFound=true;
    					break;
    				}
    			}
    		}
    	}
    }


    @Override
    public void solve() {
         bfs(s);
    }
}

