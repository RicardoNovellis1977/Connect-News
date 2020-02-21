package com.dino.connectnews.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.dino.connectnews.R
import com.dino.connectnews.view.activity.DetaheActivity
import com.dino.connectnews.view.activity.LoginActivity
import com.dino.connectnews.data.model.Article
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList


class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private var articleList: ArrayList<Article>? = ArrayList()

    constructor(articleList: ArrayList<Article>?) : super() {
        this.articleList = articleList
    }

    fun adicioarNews(newsModeloList: ArrayList<Article>?) {
        if (newsModeloList!!.isEmpty()) {
            articleList = newsModeloList
        } else {
            articleList!!.addAll(newsModeloList!!)
        }
        notifyDataSetChanged()
    }


    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ViewHolder {
        val itemNews: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.news_item_view, viewGroup, false)
        return ViewHolder(itemNews)
    }

    override fun onBindViewHolder(@NonNull viewHolder: ViewHolder, i: Int) {
        val article: Article = articleList!![i]
        viewHolder.bind(article)
    }

    override fun getItemCount(): Int {
        return articleList!!.size
    }

    fun update(articleListed: ArrayList<Article>?) {
        articleList = articleListed
        notifyDataSetChanged()
    }

    class ViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imagem: ImageView
        var titulo: TextView
        var noticia: TextView
        var favorito: ImageView
        var compartilhar: ImageView
        var linearDetalhe : LinearLayout
        private val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")
        private val autenticacao = FirebaseAuth.getInstance()
        var emailUsuario: String? = null

        fun bind(article: Article) {
            titulo.setText(article.title)
            noticia.setText(article.description)
            if (article.urlToImage != null && !article.urlToImage.equals("")) {
                Picasso.get().load(article.urlToImage)
                    .resize(500,500)
                    .placeholder(R.drawable.load_holder)
                    .into(imagem)

            } else {
                imagem.visibility = View.GONE
            }

            compartilhar.setOnClickListener {
                val minhaIntent = Intent()
                minhaIntent.action = Intent.ACTION_SEND
                minhaIntent.putExtra(Intent.EXTRA_TEXT, article.url)
                minhaIntent.type = "text/plain"
               it.context.startActivity(minhaIntent)
            }
            favorito.setOnClickListener {
                if (autenticacao.currentUser != null){
                    emailUsuario = autenticacao.currentUser!!.uid
                    databaseReference.child(emailUsuario!!).child("favoritos").push()
                        .setValue(article)
                    Toast.makeText(it.context, "Noticia Salva", Toast.LENGTH_SHORT).show()
                }else{
                    val intent : Intent = Intent(it.context, LoginActivity :: class.java)
                    it.context.startActivity(intent)
                }
            }
            linearDetalhe.setOnClickListener {
                val intent : Intent = Intent(it.context, DetaheActivity :: class.java)
                intent.putExtra("url",article.url)
                it.context.startActivity(intent)
            }
        }
        init {
            imagem = itemView.findViewById(R.id.image_item)
            titulo = itemView.findViewById(R.id.txtTitulo)
            noticia = itemView.findViewById(R.id.txtNoticias)
            favorito = itemView.findViewById(R.id.btn_user_like)
            compartilhar = itemView.findViewById(R.id.imageCompartilhar)
            linearDetalhe = itemView.findViewById(R.id.linearRecycler)
        }
    }
}