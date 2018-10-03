package org.springbus.test.chanel;


import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class FIleChannelTest {


    private Logger logger= LoggerFactory.getLogger(FIleChannelTest.class);
    @Before
    public  void  init(){
     File f=   new File("./out/");
     if(!f.exists()) {
         f.mkdirs();
     }
          f=   new File("./out/t.log");
        if(!f.exists()) {
            try {
                boolean newFile = f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void test() {


        try {


            RandomAccessFile accessFile=new RandomAccessFile(
                    new File("./out/t.log"),"rw");

            FileChannel fileChannel = accessFile.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(20);

            for(int i=0;i<10;i++){
                byteBuffer.put((byte) ('a'+i));
            }
            accessFile.seek(20);
            byteBuffer.flip();
            int size=fileChannel.write(byteBuffer);
            fileChannel.close();
            accessFile.close();
            logger.info("write file size= {}",size);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
