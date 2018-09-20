package org.springbus.test.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.charset.Charset;

public class ByteBufTest {


    @Test
    public void testBuffer() {


        ByteBuf byteBuf = Unpooled.buffer();
        for (int a = 'a'; a <= 'z'; a++) {
            byteBuf.writeByte(a);
        }
        System.out.println("data ="+ byteBuf.toString(Charset.forName("utf-8")));

        System.out.println( "readerIndex  = " + byteBuf.readerIndex());
        System.out.println( "writerIndex  = " + byteBuf.writerIndex());

        System.out.println( "capacity  = " + byteBuf.capacity());

        for (int a = 'a'; a <= 'z'; a++) {
            byteBuf.writeByte(a);
        }


        System.out.println( "readerIndex  = " + byteBuf.readerIndex());
        System.out.println( "writerIndex  = " + byteBuf.writerIndex());

        System.out.println( "capacity  = " + byteBuf.capacity());
        byteBuf.discardReadBytes();
        System.out.println(" data =" +byteBuf.toString(Charset.forName("utf-8")));
        for (int a = 'a'; a <= 'z'; a++) {
            byteBuf.writeByte(a);
        }

        System.out.println( "readerIndex  = " + byteBuf.readerIndex());
        System.out.println( "writerIndex  = " + byteBuf.writerIndex());

        System.out.println( "capacity  = " + byteBuf.capacity());

        System.out.println(" data =" +byteBuf.toString(Charset.forName("utf-8")));


        for (int a = 'a'; a <= 'z'; a++) {
            byteBuf.writeByte(a);
        }

        System.out.println( "readerIndex  = " + byteBuf.readerIndex());
        System.out.println( "writerIndex  = " + byteBuf.writerIndex());

        System.out.println( "capacity  = " + byteBuf.capacity());
        byteBuf.readerIndex(100);

        System.out.println(" data =" +byteBuf.toString(Charset.forName("utf-8")));


        System.out.println( "capacity  = " + byteBuf.capacity());
        byteBuf.readerIndex(100);
        byteBuf.discardReadBytes();


        System.out.println(" data =" +byteBuf.toString(Charset.forName("utf-8")));

    }
}
