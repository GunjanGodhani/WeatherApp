package com.weatherinformation.ui.home.activity

import android.os.Bundle
import com.example.weatherinformation.databinding.ActivityHomeBinding
import com.weatherinformation.base.BaseActivity
import com.weatherinformation.ui.home.fragment.MapFragment

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(MapFragment(), isAdd = true, isAddBackStack = false)
    }

    fun goBack() {
        binding.imgViewBack.setOnClickListener {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    fun setTitle(title:String){
        binding.textViewToolBarTitle.text = title
    }
}