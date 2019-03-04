package org.springbus.test.tree;



public class TreeMapApp {

    public  static  void main(String[] args){
        test();
    }

    private  static  void test(){
        TreeMap<Integer,Integer> map=new TreeMap<>();
        map.put(60, 60);
        map.put(10, 10);
        map.put(20, 30);
        map.put(20, 30);
        map.put(40, 40);
        map.put(50, 50);
        System.out.println(map);
    }
}
