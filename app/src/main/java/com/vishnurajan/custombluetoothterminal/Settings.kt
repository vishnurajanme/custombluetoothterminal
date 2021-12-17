package com.vishnurajan.custombluetoothterminal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.ETC1.encodeImage
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream

class Settings : AppCompatActivity() {
    private val pickImage = 0
    private var imageUri: Uri? = null
    lateinit var splashimage : ImageView
    lateinit var aboutImage : ImageView
    lateinit var bitmap : Bitmap
    private var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("bluetoothdata", Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        this.title = sharedPreferences.getString("appnametext", "custom app name here")
        val savebutton = findViewById<Button>(R.id.savebutton)

        val appnametext = findViewById<EditText>(R.id.appnametext)
        val screen1text = findViewById<EditText>(R.id.screen1text)
        val screen2text = findViewById<EditText>(R.id.screen2text)
        val abouttext = findViewById<EditText>(R.id.abouttext)
        val splashButton = findViewById<Button>(R.id.splashButton)
        val aboutButton = findViewById<Button>(R.id.aboutButton)
        splashimage = findViewById<ImageView>(R.id.chosenSplash)
        aboutImage = findViewById<ImageView>(R.id.aboutImage)
        val reset = findViewById<Button>(R.id.reset)

        if (sharedPreferences.getString("splashscreenimage", null) != null) {
            val splashscreenimage = sharedPreferences.getString("splashscreenimage", null)
            val decodedString = Base64.decode(splashscreenimage, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            splashimage.setImageBitmap(decodedByte)
        } else {
            splashimage.setImageResource(R.drawable.robot)
        }

        if (sharedPreferences.getString("aboutimage", null) != null) {
            val aboutimg = sharedPreferences.getString("aboutimage", null)
            val decodedString: ByteArray = Base64.decode(aboutimg, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            aboutImage.setImageBitmap(decodedByte)
        } else {
            aboutImage.setImageResource(R.drawable.aboutimage)
        }

        splashButton.setOnClickListener {
            id = 1
            val gallery = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            gallery.setType("image/*");
            startActivityForResult(gallery, pickImage)
        }

        aboutButton.setOnClickListener {
            id = 2
            val gallery = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            gallery.setType("image/*");
            startActivityForResult(gallery, pickImage)
        }

        reset.setOnClickListener {
            editor.clear().commit()
            Toast.makeText(this, "RESET SUCCESSFUL", Toast.LENGTH_SHORT).show()
        }


        savebutton.setOnClickListener(View.OnClickListener {
            val appnamevalue = appnametext.text.toString()
            val screen1value = screen1text.text.toString()
            val screen2value = screen2text.text.toString()
            val aboutvalue = abouttext.text.toString()

            if (appnamevalue != "") {
                editor.putString("appnametext", appnamevalue).apply()
            }
            if (screen1value != "") {
                editor.putString("screen1text",screen1value).apply()
            }
            if (screen2value != "") {
                editor.putString("screen2text",screen2value).apply()
            }
            if (aboutvalue != "") {
                editor.putString("abouttext",aboutvalue).apply()
            }
            Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show()
            finish()
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("bluetoothdata", Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            val byteArrayOutputStream = ByteArrayOutputStream()

            try {
                bitmap =
                    BitmapFactory.decodeStream(imageUri?.let {
                        this.getContentResolver().openInputStream(
                            it
                        )
                    })
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
            val imageString: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)

            if (id == 1) {
                splashimage.setImageURI(imageUri)
                editor.putString("splashscreenimage", imageString).apply()
            } else if (id == 2) {
                aboutImage.setImageURI(imageUri)
                editor.putString("aboutimage", imageString).apply()
            }
        }
    }

}

