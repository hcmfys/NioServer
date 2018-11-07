package org.springbus.test.target;

public class Student implements Person {

    @Override
    public void sayHello(String content, int age) {

        System.out.println("student say hello" + content + " " + age);
    }

    @Override
    public void sayGoodBye(boolean seeAgin, double time) {

        System.out.println("student sayGoodBye " + time + " " + seeAgin);
    }


}
