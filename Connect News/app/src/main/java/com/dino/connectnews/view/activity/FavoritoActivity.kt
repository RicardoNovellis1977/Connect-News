package com.dino.connectnews.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
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
    private lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorito)

        toolbar = findViewById(R.id.toolbarFavoritos)
        toolbar.navigationIcon = ContextCompat.getDrawable(this,R.drawable.abc_ic_ab_back_mtrl_am_alpha)

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    MainActivity::class.java
                )
            )
        })
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
