package org.springbus.test.bytebuf;


import org.junit.Test;

import java.nio.ByteBuffer;

public class ByteBuffgetTest {



    @Test

    public  void  test(){

        ByteBuffer  byteBuf =  ByteBuffer.allocate(20);
        byteBuf.put((byte)'a');
        byteBuf.put((byte)'b');
        byteBuf.put((byte)'c');
        byteBuf.put((byte)'d');
        byteBuf.put((byte)'e');
        byteBuf.flip();
         byteBuf.mark();
        System.out.println( byteBuf);
        System.out.println((char) byteBuf.get());
        System.out.println( byteBuf);

        System.out.println( (char)byteBuf.get(0));
        System.out.println( byteBuf);

        System.out.println( (char)byteBuf.get());
        System.out.println( byteBuf);

        System.out.println( (char)byteBuf.get());
        System.out.println( byteBuf);

        System.out.println( (char)byteBuf.get());
        System.out.println( byteBuf);
        System.out.println( (char)byteBuf.get());
        System.out.println( byteBuf);
        System.out.println("after reset ");
        byteBuf.reset();
        System.out.println("after reset  pos=" +  byteBuf.position() +"  limit=" +byteBuf.limit() +" -- capacity=" +byteBuf.capacity() );

       for( int i=0;i<100;i++) {
           System.out.println((char) byteBuf.get());
           System.out.println(byteBuf + "  ---> hasRemaining= " + byteBuf.hasRemaining());
           if(!byteBuf.hasRemaining()) {
               byteBuf.limit( byteBuf.limit()+1);
               if(byteBuf.limit()>byteBuf.capacity())
                break;
           }

       }
    }
}
