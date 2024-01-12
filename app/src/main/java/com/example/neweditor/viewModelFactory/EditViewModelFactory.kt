package com.example.neweditor.viewModelFactory

 import android.app.Activity
 import android.app.Application
 import androidx.lifecycle.ViewModel
 import androidx.lifecycle.ViewModelProvider
 import com.example.neweditor.fragment.EditscreenFragmentArgs
 import com.example.neweditor.viewModel.EditViewModel

class EditViewModelFactory(private val application: Application,
                           private val navArgsData: EditscreenFragmentArgs,
                           private val activity: Activity,
                           private val redirectToScreen:()->Unit,

                           ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)){
            return EditViewModel(application , navArgsData,activity,redirectToScreen) as T
        }

        throw IllegalArgumentException(" MainEditViewModel Not Found !!! ")

    }
}