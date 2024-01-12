package com.example.neweditor.fragment

import android.app.Application
import android.app.Dialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.demoeditor.screens.viewPager.collageSelectorFragment.CollageBackgroundColorViewPagerFragment
import com.android.demoeditor.screens.viewPager.collageSelectorFragment.CollageEditViewPagerFragment
import com.android.demoeditor.screens.viewPager.collageSelectorFragment.CollageResizeViewPagerFragment
import com.example.neweditor.R
import com.example.neweditor.adapter.CollageSelectorViewPagerAdapter
import com.example.neweditor.data.CommonParcelData
import com.example.neweditor.data.ViewPagerData
import com.example.neweditor.databinding.FragmentCollageSelectorBinding
import com.example.neweditor.enmus.ActiveNavArgsData
import com.example.neweditor.fragment.collageSelectorFragment.CollageLayoutViewPagerFragment
import com.example.neweditor.utils.getBitmap
import com.example.neweditor.utils.getPuzzleData
import com.example.neweditor.utils.setUpPuzzleLayout
import com.example.neweditor.viewModel.BitmapHelper
import com.example.neweditor.viewModel.CollageSelectorViewModel
import com.example.neweditor.viewModelFactory.CollageSelectorViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class CollageSelectorFragment : Fragment() {
    lateinit var binding: FragmentCollageSelectorBinding

    private val navArgs by navArgs<CollageSelectorFragmentArgs>()
    lateinit var dialog: Dialog

    private lateinit var viewModel: CollageSelectorViewModel
    private lateinit var collageViewPagerAdapter: CollageSelectorViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater ,R.layout.fragment_collage_selector, container, false)

        val factory = CollageSelectorViewModelFactory(
            requireContext().applicationContext as Application,
            navArgs.data.listOfUri,
            requireActivity(),
        )
        viewModel = ViewModelProvider(this, factory).get(CollageSelectorViewModel::class.java)

        binding.viewPager.isUserInputEnabled = false
        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setUpMainPuzzleView()
        initializeBackgroundPuzzleView()
        configViewPager()
        CreateDialog()

        binding.floatingActionButton.setOnClickListener {
            navigateToMainScreenScreen()
        }


        return binding.root
    }

    private fun initializeBackgroundPuzzleView() {

        binding.puzzleView.apply {
//            isNeedDrawLine = true
//            isNeedDrawOuterLine = true
            isTouchEnable = true
        }


        viewModel.recentColorData.observe(viewLifecycleOwner, {
            binding.puzzleView.setBackgroundColor(it.color)
        })


    }

    private fun setUpMainPuzzleView() {

        viewModel.listOfImages.observe(viewLifecycleOwner, {

            if (viewModel.getInitLayoutForPuzzleLayout() != null) {


                if (it.size == navArgs.data.listOfUri.size) {
                    val layout = viewModel.getInitLayoutForPuzzleLayout()!!.layout

                    val layoutData = getPuzzleData(layout, it.size)

                    setUpPuzzleLayout(layoutData, it, binding.puzzleView)

                }
            }
        })

    }

    private fun configViewPager() {

        val viewpagerData: List<ViewPagerData> = mutableListOf(
            ViewPagerData(
                CollageLayoutViewPagerFragment.injectParentViewModel(viewModel, binding),
                "Layout"
            ),
            ViewPagerData(
                CollageEditViewPagerFragment.injectParentViewModel(viewModel, binding),
                "Edit"
            ),
            ViewPagerData(
                CollageBackgroundColorViewPagerFragment.injectParentViewModel(
                    viewModel,
                    binding
                ), "Bg"
            ),
            ViewPagerData(
                CollageResizeViewPagerFragment.injectParentViewModel(viewModel, binding),
                "Resize"
            ),
        )

        collageViewPagerAdapter =
            CollageSelectorViewPagerAdapter(childFragmentManager, lifecycle, viewpagerData)

        binding.viewPager.adapter = collageViewPagerAdapter

        TabLayoutMediator(binding.collageTabLayout, binding.viewPager) { tab, position ->
            tab.text = viewpagerData[position].name
        }.attach()


        binding.collageBack.setOnClickListener{
            it.findNavController().navigateUp()

        }
        binding.collageSave.setOnClickListener{
            dialog.show()
            Handler().postDelayed(Runnable {
                binding.puzzleView.setDrawingCacheEnabled(true)
                val imagesaved: String = MediaStore.Images.Media.insertImage(
                    requireActivity().contentResolver,
                    binding.puzzleView.drawingCache,
                    UUID.randomUUID().toString() + ".png",
                    "drawing"
                )
                if (imagesaved != null) {
                    Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.collageSelectorFragment_to_homeFragment)
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Images not saved", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                binding.puzzleView.destroyDrawingCache()

            }, 3000)



        }



    }

    fun CreateDialog(){
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loading_dialog)


    }

    override fun onDestroy() {
        super.onDestroy()
//        backgroundScope = null
    }

    private fun navigateToMainScreenScreen() {

        val action =
            CollageSelectorFragmentDirections.collageSelectorFragmentToMainEditScreenFragment(
                getNavArgData()
            )

        findNavController().navigate(action)


    }
    private fun getBitmap(): Bitmap {
        return binding.puzzleView.getBitmap()
    }

    private fun getNavArgData(uri: Uri? = null): CommonParcelData {
        if (uri == null) BitmapHelper.setBitmap(getBitmap(), true)

        return CommonParcelData(
            uri = uri,
            availableData = if (uri != null) ActiveNavArgsData.URI else ActiveNavArgsData.BITMAP
        )
    }
}