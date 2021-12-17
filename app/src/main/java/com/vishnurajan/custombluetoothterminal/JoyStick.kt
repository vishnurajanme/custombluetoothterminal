package com.vishnurajan.custombluetoothterminal

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import android.view.View.OnLongClickListener
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.content.SharedPreferences
import android.widget.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class JoyStick : AppCompatActivity() {

    lateinit var mAdView : AdView
    var upvalue = "up"
    var stopvalue = "stop"
    var leftvalue = "left"
    var rightvalue = "right"
    var downvalue = "down"

    var buttonName1 = "Device 1"
    var buttonName2 = "Device 2"
    var buttonName3 = "Device 3"
    var buttonName4 = "Device 4"

    var buttonValue1 = "Device 1"
    var buttonValue2 = "Device 2"
    var buttonValue3 = "Device 3"
    var buttonValue4 = "Device 4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joy_stick)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("bluetoothdata", Context.MODE_PRIVATE)

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
// TODO: Add adView to your view hierarchy.

        this.title = sharedPreferences.getString("screen1text", "Joystick Mode")
        val textViewInfo = findViewById<TextView>(R.id.textViewInfo)
        val up = findViewById<Button>(R.id.up)
        val stop = findViewById<Button>(R.id.stop)
        val left = findViewById<Button>(R.id.left)
        val right = findViewById<Button>(R.id.right)
        val down = findViewById<Button>(R.id.down)
        val help = findViewById<Button>(R.id.help)
        val console = findViewById<TextView>(R.id.textViewConsole)
        val textViewSent = findViewById<TextView>(R.id.textViewSent)

        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)

        button1.text = sharedPreferences.getString("buttonName21","buttonName21");
        button2.text = sharedPreferences.getString("buttonName22","buttonName22");
        button3.text = sharedPreferences.getString("buttonName23","buttonName23");
        button4.text = sharedPreferences.getString("buttonName24","buttonName24");

        // If a bluetooth device has been selected from SelectDeviceActivity
        var deviceName = intent.getStringExtra("deviceName")

        if (deviceName != null) {
            up.isEnabled = true
            down.isEnabled = true
            left.isEnabled = true
            right.isEnabled = true
            stop.isEnabled = true
            button1.isEnabled = true;
            button2.isEnabled = true;
            button3.isEnabled = true;
            button4.isEnabled = true;
            textViewInfo.text = "Connected to $deviceName"
            textViewSent.text = "...          ...          ..."

            console.setText("CONNECTED")
            val anim: Animation = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 50 //You can manage the blinking time with this parameter

            anim.startOffset = 20
            anim.repeatMode = Animation.REVERSE
            anim.repeatCount = Animation.INFINITE
            console.startAnimation(anim)
        } else {
            up.isEnabled = false
            down.isEnabled = false
            left.isEnabled = false
            right.isEnabled = false
            stop.isEnabled = false
            button1.isEnabled = false;
            button2.isEnabled = false;
            button3.isEnabled = false;
            button4.isEnabled = false;
            Toast.makeText(this, "Connection failure. Buttons Disabled", Toast.LENGTH_SHORT).show()
        }


        /*
        Second most important piece of Code. GUI Handler
         */MainActivity.handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MainActivity.MESSAGE_READ -> {
                        val arduinoMsg = msg.obj.toString() // Read message from Arduino
                        textViewInfo.setText("Command Received : $arduinoMsg")
                        Log.d("vishnu", arduinoMsg)
                    }
                }
            }
        }

        // Button to ON/OFF LED on Arduino Board

        // Button to ON/OFF LED on Arduino Board
        up.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("upvalue","upvalue")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })

        stop.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("stopvalue","stopvalue")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })

        left.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("leftvalue","leftvalue")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })

        right.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("rightvalue","rightvalue")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })

        down.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("downvalue","downvalue")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })

        button1.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue21","buttonValue21")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })

        button2.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue22","buttonValue22")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })
        button3.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue23","buttonValue23")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })
        button4.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue24","buttonValue24")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })

        help.setOnClickListener(View.OnClickListener {
            createTextDialog();
        })


        up.setOnLongClickListener(OnLongClickListener {
            createDialog(1)
            true
        })

        left.setOnLongClickListener(OnLongClickListener {
            createDialog(2)
            true
        })

        stop.setOnLongClickListener(OnLongClickListener {
            createDialog(3)
            true
        })

        right.setOnLongClickListener(OnLongClickListener {
            createDialog(4)
            true
        })

        down.setOnLongClickListener(OnLongClickListener {
            createDialog(5)
            true
        })

        button1.setOnLongClickListener(View.OnLongClickListener {
            createDialog(6)
            true
        })

        button2.setOnLongClickListener(View.OnLongClickListener {
            createDialog(7)
            true
        })

        button3.setOnLongClickListener(View.OnLongClickListener {
            createDialog(8)
            true
        })

        button4.setOnLongClickListener(View.OnLongClickListener {
            createDialog(9)
            true
        })



    }


    fun createDialog(i: Int) {

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("bluetoothdata", Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()

        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)

        val context: Context = this
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val nameText = EditText(context)
        nameText.hint = "Button Name"
        layout.addView(nameText)

        val commandText = EditText(context)
        commandText.hint = "ASCII Command"
        layout.addView(commandText) // Another add method


        val builder = AlertDialog.Builder(this)
            .setTitle("Edit ASCII code")
            .setMessage("Set your desired ASCII code and name for this button");
        builder.setView(layout)
        builder.setIcon(R.drawable.ic_baseline_adb_24)
        builder.setPositiveButton(
            "SAVE"
        ) { dialog, which -> // Write your code here to execute after dialog
            if (i == 1) {
                upvalue = commandText.text.toString()
                editor.putString("upvalue",upvalue).apply()
            } else if (i == 2) {
                leftvalue = commandText.text.toString()
                editor.putString("leftvalue",leftvalue).apply()
            } else if (i == 3) {
                stopvalue = commandText.text.toString()
                editor.putString("stopvalue",stopvalue).apply()
            } else if (i == 4) {
                rightvalue = commandText.text.toString()
                editor.putString("rightvalue",rightvalue).apply()
            } else if (i == 5) {
                downvalue = commandText.text.toString()
                editor.putString("downvalue",downvalue).apply()
            } else if (i == 6) {
                buttonValue1 = commandText.text.toString()
                buttonName1 = nameText.text.toString()
                editor.putString("buttonValue21",buttonValue1).apply()
                editor.putString("buttonName21",buttonName1).apply()
                button1.text = sharedPreferences.getString("buttonName21","buttonName21");
            } else if (i == 7) {
                buttonValue2 = commandText.text.toString()
                buttonName2 = nameText.text.toString()
                editor.putString("buttonValue22",buttonValue2).apply()
                editor.putString("buttonName22",buttonName2).apply()
                button2.text = sharedPreferences.getString("buttonName22","buttonName22");
            } else if (i == 8) {
                buttonValue3 = commandText.text.toString()
                buttonName3 = nameText.text.toString()
                editor.putString("buttonValue23",buttonValue3).apply()
                editor.putString("buttonName23",buttonName3).apply()
                button3.text = sharedPreferences.getString("buttonName23","buttonName23");
            } else if (i == 9) {
                buttonValue4 = commandText.text.toString()
                buttonName4 = nameText.text.toString()
                editor.putString("buttonValue24",buttonValue4).apply()
                editor.putString("buttonName24",buttonName4).apply()
                button4.text = sharedPreferences.getString("buttonName4","buttonName24");
            }
            Toast.makeText(
                applicationContext,
                "Saved!", Toast.LENGTH_SHORT
            )
                .show()
        }
        builder.show()
    }


    fun createTextDialog() {
        val builder = AlertDialog.Builder(this)
            .setTitle("HELP")
            .setIcon(R.drawable.help1)
            .setMessage("Long press on any of the buttons to edit the bundled command and customize according to your need. All the commands are autosaved on exit");
        builder.setPositiveButton("OKAY") { dialog, which -> // Write your code here to execute after dialog
        }
        builder.show()
    }

}