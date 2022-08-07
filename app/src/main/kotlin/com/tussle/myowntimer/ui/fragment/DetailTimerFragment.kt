package com.tussle.myowntimer.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.DetailTimerFrameBinding
import com.tussle.myowntimer.event.EventObserver
import com.tussle.myowntimer.viewmodel.DetailViewModel

class DetailTimerFragment : Fragment() {
    private val viewModel : DetailViewModel by lazy {
        ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)
    }
    private lateinit var binding : DetailTimerFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_timer_frame,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        initFrame()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.countUpButtonEvent.observe(requireActivity(),EventObserver{
            if(it){
                getCountUpFrame()
                setCountUpStroke(true)
                setCountDownStroke(false)
            }
        })
        viewModel.countDownButtonEvent.observe(requireActivity(),EventObserver{
            if(it){
                getCountDownFrame()
                setCountDownStroke(true)
                setCountUpStroke(false)
            }

        })
        super.onViewCreated(view, savedInstanceState)
    }
    private fun setCountDownStroke(check : Boolean){
        val shape = resources.getDrawable(R.drawable.countdown_border) as GradientDrawable
        if(check)
            shape.setStroke(5, Color.BLACK)
        else
            shape.setStroke(5,requireContext().getColor(R.color.gray))
        binding.countDownButton.background = shape
    }
    private fun setCountUpStroke(check : Boolean){
        val shape = resources.getDrawable(R.drawable.countup_border) as GradientDrawable
        if(check)
            shape.setStroke(5, Color.BLACK)
        else
            shape.setStroke(5,requireContext().getColor(R.color.gray))
        binding.countUpButton.background = shape
    }
    private fun initFrame(){
        val transaction = childFragmentManager.beginTransaction()
        transaction.apply {
            add(R.id.timer_frame,DetailTimerCountDownFragment(), "countDown")
            add(R.id.timer_frame,DetailTimerCountUpFragment(), "countUp")
            commitNowAllowingStateLoss()
        }
        getCountUpFrame()
    }
    private fun getCountUpFrame(){
        val transaction = childFragmentManager.beginTransaction()
        transaction.apply {
            show(childFragmentManager.findFragmentByTag("countUp")!!)
            hide(childFragmentManager.findFragmentByTag("countDown")!!)
            commitNowAllowingStateLoss()
        }
    }
    private fun getCountDownFrame(){
        val transaction = childFragmentManager.beginTransaction()
        transaction.apply {
            show(childFragmentManager.findFragmentByTag("countDown")!!)
            hide(childFragmentManager.findFragmentByTag("countUp")!!)
            commitNowAllowingStateLoss()
        }
    }
    companion object{
        fun getInstance()
            = DetailTimerFragment()
    }
}