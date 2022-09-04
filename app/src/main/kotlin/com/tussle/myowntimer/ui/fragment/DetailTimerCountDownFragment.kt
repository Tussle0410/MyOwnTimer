package com.tussle.myowntimer.ui.fragment

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
    private lateinit var countDownTimer : CountDownTimer
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_timer_countdown_frame,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        setButton()
        return binding.root
    }
    //CountDown SetTime Dialog Setting
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
                viewModel.dialogInfoToCountDownTime(
                    bindingDialog.setHour.value,bindingDialog.setMin.value, bindingDialog.setSecond.value)
                viewModel.initCountDownPauseTime()
                viewModel.countDownSaveTimeSet(viewModel.countDownTime)
                binding.countDownText.text = viewModel.timeConvert(viewModel.countDownTime/1000)
                binding.countDownStartButton.isEnabled = true
                setCountDownTimer(viewModel.countDownTime)
            }
            .show()

    }
    //CountDown Fragment Start, End.. Button Setting
    private fun setButton(){
        viewModel.countDownEvent.observe(requireActivity()) {
            if(!it){
                countDownTimer.start()
                binding.countDownStartButton.text = resources.getString(R.string.txt_stop)
                binding.setTimeButton.isEnabled = false
                Toast.makeText(requireContext(),"타이머 실행중에는 시간 설정을 하실 수 없습니다.",Toast.LENGTH_SHORT).show()
                setNotification()
            }else{
                countDownTimer.cancel()
                setCountDownTimer(viewModel.countDownPauseTime)
                binding.countDownStartButton.text = resources.getString(R.string.txt_start)
                binding.setTimeButton.isEnabled = true
                viewModel.timeUpdate(false)
                removeNotification()
            }
        }
        binding.setTimeButton.setOnClickListener {
            setDialog()
            viewModel.saveTimeInit(false)
        }
    }
    //CountDown Timer Start Notification
    private fun setNotification(){
        val builder = NotificationCompat.Builder(requireContext(), "Start")
            .setSmallIcon(R.drawable.icon_timer)
            .setContentTitle("타이머가 진행중입니다.")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val manager : NotificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("Start","Basic",NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        manager.notify(1, builder.build())
    }
    //CountDown Timer End Notification
    private fun removeNotification(){
        NotificationManagerCompat.from(requireContext()).cancel(1)
    }
    //setCountDownTimer
    private fun setCountDownTimer(time : Long){
        countDownTimer = object : CountDownTimer(time, 1000){
            override fun onTick(p0: Long) {
                binding.countDownText.text = viewModel.timeConvert(p0/1000)
                viewModel.countDownPauseTimeSet(p0)
                binding.progressBar.progress = viewModel.progressValueCal(p0)
            }
            override fun onFinish() {
                removeNotification()
                viewModel.countDownEnd()
                binding.countDownStartButton.isEnabled = false
                binding.countDownText.text = resources.getString(R.string.txt_initTime)
                val player = MediaPlayer.create(requireContext(), R.raw.timer_end_sound)
                player.start()
            }
        }
    }
}