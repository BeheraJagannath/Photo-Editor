package com.example.neweditor.fragment

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.neweditor.R
import com.example.neweditor.adapter.FiltersNavItemListener
import com.example.neweditor.adapter.FiltersNavItemsAdapter
import com.example.neweditor.data.CommonParcelData
import com.example.neweditor.databinding.FragmentFilterBinding
import com.example.neweditor.enmus.ActiveNavArgsData
import com.example.neweditor.viewModel.BitmapHelper
import com.example.neweditor.viewModel.FiltersViewModel
import com.example.neweditor.viewModelFactory.FiltersViewModelFactory
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class FilterFragment : Fragment() {
    private lateinit var binding:FragmentFilterBinding
    private lateinit var filtersViewModel: FiltersViewModel

    private val navArgs by navArgs<FilterFragmentArgs>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater , R.layout.fragment_filter, container, false)


        val factory= FiltersViewModelFactory(requireContext().applicationContext as Application, navArgs ){
            // redirect to screen

            findNavController().navigate(R.id.filterFragment_to_homeFragment)

        }
        filtersViewModel= ViewModelProvider(this,factory).get(FiltersViewModel::class.java)

        binding.lifecycleOwner=this

        binding.viewModel=filtersViewModel

        filtersViewModel.imgSrc.observe(viewLifecycleOwner, Observer {
            binding.imgId.setImageBitmap(it)
        })
        startApp()


        return binding.root
    }

    private fun startApp(){
         val  adapter = FiltersNavItemsAdapter()

         val recyclerViewListener = FiltersNavItemListener { filter, position ->
             applyFilter(filter)
             adapter.setSingleSelection(position)
//          binding.filterRecyclerView.smoothScrollToPosition(position+1)
         }

         adapter.clickListener=recyclerViewListener

         binding.filterRecyclerView.adapter = adapter

         filtersViewModel.listOfFilters.observe(viewLifecycleOwner, Observer {
             it?.let {
                 adapter.submitList(it)
             }

         })

         binding.filterBack.setOnClickListener{
             it.findNavController().navigateUp()
         }
         binding.filterSave.setOnClickListener{
             navigateToEditScreen()

         }

    }

    private fun navigateToEditScreen() {
        val bitmap = (binding.imgId.drawable as BitmapDrawable).bitmap
        val data = getNavArgData(bitmap = bitmap)

        val action = FilterFragmentDirections.filterFragmentToEditFragment(data)

        findNavController().navigate(action)

    }

    private fun getNavArgData(uri: Uri?=null, bitmap: Bitmap?=null ) : CommonParcelData {

        bitmap?.also {
            BitmapHelper.setBitmap(it,true)
        }

        return CommonParcelData(
            uri=uri,
            availableData = if (uri !=null ) ActiveNavArgsData.URI else ActiveNavArgsData.BITMAP
        )
    }

    private fun applyFilter(filter: GPUImageFilter,) {
        var gpuImg: GPUImage?= GPUImage(context)
        gpuImg?.setFilter(filter)
        val sourceImg=filtersViewModel.imgSrc.value
        val resultImgBitmap= gpuImg?.getBitmapWithFilterApplied(sourceImg);
        binding.imgId.setImageBitmap(resultImgBitmap)
        gpuImg=null
    }

}