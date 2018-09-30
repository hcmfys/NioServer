package org.springbus.test.netty.cmd;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class NettyTest {




    public static void main(String[] args) {


        ServerBootstrap bossStrap = new ServerBootstrap();
        EventLoopGroup boosEventGroup = new NioEventLoopGroup();
        EventLoopGroup workEventGroup = new NioEventLoopGroup();
        try {
            bossStrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ch.pipeline().addLast(new LoggingHandler());
                    ch.pipeline().addLast(new LineEncoder());
                    ch.pipeline().addLast(new CmdHandler());
                }
            });

            bossStrap.group(boosEventGroup, workEventGroup);
            bossStrap.channel(NioServerSocketChannel.class);
            ChannelFuture channelFuture = bossStrap.bind(8088).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            boosEventGroup.shutdownGracefully();
            workEventGroup.shutdownGracefully();
        }


    }
}
