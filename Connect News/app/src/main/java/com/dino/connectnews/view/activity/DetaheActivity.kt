package com.dino.connectnews.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.dino.connectnews.R

class DetaheActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detahe)

        toolbar = findViewById(R.id.toolbarDetalhe)
        toolbar.navigationIcon = ContextCompat.getDrawable(this,R.drawable.abc_ic_ab_back_mtrl_am_alpha)

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    MainActivity::class.java
                )
            )
        })
        val bundle: Bundle? = intent.extras
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(bundle?.getString("url"))
    }
}
