package s213329913.clientside;

import android.view.View;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by s2133 on 2017/11/09.
 */
public class ReceivingInfo extends Thread {

    DataInputStream input;
    ViewClass view;

    private final String BOARDPUBLISHED_COMMAND = "#BOARDPUBLISHED";
    private final String BOARD_COMMAND = "#BOARD";


    public ReceivingInfo(DataInputStream input, View view) {
        this.input = input;
        this.view = (ViewClass) view;
    }

    @Override
    public void run() {
        while (true) {

            try {
                String command = input.readUTF();
                int dieID;

                switch (command) {
                    case BOARD_COMMAND:
                        int numOfDie = Integer.parseInt(input.readUTF());
                        view.dice = new ArrayList<>();
                        for (int i = 0; i < numOfDie; i++) {
                            String[] message = input.readUTF().split(",");
                            int x = Integer.parseInt(message[0]);
                            int y = Integer.parseInt(message[1]);
                            int curDieNum = Integer.parseInt(message[2]);
                            dieID = Integer.parseInt(message[3]);
                            boolean curPlayer;
                            curPlayer = message[4].equals("currentPlayer");

                            view.dice.add(new Die(x, y, curDieNum, dieID, curPlayer));
                            view.invalidate();
                        }
                        break;

                    default:
                        System.out.println("Command error");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
