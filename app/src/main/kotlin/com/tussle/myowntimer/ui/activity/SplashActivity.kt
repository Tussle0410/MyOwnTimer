package com.tussle.myowntimer.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.SplashPageBinding
import com.tussle.myowntimer.event.EventObserver
import com.tussle.myowntimer.viewmodel.SplashViewModel
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {
    private val viewModel : SplashViewModel by lazy {
        ViewModelProvider(this).get(SplashViewModel::class.java)
    }
    private lateinit var binding : SplashPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.splash_page)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.splashStart()
        viewModel.splashEvent.observe(this,EventObserver{
            startActivity<MainActivity>()
            finish()
        })
    }
}