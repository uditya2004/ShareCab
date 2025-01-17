package com.example.sharecab.model

import kotlinx.serialization.Serializable

@Serializable
data class RideItem(
    val id: String,
    val driverId: String,
    val origin: String,
    val destination: String,
    val departureDate: String,
    val totalSeats: Int,
    val availableSeats: Int,
    val totalCost: Double,
    val costPerPerson: Double,
    val vehicleType: String,
    val status: String = "active",
    val createdAt: String
)