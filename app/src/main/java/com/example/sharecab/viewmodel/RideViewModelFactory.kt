package com.example.sharecab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharecab.repository.RideRepository

class RideViewModelFactory(
    private val repository: RideRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RideViewModel::class.java)) {
            return RideViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
