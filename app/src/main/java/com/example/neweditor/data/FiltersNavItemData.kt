package com.example.neweditor.data

import android.graphics.Bitmap
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import java.util.*

data class FiltersNavItemData (val bitmap: Bitmap?=null, val filter:GPUImageFilter,
                               val uniqueId :String= UUID.randomUUID().toString(),
                               )