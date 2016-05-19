package com.example.akash.shareit;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.view.GestureDetectorCompat;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Second extends AppCompatActivity   {
    protected static final int SUCCESS_CONNECT = 0;
    EditText input;
    private GestureDetector gestureDetector;

    TextView mousePad;
    protected static final int MESSAGE_READ = 1;
    // public static final UUID MY_UUID = UUID.fromString("9bde4762-89a6-418e-bacf-fcd82f1e0677");
    public static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    BluetoothDevice selectedDevice;
    private EditText mOutEditText;
    private boolean mouseMoved=false;
    private StringBuffer mOutStringBuffer;
    String s;
    private ArrayAdapter<String> mConversationArrayAdapter;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {


            super.handleMessage(msg);
            switch(msg.what){
                case SUCCESS_CONNECT:
                    // DO something

                    ConnectedThread connectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
                    //call the function which will get th text
                    write1(connectedThread);
                    write2(connectedThread);



                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[])msg.obj;
                    String string = new String(readBuf);

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        input = (EditText) findViewById(R.id.editInp);

        BluetoothDevice bluetoothDevice = getIntent().getExtras().getParcelable("btdevice");
        ConnectThread connect = new ConnectThread(bluetoothDevice);
        connect.start();



    }

    public  void write2 (final ConnectedThread connectedThread)
    {
        View gestureView = (View)findViewById(R.id.mousePad);
        gestureView.setClickable(true);
        gestureView.setFocusable(true);
        final GestureDetector.SimpleOnGestureListener gestureListener = new GestureLestener();
        final GestureDetector gd = new GestureDetector( gestureListener);
        gestureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String s = "";
                gd.onTouchEvent(event);
                if (GestureLestener.nn == 880) {
                    s=""+880;
                    GestureLestener.nn=0;
                } else {

                    s = "!?" + -(int) GestureLestener.xx + "," + -(int) GestureLestener.yy+",";

                }

                connectedThread.write(s.getBytes());
                    return false;
                }

        });





    }
    public  void write1(final ConnectedThread connectedThread){

        Toast.makeText(getApplicationContext(),"in the write1 mode ",Toast.LENGTH_SHORT).show();

        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (s.length() > 0) {


                    // String[] a = s.toString().split(" ");
                    String str = "";
                    String s1 = "";
                    int m = start;

                    if (count == 0) {

                        s1 = "{BACKSPACE}";
                        connectedThread.write(s1.getBytes());

                    } else {
                        str = "" + s.toString().charAt(m);
                        connectedThread.write(str.getBytes());
                    }




                }
                else if( count==0){
                    String s1 = "{BACKSPACE}";
                    connectedThread.write(s1.getBytes());
                }
            }


            @Override
            public void afterTextChanged(Editable s) {


            }



        });













    }

    @Override
    protected void onPause() {

        super.onPause();

    }





    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {


            BluetoothSocket tmp = null;
            mmDevice = device;


            try {

                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {

            }
            mmSocket = tmp;
        }

        public void run() {



            try {

                mmSocket.connect();

            }
            catch (IOException connectException) {

                try {
                    mmSocket.close();
                }
                catch (IOException closeException) { }
                return;
            }



            mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();
        }




        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;


            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer;
            int bytes;


            while (true) {
                try {

                    buffer = new byte[1024];
                    bytes = mmInStream.read(buffer);

                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();

                } catch (IOException e) {
                    break;
                }
            }
        }


        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

            } catch (IOException e) { }
        }


        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }


}
