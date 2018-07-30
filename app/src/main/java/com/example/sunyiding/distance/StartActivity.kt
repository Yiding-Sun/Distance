package com.example.sunyiding.distance

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.os.StrictMode



class StartActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        findViewById<Button>(R.id.choose_photo).setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 233)
        
            } else {
                initCamara()
                chooseActivity()
            }
        }
        findViewById<Button>(R.id.take_photo).setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CAMERA),
                        233)
            } else {
                initCamara()
                photoActivity()
            }
        }
    }
    
    private fun chooseActivity() {
        val intent = Intent(this@StartActivity, ChooseActivity::class.java)
        startActivity(intent)
    }
    
    private fun photoActivity() {
        val intent = Intent(this@StartActivity, PhotoActivity::class.java)
        startActivity(intent)
    }
    
    private fun initCamara() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Activity.RESULT_OK) {
            for (i in permissions) {
                when (i) {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                        chooseActivity()
                    }
                    Manifest.permission.CAMERA -> {
                        photoActivity()
                    }
                }
            }
        }
    }
}
