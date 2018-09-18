package org.springbus.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbus.TimeUtils;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioTestOneSelector {
    private int port = 9999;

    private static final Logger logger = LoggerFactory.getLogger(NioTestOneSelector.class);

    private  ExecutorService executorService;


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
                        }else {
                            if ( key.isAcceptable()) {
                                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                                SocketChannel clientChannel = channel.accept();
                                clientChannel.configureBlocking(false);
                                clientChannel.register(bossSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            }

                            if ( key.isReadable()) {
                                executorService.execute(new WorkTaskRun(key));
                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    class WorkTaskRun implements Runnable {
        private SelectionKey key;


        public WorkTaskRun(SelectionKey selectionKey) throws IOException {

            this.key = selectionKey;

        }


        @Override
        public void run() {
            try {
                SocketChannel channel = (SocketChannel) key.channel();

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int len = channel.read(buffer);

                if (len > 0) {
                    buffer.flip();
                    String str = new String(buffer.array(), 0, len);
                    logger.info("rev msg {}",str);
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
            } catch (Exception ex) {


                key.cancel();
                try {
                    key.channel().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }




    public static void main(String[] args) {
        NioTestOneSelector test = new NioTestOneSelector();
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

            executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*8);

            Thread t = new Thread(new BossTaskRun(serverSvr));
            t.setName("boss-thread");
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
