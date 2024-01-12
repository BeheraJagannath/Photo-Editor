package com.example.neweditor.filters

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Script

interface FilterConfig  {
    val kernelId : Script.KernelID
    fun destroyFilter()
    fun setInputOnlyForScriptGroup(value:Float,inputAllocationOfGroup: Allocation? =null)
    fun executeFilter(inputAllocation: Allocation, outAllocation: Allocation, bitmap: Bitmap, filterValue:Float) : Bitmap

}