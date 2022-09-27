package com.tussle.myowntimer.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.SplashPageBinding
import com.tussle.myowntimer.event.EventObserver
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.model.DB.RepoFactory
import com.tussle.myowntimer.service.ForcedTerminationService
import com.tussle.myowntimer.viewmodel.SplashViewModel
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {
    private val viewModel : SplashViewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(this,factory).get(SplashViewModel::class.java)
    }
    private lateinit var binding : SplashPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.splash_page)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        startService(Intent(this, ForcedTerminationService::class.java))
        val window = window
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        viewModel.splashEvent.observe(this,EventObserver{
            startActivity<MainActivity>()
            finish()
        })
        viewModel.splashStart()
    }
}