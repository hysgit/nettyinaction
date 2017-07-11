package com.woslx.netty.server;

import com.woslx.netty.client.NettyClientHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by hy on 7/11/17.
 */
public class NettyServer {

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group).channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(10000))
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            ChannelFuture f = b.bind().sync();
            System.out.println(NettyServer.class.getName() +
                    " started and listening for connections on " + f.channel().localAddress());
            //(7) 获取 Channel 的CloseFuture，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        }
        finally {
            group.shutdownGracefully().sync();
        }
    }
}
