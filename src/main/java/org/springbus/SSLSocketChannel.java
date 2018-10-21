package org.springbus;

import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import javax.net.ssl.*;
import java.io.*;


/**
 *  a SocketChannel with TLS/SSL encryption
 *
 *@author     Alexander Kout
 *@created    25. Mai 2005
 */

public class SSLSocketChannel {

    int SSL;
    ByteBuffer clientIn, clientOut, cTOs, sTOc, wbuf;
    SocketChannel sc = null;
    SSLEngineResult res;
    SSLEngine sslEngine;

    public SSLSocketChannel() throws IOException {
        sc = SocketChannel.open();
    }

    public SSLSocketChannel(SocketChannel sc) {
        this.sc = sc;
    }

    public int tryTLS(int pSSL) throws IOException {
        SSL = pSSL;
        if (SSL == 0) {
            return 0;
        }

        SSLContext sslContext = null;
        try {
// create SSLContext
            sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null,
                    new TrustManager[]{new EasyX509TrustManager(null)},
                    null);
// create Engine
            sslEngine = sslContext.createSSLEngine();
// begin
            sslEngine.setUseClientMode(true);

            sslEngine.setEnableSessionCreation(true);
            SSLSession session = sslEngine.getSession();
            createBuffers(session);
// wrap
            clientOut.clear();
            sc.write(wrap(clientOut));
            while (res.getHandshakeStatus() !=
                    SSLEngineResult.HandshakeStatus.FINISHED) {
                if (res.getHandshakeStatus() ==
                        SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
// unwrap
                    sTOc.clear();
                    while (sc.read(sTOc) < 1) {
                        Thread.sleep(20);
                    }
                    sTOc.flip();
                    unwrap(sTOc);
                    if (res.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.FINISHED) {
                        clientOut.clear();
                        sc.write(wrap(clientOut));
                    }
                } else if (res.getHandshakeStatus() ==
                        SSLEngineResult.HandshakeStatus.NEED_WRAP) {
// wrap
                    clientOut.clear();
                    sc.write(wrap(clientOut));
                } else {
                    Thread.sleep(1000);
                }
            }
            clientIn.clear();
            clientIn.flip();
            SSL = 4;
            Utilities.print("SSL established\n");
        } catch (Exception e) {
            e.printStackTrace(System.out);
            SSL = 0;
        }
        return SSL;
    }

    private synchronized ByteBuffer wrap(ByteBuffer b) throws SSLException {
        cTOs.clear();
        res = sslEngine.wrap(b, cTOs);
        cTOs.flip();
        Utilities.print("wrap:\n" + res.toString() + "\n");
        return cTOs;
    }

    private synchronized ByteBuffer unwrap(ByteBuffer b) throws SSLException {
        clientIn.clear();
        int pos;
        Utilities.print("b.remaining " + b.remaining() + "\n");
        while (b.hasRemaining()) {
            Utilities.print("b.remaining " + b.remaining() + "\n");
            res = sslEngine.unwrap(b, clientIn);
            Utilities.print("unwrap:\n" + res.toString() + "\n");
            if (res.getHandshakeStatus() ==
                    SSLEngineResult.HandshakeStatus.NEED_TASK) {
// Task
                Runnable task;
                while ((task = sslEngine.getDelegatedTask()) != null) {
                    Utilities.print("task...\n");
                    task.run();
                }
                Utilities.print("task:\n" + res.toString() + "\n");
            } else if (res.getHandshakeStatus() ==
                    SSLEngineResult.HandshakeStatus.FINISHED) {
                return clientIn;
            } else if (res.getStatus() ==
                    SSLEngineResult.Status.BUFFER_UNDERFLOW) {
                Utilities.print("underflow\n");
                Utilities.print("b.remaining " + b.remaining() + "\n");
                return clientIn;
            }
        }
        return clientIn;
    }

    private void createBuffers(SSLSession session) {

        int appBufferMax = session.getApplicationBufferSize();
        int netBufferMax = session.getPacketBufferSize();

        clientIn = ByteBuffer.allocate(65536);
        clientOut = ByteBuffer.allocate(appBufferMax);
        wbuf = ByteBuffer.allocate(65536);

        cTOs = ByteBuffer.allocate(netBufferMax);
        sTOc = ByteBuffer.allocate(netBufferMax);

    }

    public int write(ByteBuffer src) throws IOException {
        if (SSL == 4) {
            return sc.write(wrap(src));
        }
        return sc.write(src);
    }

    public int read(ByteBuffer dst) throws IOException {
        Utilities.print("read\n");
        int amount = 0, limit;
        if (SSL == 4) {
// test if there was a buffer overflow in dst
            if (clientIn.hasRemaining()) {
                limit = Math.min(clientIn.remaining(), dst.remaining());
                for (int i = 0; i < limit; i++) {
                    dst.put(clientIn.get());
                    amount++;
                }
                return amount;
            }
// test if some bytes left from last read (e.g. BUFFER_UNDERFLOW)
            if (sTOc.hasRemaining()) {
                unwrap(sTOc);
                clientIn.flip();
                limit = Math.min(clientIn.limit(), dst.remaining());
                for (int i = 0; i < limit; i++) {
                    dst.put(clientIn.get());
                    amount++;
                }
                if (res.getStatus() != SSLEngineResult.Status.BUFFER_UNDERFLOW) {
                    sTOc.clear();
                    sTOc.flip();
                    return amount;
                }
            }
            if (!sTOc.hasRemaining()) {
                sTOc.clear();
            } else {
                sTOc.compact();
            }

            if (sc.read(sTOc) == -1) {
                Utilities.print("close from SSLSocketChannel" + "\n");
                sTOc.clear();
                sTOc.flip();
                return -1;
            }
            sTOc.flip();
            unwrap(sTOc);
// write in dst
            clientIn.flip();
            limit = Math.min(clientIn.limit(), dst.remaining());
            for (int i = 0; i < limit; i++) {
                dst.put(clientIn.get());
                amount++;
            }
            Utilities.print("dst.remaining " + dst.remaining() + "\n");
            return amount;
        }
        return sc.read(dst);
    }

    public boolean isConnected() {
        return sc.isConnected();
    }

    public void close() throws IOException {
        if (SSL == 4) {
            sslEngine.closeOutbound();
            clientOut.clear();
            sc.write(wrap(clientOut));
            sc.close();
        } else {
            sc.close();
        }
    }

    public SelectableChannel configureBlocking(boolean b) throws IOException {
        return sc.configureBlocking(b);
    }

    public boolean connect(SocketAddress remote) throws IOException {
        return sc.connect(remote);
    }

    public boolean finishConnect() throws IOException {
        return sc.finishConnect();
    }

    public Socket socket() {
        return sc.socket();
    }

    public boolean isInboundDone() {
        return sslEngine.isInboundDone();
    }
}

