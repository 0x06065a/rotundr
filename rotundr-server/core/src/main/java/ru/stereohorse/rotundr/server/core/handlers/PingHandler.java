package ru.stereohorse.rotundr.server.core.handlers;

import ru.stereohorse.rotundr.server.messages.MessageProtos.Message;

public class PingHandler implements RequestHandler {
    @Override
    public Message handle( Message msg ) {
        return Message.newBuilder().setOperation( Message.Operation.PONG ).build();
    }
}
