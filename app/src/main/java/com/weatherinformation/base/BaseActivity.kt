package com.weatherinformation.base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.weatherinformation.R
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    fun provideMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    /*---------------------------This block contain load fragment information----------------------*/
    fun loadFragment(fragment: Fragment, isAdd: Boolean, isAddBackStack: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (isAdd) {
            fragmentTransaction.add(R.id.placeHolder, fragment, fragment::class.java.simpleName)
        } else {
            fragmentTransaction.replace(R.id.placeHolder, fragment, fragment::class.java.simpleName)
        }
        if (isAddBackStack) {
            fragmentTransaction.addToBackStack(fragment::class.java.simpleName)
        }
        fragmentTransaction.commit()
    }
}