package org.springbus.test.alg;

import java.util.Arrays;

public class MergeSort {

    /**
     * 归并排序（Merge Sort）<br/>
     * 归并排序是一个相当“稳定”的算法对于其它排序算法，比如希尔排序，快速排序和堆排序而言，这些算法有所谓的最好与最坏情况。<br/>
     * 而归并排序的时间复杂度是固定的，它是怎么做到的？<br/>
     * <p>
     * 两个有序数组的合并：<br/>
     * <p>
     * 首先来看归并排序要解决的第一个问题：两个有序的数组怎样合成一个新的有序数组：<br/>
     * <p>
     * 比如数组1｛ 3，5，7，8 ｝数组2为｛ 1，4，9，10 ｝：<br/>
     * <p>
     * 首先那肯定是创建一个长度为8的新数组咯，然后就是分别从左到右比较两个数组中哪一个值比较小，然后复制进新的数组中：比如我们这个例子：<br/>
     * <p>
     * ｛ 3，5，7，8 ｝ ｛ 1，4，9，10 ｝  ｛  ｝一开始新数组是空的。<br/>
     * <p>
     * 然后两个指针分别指向第一个元素，进行比较，显然，1比3小，所以把1复制进新数组中：<br/>
     * <p>
     * ｛ 3，5，7，8 ｝ ｛ 1，4，9，10 ｝  ｛ 1， ｝<br/>
     * <p>
     * 第二个数组的指针后移，再进行比较，这次是3比较小：<br/>
     * <p>
     * ｛ 3，5，7，8 ｝ ｛ 1，4，9，10 ｝  ｛ 1，3， ｝<br/>
     * <p>
     * 同理，我们一直比较到两个数组中有某一个先到末尾为止，在我们的例子中，<br/>
     * 第一个数组先用完。｛ 3，5，7，8 ｝ ｛ 1，4，9，10 ｝  ｛ 1，3，4，5，7，8 ｝<br/>
     * <p>
     * 最后把第二个数组中的元素复制进新数组即可。<br/>
     * <p>
     * ｛ 1，3，4，5，7，8，9，10 ｝<br/>
     * <p>
     * 由于前提是这个两个数组都是有序的，所以这整个过程是很快的，我们可以看出，<br/>
     * 对于一对长度为N的数组，进行合并所需要的比较次数最多为2 * N -1<br/>
     * <p>
     * 这其实就是归并排序的最主要想法和实现，归并排序的做法是:<br/>
     * <p>
     * 将一个数组一直对半分，问题的规模就减小了，再重复进行这个过程，直到元素的个数为一个时，一个元素就相当于是排好顺序的。<br/>
     * <p>
     * 接下来就是合并的过程了，合并的过程如同前面的描述。一开始合成两个元素，然后合并4个，8个这样进行。<br/>
     * <p>
     * 所以可以看到，归并排序是“分治”算法的一个经典运用。<br/>
     *
     * @param args
     */


    public static void main(String[] args) {
        int arr[] = {102, 533, 4, 35, 95, 166, 436, 33, 245, 31, 451, 673, 7241, 462};
        arr=mergeSort(arr, 0, arr.length-1 );
        //int ll[]={102, 533};
        //int rr[]={4, 35};
        //merge(ll,rr);
        System.out.println(Arrays.toString(arr));
    }


    public static int[] mergeSort(int[] array, int left, int right) {
        if (right == left) {
            return new int[]{array[left]};
        }
        int mid = (left + right) / 2;
        int l[] = mergeSort(array, left, mid);
        int r[] = mergeSort(array, mid + 1, right);
        return merge(l, r);
    }

    /**
     * @param l
     * @param r
     * @return
     */
    public static int[] merge(int[] l, int[] r) {

        //System.out.println("L="+Arrays.toString(l));
        //System.out.println("R="+Arrays.toString(r));
        int[] result = new int[l.length + r.length];
        int p = 0;
        int lp = 0;
        int rp = 0;
        while (lp < l.length && rp < r.length) {

            if (l[lp] <=r[rp]) {
                result[p++] = l[lp++];
            } else {
                result[p++] = r[rp++];
            }

        }
        //System.out.println("lp="+lp +  "  P="+ p+ "  -->" +l.length   +"  size="+result.length  +"  result="+Arrays.toString(result));
        while (lp < l.length) {
            result[p++] = l[lp++];
        }
        while (rp < r.length) {
            result[p++] = r[rp++];
        }

        System.out.println("L="+Arrays.toString(l )  +" R="+Arrays.toString(r)+ "   result="+Arrays.toString(result));
        return result;
    }
}
