package com.weatherinformation.base

import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    fun provideMessage(msg: String) {
        (activity as BaseActivity).provideMessage(msg)
    }

    fun provideLoadFragmentInfo(fragment: Fragment, isAdd: Boolean, isAddBackStack: Boolean) {
        (activity as BaseActivity).loadFragment(fragment, isAdd, isAddBackStack)
    }
}