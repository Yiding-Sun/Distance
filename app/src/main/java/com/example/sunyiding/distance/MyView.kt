package com.example.sunyiding.distance

import android.view.MotionEvent
import android.widget.ImageView
import android.widget.Toast

class MyView(val activity: SuperActivity, val config: Memory) : ImageView(activity) {
    var p1 = Pair(0f, 0f)
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                if (p1 == Pair(0f, 0f)) {
                    println("delta = " + (width - event.x))
                    p1 = Pair(width - event.x, height - event.y)
                    activity.getPhoto()
                } else {
                    val p2 = Pair(width - event.x, height - event.y)
                    val screenDis = Math.sqrt((p1.first - p2.first) * (p1.first - p2.first) + (p1.second - p2.second) * (p1.second - p2.second).toDouble())
                    val realDis = config.distance * config.screen / screenDis
                    println("screenDis = $screenDis")
                    Toast.makeText(activity, "$realDis", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return false
    }
}