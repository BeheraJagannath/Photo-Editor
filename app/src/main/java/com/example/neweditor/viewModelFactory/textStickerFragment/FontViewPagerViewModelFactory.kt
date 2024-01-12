package com.android.demoeditor.viewModelFactory.viewPager.textStickerFragment

import android.app.Application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neweditor.viewModel.textStrikeViewModel.FontViewPagerViewModel


class FontViewPagerViewModelFactory (private val application: Application  ) :  ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FontViewPagerViewModel::class.java)){
            return FontViewPagerViewModel(application ) as T
        }

        throw IllegalArgumentException(" FontViewPagerViewModel Not Found !!! ")
    }
}