package com.example.neweditor.fragment

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.neweditor.HomeFragmentNavigation
import com.example.neweditor.R
import com.example.neweditor.data.CommonParcelData
import com.example.neweditor.databinding.FragmentHomeFragmentBinding
import com.example.neweditor.fragment.interfacee.HomeGridItemListener
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType

class Home_fragment : Fragment() , EasyPermissions.PermissionCallbacks {
   lateinit var binding: FragmentHomeFragmentBinding
    private var view: View? = null

   lateinit var data :CommonParcelData

    private lateinit var navigate: HomeFragmentNavigation
    private  val WRITE_STORAGE_PERMISSION_REQUEST_CODE_AND_READ_STORAGE_PERMISSION_REQUEST_CODE = 201


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_home_fragment,container,false)
        view=binding.root
        binding.lifecycleOwner = this


        startApp()



        return view
    }


    private fun startApp() {

        navigate = HomeFragmentNavigation(findNavController(), getImgPickerObj())

        binding.homeGridListener = object : HomeGridItemListener {
            override fun singlePhoto() {
                navigate.singlePhotoSelector()

            }

            override fun collagePhoto() {
                navigate.colleaguePhotosSelector()
            }

            override fun crop() {
                navigate.gotoCropFragment()

            }

            override fun filter() {
                navigate.gotoFilterFragment()

            }


        }


    }

    private fun hasStorageWritePermissions(): Boolean {
        return EasyPermissions.hasPermissions(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }


    private fun requestReadAndWritePerMission() {
        val deniedPermissionString = "Turn on storage permission to save the photo"
        EasyPermissions.requestPermissions(
            this,
            deniedPermissionString,
            WRITE_STORAGE_PERMISSION_REQUEST_CODE_AND_READ_STORAGE_PERMISSION_REQUEST_CODE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        )

    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestReadAndWritePerMission()
        }

    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

    }


    private fun getImgPickerObj() = object : HomeFragmentNavigation.TedImageSelector {
        override fun singleImagePicker(result: (uri: Uri) -> Unit) {
            if (hasStorageWritePermissions()) {
                TedImagePicker
                    .with(requireContext())
                    .mediaType(MediaType.IMAGE)
                    .dropDownAlbum()
                    .zoomIndicator(false)
                    .start {
                        result(it)
                    }

            } else {
                requestReadAndWritePerMission()
            }

        }

        override fun multiImagePicker(result: (uriList: List<Uri>) -> Unit) {
            if (hasStorageWritePermissions()) {

                val maxLimit = 9
                val minLimit = 1

                TedImagePicker
                    .with(requireContext())
                    .mediaType(MediaType.IMAGE)
                    .dropDownAlbum()
                    .max(maxLimit, "You can't select up-to $maxLimit ")
                    .min(minLimit, "You must be select at least $minLimit")
                    .zoomIndicator(false)
                    .startMultiImage {
                        result(it)
                    }

            } else {
                requestReadAndWritePerMission()
            }

        }
    }
}