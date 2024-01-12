package com.example.neweditor.fragment.collageSelectorFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.neweditor.R
import com.example.neweditor.adapter.CollageNavbarAdapter
import com.example.neweditor.adapter.CollageNavbarItemListener
import com.example.neweditor.databinding.FragmentCollageLayoutViewPagerBinding
import com.example.neweditor.databinding.FragmentCollageSelectorBinding
import com.example.neweditor.utils.setUpPuzzleLayout
import com.example.neweditor.viewModel.CollageSelectorViewModel


class CollageLayoutViewPagerFragment : Fragment() {

    private val TAG = this::class.java.simpleName

    companion object {
        private var staticValueOfCollageViewModel: CollageSelectorViewModel? = null
        private var staticValueOfCollageBinding: FragmentCollageSelectorBinding? = null
        fun injectParentViewModel(
            collageViewModel: CollageSelectorViewModel,
            collageBinding: FragmentCollageSelectorBinding? = null
        ): Fragment {
            staticValueOfCollageViewModel = collageViewModel
            staticValueOfCollageBinding = collageBinding

            return CollageLayoutViewPagerFragment()
        }
    }

    private val parentBinding by lazy { staticValueOfCollageBinding }

    private val parentViewModel by lazy { staticValueOfCollageViewModel }

    private lateinit var layoutSelectorAdapter: CollageNavbarAdapter

    private lateinit var binding: FragmentCollageLayoutViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_collage_layout_view_pager,
            container,
            false
        )

        configLayoutSelectorRecyclerView()

        return binding.root
    }

    private fun configLayoutSelectorRecyclerView() {


        layoutSelectorAdapter = CollageNavbarAdapter(
            parentViewModel!!.listOfUris.size,
            requireContext(),
        )

        layoutSelectorAdapter.clickListener =
            CollageNavbarItemListener { layoutInfoData, position ->

                if (parentViewModel?.isImgLoading?.value == true) {

                    setUpPuzzleLayout(
                        layoutInfoData, parentViewModel?.listOfImages?.value!!,
                        parentBinding!!.puzzleView
                    )

                    layoutSelectorAdapter.setSingleSelection(position)

                }


            }

        binding.layoutSelectorRecyclerView.adapter = layoutSelectorAdapter

        parentViewModel?.recyclerViewLayoutSelectorData?.observe(viewLifecycleOwner, {
            Log.d(TAG, "configLayoutSelectorRecyclerView: ${it.size}")
            layoutSelectorAdapter.submitList(it)

        })

    }


}