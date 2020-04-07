package com.dino.leitordebarcode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var resutado : TextView
    }
    lateinit var scanCode :Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resutado = findViewById(R.id.resultText)
        scanCode = findViewById(R.id.btnScan)

        scanCode.setOnClickListener {
            startActivity(Intent(applicationContext,ScanCodeActivity :: class.java))
        }
    }
}
