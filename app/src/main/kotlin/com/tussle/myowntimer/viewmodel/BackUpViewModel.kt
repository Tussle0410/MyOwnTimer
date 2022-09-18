package com.tussle.myowntimer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tussle.myowntimer.sharedPreference.GlobalApplication
import java.text.SimpleDateFormat
import java.util.*

class BackUpViewModel : ViewModel() {
    val backupDate = MutableLiveData<String>()
    init {
        backupDate.value = GlobalApplication.prefs.settingGetString("backUpDate","-")
    }
    fun setBackupDate(){
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        val time = Calendar.getInstance().time
        val date = format.format(time)
        GlobalApplication.prefs.settingSetString("backUpDate", date)
        backupDate.value = date
    }
}