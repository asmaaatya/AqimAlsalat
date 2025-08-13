package com.github.asmaaatya.aqimsalat.startup

import com.github.asmaaatya.aqimsalat.services.PrayerService
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import javax.swing.JOptionPane
import kotlin.concurrent.thread

class MyProjectActivity: StartupActivity {
    override fun runActivity(project: Project) {
//        PrayerTray.init()
//        PrayerReminder.start()
        thread {
            val timings = PrayerService.getPrayerTimes("Cairo", "Egypt")
            val message = timings?.let {
                """
                Fajr: ${it.Fajr}
                Dhuhr: ${it.Dhuhr}
                Asr: ${it.Asr}
                Maghrib: ${it.Maghrib}
                Isha: ${it.Isha}
                """.trimIndent()
            } ?: "Could not fetch prayer times."

            JOptionPane.showMessageDialog(
                null,
                message,
                "Aqim Alsalat",
                JOptionPane.INFORMATION_MESSAGE
            )
        }
    }
}