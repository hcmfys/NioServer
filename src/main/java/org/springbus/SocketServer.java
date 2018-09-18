package org.springbus;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


public class SocketServer {

private final static Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private int port=8080;





    public void bind(){
        try {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port));
            //获取通道管理器
            Selector   selector= Selector.open();
            //将通道管理器与通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件，
            //只有当该事件到达时，Selector.select()会返回，否则一直阻塞。
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            logger.info(" bind sucesss");

            NioSocketContext.newWorks(0);
            NioSocketContext.getPineLine().addHandler(new LineSplitHandler());
            for (; ; ) {

                //当有注册的事件到达时，方法返回，否则阻塞。
                selector.select();

                //获取selector中的迭代器，选中项为注册的事件
                Iterator<SelectionKey> ite=selector.selectedKeys().iterator();

                while(ite.hasNext()) {
                    SelectionKey key = ite.next();
                    //删除已选key，防止重复处理
                    ite.remove();
                    //客户端请求连接事件
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        //获得客户端连接通道
                        SocketChannel channel = server.accept();
                        logger.info(" remote client {}-{} connected  ", channel.getRemoteAddress());
                        NioSocketContext.getNext().addToWorker(channel);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static  void main(String[] args) {
        SocketServer socketServer=new SocketServer();
        socketServer.bind();
    }


}