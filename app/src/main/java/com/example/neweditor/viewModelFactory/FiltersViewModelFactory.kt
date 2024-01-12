package com.example.neweditor.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neweditor.fragment.FilterFragmentArgs
import com.example.neweditor.viewModel.FiltersViewModel

class FiltersViewModelFactory(
    private val application: Application,
    val navArgs: FilterFragmentArgs,
    private val redirectToScreen:()->Unit ,

    ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FiltersViewModel::class.java)) {
            return FiltersViewModel(application,navArgs,redirectToScreen) as T
        }

        throw IllegalArgumentException(" FilterViewModel Not Found !!! ")

    }

}