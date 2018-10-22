package org.springbus.naga;

public interface SSLSocketObserver extends SocketObserver {

    void handleFinished(NIOSocket nioSocket);
}
