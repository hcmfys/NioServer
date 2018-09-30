package org.springbus.test.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class ByteBuffTest {


    Logger  logger= LoggerFactory.getLogger(ByteBuffTest.class);
    @Test
    public void test() {

        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeByte('b');
        byteBuf.writeZero(10);
        byteBuf.writeByte('a');
        byteBuf.writeByte('c');
        byteBuf.readerIndex(11);

        String s = byteBuf.toString(Charset.defaultCharset());
        logger.info("a={}",s);
        logger.info("a={}",byteBuf);
        byteBuf.discardReadBytes();

        s = byteBuf.toString(Charset.defaultCharset());
        logger.info("a={}",s);
        logger.info("a={}",byteBuf);

    }
}
