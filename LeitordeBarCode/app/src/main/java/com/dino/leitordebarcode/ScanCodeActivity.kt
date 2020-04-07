package com.dino.leitordebarcode

import android.Manifest
import android.app.ActionBar
import android.content.pm.PackageManager
import android.graphics.Camera
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.*
import android.view.ViewGroup.LayoutParams.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanCodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler{

    lateinit var ScannerView : ZXingScannerView
    var MY_PERMISSIONS_REQUEST_CAMERA = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScannerView = ZXingScannerView(this)
        setContentView(ScannerView)
    }

    override fun handleResult(p0: Result?) {

        if (p0 != null){
            MainActivity.resutado.text = p0?.text.toString()
        }else{
            Toast.makeText(applicationContext,"Scan Cancelado", Toast.LENGTH_LONG).show()
        }

        onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        ScannerView.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        ScannerView.setResultHandler(this)
        ScannerView.startCamera()

    }

    override fun onPostResume() {
        super.onPostResume()
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                MY_PERMISSIONS_REQUEST_CAMERA
            )
        }
        ScannerView.setResultHandler(this)
        ScannerView.startCamera()
    }
}
