import org.junit.Test;
import static org.junit.Assert.*;
public class TestArrayDeque1B {
	
	public static void main(String[] args){
		ArrayDequeSolution<Integer> sad1 = new ArrayDequeSolution<Integer>();
		 StudentArrayDeque<Integer> sad2 = new StudentArrayDeque<Integer>();
		 assertEquals(sad1.isEmpty(), true);
		 sad1.addFirst(75);
		 sad1.addFirst(90);
		sad1.addFirst(5);
		sad1.addFirst(3);
		sad1.addFirst(4);
		sad1.addFirst(4);
		assertEquals((int)sad1.size(), 6);
		assertEquals((int)sad1.get(3), 5);
		 assertEquals(sad1.isEmpty(), false);
		sad1.printDeque();
		assertEquals((int)sad1.removeFirst(), 4);
		assertEquals((int)sad1.removeFirst(), 4);
		assertEquals((int)sad1.removeFirst(), 3);
		assertEquals((int)sad1.removeFirst(), 5);
		 assertEquals((int)sad1.removeFirst(), 90);
		 assertEquals((int)sad1.removeFirst(), 75);
		 sad1.addLast(75);
		 sad1.addLast(90);
		 org.junit.Assert.assertEquals(sad1.isEmpty(), false);
		 assertEquals((int)sad1.removeLast(), 90);
		 assertEquals((int)sad1.removeLast(), 75);
		 assertEquals(sad1.isEmpty(), true);
		 sad1.printDeque();
		 System.out.println("Test passed!");
	}
}
