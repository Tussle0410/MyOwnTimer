package com.tussle.myowntimer.ui.fragment

import android.app.AlertDialog
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
import com.tussle.myowntimer.databinding.UpdateDialogBinding
import com.tussle.myowntimer.event.EventObserver
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.model.DB.RepoFactory
import com.tussle.myowntimer.ui.adapter.DetailTodoRecyclerAdapter
import com.tussle.myowntimer.ui.listener.TodoSuccessUpdate
import com.tussle.myowntimer.ui.listener.TodoTextUpdate
import com.tussle.myowntimer.viewmodel.DetailViewModel

class DetailTodoFragment : Fragment(), TodoSuccessUpdate, TodoTextUpdate {
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
        viewModel.todoInfo.observe(requireActivity()){
            toDoSetting()
        }
        viewModel.todoUpdateEvent.observe(requireActivity(), EventObserver{
            recyclerAdapter.notifyDataSetChanged()
        })
        viewModel.todoDeleteEvent.observe(requireActivity(), EventObserver{
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
                val alertDialog = AlertDialog.Builder(requireActivity())
                    .setView(bindingDialog.root)
                    .show()
                bindingDialog.todoAddCancel.setOnClickListener {
                    alertDialog.cancel()
                }
                bindingDialog.todoAddAdd.setOnClickListener {
                    viewModel.insertTodo(
                        bindingDialog.todoDialogEdit.text.toString(),
                        bindingDialog.todoDialogSuccess.isChecked)
                    alertDialog.cancel()
                }
            }
        }
    }
    //TodoRecyclerView Setting
    private fun toDoSetting(){
        recyclerAdapter = DetailTodoRecyclerAdapter(viewModel.todoInfo.value!!, this, this)
        with(binding.detailTodoRecycler){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }
    }
    //TodoSuccessUpdate Interface Method
    override fun successUpdate(todo: String, success: Boolean) {
        viewModel.todoSuccessUpdate(todo, success)
    }
    //TodoTextUpdate Interface Method
    override fun todoTextUpdateListener(previousTodo: String) {
        val bindingDialog = UpdateDialogBinding.inflate(LayoutInflater.from(binding.root.context))
        bindingDialog.updateCategory.text = "할 일"
        val alertDialog = AlertDialog.Builder(requireActivity())
            .setView(bindingDialog.root)
            .show()

        bindingDialog.todoUpdateDialogEdit.setText(previousTodo)
        bindingDialog.todoUpdateCancel.setOnClickListener {
            alertDialog.cancel()
        }
        bindingDialog.todoUpdateDelete.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setTitle("정말 삭제하시겠습니까?")
                .setPositiveButton("삭 제") { _,_ ->
                    viewModel.deleteTodo(bindingDialog.todoUpdateDialogEdit.text.toString())
                    alertDialog.cancel()
                }
                .setNegativeButton("취 소"){ _,_ ->}
                .show()
        }
        bindingDialog.todoUpdateUpdate.setOnClickListener {
            viewModel.todoUpdate(bindingDialog.todoUpdateDialogEdit.text.toString(), previousTodo)
            alertDialog.cancel()
        }
    }
    companion object{
        fun getInstance()
            =DetailTodoFragment()
    }
}