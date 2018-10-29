package org.springbus.test.datagram;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

public class DataGramdChannelClientTest {

    public static void main(String[] args) throws IOException {

        final DatagramChannel channel = DatagramChannel.open();
        //接收消息线程
        new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            byte b[];
            while (true) {
                buffer.clear();
                SocketAddress socketAddress = null;
                try {
                    socketAddress = channel.receive(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (socketAddress != null) {
                    int position = buffer.position();
                    b = new byte[position];
                    buffer.flip();
                    for (int i = 0; i < position; ++i) {
                        b[i] = buffer.get();
                    }
                    try {
                        System.out.println("receive remote " + socketAddress.toString() + ":" + new String(b, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        //发送控制台输入消息
        while (true) {
            Scanner sc = new Scanner(System.in);
            String next = sc.next();
            try {
                sendMessage(channel, next);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendMessage(DatagramChannel channel, String mes) throws IOException {
        if (mes == null || mes.isEmpty()) {
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put(mes.getBytes("UTF-8"));
        buffer.flip();
        System.out.println("send msg:" + mes);
        int send = channel.send(buffer, new InetSocketAddress("localhost", 1234));
        System.out.println("send msg: len= "     + send);
    }

}