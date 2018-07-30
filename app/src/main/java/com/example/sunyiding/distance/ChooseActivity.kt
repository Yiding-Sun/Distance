package com.example.sunyiding.distance

import android.content.Intent

class ChooseActivity : SuperActivity(R.layout.activity_main, R.id.layout) {
    
    override fun getPhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, GET_PHOTO)
    }
}

