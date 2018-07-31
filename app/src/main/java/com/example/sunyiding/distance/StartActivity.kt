package com.example.sunyiding.distance

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.os.StrictMode
import android.widget.EditText
import java.io.File
import java.io.ObjectOutputStream


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
        findViewById<Button>(R.id.calibration).setOnClickListener {
            val memory = Memory(0f, 0f)
            var editText = EditText(this@StartActivity)
            val SUCCESS = 1
            val FAIL = 2
            val part2handler = Handler() {
                when (it.what) {
                    SUCCESS -> {
                        val os = ObjectOutputStream(File(filesDir.path + "config").outputStream())
                        os.writeObject(memory)
                    }
                    FAIL -> {
                    }
                }
                true
            }
            val part1handler = Handler() {
                when (it.what) {
                    SUCCESS -> {
                        editText = EditText(this@StartActivity)
                        AlertDialog.Builder(this@StartActivity)
                                .setTitle("Enter Distance")
                                .setView(editText)
                                .setPositiveButton("SAVE") { _, _ ->
                                    val input = editText.text.toString().toFloat()
                                    memory.distance = input
                                    val message = Message()
                                    message.what = SUCCESS
                                    part2handler.sendMessage(message)
                                }
                                .setNegativeButton("CANCEL") { _, _ ->
                                    val message = Message()
                                    message.what = FAIL
                                    part2handler.sendMessage(message)
                                }.create().show()
                    }
                    FAIL -> {
                    
                    }
                }
                true
            }
            AlertDialog.Builder(this@StartActivity)
                    .setTitle("Enter Screen")
                    .setView(editText)
                    .setPositiveButton("SAVE") { _, _ ->
                        val input = editText.text.toString().toFloat()
                        memory.screen = input
                        val message = Message()
                        message.what = SUCCESS
                    
                        part1handler.sendMessage(message)
                    }
                    .setNegativeButton("CANCEL") { _, _ ->
                        val message = Message()
                        message.what = FAIL
                        part1handler.sendMessage(message)
                    }.create().show()
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
