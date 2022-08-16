package com.tussle.myowntimer.ui.fragment

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
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
        setChronometer()
        setButton()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    private fun setButton(){
        viewModel.countUpEvent.observe(requireActivity(), Observer {
            if(!it){
                binding.countUpChronometer.base = SystemClock.elapsedRealtime() + viewModel.pauseTime
                binding.countUpChronometer.start()
                binding.countUpStartButton.text = resources.getString(R.string.txt_stop)
            }else{
                viewModel.pauseTime = binding.countUpChronometer.base - SystemClock.elapsedRealtime()
                binding.countUpChronometer.stop()
                binding.countUpStartButton.text = resources.getString(R.string.txt_start)
            }
        })
    }
    private fun setChronometer(){
        binding.countUpChronometer.setOnChronometerTickListener {
            val time = (SystemClock.elapsedRealtime() - it.base)/1000
            val h = time/3600
            val m = (time/60)%60
            val s = time%60
            val txtH = if(h<10) "0$h" else h.toString()
            val txtM = if(m<10) "0$m" else m.toString()
            val txtS = if(s<10) "0$s" else s.toString()
            it.format = "$txtH:$txtM:$txtS"
        }
    }
}