package com.github.asmaaatya.aqimsalat.reminder
import com.github.asmaaatya.aqimsalat.services.PrayerService
import com.github.asmaaatya.aqimsalat.setting.SettingsState
import com.github.asmaaatya.aqimsalat.tray.PrayerTray
import com.intellij.openapi.application.ApplicationManager
import kotlinx.coroutines.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object PrayerReminder {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

//    fun start() {
//        scope.launch {
//            val settings = SettingsState.getInstance()
//            val timings = PrayerService.getPrayerTimes(settings.city, settings.country, settings.method)
//            if (timings == null) return@launch
//
//            val prayerTimes = listOf(timings.Fajr, timings.Dhuhr, timings.Asr, timings.Maghrib, timings.Isha)
//            while (isActive) {
//                val now = LocalTime.now().format(formatter)
//                if (prayerTimes.contains(now)) {
//                    PrayerTray.showMessage("Prayer Time", "It's time for prayer ($now)")
//
//                    if (settings.shutdownOnPrayer) {
//                        ApplicationManager.getApplication().exit()
//                    }
//                }
//                delay(60_000) // check every minute
//            }
//        }
//    }
}
