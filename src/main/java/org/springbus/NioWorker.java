package org.springbus;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


public class NioWorker  implements  Runnable  {

   private static final  Logger logger= LoggerFactory.getLogger(NioWorker.class);

    private Selector select;

    public Selector getSelect() {
        return select;
    }

    public void setSelect(Selector select) {
        this.select = select;
    }

    public boolean isSucess() {
        return sucess;
    }

    public void setSucess(boolean sucess) {
        this.sucess = sucess;
    }

    private boolean sucess=false;



    public  NioWorker(){
        try {
            select= Selector.open();
            sucess=true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(!sucess) {
                try {
                    throw new  Exception("select error");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addToWorker(SocketChannel  clientChanel ) throws  Exception {

        clientChanel.configureBlocking(false);
        clientChanel.register(select, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

    }

    @Override
    public void run() {
        while(sucess) {
            try {
             int op=   select.select(1);
               {
                 Set<SelectionKey> keys = select.selectedKeys();
               Iterator<SelectionKey> inters= keys.iterator();
                 while( inters.hasNext()) {
                     SelectionKey key=   inters.next();
                     if( key.isReadable() ){
                       ByteUtils.readBuffer(key);


                     }
                     inters.remove();
                 }
             }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }
}
