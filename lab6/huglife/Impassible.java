package huglife;
import java.awt.Color;

public class Impassible extends Occupant {
    public Impassible() {
        super("impassible");
    }

    /** Returns hardcoded black */
    public Color color() {
    	System.out.println("Hi");
        return color(100, 100, 100);
    }    
}