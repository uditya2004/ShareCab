package com.example.sharecab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharecab.model.RideFilter
import com.example.sharecab.model.RideItem
import com.example.sharecab.repository.RideRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RideViewModel(
    private val repository: RideRepository
) : ViewModel() {

    // Holds the list of rides to display in the UI
    private val _rides = MutableStateFlow<List<RideItem>>(emptyList())
    val rides: StateFlow<List<RideItem>> = _rides.asStateFlow()

    // Holds errors to notify the UI
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Filter for rides
    private val _filter = MutableStateFlow(RideFilter())
    val filter: StateFlow<RideFilter> = _filter.asStateFlow()

    // Subscribe to ride updates in real-time
    private val _realTimeRides = MutableSharedFlow<List<RideItem>>(replay = 1)

    init {
        fetchRides() // Fetch initial rides
        subscribeToRideUpdates() // Subscribe to real-time updates
    }

    // Fetch active rides
    fun fetchRides() {
        viewModelScope.launch {
            try {
                repository.getActiveRides().collect { rideList ->
                    _rides.value = rideList
                }
            } catch (e: Exception) {
                _error.value = "Error fetching rides: ${e.message}"
            }
        }
    }

    // Create a new ride
    fun createRide(ride: RideItem) {
        viewModelScope.launch {
            try {
                repository.createRide(ride)
                fetchRides() // Refresh the rides list after creation
            } catch (e: Exception) {
                _error.value = "Error creating ride: ${e.message}"
            }
        }
    }

    // Subscribe to real-time ride updates
    private fun subscribeToRideUpdates() {
        viewModelScope.launch {
            repository.subscribeToRideChanges().collect { updatedRides ->
                _realTimeRides.emit(updatedRides)
            }
        }
    }

    // Update filter
    fun updateFilter(newFilter: RideFilter) {
        _filter.value = newFilter
        fetchRides() // Apply the new filter and fetch rides
    }
}
