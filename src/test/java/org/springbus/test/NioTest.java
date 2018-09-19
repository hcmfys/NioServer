package org.springbus.test;


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
import java.util.concurrent.atomic.AtomicInteger;

public class NioTest {
    private int port = 8013;


    private static AtomicInteger atomicInteger = new AtomicInteger(0);


    class BossTaskRun implements Runnable {
        private Selector bossSelector;
        private  boolean restart=true;

        public void setRestart(boolean restart){
            this.restart=restart;
        }

        public  void waveUp() {

        }

        public BossTaskRun(ServerSocketChannel serverSvr) throws IOException {
            bossSelector = Selector.open();
            serverSvr.socket().setReuseAddress(true);
            serverSvr.register(bossSelector, SelectionKey.OP_ACCEPT, this);
            System.out.println(" success  to create selector ");
        }

        @Override
        public void run() {

            for (; ; ) {
                try {

                    while (restart) {
                        System.out.println(Thread.currentThread().getName() + " accept  running ");
                        int op = bossSelector.select(1000);
                        if (op == 0) continue;
                        System.out.println(Thread.currentThread().getName() + "  op ={} " + op);
                        Set<SelectionKey> keys = bossSelector.selectedKeys();
                        if (keys.size() <= 0) continue;
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
                    }

                    } catch(Exception e){
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

        private  boolean restart=true;

        public void setRestart(boolean restart){
            this.restart=restart;
        }

        public  void waveUp() {

            restart = true;
        }

        public void doSelector(SocketChannel clientChannel) throws IOException {

            clientChannel.configureBlocking(false);
            setRestart(false);
            clientChannel.register(workSelector, SelectionKey.OP_READ, this);
            workSelector.wakeup();
            setRestart(true);
            System.out.println(" remote client " + clientChannel.getRemoteAddress().toString() + " connected ");
        }


        @Override
        public String toString() {
            return name;
        }

        @Override
        public void run() {

            for (; ; ) {
                try {
                    while (restart) {
                        System.out.println(Thread.currentThread().getName() + " running ");
                        int op = workSelector.select(1000);
                        if (op == 0) continue;
                        System.out.println(Thread.currentThread().getName() + "  op =" + op);
                        Set<SelectionKey> keys = workSelector.selectedKeys();
                        {
                            Iterator<SelectionKey> inters = keys.iterator();
                            while (inters.hasNext()) {
                                SelectionKey key = inters.next();
                                inters.remove();


                                if (!key.isValid()) {
                                    System.out.println(" remote client " + ((SocketChannel) key.channel()).getRemoteAddress() + " closed");
                                    key.cancel();
                                    key.channel().close();

                                    continue;
                                }


                                if (key.isReadable()) {
                                    try {
                                        SocketChannel channel = (SocketChannel) key.channel();
                                        ByteBuffer buffer = ByteBuffer.allocate(256);
                                        int len = channel.read(buffer);
                                        System.out.println("rev {} " + len);
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
                                            System.out.println(" remote client " + ((SocketChannel) key.channel()).getRemoteAddress() + " closed");
                                            key.channel().close();

                                        }
                                    } catch (Exception ex) {

                                        System.out.println(" read data error close connection \n");
                                        System.out.println(" remote client " + ((SocketChannel) key.channel()).getRemoteAddress() + " closed");
                                        key.cancel();
                                        key.channel().close();
                                    }
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }


    private WorkTaskRun[] workTasks;

    private void initWork() throws IOException {

        int size = Runtime.getRuntime().availableProcessors() ;
        workTasks = new WorkTaskRun[size];


        for (int i = 0; i < size; i++) {
            WorkTaskRun workTaskRun = new WorkTaskRun("worker-" + (i + 1));
            workTasks[i] = workTaskRun;
            Thread t = new Thread(workTaskRun);
            t.setName("worker-" + (i + 1));
            t.start();
        }

    }

    private void assign(SocketChannel clientChannel) throws IOException {

        WorkTaskRun workTaskRun = workTasks[atomicInteger.getAndIncrement() % workTasks.length];
        System.out.println("assign worker  "+clientChannel +" to "+  workTaskRun.name);
        workTaskRun.doSelector(clientChannel);



    }

    public static void main(String[] args) {
        NioTest test = new NioTest();
        test.startServer();
    }


    public void startServer() {

        try {
            ServerSocketChannel serverSvr;
            serverSvr = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress(port);
            serverSvr.configureBlocking(false);
            serverSvr.bind(address);
            System.out.println("start server " + serverSvr);

            initWork();


            Thread t = new Thread(new BossTaskRun(serverSvr));
            t.setName("boss-thread");
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
