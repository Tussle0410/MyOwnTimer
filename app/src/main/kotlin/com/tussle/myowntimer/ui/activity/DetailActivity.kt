package com.tussle.myowntimer.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.DetailPageBinding
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.model.DB.RepoFactory
import com.tussle.myowntimer.model.DetailNaviMenu
import com.tussle.myowntimer.ui.fragment.DetailCalendarFragment
import com.tussle.myowntimer.ui.fragment.DetailChartFragment
import com.tussle.myowntimer.ui.fragment.DetailTimerFragment
import com.tussle.myowntimer.ui.fragment.DetailTodoFragment
import com.tussle.myowntimer.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {
    private val viewModel : DetailViewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(this, factory).get(DetailViewModel::class.java)
    }
    private lateinit var binding : DetailPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.detail_page)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        init()
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.titleTimeUpdate()
    }
    //Set Observer
    private fun setObserver(){
        viewModel.detailFragment.observe(this) {
            changeFragment(it!!)
        }
        viewModel.calendarTimeInfo.observe(this){
            viewModel.calendarTimeListToHash()
        }
    }
    //Setting Methods
    private fun init(){
        setObserver()
        adsSetting()
        viewModel.setDate()
        viewModel.getCalendarTime()
    }
    //Set Google Ads
    private fun adsSetting(){
        MobileAds.initialize(this){}
        val adRequest = AdRequest.Builder().build()
        binding.mainAdView.loadAd(adRequest)
    }
    //BottomNavigation Fragment Change
    private fun changeFragment(menu_tag : DetailNaviMenu){
        val transaction = supportFragmentManager.beginTransaction()
        var targetFragment = supportFragmentManager.findFragmentByTag(menu_tag.tag)
        if(targetFragment == null){
            targetFragment = when(menu_tag){
                DetailNaviMenu.Timer -> DetailTimerFragment.getInstance()
                DetailNaviMenu.Todo -> DetailTodoFragment.getInstance()
                DetailNaviMenu.Calendar -> DetailCalendarFragment.getInstance()
                DetailNaviMenu.Chart -> DetailChartFragment.getInstance()
            }
            transaction.add(R.id.detail_Frame, targetFragment, menu_tag.tag)
        }
        transaction.show(targetFragment)
        DetailNaviMenu.values()
            .filterNot { it == menu_tag }
            .forEach{ menu ->
                supportFragmentManager.findFragmentByTag(menu.tag)?.let {
                    transaction.hide(it)
                }
            }
        transaction.commitNowAllowingStateLoss()
    }

}