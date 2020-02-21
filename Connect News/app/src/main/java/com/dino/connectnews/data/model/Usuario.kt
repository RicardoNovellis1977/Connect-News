package com.dino.connectnews.data.model

class Usuario(
     val nome: String = "",
     val email: String = "",
     val senha: String = "",
     val id : String = "",
     val favoritos : ArrayList<Article>? = null
)

