package com.github.asmaaatya.aqimsalat.core.dialog

import com.github.asmaaatya.aqimsalat.lang.MyBundle
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.swing.*
import kotlin.concurrent.fixedRateTimer

class FocusModeDialog(
    project: Project? = null,
    private val prayerName: String,
    private val focusMinutes: Int = 15,
    private val playSound: Boolean = true
) : DialogWrapper(project, true) {

    private val countdownLabel = JLabel()
    private var remainingSeconds = focusMinutes * 60
    private var timer: java.util.Timer? = null

    init {
        title = "Prayer Time – $prayerName"
        isModal = true
        init() // IMPORTANT: must call init()
        startCountdown()
        if (playSound) {
            playAdhanSound()
        }
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout(10, 10))
        panel.preferredSize = Dimension(400, 150)

        val message = JLabel(
            "<html><center>حان وقت صلاة <b>$prayerName</b><br/>" +
                    "سينتهي التذكير بعد :</center></html>",
            SwingConstants.CENTER
        )
        message.font = Font("Dialog", Font.PLAIN, 16)

        countdownLabel.font = Font("Monospaced", Font.BOLD, 24)
        countdownLabel.horizontalAlignment = SwingConstants.CENTER
        updateCountdownLabel()

        panel.add(message, BorderLayout.NORTH)
        panel.add(countdownLabel, BorderLayout.CENTER)

        return panel
    }

    override fun createActions(): Array<Action> {
        return arrayOf(object : DialogWrapperAction("أنهيت الصلاة") {
            override fun doAction(e: java.awt.event.ActionEvent?) {
                stopTimer()
                close(OK_EXIT_CODE)
            }
        })
    }

    private fun startCountdown() {
        timer = fixedRateTimer(name = "focus-countdown", initialDelay = 1000, period = 1000) {
            remainingSeconds--
            SwingUtilities.invokeLater { updateCountdownLabel() }

            if (remainingSeconds <= 0) {
                stopTimer()
                SwingUtilities.invokeLater { close(OK_EXIT_CODE) }
            }
        }
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    private fun updateCountdownLabel() {
        val mins = remainingSeconds / 60
        val secs = remainingSeconds % 60
        countdownLabel.text = String.format("%02d:%02d", mins, secs)
    }

    private fun playAdhanSound() {
        try {
            val audioInputStream = AudioSystem.getAudioInputStream(
                BufferedInputStream(javaClass.getResourceAsStream("/sounds/adhan_sound.wav"))
            )
            val clip: Clip = AudioSystem.getClip()
            clip.open(audioInputStream)
            clip.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        /** Show dialog safely on the EDT */
        fun show(project: Project? = null, prayerName: String, focusMinutes: Int = 15) {
            SwingUtilities.invokeLater {
                FocusModeDialog(project, prayerName, focusMinutes).showAndGet()
            }
        }
    }
}
