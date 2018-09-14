package org.springbus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public    class NioSocketContext {

    private  NioSocketContext(){}

     private static    NioWorker workers [] ;
     private static  AtomicInteger  atomicInteger=new AtomicInteger(0);
    public static  void  newWorks(int nums)  throws  Exception {
        if (nums <= 0) {
            nums = Runtime.getRuntime().availableProcessors() ;
        }
        ExecutorService pool = Executors.newFixedThreadPool(nums);

        workers = new NioWorker[nums];
        for (int i = 0; i < nums; i++) {
            NioWorker worker = NioWorkerFactory.newClientWorker();
            if (worker != null) {
                workers[i] = worker;
                pool.execute(worker);
            } else {
                throw new Exception("worker create error ！");
            }
        }
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
