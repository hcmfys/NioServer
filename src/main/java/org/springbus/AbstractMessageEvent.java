package org.springbus;

import java.nio.ByteBuffer;

public   abstract   class AbstractMessageEvent extends  MessageEvent {

    @Override
    void messageReceive(Object byteBuffer) {
        MessageEvent e= getNext();
        if (e!=null) {
            e.messageReceive(byteBuffer);
        }
    }


}
