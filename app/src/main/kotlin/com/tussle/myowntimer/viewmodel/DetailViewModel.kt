package com.tussle.myowntimer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.navigation.NavigationBarView
import com.tussle.myowntimer.R
import com.tussle.myowntimer.model.DetailNaviMenu

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentFragment = MutableLiveData(DetailNaviMenu.Timer)
    val currentFragment : LiveData<DetailNaviMenu>
        get() = _currentFragment
    //Detail Page Bottom Menu Click Listener
    val bottomMenuClickListener =
        NavigationBarView.OnItemSelectedListener { item ->
            val fragment = getFragment(item.itemId)
            fragmentChange(fragment)
            true
        }
    //Get Current DetailPage Fragment
    private fun getFragment(menu_id : Int) : DetailNaviMenu
        =when(menu_id){
            R.id.navi_timer -> DetailNaviMenu.Timer
            R.id.navi_todo -> DetailNaviMenu.Todo
            R.id.navi_calendar -> DetailNaviMenu.Calendar
            R.id.navi_chart -> DetailNaviMenu.Chart
            else -> throw IllegalArgumentException("Not Found Menu ID")
        }
    //DetailPage Fragment Change
    private fun fragmentChange(fragment : DetailNaviMenu){
        if(_currentFragment.value != fragment)
            _currentFragment.value = fragment
    }

}