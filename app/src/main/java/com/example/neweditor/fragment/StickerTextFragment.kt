package com.example.neweditor.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.SharedElementCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.neweditor.R
import com.example.neweditor.adapter.StickerViewPagerAdapter
import com.example.neweditor.customView.strickerView.*
import com.example.neweditor.data.CommonParcelData
import com.example.neweditor.data.ViewPagerData
import com.example.neweditor.databinding.BottomViewTextStickerBinding
import com.example.neweditor.databinding.FragmentStickertextBinding
import com.example.neweditor.dialog.EditTextDialog
import com.example.neweditor.enmus.ActiveNavArgsData
import com.example.neweditor.viewModel.BitmapHelper
import com.example.neweditor.viewModel.TextStickerViewModel
import com.example.neweditor.viewModelFactory.TextStickerViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class StickerTextFragment : Fragment() , EditTextDialog.Callback{
    companion object{
        private const val REQUEST_CODE = 11
    }
    private lateinit var binding : FragmentStickertextBinding
    private val navArgs by navArgs<StickerTextFragmentArgs>()
    private lateinit var textViewModel: TextStickerViewModel
    private var isLargeLayout: Boolean = false

    private lateinit var includeLayoutBinding: BottomViewTextStickerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_stickertext, container, false)

        includeLayoutBinding = binding.includeLayoutOfBottomViewText



        postponeEnterTransition()
        binding.mainImg.post {
            startPostponedEnterTransition()

        }
        /** end for shared animation delay */


        includeLayoutBinding = binding.includeLayoutOfBottomViewText

        isLargeLayout = resources.getBoolean(R.bool.large_layout)

        setStickerViewIcon()

        val application = requireContext().applicationContext as Application
        val factory = TextStickerViewModelFactory(
            application,
            navArgs,
            binding
        ){
            // redirect to screen
            findNavController().navigate(R.id.stickerTextFragment_to_homeFragment)

        }

        textViewModel = ViewModelProvider(this, factory).get(TextStickerViewModel::class.java)
        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this
        binding.textViewModel = textViewModel


        /**
         * disable [ViewPager2] swift left right gesture
         */
        includeLayoutBinding.textToolViewPager.isUserInputEnabled = false


        /**
         * setting bitmap in ImageView
         */


        textViewModel.imgSrc.observe(viewLifecycleOwner, {


            binding.mainImg.setImageBitmap(it)

            binding.textStickerView.setMainImage(binding.mainImg)
            binding.textStickerView.setMainBitmap(it)


        })

        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onSharedElementEnd(
                    sharedElementNames: List<String?>?,
                    sharedElements: List<View?>?,
                    sharedElementSnapshots: List<View?>?
                ) {
                    // transition has ended

//                    binding.textStickerView.addSticker(textViewModel.createNewSticker("Double Tap To Edit"))


                }
            }
        )


        textViewModel.viewPagerData.observe(viewLifecycleOwner, Observer {
            includeLayoutBinding.textToolViewPager.adapter =
                StickerViewPagerAdapter(childFragmentManager, lifecycle, it)
            configTabLayout(it)

        })




        includeLayoutBinding.addStickerBtn.setOnClickListener {
            showEditTextDialog(
                isTextEdit = false
            )
        }



        binding.textStickerView.onStickerOperationListener =
            object : StickerView.OnStickerOperationListener {
                override fun onStickerAdded(sticker: Sticker) {
                    textViewModel.updateCurrentSticker(sticker)
                }

                override fun onStickerClicked(sticker:Sticker) {
                    textViewModel.updateCurrentSticker(sticker)
                }

                override fun onStickerDeleted(sticker:Sticker) {

                    textViewModel.deleteOneStickerFromList(sticker as TextSticker)

                }

                override fun onStickerDragFinished(sticker: Sticker) {}

                override fun onStickerTouchedDown(sticker: Sticker) {}

                override fun onStickerZoomFinished(sticker: Sticker) {}

                override fun onStickerFlipped(sticker: Sticker) {}

                override fun onStickerDoubleTapped(sticker:Sticker) {
                    val textSticker = sticker as TextSticker

                    EditTextDialog.show(
                        this@StickerTextFragment,
                        REQUEST_CODE,
                        textSticker.text!!,
                        isTextIsAlReadyEdited = true
                    )


                }
            }
        saveImage()


        return binding.root
    }



    private fun configTabLayout(viewpagerData: List<ViewPagerData>) {
        TabLayoutMediator(
            includeLayoutBinding.textStickerTabLayout,
            includeLayoutBinding.textToolViewPager
        ) { tab, position ->
            tab.text = viewpagerData[position].name
        }.attach()
    }


    private fun setStickerViewIcon() {

        val editIcon = BitmapStickerIcon(
            getDrawable(R.drawable.ic_edit_vector),
            BitmapStickerIcon.RIGHT_TOP
        )
        val deleteIcon = BitmapStickerIcon(
            getDrawable(R.drawable.stricker_close_icon),
            BitmapStickerIcon.LEFT_TOP
        )
        val resizeIcon = BitmapStickerIcon(
            getDrawable(R.drawable.ic_resize_vector),
            BitmapStickerIcon.RIGHT_BOTOM
        )


        editIcon.iconEvent = EditIconEvent() {
            if (it != null) {
                val textSticker = it.currentSticker!! as  TextSticker
                EditTextDialog.show(
                    this,
                    REQUEST_CODE,
                    textSticker.text!!,
                    isTextIsAlReadyEdited = true
                )
            }


        }
        deleteIcon.iconEvent = DeleteIconEvent()
        resizeIcon.iconEvent = ZoomIconEvent()

        binding.textStickerView.icons = listOf(editIcon, deleteIcon, resizeIcon)


    }
    private fun saveImage() {
        binding.textBack.setOnClickListener{
            it.findNavController().navigateUp()

        }

        binding.textSave.setOnClickListener{
            val bitmap = binding.textStickerView.createBitmap()
            val data = getNavArgData(bitmap = bitmap)
            navigateToScreenWithExtra(data)

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


    private fun navigateToScreenWithExtra(data: CommonParcelData) {

        val imgView = binding.mainImg
        val imgTransitionUniqueName = resources.getString(R.string.img_transition_unique_name)

        val extra = FragmentNavigatorExtras(imgView to imgTransitionUniqueName)

        val action = StickerTextFragmentDirections.stickerTextFragmentToEditFragment(data)
        findNavController().navigate(action, extra)
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getDrawable(id: Int): Drawable {
        return requireContext().getDrawable(id)!!
    }


    private fun showEditTextDialog(text: String = "", isTextEdit: Boolean) {

        EditTextDialog.show(this, REQUEST_CODE, text, isTextEdit)


    }


    override fun onEditTextDialogResult(
        requestCode: Int,
        resultCode: Int,
        text: String,
        isTextIsAlreadyEdited: Boolean
    ) {
        if (requestCode != REQUEST_CODE || resultCode != Activity.RESULT_OK) return


        if (!isTextIsAlreadyEdited) {
            binding.textStickerView.addSticker(textViewModel.createNewSticker(text))
        } else {

            val textSticker = textViewModel.currentSticker?.value as TextSticker
            textSticker.text = text
            textSticker.resizeText()

            textViewModel.updateCurrentSticker(textSticker)
            textViewModel.updateOneStickerInList(textSticker)

        }

    }
}