package ru.stereohorse.rotundr.network.handlers;

import ru.stereohorse.rotundr.network.messages.MessageProtos.Message;

public class PingHandler implements RequestHandler {
    @Override
    public Message handle( Message msg ) {
        return Message.newBuilder().setOperation( Message.Operation.PONG ).build();
    }
}
