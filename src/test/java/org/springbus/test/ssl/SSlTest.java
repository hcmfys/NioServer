package org.springbus.test.ssl;

import javax.net.ssl.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSlTest {

    public static void main(String[] args) throws Exception {


        X509TrustManager x509m = new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        };
        // 获取一个SSLContext实例
        SSLContext s = SSLContext.getInstance("TLS");
        // 初始化SSLContext实例
        s.init(null, new TrustManager[]{x509m}, new java.security.SecureRandom());
        // 打印这个SSLContext实例使用的协议
        System.out.println("缺省安全套接字使用的协议: " + s.getProtocol());
        SSLEngine sslEngine = s.createSSLEngine();


        SSLSocketFactory sslSocketFactory = s.getSocketFactory();
        System.out.println(sslEngine.getSupportedCipherSuites());


        Socket socket = sslSocketFactory.createSocket("www.taobao.com", 443);
        socket.setSoTimeout(3000);
        // sslEngine.beginHandshake();
        System.out.println(socket);

        String msg = "GET / HTTP/1.1\n";
        msg += "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n";
        msg += "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36\n";
        msg += "HOST: www.taobao.com\n";
        msg += "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8\n";
        msg += "Upgrade-Insecure-Requests: 1\r\n";
        msg += "\r\n";

        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        out.write(msg.getBytes(), 0, msg.getBytes().length);
        byte[] buff = new byte[1024];
        long currTime=System.currentTimeMillis();
        while(true) {
            long t=(System.currentTimeMillis()-currTime ) /1000;
            System.out.println("size="+t);
            if(t >3) break;
            int size = in.read(buff);

            if(size>0) {
                // System.out.println(size);
                System.out.println(new String(buff, 0, size));
            }else{
                break;
            }

        }


        socket.close();


    }
}
