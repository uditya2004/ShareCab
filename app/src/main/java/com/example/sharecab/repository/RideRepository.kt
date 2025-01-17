package com.example.sharecab.repository

import com.example.sharecab.model.RideItem
import com.example.sharecab.supabase.SupabaseClientInstance
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json


class RideRepository {

    private val client = SupabaseClientInstance.client

    // Fetch active rides
    fun getActiveRides(): Flow<List<RideItem>> = flow {
        try {
            // Fetching data and decoding it directly
            val ridesList: List<RideItem> = client
                .from("rides")
                .select { filter { eq("status", "active") } }
                .decodeList()

            // Emitting the decoded list
            emit(ridesList)
        } catch (e: Exception) {
            // Handling errors during fetch or decoding
            throw Exception("Error fetching rides: ${e.message}", e)
        }
    }

    // Create a new ride
    suspend fun createRide(ride: RideItem) {
        try {
            val response = client.from("rides").insert(
                mapOf(
                    "driver_id" to ride.driverId,
                    "origin" to ride.origin,
                    "destination" to ride.destination,
                    "departure_date" to ride.departureDate,
                    "total_seats" to ride.totalSeats,
                    "available_seats" to ride.availableSeats,
                    "total_cost" to ride.totalCost,
                    "cost_per_person" to ride.costPerPerson,
                    "vehicle_type" to ride.vehicleType
                )
            ).decodeSingle<RideItem>() // Decode the response to a single RideItem object

            // You can handle the response further if necessary
            println("Ride created successfully: $response")
        } catch (e: Exception) {
            // Handle any exceptions that occur during insertion
            throw Exception("Error creating ride: ${e.message}", e)
        }
    }

    // Real-time subscription for ride updates
    fun subscribeToRideChanges(): Flow<List<RideItem>> {
        val channel = client.realtime.channel("rides-channel")

        // Subscribe to changes in the "rides" table
        return channel.postgresChangeFlow<PostgresAction>(
            schema = "public"
        ) {
            table = "rides"
        }.map { action ->
            when (action) {
                is PostgresAction.Insert -> listOf(
                    Json.decodeFromString<RideItem>(action.record.toString())
                )
                is PostgresAction.Update -> listOf(
                    Json.decodeFromString<RideItem>(action.record.toString())
                )
                else -> emptyList()
            }
        }
    }
}