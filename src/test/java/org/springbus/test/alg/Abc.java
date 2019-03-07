package org.springbus.test.alg;

import java.util.Arrays;

public class Abc {


    public static void main(String[] args) {
        abc(new char[]{'a', 'b', 'c'});
    }

    private static void abc(char[] list) {

        int len = list.length;
        for (int i = 0; i < len; i++) {
            round(list, list[i]);
        }


    }

    private static void round(char[] list, char a) {
        int size=list.length;
        if(size>0) {
            if(size==1) {
               char c= list[0];
               System.out.println(c);
            }else{
                if(size==2) {
                    System.out.println(  list[0]+""+list[1]);
                    System.out.print(  list[1]+""+list[0]);
                }else{
                    int newIndex=0;
                    char newCharList[]=new char[size-1];
                    for (int i = 0; i < size; i++) {
                        if(list[i]!=a) {
                            newCharList[newIndex++] =list[i];
                        }
                    }
                    if(newIndex>0)
                    round(newCharList,newCharList[0]);
                }
            }

        }

    }
}
