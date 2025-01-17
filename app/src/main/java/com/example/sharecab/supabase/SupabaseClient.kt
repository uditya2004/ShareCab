package com.example.sharecab.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import kotlin.time.Duration.Companion.seconds

// Replace with your Supabase URL and Anon Key
private const val SUPABASE_URL = "SUPABASE URL"
private const val SUPABASE_ANON_KEY = "SUPABASE KEY"

// Singleton Supabase Client
object SupabaseClientInstance {
    val client: SupabaseClient = createSupabaseClient(SUPABASE_URL,SUPABASE_ANON_KEY){
        install(Postgrest)
        install(Realtime)
    }
}