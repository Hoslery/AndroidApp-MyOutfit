package com.toropov.my_outfit.adapters

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.toropov.my_outfit.fragments.OnBoardingFragment_1
import com.toropov.my_outfit.fragments.OnBoardingFragment_2
import com.toropov.my_outfit.fragments.OnBoardingFragment_3

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