package com.tussle.myowntimer.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.ProfilePageBinding
import com.tussle.myowntimer.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    private val viewModel : ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }
    private lateinit var binding : ProfilePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.profile_page)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}