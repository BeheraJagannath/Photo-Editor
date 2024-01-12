package com.example.neweditor.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.neweditor.data.ViewPagerData

open class StickerViewPagerAdapter(fragmentManager: FragmentManager,
                                   lifecycle: Lifecycle,
                                   private val dataArr: List<ViewPagerData>
)
    : FragmentStateAdapter(fragmentManager, lifecycle )

{

    private val fragmentCount=dataArr.size

    override fun getItemCount(): Int {
        return fragmentCount
    }


    override fun createFragment(position: Int): Fragment {

        val (fragment )=dataArr[position]

        return fragment

    }
}