package org.springbus.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbus.TimeUtils;

public class TimeServer {

    private  static  Logger  logger= LoggerFactory.getLogger(TimeServer.class);

    class ChildChannel extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new StringDecoder( )  );
            ch.pipeline().addLast(new TimeHandler());
        }
    }

    class TimeHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.fireExceptionCaught(cause);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String str=(String)  msg;
            logger.info("rev "+str);
            ctx.writeAndFlush(TimeUtils.getTime()+" >>"+ str+"\n");
            ctx.fireChannelRead(msg);
        }


    }


    public void bind(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChildChannel());
            ChannelFuture future = b.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception ex) {

        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        TimeServer timeServer = new TimeServer();
        try {
            timeServer.bind(9999);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
