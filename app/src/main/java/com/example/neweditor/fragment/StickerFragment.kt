package com.example.neweditor.fragment

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.demoeditor.viewModel.viewPager.stickersFragment.AnimalViewPagerViewModel
import com.android.demoeditor.viewModelFactory.viewPager.stickerFragment.AnimalViewPagerViewModelFactory
import com.example.neweditor.R
import com.example.neweditor.adapter.StickerAdapter
import com.example.neweditor.adapter.StickerItemListener
import com.example.neweditor.customView.strickerView.DrawableSticker
import com.example.neweditor.data.CommonParcelData
import com.example.neweditor.databinding.FragmentStickerBinding
import com.example.neweditor.enmus.ActiveNavArgsData
import com.example.neweditor.viewModel.BitmapHelper
import com.example.neweditor.viewModel.StickerViewModel
import com.example.neweditor.viewModelFactory.StickerViewModelFactory


class StickerFragment : Fragment() {
    private lateinit var binding: FragmentStickerBinding

    //    private val mainViewModel: MainPaintViewModel by activityViewModels()
    private val navArgs by navArgs<StickerFragmentArgs>()

    private lateinit var stickerViewModel: StickerViewModel

    private lateinit var animalViewModel: AnimalViewPagerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_sticker, container, false)

        SetStricker()

        val application = requireContext().applicationContext as Application
        val factory = StickerViewModelFactory(
            application,
            navArgs
        ){
            // redirect to screen
            findNavController().navigate(R.id.stickerTextFragment_to_homeFragment)

        }

        stickerViewModel = ViewModelProvider(this, factory).get(StickerViewModel::class.java)

        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        binding.stickerViewModel = stickerViewModel


        stickerViewModel.imgSrc.observe(viewLifecycleOwner,   {

            binding.stickerView.setMainImage(binding.mainImg)
            binding.stickerView.setMainBitmap(it)
            binding.mainImg.setImageBitmap(it)


        })



        return binding.root
    }

    private fun SetStricker() {
        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onSharedElementEnd(
                    sharedElementNames: List<String?>?,
                    sharedElements: List<View?>?,
                    sharedElementSnapshots: List<View?>?
                ) {
                    // transition has ended

                    /** if you are get result bitmap u have to then MUST be View.VISIBLE the  [binding.resultImageview]  */
//                    binding.resultImageview.visibility=View.VISIBLE


                }
            }
        )

        val stickerList = resources.obtainTypedArray(R.array.animal_photo)

        val factory = AnimalViewPagerViewModelFactory(
            (requireContext().applicationContext as Application),
            stickerList
        )

        animalViewModel = ViewModelProvider(this, factory).get(AnimalViewPagerViewModel::class.java)


        val adapter = StickerAdapter(StickerItemListener {
            if (binding.stickerViewModel?.isImgLoading?.value == false) {
                val drawableSticker = DrawableSticker(it)
                binding.stickerView.addSticker(drawableSticker)
            }
        })



        binding.animalRecyclerView.adapter = adapter




        animalViewModel.recyclerViewData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


//        val viewpagerData: List<ViewPagerData> = mutableListOf(
//
//            ViewPagerData(
//                AnimalViewPagerFragment.injectStickerBinding(binding),
//                getString(R.string.animal)
//            ),
//            ViewPagerData(
//                FacialViewPagerFragment.injectStickerBinding(binding),
//                getString(R.string.facial)
//            )
//            ,
//            ViewPagerData(
//                FunViewPagerFragment.injectStickerBinding(binding),
//                getString(R.string.cartoon)
//            ),
//            ViewPagerData(
//                FoodViewPagerFragment.injectStickerBinding(binding),
//                getString(R.string.food)
//            ),
//            ViewPagerData(
//                WordsViewPagerFragment.injectStickerBinding(binding),
//                getString(R.string.words)
//            ),
//        )


//        binding.stickerViewPager.adapter =
//            StickerViewPagerAdapter(childFragmentManager, lifecycle, viewpagerData)
//
//        TabLayoutMediator(binding.stickerTabLayout, binding.stickerViewPager) { tab, position ->
//
//            tab.text = viewpagerData[position].name
//
//        }.attach()


        binding.stickerBack.setOnClickListener {
            it.findNavController().navigateUp()

        }

        binding.stickerSave.setOnClickListener {
            val bitmap = binding.stickerView.createBitmap()
            val data = getNavArgData(bitmap = bitmap)
            navigateToScreen(data)

        }


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
    private fun navigateToScreen(data: CommonParcelData) {
        val action = StickerFragmentDirections.strickerFragmentToEditFragment(data)
        findNavController().navigate(action)
    }

}