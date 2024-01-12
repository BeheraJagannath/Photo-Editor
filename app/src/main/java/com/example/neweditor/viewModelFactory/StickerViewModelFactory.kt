package com.example.neweditor.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neweditor.fragment.StickerFragmentArgs
import com.example.neweditor.viewModel.StickerViewModel

class StickerViewModelFactory(
    private val application: Application,
    private val navArgs: StickerFragmentArgs,
    private val redirectToScreen: () -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StickerViewModel::class.java)) {
            return StickerViewModel(application, navArgs, redirectToScreen) as T
        }
        throw IllegalArgumentException(" StickerViewModel Not Found !!! ")

    }
}