package ru.pavelnazaro.chat.controller.message;

import javafx.scene.control.TextArea;
import ru.pavelnazaro.chat.Message;

public class MockMessageService implements IMessageService {

    private TextArea chatTextArea;

    public MockMessageService(TextArea chatTextArea) {
        this.chatTextArea = chatTextArea;
    }

    @Override
    public void sendMessage(Message message) {
        System.out.printf("Message %s has been sent%n", message.toJson());
        chatTextArea.appendText(message + System.lineSeparator());
//        processRetrievedMessage(message);
    }

    @Override
    public void processRetrievedMessage(Message message) {
        throw new UnsupportedOperationException();
//        chatTextArea.appendText(message + System.lineSeparator());
    }
}
