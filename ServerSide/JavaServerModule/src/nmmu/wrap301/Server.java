package nmmu.wrap301;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by s2133 on 2017/11/08.
 */
public class Server {
    private final String BOARD_COMMAND = "#BOARD";
    ServerSocket server;
    Socket socket;
    ClientConnection connection;
    ArrayList<Die> dice = new ArrayList<>();

    ArrayList<ClientConnection> clientConnections;

    public Server() {
        runServer();
    }

    public static void main(String[] args) {
        new Server();
    }

    private void runServer() {
        clientConnections = new ArrayList<>();

        try {
            System.out.println("Starting Server");
            server = new ServerSocket(99);
            System.out.println("SERVER: Server started " + InetAddress.getLocalHost().getHostAddress());


            while (true) {

                socket = server.accept();
                System.out.println("A new client was accepted");
                connection = new ClientConnection(this, socket);
                connection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClient(ClientConnection connection) {

        clientConnections.add(connection);
        publishBoard(connection);

    }

    public void removeClient(ClientConnection connection) {

        clientConnections.remove(connection);

    }

    public void addDie(Die die) {
        dice.add(die);
    }

    public void publishBoard() {
        for (int i = 0; i < clientConnections.size(); i++) {
            publishBoard(connection);
        }
    }
    
    public void publishBoard(ClientConnection connection){
        try {
            connection.output.writeUTF(BOARD_COMMAND);
            connection.output.writeUTF("" + dice.size());
            for (int j = 0; j < dice.size(); j++) {
                if (connection == dice.get(j).player) {
                    connection.output.writeUTF(dice.get(j).toString() + "," + j + ",currentPlayer");
                } else
                    connection.output.writeUTF(dice.get(j).toString() + "," + j + ",notCurrentPlayer");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
