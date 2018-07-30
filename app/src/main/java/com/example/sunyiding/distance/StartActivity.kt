package com.example.sunyiding.distance

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        findViewById<Button>(R.id.choose_photo).setOnClickListener {
            val intent = Intent(this@StartActivity, ChooseActivity::class.java)
            startActivity(intent)
        }
    }
}
