package org.springbus.test;


import org.springbus.agent.ExclusiveTime;

import java.io.IOException;

@ExclusiveTime
public class TestTime {

    public static void testTime() throws InterruptedException, IOException {
        Thread.sleep(1000);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        // Thread.sleep(2000);
        //TestTime.testTime();
        TestTime testTime = new TestTime();
        // new TestTime().getTest();
        testTime.doIt();
    }

    public int getTest() throws InterruptedException {
        Thread.sleep(500);
        return 0;
    }

    public void doIt() {
        System.out.println("do it");

    }

}
