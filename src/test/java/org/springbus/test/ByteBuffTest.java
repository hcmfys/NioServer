package org.springbus.test;

import org.junit.Test;
import org.junit.internal.builders.NullBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class ByteBuffTest {

    private  final Logger  logger= LoggerFactory.getLogger(ByteBuffTest.class);


    @Test


    public void testByteBuff(){

        ByteBuffer buffer=ByteBuffer.allocate(12);

        for(int i='a'; i<='z';i++) {
            if(buffer.hasRemaining()) {
                buffer.put((byte) i);
            }else{
                ByteBuffer  newBuffer=   ByteBuffer.allocate(buffer.capacity() *2 );
                newBuffer.put(buffer.array(),0,buffer.position());
                buffer.clear();

                buffer=newBuffer;
                buffer.put((byte) i);

            }
        }
        logger.info(buffer.toString());

        logger.info( "buffer={} ,pos={} remain={}  limit={}",new String( buffer.array(),0,buffer.position()) ,buffer.position(),    buffer.remaining(), buffer.limit());


        ByteBuffer newBuffer=    buffer.slice();

        logger.info( "newBuffer ={}  pos={} offset={} " ,new String( newBuffer.array(),0,newBuffer.arrayOffset())  ,newBuffer.position(),newBuffer.arrayOffset());


        ByteBuffer newBufferByPos=   buffer.compact();

        logger.info( "newBufferByPos={} " ,new String( newBufferByPos.array()));






    }



}
