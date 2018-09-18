package org.springbus.test;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbus.TimeUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NioTest {
    private int port = 9999;

    private static final Logger logger = LoggerFactory.getLogger(NioTest.class);
    private static AtomicInteger atomicInteger = new AtomicInteger(0);


    class BossTaskRun implements Runnable {
        private Selector bossSelector;

        public BossTaskRun(ServerSocketChannel serverSvr) throws IOException {
            bossSelector = Selector.open();
            serverSvr.socket().setReuseAddress(true);
            serverSvr.register(bossSelector, SelectionKey.OP_ACCEPT, serverSvr);
        }

        @Override
        public void run() {

            for (; ; ) {
                try {
                    bossSelector.select(1000);
                    Set<SelectionKey> keys = bossSelector.selectedKeys();
                    Iterator<SelectionKey> inters = keys.iterator();
                    while (inters.hasNext()) {

                        SelectionKey key = inters.next();
                        inters.remove();

                        if (!key.isValid()) {
                            key.cancel();
                            key.channel().close();
                            continue;
                        }
                        if (key.isAcceptable()) {
                            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                            SocketChannel clientChannel = channel.accept();
                            assign(clientChannel);
                        }


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    class WorkTaskRun implements Runnable {
        private Selector workSelector;
        private String name;

        public WorkTaskRun(String name) throws IOException {
            workSelector = Selector.open();
            this.name = name;

        }

        public void doSelector(SocketChannel clientChannel) throws IOException {
            clientChannel.configureBlocking(false);
            clientChannel.socket().setTcpNoDelay(true);
            clientChannel.socket().setReuseAddress(true);
            clientChannel.register(workSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, clientChannel);
            logger.info(" remote client " + clientChannel.getRemoteAddress().toString() + " connected ");
        }

        public void waveUp() {
            workSelector.wakeup();
        }


        @Override
        public String toString() {
            return name;
        }

        @Override
        public void run() {

            for (; ; ) {
                try {
                    logger.info(Thread.currentThread().getName() + " running ");
                    workSelector.select(1000);
                    Set<SelectionKey> keys = workSelector.selectedKeys();
                    if (keys.size() > 0) {
                        Iterator<SelectionKey> inters = keys.iterator();
                        while (inters.hasNext()) {

                            SelectionKey key = inters.next();
                            inters.remove();

                            if (!key.isValid()) {
                                key.cancel();
                                key.channel().close();
                                continue;
                            }


                            if (key.isReadable()) {
                                SocketChannel channel = (SocketChannel) key.channel();
                                ByteBuffer buffer = ByteBuffer.allocate(1024);
                                int len = channel.read(buffer);
                                logger.info("rev {} ", len);
                                if (len > 0) {
                                    buffer.flip();
                                    String str = new String(buffer.array(), 0, len);
                                    if (str.equals("q") || str.equals("Q")) {
                                        String time = "\n time is : " + TimeUtils.getTime() + "\n";
                                        channel.write(ByteBuffer.wrap(time.getBytes()));

                                    }
                                    if (str.equals("e") || str.equals("E")) {
                                        String time = "\n byte bye time is : " + TimeUtils.getTime() + "\n";
                                        channel.write(ByteBuffer.wrap(time.getBytes()));
                                        key.cancel();
                                        channel.close();

                                    } else {
                                        String msg = "\nbad msg  please press q to query time\n";
                                        channel.write(ByteBuffer.wrap(msg.getBytes()));
                                    }

                                } else if (len < 0) {
                                    key.cancel();
                                    logger.info(" remote client " + ((SocketChannel) key.channel()).getRemoteAddress() + " closed");
                                    key.channel().close();

                                }
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }


    private WorkTaskRun[] workTasks;

    private void initWork() throws IOException {

        int size = 1;//Runtime.getRuntime().availableProcessors() *8;
        workTasks = new WorkTaskRun[size];
        ThreadFactory threadFactory = new DefaultThreadFactory("nio-worker-pool");

        for (int i = 0; i < size; i++) {
            WorkTaskRun workTaskRun = new WorkTaskRun("worker-" + (i + 1));
            workTasks[i] = workTaskRun;
            threadFactory.newThread(workTaskRun).start();
        }

    }

    private void assign(SocketChannel clientChannel) throws IOException {
        if (clientChannel.isConnected()) {
            WorkTaskRun workTaskRun = workTasks[atomicInteger.getAndIncrement() % workTasks.length];
            logger.info("assign worker  {}  {} ", workTaskRun, atomicInteger.get());
            workTaskRun.doSelector(clientChannel);
            workTaskRun.waveUp();
        }

    }

    public static void main(String[] args) {
        NioTest test = new NioTest();
        test.startServer();
    }

    @Test
    public void startServer() {

        try {
            ServerSocketChannel serverSvr;
            serverSvr = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress(port);
            serverSvr.configureBlocking(false);
            serverSvr.socket().bind(address);
            logger.info("start server " + serverSvr);

            initWork();


            Thread t = new Thread(new BossTaskRun(serverSvr));
            t.setName("boss-thread");
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}