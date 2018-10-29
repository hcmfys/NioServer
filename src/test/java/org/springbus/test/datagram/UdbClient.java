package org.springbus.test.datagram;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

public class UdbClient {


    static    int port=1236;
    public static  void main(String [] args ) throws  Exception {
        DatagramChannel datagramChannel = DatagramChannel.open();

        datagramChannel.configureBlocking(false);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        byteBuffer.clear();
                        SocketAddress socketAddress = datagramChannel.receive(byteBuffer);
                        if (socketAddress != null) {
                            byteBuffer.flip();
                            int pos = byteBuffer.limit();
                            System.out.println("<<< "+ new String(byteBuffer.array(), 0, pos));
                        }else{
                            Thread.sleep(1000);
                        }
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        t.start();

        //发送控制台输入消息
        while (true) {
            Scanner sc = new Scanner(System.in);
            String next = sc.next();
            try {
                sendMessage(datagramChannel,next);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     *
     * @param channel
     * @param mes
     * @throws IOException
     */

    public static void sendMessage(DatagramChannel channel, String mes) throws IOException {
        if (mes == null || mes.isEmpty()) {
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put(mes.getBytes("UTF-8"));
        buffer.flip();
        System.out.println("send msg:" + mes);
        int send = channel.send(buffer, new InetSocketAddress("localhost", port));
        System.out.println("send msg:" + mes +" size=" +send);

    }
}
