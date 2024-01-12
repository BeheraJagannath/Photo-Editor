package com.example.neweditor.fragment

import android.app.Application
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.neweditor.R
import com.example.neweditor.data.CommonParcelData
import com.example.neweditor.databinding.FragmentCropBinding
import com.example.neweditor.enmus.ActiveNavArgsData
import com.example.neweditor.fragment.interfacee.CropListener
import com.example.neweditor.viewModel.BitmapHelper
import com.example.neweditor.viewModel.CropScreenViewModel
import com.example.neweditor.viewModelFactory.CropScreenViewModelFactory
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback


class CropFragment : Fragment() {
    lateinit var binding :FragmentCropBinding
    private val navArgs by navArgs<CropFragmentArgs>()
    private lateinit var cropViewModel: CropScreenViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(layoutInflater ,R.layout.fragment_crop, container, false)


        val cropViewModelFactory = CropScreenViewModelFactory(
            requireContext().applicationContext as Application,
            navArgs
        ) {
            // redirect to screen
            findNavController().navigate(R.id.cropFragment_to_homeFragment)

        }

        /**
         * Instantiate [CropScreenViewModel] inside [cropViewModel] variable
         */

        cropViewModel =
            ViewModelProvider(this, cropViewModelFactory).get(CropScreenViewModel::class.java)


        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        binding.cropImageView.setInitialFrameScale(.80f)

        binding.cropViewModel = cropViewModel

        cropViewModel.imgSrcBitmap.observe(viewLifecycleOwner, {

            binding.cropImageView.imageBitmap = it

        })
        startCrop()

        return binding.root
    }



    private fun startCrop() {
        binding.cropImageView.setCropMode(CropImageView.CropMode.FREE)
        binding.onClick = object : CropListener {

            override fun cropFree() {
                binding.cropImageView.setCropMode(CropImageView.CropMode.FREE)

            }

            override fun cropCircle() {
                binding.cropImageView.setCropMode(CropImageView.CropMode.CIRCLE)

            }

            override fun cropOneOne() {
                binding.cropImageView.setCustomRatio(1, 1)

            }

            override fun cropThreeFour() {
                binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4)

            }

            override fun cropFourThree() {
                binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3)

            }

            override fun cropTwoThree() {
                binding.cropImageView.setCustomRatio(2, 3)

            }

            override fun cropNineSixteen() {
                binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_9_16)

            }

            override fun cropSixteenNine() {
                binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9)

            }

            override fun cropSave() {
                saveAsBitmap()

            }
            override fun cropBack() {
                findNavController().navigateUp()

            }

        }

        binding.cropBack.setOnClickListener{
            it.findNavController().navigateUp()

        }

    }

    private fun saveAsBitmap() {
        binding.cropImageView.crop(binding.cropImageView.sourceUri).execute(object : CropCallback {
            override fun onError(e: Throwable?) {
                Log.i(TAG, e?.message.toString())
                Toast.makeText(requireContext(), "Failed to save as Bitmap", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(croppedImg: Bitmap?) {

                val bitmap = binding.cropImageView.croppedBitmap
                val data = getNavArgData(bitmap = bitmap)

                val action = CropFragmentDirections.cropFragmentToEditFragment(data)

                findNavController().navigate(action)

            }
        })
    }

    private fun getNavArgData(uri: Uri? = null, bitmap: Bitmap? = null): CommonParcelData {

        bitmap?.also {
            BitmapHelper.setBitmap(it, true)
        }

        return CommonParcelData(
            uri = uri,
            availableData = if (uri != null) ActiveNavArgsData.URI else ActiveNavArgsData.BITMAP
        )
    }


}



