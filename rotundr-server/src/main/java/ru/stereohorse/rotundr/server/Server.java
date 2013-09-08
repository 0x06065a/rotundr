package ru.stereohorse.rotundr.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Server implements ServletContextListener, Runnable {
    private static final Logger log = LoggerFactory.getLogger( Server.class );

    private boolean terminationStarted = false;
    private int port = 6699;

    @Override
    public void contextInitialized( ServletContextEvent servletContextEvent ) {
        Object rotundrServerPort = servletContextEvent.getServletContext().getInitParameter( "rotundrServerPort" );
        try {
            port = Integer.parseInt( rotundrServerPort.toString() );
        } catch ( Exception e ) {
            log.warn( "Unable to set rotundr server port to value [{}]", rotundrServerPort, port );
        }

        new Thread( this ).start();
    }

    @Override
    public void contextDestroyed( ServletContextEvent servletContextEvent ) {
        terminationStarted = true;
    }

    @Override
    public void run() {
        log.info( "Staring on port [{}]...", port );
        long startTime = 0;

        EventLoopGroup masters = new NioEventLoopGroup();
        EventLoopGroup slaves = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group( masters, slaves )
                    .channel( NioServerSocketChannel.class )
                    .childHandler( new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel( SocketChannel socketChannel ) throws Exception {
                            socketChannel.pipeline().addLast( new MsgHandler() );
                        }
                    })
                    .option( ChannelOption.SO_BACKLOG, 128 )            // TODO: is it mandatory?
                    .childOption( ChannelOption.SO_KEEPALIVE, true );   // TODO: is it mandatory?

            ChannelFuture channelFuture = serverBootstrap.bind( port ).sync();
            startTime = System.currentTimeMillis();
            log.info( "Started" );

            while ( !terminationStarted ) {
                try {
                    Thread.sleep( 5000 );
                } catch ( InterruptedException e ) {
                    log.warn( "Termination wait loop is interrupted", e );
                }
            }

            channelFuture.channel().close(); // TODO: is this mandatory?
        } catch ( Exception e ) {
            log.error( "Failed", e );
        } finally {
            slaves.shutdownGracefully();
            masters.shutdownGracefully();
        }

        if ( startTime != 0 ) {
            log.info( "Stopped. Uptime: {} ms", System.currentTimeMillis() - startTime );
        }
    }

    private class MsgHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead( ChannelHandlerContext ctx, Object msg ) throws Exception {
            try {
                ByteBuf buffer = (ByteBuf) msg;
                System.out.println( buffer.toString(io.netty.util.CharsetUtil.US_ASCII) );
            } finally {
                ReferenceCountUtil.release( msg );
            }
        }
    }
}
