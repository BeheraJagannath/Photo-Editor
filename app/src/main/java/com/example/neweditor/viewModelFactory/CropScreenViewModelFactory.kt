package com.example.neweditor.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neweditor.fragment.CropFragmentArgs
import com.example.neweditor.viewModel.CropScreenViewModel

class CropScreenViewModelFactory(
    private val application: Application,
    private val navArgs: CropFragmentArgs,
    private val redirectToScreen: () -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CropScreenViewModel::class.java)) {
            return CropScreenViewModel(application, navArgs, redirectToScreen) as T
        }

        throw IllegalArgumentException(" CropScreenViewModel Not Found !!! ")

    }
}