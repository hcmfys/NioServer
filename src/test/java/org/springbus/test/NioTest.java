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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class NioTest {
    private int port = 8188;


    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    class BossTaskRun implements Runnable {
        private Selector bossSelector;
        private  boolean restart=true;


        public BossTaskRun(ServerSocketChannel serverSvr) throws IOException {
            bossSelector = Selector.open();
            serverSvr.socket().setReuseAddress(true);
            restart=false;
            bossSelector.wakeup();
            serverSvr.register(bossSelector, SelectionKey.OP_ACCEPT, this);
            restart=true;
            System.out.println(" success  to create selector ");
        }

        @Override
        public void run() {

            while (true) {
                try {

                    while (restart) {
                        System.out.println(Thread.currentThread().getName() + " accept  running ");
                        int op = bossSelector.select(1000);
                        if (op == 0) continue;
                        System.out.println(Thread.currentThread().getName() + "  op = " + op);
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
                    Thread.sleep(10);

                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }


        }
    }

    class WorkRunInfo {
        public WorkTaskRun getTask() {
            return task;
        }

        public void setTask(WorkTaskRun task) {
            this.task = task;
        }

        private  WorkTaskRun task;
        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

        private String  hostName;
    }

    class WorkTaskRun implements Runnable {
        private Selector workSelector;
        private String name;

        public WorkTaskRun(String name) throws IOException {
            workSelector = Selector.open();
            this.name = name;

        }



        private  boolean restart=true;

        public void doSelector(SocketChannel clientChannel) throws IOException {

            clientChannel.configureBlocking(false);
            restart=false;
            workSelector.wakeup();
            WorkRunInfo workRunInfo=new WorkRunInfo();
            workRunInfo.setTask(this);
            workRunInfo.setHostName(clientChannel.getRemoteAddress().toString());
            clientChannel.register(workSelector, SelectionKey.OP_READ, workRunInfo);
            restart=true;
            System.out.println(" remote client " + clientChannel.getRemoteAddress().toString() + " connected ");
        }


        @Override
        public String toString() {
            return name;
        }

        @Override
        public void run() {

            while (!Thread.interrupted()) {
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
                                if (  key.isValid() && key.isReadable()) {
                                    //handler(key);
                                    executorService.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            handler(key);
                                        }
                                    });
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

    private  void handler(SelectionKey key ) {

        WorkRunInfo workRunInfo = (WorkRunInfo) key.attachment();
        String threadName=  Thread.currentThread().getName();
        try {

            SocketChannel channel = (SocketChannel) key.channel();
            if(!channel.isOpen()) {
                key.attach(null);
                key.cancel();
                return;
            }
            byte[] bytes = new byte[128];
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            int len = channel.read(buffer);
            System.out.println( threadName + "rev  size=  " + len);
            if (len > 0) {
                buffer.flip();
                String str = new String(buffer.array(), 0, len);
                System.out.println( threadName + "rev  data >>  " + str);
                if (str.equals("q") || str.equals("Q")) {
                    String time = "\n time is : " + TimeUtils.getTime() + "\n";
                    channel.write(ByteBuffer.wrap(time.getBytes()));

                }
                if (str.equals("ex") || str.equals("E")) {
                    String time = "\n byte bye time is : " + TimeUtils.getTime() + "\n";
                    channel.write(ByteBuffer.wrap(time.getBytes()));
                    key.cancel();
                    channel.close();
                } else {
                    String msg = "\nbad msg  please press q to query time\n";
                    channel.write(ByteBuffer.wrap(msg.getBytes()));
                }
                buffer.clear();

            } else if (len < 0) {
                key.cancel();
                System.out.println(threadName + " remote client " + workRunInfo.getHostName() + " closed");
                key.channel().close();

            }
        } catch (Exception ex) {
            key.cancel();
            System.out.println(threadName+ " read data error   " + workRunInfo.getHostName() + " close connection ");

            try {
                key.channel().close();
            } catch (IOException e) {
                e.printStackTrace();
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
