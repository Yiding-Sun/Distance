package com.example.sunyiding.distance

import android.content.Intent
import android.provider.MediaStore

class PhotoActivity : SuperActivity(R.layout.activity_main, R.id.layout) {
    override fun getPhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, GET_PHOTO)
    }
    
}