package com.tussle.myowntimer.sharedPreference

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context : Context) {
    private val titlePrefs : SharedPreferences = context.getSharedPreferences("title",0)
    fun titleGetString(key : String,value:String):String
            =titlePrefs.getString(key,value).toString()
    fun titleSetString(key : String,str:String){
        titlePrefs.edit().putString(key,str).apply()
    }
    fun titleGetInt(key : String, value : Int) : Int
        =titlePrefs.getInt(key,value)
    fun titleSetInt(key : String, num : Int){
        titlePrefs.edit().putInt(key,num).apply()
    }
}