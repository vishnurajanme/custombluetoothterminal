package com.vishnurajan.custombluetoothterminal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static android.content.ContentValues.TAG;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;
    private String deviceName = null;
    private String deviceAddress;
    public static Handler handler;
    public static BluetoothSocket mmSocket;
    public static ConnectedThread connectedThread;
    public static CreateConnectThread createConnectThread;
    boolean connection_status = false;
    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        SharedPreferences sharedPreferences = getSharedPreferences("bluetoothdata",MODE_PRIVATE);
        // calling this activity's function to
        // use ActionBar utility methods
        ActionBar actionBar = getSupportActionBar();

        // providing title for the ActionBar
        actionBar.setTitle(sharedPreferences.getString("appnametext","Your App Name"));



        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // UI Initialization
        final com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton help = findViewById(R.id.help);
        final com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton about = findViewById(R.id.about);
        final Button buttonScreen1 = findViewById(R.id.buttonScreen1);
        final Button buttonScreen2 = findViewById(R.id.buttonScreen2);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final TextView textViewInfo = findViewById(R.id.textViewInfo);
        final Button buttonToggle = findViewById(R.id.buttonToggle);
        final Button buttonToggle1 = findViewById(R.id.buttonToggle1);
        final TextView console = findViewById(R.id.textViewConsole);
        final TextView textViewSent = findViewById(R.id.textViewSent);
        final androidx.appcompat.widget.AppCompatEditText CustomText = findViewById(R.id.customText);
        final Button buttonSend = findViewById(R.id.send);
        final RelativeLayout splashscreen = findViewById(R.id.splashscreen);
        final TextView appname = findViewById(R.id.appname);
        final ImageView signal  = findViewById(R.id.signal);
        final ImageView robot = findViewById(R.id.robot);

        if (sharedPreferences.getString("splashscreenimage", null) != null) {
            String splashscreenimage = sharedPreferences.getString("splashscreenimage", null);
            byte[] decodedString = Base64.decode(splashscreenimage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            robot.setImageBitmap(decodedByte);
        }

        else {
            robot.setImageResource(R.drawable.robot);
        }


        appname.setText(sharedPreferences.getString("appnametext","Your app name"));

        actionBar.hide();
        help.hide();
        about.hide();
        Animation animsplash = new AlphaAnimation(0.0f, 1.0f);
        animsplash.setDuration(50); //You can manage the blinking time with this parameter
        animsplash.setStartOffset(20);
        animsplash.setRepeatMode(Animation.REVERSE);
        animsplash.setRepeatCount(Animation.INFINITE);
        signal.startAnimation(animsplash);

        splashscreen.postDelayed(new Runnable(){
            @Override
            public void run()
            {
                splashscreen.setVisibility(View.GONE);
                actionBar.show();
                animsplash.cancel();
                help.show();
                about.show();
            }
        }, 2200);

        progressBar.setVisibility(View.GONE);

        CustomText.setEnabled(false);
        buttonSend.setEnabled(false);
        buttonToggle.setEnabled(false);
        buttonToggle1.setEnabled(false);

        buttonScreen1.setText(sharedPreferences.getString("screen1text","Joystick Mode"));
        buttonScreen2.setText(sharedPreferences.getString("screen2text","Custom Buttons Mode"));


        // If a bluetooth device has been selected from SelectDeviceActivity
        deviceName = getIntent().getStringExtra("deviceName");
        if (deviceName != null){

            // Get the device address to make BT Connection
            deviceAddress = getIntent().getStringExtra("deviceAddress");
            // Show progree and connection status
            textViewSent.setText("Connecting to " + deviceName + "...");
            progressBar.setVisibility(View.VISIBLE);
            CustomText.setEnabled(true);
            buttonSend.setEnabled(true);
            /*
            This is the most important piece of code. When "deviceName" is found
            the code will call a new thread to create a bluetooth connection to the
            selected device (see the thread code below)
             */
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            createConnectThread = new CreateConnectThread(bluetoothAdapter,deviceAddress);
            createConnectThread.start();
        }


        if (deviceName != null) {
            console.setText("CONNECTED");
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(50); //You can manage the blinking time with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            console.startAnimation(anim);
        }

        /*
        Second most important piece of Code. GUI Handler
         */
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case CONNECTING_STATUS:
                        switch(msg.arg1){
                            case 1:
                                textViewSent.setText("Connected to " + deviceName);
                                textViewInfo.setText("...             ...");
                                progressBar.setVisibility(View.GONE);
                                buttonToggle.setEnabled(true);
                                buttonToggle1.setEnabled(true);
                                CustomText.setEnabled(true);
                                buttonSend.setEnabled(true);
                                connection_status = true;
                                break;
                            case -1:
                                textViewSent.setText("Connection Failure");
                                buttonToggle.setEnabled(false);
                                buttonToggle1.setEnabled(false);
                                CustomText.setEnabled(false);
                                buttonSend.setEnabled(false);
                                textViewInfo.setText("Disconnected from Device");
                                textViewSent.setText("...             ...");
                                console.setText("CONSOLE");
                                console.clearAnimation();
                                progressBar.setVisibility(View.GONE);
                                connection_status = false;
                                deviceName = null;
                                deviceAddress = null;
                                break;
                        }
                        break;

                    case MESSAGE_READ:
                        String arduinoMsg = msg.obj.toString(); // Read message from Arduino
                        textViewInfo.setText("Command Received : " + arduinoMsg);
                        Log.d("vishnu", arduinoMsg);
                        break;
                }
            }
        };



        buttonScreen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to adapter list
                Intent intent = new Intent(MainActivity.this, JoyStick.class);
                intent.putExtra("deviceName", deviceName);
                intent.putExtra("deviceAddress",deviceAddress);
                startActivity(intent);
            }
        });

        buttonScreen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to adapter list
                Intent intent = new Intent(MainActivity.this, CustomButtons.class);
                intent.putExtra("deviceName", deviceName);
                intent.putExtra("deviceAddress",deviceAddress);
                startActivity(intent);
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmdText = CustomText.getText().toString();
                // Move to adapter list
                connectedThread.write(cmdText);
                textViewSent.setText("Command Sent: " + cmdText);
                CustomText.setText("");
            }
        });

        // Button to ON/OFF LED on Arduino Board
        buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmdText = null;
                String btnState = buttonToggle.getText().toString().toLowerCase();
                switch (btnState){
                    case "turn on device 1":
                        buttonToggle.setText("Turn Off Device 1");
                        // Command to turn on LED on Arduino. Must match with the command in Arduino code
                        cmdText = "turn on device 1";
                        break;
                    case "turn off device 1":
                        buttonToggle.setText("Turn On Device 1");
                        // Command to turn off LED on Arduino. Must match with the command in Arduino code
                        cmdText = "turn off device 1";
                        break;
                }
                // Send command to Arduino board
                connectedThread.write(cmdText);
                textViewSent.setText("Command Sent: " + cmdText);
                textViewInfo.setText("...             ...");
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createTextDialog();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createTextDialogabout();
            }
        });

        buttonToggle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmdText = null;
                String btnState1 = buttonToggle1.getText().toString().toLowerCase();
                switch (btnState1){
                    case "turn on device 2":
                        buttonToggle1.setText("Turn Off Device 2");
                        // Command to turn on LED on Arduino. Must match with the command in Arduino code
                        cmdText = "turn on device 2";
                        break;
                    case "turn off device 2":
                        buttonToggle1.setText("Turn On Device 2");
                        // Command to turn off LED on Arduino. Must match with the command in Arduino code
                        cmdText = "turn off device 2";
                        break;
                }
                // Send command to Arduino board
                connectedThread.write(cmdText);
                textViewSent.setText("Command Sent: " + cmdText);
                textViewInfo.setText("...             ...");
            }
        });
    }




    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("bluetoothdata",MODE_PRIVATE);

        final Button buttonScreen1 = findViewById(R.id.buttonScreen1);
        final Button buttonScreen2 = findViewById(R.id.buttonScreen2);
        ActionBar actionBar = getSupportActionBar();
        // providing title for the ActionBar
        actionBar.setTitle(sharedPreferences.getString("appnametext","Your App Name"));
        this.setTitle(sharedPreferences.getString("appnametext","Your App Name"));

        buttonScreen1.setText(sharedPreferences.getString("screen1text","Joystick Mode"));
        buttonScreen2.setText(sharedPreferences.getString("screen2text","Custom Buttons Mode"));
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final TextView textViewInfo = findViewById(R.id.textViewInfo);
        final Button buttonToggle = findViewById(R.id.buttonToggle);
        final Button buttonToggle1 = findViewById(R.id.buttonToggle1);
        final TextView textViewSent = findViewById(R.id.textViewSent);
        /*
        Second most important piece of Code. GUI Handler
         */
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case CONNECTING_STATUS:
                        switch(msg.arg1){
                            case 1:
                                textViewSent.setText("Connected to " + deviceName);
                                textViewInfo.setText("...             ...");
                                progressBar.setVisibility(View.GONE);
                                buttonToggle.setEnabled(true);
                                buttonToggle1.setEnabled(true);
                                connection_status = true;
                                break;
                            case -1:
                                textViewSent.setText("Connection Failure");
                                progressBar.setVisibility(View.GONE);
                                connection_status = false;
                                break;
                        }
                        break;

                    case MESSAGE_READ:
                        String arduinoMsg = msg.obj.toString(); // Read message from Arduino
                        textViewInfo.setText("Command Received : " + arduinoMsg);
                        Log.d("vishnu", arduinoMsg);

                        break;
                }
            }
        };

    }



    /* ============================ Thread to Create Bluetooth Connection =================================== */
    public static class CreateConnectThread extends Thread {

        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
            /*
            Use a temporary object that is later assigned to mmSocket
            because mmSocket is final.
             */
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            UUID uuid = bluetoothDevice.getUuids()[0].getUuid();

            try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.e("Status", "Device connected");
                handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                    Log.e("Status", "Cannot connect to device");
                    handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.run();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    /* =============================== Thread for Data Transfer =========================================== */
    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes = 0; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    /*
                    Read from the InputStream from Arduino until termination character is reached.
                    Then send the whole String message to GUI Handler.
                     */
                    buffer[bytes] = (byte) mmInStream.read();
                    String readMessage;
                    if (buffer[bytes] == '\n'){
                        readMessage = new String(buffer,0,bytes);
                        Log.e("Command Received",readMessage);
                        handler.obtainMessage(MESSAGE_READ,readMessage).sendToTarget();
                        bytes = 0;
                    } else {
                        bytes++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes(); //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e("Send Error","Unable to send message",e);
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    /* ============================ Terminate Connection at BackPress ====================== */
    @Override
    public void onBackPressed() {
        // Terminate Bluetooth Connection and close app
        if (createConnectThread != null){
            createConnectThread.cancel();
        }
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void disconnect() {
        // Terminate Bluetooth Connection
        if (createConnectThread != null){
            createConnectThread.cancel();
        }
    }

    public void createTextDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("HELP")
                .setIcon(R.drawable.help1)
                .setMessage("This application is fully customizable as per your need. Just click on the settings icon and rename appname, button names, screen names etc. You can even customize the splash screen image, about screen image and description on your own. So design your own bluetooth terminal app without knowing android coding. Enjoy");
        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
    });
        AlertDialog alert = builder.create();
        alert.setTitle("Instructions!");
        alert.show();
    }



    public void createTextDialogabout() {
        ImageView aboutimage = new ImageView(this);
        SharedPreferences sharedPreferences = getSharedPreferences("bluetoothdata",MODE_PRIVATE);
        String aboutvalue = (sharedPreferences.getString("abouttext", "Designed and Developed by Dr. Vishnu Rajan as a part of the project CrankurBrain. Visit me at drvishnurajan.wordpress.com You can edit this info and the photo by visiting settings"));
        if (sharedPreferences.getString("aboutimage", null) != null) {
            String splashscreenimage = sharedPreferences.getString("aboutimage", null);
            byte[] decodedString = Base64.decode(splashscreenimage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            aboutimage.setImageBitmap(decodedByte);
        }

        else {
            aboutimage.setImageResource(R.drawable.aboutimage);
        }
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this).
                        setTitle("About Us").
                        setMessage(aboutvalue).
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).
                        setView(aboutimage);
        builder.create().show();
    }

    // method to inflate the options menu when
    // the user opens the menu for the first time
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // methods to control the operations that will
    // happen when user clicks on the action buttons
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        final Button buttonToggle = findViewById(R.id.buttonToggle);
        final Button buttonToggle1 = findViewById(R.id.buttonToggle1);
        final androidx.appcompat.widget.AppCompatEditText CustomText = findViewById(R.id.customText);
        final Button buttonSend = findViewById(R.id.send);
        final TextView textViewInfo = findViewById(R.id.textViewInfo);
        final TextView textViewSent = findViewById(R.id.textViewSent);
        final TextView console = findViewById(R.id.textViewConsole);

        switch (item.getItemId()){
            case R.id.bluetoothconnect:
                if (deviceName == null) {
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.bluetooth_connecting));
                    Intent intent = new Intent(MainActivity.this, SelectDeviceActivity.class);
                    startActivity(intent);
                } else {
                    disconnect();
                deviceName = null;
                deviceAddress = null;
                buttonToggle.setEnabled(false);
                buttonToggle1.setEnabled(false);
                CustomText.setEnabled(false);
                buttonSend.setEnabled(false);
                textViewInfo.setText("Disconnected from Device");
                textViewSent.setText("...             ...");
                console.setText("CONSOLE");
                console.clearAnimation();
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.bluetooth_disconnect));
                }
                break;
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}