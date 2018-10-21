package org.springbus;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ByteUtils {


    /**
     * @param str
     * @return
     */
    public static ByteBuffer string2Buffer(String str) {

        return ByteBuffer.wrap(str.getBytes());

    }


    /**
     * @param buffer
     * @return
     */
    public static String convert2String(ByteBuffer buffer) {

        try {

            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            //用这个的话，只能输出来一次结果，第二次显示为空
            // charBuffer = decoder.decode(buffer);
            CharBuffer charBuffer = decoder.decode(buffer.asReadOnlyBuffer());

            return charBuffer.toString();

        } catch (Exception ex) {

            ex.printStackTrace();

            return "error";

        }
    }


    /**
     * @param key
     * @return
     * @throws Exception
     */
    public static ByteBuffer readBuffer(SelectionKey key) throws Exception {

        int defaultSize=10;
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        if (byteBuffer == null) {
            byteBuffer = ByteBuffer.allocate(defaultSize);
            key.attach(byteBuffer);
        }

        SocketChannel channel = (SocketChannel) key.channel();

        for (; ; ) {

            int r ;
            while ((r = channel.read(byteBuffer)) > 0) {
                if (!byteBuffer.hasRemaining()) {
                    ByteBuffer newBuff = ByteBuffer.allocate(byteBuffer.capacity() + defaultSize);
                    newBuff.put(byteBuffer.array(), 0, byteBuffer.limit());
                    byteBuffer.clear();
                    byteBuffer = newBuff;
                    key.attach(byteBuffer);
                }
            }

            if (r == 0) {
                break;
            } else if (r < 0) {

                try {
                    channel.close();
                    key.cancel();
                } catch (Exception ex) {
                    throw ex;
                }
            }else{

                System.out.println("========");
                System.out.println( new String( byteBuffer.array(),0,byteBuffer.position()));
                System.out.println("============");
                System.out.println(byteBuffer);
                return  byteBuffer;

            }
        }
        return null;

    }


}
