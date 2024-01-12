package com.example.neweditor.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neweditor.databinding.FragmentStickertextBinding
import com.example.neweditor.fragment.StickerTextFragmentArgs
import com.example.neweditor.viewModel.TextStickerViewModel

class TextStickerViewModelFactory(
    private val application: Application,
    private val navArgs: StickerTextFragmentArgs,
    private val textStickerBind: FragmentStickertextBinding,
    private val redirectToScreen: () -> Unit,

    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TextStickerViewModel::class.java)) {
            return TextStickerViewModel(
                application,
                navArgs,
                textStickerBind,
                redirectToScreen
            ) as T
        }

        throw IllegalArgumentException(" TextStickerViewModel Not Found !!! ")

    }
}