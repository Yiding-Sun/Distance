package com.example.sunyiding.distance

import android.Manifest
import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import java.io.File

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
            when (requestCode) {
                GET_PHOTO -> cropPhoto(data!!.data)
                CROP_PHOTO -> {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(photoUri.path))
                    File(photoUri.path).deleteOnExit()
                }
            }
        } else {
            getPhoto()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请读写内存卡内容的权限
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 233)
            
        }
        imageView = MyView(this)
        val textView = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        imageView.layoutParams = textView
        findViewById<RelativeLayout>(layout).addView(imageView)
        imageView.scaleType = ImageView.ScaleType.CENTER
        getPhoto()
    }
}