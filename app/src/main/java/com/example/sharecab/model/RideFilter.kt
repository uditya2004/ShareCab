package com.example.sharecab.model

data class RideFilter(
    val date: String? = null,
    val vehicleType: String = "All Vehicle",
    val sortingOption: String = "Select sorting"
)