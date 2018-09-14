package org.springbus;

public class NioWorkerFactory {



    public  static  NioWorker   newClientWorker( ){

        NioWorker worker=new NioWorker();
        if(worker.isSucess()) return  worker;
        return  null;
    }

}
