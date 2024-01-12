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
import com.android.demoeditor.viewModelFactory.viewPager.textStickerFragment.FontViewPagerViewModelFactory
import com.example.neweditor.R
import com.example.neweditor.adapter.FontTextStickerAdapter
import com.example.neweditor.adapter.FontTextStickerListener
import com.example.neweditor.databinding.FragmentFontViewPagerBinding
import com.example.neweditor.viewModel.TextStickerViewModel
import com.example.neweditor.viewModel.textStrikeViewModel.FontViewPagerViewModel


class FontViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentFontViewPagerBinding

    companion object {
        private val TAG =FontViewPagerFragment::class.java.simpleName
        private var staticValueOfTextViewModel: TextStickerViewModel? = null
        fun injectParentStickerView(
            textViewModel: TextStickerViewModel
        ): Fragment {
            staticValueOfTextViewModel = textViewModel
            return FontViewPagerFragment()
        }
    }


    private val parentViewModel by lazy { staticValueOfTextViewModel }

    private lateinit var fontViewModel: FontViewPagerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        binding = FragmentFontViewPagerBinding.inflate(layoutInflater, container, false)

        val application = requireContext().applicationContext as Application


        val factory = FontViewPagerViewModelFactory(application )
        fontViewModel = ViewModelProvider(this, factory).get(FontViewPagerViewModel::class.java)

        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this
        binding.viewModel = fontViewModel


        configRecyclerView()



        return binding.root
    }


    private fun configRecyclerView() {

        val fontRecyclerViewAdapter = FontTextStickerAdapter()

        val fontRecyclerViewItemListener = FontTextStickerListener { typeface, fontName, position ->
            parentViewModel?.changeTypeFace(typeface,fontName,position)

            if(parentViewModel?.currentSticker?.value != null){
                fontRecyclerViewAdapter.setSingleSelection(position)
            }


        }

        /**
         * this for updating position of when recycler view is appera
         */
        parentViewModel?.fontPositionInRecyclerView?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                fontRecyclerViewAdapter.setSingleSelection(it)
            }
        })

        fontRecyclerViewAdapter.clickListener = fontRecyclerViewItemListener

        binding.fontViewpagerRecyclerview.adapter = fontRecyclerViewAdapter

        parentViewModel?.fontViewPager_RecyclerViewData?.observe(viewLifecycleOwner, Observer {
            fontRecyclerViewAdapter.submitList(it)
        })


    }


}