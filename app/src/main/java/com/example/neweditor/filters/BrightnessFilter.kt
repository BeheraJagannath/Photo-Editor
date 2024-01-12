package com.android.demoeditor.filters

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.Script
import com.android.demoeditor.ScriptC_brightness

import com.example.neweditor.filters.FilterConfig


class BrightnessFilter(private val rs : RenderScript) : FilterConfig {

    private val TAG =BrightnessFilter::class.java.simpleName

    private val brightnessScript = ScriptC_brightness(rs)

    override val kernelId: Script.KernelID
        get() = brightnessScript.kernelID_brightness

    override fun destroyFilter() {
        brightnessScript.destroy()
    }

    override fun setInputOnlyForScriptGroup(value: Float, inputAllocationOfGroup: Allocation?) {
        brightnessScript.invoke_setBright(value)
    }

    override fun executeFilter(
        inputAllocation: Allocation,
        outAllocation: Allocation,
        bitmap: Bitmap,
        filterValue: Float
    ): Bitmap {

        brightnessScript.invoke_setBright(filterValue)
        brightnessScript.forEach_brightness(inputAllocation,outAllocation)
        outAllocation.copyTo(bitmap)

        inputAllocation.destroy()
        outAllocation.destroy()

        return bitmap
    }




}