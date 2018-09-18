package org.springbus;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public    class NioSocketContext {

    private  NioSocketContext(){}

     private static    NioWorker workers [] ;
     private static  AtomicInteger  atomicInteger=new AtomicInteger(0);
    public static  void  newWorks(int nuns)  throws  Exception {
        if (nuns <= 0) {
            nuns = Runtime.getRuntime().availableProcessors() ;
        }
        ExecutorService pool = Executors.newFixedThreadPool(nuns);

        workers = new NioWorker[nuns];
        for (int i = 0; i < nuns; i++) {
            NioWorker worker = NioWorkerFactory.newClientWorker();
            if (worker != null) {
                workers[i] = worker;
                pool.execute(worker);
            } else {
                throw new Exception("worker create error ！");
            }
        }
    }


    private  static EventPipLine  pipLine;

   public  static   EventPipLine  getPineLine() {
       if (pipLine == null) {
           pipLine = new EventPipLine();
       }
       return pipLine;
   }

   public  static  void fireEvent(ByteBuffer byteBuffer) {

   }


    /**
     *  get next worker
     * @return
     */

    public  static  NioWorker getNext(){

        int size=workers.length;
        NioWorker nioWorker =  workers[atomicInteger.incrementAndGet() % size ];
        return  nioWorker;
    }
}
