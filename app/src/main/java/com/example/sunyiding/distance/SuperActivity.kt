package com.example.sunyiding.distance

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import java.io.File
import java.io.ObjectInputStream

abstract class SuperActivity(val contentView: Int, val layout: Int) : AppCompatActivity() {
    val CROP_PHOTO = 1
    val GET_PHOTO = 2
    lateinit var imageView: MyView
    abstract fun getPhoto()
    val photoUri = Uri.parse("file:////sdcard/image_output.jpg")
    fun cropPhoto(inputUri: Uri) {
        val cropPhotoIntent = Intent("com.android.camera.action.CROP")
        cropPhotoIntent.setDataAndType(inputUri, "image/*")
        cropPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        cropPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(cropPhotoIntent, CROP_PHOTO)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            println("OK")
            when (requestCode) {
                GET_PHOTO -> cropPhoto(data?.data ?: photoUri)
                CROP_PHOTO -> {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(photoUri.path))
                    File(photoUri.path).deleteOnExit()
                }
            }
        } else {
            println("FAILED")
            getPhoto()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
        imageView = MyView(this, ObjectInputStream(File(filesDir.path + "config").inputStream()).readObject() as Memory)
        val textView = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        imageView.layoutParams = textView
        findViewById<RelativeLayout>(layout).addView(imageView)
        imageView.scaleType = ImageView.ScaleType.CENTER
        getPhoto()
    }
}