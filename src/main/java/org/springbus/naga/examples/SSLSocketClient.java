package org.springbus.naga.examples;

import org.springbus.naga.*;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class SSLSocketClient {

    public static final String TAG = "SSL";
    private static final String SERVICR_HOST = "10.192.42.174";
    private static final int SERVICR_PORT = 8881;

    public static void main(String... args) {
        creatNIOSSLConnect();
    }

    private static void creatNIOSSLConnect() {
        new Thread() {
            public void run() {
                try {
                    NIOService service = new NIOService();

                    SSLEngine engine = getSSLContext().createSSLEngine();
                    if (null != engine) {
                        SSLSocketChannelResponder socket = service.openSSLSocket(engine,
                                SERVICR_HOST, SERVICR_PORT);
                        socket.listen(new SSLSocketObserver() {
                            public void packetSent(NIOSocket socket, Object tag) {
                                System.out.println("Packet sent");
                            }

                            public void connectionOpened(NIOSocket nioSocket) {
                                try {
                                    ((NIOSocketSSL) nioSocket).beginHandshake();
                                } catch (SSLException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("*Connection opened");
                            }

                            public void connectionBroken(NIOSocket nioSocket, Exception exception) {
                                System.out.println("*Connection broken");
                                if (exception != null)
                                    exception.printStackTrace();
                            }

                            public void packetReceived(NIOSocket socket, byte[] packet) {
                                System.out.println("Client Received: " + new String(packet));
                                receivMessage(new String(packet));
                            }

                            @Override
                            public void handleFinished(NIOSocket socket) {
                                sendMessage(socket);
                            }

                        });
                        while (true) {
                            service.selectBlocking();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    public static SSLContext getSSLContext() throws KeyStoreException, IOException,
            NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException,
            KeyManagementException {

        char[] password = "testssl".toCharArray();
        KeyStore keyStore = KeyStore.getInstance("BKS");
        KeyStore trustStore = KeyStore.getInstance("BKS");

        keyStore.load(new FileInputStream("ssl/clientkey.jks"), password);
        trustStore.load(new FileInputStream("ssl/clienttrust.jks"), password);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(keyStore, password);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return sslContext;
    }

    public static void sendMessage(NIOSocket socket) {
        socket.write("hello word".getBytes());
        System.out.println("*Client sent: hello word -- 11111");
    }

    public static void receivMessage(String string) {
        // TODO Auto-generated method stub
    }


}
