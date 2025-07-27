package com.github.asmaaatya.aqimsalat.services

import com.github.asmaaatya.aqimsalat.api.PrayerApiClient
import com.github.asmaaatya.aqimsalat.api.PrayerTimesResponse
import com.github.asmaaatya.aqimsalat.setting.PrayerSettingsState
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.Duration

@Service(Service.Level.PROJECT)
class MyProjectService(private val project: Project) {
    private val logger = thisLogger()
    private var prayerTimes: List<LocalTime> = emptyList()
    private var lastFetchTime: Instant? = null
     var testMode = false
    private var testPrayerTimes: List<LocalTime> = emptyList()

    fun enableTestMode() {
        testMode = true
        val now = LocalTime.now()
        testPrayerTimes = listOf(
            now.minusMinutes(30),  // Fajr (past)
            now.plusMinutes(2),    // Dhuhr (very soon)
            now.plusMinutes(30),   // Asr
            now.plusHours(1),      // Maghrib
            now.plusHours(2)       // Isha
        )
        logger.info("Test mode enabled with mock prayer times")
    }

    fun disableTestMode() {
        testMode = false
        fetchInitialPrayerTimes()
    }

    fun getPrayerTimesForTesting(): List<LocalTime> {
        return if (testMode) testPrayerTimes else prayerTimes
    }

    // Modify existing methods to use test times when in test mode
    fun getNextPrayerTime(): LocalTime? {
        val times = if (testMode) testPrayerTimes else prayerTimes
        val now = LocalTime.now()
        return times.firstOrNull { it.isAfter(now) } ?: times.firstOrNull()
    }


    init {
        logger.info("Initializing PrayerTimeService for project: ${project.name}")
        initializeService()
    }

    private fun initializeService() {
        try {
            fetchInitialPrayerTimes()
            setupPrayerTimeRefresh()
        } catch (e: Exception) {
            logger.error("Failed to initialize PrayerTimeService", e)
        }
    }

    private fun fetchInitialPrayerTimes() {
        val settings = PrayerSettingsState.getInstance().state
       PrayerApiClient.service.getPrayerTimes(
            city = settings.city,
            country = settings.country,
            method = settings.method
        ).enqueue(object : Callback<PrayerTimesResponse> {
            override fun onResponse(call: Call<PrayerTimesResponse>, response: Response<PrayerTimesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data?.timings?.let { timings ->
                        prayerTimes = parsePrayerTimes(timings)
                        lastFetchTime = Instant.now()
                        logger.info("Successfully fetched prayer times for ${settings.city}, ${settings.country}")
                    }
                } else {
                    logger.warn("Failed to fetch prayer times: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PrayerTimesResponse>, t: Throwable) {
                logger.error("API call failed for prayer times", t)
            }
        })
    }

    private fun parsePrayerTimes(timings: PrayerTimesResponse.Timings): List<LocalTime> {
        return try {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            listOf(
                LocalTime.parse(timings.fajr, formatter),
                LocalTime.parse(timings.dhuhr, formatter),
                LocalTime.parse(timings.asr, formatter),
                LocalTime.parse(timings.maghrib, formatter),
                LocalTime.parse(timings.isha, formatter)
            )
        } catch (e: Exception) {
            logger.error("Error parsing prayer times", e)
            emptyList()
        }
    }

    private fun setupPrayerTimeRefresh() {
        // Refresh prayer times every 6 hours
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(6 * 60 * 60 * 1000) // 6 hours
                fetchInitialPrayerTimes()
            }
        }
    }



//    fun getNextPrayerTime(): LocalTime? {
//        val now = LocalTime.now()
//        return prayerTimes.firstOrNull { it.isAfter(now) } ?: prayerTimes.firstOrNull()
//    }

    fun getAllPrayerTimes(): List<LocalTime> = prayerTimes

    fun timeUntilNextPrayer(): Duration? {
        val nextPrayer = getNextPrayerTime() ?: return null
        val now = LocalTime.now()
        return if (nextPrayer.isAfter(now)) {
            Duration.between(now, nextPrayer)
        } else {
            Duration.between(now, nextPrayer.plusHours(24)) // Wrap around to next day
        }
    }
    fun refreshPrayerTimes() {
        fetchInitialPrayerTimes()
    }

    companion object {
        fun getInstance(project: Project): MyProjectService = project.service()
    }

}
