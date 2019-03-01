package org.springbus.test.alg;

import java.util.Arrays;

public class ShellSort {



    public static void main(String[] args) {
        int arr[] = {102, 533, 4, 35, 95, 166, 436, 33, 245, 31, 451, 673, 7241, 462,102};
        shellSort(arr );

        System.out.println(Arrays.toString(arr));
    }

    public static  void shellSort (int [] arr) {
        int N = arr.length;
        for (int gap = N / 2; gap > 0; gap /= 2) {
            System.out.println("gap="+gap);
            for (int i = gap; i < N; i++) {
                System.out.println("i="+i);
                insertI(arr,gap,i);
            }
        }

    }

    /**
     *
     * @param arr
     * @param gap
     * @param i
     */
    public static void insertI(int[] arr, int gap, int i) {


    }
}
