package com.example.neweditor.adapter

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.neweditor.data.ViewPagerData

class CollageSelectorViewPagerAdapter(fragmentManager: FragmentManager,
                                      lifecycle: Lifecycle,
                                      private val dataArr: List<ViewPagerData>) :StickerViewPagerAdapter(fragmentManager, lifecycle, dataArr)