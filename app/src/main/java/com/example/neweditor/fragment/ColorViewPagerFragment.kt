package com.example.neweditor.fragment

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.demoeditor.viewModelFactory.viewPager.textStickerFragment.ColorViewPagerViewModelFactory
import com.example.neweditor.R
import com.example.neweditor.adapter.ColorPickerAdapter
import com.example.neweditor.adapter.ColorPickerItemListener
import com.example.neweditor.databinding.FragmentColorViewPagerBinding
import com.example.neweditor.viewModel.TextStickerViewModel
import com.example.neweditor.viewModel.textStrikeViewModel.ColorViewPagerViewModel

class ColorViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentColorViewPagerBinding

    companion object {
        private var staticValueOfTextViewModel:TextStickerViewModel?=null
        fun injectParentStickerView(textViewModel: TextStickerViewModel): Fragment {
            staticValueOfTextViewModel=textViewModel
            return ColorViewPagerFragment()
        }
    }



    private val parentViewModel by lazy { staticValueOfTextViewModel }

    /**
     * [colorViewModel] is for Color-ViewModel
     */
    private lateinit var colorViewModel: ColorViewPagerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentColorViewPagerBinding.inflate(layoutInflater, container, false)


        val application = requireContext().applicationContext as Application

        val factory = ColorViewPagerViewModelFactory(application)
        colorViewModel = ViewModelProvider(this, factory).get(ColorViewPagerViewModel::class.java)

        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this
        binding.viewModel = colorViewModel


        configRecyclerView()


        return binding.root
    }


    private fun configRecyclerView() {
        val colorRecyclerViewAdapter = ColorPickerAdapter( )

        val colorViewPagerItemListener = ColorPickerItemListener { colorValue, position ->
            parentViewModel?.changeStickerColor(colorValue,position)
            if(parentViewModel?.currentSticker?.value != null){
                colorRecyclerViewAdapter.setSingleSelection(position)
            }

        }

        colorRecyclerViewAdapter.clickListener = colorViewPagerItemListener

        binding.colorViewpagerRecyclerView.adapter = colorRecyclerViewAdapter


        /**
         * this for updating position of when recycler view is appera
         */
        parentViewModel?.colorPositionInRecyclerView?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                colorRecyclerViewAdapter.setSingleSelection(it)
            }
        })

        parentViewModel?.colorViewPager_RecyclerViewData?.observe(viewLifecycleOwner, Observer {
            colorRecyclerViewAdapter.submitList(it)
        })

    }


    override fun onDestroy() {
        staticValueOfTextViewModel = null

        super.onDestroy()
    }


}