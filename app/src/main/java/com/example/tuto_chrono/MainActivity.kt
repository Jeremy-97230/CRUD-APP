package com.example.tuto_chrono

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.example.tuto_chrono.db.FacebookDatabase

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreference: SharedPreferences
    lateinit var db : FacebookDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init constant and variable
        db = FacebookDatabase(this)
        sharedPreference = this.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val btnSubmit = findViewById<Button>(R.id.v_btnSubmit)
        val textMail = findViewById<EditText>(R.id.v_loginInput)
        val textPws = findViewById<EditText>(R.id.v_PwsInput)
        val textbtntoRegister = findViewById<TextView>(R.id.v_textBtnRegister)
        val TextError = findViewById<TextView>(R.id.v_errorText)
        val  isAuth = sharedPreference.getBoolean("auth", false)
        val SharedEmail = sharedPreference.getString("email", "")

        // is auth swip to home
        if(isAuth){
            Intent(this, HomeActivity::class.java).also {
                it.putExtra("email", SharedEmail )
                startActivity(it)
            }
            finish()
        }

        // event btn go to register view
        textbtntoRegister.setOnClickListener {
            Intent(this, Register::class.java).also {
                startActivity(it)
            }
        }

        // event to submit btn form login
        btnSubmit.setOnClickListener{
            TextError.visibility = View.GONE
            if(textMail.text.toString().trim().isEmpty() ||  textPws.text.toString().trim().isEmpty()){

                TextError.text = "Les champs de doivent pas être vide!"
                TextError.visibility = View.VISIBLE

            }else{
                val dbRequest = db.findUser(textMail.text.toString().trim(), textPws.text.toString().trim())
                if ( dbRequest != null){

                    val editor = sharedPreference.edit()
                    editor.putBoolean("auth", true)
                    editor.putString("userName", dbRequest.name)
                    editor.putString("email", textMail.text.toString())
                    editor.apply()

                    Intent(this, HomeActivity::class.java).also {
                        it.putExtra("email", textMail.text.toString().trim() )
                        startActivity(it)
                    }
                    textMail.text.clear()
                    textPws.text.clear()
                    finish()

                }else{
                    TextError.text = "Email ou Mot de pass incorect"
                    TextError.visibility = View.VISIBLE
                }
            }
        }

        // event change input hidden text error
        textMail.doAfterTextChanged {
            TextError.visibility = View.GONE
        }

        // event change input hidden text error
        textPws.doAfterTextChanged {
            TextError.visibility = View.GONE
        }

    }
}