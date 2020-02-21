package com.dino.connectnews.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class CategoryPageAdapter(
    fm: FragmentManager?,
    private val fragmentList: List<Fragment>
) : FragmentStatePagerAdapter(fm!!) {
    override fun getItem(i: Int): Fragment {
        return fragmentList[i]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

}