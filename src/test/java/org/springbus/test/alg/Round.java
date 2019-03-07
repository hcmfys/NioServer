package org.springbus.test.alg;

/**
 * 排列组合
 */
public class Round {

    private static int n = 0;


    private static void swap(int list[], int i, int j) {
        int c = list[j];
        list[j] = list[i];
        list[i] = c;
    }

    public static void perm(int list[], int k, int m) {

        if (k > m) {
            for (int i = 0; i <= m; i++)
                System.out.print(list[i]);
            System.out.print("\n");
            n++;
        } else {
            for (int i = k; i <= m; i++) {
                swap(list, k, i);
                perm(list, k + 1, m);
                swap(list, k, i);
            }
        }
    }

    public static void main(String[] args) {
        int list[] = {1, 2, 5};
        perm(list, 0, list.length - 1);
        System.out.print("total:" + n + "\n");
    }
}

