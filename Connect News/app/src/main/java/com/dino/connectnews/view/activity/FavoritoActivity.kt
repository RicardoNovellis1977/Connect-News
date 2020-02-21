package com.dino.connectnews.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dino.connectnews.R
import com.dino.connectnews.view.adapter.AdapterFavorito
import com.dino.connectnews.data.model.Article
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavoritoActivity : AppCompatActivity() {

    var recyclerFavorito: RecyclerView? = null
    var articles: ArrayList<Article> = ArrayList<Article>()
    private var databaseReference: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorito)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance()
            .reference.child("usuario").child(firebaseAuth?.getCurrentUser()!!.uid)

        recyclerFavorito = findViewById(R.id.recyclerFavorito)

        val adapterFavorito = AdapterFavorito(ArrayList<Article>(), this)

        val linearLayoutManager = LinearLayoutManager(applicationContext)

        recyclerFavorito?.setLayoutManager(linearLayoutManager)
        recyclerFavorito?.setHasFixedSize(true)
        recyclerFavorito?.setAdapter(adapterFavorito)

        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (itemSnapshot in dataSnapshot.child("favoritos").getChildren()) {
                    val article = itemSnapshot.getValue(Article::class.java)
                    article?.chave = itemSnapshot.key
                    articles.add(article!!)
                }
                adapterFavorito.update(articles)
            }


        })
    }
}
