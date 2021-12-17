package com.vishnurajan.custombluetoothterminal

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class CustomButtons : AppCompatActivity() {


    lateinit var mAdView : AdView

    var buttonName1 = "Device 1"
    var buttonName2 = "Device 2"
    var buttonName3 = "Device 3"
    var buttonName4 = "Device 4"
    var buttonName5 = "Device 5"
    var buttonName6 = "Device 6"
    var buttonName7 = "Device 7"
    var buttonName8 = "Device 8"
    var buttonName9 = "Device 9"
    var buttonName10 = "Device 10"
    var buttonName11 = "Device 11"
    var buttonName12 = "Device 12"

    var buttonValue1 = "Device 1"
    var buttonValue2 = "Device 2"
    var buttonValue3 = "Device 3"
    var buttonValue4 = "Device 4"
    var buttonValue5 = "Device 5"
    var buttonValue6 = "Device 6"
    var buttonValue7 = "Device 7"
    var buttonValue8 = "Device 8"
    var buttonValue9 = "Device 9"
    var buttonValue10 = "Device 10"
    var buttonValue11 = "Device 11"
    var buttonValue12 = "Device 12"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_buttons)

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("bluetoothdata", Context.MODE_PRIVATE)

        this.title = sharedPreferences.getString("screen2text", "Custom Buttons")
        val textViewInfo = findViewById<TextView>(R.id.textViewInfo)
        val console = findViewById<TextView>(R.id.textViewConsole)
        val textViewSent = findViewById<TextView>(R.id.textViewSent)
        val buttonset = findViewById<GridLayout>(R.id.buttonset)

        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button8 = findViewById<Button>(R.id.button8)
        val button9 = findViewById<Button>(R.id.button9)
        val button10 = findViewById<Button>(R.id.button10)
        val button11 = findViewById<Button>(R.id.button11)
        val button12 = findViewById<Button>(R.id.button12)

        button1.text = sharedPreferences.getString("buttonName1","buttonName1");
        button2.text = sharedPreferences.getString("buttonName2","buttonName2");
        button3.text = sharedPreferences.getString("buttonName3","buttonName3");
        button4.text = sharedPreferences.getString("buttonName4","buttonName4");
        button5.text = sharedPreferences.getString("buttonName5","buttonName5");
        button6.text = sharedPreferences.getString("buttonName6","buttonName6");
        button7.text = sharedPreferences.getString("buttonName7","buttonName7");
        button8.text = sharedPreferences.getString("buttonName8","buttonName8");
        button9.text = sharedPreferences.getString("buttonName9","buttonName9");
        button10.text = sharedPreferences.getString("buttonName10","buttonName10");
        button11.text = sharedPreferences.getString("buttonName11","buttonName11");
        button12.text = sharedPreferences.getString("buttonName12","buttonName12");

        var deviceName = intent.getStringExtra("deviceName")
        if (deviceName != null) {
            button1.isEnabled = true;
            button2.isEnabled = true;
            button3.isEnabled = true;
            button4.isEnabled = true;
            button5.isEnabled = true;
            button6.isEnabled = true;
            button7.isEnabled = true;
            button8.isEnabled = true;
            button9.isEnabled = true;
            button10.isEnabled = true;
            button11.isEnabled = true;
            button12.isEnabled = true;

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
            button1.isEnabled = false;
            button2.isEnabled = false;
            button3.isEnabled = false;
            button4.isEnabled = false;
            button5.isEnabled = false;
            button6.isEnabled = false;
            button7.isEnabled = false;
            button8.isEnabled = false;
            button9.isEnabled = false;
            button10.isEnabled = false;
            button11.isEnabled = false;
            button12.isEnabled = false;
            buttonset.isEnabled = false;
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

        button1.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue1","buttonValue1")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })

        button2.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue2","buttonValue2")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })
        button3.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue3","buttonValue3")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })
        button4.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue4","buttonValue4")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })
        button5.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue5","buttonValue5")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })
        button6.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue6","buttonValue6")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })
        button7.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue7","buttonValue7")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })
        button8.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue8","buttonValue8")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })
        button9.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue9","buttonValue9")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })
        button10.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue10","buttonValue10")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })
        button11.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue11","buttonValue11")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })
        button12.setOnClickListener(View.OnClickListener {
            var cmdText: String? = null
            cmdText = sharedPreferences.getString("buttonValue12","buttonValue12")
            // Send command to Arduino board
            MainActivity.connectedThread.write(cmdText)
            textViewInfo.text = "...          ...          ..."
            textViewSent.text = "Command Sent: " + cmdText
        })




        button1.setOnLongClickListener(View.OnLongClickListener {
            createDialog(1)
            true
        })

        button2.setOnLongClickListener(View.OnLongClickListener {
            createDialog(2)
            true
        })

        button3.setOnLongClickListener(View.OnLongClickListener {
            createDialog(3)
            true
        })

        button4.setOnLongClickListener(View.OnLongClickListener {
            createDialog(4)
            true
        })

        button5.setOnLongClickListener(View.OnLongClickListener {
            createDialog(5)
            true
        })

        button6.setOnLongClickListener(View.OnLongClickListener {
            createDialog(6)
            true
        })

        button7.setOnLongClickListener(View.OnLongClickListener {
            createDialog(7)
            true
        })

        button8.setOnLongClickListener(View.OnLongClickListener {
            createDialog(8)
            true
        })

        button9.setOnLongClickListener(View.OnLongClickListener {
            createDialog(9)
            true
        })

        button10.setOnLongClickListener(View.OnLongClickListener {
            createDialog(10)
            true
        })

        button11.setOnLongClickListener(View.OnLongClickListener {
            createDialog(11)
            true
        })

        button12.setOnLongClickListener(View.OnLongClickListener {
            createDialog(12)
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
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button8 = findViewById<Button>(R.id.button8)
        val button9 = findViewById<Button>(R.id.button9)
        val button10 = findViewById<Button>(R.id.button10)
        val button11 = findViewById<Button>(R.id.button11)
        val button12 = findViewById<Button>(R.id.button12)

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
                buttonValue1 = commandText.text.toString()
                buttonName1 = nameText.text.toString()
                editor.putString("buttonValue1",buttonValue1).apply()
                editor.putString("buttonName1",buttonName1).apply()
                button1.text = sharedPreferences.getString("buttonName1","buttonName1");
            } else if (i == 2) {
                buttonValue2 = commandText.text.toString()
                buttonName2 = nameText.text.toString()
                editor.putString("buttonValue2",buttonValue2).apply()
                editor.putString("buttonName2",buttonName2).apply()
                button2.text = sharedPreferences.getString("buttonName2","buttonName2");
            } else if (i == 3) {
                buttonValue3 = commandText.text.toString()
                buttonName3 = nameText.text.toString()
                editor.putString("buttonValue3",buttonValue3).apply()
                editor.putString("buttonName3",buttonName3).apply()
                button3.text = sharedPreferences.getString("buttonName3","buttonName3");
            } else if (i == 4) {
                buttonValue4 = commandText.text.toString()
                buttonName4 = nameText.text.toString()
                editor.putString("buttonValue4",buttonValue4).apply()
                editor.putString("buttonName4",buttonName4).apply()
                button4.text = sharedPreferences.getString("buttonName4","buttonName4");
            } else if (i == 5) {
                buttonValue5 = commandText.text.toString()
                buttonName5 = nameText.text.toString()
                editor.putString("buttonValue5",buttonValue5).apply()
                editor.putString("buttonName5",buttonName5).apply()
                button5.text = sharedPreferences.getString("buttonName5","buttonName5");
            } else if (i == 6) {
                buttonValue6 = commandText.text.toString()
                buttonName6 = nameText.text.toString()
                editor.putString("buttonValue6",buttonValue6).apply()
                editor.putString("buttonName6",buttonName6).apply()
                button6.text = sharedPreferences.getString("buttonName6","buttonName6");
            } else if (i == 7) {
                buttonValue7 = commandText.text.toString()
                buttonName7 = nameText.text.toString()
                editor.putString("buttonValue7",buttonValue7).apply()
                editor.putString("buttonName7",buttonName7).apply()
                button7.text = sharedPreferences.getString("buttonName7","buttonName7");
            } else if (i == 8) {
                buttonValue8 = commandText.text.toString()
                buttonName8 = nameText.text.toString()
                editor.putString("buttonValue8",buttonValue8).apply()
                editor.putString("buttonName8",buttonName8).apply()
                button8.text = sharedPreferences.getString("buttonName8","buttonName8");
            } else if (i == 9) {
                buttonValue9 = commandText.text.toString()
                buttonName9 = nameText.text.toString()
                editor.putString("buttonValue9",buttonValue9).apply()
                editor.putString("buttonName9",buttonName9).apply()
                button9.text = sharedPreferences.getString("buttonName9","buttonName9");
            } else if (i == 10) {
                buttonValue10 = commandText.text.toString()
                buttonName10 = nameText.text.toString()
                editor.putString("buttonValue10",buttonValue10).apply()
                editor.putString("buttonName10",buttonName10).apply()
                button10.text = sharedPreferences.getString("buttonName10","buttonName10");
            } else if (i == 11) {
                buttonValue11 = commandText.text.toString()
                buttonName11 = nameText.text.toString()
                editor.putString("buttonValue11",buttonValue11).apply()
                editor.putString("buttonName11",buttonName11).apply()
                button11.text = sharedPreferences.getString("buttonName11","buttonName11");
            } else if (i == 12) {
                buttonValue12 = commandText.text.toString()
                buttonName12 = nameText.text.toString()
                editor.putString("buttonValue12",buttonValue12).apply()
                editor.putString("buttonName12",buttonName12).apply()
                button12.text = sharedPreferences.getString("buttonName12","buttonName12");
            }
            Toast.makeText(
                applicationContext,
                "Saved!", Toast.LENGTH_SHORT
            )
                .show()
        }
        builder.show()
    }

}