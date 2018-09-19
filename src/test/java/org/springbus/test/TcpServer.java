package org.springbus.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TcpServer {


    static class TcpClientHandler extends ChannelInboundHandlerAdapter {


        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush("q");

            System.out.println("------------->>>> conncted "+ctx.channel().metadata());
            super.channelRegistered(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String e=msg.toString();
            System.out.println("recv " + e);

            if(e.trim().equals("e")) {
                ctx.close();
                ctx.channel().close();
            }
            super.channelRead(ctx, msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            ctx.fireExceptionCaught(cause);
            ctx.close();
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        ServerBootstrap   b = new ServerBootstrap();
        int port = 9999;
        EventLoopGroup workGroup = new NioEventLoopGroup();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        b.group(bossGroup,workGroup);
        ChannelFuture future = null;
        try {
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new LoggingHandler(LogLevel.TRACE));
                    pipeline.addLast(new LineBasedFrameDecoder(512));
                    pipeline.addLast(new StringDecoder()  );

                    pipeline.addLast(new TcpClient.TcpClientHandler());
                    pipeline.addLast(new StringEncoder());
                }
            });
            future = b.bind("127.0.0.1", port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
