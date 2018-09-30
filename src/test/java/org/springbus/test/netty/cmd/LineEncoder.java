package org.springbus.test.netty.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.charset.Charset;
import java.util.List;

public class LineEncoder extends ByteToMessageCodec<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        out.writeBytes(msg.getBytes());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int startIndex = in.readerIndex();
        int endIndex = in.writerIndex();
        byte CR = '\r';
        byte CN = '\n';
        boolean hasData = false;
        for (int i = startIndex; i < endIndex; i++) {
            byte b = in.getByte(i);
            if (b == CR) {
                int next = i + 1;
                hasData = true;
                if (next < endIndex) {
                    b = in.getByte(next);
                    if (b == CN) {
                        hasData = true;
                        endIndex = next;
                        break;
                    }
                }
                break;
            }
        }
        if (hasData) {
            ByteBuf dst = Unpooled.buffer();
            int length = endIndex - startIndex + 1;
            in.getBytes(startIndex, dst, length);
            in.readerIndex(endIndex+ 1);
            out.add(dst.toString(Charset.defaultCharset()));
        }

    }
}
