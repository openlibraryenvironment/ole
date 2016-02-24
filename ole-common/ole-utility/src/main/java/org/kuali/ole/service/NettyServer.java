package org.kuali.ole.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

/**
 * Created by chenchulakshmig on 8/27/15.
 */

public class NettyServer implements Runnable {

    protected int serverPort = 7052;
    protected boolean isStopped = false;
    protected Thread runningThread = null;
    protected String serverUrl = null;
    private String message;
    private static final Logger LOG = Logger.getLogger(NettyServer.class);
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    ChannelFuture f = null;
    private NettyHandler nettyHandler;

    public NettyServer() {
    }

    public NettyServer(Integer portNo, NettyHandler nettyHandler) {
        this.serverPort = portNo;
        this.nettyHandler = nettyHandler;
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        while (!isStopped()) {
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch)
                                    throws Exception {
                                ch.pipeline().addLast(nettyHandler);
                            }
                        }).option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                Channel serverChannel = b.bind(serverPort).sync().channel();
                serverChannel.closeFuture().sync();
                System.out.println(" ***************** Server started ************************* ");
            } catch (Exception e) {
                e.printStackTrace();
                this.message = "Error starting server" + e;
            }
        }
    }


    public synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        } catch (Exception e) {
            this.message = "Error closing server" + e;
            throw new RuntimeException("Error closing server", e);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

