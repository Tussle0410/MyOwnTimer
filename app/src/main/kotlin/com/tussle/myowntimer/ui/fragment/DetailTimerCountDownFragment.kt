package com.tussle.myowntimer.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.DetailTimerCountdownFrameBinding
import com.tussle.myowntimer.databinding.DetailTimerCountdownSetTimeBinding
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.model.DB.RepoFactory
import com.tussle.myowntimer.viewmodel.DetailViewModel

class DetailTimerCountDownFragment : Fragment() {
    private val viewModel : DetailViewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(requireActivity(),factory).get(DetailViewModel::class.java)
    }
    private lateinit var binding : DetailTimerCountdownFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_timer_countdown_frame,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        init()
        return binding.root
    }

    private fun init(){
        setButton()
        setChronometer()
    }
    private fun setDialog(){
        val bindingDialog = DetailTimerCountdownSetTimeBinding.inflate(LayoutInflater.from(requireContext()))
        with(bindingDialog){
            setHour.maxValue = 23
            setHour.minValue = 0
            setMin.maxValue = 59
            setMin.minValue = 0
            setSecond.maxValue = 59
            setSecond.minValue = 0
        }
        AlertDialog.Builder(requireContext())
            .setTitle("시간 설정")
            .setView(bindingDialog.root)
            .setPositiveButton("확 인") { _, _ ->
                viewModel.countDownTime =
                    (bindingDialog.setHour.value * 3600 +
                            bindingDialog.setMin.value * 60 +bindingDialog.setSecond.value).toLong()
                viewModel.countDownPauseTime = 0L
                binding.countDownChronometer.visibility = View.INVISIBLE
                binding.countDownText.visibility = View.VISIBLE
                binding.countDownText.text = viewModel.timeConvert(viewModel.countDownTime)
                binding.countDownStartButton.isEnabled = true
            }
            .show()

    }
    private fun setButton(){
        viewModel.countDownEvent.observe(requireActivity()) {
            if(!it){
                binding.countDownChronometer.base = SystemClock.elapsedRealtime() + viewModel.countDownPauseTime
                binding.countDownChronometer.start()
                if(!binding.countDownChronometer.isVisible){
                    binding.countDownChronometer.visibility = View.VISIBLE
                    binding.countDownText.visibility = View.INVISIBLE
                }
                binding.countDownStartButton.text = resources.getString(R.string.txt_stop)
                binding.setTimeButton.isEnabled = false
                Toast.makeText(requireContext(),"타이머 실행중에는 시간 설정을 하실 수 없습니다.",Toast.LENGTH_SHORT).show()
            }else{
                binding.countDownChronometer.stop()
                viewModel.countDownPauseTime = binding.countDownChronometer.base - SystemClock.elapsedRealtime()
                binding.countDownStartButton.text = resources.getString(R.string.txt_start)
                binding.setTimeButton.isEnabled = true
                viewModel.timeUpdate(false)
            }
        }
        binding.setTimeButton.setOnClickListener {
            setDialog()
            viewModel.saveTimeInit(false)
        }
    }
    private fun setChronometer(){
        binding.countDownChronometer.setOnChronometerTickListener {
            val time = viewModel.countDownTime - (SystemClock.elapsedRealtime() - it.base)/1000
            if(time<0){
                viewModel.countDownEnd()
                binding.countDownStartButton.isEnabled = false
                binding.countDownText.text = resources.getString(R.string.txt_initTime)
            }else{
                binding.countDownChronometer.format = viewModel.timeConvert(time)
                binding.progressBar.progress = ((time.toFloat()/viewModel.countDownTime)*100).toInt()
            }
        }
    }
}