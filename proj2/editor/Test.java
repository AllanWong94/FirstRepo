import java.util.ArrayList;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;



public class Test {

	public static void main(String[] args) {
		UndoArray<String> al=new UndoArray<String>(5);
		al.add("H");
		al.add("He");
		al.add("Hel");
		al.add("Hell");
		al.add("Hello");
		al.setCursor(3);
		al.removeToLast();
		al.print();
	}

}
