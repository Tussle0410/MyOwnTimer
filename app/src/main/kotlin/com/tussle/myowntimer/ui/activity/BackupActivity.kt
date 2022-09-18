package com.tussle.myowntimer.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.SettingBackupPageBinding
import com.tussle.myowntimer.sharedPreference.GlobalApplication
import com.tussle.myowntimer.viewmodel.BackUpViewModel
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import org.jetbrains.anko.startActivity

class BackupActivity : AppCompatActivity() {
    private val viewModel : BackUpViewModel by lazy {
        ViewModelProvider(this).get(BackUpViewModel::class.java)
    }
    private lateinit var binding : SettingBackupPageBinding
    private val backup = RoomBackup(this)
    private val restore = RoomBackup(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.setting_backup_page)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        init()
    }
    private fun init(){
        backUpInit()
        restoreInit()
        setButton()
    }
    private fun setButton(){
        binding.settingBackupBack.setOnClickListener {
            finish()
        }
        binding.settingBackupBackup.setOnClickListener {
            backup.backup()
        }
        binding.settingBackupRestore.setOnClickListener {
            restore.restore()
        }
    }
    private fun backUpInit(){
        backup
            .database(GlobalApplication.databaseInstance)
            .enableLogDebug(true)
            .backupIsEncrypted(true)
            .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
            .maxFileCount(5)
            .apply {
                onCompleteListener { success, _, _ ->
                    if(success) viewModel.setBackupDate()
                }
            }
    }
    private fun restoreInit(){
        restore
            .database(GlobalApplication.databaseInstance)
            .enableLogDebug(true)
            .backupIsEncrypted(true)
            .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
            .maxFileCount(5)
    }
}