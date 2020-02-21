package com.dino.connectnews.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.dino.connectnews.R
import java.util.*

class SplahActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splah)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                jump(null)
            }
        }, 5000)
        val image = findViewById(R.id.imageSplash) as ImageView
        val animation = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.animation_blink
        )
        image.startAnimation(animation)
    }

    fun jump(view: View?) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}
