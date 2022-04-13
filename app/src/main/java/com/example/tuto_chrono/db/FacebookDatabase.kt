package com.example.tuto_chrono.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tuto_chrono.Post
import com.example.tuto_chrono.User

class FacebookDatabase(
    mContexte: Context,
    name: String = "facebook_db",
    version: Int = 2
) : SQLiteOpenHelper(
    mContexte,
    name,
    null,
    version
){
    override fun onCreate(db: SQLiteDatabase?) {
       val CreateTableUser = """
            CREATE TABLE users(
                id integer PRIMARY KEY,
                name Varchar(50),
                email varchar(100),
                password varchar(100)
            )
       """.trimIndent()

        val CreateTablePoste = """
            CREATE TABLE poste(
                id integer PRIMARY KEY,
                title Varchar(50),
                description text,
                image Varchar(254)
            )
       """.trimIndent()

        db?.execSQL(CreateTableUser)
        db?.execSQL(CreateTablePoste)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS users")
        db?.execSQL("DROP TABLE IF EXISTS posts")
        onCreate(db)
    }

    fun addUser(user: User): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", user.name)
        values.put("email", user.email)
        values.put("password", user.password)
        val endRequedst = db.insert("users", null, values).toInt()

        db.close()
        return endRequedst != -1
    }

    //User functions

    fun findUser(email: String, password: String) : User? {

        var user: User? = null
        val db = this.readableDatabase
        val selectArgs = arrayOf(email, password)
        val endRequedst = db.query("users", null, "email=? AND password=?", selectArgs, null, null, null)
        if(endRequedst != null && endRequedst.moveToFirst()){
            user = User(endRequedst.getInt(0), endRequedst.getString(1), endRequedst.getString(2), "")
        }

        db.close()
        return user
    }

    // poste functions

    fun addPoste(post: Post): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("title", post.title)
        values.put("description", post.description)
        values.put("image", post.image)
        val endRequedst = db.insert("poste", null, values).toInt()

        db.close()
        return endRequedst != -1
    }

    fun findPoste() : ArrayList<Post> {

        var poste = ArrayList<Post>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM poste"
        val endRequedst = db.rawQuery(selectQuery, null)

        if(endRequedst != null && endRequedst.moveToFirst()){
            do {
                val id = endRequedst.getInt(endRequedst.getColumnIndexOrThrow("id"))
                val title = endRequedst.getString(endRequedst.getColumnIndexOrThrow("title"))
                val description = endRequedst.getString(endRequedst.getColumnIndexOrThrow("description"))
                val image = endRequedst.getString(endRequedst.getColumnIndexOrThrow("image"))
                val tempPost = Post(id,title, description, image)
                poste.add(tempPost)
            }while (endRequedst.moveToNext())
        }
        db.close()
        return poste
    }

    fun deletePoste(id: Int) : Boolean{
        val db = this.writableDatabase
        val rowDeleted = db.delete("poste","id=?", arrayOf(id.toString()))
        db.close()
        return rowDeleted > 0
    }

}