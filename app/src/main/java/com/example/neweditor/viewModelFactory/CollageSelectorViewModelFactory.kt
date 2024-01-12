package com.example.neweditor.viewModelFactory

import android.app.Activity
import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neweditor.viewModel.CollageSelectorViewModel

class CollageSelectorViewModelFactory(
    private val application: Application,
    private val listOfUris: List<Uri>,
    private val activity: Activity,
 ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollageSelectorViewModel::class.java)) {
            return CollageSelectorViewModel(
                application,
                listOfUris,
                activity,

            ) as T
        }

        throw  IllegalArgumentException(" CollageSelectorViewModel  Not Found !!! ")
    }
}