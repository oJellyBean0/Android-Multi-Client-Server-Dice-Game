package s213329913.clientside;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    int portNum = 99;
    String address = "10.122.11.159";
    Socket client;

    DataInputStream input;
    DataOutputStream output;

    ReceivingInfo receiveInfo;
    SendingInfo sendingInfo;

    View appView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnConnect = (Button) findViewById(R.id.btnConnect);
        final Button btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
        final Button btnAddDie = (Button) findViewById(R.id.btnAddDie);

        appView = findViewById(R.id.view);
        appView.setOnTouchListener(this);

        btnConnect.setEnabled(true);
        btnDisconnect.setEnabled(false);
        btnAddDie.setEnabled(false);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Thread newThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            client = new Socket(address, portNum);
                            System.out.println("Connected to: " + address);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnConnect.setEnabled(false);
                                    btnDisconnect.setEnabled(true);
                                    btnAddDie.setEnabled(true);
                                }
                            });

                            input = new DataInputStream(client.getInputStream());
                            output = new DataOutputStream(client.getOutputStream());
                            System.out.println("Got I/O Streams");

                            receiveInfo = new ReceivingInfo(input, appView);
                            receiveInfo.start();
                            sendingInfo = new SendingInfo(output);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                newThread.start();
            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Transmission complete. Closing connection.");

                Thread newThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnConnect.setEnabled(true);
                                    btnDisconnect.setEnabled(false);
                                    btnAddDie.setEnabled(false);
                                }
                            });

                            sendingInfo.disconnect();

                            if (input != null)
                                input.close();
                            if (output != null)
                                output.close();
                            if (client != null)
                                client.close();

                            System.out.println("Connection closed");
                        } catch (IOException e) {
                            System.out.println("Error occurred while closing connection ");
                            e.printStackTrace();
                        }
                    }
                });
                newThread.start();
            }
        });

        btnAddDie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread newThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendingInfo.addDie();
                    }
                });
                newThread.start();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                ((ViewClass) appView).selectDie(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                ((ViewClass) appView).moveDie(event.getX(), event.getY(), sendingInfo);
                break;
            case MotionEvent.ACTION_UP:
                ((ViewClass) appView).rollDie(sendingInfo);
                break;
            default:
                break;
        }
        v.performClick();
        return true;
    }
}
