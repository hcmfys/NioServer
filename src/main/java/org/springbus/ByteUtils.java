package org.springbus;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ByteUtils {


    /**
     *
     * @param str
     * @return
     */
    public static ByteBuffer string2Buffer(String str) {

        return ByteBuffer.wrap(str.getBytes());

    }


    /**
     *
     * @param buffer
     * @return
     */
    public static String convert2String(ByteBuffer buffer) {

        try {

            Charset   charset = Charset.forName("UTF-8");
            CharsetDecoder  decoder = charset.newDecoder();
            //用这个的话，只能输出来一次结果，第二次显示为空
            // charBuffer = decoder.decode(buffer);
            CharBuffer  charBuffer = decoder.decode(buffer.asReadOnlyBuffer());

            return charBuffer.toString();

        } catch (Exception ex) {

            ex.printStackTrace();

            return "error";

        }

    }
}
