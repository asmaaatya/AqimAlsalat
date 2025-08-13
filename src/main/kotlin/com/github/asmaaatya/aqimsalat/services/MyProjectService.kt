package com.github.asmaaatya.aqimsalat.services

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class PrayerTimings(val Fajr: String, val Dhuhr: String, val Asr: String, val Maghrib: String, val Isha: String)
data class PrayerData(val timings: PrayerTimings)
data class PrayerResponse(val data: PrayerData)

object PrayerService {
    private val client = OkHttpClient()
    private val gson = Gson()

    fun getPrayerTimes(city: String, country: String, method: Int = 5): PrayerTimings? {
        val date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val url = "https://api.aladhan.com/v1/timingsByCity/$date?city=$city&country=$country&method=$method"

        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            val parsed = gson.fromJson(body, PrayerResponse::class.java)
            return parsed.data.timings
        }
    }
}

