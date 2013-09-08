package ru.stereohorse.rotundr.server.core.handlers;

import ru.stereohorse.rotundr.server.messages.MessageProtos.Message;

public interface RequestHandler {
    public Message handle( Message msg );
}
