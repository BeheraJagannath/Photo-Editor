package com.android.demoeditor.filters

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.Script
import com.android.demoeditor.ScriptC_saturation
import com.example.neweditor.filters.FilterConfig

class SaturationFilter(
    private val rs : RenderScript
) : FilterConfig {

    private val saturationFilter= ScriptC_saturation(rs)

    override val kernelId: Script.KernelID
        get() = saturationFilter.kernelID_saturation

    override fun setInputOnlyForScriptGroup(value: Float, inputAllocationOfGroup: Allocation?) {
        saturationFilter._saturationValue=value
    }

    override fun destroyFilter() {
        saturationFilter.destroy()
    }

    override fun executeFilter(
        inputAllocation: Allocation,
        outAllocation: Allocation,
        bitmap: Bitmap,
        filterValue: Float
    ): Bitmap {
        saturationFilter._saturationValue=filterValue
        saturationFilter.forEach_saturation(inputAllocation,outAllocation)
        outAllocation.copyTo(bitmap)

        inputAllocation.destroy()
        outAllocation.destroy()

        return bitmap
    }
}