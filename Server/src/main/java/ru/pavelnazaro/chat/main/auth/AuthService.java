package ru.pavelnazaro.chat.main.auth;

import javax.annotation.Nullable;

public interface AuthService {

    void start();
    void stop();

    /**
     *
     * @param login
     * @param pass
     * @return nick or null
     */
    @Nullable
    String getNickByLoginPass(String login, String pass);

}
