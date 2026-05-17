package ru.bindywashere;

import io.github.cdimascio.dotenv.Dotenv;
import ru.bindywashere.db.DatabaseManager;
import ru.bindywashere.db.MessageDAO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server {
    private MessageDAO messageDAO;
    private ServerSocket serverSocket;
    static Dotenv dotenv;

    public Server(ServerSocket serverSocket, MessageDAO messageDAO) {
        this.serverSocket = serverSocket;
        this.messageDAO = messageDAO;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("a new client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket, messageDAO);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("an error occurred! ");
            e.printStackTrace();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        EnvInit.init();
        dotenv = Dotenv.load();

        int port = Integer.parseInt(dotenv.get("APP_PORT"));

        DatabaseManager dbManager = DatabaseManager.getInstance();
        MessageDAO messageDAO = new MessageDAO(dbManager);

        ServerSocket serverSocket = new ServerSocket(port);
        Server server = new Server(serverSocket, messageDAO);
        server.startServer();
    }


}
