package com.example.neweditor.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neweditor.fragment.RotateFragmentArgs
import com.example.neweditor.viewModel.RotationViewModel

class RotationViewModelFactory(
    private val application: Application,
    private val navArg: RotateFragmentArgs,
    private val redirectToScreen: () -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RotationViewModel::class.java)) {
            return RotationViewModel(application, navArg, redirectToScreen) as T
        }

        throw IllegalArgumentException(" RotationViewModel Not Found !!! ")


    }
}