package org.kuali.ole.sip2.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;
import org.kuali.ole.sip2.sip2Server.NettyServerHandler;

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

    public NettyServer() {
    }

    public NettyServer(int port, String serverUrl) {
        this.serverPort = port;
        this.serverUrl = serverUrl;
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

                                ch.pipeline().addLast(new NettyServerHandler(serverUrl));
                            }
                        }).option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                f = b.bind(serverPort).sync();
                LOG.info("Server started and is ready for client connections....");
                this.message = "Socket server started Successfully";
                f.channel().closeFuture().sync();
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

