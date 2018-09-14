package org.springbus;

public abstract class  MessageEvent {


    abstract void messageReceive(Object byteBuffer);




    private MessageEvent e;

    public void setNext(MessageEvent e) {
        this.e = e;
    }

    public MessageEvent getNext() {
        return e;
    }

}
