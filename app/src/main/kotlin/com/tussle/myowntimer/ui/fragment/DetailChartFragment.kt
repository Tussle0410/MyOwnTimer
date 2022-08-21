package com.tussle.myowntimer.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.DetailChartFrameBinding
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.model.DB.RepoFactory
import com.tussle.myowntimer.viewmodel.DetailViewModel

class DetailChartFragment : Fragment() {
    private val viewModel : DetailViewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(requireActivity(),factory).get(DetailViewModel::class.java)
    }
    private val entries = ArrayList<BarEntry>()
    private lateinit var binding : DetailChartFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_chart_frame,container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        setEntries()
        setChart()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    private fun setEntries(){
        val range = (30..300)
        with(entries){
            for( n in 2..31)
                add(BarEntry(n.toFloat(),range.random().toFloat()))
        }
    }
    private fun setChart(){
        binding.barChart.run {
            description.isEnabled = false      //설명 보이지 않게
            setMaxVisibleValueCount(31)     //최대 보이는 그래프 31 설정
            setPinchZoom(false)     //두 손가락 줌인 거부
            setDrawBarShadow(false)     //그래프 바 그림자 제거
            setDrawGridBackground(false)        //배경에 격자판 제거
            axisLeft.run {      //y축 설정
                axisMaximum = 601f      //높이 최대
                axisMinimum = 0f        //높이 최소
                granularity = 60f       //50단위마다 선을 그려준다.
                setLabelCount(4 ,true)
                setDrawGridLines(false)  //격자 라인 활용
                valueFormatter = MyYAxisFormatter()
                axisLineColor = R.color.gray        //축 색깔 설정
                gridColor = R.color.gray        //격자판 색
                textColor = R.color.black       //글자 색
                textSize = 14f      //글자 크기기
            }
            axisRight.run {
                setDrawLabels(false)
                setDrawGridLines(false)
            }
            xAxis.run {
                axisMaximum = 32f
                axisMinimum = 1.7f
                setLabelCount(6, true)
                position = XAxis.XAxisPosition.BOTTOM//X축을 아래에다가 둔다.
                granularity = 1f // 1 단위만큼 간격 두기
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
                textColor = R.color.black//라벨 색상
                valueFormatter = MyXAxisFormatter() // 축 라벨 값 바꿔주기 위함
                textSize = 14f // 텍스트 크기
            }
        }
        val set = BarDataSet(entries, "DataSet")
        set.color = Color.RED
        val dataSet : ArrayList<IBarDataSet> = ArrayList()
        dataSet.add(set)
        val data = BarData(dataSet)
        data.barWidth = 0.3f
        data.setDrawValues(false)
        binding.barChart.run {
            this.data = data
            setFitBars(true)
            invalidate()
        }
    }
    inner class MyXAxisFormatter : ValueFormatter(){
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            if (value==1.7f)
                return "1"
            return (value-1).toInt().toString()
        }
    }
    inner class MyYAxisFormatter : ValueFormatter(){
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return (value/60).toInt().toString() + "시간"
            //return days.getOrNull(value.toInt()-1) ?: ""
        }
    }
    companion object{
        fun getInstance()
            = DetailChartFragment()
    }
}