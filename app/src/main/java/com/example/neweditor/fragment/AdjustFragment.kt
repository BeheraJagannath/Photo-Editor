package com.example.neweditor.fragment

import android.app.Application
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.neweditor.R
import com.example.neweditor.customClass.FilterName
import com.example.neweditor.customView.RulerView
import com.example.neweditor.data.CommonParcelData
import com.example.neweditor.databinding.FragmentAdjustBinding
import com.example.neweditor.enmus.ActiveNavArgsData
import com.example.neweditor.utils.resetSliderValAgain
import com.example.neweditor.viewModel.AdjustScreenViewModel
import com.example.neweditor.viewModel.BitmapHelper
import com.example.neweditor.viewModelFactory.AdjustScreenViewModelFactory


class AdjustFragment : Fragment() {
    lateinit var binding: FragmentAdjustBinding

    private lateinit var adjustViewModel: AdjustScreenViewModel

    private val navArgs by navArgs<AdjustFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil. inflate(layoutInflater ,R.layout.fragment_adjust, container, false)

        val adjustViewModelFactory = AdjustScreenViewModelFactory(
            requireContext().applicationContext as Application,
            navArgs,
            requireActivity(),
        ){
            // redirect to screen
            findNavController().navigate(R.id.adjustFragment_to_homeFragment)

        }

        adjustViewModel = ViewModelProvider(
            this,
            adjustViewModelFactory
        ).get(AdjustScreenViewModel::class.java)

        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        binding.adjustViewModel = adjustViewModel

        /** Setting bitmap in Image-View */
        adjustViewModel.imgBitmap.observe(viewLifecycleOwner, {
            binding.renderScriptImgId.setImageBitmap(it)
        })

        binding.rulerView.setScrollingListener(object : RulerView.ScrollingListener {
            override fun onScrollStart() {
                binding.adjustPercentIndigator.visibility = View.VISIBLE

            }

            override fun onScroll(delta: Float, totalDistance: Float, currentVal: Float) {

                Log.d(TAG, "onScroll: ${adjustViewModel.isImgLoading.value}")

                if (adjustViewModel.isImgLoading.value == false) {
                    adjustViewModel.updateImage(currentVal, binding.renderScriptImgId)
                    binding.adjustPercentIndigator.text =
                        adjustViewModel.calculatePercentAge(currentVal)
                }

            }

            override fun onScrollEnd() {

                binding.adjustPercentIndigator.visibility = View.INVISIBLE
            }
        })



        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.brightnessId -> {
                    resetTab(FilterName.BRIGHTNESS)
                    true
                }
                R.id.contrastId -> {
                    resetTab(FilterName.CONTRAST)
                    true
                }
                R.id.sharpenId -> {
                    resetTab(FilterName.SHARPEN)
                    true
                }
                R.id.saturationId -> {
                    resetTab(FilterName.SATURATION)
                    true
                }
                R.id.vignetteId -> {
                    resetTab(FilterName.VIGNETTE)
                    true
                }

                else -> true

            }


        }

        saveImage()


        return binding.root
    }

    private fun saveImage() {
        binding.adjustBack.setOnClickListener {
            it.findNavController().navigateUp()

        }

        binding.adjustSave.setOnClickListener{
            adjustViewModel.changeTheBitmapToMainScreen {

                val data = getNavArgData(bitmap = it)

                navigateToMainScreen(data)
            }

        }
    }
    private fun navigateToMainScreen(data: CommonParcelData) {

        val action = AdjustFragmentDirections.adjustFragmentToEditFragment(data)

        findNavController().navigate(action)
    }

    private fun toastFun(value: String) {
        Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
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

    private fun resetTab(filterName: FilterName) {
        adjustViewModel.changeRecentTab(filterName) {
            // binding.seek.resetSliderValAgain(adjustViewModel)
            binding.rulerView.resetSliderValAgain(adjustViewModel)

        }
    }


}