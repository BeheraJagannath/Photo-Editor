package com.example.neweditor

import android.net.Uri
import androidx.navigation.NavController
import com.example.neweditor.data.CollageSelectorData
import com.example.neweditor.data.CommonParcelData
import com.example.neweditor.enmus.ActiveNavArgsData
import com.example.neweditor.fragment.Home_fragmentDirections


class HomeFragmentNavigation(
    private val findNavController: NavController,
    private val imgPicker:TedImageSelector
 ) {

     fun colleaguePhotosSelector() {
        imgPicker.multiImagePicker {
            val action =
                Home_fragmentDirections.homeFragmentToCollageSelectorFragment(
                    CollageSelectorData(it)
                )
            findNavController.navigate(action)

        }


    }


     fun singlePhotoSelector() {
        imgPicker.singleImagePicker  {
            val data = CommonParcelData(
                uri = it,
                availableData = ActiveNavArgsData.URI,

                )
            val action =
                Home_fragmentDirections.homeFragmentToEditScreenFragment(data)


            findNavController.navigate(action)

        }

    }


    /**
     * [gotoAdjustFragment] is for goto adjust fragment
     */
//     fun gotoAdjustFragment() {
//
//        imgPicker.singleImagePicker {
//            val data = CommonParcelData(
//                uri = it,
//                availableData = ActiveNavArgsData.URI,
//
//                )
//
//            val action = HomeFragmentDirections.homeFragmentToAdjustEditFragment(data)
//            findNavController.navigate(action)
//        }
//
//    }
//
//
//    /**
//     *
//     * [gotoFilterFragment] is goto Filter Fragment
//     */
//
     fun gotoFilterFragment() {

        imgPicker.singleImagePicker {
            val data = CommonParcelData(
                uri = it,
                availableData = ActiveNavArgsData.URI,

                )


            val action = Home_fragmentDirections.homeFragmentToFilterFragment(data)

            findNavController.navigate(action)

        }


    }
//
//    /**
//     *
//     * [gotoCropFragment] is goto Crop Fragment
//     */
//
     fun gotoCropFragment() {
        imgPicker.singleImagePicker {
            val data = CommonParcelData(
                uri = it,
                availableData = ActiveNavArgsData.URI,

                )

            val action = Home_fragmentDirections.homeFragmentToCropFragment(data)

            findNavController.navigate(action)

        }


    }

//    /**
//     * [gotoRotatedFragment] is for goto Rotated Fragment
//     */
//
//     fun gotoRotatedFragment() {
//        imgPicker.singleImagePicker {
//            val data = CommonParcelData(
//                uri = it,
//                availableData = ActiveNavArgsData.URI,
//
//                )
//
//            val action = HomeFragmentDirections.homeFragmentToRotationFragment(data)
//
//            findNavController.navigate(action)
//
//        }
//
//    }
//
//
//    /**
//     * [gotoStickerFragment] is for goto Sticker Fragment
//     */
//
//     fun gotoStickerFragment() {
//        imgPicker.singleImagePicker {
//            val data = CommonParcelData(
//                uri = it,
//                availableData = ActiveNavArgsData.URI,
//
//                )
//
//            val action = HomeFragmentDirections.homeFragmentToNewStickerFragment(data)
//
//            findNavController.navigate(action)
//
//        }
//
//    }
//
//
//    /**
//     * [gotoTextStickerFragment] is for goto Sticker Fragment
//     */
//
//
//    private fun gotoTextStickerFragment() {
//        imgPicker.singleImagePicker {
//            val data = CommonParcelData(
//                uri = it,
//                availableData = ActiveNavArgsData.URI,
//
//                )
//
//            val action = HomeFragmentDirections.homeFragmentToTextStickerFragment(data)
//
//
//            findNavController.navigate(action)
//
//        }
//    }



    interface TedImageSelector{
        fun singleImagePicker(result:(uri:Uri)->Unit)
        fun multiImagePicker(result:(uriList:List<Uri>)->Unit)
    }

}



