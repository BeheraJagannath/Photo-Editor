package com.example.neweditor.data

import android.net.Uri
import android.os.Parcelable
import com.example.neweditor.enmus.ActiveNavArgsData
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommonParcelData(
    val uri:Uri?=null,
    val availableData: ActiveNavArgsData,
                                ): Parcelable
