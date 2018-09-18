package org.springbus;

public   abstract   class AbstractMessageEvent extends  MessageEvent {

    @Override
    void messageReceive(Object byteBuffer) {
        MessageEvent e= getNext();
        if (e!=null) {
            e.messageReceive(byteBuffer);
        }
    }


}
