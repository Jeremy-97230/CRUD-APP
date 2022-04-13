package com.example.tuto_chrono

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.net.toUri

class DetailPost : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)
        supportActionBar?.hide()

        val detailImage = findViewById<ImageView>(R.id.v_detailImageFull)
        val detailTitle = findViewById<TextView>(R.id.v_detailTitle)
        val description = findViewById<TextView>(R.id.v_adp_decription)
        val imageExtract = intent.getByteArrayExtra("img")

        detailImage.setImageURI(intent.getStringExtra("img")?.toUri())
        detailTitle.text = intent.getStringExtra("title")
        description.text = intent.getStringExtra("description")
    }

    fun getBitmap(image: ByteArray): Bitmap {
        val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
        return bitmap
    }
}