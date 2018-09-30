package org.springbus.test.bytebuf;

import org.junit.Test;

import java.nio.ByteBuffer;

public class ByteBufferAppend {




    public ByteBuffer  append(ByteBuffer src,ByteBuffer to) {

        int pos = to.position();
        int cap = to.capacity();
        if (pos + src.limit() > cap) {
            int newSize = 2 * cap + src.limit();
            ByteBuffer newBuf = ByteBuffer.allocate(newSize);
            newBuf.put(to.array(), 0, to.limit());
            to.clear();
            to = newBuf;
        }

        to.put(src.array(), 0, src.limit());
        return to;
    }


    @Test

    public void appendTest() {
        ByteBuffer src = ByteBuffer.allocate(5);
        for (byte a = 'a'; a < 'a' + 5; a++) {
            src.put(a);

        }

        ByteBuffer to = ByteBuffer.allocate(5);
        for (byte a = 'b'; a < 'b' + 5; a++) {
            to.put(a);
        }

        to = append(src, to);
        System.out.println(to);
       // to.flip();
        to.capacity();

        System.out.println(to);

    }

}
