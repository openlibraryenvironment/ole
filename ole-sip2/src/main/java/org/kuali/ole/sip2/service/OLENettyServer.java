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
import org.kuali.ole.sip2.sip2Server.OLENettyServerHandler;

/**
 * Created by sheiksalahudeenm on 12/29/14.
 */
public class OLENettyServer implements Runnable {

    protected int serverPort = 7052;
    protected boolean isStopped = false;
    protected Thread runningThread = null;
    protected String serverUrl = null;
    protected String circulationService = null;
    private String message;
    private static final Logger LOG = Logger.getLogger(OLENettyServer.class);
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    ChannelFuture f = null;

    public OLENettyServer() {
    }

    public OLENettyServer(int port, String serverUrl, String circulationService) {
        this.serverPort = port;
        this.serverUrl = serverUrl;
        this.circulationService = circulationService;
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

                                ch.pipeline().addLast(new OLENettyServerHandler(serverUrl, circulationService));
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
