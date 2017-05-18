import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;



public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] s=new String[]{"Hello","world","!"};
		
		FileHandler fh=new FileHandler("G:\\Workspace\\Git_Allan\\proj2\\editor\\1.txt", "C:\\Users\\Allan Wong\\Git_Allan\\proj2\\editor\\2.txt");
		System.out.println(fh.Read());
		fh.Save(s[0]);
	}

}
