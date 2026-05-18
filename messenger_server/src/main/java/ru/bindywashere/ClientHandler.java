package ru.bindywashere;

import ru.bindywashere.db.MessageDAO;
import ru.bindywashere.model.Message;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private MessageDAO messageDAO;

    public ClientHandler(Socket socket, MessageDAO messageDAO) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            this.messageDAO = messageDAO;
            clientHandlers.add(this);
            sendHistory();
            broadcastMessage("SERVER >> " + clientUsername + " has entered the chat!");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient == null) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
                Message msg = new Message(clientUsername, messageFromClient);
                messageDAO.saveMessage(msg);
                broadcastMessage(msg.toString());
            } catch (IOException | SQLException e) {
                System.out.println("[SERVER] >> an error occurred on a side of \"" + clientUsername + "\" side: " + e.getMessage());
                e.printStackTrace();

                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    private void sendHistory() {
        try {
            List<Message> history = messageDAO.showHistory();

            if (history.isEmpty()) {
                return;
            }

            bufferedWriter.write("[SERVER] >> --- start of chat history ---");
            bufferedWriter.newLine();
            bufferedWriter.flush();

            for (Message msg : history) {
                bufferedWriter.write(msg.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

            bufferedWriter.write("[SERVER] >> --- end of chat history ---");
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (SQLException | IOException e) {
            System.err.println("[ERROR] couldn't get chat history >> " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (Exception e) {
                System.err.println("[ERROR] >> couldn't send a message to client: " + e.getMessage());
                e.printStackTrace();
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER >> " + clientUsername + " has left the chat!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
