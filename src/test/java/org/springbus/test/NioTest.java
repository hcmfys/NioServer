package org.springbus.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NioTest {

    private  static  int port = 8088;

    ReentrantReadWriteLock readWriteLock =new ReentrantReadWriteLock();
private Logger  logger= LoggerFactory.getLogger( NioTest.class);

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
            logger.info(" success  to create selector ");
        }

        @Override
        public void run() {

            while (!Thread.interrupted()) {
                try {

                    while (restart) {
                        //System.out.println(Thread.currentThread().getName() + " accept  running ");
                        int op = bossSelector.select(1000);
                        if (op == 0) continue;
                        logger.info(Thread.currentThread().getName() + "  op = " + op);
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
                        keys.clear();
                    }
                    Thread.sleep(10);

                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }


        }
    }

    class WorkRunInfo {
         WorkRunInfo(){
             msgBuf=ByteBuffer.allocate(5);

         }

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


        public  volatile  ByteBuffer msgBuf;
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
            logger.info(" remote client " + clientChannel.getRemoteAddress().toString() + " connected ");
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
                       // System.out.println(Thread.currentThread().getName() + " running ");
                        int op = workSelector.select(1000);
                        if (op == 0) continue;
                        //System.out.println(Thread.currentThread().getName() + "  op =" + op);
                        Set<SelectionKey> keys = workSelector.selectedKeys();

                            Iterator<SelectionKey> inters = keys.iterator();
                            while (inters.hasNext()) {
                                SelectionKey key = inters.next();
                                inters.remove();
                                if (  key.isValid() && key.isReadable()) {
                                     handler(key);
                                   // executorService.execute(() -> handler(key));
                                }
                            }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public ByteBuffer  appendBuffer(ByteBuffer src,ByteBuffer to) {

        int pos = to.position();
        int cap = to.capacity();
        if (pos + src.limit() > cap) {
            int newSize = 2 * cap + src.limit();
            ByteBuffer newBuf = ByteBuffer.allocate(newSize);
            newBuf.put(to.array(), 0, to.limit());
            to.clear();
            to = newBuf;
        }

        to.put(src.array(), 0, src.limit());
        return to;
    }

    private  void handler(SelectionKey key ) {

        WorkRunInfo workRunInfo = (WorkRunInfo) key.attachment();
        String threadName=  Thread.currentThread().getName();
        try {

            SocketChannel channel = (SocketChannel) key.channel();
            if(!channel.isOpen()) {
             throw  new Exception("close channel");
            }


            ByteBuffer msgBuf=ByteBuffer.allocate(5);

            int len = channel.read(msgBuf);





            if (len > 0) {

                msgBuf.flip();


                if( workRunInfo.msgBuf.position() +len >   workRunInfo.msgBuf.capacity() ) {
                    workRunInfo.msgBuf=  appendBuffer(msgBuf, workRunInfo.msgBuf);
                }else{
                    workRunInfo.msgBuf.put(msgBuf);
                }


                int pos=workRunInfo.msgBuf.position();
                int limit=workRunInfo.msgBuf.limit();
                workRunInfo.msgBuf.flip();
                int nextIndex=0;
                List<String> lineList=new ArrayList<>();
                for(int i=0;i<workRunInfo.msgBuf.limit();i++ ) {
                    byte b = workRunInfo.msgBuf.get(i);
                    if (b == '\r') {
                        if( (i+1)<workRunInfo.msgBuf.limit() ) {
                            byte nextB = workRunInfo.msgBuf.get(i + 1);
                            if (nextB == '\n') {
                                i++;
                            }else{
                                workRunInfo.msgBuf.position(i);
                            }
                        }
                        String str = new String(workRunInfo.msgBuf.array(), nextIndex, i -nextIndex-1);
                        lineList.add(str);
                        nextIndex = i ;
                    }
                }


                if(lineList.size()>0) {
                    logger.info(threadName+ " --msg ="+ workRunInfo.msgBuf  );
                    workRunInfo.msgBuf.position(nextIndex+1);
                    workRunInfo.msgBuf=  workRunInfo.msgBuf.compact();
                    for ( int i=0;i<lineList.size();i++ ){
                        logger.info(threadName+ " -->>>>"+ lineList.get(i) );
                    }
                    logger.info(threadName+ " end --msg ="+ workRunInfo.msgBuf  );

                }else {
                    workRunInfo.msgBuf.position(pos);
                    workRunInfo.msgBuf.limit(limit);
                }
            } else if (len < 0) {
                logger.info(threadName+ " exit on len <0 ");
                throw  new Exception("close channel");

            }
        } catch (Exception ex) {
            key.cancel();

            if(workRunInfo!=null) {
                logger.info(threadName+ " read data error   " + workRunInfo.getHostName() + " close connection " +ex.getMessage());
                if( workRunInfo.msgBuf!=null) {
                    workRunInfo.msgBuf.clear();
                    workRunInfo.msgBuf = null;
                }

            }
            try {
                key.channel().close();
            } catch (IOException e) {
                logger.info(e.getLocalizedMessage());
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
