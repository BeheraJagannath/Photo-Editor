package com.example.neweditor.fragment

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.neweditor.R
import com.example.neweditor.adapter.ColorPickerAdapter
import com.example.neweditor.adapter.ColorPickerItemListener
import com.example.neweditor.customView.CustomPhotoEditor
import com.example.neweditor.data.CommonParcelData
import com.example.neweditor.databinding.FragmentPaintBinding
import com.example.neweditor.enmus.ActiveNavArgsData
import com.example.neweditor.viewModel.BitmapHelper
import com.example.neweditor.viewModel.PaintingScreenViewModel
import com.example.neweditor.viewModelFactory.PaintingScreenViewModelFactory
import com.google.android.material.slider.Slider
import com.google.android.material.tabs.TabLayout


class PaintFragment : Fragment() {
    lateinit var binding: FragmentPaintBinding
    private lateinit var paintViewModel: PaintingScreenViewModel
    private val navArgs by navArgs<PaintFragmentArgs>()
    private lateinit var colorPickerRecyclerAdapter: ColorPickerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_paint, container, false)

        postponeEnterTransition()
        binding.paintView.source.post {
            startPostponedEnterTransition()
        }
        /**
         * Don't touch upper 3 lines
         */


        val photoEditorBuilder = CustomPhotoEditor(requireContext(), binding.paintView)

        val factory =
            PaintingScreenViewModelFactory(
                requireContext().applicationContext as Application,
                photoEditorBuilder,
                navArgs
            ) {
                // redirect to screen
                findNavController().navigate(R.id.paintFragment_to_homeFragment)

            }
        paintViewModel = ViewModelProvider(this, factory).get(PaintingScreenViewModel::class.java)

        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        binding.paintViewModel = paintViewModel


        paintViewModel.imgSrcBitmap.observe(viewLifecycleOwner, Observer {
            binding.paintView.source.setImageBitmap(it)
        })

        paintViewModel.defaultDrawingMode()

        brushToolApapter()
        eraserToolLayout()
        fragmentStart()


        return binding.root
    }

    private fun fragmentStart() {
        binding.paintTabLayout.addTab( binding.paintTabLayout.newTab().setText("Brush"))
        binding.paintTabLayout.addTab( binding.paintTabLayout.newTab().setText("Eraser"))
        binding.paintTabLayout.setTabGravity(TabLayout.GRAVITY_FILL)

        binding.paintTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                paintViewModel.changeRecentTab(tab?.text.toString())
                setVisibility()

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.paintBack.setOnClickListener{
            it.findNavController().navigateUp()
        }

        binding.paintSave.setOnClickListener{
            paintViewModel.savePhotoAsBitmap {
                val data = getNavArgData(bitmap = it)
                navigateToMainScreen(data)
            }
        }

    }


    private fun brushToolApapter() {

        colorPickerRecyclerAdapter = ColorPickerAdapter()
        val colorPickerItemListener = ColorPickerItemListener { colorValue, position ->
            paintViewModel.setBrushColor(colorValue)
            colorPickerRecyclerAdapter.setSingleSelection(position)
        }

        colorPickerRecyclerAdapter.clickListener = colorPickerItemListener

        binding.recyclerView.adapter = colorPickerRecyclerAdapter

        paintViewModel.recyclerViewsData.observe(viewLifecycleOwner, Observer {
            colorPickerRecyclerAdapter.submitList(it)
        })


        binding.brushSizeSeekbar.progress = 50
        binding.brushSizeSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                paintViewModel.setBrushSize(progress.toFloat())

            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                showSliderTextIndigator()
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                hideSliderTextIndigator()

            }
        })


    }

    private fun eraserToolLayout() {

        binding.eraserSizeSeekbar.progress = 50
        binding.eraserSizeSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                paintViewModel.setEraserSize(progress.toFloat())

            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                showSliderTextIndigator()
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                hideSliderTextIndigator()

            }
        })

    }

    private fun showSliderTextIndigator() {
        binding.paintBrushIndigator.visibility = View.VISIBLE
    }

    private fun hideSliderTextIndigator() {
        binding.paintBrushIndigator.visibility = View.GONE

    }

    private fun setVisibility() {
        if (paintViewModel.isBrushLayoutVisible()) {
            binding.brushContainerLayout.visibility = View.VISIBLE
            binding.eraserContainerLayout.visibility = View.GONE
        } else {
            binding.eraserContainerLayout.visibility = View.VISIBLE
            binding.brushContainerLayout.visibility = View.GONE

        }
    }


    private fun navigateToMainScreen(data: CommonParcelData) {
        val action = PaintFragmentDirections.paintFragmentToEditFragment(data)

        findNavController().navigate(action)
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