package com.android.demoeditor.screens.viewPager.collageSelectorFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.example.neweditor.R
import com.example.neweditor.databinding.FragmentCollageResizeViewPagerBinding
import com.example.neweditor.databinding.FragmentCollageSelectorBinding
import com.example.neweditor.viewModel.CollageSelectorViewModel


class CollageResizeViewPagerFragment : Fragment() {

    private val TAG = this::class.java.simpleName

    private lateinit var binding: FragmentCollageResizeViewPagerBinding

    companion object {
        private var staticValueOfCollageViewModel: CollageSelectorViewModel? = null
        private var staticValueOfCollageBinding: FragmentCollageSelectorBinding? = null
        fun injectParentViewModel(
            collageViewModel: CollageSelectorViewModel,
            collageBinding: FragmentCollageSelectorBinding? = null
        ): Fragment {
            staticValueOfCollageViewModel = collageViewModel
            staticValueOfCollageBinding = collageBinding

            return CollageResizeViewPagerFragment()
        }
    }

    private val parentBinding by lazy { staticValueOfCollageBinding }

    private val parentViewModel by lazy { staticValueOfCollageViewModel }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_collage_resize_view_pager,
            container,
            false
        )

        /**
         * this for corner re-sizer
         */
//        binding.cornerSlider.addOnChangeListener { slider, value, fromUser ->
//            if (parentViewModel?.isImgLoading?.value == true) {
//                parentBinding?.puzzleView?.pieceRadian = value
//            }
//        }
//
//        binding.gridCornerSlider.addOnChangeListener { slider, value, fromUser ->
//            if (parentViewModel?.isImgLoading?.value == true) {
//                parentBinding?.puzzleView?.lineSize = value.toInt()
//            }
//
//        }
        binding.cornerSlider.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                if (parentViewModel?.isImgLoading?.value == true) {
                parentBinding?.puzzleView?.pieceRadian = progress.toFloat()
            }

            }

            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seek: SeekBar) {

            }
        })

        binding.gridCornerSlider.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                if (parentViewModel?.isImgLoading?.value == true) {
                parentBinding?.puzzleView?.lineSize = progress
            }


            }

            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seek: SeekBar) {

            }
        })

        binding.borderSlider.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                if (parentViewModel?.isImgLoading?.value == true) {
                    parentBinding?.puzzleView?.piecePadding = progress.toFloat()
                }


            }

            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seek: SeekBar) {

            }
        })

//        binding.borderSlider.addOnChangeListener { slider, value, fromUser ->
//            if (parentViewModel?.isImgLoading?.value == true) {
//                parentBinding?.puzzleView?.piecePadding = value
//            }
//        }


        return binding.root
    }


}