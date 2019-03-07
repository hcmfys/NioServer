package org.springbus.test.alg;

import java.util.Arrays;

/**
 *
 */
public class RadixSort {

    public static void main(String[] args) {
        int arr[] = {102, 533, 4, 35, 95, 166,9999, 0,436,5643, 33, 245, 31, 451, 673, 7241, 462,1234556789};
        radixSort(arr);
        System.out.println(Arrays.toString(arr));
        getRadixLength(1245);
        getLoopTimes(1245);
      long f=  Long.parseLong("3113298282689536");
      System.out.println(f);

    }

    public static void radixSort(int arr[]) {
        int max=getMax(arr);
        int len=getRadixLength(max);
        int temp[][]=new int[10][arr.length];
        int counts[]=new int[10];

        for(int i=0;i<len;i++) {
            for (int k = 0; k < arr.length; k++) {
                int q = arr[k];
                int r = q / ((int) Math.pow(10, i)) % 10;
                temp[r][counts[r]] = q;
                counts[r] +=1;
            }
            int index = 0;
            for (int k = 0; k < 10; k++) {
                for (int j = 0; j <counts[k]; j++) {
                    arr[index++] = temp[k][j];
                }
                counts[k]=0;
            }
        }


    }

    //获取数字的位数
   public static  int getLoopTimes(int num) {
       int count = 1;
       int temp = num / 10;
       while (temp != 0) {
           count++;
           temp = temp / 10;
       }
       System.out.println(" getLoopTimes length="+count);
       return count;
   }

    private  static  int getRadixLength(int max){
        int i=1;
        for( ; max >10;max=max/ 10) {
            i++;
        }
        System.out.println("length="+i);
        return  i;
    }

    /**
     *
     * @param arr
     * @return
     */
    private static  int getMax(int [] arr){
        int len=arr.length;
        int max=Integer.MIN_VALUE;
        for(int i=0;i<len;i++){
            if(max<arr[i]){
                max=arr[i];
            }
        }
        return  max;
    }


}



