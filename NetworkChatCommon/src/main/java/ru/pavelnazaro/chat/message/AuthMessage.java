package ru.pavelnazaro.chat.message;

public class AuthMessage {

    public String login;
    public String password;

    public AuthMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }

}
