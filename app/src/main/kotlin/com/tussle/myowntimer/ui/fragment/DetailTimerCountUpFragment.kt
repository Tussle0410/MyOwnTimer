package com.tussle.myowntimer.ui.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.DetailTimerCountupFrameBinding
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.model.DB.RepoFactory
import com.tussle.myowntimer.viewmodel.DetailViewModel

class DetailTimerCountUpFragment : Fragment() {
    private val viewModel : DetailViewModel by lazy{
        val factory = RepoFactory(Repo())
        ViewModelProvider(requireActivity(),factory).get(DetailViewModel::class.java)
    }
    private lateinit var binding : DetailTimerCountupFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_timer_countup_frame,container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        setButton()
        return binding.root
    }
    private fun setButton(){
        viewModel.countUpEvent.observe(requireActivity()) {
            if (!it) {
                binding.countUpChronometer.base =
                    SystemClock.elapsedRealtime() + viewModel.countUpPauseTime
                binding.countUpChronometer.start()
                binding.countUpStartButton.text = resources.getString(R.string.txt_stop)
                binding.countUpResetButton.visibility = View.INVISIBLE
            } else {
                binding.countUpChronometer.stop()
                viewModel.countUpPauseTime =
                    binding.countUpChronometer.base - SystemClock.elapsedRealtime()
                binding.countUpStartButton.text = resources.getString(R.string.txt_start)
                binding.countUpResetButton.visibility = View.VISIBLE
                if (!viewModel.timeUpdate(true))
                    Toast.makeText(
                        requireContext(),
                        "시작 버튼을 누른 후 3초 이상 지나야 목표 정보에 저장됩니다.",
                        Toast.LENGTH_SHORT
                    ).show()

            }
        }
        binding.countUpResetButton.setOnClickListener {
            binding.countUpChronometer.base = SystemClock.elapsedRealtime()
            viewModel.countUpPauseTime = 0L
            viewModel.saveTimeInit(true)
        }
    }
}