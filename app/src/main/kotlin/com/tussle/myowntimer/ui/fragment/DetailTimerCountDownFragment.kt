package com.tussle.myowntimer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.DetailTimerCountdownFrameBinding
import com.tussle.myowntimer.viewmodel.DetailViewModel

class DetailTimerCountDownFragment : Fragment() {
    private val viewModel : DetailViewModel by lazy {
        ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)
    }
    private lateinit var binding : DetailTimerCountdownFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_timer_countdown_frame,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}