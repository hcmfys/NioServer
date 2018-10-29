package org.springbus.test.datagram;

import io.netty.handler.codec.protobuf.ProtobufDecoder;
import sun.util.resources.cldr.ebu.CurrencyNames_ebu;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UdpServerTest {
static    int port=1236;

    public static  void main(String [] args ) {
        try {
            DatagramChannel datagramChannel = DatagramChannel.open();


            datagramChannel.bind(new InetSocketAddress( port));
            datagramChannel.configureBlocking(true);
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            while (true) {
                byteBuffer.clear();
                SocketAddress socketAddress = datagramChannel.receive(byteBuffer);
                if (socketAddress != null) {
                    byteBuffer.flip();
                    int size = byteBuffer.limit();

                    if (size > 0) {
                        byteBuffer.flip();
                        String data = new String(byteBuffer.array(), 0, size);
                        System.out.println("read string =" + data);
                        byteBuffer.flip();
                        ByteBuffer myBuff = ByteBuffer.allocate(1024);
                        myBuff.put(data.getBytes());
                        myBuff.flip();
                        int len=datagramChannel.send( myBuff, socketAddress);
                        System.out.println("read size =" + len);
                        Thread.sleep(1000);
                        sendReback(socketAddress, datagramChannel);
                        Thread.sleep(1000);
                        sendReback(socketAddress, datagramChannel);

                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public static void sendReback(SocketAddress socketAddress,DatagramChannel datagramChannel) throws IOException {
        String message = "I has receive your message";
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(message.getBytes("UTF-8"));
        buffer.flip();
         int len= datagramChannel.send(buffer, socketAddress);
        System.out.println( " >> send len=" + len + "   >> addr= " + socketAddress);
    }

}
