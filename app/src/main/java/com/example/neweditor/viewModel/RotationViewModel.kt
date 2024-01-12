package com.example.neweditor.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.neweditor.data.CommonParcelData
import com.example.neweditor.enmus.ActiveNavArgsData
import com.example.neweditor.fragment.RotateFragmentArgs
import kotlinx.coroutines.*

class RotationViewModel(
                        application: Application,
                        navArgs: RotateFragmentArgs,
                        private val redirectToScreen:()->Unit,
                        ) :
    AndroidViewModel(application) {

    companion object {
        private val TAG = RotationViewModel::class.java.simpleName
    }

    private val backgroundScope = CoroutineScope(Dispatchers.IO + Job())


    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    /**
     * [_imgSrc] Img Src
     */

    private val _imgSrc = MutableLiveData<Bitmap>()
    val imgSrc: LiveData<Bitmap>
        get() = _imgSrc


    /**
     * is Img Loading
     */
    private val _isImgLoading = MutableLiveData<Boolean>(false)
    val isImgLoading: LiveData<Boolean>
        get() = _isImgLoading


    init {

        setUriOrBitmap(navArgs)

    }

    private fun setUriOrBitmap(navArgs: RotateFragmentArgs) {
        when (navArgs.imgData.availableData) {
            ActiveNavArgsData.BITMAP -> {
                if (BitmapHelper.bitmap != null) {

                    _isImgLoading.value = true

                    /**
                     * if [BitmapHelper.isResized] is [true] don't resize
                     * else [BitmapHelper.isResized] is [false] do resize
                     */

                    if (BitmapHelper.isResized) {

                        _imgSrc.value = BitmapHelper.bitmap
                        _isImgLoading.value = false

                    } else {
                        val resizePhoto =
                            ResizePhoto(context, BitmapHelper.bitmap!!, true, UriBitmapUtils.RESOLUTION.HD_720P)
                        backgroundScope.launch {
                            val resizeBitmap = resizePhoto.execute()
                            withContext(Dispatchers.Main) {
                                _imgSrc.value = resizeBitmap
                                _isImgLoading.value = false
                            }
                        }
                    }


                } else {
                    // navigate screen
                    redirectToScreen()
                }

            }

            ActiveNavArgsData.URI -> {

                _isImgLoading.value = true

                convertUriToBitmap(navArgs.imgData)
            }

            else -> {}
        }

    }


    private fun convertUriToBitmap(imgData: CommonParcelData) {
        if (imgData.uri != null) {
            UriBitmapUtils(imgData.uri, context, backgroundScope) {
                val resizePhoto = ResizePhoto(context, it, true, UriBitmapUtils.RESOLUTION.HD_720P)

                 backgroundScope.launch {

                    val resizeBitmap = resizePhoto.execute()
                    withContext(Dispatchers.Main) {
                        _isImgLoading.value = false
                        _imgSrc.value = resizeBitmap

                    }
                }

            }

        } else {
            throw Exception("Uri not available")
        }
    }


    /**
     * this fun change value of [_isImgLoading]
     */
    fun setLoadingState(value: Boolean) {
        _isImgLoading.value = value
    }


}