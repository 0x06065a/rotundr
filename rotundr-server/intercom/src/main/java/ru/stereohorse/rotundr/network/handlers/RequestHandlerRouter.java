package ru.stereohorse.rotundr.network.handlers;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stereohorse.rotundr.network.messages.MessageProtos.Message;

import java.util.HashMap;
import java.util.Map;

public class RequestHandlerRouter extends ChannelInboundHandlerAdapter {
    public static final Logger log = LoggerFactory.getLogger( RequestHandlerRouter.class );

    private static final Map<Message.Operation,RequestHandler> requestHandlers =
            new HashMap<Message.Operation, RequestHandler>();

    static {
        requestHandlers.put( Message.Operation.PING, new PingHandler() );
    }

    @Override
    public void channelRead( final ChannelHandlerContext ctx, Object msgObj ) throws Exception {
        ChannelFuture future = processRequest( ctx, (Message) msgObj );
        if ( future == null ) {
            ctx.close();
        } else {
            future.addListener( new ChannelFutureListener() {
                @Override
                public void operationComplete( ChannelFuture future ) throws Exception {
                    ctx.close();
                }
            } );
        }
    }

    protected ChannelFuture processRequest( ChannelHandlerContext ctx, Message msg ) {
        log.debug( "Received message [{}]", msg.getOperation() );

        RequestHandler requestHandler = requestHandlers.get( msg.getOperation() );
        if ( requestHandler == null ) {
            log.warn( "Unknown operation [{}]", msg.getOperation() );
            return null;
        }

        Message response = requestHandler.handle( msg );
        ChannelFuture future = ctx.writeAndFlush( response );

        log.debug( "Sending response [{}]...", response.getOperation() );
        return future;
    }
}
