package org.springbus.test.chanel;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AsynFileTest {

    private Logger logger= LoggerFactory.getLogger(AsynFileTest.class);

    @Test
    public  void test(){


        Path path= Paths.get("./out/t.log");


        AsynchronousFileChannel fileChannel= null;
        try {
            fileChannel = AsynchronousFileChannel.open(path);

        ByteBuffer byteBuffer=ByteBuffer.allocate(50);
        fileChannel.read(byteBuffer, 0, byteBuffer, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                logger.info( "complete ={} {},",result,attachment);
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                logger.info( "exc ={},",exc);
            }
        });

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2000);
            fileChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
