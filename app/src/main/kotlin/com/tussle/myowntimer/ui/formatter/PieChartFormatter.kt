package com.tussle.myowntimer.ui.formatter

import android.util.Log
import com.github.mikephil.charting.formatter.ValueFormatter

class PieChartFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val mValue = value.toInt()
        val h = mValue/3600
        val m = (mValue/60)%60
        val s = mValue%60
        val txtH = if(h<10) "0$h" else h.toString()
        val txtM = if(m<10) "0$m" else m.toString()
        val txtS = if(s<10) "0$s" else s.toString()
        return "$txtH:$txtM:$txtS"
    }
}