package com.android.demoeditor.viewModel.viewPager.stickersFragment

import android.app.Application
import android.content.res.TypedArray
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.neweditor.data.StickerData
import java.lang.Exception

class AnimalViewPagerViewModel(application: Application, stickerListArg: TypedArray) :
    AndroidViewModel(application) {

    private val TAG = this::class.java.simpleName


    private var stickerList: TypedArray = stickerListArg

    private val _recyclerViewData = MutableLiveData(getRecyclerViewData())
    val recyclerViewData: LiveData<MutableList<StickerData>>
        get() = _recyclerViewData

    private fun getRecyclerViewData(): MutableList<StickerData> {

        val list = mutableListOf<StickerData>()

        var i = 0
        while (i < stickerList.length()) {
            try {
                val drawable = stickerList.getDrawable(i)!!
               // val bitmap = (drawable as BitmapDrawable).bitmap!!
                val data = StickerData(drawable)
                list.add(data)
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
            i++
        }

        return list
    }





}