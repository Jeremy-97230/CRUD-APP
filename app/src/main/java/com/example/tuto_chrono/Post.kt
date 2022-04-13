package com.example.tuto_chrono

data class Post(
     var title: String,
     val description: String,
     var image: String
){
     var id: Int = -1
     constructor(id: Int, title: String,description: String, image: String): this(title, description, image){
          this.id = id
     }
}
