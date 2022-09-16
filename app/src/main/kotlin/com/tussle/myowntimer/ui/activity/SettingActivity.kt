package com.tussle.myowntimer.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.SettingPageBinding
import com.tussle.myowntimer.sharedPreference.GlobalApplication
import com.tussle.myowntimer.viewmodel.SettingViewModel
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import org.jetbrains.anko.toast

class SettingActivity : AppCompatActivity() {
    private val viewModel : SettingViewModel by lazy {
        ViewModelProvider(this).get(SettingViewModel::class.java)
    }
    private lateinit var binding : SettingPageBinding
    private val backUp = RoomBackup(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.setting_page)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        init()
    }
    //Setting Activity Init
    private fun init(){
        setButton()
        setRadioGroup()
        backUpInit()
    }
    //Setting Activity Button Setting
    private fun setButton(){
        binding.settingBackButton.setOnClickListener {
            finish()
        }
        binding.backupLayout.setOnClickListener {
            backUp.backup()
        }
        binding.reviewLayout.setOnClickListener {
            backUp.restore()
        }
    }
    private fun setRadioGroup(){
        binding.timerRadio.check(R.id.radio_sound)
        binding.timerRadio.setOnCheckedChangeListener { _, i ->
            when(i) {
                R.id.radio_sound -> {GlobalApplication.prefs.settingSetString("alarm", "sound")}
                R.id.radio_vibrate -> {GlobalApplication.prefs.settingSetString("alarm", "vibrate")}
                R.id.radio_soundAndVibrate ->{GlobalApplication.prefs.settingSetString("alarm","sound+vibrate")}
            }
        }
    }
    private fun backUpInit(){
        backUp
            .database(GlobalApplication.databaseInstance)
            .enableLogDebug(true)
            .backupIsEncrypted(true)
            .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
            .maxFileCount(5)
            .apply {
                onCompleteListener { success, message, exitCode ->
                    Log.d("백업 성공", "success: $success, message: $message, exitCode: $exitCode")
                }
            }
    }
}