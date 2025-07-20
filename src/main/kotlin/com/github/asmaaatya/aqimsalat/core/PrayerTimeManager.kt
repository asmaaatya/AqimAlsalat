package com.github.asmaaatya.aqimsalat.core

import com.github.asmaaatya.aqimsalat.api.PrayerApiClient
import com.github.asmaaatya.aqimsalat.api.PrayerTimesResponse
import com.github.asmaaatya.aqimsalat.core.dialog.FocusModeDialog
import com.github.asmaaatya.aqimsalat.tray.TrayIconManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class PrayerTimeManager {
    private var fetchedPrayerTimes: List<LocalTime> = emptyList()

    init {
        TrayIconManager.setupTrayIcon()
        startPrayerCheckLoop()
    }

    private fun fetchPrayerTimes(
        city: String = "Cairo",
        country: String = "Egypt",
        callback: (List<LocalTime>) -> Unit
    ) {
        val call = PrayerApiClient.service.getPrayerTimes(city, country)

        call.enqueue(object : Callback<PrayerTimesResponse> {
            override fun onResponse(call: Call<PrayerTimesResponse>, response: Response<PrayerTimesResponse>) {
                val times = response.body()?.data?.timings
                if (times != null) {
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
                }
            }

            override fun onFailure(call: Call<PrayerTimesResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun startPrayerCheckLoop() {
        fetchPrayerTimes { prayerTimes ->
            val triggered = mutableSetOf<LocalTime>()


            java.util.Timer().scheduleAtFixedRate(object : java.util.TimerTask() {
                override fun run() {
                    val now = LocalTime.now().withSecond(0).withNano(0)

                    for (prayerTime in prayerTimes) {
                        if (now == prayerTime && prayerTime !in triggered) {
                            triggered.add(prayerTime)

                            javax.swing.SwingUtilities.invokeLater {
                              FocusModeDialog(
                                    prayerName = getPrayerName(prayerTime),
                                    focusMinutes = 15
                                ).showAndGet()
                            }
                        }
                    }

                    if (now == LocalTime.MIDNIGHT) {
                        triggered.clear()
                        fetchPrayerTimes { updated -> prayerTimes.toMutableList().clear(); prayerTimes.toMutableList().addAll(updated) }
                    }
                }
            }, 0, 60_000)
        }
    }

    private fun getPrayerName(time: LocalTime): String {
        return when (time) {
            fetchedPrayerTimes[0] -> "Fajr"
            fetchedPrayerTimes[1] -> "Dhuhr"
            fetchedPrayerTimes[2] -> "Asr"
            fetchedPrayerTimes[3] -> "Maghrib"
            fetchedPrayerTimes[4] -> "Isha"
            else -> "Prayer"
        }
    }

}