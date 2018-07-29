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
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    
    lateinit var imageView:MyView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请读写内存卡内容的权限
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            
        }
        imageView= MyView(this)
        val textView = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        imageView.layoutParams=textView
        findViewById<RelativeLayout>(R.id.layout).addView(imageView)
        imageView.scaleType=ImageView.ScaleType.CENTER
        chooseFromAlbum()
    }
    fun chooseFromAlbum(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 4)
        
    }
    val photoUri=Uri.parse("file:////sdcard/image_output.jpg")
    private fun cropPhoto(inputUri: Uri) {
        val cropPhotoIntent = Intent("com.android.camera.action.CROP")
        cropPhotoIntent.setDataAndType(inputUri, "image/*")
        cropPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        cropPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(cropPhotoIntent,5)
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode== Activity.RESULT_OK){
            when(requestCode){
                4 -> cropPhoto(data!!.data)
                5 -> {
                    println("success")
                    imageView.setImageBitmap(BitmapFactory.decodeFile(photoUri.path))
                    File(photoUri.path).deleteOnExit()
                }
            }
        }else{
            chooseFromAlbum()
        }
    }
}
class MyView(val activity: MainActivity):ImageView(activity){
    var p1=Pair(0f,0f)
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN ->{
                if(p1==Pair(0f,0f)) {
                    println("delta = " + (width - event.x))
                    p1 = Pair(width - event.x, height - event.y)
                    activity.chooseFromAlbum()
                }else{
                    val p2 = Pair(width - event.x, height - event.y)
                    val screenDis=Math.sqrt((p1.first-p2.first)*(p1.first-p2.first)+(p1.second-p2.second)*(p1.second-p2.second).toDouble())
                    val realDis=3616/screenDis
                    println("screenDis = $screenDis")
                    Toast.makeText(activity,"$realDis",Toast.LENGTH_SHORT).show()
                }
            }
        }
        return false
    }
}
data class Memory(val screen:Float,val distance:Float):Serializable