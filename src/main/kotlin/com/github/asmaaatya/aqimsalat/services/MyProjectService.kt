package com.github.asmaaatya.aqimsalat.services

import com.github.asmaaatya.aqimsalat.api.PrayerApiClient
import com.intellij.openapi.components.Service
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

data class PrayerTimings(
    val Fajr: String,
    val Dhuhr: String,
    val Asr: String,
    val Maghrib: String,
    val Isha: String
){
    fun toLocalTimeList(): List<LocalTime> {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return listOfNotNull(
            Fajr.let { LocalTime.parse(it, formatter) },
            Dhuhr.let { LocalTime.parse(it, formatter) },
            Asr.let { LocalTime.parse(it, formatter) },
            Maghrib.let { LocalTime.parse(it, formatter) },
            Isha.let { LocalTime.parse(it, formatter) }
        )
    }
}

@Service(Service.Level.APP)
class PrayerService {

    /**
     * Fetch prayer times asynchronously (non-blocking).
     * @param city Name of the city
     * @param country Name of the country
     * @param method Calculation method (default 5)
     * @return CompletableFuture<PrayerTimings?>
     */
    fun getPrayerTimesAsync(
        city: String,
        country: String,
        method: Int
    ): CompletableFuture<PrayerTimings?> {
        return CompletableFuture.supplyAsync {
            try {
                val response = PrayerApiClient.service
                    .getPrayerTimes(city, country, method)
                    .execute()

                if (response.isSuccessful) {
                    val body = response.body()
                    body?.data?.timings?.let { timings ->
                        PrayerTimings(
                            Fajr = timings.fajr,
                            Dhuhr = timings.dhuhr,
                            Asr = timings.asr,
                            Maghrib = timings.maghrib,
                            Isha = timings.isha
                        )
                    }
                } else {
                    println("❌ API Error: ${response.code()} - ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

}
