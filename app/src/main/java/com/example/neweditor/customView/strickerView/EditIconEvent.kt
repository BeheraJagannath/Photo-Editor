package com.example.neweditor.customView.strickerView

import android.view.MotionEvent

class EditIconEvent(private val listener:(stickerView:  StickerView?)->Unit) : StickerIconEvent {
    override fun onActionDown(stickerView:StickerView?, event: MotionEvent?) {

    }

    override fun onActionMove(stickerView: StickerView?, event: MotionEvent?) {
    }

    override fun onActionUp(stickerView: StickerView?, event: MotionEvent?) {
        listener(stickerView)
     }
}