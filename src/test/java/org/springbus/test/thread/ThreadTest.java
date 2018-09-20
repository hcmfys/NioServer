package org.springbus.test.thread;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ThreadTest {


    private Logger log = LoggerFactory.getLogger(ThreadTest.class);

    class TaskRun implements Runnable {
        private int index;
        private MultithreadEventExecutorGroup group;

        public TaskRun(int index, MultithreadEventExecutorGroup group) {
            this.index = index;
            this.group = group;
        }

        @Override
        public void run() {
            // Thread.currentThread().setDaemon(true);
            log.info(Thread.currentThread().isDaemon() + "--》" + Thread.currentThread().getName() + "--  execute ->"


                    + index);
        }
    }


    @Test

    public void testNioThread() throws Exception {


        NioEventLoopGroup threadGroup = new NioEventLoopGroup(10);


        for (int i = 1; i <= 100; i++) {

            Future f = threadGroup.submit(new TaskRun(i, threadGroup));
            f.addListener(future -> {
                log.info(" success " + future.get());
            });
        }
        Future<?> future = threadGroup.terminationFuture();

        future.await();


    }


    @Test

    public void testSimpleThreadNioLoop() throws Exception {

        SimpleThread simpleThread = new SimpleThread();


        for (int i = 1; i <= 100; i++) {
            simpleThread.submit(new TaskRun(i, null));

        }


    }


    @Test

    public void testThread() {


        DefaultEventExecutorGroup threadGroup = new DefaultEventExecutorGroup(10);

        for (int i = 1; i <= 100; i++) {

            threadGroup.execute(new TaskRun(i, threadGroup));
        }
        try {
            threadGroup.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
