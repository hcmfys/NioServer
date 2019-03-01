package org.springbus.test.alg;

import java.util.Arrays;

/**
 * 快速排序
 */
public class QuickSort {


    public static void main(String[] args) {
        int arr[] = {102, 533, 4, 35, 95, 166, 436, 33, 245, 31, 451, 673, 7241, 462};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));

    }

    /**
     * 快速排序
     * 总体思路是分段比较
     * 找出一个书来作为分段的标准
     * 把比标准小的移到左边，比标准大的移到右边
     * 让后继续递归分段比较
     * @param arr
     * @param start
     * @param end
     */
    public static void quickSort(int arr[], int start, int end) {

        if (start < end) {
            //找出一个标准来比较
            int stand = arr[start];
            System.out.println("start="+start +" Len="+arr.length +"  end="+end +" 比较标准="+stand);
            log(arr,start, end,"比较前");
            int low = start;
            int high = end;
            while(low<high) {
                //从后往前一直比较，发现比较小的就调换位置
                while (arr[high] >= stand && high > low) {
                    high--;
                }
                arr[low] = arr[high];
                //从前往后一直比较，发现比较小的就调换位置
                while (arr[low] < stand && low < high) {
                    low++;
                }
                arr[high] = arr[low];
            }

            //最后会相等 low = high
            arr[low] = stand;

            log(arr,start, end,"比较后");
            System.out.println("=====");
            quickSort(arr, start, low);
            quickSort(arr, low + 1, end);
        }
    }

    public  static  void log(int arr[], int start, int end, String msg) {
        StringBuilder sb=new StringBuilder();
        for(int i=start;i<end;i++) {
            sb.append(arr[i ]+",");
        }
        System.out.println("["+msg+"] arr=("+sb.toString()+")");

    }
}
