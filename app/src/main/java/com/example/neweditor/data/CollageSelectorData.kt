package com.example.neweditor.data


import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollageSelectorData(val listOfUri:List<Uri>) : Parcelable
