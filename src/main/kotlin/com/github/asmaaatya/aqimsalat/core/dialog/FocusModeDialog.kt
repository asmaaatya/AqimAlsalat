package com.github.asmaaatya.aqimsalat.core.dialog


import com.github.asmaaatya.aqimsalat.setting.PrayerSettingsState
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import java.io.BufferedInputStream
import java.util.Timer
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.swing.*
import kotlin.concurrent.fixedRateTimer

class FocusModeDialog(
    private val prayerName: String,
    private val focusMinutes: Int = 15
) : DialogWrapper(true) {

    private val countdownLabel = JLabel()
    private var remainingSeconds = focusMinutes * 60

    private lateinit var timer: Timer

    init {
        title = "Prayer Time – $prayerName"
        isModal = true
        init()
        startCountdown()
        if (PrayerSettingsState.getInstance().state.playSound) {
            playAdhanSound()
        }
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout(10, 10)).apply {
            preferredSize = Dimension(400, 150)
        }

        val message = JLabel(
            "<html><center>It’s time for <b>$prayerName</b> prayer.<br/>" +
                    "Focus Mode will end in:</center></html>",
            SwingConstants.CENTER
        )
        message.font = Font("Dialog", Font.PLAIN, 16)

        countdownLabel.horizontalAlignment = SwingConstants.CENTER
        countdownLabel.font = Font("Monospaced", Font.BOLD, 24)

        panel.add(message, BorderLayout.NORTH)
        panel.add(countdownLabel, BorderLayout.CENTER)

        return panel
    }

    override fun createActions(): Array<Action> {
        return arrayOf(
            object : DialogWrapperAction("I Already Prayed") {
                override fun doAction(e: java.awt.event.ActionEvent?) {
                    stopTimer()
                    close(OK_EXIT_CODE)
                }
            }
        )
    }

    private fun startCountdown() {
        updateCountdownLabel()
        timer = fixedRateTimer(name = "focus-countdown", initialDelay = 1000, period = 1000) {
            remainingSeconds--
            SwingUtilities.invokeLater {
                updateCountdownLabel()
            }
            if (remainingSeconds <= 0) {
                stopTimer()
                SwingUtilities.invokeLater {
                    close(OK_EXIT_CODE)
                }
            }
        }
    }

    private fun stopTimer() {
        timer.cancel()
    }

    private fun updateCountdownLabel() {
        val mins = remainingSeconds / 60
        val secs = remainingSeconds % 60
        countdownLabel.text = String.format("%02d:%02d", mins, secs)
    }
    private fun playAdhanSound() {
        try {
            val audioInputStream = AudioSystem.getAudioInputStream(
                BufferedInputStream(javaClass.getResourceAsStream("/main_sound/adhan.mp3"))
            )
            val clip: Clip = AudioSystem.getClip()
            clip.open(audioInputStream)
            clip.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
