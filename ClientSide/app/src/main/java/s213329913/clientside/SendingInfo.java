package s213329913.clientside;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by s2133 on 2017/11/09.
 */
public class SendingInfo {

    DataOutputStream output;

    private final String ADDDIE_COMMAND = "#ADDDIE";
    private final String ROLLDIE_COMMAND = "#ROLLDIE";
    private final String MOVEDIE_COMMAND = "#MOVEDIE";
    private final String CLOSECONNECTION_COMMAND = "#CLOSECONNECTION";

    public SendingInfo(DataOutputStream output) {
        this.output = output;
    }

    public void disconnect() {
        try {
            output.writeUTF(CLOSECONNECTION_COMMAND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDie() {
        try {
            output.writeUTF(ADDDIE_COMMAND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rollDie(int currentDieID) {
        try {
            output.writeUTF(ROLLDIE_COMMAND);
            output.writeUTF(currentDieID+"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void moveDie(int x, int y, int currentDieID) {
        try {
            output.writeUTF(MOVEDIE_COMMAND);
            output.writeUTF(currentDieID+","+x+","+y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
