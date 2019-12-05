package ru.pavelnazaro.chat.main;

import ru.pavelnazaro.chat.Message;
import ru.pavelnazaro.chat.main.auth.AuthService;
import ru.pavelnazaro.chat.main.auth.BaseAuthService;
import ru.pavelnazaro.chat.main.client.ClientHandler;
import ru.pavelnazaro.chat.main.log.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class MyServer {

    private static final int PORT = 8189;

    private final AuthService authService = new BaseAuthService();

    private List<ClientHandler> clients = new ArrayList<>();

    private ServerSocket serverSocket = null;

    MyServer() {

        try {
            Log.init();
            Log.setLog(Level.INFO, "Server is running");

            serverSocket = new ServerSocket(PORT);
            authService.start();
            //noinspection InfiniteLoopStatement
            while (true) {
                Log.setLog(Level.INFO, "Awaiting client connection...");
                Socket socket = serverSocket.accept();
                Log.setLog(Level.INFO, "Client has connected");
                new ClientHandler(socket, this);
            }

        } catch (IOException e) {
            Log.setLog(Level.SEVERE, "Ошибка в работе сервера. Причина: " + e.getMessage());
            e.printStackTrace();
        } finally {
            shutdownServer();
        }
    }

    private void shutdownServer() {
        try {
            authService.stop();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientsList();
    }

    private void broadcastClientsList() {
        List<String> nicknames = new ArrayList<>();
        for (ClientHandler client : clients) {
            nicknames.add(client.getClientName());
        }

        Message message = Message.createClientList(nicknames);
        broadcastMessage(message.toJson());
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientsList();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler client : clients) {
            if (client.getClientName().equals(nick)) {
                return true;
            }
        }

        return false;
    }

    public void broadcastMessage(Message message, ClientHandler... unfilteredClients) {
        broadcastMessage(message.toJson(), unfilteredClients);
    }

    public synchronized void broadcastMessage(String message, ClientHandler... unfilteredClients) {
        List<ClientHandler> unfiltered = Arrays.asList(unfilteredClients);
        for (ClientHandler client : clients) {
            if (!unfiltered.contains(client)) {
                client.sendMessage(message);
            }
        }
    }

    public synchronized void sendPrivateMessage(Message message) {
        for (ClientHandler client : clients) {
            if (client.getClientName().equals(message.privateMessage.to)) {
                client.sendMessage(message.toJson());
                break;
            }
        }
    }
}
