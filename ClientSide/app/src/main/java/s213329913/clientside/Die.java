package s213329913.clientside;

/**
 * Created by s2133 on 2017/11/09.
 */
public class Die {

    int x;
    int y;
    int curDieNum;
    boolean curPlayer;
    int dieID;

    public Die(int x, int y, int curDieNum, int dieID, boolean curPlayer) {
        this.x = x;
        this.y = y;
        this.curDieNum = curDieNum;
        this.dieID = dieID;
        this.curPlayer = curPlayer;
    }
}
