package com.github.asmaaatya.aqimsalat.startup


import com.github.asmaaatya.aqimsalat.core.PrayerTimeManager
import com.github.asmaaatya.aqimsalat.setting.SettingsState
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

class MyProjectActivity : StartupActivity {
    override fun runActivity(project: Project) {
        PrayerTimeManager(project)
        if (SettingsState.getInstance().state.playSound) {
            try {
                val audioInputStream = AudioSystem.getAudioInputStream(
                    BufferedInputStream(javaClass.getResourceAsStream("/sounds/main_sound.wav"))
                )
                val clip: Clip = AudioSystem.getClip()
                clip.open(audioInputStream)
                clip.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
