package com.github.asmaaatya.aqimsalat.api


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PrayerTimesApi {
    @GET("v1/timingsByCity")
    fun getPrayerTimes(
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("method") method: Int = 5
    ): Call<PrayerTimesResponse>
}