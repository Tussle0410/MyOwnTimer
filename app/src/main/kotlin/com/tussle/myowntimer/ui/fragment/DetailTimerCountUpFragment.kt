package com.tussle.myowntimer.ui.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.DetailTimerCountupFrameBinding
import com.tussle.myowntimer.viewmodel.DetailViewModel

class DetailTimerCountUpFragment : Fragment() {
    private val viewModel : DetailViewModel by lazy{
        ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)
    }
    private lateinit var binding : DetailTimerCountupFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_timer_countup_frame,container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        viewModel.countUpEvent.observe(requireActivity(), Observer {
            if(!it){
                binding.countUpChronometer.start()
                binding.countUpStartButton.text = resources.getString(R.string.txt_stop)
            }else{
                binding.countUpChronometer.stop()
                binding.countUpStartButton.text = resources.getString(R.string.txt_start)
                viewModel.mStartTime = binding.countUpChronometer.base
            }
        })
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    private fun initChronometer(){
        if(viewModel.mStartTime == 0L){
            val startTime = SystemClock.elapsedRealtime()
            viewModel.mStartTime = startTime
            binding.countUpChronometer.base = startTime
        }else
            binding.countUpChronometer.base = viewModel.mStartTime
    }
}