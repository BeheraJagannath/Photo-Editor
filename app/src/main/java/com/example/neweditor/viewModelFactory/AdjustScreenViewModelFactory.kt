package com.example.neweditor.viewModelFactory

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neweditor.fragment.AdjustFragmentArgs
import com.example.neweditor.viewModel.AdjustScreenViewModel

class AdjustScreenViewModelFactory(
    private val application: Application,
    private val navArgs: AdjustFragmentArgs,
    private val activity: Activity,
    private val redirectToScreen:()->Unit,

    ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AdjustScreenViewModel::class.java)){
           return AdjustScreenViewModel(application,navArgs,  activity,redirectToScreen) as T
        }

        throw IllegalArgumentException(" AdjustEditScreenViewModel Not Found !!! ")

    }
}