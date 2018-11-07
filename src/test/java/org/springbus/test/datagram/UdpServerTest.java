package org.springbus.test.datagram;

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
            ByteBuffer byteBuffer = ByteBuffer.allocate(8092);

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
                        ByteBuffer myBuff = ByteBuffer.wrap(data.getBytes());
                        //myBuff.put(data.getBytes());
                        myBuff.flip();
                        myBuff.limit(data.length());
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
        ByteBuffer buffer = ByteBuffer.allocate(4097);
        buffer.put(message.getBytes("UTF-8"));
        for(int i=message.length();i<4096;i++) {
            buffer.put((byte) 'A');
        }
        buffer.flip();
         int len= datagramChannel.send(buffer, socketAddress);
        System.out.println( " >> send len=" + len + "   >> addr= " + socketAddress);
    }

}
