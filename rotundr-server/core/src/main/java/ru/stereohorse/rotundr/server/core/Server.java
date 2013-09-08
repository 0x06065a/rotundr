package ru.stereohorse.rotundr.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stereohorse.rotundr.server.messages.MessageProtos;

public class Server {
    private static final Logger log = LoggerFactory.getLogger( Server.class );

    private int port;
    private boolean terminationStarted = false;

    public Server( int port ) {
        this.port = port;
    }

    public void stop() {
        terminationStarted = true;
    }

    public void start() {
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
                            socketChannel.pipeline().addLast( "frameDecoder", new LengthFieldBasedFrameDecoder( 1048576, 0, 4, 0, 4 ) );
                            socketChannel.pipeline().addLast( "protobufDecoder", new ProtobufDecoder( MessageProtos.Message.getDefaultInstance() ) );
                        }
                    } )
                    .option( ChannelOption.SO_BACKLOG, 128 )            // TODO: is it mandatory?
                    .childOption( ChannelOption.SO_KEEPALIVE, true );   // TODO: is it mandatory?

            serverBootstrap.bind( port ).sync();
            startTime = System.currentTimeMillis();
            log.info( "Started" );

            while ( !terminationStarted ) {
                try {
                    Thread.sleep( 5000 );
                } catch ( InterruptedException e ) {
                    log.warn( "Termination wait loop is interrupted", e );
                }
            }
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
}
