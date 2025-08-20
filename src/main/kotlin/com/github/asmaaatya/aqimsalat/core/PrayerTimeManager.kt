package com.github.asmaaatya.aqimsalat.core

import com.github.asmaaatya.aqimsalat.core.dialog.FocusModeDialog
import com.github.asmaaatya.aqimsalat.services.PrayerService
import com.github.asmaaatya.aqimsalat.setting.SettingsState
import com.github.asmaaatya.aqimsalat.tray.PrayerTray
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import java.time.LocalTime
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

class PrayerTimeManager(
    val project: Project
) {
    private var fetchedPrayerTimes: List<LocalTime> = emptyList()

    init {
//        startTestPrayerCheckLoop()
        startPrayerCheckLoop()
    }

    private fun fetchPrayerTimes(onResult: (List<LocalTime>) -> Unit) {
        val settings = SettingsState.getInstance()
        val city = settings.city
        val country = settings.country
        val method = settings.method

        ApplicationManager.getApplication().executeOnPooledThread {
            showNotification("[AqimAlsalat] Fetching prayer times for $city, $country")
            try {
                val prayerService = ApplicationManager.getApplication().getService(PrayerService::class.java)
                val timings = prayerService.getPrayerTimesAsync(city, country, method).get()
                showNotification("[AqimAlsalat] Prayer times : $timings")

                val timesList = timings?.toLocalTimeList() ?: emptyList()
                fetchedPrayerTimes = timesList

//                SwingUtilities.invokeLater {
//                    JOptionPane.showMessageDialog(
//                        null,
//                        if (timesList.isNotEmpty()) "Fetch success: $timesList" else "Fetch failed, timings empty",
//                        "Aqim Alsalat",
//                        JOptionPane.INFORMATION_MESSAGE
//                    )
////                    PrayerTray.showMessage("Aqim Alsalat", "It’s time for ${getPrayerName(LocalTime.now().withSecond(0).withNano(0))}")
//                }

                onResult(timesList)

            } catch (e: Exception) {
                e.printStackTrace()
                SwingUtilities.invokeLater {
                    JOptionPane.showMessageDialog(
                        null,
                        "Error fetching: ${e.message}",
                        "Aqim Alsalat",
                        JOptionPane.ERROR_MESSAGE
                    )
                }
                onResult(emptyList())
            }
        }
    }



    private fun startTestPrayerCheckLoop() {
        // Set a test prayer time 1 minute from now
        val testTime = LocalTime.now().plusMinutes(1).withSecond(0).withNano(0)
        fetchedPrayerTimes = listOf(testTime)

        showNotification("[TEST] Using prayer time: $testTime")

        val triggered = mutableSetOf<LocalTime>()

        java.util.Timer().scheduleAtFixedRate(object : java.util.TimerTask() {
            override fun run() {
                val now = LocalTime.now().withSecond(0).withNano(0)

                for (prayerTime in fetchedPrayerTimes) {
                    if (now == prayerTime && prayerTime !in triggered) {
                        triggered.add(prayerTime)
                        SwingUtilities.invokeLater {
                            FocusModeDialog(project = project, "Fajr", 1).show()
                        }
                        showNotification("[TEST] Prayer time reached: $prayerTime")
                        val settings = SettingsState.getInstance()

                        if (settings.state.shutdownOnPrayer) {
                            ApplicationManager.getApplication().invokeLater {
                                ApplicationManager.getApplication().exit()
                            }
                        }
                    }
                }
            }
        }, 0, 10_000) // check every 10 seconds for testing
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
                            SwingUtilities.invokeLater {
                                FocusModeDialog(project = project, getPrayerName(
                                    prayerTime
                                ), 15).show()
                            }
                            val settings = SettingsState.getInstance()

                            if (settings.state.shutdownOnPrayer) {
                                ApplicationManager.getApplication().invokeLater {
                                    ApplicationManager.getApplication().exit()
                                }
                            }
                        }
//                        else{
//                        showNotification("[AqimAlsalat]not Prayer time: $prayerTime")
//                        }
                    }

                    // Refresh times at midnight
                    if (now == LocalTime.MIDNIGHT) {
                        triggered.clear()
                        fetchPrayerTimes { updated ->
                            prayerTimes.toMutableList().clear()
                            prayerTimes.toMutableList().addAll(updated)
                        }
                    }
                }
            }, 0, 60_000)
        }
    }


    private fun getPrayerName(time: LocalTime): String {
        return when (time) {
            fetchedPrayerTimes.getOrNull(0) -> "Fajr"
            fetchedPrayerTimes.getOrNull(1) -> "Duhr"
            fetchedPrayerTimes.getOrNull(2) -> "Asr"
            fetchedPrayerTimes.getOrNull(3) -> "Maghrib"
            fetchedPrayerTimes.getOrNull(4) -> "Isha"
            else -> "Prayer"
        }
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
