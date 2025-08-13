package com.github.asmaaatya.aqimsalat.api

import com.intellij.testFramework.Timings

//import com.squareup.moshi.Json

data class PrayerTimesResponse(
    val data: PrayersData
) {
    data class PrayersData(
        val timings: Timings
    )
//
//    data class Timings(
//        @Json(name = "Fajr") val fajr: String,
//        @Json(name = "Dhuhr") val dhuhr: String,
//        @Json(name = "Asr") val asr: String,
//        @Json(name = "Maghrib") val maghrib: String,
//        @Json(name = "Isha") val isha: String
//    )
}
