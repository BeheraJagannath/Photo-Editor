package com.example.neweditor.viewModel

import android.graphics.Bitmap

object BitmapHelper {


    private var _isResized = false

    val isResized:Boolean
    get() = _isResized



    private var _bitmap: Bitmap? = null
    val bitmap: Bitmap?
        get() = _bitmap


    fun setBitmap(bitmapImg:Bitmap,resize: Boolean){
        _bitmap=bitmapImg
        _isResized=resize
    }



}