package lab9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import edu.princeton.cs.algs4.Stopwatch;

public class Test {

	public static void main(String[] args) {
    	MyHashMap<String, Integer> b = new MyHashMap<String, Integer>();
    	Stopwatch sw = new Stopwatch();
    	System.out.println("Stopwatch starts...");
        for (int i = 0; i < 2000; i++) {
            b.put("hi" + i, i);
            b.get("hi" + i);
            //make sure put is working via containsKey and get
        }
        System.out.println("Done.");
        System.out.println(sw.elapsedTime());
        b.clear();
        assertEquals(0, b.size());

	}

}
