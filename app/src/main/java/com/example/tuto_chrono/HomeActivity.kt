package com.example.tuto_chrono

import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.tuto_chrono.db.FacebookDatabase

class HomeActivity : AppCompatActivity() {

    lateinit var listHome: ListView
    lateinit var posteArray: ArrayList<Post>
    lateinit var adapter: PostsAdapter
    lateinit var sharedPreference: SharedPreferences
    lateinit var db: FacebookDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // init contant and variable
        db = FacebookDatabase(this)
        sharedPreference = this.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val sharedUserName = sharedPreference.getString("userName", "")
        listHome = findViewById(R.id.v_listHome)
        var textName = findViewById<TextView>(R.id.v_home_texteName)

        // show name user
        textName.text = "Bonjour " + sharedUserName

        listHome.setOnItemClickListener { adapterView, view, i, l ->
            Intent(this, DetailPost::class.java).also {
                it.putExtra("img", posteArray[i].image )
                it.putExtra("title", posteArray[i].title )
                it.putExtra("description", posteArray[i].description )
                startActivity(it)
            }
        }

        registerForContextMenu(listHome)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.v_menu_addPost ->{
                Toast.makeText(this, "ajouter un post", Toast.LENGTH_SHORT).show()
                Intent(this, AddPostActivity::class.java).also {
                    startActivity(it)
                }
            }

            R.id.v_menu_Setting ->{
                Toast.makeText(this, "Direction Setting", Toast.LENGTH_SHORT).show()
            }

            R.id.v_menu_LogOut ->{
                showAlertLogout()
//                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun showAlertLogout(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Deconnecter?")
        builder.setMessage("Voulez-vous Vraiment vous deconnectez ?")
        builder.setPositiveButton("oui"){ dialoginterface, id ->
            val editor = this.getSharedPreferences("auth", Context.MODE_PRIVATE).edit()
            editor.putBoolean("auth", false).commit()
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }
        builder.setNegativeButton("non"){dialogInterface, id ->
            dialogInterface.dismiss()
        }
        builder.setNeutralButton("Annuler"){dialogInterface, id ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun onStart() {
        super.onStart()
        posteArray = db.findPoste()

        adapter = PostsAdapter(this, R.layout.item_post, posteArray)

        listHome.adapter = adapter
    }

}