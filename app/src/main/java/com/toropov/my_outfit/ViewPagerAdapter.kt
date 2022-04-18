package com.toropov.my_outfit

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter (fragment: FragmentManager) : FragmentPagerAdapter(fragment,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getCount(): Int {
        return 3
    }

    @NonNull
    override fun getItem(position: Int): Fragment {
        return when (position) {
                0 -> {
                    OnBoardingFragment_1()
                }
                1 -> {
                    OnBoardingFragment_2()
                }
                2 -> {
                    OnBoardingFragment_3()
                }
                else->{
                    return OnBoardingFragment_1()
                }
        }
    }
}