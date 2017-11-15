package nmmu.wrap301;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

/**
 * Created by s2133 on 2017/11/09.
 */
public class ClientConnection extends Thread {
    private static final String STARTCONNECTION_COMMAND = "#STARTCONNECTION";
    private static final String ADDDIE_COMMAND = "#ADDDIE";
    private static final String ROLLDIE_COMMAND = "#ROLLDIE";
    private static final String MOVEDIE_COMMAND = "#MOVEDIE";
    private static final String CLOSECONNECTION_COMMAND = "#CLOSECONNECTION";
    public Server server;
    public Socket socket;
    public DataInputStream input;
    public DataOutputStream output;


    public ClientConnection(Server server, Socket socket) {

        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            server.addClient(this);

            String command = "";
            while (!command.equals(CLOSECONNECTION_COMMAND)) {
                command = input.readUTF();
                int dieID;

                switch (command) {
                    case STARTCONNECTION_COMMAND:
                        String message = input.readUTF();
                        break;

                    case ADDDIE_COMMAND:
                        Random random = new Random();
                        int x = random.nextInt(800);
                        int y = random.nextInt(800);
                        server.addDie(new Die(x, y, this));
                        server.publishBoard();
                        break;

                    case ROLLDIE_COMMAND:
                        dieID = Integer.parseInt(input.readUTF());
                        server.dice.get(dieID).rollDie();
                        server.publishBoard();
                        break;

                    case MOVEDIE_COMMAND:
                        String[] moveMessage = input.readUTF().split(",");
                        int dieID2 = Integer.parseInt(moveMessage[0]);
                        int x1 = Integer.parseInt(moveMessage[1]);
                        int y1 = Integer.parseInt(moveMessage[2]);
                        server.dice.get(dieID2).x = x1;
                        server.dice.get(dieID2).y = y1;
                        server.publishBoard();
                        break;

                    default:
                        System.out.println("Command error");
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred. The connection may have been lost");
            e.printStackTrace();

        } finally {
            try {
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                System.out.println("An error has occurred while closing the connection");
                e.printStackTrace();
            }

            server.removeClient(this);
        }
    }
}
