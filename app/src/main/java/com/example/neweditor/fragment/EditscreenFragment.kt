package com.example.neweditor.fragment

import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.neweditor.R
import com.example.neweditor.data.CommonParcelData
import com.example.neweditor.databinding.FragmentEditscreenBinding
import com.example.neweditor.enmus.ActiveNavArgsData
import com.example.neweditor.fragment.interfacee.EditScreenListener
import com.example.neweditor.viewModel.BitmapHelper
import com.example.neweditor.viewModel.EditViewModel
import com.example.neweditor.viewModelFactory.EditViewModelFactory
import java.util.*


class EditscreenFragment : Fragment() {
    private lateinit var binding :FragmentEditscreenBinding


    private var view: View? = null
    private val navArgs by navArgs<EditscreenFragmentArgs>()
    private lateinit var viewModel: EditViewModel
    lateinit var dialog:Dialog



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(layoutInflater ,R.layout.fragment_editscreen, container, false)
        view = binding.root

        val application = requireContext().applicationContext as Application
        val factory = EditViewModelFactory(application, navArgs, requireActivity()) {
            // redirect to  screen
            findNavController().navigate(R.id.editFragment_to_homeFragment)
        }
        viewModel = ViewModelProvider(this, factory).get(EditViewModel::class.java)

        viewModel.imgSrc.observe(viewLifecycleOwner, {
            binding.editImage.setImageBitmap(it)
        })

        startFragment()
        CreateDialog()
        return view
    }

    private fun startFragment() {

        binding.editBack.setOnClickListener{
            it.findNavController().navigateUp()
        }
        binding.editSave.setOnClickListener {
            dialog.show()
            Handler().postDelayed(Runnable {
                binding.editImage.setDrawingCacheEnabled(true)
                val imagesaved: String = MediaStore.Images.Media.insertImage(
                    requireActivity().contentResolver,
                    binding.editImage.drawingCache,
                    UUID.randomUUID().toString() + ".png",
                    "drawing"
                )
                if (imagesaved != null) {
                    Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                   it.findNavController().navigateUp()
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Images not saved", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                binding.editImage.destroyDrawingCache()

            }, 3000)




        }
        binding.bottomNavigationListener = object : EditScreenListener {
            override fun crop() {
                navigateToCropFragment()
            }

            override fun rotate() {
                navigateToRotateFragment()
            }

            override fun adjust() {
                navigateToAdjustFragment()


            }

            override fun paint() {
                navigateToPaintFragment()

            }

            override fun filter() {
                navigateToFilterFragment()

            }

            override fun textSticker() {
                navigateToStickerTextFragment()


            }

            override fun sticker() {
                navigateToStickerFragment()

            }
        }
    }


    private fun navigateToAdjustFragment() {
        val action = EditscreenFragmentDirections
            .editFragmentToAdjustFragment(getNavArgData())
        findNavController().navigate(action, getSharedElementExtra())

    }

    private fun navigateToStickerFragment() {
        val action = EditscreenFragmentDirections
            .editFragmentToStickerFragment(getNavArgData())
        findNavController().navigate(action, getSharedElementExtra())

    }


    private fun navigateToCropFragment() {
        val action = EditscreenFragmentDirections
            .editFragmentToCropFragment(getNavArgData())
        findNavController().navigate(action, getSharedElementExtra())

    }
    private fun navigateToRotateFragment() {

        val action = EditscreenFragmentDirections
            .editFragmentToRotateFragment(getNavArgData())

        findNavController().navigate(action, getSharedElementExtra())

    }
    private fun navigateToPaintFragment() {
        val action = EditscreenFragmentDirections
            .editFragmentToPaintFragment(getNavArgData())
        findNavController().navigate(action, getSharedElementExtra())

    }
    private fun navigateToFilterFragment() {
        val action = EditscreenFragmentDirections
            .editFragmentToFilterFragment(getNavArgData())

        findNavController().navigate(action, getSharedElementExtra())
    }

    private fun navigateToStickerTextFragment() {
        val action = EditscreenFragmentDirections
            .editFragmentToStickerTextFragment(getNavArgData())

        findNavController().navigate(action, getSharedElementExtra())

    }


    private fun getSharedElementExtra(): FragmentNavigator.Extras {
        val imgView = binding.editImage
        val imgTransitionUniqueName = resources.getString(R.string.img_transition_unique_name)

        return FragmentNavigatorExtras(imgView to imgTransitionUniqueName)
    }


    /**
     * [getBitmap] for get the bitmap from image view
     */
    private fun getBitmap(): Bitmap {
        return (binding.editImage.drawable as BitmapDrawable).bitmap
    }


    /**
     * [getNavArgData] for get the nav arg data
     */
    private fun getNavArgData(uri: Uri? = null): CommonParcelData {
        if (uri == null) BitmapHelper.setBitmap(getBitmap(), true)
        Log.d("tag",uri.toString())

        return CommonParcelData(
            uri = uri,
            availableData = if (uri != null) ActiveNavArgsData.URI else ActiveNavArgsData.BITMAP
        )
    }
    fun CreateDialog(){
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loading_dialog)


    }



}
