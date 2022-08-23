package com.tussle.myowntimer.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.DetailTodoAddDialogBinding
import com.tussle.myowntimer.databinding.DetailTodoFrameBinding
import com.tussle.myowntimer.event.EventObserver
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.model.DB.RepoFactory
import com.tussle.myowntimer.ui.adapter.DetailTodoRecyclerAdapter
import com.tussle.myowntimer.ui.listener.SuccessUpdate
import com.tussle.myowntimer.viewmodel.DetailViewModel

class DetailTodoFragment : Fragment(), SuccessUpdate {
    private val viewModel : DetailViewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(requireActivity(),factory).get(DetailViewModel::class.java)
    }
    private lateinit var recyclerAdapter : DetailTodoRecyclerAdapter
    private lateinit var binding : DetailTodoFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_todo_frame,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        viewModel.getTodoInfo()
        setAddButton()
        viewModel.getTodoEvent.observe(requireActivity(), EventObserver {
            toDoSetting()
        })
        viewModel.insertTodoEvent.observe(requireActivity(), EventObserver{
            recyclerAdapter.notifyDataSetChanged()
        })
        return binding.root
    }
    //TodoAddButton Setting
    private fun setAddButton(){
        binding.detailTodoAddButton.setOnClickListener {
            if(viewModel.todoInfo.value!!.size >= 5){
                Toast.makeText(requireContext(),"할 일을 5개까지 만들 수 있습니다.", Toast.LENGTH_SHORT).show()
            }else{
                val bindingDialog = DetailTodoAddDialogBinding.inflate(LayoutInflater.from(binding.root.context))
                AlertDialog.Builder(requireActivity())
                    .setTitle("할 일 추가하기")
                    .setView(bindingDialog.root)
                    .setPositiveButton("추가", DialogInterface.OnClickListener { _, _ ->
                        viewModel.insertTodo(
                            bindingDialog.todoDialogEdit.text.toString(),
                            bindingDialog.todoDialogSuccess.isChecked)
                    })
                    .show()
            }
        }
    }
    //TodoRecyclerView Setting
    private fun toDoSetting(){
        recyclerAdapter = DetailTodoRecyclerAdapter(viewModel.todoInfo.value!!, this)
        with(binding.detailTodoRecycler){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }
    }
    //SuccessUpdate Interface Method
    override fun SuccessUpdate(todo: String, success: Boolean) {
        viewModel.todoSuccessUpdate(todo, success)
    }
    companion object{
        fun getInstance()
            =DetailTodoFragment()
    }
}