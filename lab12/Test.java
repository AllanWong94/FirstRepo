import edu.princeton.cs.algs4.Queue;


public class Test {
	public static void main(String[] args) {
		Queue<String> q=new Queue<>();
		q.enqueue("Michael");
		q.enqueue("Ray");
		q.enqueue("Peter");
		q.enqueue("Allan");
		MergeSort ms=new MergeSort();
		Queue<String> sortedQ=ms.mergeSort(q);
		System.out.println(sortedQ.toString());
	}
}
