package org.springbus.test.alg;

import java.util.Arrays;

/**
 * 插入排序
 */
public class InsertSort {

    public static void main(String[] args) {
        int arr[] = {102, 533, 4, 35, 95, 166, 436, 33, 245, 31, 451, 673, 7241, 462,102};
        insertSort(arr );
        //int ll[]={102, 533};
        //int rr[]={4, 35};
        //merge(ll,rr);
        System.out.println(Arrays.toString(arr));
    }


    /**
     * @param arr
     */
    public static void insertSort(int[] arr) {

        for (int i=1;i<arr.length;i++){

            for(int j=0;j<i;j++){


                if(arr[j] > arr[i]) {
                    int temp=arr[j];
                    arr[j]=arr[i];
                    arr[i]=temp;
                }
            }

        }

    }


}
