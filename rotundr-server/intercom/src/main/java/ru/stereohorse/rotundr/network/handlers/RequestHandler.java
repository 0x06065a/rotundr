package ru.stereohorse.rotundr.network.handlers;

import ru.stereohorse.rotundr.network.messages.MessageProtos.Message;

public interface RequestHandler {
    public Message handle( Message msg );
}
