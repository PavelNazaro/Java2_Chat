package ru.pavelnazaro.chat.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import ru.pavelnazaro.chat.Message;
import ru.pavelnazaro.chat.controller.message.IMessageService;
import ru.pavelnazaro.chat.controller.message.ServerMessageService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static ru.pavelnazaro.chat.Message.createAuth;

public class PrimaryController implements Initializable {

    public static final String ALL_ITEM = "All";

    @FXML public TextArea chatTextArea;
    @FXML public TextField messageText;
    @FXML public Button sendMessageButton;

    @FXML public MenuBar menuBar;

    @FXML public TextField loginField;
    @FXML public PasswordField passField;

    @FXML public Button btnAuth;
    @FXML public VBox authPanel;
    @FXML public GridPane chatPanel;

    public @FXML ListView<String> clientList;

    private String nickName;

    private IMessageService messageService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.messageService = new ServerMessageService(this, true);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("oops! Something went wrong!");
        alert.setHeaderText(e.getMessage());

        VBox dialogPaneContent = new VBox();

        Label label = new Label("Stack Trace:");

        String stackTrace = ExceptionUtils.getStackTrace(e);
        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);

        dialogPaneContent.getChildren().addAll(label, textArea);

        // Set content for Dialog Pane
        alert.getDialogPane().setContent(dialogPaneContent);
        alert.setResizable(true);
        alert.showAndWait();

        e.printStackTrace();
    }

    @FXML
    public void sendMessage(ActionEvent actionEvent) {
        sendMessage();
    }

    private void sendMessage() {
        String message = messageText.getText();
        if (StringUtils.isNotBlank(message)) {
            chatTextArea.appendText(String.format("Я: %s%n", message));
            Message msg = buildMessage(message);
            messageService.sendMessage(msg);
            messageText.clear();
        }
    }

    private Message buildMessage(String message) {
        String selectedNickname = clientList.getSelectionModel().getSelectedItem();
        if (selectedNickname != null && !selectedNickname.equals(ALL_ITEM)) {
            return Message.createPrivate(nickName, selectedNickname, message);
        }

        return Message.createPublic(nickName, message);
    }

    public void shutdown() {
        try {
            messageService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sendAuth(ActionEvent actionEvent) {
        String login = loginField.getText();
        String password = passField.getText();
        if (!(login.equals("") && password.equals(""))){
            messageService.sendMessage(createAuth(login, password));
        }
    }

    public void showChatPanel() {
        authPanel.setVisible(false);
        chatPanel.setVisible(true);
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        refreshWindowTitle(nickName);
    }

    private void refreshWindowTitle(String nickName) {
        Stage stage = (Stage) chatPanel.getScene().getWindow();
        stage.setTitle(nickName);
    }

    public void showAuthError(String errorMsg) {
        if (authPanel.isVisible()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Authentication is failed");
            alert.setContentText(errorMsg);
            alert.showAndWait();
        }
    }

    public void refreshUsersList(List<String> onlineUserNicknames) {
        onlineUserNicknames.add(ALL_ITEM);
        clientList.setItems(FXCollections.observableArrayList(onlineUserNicknames));
    }

    @FXML private void handleAboutAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Create by PavelNazaro");
        alert.setHeaderText("About");
        alert.show();
    }

    @FXML private void handleExitAction(ActionEvent event) {
        Platform.exit();
    }
}