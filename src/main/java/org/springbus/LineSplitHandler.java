package org.springbus;

import java.nio.ByteBuffer;

public class LineSplitHandler  extends  AbstractMessageEvent{

    @Override
    void messageReceive(Object  msg) {
        if (msg instanceof ByteBuffer ){


        }else {
            super.messageReceive(msg);
        }

    }
}
