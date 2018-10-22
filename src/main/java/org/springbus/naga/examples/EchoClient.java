package org.springbus.naga.examples;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class EchoClient {

    public static void main(String... args) {
        int port = 8080;//Integer.parseInt(args[0]);
        Thread t1 = null;
        try {

            SocketChannel socketChannel = SocketChannel.open();

            boolean ok = socketChannel.connect(new InetSocketAddress(8080));
            socketChannel.configureBlocking(false);
            System.out.println("connected ok " + ok);

            if (ok) {

                t1 = new Thread(new Reader(socketChannel.socket()));
                t1.start();

                Thread t2 = new Thread(new Writer(socketChannel.socket()));
                t2.start();
            }

        } catch (Exception ex) {

        }
        try {
            t1.join();
            System.out.println("exit ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

   static class Reader  implements   Runnable {

       private Socket s;

       Reader(Socket s) {
           this.s = s;
       }

       @Override
       public void run() {

           while (true) {
               try {
                   System.out.println("  run ...");
                   Thread.sleep(1000);
                   SocketChannel channel = s.getChannel();
                   ByteBuffer byteBuffer = ByteBuffer.allocate(200);
                   int size = channel.read(byteBuffer);
                   System.out.println(" read size= "+size);
                   if (size > 0) {
                       byteBuffer.flip();
                       System.out.println(new String(byteBuffer.array(), 0, size));
                   }else if(size==-1) {
                       break;
                   }
               } catch (Exception e) {
                   e.printStackTrace();
                   break;
               }

           }
       }
   }

  static   class Writer  implements   Runnable {

        private  Socket s;
        Writer(Socket s) {
            this.s=s;
        }

        @Override
        public void run() {

            while (true) {
                try {
                    Thread.sleep(2000);
                    SocketChannel channel=     s.getChannel();
                    ByteBuffer byteBuffer=ByteBuffer.allocate(200);
                    byteBuffer.put(("hi "+new Date().toString() +"\r\n") .getBytes());
                    byteBuffer.flip();
                    int size=channel.write(byteBuffer);

                    System.out.println(" write size= "+size);
                } catch ( Exception e) {
                    e.printStackTrace();
                    break;
                }

            }
        }
    }
}