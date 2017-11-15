package s213329913.clientside;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by s2133 on 2017/11/09.
 */
public class ViewClass extends View {

    ArrayList<Die> dice = new ArrayList<>();
    Bitmap[] dieBitmaps = new Bitmap[6];
    int currentDie = -1;
    private int dieHeight = 100;
    private int dieWidth = 100;

    public ViewClass(Context context) {
        super(context);
        initialise();
    }

    public ViewClass(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    public ViewClass(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise();
    }

    private void initialise() {
        tint.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN));
        for (int i = 0; i < 6; i++)
            dieBitmaps[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("die" + String.valueOf(i + 1), "drawable", "s213329913.clientside")), dieWidth, dieHeight, false);
    }

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint tint = new Paint();


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        for (int i = 0; i < dice.size(); i++) {
            Die die = dice.get(i);
            if (die.curPlayer)
                canvas.drawBitmap(dieBitmaps[die.curDieNum - 1], die.x, die.y, paint);
            else canvas.drawBitmap(dieBitmaps[die.curDieNum - 1], die.x, die.y, tint);
        }
    }


    public void selectDie(float x, float y) {
        for (int i = dice.size() - 1; i >= 0; i--) {
            Die cur = dice.get(i);
            float xDiff = x - cur.x;
            float yDiff = y - cur.y;
            if (0 <= xDiff && xDiff < dieWidth && 0 <= yDiff && yDiff < dieHeight) {
                currentDie = i;
                return;
            }
        }
        currentDie = -1;
    }

    public void moveDie(final float x, final float y, final SendingInfo sendInfo) {
        if (currentDie > -1) {
            Die cur = dice.get(currentDie);
            if (!(cur.x == ((int) x) && cur.y == ((int) y))) {
                Thread newThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendInfo.moveDie((int) x, (int) y, currentDie);
                    }
                });
                newThread.start();
            }
        }
    }

    public void rollDie(final SendingInfo sendInfo) {
        if (currentDie > -1) {
            final int id = currentDie;
            Thread newThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sendInfo.rollDie(id);
                }
            });
            newThread.start();
            currentDie = -1;
        }
    }
}
