package creatures;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.HugLifeUtils;
import huglife.Occupant;

public class Clorus extends Creature{

    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;
    /** probability of taking a move when ample space available. */
    private double moveProbability = 0.5;

    /** fraction of energy to retain when replicating. */
    private double repEnergyRetained = 0.5;
    /** fraction of energy to bestow upon offspring. */
    private double repEnergyGiven = 0.5;
    /** creates plip with energy equal to E. */
    
    public Clorus(double e){
    	super("clorus");
    	r=34;
    	g=0;
    	b=231;
    	energy=e;
    }
    
    public Clorus(){
    	this(1);
    }


    /** Called when this creature moves. */
    public void move(){
    	energy-=0.03;
    }

    /** Called when this creature attacks C. */
    public void attack(Creature c){
    	energy+=c.energy();
    }

    /** Called when this creature chooses replicate.
      * Must return a creature of the same type.
      */
    public Clorus replicate() {
        double babyEnergy = energy * repEnergyGiven;
        energy = energy * repEnergyRetained;
        return new Clorus(babyEnergy);
    }

    /** Called when this creature chooses stay. */
    public void stay(){
    	energy-=0.01;
    }

    /** Returns an action. The creature is provided information about its
     *  immediate NEIGHBORS with which to make a decision.
     *  If there are no empty squares, the Clorus will STAY (even if there are Plips 
     *  nearby they could attack).
		Otherwise, if any Plips are seen, the Clorus will ATTACK one of them randomly.
		Otherwise, if the Clorus has energy greater than or equal to one, it 
		will REPLICATE to a random empty square.
		Otherwise, the Clorus will MOVE to a random empty square.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors){
    	List<Direction> empties = getNeighborsOfType(neighbors, "empty");
    	List<Direction> plips = getNeighborsOfType(neighbors, "plip");
    	if (empties.size() == 0) {
    		return new Action(Action.ActionType.STAY);
    	}else if (plips.size()>0){
			Direction moveDir = HugLifeUtils.randomEntry(plips);
			return new Action(Action.ActionType.ATTACK, moveDir);
    	}else if (energy>=1){
    		Direction moveDir = HugLifeUtils.randomEntry(empties);
    		return new Action(Action.ActionType.REPLICATE, moveDir);
    	}
		Direction moveDir = HugLifeUtils.randomEntry(empties);
		return new Action(Action.ActionType.MOVE, moveDir);
    	
    	
    }


    public Color color() {
        return color(r, g, b);
    }
}
