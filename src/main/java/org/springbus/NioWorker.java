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
                         SocketChannel  channel= (SocketChannel ) key.channel();
                         ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
                         for (;;) {
                             int r = channel.read(byteBuffer);
                             logger.info(" rev len "+r);
                             if(r>0) {
                                 byteBuffer.flip();
                                 logger.info(ByteUtils.convert2String(byteBuffer));
                             }
                             else if (r == 0) {
                                 break;
                             } else if (r < 0) {

                                 try {
                                     channel.close();
                                     key.cancel();
                                 }catch (Exception ex) {
                                     logger.info("channel  connection close {} ",channel);
                                 }
                             }
                         }

                     }
                     inters.remove();
                 }
             }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
