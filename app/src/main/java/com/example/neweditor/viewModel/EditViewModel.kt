package com.example.neweditor.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore.MediaColumns.RESOLUTION
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.neweditor.data.CommonParcelData
import com.example.neweditor.enmus.ActiveNavArgsData
import com.example.neweditor.enmus.ActiveNavArgsData.*
import com.example.neweditor.fragment.EditscreenFragmentArgs

import kotlinx.coroutines.*

class EditViewModel(
    application: Application,
    navArgs: EditscreenFragmentArgs,
    activity: Activity,
    private val redirectToScreen:()->Unit,
) :
    AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext


//    private val repository = MainScreenRepository(context)


    @SuppressLint("StaticFieldLeak")
    private var activity: Activity = activity

    companion object {
        private val TAG by lazy { EditViewModel::class.java.simpleName }
    }


    private val backgroundScope = CoroutineScope(Dispatchers.IO + Job())


    private val _imgSrc = MutableLiveData<Bitmap>()

    val imgSrc: LiveData<Bitmap>
        get() = _imgSrc


    /**
     * is Img Loading
     */
    private val _isImgLoading = MutableLiveData<Boolean>(false)
    val isImgLoading: LiveData<Boolean>
        get() = _isImgLoading


//    val dataForMainScreenNavBarData = MutableLiveData(repository.dataForMainNavBar)


    private lateinit var uriBitmapUtils: UriBitmapUtils

    init {

        setUriOrBitmap(navArgs)

    }


    private fun setUriOrBitmap(navArgs: EditscreenFragmentArgs) {
        when (navArgs.imgData.availableData) {
            BITMAP -> {

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
                            ResizePhoto(context, BitmapHelper.bitmap!!, true,
                                UriBitmapUtils.RESOLUTION.HD_720P
                            )
                        backgroundScope.launch {
                            val resizeBitmap = resizePhoto.execute()
                            withContext(Dispatchers.Main) {
                                _imgSrc.value = resizeBitmap
                                _isImgLoading.value = false
                            }

                        }

                    }

                } else {
                    // Navigate to another screen
                    redirectToScreen()

                }

            }

            URI -> {

                convertUriToBitmap(navArgs.imgData)


            }
            BYTE_ARRAY -> TODO()
        }

    }


    private fun convertUriToBitmap(imgData: CommonParcelData) {
        if (imgData.uri != null) {

            _isImgLoading.value = true

            uriBitmapUtils = UriBitmapUtils(imgData.uri, context, viewModelScope) {
                Log.d(TAG, "URI --> Bitmap converted ")


                withContext(Dispatchers.Main) {
                    Log.d(TAG, "Bitmap --> Resized-Bitmap converted ")

                    _isImgLoading.value = false

                    _imgSrc.value = it

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


//    fun savePhotoInStorage(result: (resultUri: Uri) -> Unit) {
//
//        val saveBitmapInStorage = SaveBitmapInStorage(context)
//        backgroundScope.launch {
//            val uri = saveBitmapInStorage.save(_imgSrc.value!!)
//            delay(3000)
//            withContext(Dispatchers.Main) {
//                result(uri)
//            }
//        }
//
//    }


}