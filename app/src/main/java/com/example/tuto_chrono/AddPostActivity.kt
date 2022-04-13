package com.example.tuto_chrono

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tuto_chrono.db.FacebookDatabase
import java.io.*
import java.util.*

class AddPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        // init constant and variable
        val db = FacebookDatabase(this)
        var imageUpdate = findViewById<ImageView>(R.id.v_addPost_imageView)
        var bitmapResult: Bitmap? = null
        val inputTitle = findViewById<EditText>(R.id.v_addPost_inputTitle)
        val inputDescrip = findViewById<EditText>(R.id.v_addPost_inputDescrip)
        val textError = findViewById<TextView>(R.id.v_addPost_textError)
        val btnAdd = findViewById<Button>(R.id.v_addPost_btnAdd)

        //if form ok submit
        fun formChek() :Boolean{
            var final = true
            if(inputDescrip.text.toString().trim().isEmpty()){
                final= false
                textError.text = "Ecrire une description"
            }
            if (inputTitle.text.toString().trim().isEmpty()){
                final = false
                textError.text = "Titre obligatoire"
            }
            if (bitmapResult == null){
                final = false
                textError.text = "Selectionnez une Image"
            }

            if (!final){
                textError.visibility = View.VISIBLE
            }
            return final
        }

        // function convert bitmap to byteArray
        fun BitmapToByteArray(bitmap: Bitmap ) :ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            val image = stream.toByteArray()
            return image
        }

        // function save file local storage
        fun SaveImageGo (btmImg: Bitmap) :Uri {

            // Get the context wrapper instance
            val wrapper = ContextWrapper(applicationContext)

            // Initializing a new file
            // The bellow line return a directory in internal storage
            var file = wrapper.getDir("images", Context.MODE_PRIVATE)

            // Create a file to save the image
            file = File(file, "${UUID.randomUUID()}.jpg")

            try {
                // Get the file output stream
                val stream: OutputStream = FileOutputStream(file)

                // Compress bitmap
                btmImg.compress(Bitmap.CompressFormat.JPEG, 100, stream)

                // Flush the stream
                stream.flush()

                // Close stream
                stream.close()
                print("image save local  ok")

            }catch (e: IOException){
                e.printStackTrace()
            }

            return Uri.parse(file.absolutePath)
        }

        // action reception image
        val getResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                val inputStream = contentResolver.openInputStream(it)
                bitmapResult = BitmapFactory.decodeStream(inputStream)
                imageUpdate.setImageBitmap(bitmapResult)
            }
        }

        //event image click
        imageUpdate.setOnClickListener() {
            getResult.launch("image/*")
        }

        //event click add button
        btnAdd.setOnClickListener(){
            if(formChek()){
                textError.visibility = View.GONE

                val uriLocalSave = SaveImageGo(bitmapResult!!)
                val newPoste = Post(inputTitle.text.toString().trim(), inputDescrip.text.toString().trim(), uriLocalSave.toString())

                if(db.addPoste(newPoste)){
                    finish()
                }else{
                    textError.text = "Erreur de publication essay plustard"
                    textError.visibility = View.VISIBLE
                }
            }
        }
    }

}