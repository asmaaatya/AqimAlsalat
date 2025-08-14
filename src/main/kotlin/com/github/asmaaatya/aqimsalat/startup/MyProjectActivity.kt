package com.github.asmaaatya.aqimsalat.startup


import com.github.asmaaatya.aqimsalat.core.PrayerTimeManager
import com.github.asmaaatya.aqimsalat.core.dialog.FocusModeDialog
import com.github.asmaaatya.aqimsalat.services.PrayerService
import com.github.asmaaatya.aqimsalat.setting.SettingsState
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import javax.swing.SwingUtilities

class MyProjectActivity : StartupActivity {
    override fun runActivity(project: Project) {
//        val settings = SettingsState.getInstance()
//        val city = settings.city.ifBlank { "London" }
//        val country = settings.country.ifBlank { "GB" }
//        val prayerService =
//            ApplicationManager.getApplication().getService(PrayerService::class.java)
//        prayerService?.getPrayerTimesAsync("London", "GB")?.thenAccept {
//            showNotification("Prayer times: $it")
//        }
        PrayerTimeManager(project)

//        ApplicationManager.getApplication().executeOnPooledThread {
//            try {
//                val settings = SettingsState.getInstance()
//                val city = settings.city.ifBlank { "Cairo" }
//                val country = settings.country.ifBlank { "Egypt" }
//
//                println("[AqimAlsalat] Fetching prayer times for $city, $country")
//
//                // .get() will block until the async call finishes
//                val timings = PrayerService.getPrayerTimesAsync(city, country).get()
//
//                if (timings != null) {
//                    val timesMap = mapOf(
//                        "Fajr" to timings.Fajr,
//                        "Dhuhr" to timings.Dhuhr,
//                        "Asr" to timings.Asr,
//                        "Maghrib" to timings.Maghrib,
//                        "Isha" to timings.Isha
//                    )
//                    val localTimes: List<LocalTime> = timings.toLocalTimeList()
//
//                    showNotification("Prayer times fetched: $localTimes")
//                } else {
//                    showNotification("Failed to fetch prayer times — null result")
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                showNotification("Error fetching prayer times: ${e.message}")
//            }
//        }
    }

    private fun showNotification(message: String) {
        val notification = Notification(
            "AqimAlsalat", // groupId from plugin.xml
            "Aqim Alsalat",
            message,
            NotificationType.INFORMATION
        )
        Notifications.Bus.notify(notification)
    }
}
