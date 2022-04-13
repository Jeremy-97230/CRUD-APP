package com.example.tuto_chrono

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import com.example.tuto_chrono.db.FacebookDatabase
import java.io.File

class PostsAdapter(
    var mcontext: Context,
    var resource: Int,
    var values: ArrayList<Post>
): ArrayAdapter<Post>(mcontext, resource, values){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val post = values[position]
        val viewItem = LayoutInflater.from(mcontext).inflate(resource, parent, false)
        val db = FacebookDatabase(mcontext)
        val titleItem = viewItem.findViewById<TextView>(R.id.v_itemTitle)
        val imageView = viewItem.findViewById<ImageView>(R.id.v_itemImage)
        val btnMenu = viewItem.findViewById<ImageView>(R.id.v_btnItemMenu)

        titleItem.text = post.title
        imageView.setImageURI(post.image.toUri())

        btnMenu.setOnClickListener {
            val popupMenu = PopupMenu(mcontext, it)
            popupMenu.menuInflater.inflate(R.menu.menu_popup_item, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item->
                when (item.itemId){
                    R.id.v_menu_itemShow -> {
                        Intent(mcontext, DetailPost::class.java).also {
                            it.putExtra("img", post.image )
                            it.putExtra("title", post.title )
                            mcontext.startActivity(it)
                        }
                    }
                    R.id.v_menu_deletItem -> {
                        val resultDelete = db.deletePoste(post.id)
                        if(resultDelete){
                            val  file = File(post.image.toUri().getPath())
                            file.delete()
                            values.removeAt(position)
                            notifyDataSetChanged()
                        }else{
                            Toast.makeText(mcontext, "Eroor de suppression", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                true
            }
            popupMenu.show()
       }

        return viewItem
    }

    fun getBitmap(image: ByteArray): Bitmap{
        val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
        return bitmap
    }
}