package com.tussle.myowntimer.sharedPreference

import android.app.Application
import androidx.room.Room
import com.tussle.myowntimer.model.DB.DB

class GlobalApplication : Application() {
    companion object{
        lateinit var prefs : PreferenceUtil
        lateinit var appInstance : GlobalApplication
            private set

        lateinit var databaseInstance : DB
            private set
    }

    override fun onCreate() {
        super.onCreate()
        prefs = PreferenceUtil(applicationContext)

        appInstance = this
        databaseInstance = Room.databaseBuilder(
            appInstance.applicationContext,
            DB::class.java,
            "DB"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}