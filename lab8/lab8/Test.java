package lab8;

public class Test {

	public static void main(String[] args) {
    	BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        for (int i = 0; i < 10; i++) {
            b.put("hi" + i, 1);
            //make sure put is working via containsKey and get
            System.out.println(b.get("hi" + i)); 
            System.out.println("Does b contains key hi"+i+": "+b.containsKey("hi" + i));
        }
        b.clear();
        System.out.println(b.size());

	}

}
