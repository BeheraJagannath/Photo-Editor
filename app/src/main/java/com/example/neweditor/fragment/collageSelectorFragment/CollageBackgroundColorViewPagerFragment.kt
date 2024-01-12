package com.android.demoeditor.screens.viewPager.collageSelectorFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.neweditor.R
import com.example.neweditor.adapter.CollageColorPickerAdapter
import com.example.neweditor.adapter.CollageColorPickerItemListener
import com.example.neweditor.databinding.FragmentCollageBackgroundColorViewPagerBinding
import com.example.neweditor.databinding.FragmentCollageSelectorBinding
import com.example.neweditor.viewModel.CollageSelectorViewModel


class CollageBackgroundColorViewPagerFragment : Fragment() {

    private val TAG = this::class.java.simpleName

    private lateinit var binding: FragmentCollageBackgroundColorViewPagerBinding

    companion object {
        private var staticValueOfCollageViewModel: CollageSelectorViewModel? = null
        private var staticValueOfCollageBinding: FragmentCollageSelectorBinding? = null
        fun injectParentViewModel(
            collageViewModel: CollageSelectorViewModel,
            collageBinding: FragmentCollageSelectorBinding? = null
        ): Fragment {
            staticValueOfCollageViewModel = collageViewModel
            staticValueOfCollageBinding = collageBinding

            return CollageBackgroundColorViewPagerFragment()
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
            R.layout.fragment_collage_background_color_view_pager,
            container,
            false
        )



        setupColorRecyclerView()


        return binding.root
    }

    private fun setupColorRecyclerView() {
        val colorRecyclerViewAdapter = CollageColorPickerAdapter()

        val colorViewPagerItemListener = CollageColorPickerItemListener { colorValue, position ->
            if (parentViewModel?.isImgLoading?.value == true) {
                parentViewModel?.updateRecentColor(colorValue, position)
                colorRecyclerViewAdapter.setSingleSelection(position)
            }
        }

        colorRecyclerViewAdapter.clickListener = colorViewPagerItemListener

        binding.collageColorRecyclerView.adapter = colorRecyclerViewAdapter


        /**
         * this for updating position of when recycler view is appera
         */
        parentViewModel?.recentColorData?.observe(viewLifecycleOwner, {
            if (it != null) {
                colorRecyclerViewAdapter.setSingleSelection(it.color)
            }
        })

        parentViewModel?.colorRecyclerViewData?.observe(viewLifecycleOwner, {
            colorRecyclerViewAdapter.submitList(it)
        })
    }


}