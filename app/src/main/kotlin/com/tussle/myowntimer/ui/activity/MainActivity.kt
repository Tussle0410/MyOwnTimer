package com.tussle.myowntimer.ui.activity

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.MainAddDialogBinding
import com.tussle.myowntimer.databinding.MainPageBinding
import com.tussle.myowntimer.event.EventObserver
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.model.DB.RepoFactory
import com.tussle.myowntimer.sharedPreference.GlobalApplication
import com.tussle.myowntimer.ui.adapter.MainViewPagerAdapter
import com.tussle.myowntimer.ui.listener.CheckCalendarTime
import com.tussle.myowntimer.viewmodel.MainViewModel
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), CheckCalendarTime {
    private val viewModel : MainViewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }
    private lateinit var binding : MainPageBinding
    private lateinit var viewPagerAdapter: MainViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_page)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initPage()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTitle()
        listSetting()
        viewModel.setDate()
    }
    //Main Page Ads, ViewPager2... init
    private fun initPage(){
        viewModel.getTitle()
        listSetting()
        adsSetting()
        setProfileAndSettingButton()
        setAddButton()
        viewModel.setDate()
    }
    private fun listSetting(){
        viewModel.titleInfo.observe(this) {
            viewModel.setList()
            pageSetting()
        }
        viewModel.insertEvent.observe(this,EventObserver{
            viewPagerAdapter.notifyDataSetChanged()
        })
    }
    //Set Main Page TitleAdd Button
    private fun setAddButton(){
        binding.addButton.setOnClickListener {
            val bindingDialog = MainAddDialogBinding.inflate(LayoutInflater.from(binding.root.context))
            AlertDialog.Builder(this)
                .setTitle("목표 추가하기!")
                .setView(bindingDialog.root)
                .setPositiveButton("추가") { _, _ ->
                    if (viewModel.titleCount < 10)
                        viewModel.insertTitle(bindingDialog.mainDialogTitle.text.toString())
                    else
                        toast("목표를 10개 이상 초과할 수 없습니다.")
                }
                .show()
        }
    }
    //Set Main Page Profile, Setting Button
    private fun setProfileAndSettingButton(){
        binding.mainProfileButton.setOnClickListener {
            startActivity<ProfileActivity>()
        }
        binding.mainSettingButton.setOnClickListener {
            startActivity<SettingActivity>()
        }
    }
    //Set ViewPager2
    private fun pageSetting(){
        viewPagerAdapter = MainViewPagerAdapter(viewModel.viewPagerInfo.value!!,this, this)
        with(binding.mainViewPager){
            offscreenPageLimit
            adapter = viewPagerAdapter
        }
        binding.mainIndicator.attachTo(binding.mainViewPager)
    }
    //Set Google Ads
    private fun adsSetting(){
        MobileAds.initialize(this){}
        val adRequest = AdRequest.Builder().build()
        binding.mainAdView.loadAd(adRequest)
    }

    override fun checkCalendarTime(title: String) {
        viewModel.calendarTimeCheck(title)
    }
}