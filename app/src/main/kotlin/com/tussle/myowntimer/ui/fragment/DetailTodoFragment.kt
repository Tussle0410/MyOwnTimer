package com.tussle.myowntimer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.DetailTodoFrameBinding
import com.tussle.myowntimer.ui.adapter.DetailTodoRecyclerAdapter
import com.tussle.myowntimer.viewmodel.DetailViewModel

class DetailTodoFragment : Fragment() {
    private val test = mutableListOf("할일1", "할일2", "할일3", "할일4","할일5","할일6","할일7","할일8",
        "할일9", "할일10")
    private val viewModel : DetailViewModel by lazy {
        ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)
    }
    private lateinit var binding : DetailTodoFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_todo_frame,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        toDoSetting()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    private fun toDoSetting(){
        with(binding.detailTodoRecycler){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = DetailTodoRecyclerAdapter(test)
        }
    }
    companion object{
        fun getInstance()
            =DetailTodoFragment()
    }
}