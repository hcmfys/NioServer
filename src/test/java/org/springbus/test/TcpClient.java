package org.springbus.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TcpClient {


    static class TcpClientHandler extends ChannelInboundHandlerAdapter {


        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush("e");
            super.channelRegistered(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("recv " + msg);
            super.channelRead(ctx, msg);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Bootstrap b = new Bootstrap();
        int port = 9999;
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ChannelFuture future = null;
        try {
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    ch.pipeline().addLast(new StringEncoder());
                    ch.pipeline().addLast(new StringEncoder());
                    ch.pipeline().addLast(new TcpClientHandler());

                }
            });
            future = b.group(workGroup).connect("127.0.0.1", port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}
