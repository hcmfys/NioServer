package org.springbus.test.alg;

import java.util.Arrays;

public class BubbleSort {

    public static void main(String[] args) {

        int arr[] = {102, 533, 4, 35, 95, 166, 436, 33, 245, 31, 451, 673, 7241, 462};
        bubbleSort(arr);
        System.out.println(Arrays.toString(arr));

    }

    /**
     * 冒泡排序
     * 就是2个直接一个个往后排序
     * 大的数据放在后面
     * O = n * n
     * @param arr
     */
    public static  void bubbleSort(int arr[]) {
        for(int i=0;i<arr.length-1;i++) {
            int end = arr.length - 1 - i;
            for (int j = 0; j < end; j++) {
                if(arr[j]>arr[j+1]) {
                    int temp = arr[j+1];
                    arr[j+1]=arr[j];
                    arr[j]=temp;
                }

            }
        }

    }

}
