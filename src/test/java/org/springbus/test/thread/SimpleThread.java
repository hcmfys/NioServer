package org.springbus.test.thread;

import io.netty.channel.SingleThreadEventLoop;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;

public class SimpleThread extends SingleThreadEventLoop {

    private Logger  logger= LoggerFactory.getLogger(SimpleThread.class);
   private  static ThreadFactory threadFactory = new DefaultThreadFactory("wf-pool", 10);

    public  SimpleThread() {
        super(null, threadFactory, false);
    }


    @Override
    protected void run() {
        logger.info(" run excute ");

    }
}
