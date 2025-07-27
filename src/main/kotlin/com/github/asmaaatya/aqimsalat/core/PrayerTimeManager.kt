package com.github.asmaaatya.aqimsalat.core

import com.github.asmaaatya.aqimsalat.api.PrayerApiClient
import com.github.asmaaatya.aqimsalat.api.PrayerTimesResponse
import com.github.asmaaatya.aqimsalat.core.dialog.FocusModeDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Collections
import java.util.TimerTask
import javax.swing.SwingUtilities


class PrayerTimeManager {
    @Volatile
    private var fetchedPrayerTimes: List<LocalTime> = emptyList()
    private val triggered = Collections.synchronizedSet(mutableSetOf<LocalTime>())
    private val timer = java.util.Timer()

    init {
//        TrayIconManager.setupTrayIcon()
        startPrayerCheckLoop()
    }

    private fun fetchPrayerTimes(
        city: String = "Cairo",
        country: String = "Egypt",
        callback: (List<LocalTime>) -> Unit
    ) {
        PrayerApiClient.service.getPrayerTimes(city, country).enqueue(
            object : Callback<PrayerTimesResponse> {
                override fun onResponse(
                    call: Call<PrayerTimesResponse>,
                    response: Response<PrayerTimesResponse>
                ) {
                    if (response.isSuccessful) {
                        val times = response.body()?.data?.timings
                        if (times != null) {
                            try {
                                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                                val result = listOf(
                                    LocalTime.parse(times.fajr, formatter),
                                    LocalTime.parse(times.dhuhr, formatter),
                                    LocalTime.parse(times.asr, formatter),
                                    LocalTime.parse(times.maghrib, formatter),
                                    LocalTime.parse(times.isha, formatter)
                                )
                                fetchedPrayerTimes = result
                                callback(result)
                            } catch (e: Exception) {
                                showErrorNotification("Failed to parse prayer times: ${e.message}")
                            }
                        }
                    } else {
                        showErrorNotification("Failed to fetch prayer times: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<PrayerTimesResponse>, t: Throwable) {
                    showErrorNotification("Network error: ${t.message}")
                }
            })
    }

    private fun startPrayerCheckLoop() {
        fetchPrayerTimes { prayerTimes ->
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    try {
                        val now = LocalTime.now().withSecond(0).withNano(0)

                        // Check each prayer time
                        prayerTimes.forEach { prayerTime ->
                            if (now == prayerTime && !triggered.contains(prayerTime)) {
                                triggered.add(prayerTime)
                                showPrayerNotification(prayerTime)
                            }
                        }

                        // Reset at midnight
                        if (now == LocalTime.MIDNIGHT) {
                            triggered.clear()
                            fetchPrayerTimes { updatedTimes ->
                                fetchedPrayerTimes = updatedTimes
                            }
                        }
                    } catch (e: Exception) {
                        showErrorNotification("Error in prayer check loop: ${e.message}")
                    }
                }
            }, 0, 60_000)
        }
    }

    private fun showPrayerNotification(prayerTime: LocalTime) {
        SwingUtilities.invokeLater {
            FocusModeDialog(
                prayerName = getPrayerName(prayerTime),
                focusMinutes = 15
            ).showAndGet()
        }
    }

    private fun showErrorNotification(message: String) {
        SwingUtilities.invokeLater {
            // Implement your error notification here
            println("Error: $message")
        }
    }

    private fun getPrayerName(time: LocalTime): String {
        return when {
            fetchedPrayerTimes.size > 4 -> when (time) {
                fetchedPrayerTimes[0] -> "Fajr"
                fetchedPrayerTimes[1] -> "Dhuhr"
                fetchedPrayerTimes[2] -> "Asr"
                fetchedPrayerTimes[3] -> "Maghrib"
                fetchedPrayerTimes[4] -> "Isha"
                else -> "Prayer"
            }
            else -> "Prayer"
        }
    }

    fun dispose() {
        timer.cancel()
    }
}