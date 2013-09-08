package ru.stereohorse.rotundr.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stereohorse.rotundr.server.core.handlers.RequestHandlerRouter;
import ru.stereohorse.rotundr.server.messages.MessageProtos.Message;

import java.util.concurrent.Future;

public class Server {
    private static final Logger log = LoggerFactory.getLogger( Server.class );

    private int port;
    private EventLoopGroup masters = new NioEventLoopGroup();
    private EventLoopGroup slaves = new NioEventLoopGroup();

    public Server( int port ) {
        this.port = port;
    }

    public Future[] stop() {
        log.info( "Stopping..." );

        return new Future[] {
                slaves.shutdownGracefully(),
                masters.shutdownGracefully()
        };
    }

    public ChannelFuture start() {
        log.info( "Starting on port [{}]...", port );

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group( masters, slaves )
                .channel( NioServerSocketChannel.class )
                .childHandler( new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel( SocketChannel socketChannel ) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        
                        p.addLast( "frameDecoder", new ProtobufVarint32FrameDecoder() );
                        p.addLast( "protobufDecoder", new ProtobufDecoder( Message.getDefaultInstance() ) );
                        p.addLast( "frameEncoder", new ProtobufVarint32LengthFieldPrepender() );
                        p.addLast( "protobufEncoder", new ProtobufEncoder() );
                        
                        p.addLast( getRequestHandler() );
                    }
                } )
                .option( ChannelOption.SO_BACKLOG, 128 )            // TODO: is it mandatory?
                .option( ChannelOption.SO_REUSEADDR, true )
                .childOption( ChannelOption.SO_KEEPALIVE, true );   // TODO: is it mandatory?

        return serverBootstrap.bind( port );
    }

    protected RequestHandlerRouter getRequestHandler() {
        return new RequestHandlerRouter();
    }
}
