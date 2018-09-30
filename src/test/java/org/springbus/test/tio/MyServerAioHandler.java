package org.springbus.test.tio;

import org.springbus.test.bytebuf.ByteBuff;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.nio.ByteBuffer;

public class MyServerAioHandler implements ServerAioHandler {

    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {


        int i=position;
        boolean hasData=false;
        int endIndex=0;
        int startIndex=position;
        for(;i<  limit;i++) {
            if( buffer.get(i)=='\r'  ) {
                int next=i;
                endIndex=next;
                hasData=true;
                if(next+1 < limit) {
                    if (buffer.get(next + 1) == '\n') {
                        hasData = true;
                        endIndex = next + 1;
                    }
                }
                if(hasData){
                    break;
                }
            }
        }
        if(hasData) {
            byte[] bytes = new byte[endIndex - startIndex+1];
            System.arraycopy(buffer.array(), startIndex, bytes, 0, bytes.length);
            StringPacket packet = new StringPacket(bytes);
            buffer.position(endIndex+1);
            return packet;
        }

        return null;
    }

    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        StringPacket stringPacket = (StringPacket) packet;

        ByteBuffer newBuf = ByteBuffer.wrap(stringPacket.getBytes());
        return newBuf;
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        StringPacket stringPacket = (StringPacket) packet;
        System.out.println("recv -->" + stringPacket.toString());
        Tio.send(channelContext,stringPacket);

    }
}
