package com.example.neweditor.fragment

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.neweditor.R
import com.example.neweditor.customView.rotateView.OverlayView.FREESTYLE_CROP_MODE_ENABLE
import com.example.neweditor.customView.rotateView.TransformImageView
import com.example.neweditor.customView.widget.HorizontalProgressWheelView
import com.example.neweditor.data.CommonParcelData
import com.example.neweditor.databinding.FragmentRotateBinding
import com.example.neweditor.enmus.ActiveNavArgsData
import com.example.neweditor.viewModel.BitmapHelper
import com.example.neweditor.viewModel.RotationViewModel
import com.example.neweditor.viewModelFactory.RotationViewModelFactory
import java.lang.Exception
import java.util.*

class RotateFragment : Fragment() {
    lateinit var binding: FragmentRotateBinding

    private lateinit var viewModel: RotationViewModel
    private val ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42
    private val navArgs by navArgs<RotateFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater ,R.layout.fragment_rotate, container, false)

        val application = requireContext().applicationContext as Application
        val factory = RotationViewModelFactory(application, navArgs){
            // redirect to screen
            findNavController().navigate(R.id.rotateFragment_to_homeFragment)
        }
        viewModel = ViewModelProvider(this, factory).get(RotationViewModel::class.java)

        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.btn90Degree.setOnClickListener {
            rotateByAngle(90)

        }
        setAngelText(0f)


        viewModel.imgSrc.observe(viewLifecycleOwner, Observer {
            binding.ucrop.cropImageView.setImageBitmap(it)
        })


        binding.ucrop.overlayView.freestyleCropMode = FREESTYLE_CROP_MODE_ENABLE





        binding.rulerView.apply {
            setMiddleLineColor(ContextCompat.getColor(context, android.R.color.transparent))

        }
        binding.ucrop.cropImageView.setTransformImageListener(object :
            TransformImageView.TransformImageListener {
            override fun onLoadComplete() {
            }

            override fun onLoadFailure(e: Exception) {
            }

            override fun onRotate(currentAngle: Float) {
                setAngelText(currentAngle)
            }

            override fun onScale(currentScale: Float) {
            }
        })


        binding.rulerView.setScrollingListener(object : HorizontalProgressWheelView.ScrollingListener {

            override fun onScrollStart() {
                binding.ucrop.cropImageView.cancelAllAnimations();
            }

            override fun onScroll(delta: Float, totalDistance: Float) {
                binding.ucrop.cropImageView.postRotate(delta / ROTATE_WIDGET_SENSITIVITY_COEFFICIENT);
            }

            override fun onScrollEnd() {
                binding.ucrop.cropImageView.setImageToWrapCropBounds();
            }
        })

        saveImage()
        return binding.root
    }

    private fun saveImage() {
        binding.rotateBack.setOnClickListener{
            it.findNavController().navigateUp()

        }
        binding.rotateSave.setOnClickListener {
            val bitmap = binding.ucrop.cropImageView.croppedBitmap
            bitmap?.apply {
                val data = getNavArgData(bitmap = this)
                navigateToScreen(data)
            }

        }
    }
    private fun navigateToScreen(data: CommonParcelData) {
        val action = RotateFragmentDirections.rotateFragmentToEditFragment(data)
        findNavController().navigate(action)
    }

    /**
     * [getNavArgData] for get the nav arg data
     */
    private fun getNavArgData(uri: Uri? = null, bitmap: Bitmap? = null): CommonParcelData {
        bitmap?.also {
            BitmapHelper.setBitmap(it, true)
        }
        return CommonParcelData(
            uri = uri,
            availableData = if (uri != null) ActiveNavArgsData.URI else ActiveNavArgsData.BITMAP
        )
    }



    private fun setAngelText(angel: Float) {
        binding.rotatedTextShower.text = String.format(Locale.getDefault(), "%.1f°", angel)
    }

    private fun rotateByAngle(angle: Int) {
        binding.ucrop.cropImageView.apply {
            postRotate(angle.toFloat());
            setImageToWrapCropBounds();
        }
    }
}