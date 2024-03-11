package com.weatherinformation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.weatherinformation.base.BaseActivity
import com.example.weatherinformation.databinding.ActivitySplashBinding
import com.weatherinformation.ui.home.activity.HomeActivity

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 2000)
    }

}