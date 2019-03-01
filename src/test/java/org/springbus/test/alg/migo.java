package org.springbus.test.alg;

/**
 * 迷宫
 */
public class migo {


    int migo[][] = {
            {2, 2, 2, 2, 2, 2, 2},
            {2, 0, 0, 0, 0, 0, 2},
            {2, 0, 2, 0, 2, 0, 2},
            {2, 0, 0, 0, 0, 2, 2},
            {2, 2, 0, 2, 0, 2, 2},
            {2, 0, 0, 0, 0, 0, 2},
            {2, 2, 2, 2, 2, 2, 2}};

    private  int[][] data(){
        return  migo;
    }

    int startX = 1, startY = 1;
    int endX = 5, endY = 5;

    int flag = 0;

    int find(int x, int y) {
        System.out.println("x="+x +" y="+y);
        migo[x][y] = 1;
        if (x == endX && y == endY)
            flag = 1;
        if (migo[x][y + 1] == 0 && flag != 1)
            find(x, y + 1);
        if (migo[x][y - 1] == 0 && flag != 1)
            find(x, y - 1);
        if (migo[x + 1][y] == 0 && flag != 1)
            find(x + 1, y);
        if (migo[x - 1][y] == 0 && flag != 1)
            find(x - 1, y);
        if (flag != 1)
            migo[x][y] = 0;
        return flag;
    }


    void main() {
        int i, j;
        System.out.print("显示迷宫：\n");
        for (i = 0; i < 7; i++) {
            for (j = 0; j < 7; j++)
                if (migo[i][j] == 2)
                    System.out.print("█");
                else
                    System.out.print(" ");
            System.out.print("\n");
        }

        if (find(startX, startY) == 0) {
            System.out.print("\n没有找到出口！\n");
        } else {
            System.out.print("\n显示路径:\n");
            for (i = 0; i < 7; i++) {
                for (j = 0; j < 7; j++) {
                    if (migo[i][j] == 2)
                        System.out.print("█");
                    else if (migo[i][j] == 1)
                        System.out.print("*");
                    else
                        System.out.print(" ");
                }
                System.out.print("\n");
            }
        }

    }

    public static void main(String[] args) {
        migo m=new migo();
        m.main();
        m.data();
        System.out.println("ok");
    }


}
