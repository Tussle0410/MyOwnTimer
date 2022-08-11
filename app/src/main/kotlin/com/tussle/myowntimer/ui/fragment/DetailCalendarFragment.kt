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
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.CalendarDayLayoutBinding
import com.tussle.myowntimer.databinding.DetailCalendarFrameBinding
import com.tussle.myowntimer.ui.adapter.DetailCalendarRecyclerAdapter
import com.tussle.myowntimer.ui.adapter.DetailTodoRecyclerAdapter
import com.tussle.myowntimer.viewmodel.DetailViewModel
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class DetailCalendarFragment : Fragment() {
    private val test = mutableListOf("할일1", "할일2", "할일3", "할일4","할일5","할일6","할일7","할일8",
        "할일9", "할일10")
    private val viewModel : DetailViewModel by lazy {
        ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)
    }
    private lateinit var binding : DetailCalendarFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_calendar_frame,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        toDoSetting()
        var currentMonth = YearMonth.now()
        var firstMonth = currentMonth.minusMonths(10)
        var lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        binding.calendarView.apply {
            setup(firstMonth,lastMonth, firstDayOfWeek)
            scrollToMonth(currentMonth)
        }
        class DayViewContainer(view: View) : ViewContainer(view) {
            val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
        }
        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    container.textView.setTextColor(Color.BLACK)
                } else {
                    container.textView.setTextColor(Color.GRAY)
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    private fun toDoSetting(){
        with(binding.calendarRecycler){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = DetailCalendarRecyclerAdapter(test)
        }
    }
    companion object{
        fun getInstance()
                =DetailCalendarFragment()
    }
}