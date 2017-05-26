import edu.princeton.cs.algs4.Queue;

public class QuickSort {
    /**
     * Returns a new queue that contains the given queues catenated together.
     *
     * The items in q2 will be catenated after all of the items in q1.
     */
    private static <Item extends Comparable> Queue<Item> catenate(Queue<Item> q1, 
    		Queue<Item> q2, Queue<Item> q3) {
        Queue<Item> catenated = new Queue<Item>();
        for (Item item : q1) {
            catenated.enqueue(item);
        }
        for (Item item: q2) {
            catenated.enqueue(item);
        }
        for (Item item: q3) {
            catenated.enqueue(item);
        }
        return catenated;
    }

    /** Returns a random item from the given queue. */
    private static <Item extends Comparable> Item getRandomItem(Queue<Item> items) {
        int pivotIndex = (int) (Math.random() * items.size());
        Item pivot = null;
        // Walk through the queue to find the item at the given index.
        for (Item item : items) {
            if (pivotIndex == 0) {
                pivot = item;
                break;
            }
            pivotIndex--;
        }
        return pivot;
    }

    /**
     * Partitions the given unsorted queue by pivoting on the given item.
     *
     * @param unsorted a Queue of unsorted items
     * @param pivot the item to pivot on
     * @param less an empty Queue. When the function completes, this queue will contain
     *             all of the items in unsorted that are less than the given pivot.
     * @param equal an empty Queue. When the function completes, this queue will contain
     *              all of the items in unsorted that are equal to the given pivot.
     * @param greater an empty Queue. When the function completes, this queue will contain
     *                all of the items in unsorted that are greater than the given pivot.
     */
    private static <Item extends Comparable> void partition(
            Queue<Item> unsorted, Item pivot, Queue<Item> less,
            Queue<Item> equal, Queue<Item> greater) {
    	if(unsorted.size()<=1)
    		return;
    	for(Item item:unsorted){
    		int res=item.compareTo(pivot);
    		if(res<0){
    			less.enqueue(item);
    		}
    			
    		else if(res>0){
    			greater.enqueue(item);
    		}else{
    			equal.enqueue(item);
    		}
    	}
    	Queue<Item> next_equal=new Queue<>();
    	Queue<Item> next_less=new Queue<>();
    	Queue<Item> next_greater=new Queue<>();
    	Item pvt;
    	pvt=getRandomItem(less);
    	partition(less, pvt, next_less, next_equal, next_greater);
    	less=catenate(next_less, next_equal, next_greater);
    	next_equal=new Queue<>();
    	next_less=new Queue<>();
    	next_greater=new Queue<>();
    	pvt=getRandomItem(greater);
    	partition(greater, pvt, next_less, next_equal, next_greater);
    	greater=catenate(next_less, next_equal, next_greater);
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> quickSort(
            Queue<Item> items) {
    	if(items.size()==1){
    		return items;
    	}
    	Queue<Item> equal=new Queue<>();
    	Queue<Item> less=new Queue<>();
    	Queue<Item> greater=new Queue<>();
    	Item pivot=getRandomItem(items);
    	partition(items, pivot, less, equal, greater);
    	items=catenate(less, equal, greater);
        return items;
    }
    
    
	public static void main(String[] args) {
		Queue<String> q=new Queue<>();
		q.enqueue("Michael");
		q.enqueue("Ray");
		q.enqueue("Peter");
		q.enqueue("Allan");
		QuickSort qs=new QuickSort();
		Queue<String> sortedQ=qs.quickSort(q);
		System.out.println(sortedQ.toString());
	}
    
    
}
