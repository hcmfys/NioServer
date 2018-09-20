package org.springbus.test.thread;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssertTest {


    private  final static  Logger logger=LoggerFactory.getLogger(AssertTest.class);
    @Test
    public  void test(){

        assert  isOK();
        logger.info(" test true");
        assert  isError();
        logger.info(" test false");
    }

    private  boolean isOK(){
        return true;
    }


    private  boolean isError(){
        return false;
    }


    public  static  void main(String[] args) {
        AssertTest assertTest=new AssertTest();
        assertTest.test();
    }
}
