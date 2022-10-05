package com.tussle.myowntimer.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.CalendarDayLayoutBinding
import com.tussle.myowntimer.databinding.CalendarHeadLayoutBinding
import com.tussle.myowntimer.databinding.DetailCalendarFrameBinding
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.model.DB.RepoFactory
import com.tussle.myowntimer.ui.adapter.DetailCalendarRecyclerAdapter
import com.tussle.myowntimer.viewmodel.DetailViewModel
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class DetailCalendarFragment : Fragment() {
    private val viewModel : DetailViewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(requireActivity(),factory).get(DetailViewModel::class.java)
    }
    private lateinit var binding : DetailCalendarFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_calendar_frame,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        toDoSetting()
        calendarSetting()
        return binding.root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            textViewSetting(viewModel.curCalendarDate)
            viewModel.getCalendarTodo(viewModel.curCalendarDate)
        }
    }
    //Calendar Setting
    private fun calendarSetting(){
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        textViewSetting(viewModel.date)
        viewModel.getCalendarTodo(viewModel.date)
        binding.calendarView.apply {
            setup(firstMonth,lastMonth, firstDayOfWeek)
            scrollToMonth(currentMonth)
        }
        class DayViewContainer(view: View) : ViewContainer(view) {
            val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
        }
        class MonthHeaderContainer(view : View) : ViewContainer(view){
            val yearMonth = CalendarHeadLayoutBinding.bind(view).headYearMonth
        }
        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()
                val date = "${day.date} 00:00:00"
                if(viewModel.calendarTimeEmpty(date)){
                    container.textView.setBackgroundColor(resources.getColor(R.color.gray))
                }else
                    container.textView.setBackgroundColor(Color.WHITE)

                if (day.owner == DayOwner.THIS_MONTH)
                    container.textView.setTextColor(Color.BLACK)
                else
                    container.textView.setTextColor(Color.GRAY)
                container.textView.setOnClickListener {
                    textViewSetting(date)
                    viewModel.setCalendarDate(date)
                    viewModel.getCalendarTodo(date)
                }
            }
        }
        binding.calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthHeaderContainer>{
            override fun create(view: View): MonthHeaderContainer = MonthHeaderContainer(view)

            override fun bind(container: MonthHeaderContainer, month: CalendarMonth) {
                container.yearMonth.text = month.yearMonth.toString()
            }
        }
    }
    //Calendar TodoSetting
    private fun toDoSetting(){
        binding.calendarRecycler.layoutManager = LinearLayoutManager(requireContext())
        viewModel.calendarTodoInfo.observe(requireActivity()){
            binding.calendarRecycler.adapter = DetailCalendarRecyclerAdapter(it, requireContext())
        }
    }
    //Calendar TextViewSetting
    private fun textViewSetting(date : String){
        binding.detailCalendarTime.text = viewModel.getCalendarTimeHashConvert(date)
    }
    companion object{
        fun getInstance()
                =DetailCalendarFragment()
    }
}