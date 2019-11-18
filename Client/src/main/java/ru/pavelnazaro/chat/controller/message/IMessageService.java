package ru.pavelnazaro.chat.controller.message;

import ru.pavelnazaro.chat.Message;

import java.io.Closeable;
import java.io.IOException;

public interface IMessageService extends Closeable {

    void sendMessage(Message message);

    void processRetrievedMessage(Message message) throws IOException;

    @Override
    default void close() throws IOException {
        //Do nothing
    }
}
