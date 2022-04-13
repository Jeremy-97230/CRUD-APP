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
import android.widget.Toast
import com.example.tuto_chrono.db.FacebookDatabase

class Register : AppCompatActivity() {

    lateinit var sharedPreference: SharedPreferences
    lateinit var db: FacebookDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //  init constant and variable
        db = FacebookDatabase(this)
        sharedPreference = this.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val inputName = findViewById<EditText>(R.id.v_register_name)
        val inputEmail = findViewById<EditText>(R.id.v_register_email)
        val inputPassword = findViewById<EditText>(R.id.v_register_password)
        val inputConfirPassword = findViewById<EditText>(R.id.v_register_confPassWord)
        val btnRegister = findViewById<Button>(R.id.v_register_btnSubmit)
        val textError = findViewById<TextView>(R.id.v_register_errorText)
        val textBtnToLogin = findViewById<TextView>(R.id.v_register_textBtnToLogin)

        //return login view
        textBtnToLogin.setOnClickListener {
            finish()
        }

        // function if form corectly completed
        fun isFormOk (): Boolean{

            var result = true
            var errorText = ""

            if (inputConfirPassword.text.toString().trim() != inputPassword.text.toString().trim()){
                result = false
                errorText = "Mot de passe ne correspond pas"
            }
            if (inputPassword.text.toString().trim().isEmpty()){
                result = false
                errorText = "Mot de passe ne doit pas être vide"
            }
            if (inputEmail.text.toString().trim().isEmpty()){
                result = false
                errorText = "Email ne doit pas être vide"
            }
            if (inputName.text.toString().trim().isEmpty()){
                result = false
                errorText = "Nom ne doit pas être vide"
            }

            if (result == false){
                textError.text = errorText
                textError.visibility = View.VISIBLE
            }else{
                textError.visibility = View.GONE
            }

            return result
        }

        // action click btn submit
        btnRegister.setOnClickListener(){
            if (isFormOk()){
                val dataNewUser = User(0, inputName.text.toString().trim(), inputEmail.text.toString().trim(), inputPassword.text.toString().trim())
                val isInserted = db.addUser(dataNewUser)

                if(isInserted){
                    Toast.makeText(this, "Bienvenue parmis nous", Toast.LENGTH_SHORT).show()

                    val editor = sharedPreference.edit()
                    editor.putBoolean("auth", true)
                    editor.putString("email",  dataNewUser.email )
                    editor.putString("userName",  dataNewUser.name )
                    editor.apply()

                    Intent(this, HomeActivity::class.java).also {
                        it.putExtra("email", dataNewUser.email )
                        startActivity(it)
                    }
                }
            }
        }


    }
}