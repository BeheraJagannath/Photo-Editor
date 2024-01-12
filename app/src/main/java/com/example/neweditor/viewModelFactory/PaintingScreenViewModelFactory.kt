package com.example.neweditor.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neweditor.customView.CustomPhotoEditor
import com.example.neweditor.fragment.PaintFragmentArgs
import com.example.neweditor.viewModel.PaintingScreenViewModel

class PaintingScreenViewModelFactory(
    private val application: Application,
    private val photoEditorBuilder: CustomPhotoEditor,
    private val navArgs: PaintFragmentArgs,
    private val redirectToScreen: () -> Unit,


    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaintingScreenViewModel::class.java)) {
            return PaintingScreenViewModel(
                application,
                photoEditorBuilder,
                navArgs,
                redirectToScreen
            ) as T
        }

        throw IllegalArgumentException(" PaintingScreenViewModel Not Found !!! ")

    }

}