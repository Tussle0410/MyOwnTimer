package com.tussle.myowntimer.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.MainPageBinding
import com.tussle.myowntimer.model.ViewPagerModel
import com.tussle.myowntimer.ui.adapter.MainViewPagerAdapter
import com.tussle.myowntimer.viewmodel.MainViewModel
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private lateinit var binding : MainPageBinding
    private val temp = mutableListOf(ViewPagerModel(
        "정보처리기사","4", "5",
        "할일1", "할일2","할일3","1H",
        "10H","20H","100H"),
        ViewPagerModel(
            "코딩 공부","2", "5",
            "JAVA", "Algorithm","Android","4H",
            "10H","30H","100H"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_page)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        adsSetting()
        pageSetting()
        binding.mainProfileButton.setOnClickListener {
            startActivity<ProfileActivity>()
        }
        binding.mainSettingButton.setOnClickListener {
            startActivity<SettingActivity>()
        }
    }
    private fun pageSetting(){
        with(binding.viewPager2){
            offscreenPageLimit
            adapter = MainViewPagerAdapter(temp)
        }
    }
    private fun adsSetting(){
        MobileAds.initialize(this){}
        val adRequest = AdRequest.Builder().build()
        binding.mainAdView.loadAd(adRequest)
    }
}