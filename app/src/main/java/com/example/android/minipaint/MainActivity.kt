package com.example.android.minipaint

import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val canvasView = MyCanvasView(this)
        canvasView.systemUiVisibility =  SYSTEM_UI_FLAG_FULLSCREEN
        canvasView.contentDescription = getString(R.string.canvasContentDescription)
        setContentView(canvasView)

    }
}
