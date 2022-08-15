package com.tussle.myowntimer.model.DB

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RepoFactory(private val repo: Repo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Repo::class.java).newInstance(repo)
    }
}