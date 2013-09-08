package ru.stereohorse.rotundr.server.core.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.stereohorse.rotundr.server.core.Server;
import ru.stereohorse.rotundr.server.core.handlers.RequestHandlerRouter;
import ru.stereohorse.rotundr.server.messages.MessageProtos.Message;

import static org.junit.Assert.assertEquals;

public class ServerTest {
    public static final int PORT = 6699;

    private static Server server;

    @BeforeClass
    public static void startServer() throws InterruptedException {
        server = new Server( PORT );
        server.start().syncUninterruptibly();
    }

    @AfterClass
    public static void stopServer() {
        if ( server != null ) {
            server.stop();
        }
    }

    @Test
    public void testRequestHandler() {
        new CommunicationTest() {
            @Override
            protected void setRequestBuilder( Message.Builder builder ) {
                builder.setOperation( Message.Operation.PING );
            }

            @Override
            protected void checkRespose( Message msg ) {
                assertEquals( Message.Operation.PONG, msg.getOperation() );
            }
        }.test();
    }

    private static abstract class CommunicationTest {
        private Message response;

        public void test() {
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            try {
                Bootstrap b = new Bootstrap();
                b.group( workerGroup );
                b.channel( NioSocketChannel.class );
                b.option( ChannelOption.SO_KEEPALIVE, true ); // TODO: is this necessary?
                b.handler( new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel( SocketChannel socketChannel ) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();

                        p.addLast( "frameDecoder", new ProtobufVarint32FrameDecoder() );
                        p.addLast( "protobufDecoder", new ProtobufDecoder( Message.getDefaultInstance() ) );
                        p.addLast( "frameEncoder", new ProtobufVarint32LengthFieldPrepender() );
                        p.addLast( "protobufEncoder", new ProtobufEncoder() );

                        p.addLast( new RequestHandlerRouter() {
                            @Override
                            public void channelActive( final ChannelHandlerContext ctx ) throws Exception {
                                Message.Builder builder = Message.newBuilder();
                                setRequestBuilder( builder );
                                ctx.writeAndFlush( builder.build() );
                            }

                            @Override
                            public void channelRead( ChannelHandlerContext ctx, Object msgObj ) throws Exception {
                                response = (Message) msgObj;
                                ctx.close();
                            }
                        } );
                    }
                } );

                b.connect( "127.0.0.1", PORT ).channel().closeFuture().syncUninterruptibly();
            } finally {
                workerGroup.shutdownGracefully();
            }

            checkRespose( response );
        }

        protected abstract void setRequestBuilder( Message.Builder builder );

        protected abstract void checkRespose( Message msg );
    }
}
