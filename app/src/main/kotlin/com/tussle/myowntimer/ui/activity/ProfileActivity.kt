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
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.model.DB.RepoFactory
import com.tussle.myowntimer.ui.formatter.PieChartFormatter
import com.tussle.myowntimer.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    private val viewModel : ProfileViewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
    }
    private val entries = ArrayList<PieEntry>()
    private var pieChartColors : MutableList<Int> = mutableListOf()
    private lateinit var binding : ProfilePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.profile_page)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        init()
    }
    //Init ProfileFragment Setting
    private fun init(){
        setBackButton()
        viewModel.getTitleInfo()
        viewModel.titleInfo.observe(this){
            viewModel.setProfileInfo()
            setEntries()
            setPieChart()
        }
    }
    private fun setEntries(){
        for(info in viewModel.titleInfo.value!!)
            entries.add(PieEntry(info.totalTime.toFloat(), info.title))
    }
    //ProfileFragment BackButton Setting Method
    private fun setBackButton(){
        binding.profileBackButton.setOnClickListener {
            finish()
        }
    }
    //PieChart Setting
    private fun setPieChart(){
        setPieChartColor()
        val pieDataSet = PieDataSet(entries,"").apply {
            colors = pieChartColors     //PieChart Colors
            valueLinePart1Length = 0.6f        //ValueLine1(-)
            valueLinePart2Length = 0.3f         //ValueLine2(/)
            valueLineWidth = 2f     //ValueLine Width
            valueLinePart1OffsetPercentage = 115f
            isUsingSliceColorAsValueLineColor = true    //PieChartColor == ValueLineColor
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE     //ValueLine Position OutSide
            valueTextSize = 16f     //valueTextSize
            valueTypeface = Typeface.DEFAULT_BOLD       //valueText Bold
            valueFormatter = PieChartFormatter()
        }
        val pieData = PieData(pieDataSet).apply {
            setValueTextSize(20f)
            setValueTextColor(Color.BLACK)
        }
        with(binding.profilePieChart){
            setUsePercentValues(false)
            description.isEnabled = false       //description InVisible
            setExtraOffsets(25f, 25f, 25f, 25f)
            dragDecelerationFrictionCoef = 0.95f
            transparentCircleRadius = 61f
            setEntryLabelTextSize(20f)
            setDrawEntryLabels(false)
            setHoleColor(Color.WHITE)       //PieChart Center Hole Color
            data = pieData
            invalidate()
        }
    }
    //Set ColorList
    private fun setPieChartColor(){
        pieChartColors = resources.getIntArray(R.array.pieChartColor).toMutableList()
    }
}