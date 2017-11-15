package nmmu.wrap301;


import java.util.Random;

/**
 * Created by s2133 on 2017/11/09.
 */
public class Die {

    int x;
    int y;
    int curDieNum;
    ClientConnection player;

    public Die(int x, int y, ClientConnection player) {
        this.x = x;
        this.y = y;
        this.player = player;
        rollDie();
    }

    public void rollDie() {
        Random random = new Random();
        curDieNum = random.nextInt(6) + 1;
    }

    @Override
    public String toString() {
        return x + "," + y + "," + curDieNum;
    }
}
