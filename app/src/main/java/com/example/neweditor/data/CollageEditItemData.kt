package com.example.neweditor.data

import android.graphics.drawable.Drawable
import com.example.neweditor.adapter.CollageIconType
import java.util.*

data class CollageEditItemData(val id:String = UUID.randomUUID().toString(), val iconType: CollageIconType, val icon: Drawable, val iconName:String)
