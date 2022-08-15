package com.tussle.myowntimer.ui.activity

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.ProfilePageBinding
import com.tussle.myowntimer.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    private val viewModel : ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }
    private val test = ArrayList<PieEntry>()
    private lateinit var binding : ProfilePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.profile_page)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setPieChart()
    }
    private fun setTestEntry(){
        with(test){
            val range = (100..600)
            for( i in 1..4)
                test.add(PieEntry(range.random().toFloat(), "주제$i"))
        }
    }
    private fun setPieChart(){
        setTestEntry()
        val color = listOf(Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW)
        val pieDataSet = PieDataSet(test,"").apply {
            colors = color
            valueLinePart1Length = 0.6f
            valueLinePart2Length = 0.3f
            valueLineWidth = 2f
            valueLinePart1OffsetPercentage = 115f
            isUsingSliceColorAsValueLineColor = true
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            valueTextSize = 16f
            valueTypeface = Typeface.DEFAULT_BOLD
        }
        val pieData = PieData(pieDataSet).apply {
            setValueTextSize(20f)
            setValueTextColor(Color.BLACK)
        }
        with(binding.profilePieChart){
            setUsePercentValues(true)
            description.isEnabled = false
            setExtraOffsets(25f, 25f, 25f, 25f)
            dragDecelerationFrictionCoef = 0.95f
            transparentCircleRadius = 61f
            setEntryLabelTextSize(20f)
            setDrawEntryLabels(false)
            setHoleColor(Color.WHITE)
            data = pieData
            invalidate()

        }
    }
}