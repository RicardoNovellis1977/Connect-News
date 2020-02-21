package com.dino.connectnews.view.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
import com.dino.connectnews.data.model.Article
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class AdapterFavorito : RecyclerView.Adapter<AdapterFavorito.ViewHolder> {

    var articleList: ArrayList<Article>? = null
    var context : Context

    constructor(articleList1: ArrayList<Article>?, context: Context) : super() {
        this.articleList = articleList1
        this.context = context
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemFavotito: View = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.activity_item_favorito, viewGroup, false)
        return ViewHolder(itemFavotito)
    }

    override fun getItemCount(): Int {
        return articleList!!.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val article: Article = articleList!!.get(position)
        viewHolder.bind(article)

        viewHolder.delete?.setOnClickListener {

            excluirNews(article,context,position)
        }

    }

    class ViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var imagem: ImageView? = null
        var titulo: TextView? = null
        var noticia: TextView? = null
        var delete: ImageView? = null
        var favorito :ImageView? = null
        var layoutFavorito: LinearLayout

        init {

            imagem = itemView.findViewById(R.id.image_item)
            titulo = itemView.findViewById(R.id.txtTitulo)
            noticia = itemView.findViewById(R.id.txtNoticias)
            delete = itemView.findViewById(R.id.image_delete)
            favorito = itemView.findViewById(R.id.btn_user_like)
            layoutFavorito = itemView.findViewById(R.id.linearRecyclerFavorito)
        }

        fun bind(article: Article) {

            titulo?.text = article.title
            noticia?.text = article.description

            if (article.urlToImage != null && !article.urlToImage.equals("")) {
                Picasso.get().load(article.urlToImage)
                    .error(R.drawable.ic_logotop)
                    .placeholder(R.drawable.ic_logotop)
                    .into(imagem)
            } else {
                imagem?.setVisibility(View.GONE)
            }

            layoutFavorito.setOnClickListener {

                val intent: Intent = Intent(it.context, DetaheActivity::class.java)
                intent.putExtra("url", article.url)
                it.context.startActivity(intent)
            }

        }
    }

    fun update(articles: ArrayList<Article>) {
        this.articleList = articles
        notifyDataSetChanged()
    }

    fun excluirNews (article : Article , context: Context,position : Int){

        val alertDialog : AlertDialog.Builder  = AlertDialog.Builder(context)
        alertDialog.setTitle("Excluir Noticias")
        alertDialog.setMessage("Você tem certeza que deseja excluir a noticia?")
        alertDialog.setCancelable(false)

        alertDialog.setPositiveButton("Confirmar", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

                val databaseReference =
                    FirebaseDatabase.getInstance().getReference("usuario")
                val autenticacao = FirebaseAuth.getInstance()

                val idUsuario: String
                val idFavorito: DatabaseReference

                idUsuario = autenticacao.currentUser!!.uid
                idFavorito =
                    databaseReference.child(idUsuario).child("favoritos").child(article.chave!!)

                idFavorito.removeValue()

                Toast.makeText(context, "Noticia Removida", Toast.LENGTH_SHORT).show()

                notifyDataSetChanged()
                articleList!!.remove(article)

            }

        })

        alertDialog.setNegativeButton("Cancelar", object  : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                Toast.makeText(context,"Cancelado", Toast.LENGTH_SHORT).show()
            }
        })

        val alert :  AlertDialog = alertDialog.create()
        alert.show()
    }
}