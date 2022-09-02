package com.tussle.myowntimer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.model.Title
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo : Repo) : ViewModel() {
    val titleCount = MutableLiveData<String>()
    val titleMaxTime = MutableLiveData<String>()
    val titleAvgTime = MutableLiveData<String>()
    val titleSumTime = MutableLiveData<String>()
    private var _titleMaxTime = 0
    private var _titleAvgTime = 0
    private var _titleSumTime = 0
    val titleInfo = MutableLiveData<List<Title>>()
    //Get Room DB Title Info
    fun getTitleInfo(){
        CoroutineScope(Dispatchers.IO).launch {
            repo.getTitle().let {
                titleInfo.postValue(it)
            }
        }
    }
    //Set ProfileInfo
    fun setProfileInfo(){
        titleCount.value = titleInfo.value!!.size.toString()
        for(curTitle in titleInfo.value!!){
            _titleSumTime += curTitle.totalTime
            if(_titleMaxTime < curTitle.totalTime)
                _titleMaxTime = curTitle.totalTime
        }
        _titleAvgTime = _titleSumTime / titleCount.value!!.toInt()
        titleMaxTime.value = timeConverter(_titleMaxTime)
        titleSumTime.value = timeConverter(_titleSumTime)
        titleAvgTime.value = timeConverter(_titleAvgTime)
    }
    private fun timeConverter(time : Int) : String{
        val h = time/3600
        val m = (time/60)%60
        val s = time%60
        val txtH = if(h<10) "0$h" else h.toString()
        val txtM = if(m<10) "0$m" else m.toString()
        val txtS = if(s<10) "0$s" else s.toString()
        return "$txtH:$txtM:$txtS"
    }

}