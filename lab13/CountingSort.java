/**
 * Class with 2 ways of doing Counting sort, one naive way and one "better" way
 * 
 * @author 	Akhil Batra
 * @version	1.1 - April 16, 2016
 * 
**/
public class CountingSort {
    
    /**
     * Counting sort on the given int array. Returns a sorted version of the array.
     *  does not touch original array (non-destructive method)
     * DISCLAIMER: this method does not always work, find a case where it fails
     *
     * @param arr int array that will be sorted
     * @return the sorted array
    **/
    public static int[] naiveCountingSort(int[] arr) {
        // find max
        int max = Integer.MIN_VALUE;
        for (int i : arr) {
            if (i > max) {
                max = i;
            }
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (int i : arr) {
            counts[i] += 1;
        }

        // put the value count times into a new array
        int[] sorted = new int[arr.length];
        int k = 0;
        for (int i = 0; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                sorted[k] = i;
            }
        }

        // return the sorted array
        return sorted;
    }

    public static int findMin(int[] arr){
    	int min=Integer.MAX_VALUE;
    	for(int i:arr){
    		if(i<min)
    			min=i;
    	}
    	return min;
    }
    
    public static int findMax(int[] arr){
    	int max=Integer.MIN_VALUE;
        for (int i : arr) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }


    /**
     * Counting sort on the given int array, must work even with negative numbers.
     * Note, this code does not need to work for ranges of numbers greater
     * than 2 billion.
     *  does not touch original array (non-destructive method)
     * 
     * @param toSort int array that will be sorted
    **/
    public static int[] betterCountingSort(int[] toSort) {
    	//find the minimum
    	int min=findMin(toSort);
    	if(min>=0){
    		return naiveCountingSort(toSort);
    	}
    	int max=findMax(toSort);
    	int[] negativeCounts=new int[-1*min+1];
    	for(int i:toSort){
    		if(i<=0){
    			negativeCounts[-1*i]+=1;
    		}
    	}
    	int[] sorted=new int[toSort.length];
    	int k=0;
    	for (int i=negativeCounts.length-1;i>=0;i--){
    		for(int j=0;j<negativeCounts[i];j++,k++){
    			sorted[k]=i*-1;
    		}
    	}
        if(max<=0){
        	return sorted;
        }
        int numOfPos=toSort.length-k;
    	int[] positiveArr=new int[numOfPos];
    	int l=0;
    	for(int j:toSort){
    		if(j>0){
    			positiveArr[l]=j;
    			l++;
    		}
    	}
    	int[] sortedPosArr=naiveCountingSort(positiveArr);
    	System.arraycopy(sortedPosArr, 0, sorted, k, sortedPosArr.length);
    	
    	
    	
    	
    	
        return sorted;
    }
}
