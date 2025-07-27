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
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI

class FocusModeDialog(
    private val prayerName: String,
    private val focusMinutes: Int = 15
) : DialogWrapper(true) {

    private val countdownLabel = JLabel().apply {
        horizontalAlignment = SwingConstants.CENTER
        font = Font("Monospaced", Font.BOLD, 24)
        foreground = JBColor(0x2E7D32, 0x81C784) // Green color scheme
    }

    private var remainingSeconds = focusMinutes * 60
    private var timer: Timer? = null
    private var audioClip: Clip? = null

    init {
        title = "🕌 $prayerName Prayer Time"
        isModal = true
        init()
        startCountdown()
        playAdhanSoundIfEnabled()
    }

    override fun createCenterPanel(): JComponent {
        return JPanel(BorderLayout(10, 10)).apply {
            preferredSize = Dimension(400, 180)
            border = JBUI.Borders.empty(20)

            val message = JLabel(
                "<html><div style='text-align: center;'>" +
                        "It's time for <b style='color:#2E7D32;'>$prayerName</b> prayer<br>" +
                        "Focus mode will end in:</div></html>",
                SwingConstants.CENTER
            ).apply {
                font = Font("Dialog", Font.PLAIN, 16)
            }

            add(message, BorderLayout.NORTH)
            add(countdownLabel, BorderLayout.CENTER)

            // Add prayer icon
            add(JLabel(ImageIcon(javaClass.getResource("/icons/prayer.png"))).apply {
                horizontalAlignment = SwingConstants.CENTER
            }, BorderLayout.SOUTH)
        }
    }

    override fun createActions(): Array<Action> {
        return arrayOf(
            object : DialogWrapperAction("I've Prayed 🙏") {
                override fun doAction(e: java.awt.event.ActionEvent?) {
                    cleanupResources()
                    close(OK_EXIT_CODE)
                }
            },
            object : DialogWrapperAction("Snooze (5 min)") {
                override fun doAction(e: java.awt.event.ActionEvent?) {
                    remainingSeconds = 5 * 60
                    updateCountdownLabel()
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
                if (remainingSeconds <= 0) {
                    cleanupResources()
                    SwingUtilities.invokeLater { close(OK_EXIT_CODE) }
                }
            }
        }
    }

    private fun playAdhanSoundIfEnabled() {
        if (PrayerSettingsState.getInstance().state.playSound) {
            Thread {
                try {
                    val audioStream = BufferedInputStream(
                        javaClass.getResourceAsStream("/sounds/adhan.wav")
                            ?: return@Thread
                    )
                    val audioIn = AudioSystem.getAudioInputStream(audioStream)
                    audioClip = AudioSystem.getClip().apply {
                        open(audioIn)
                        start()
                    }
                } catch (e: Exception) {
                    println("Error playing adhan: ${e.message}")
                }
            }.start()
        }
    }

    private fun updateCountdownLabel() {
        val mins = remainingSeconds / 60
        val secs = remainingSeconds % 60
        countdownLabel.text = String.format("%02d:%02d", mins, secs)

        // Visual feedback when time is running out
        when (remainingSeconds) {
            in 0..30 -> countdownLabel.foreground = JBColor.RED
            in 31..60 -> countdownLabel.foreground = JBColor.ORANGE
        }
    }

    private fun cleanupResources() {
        timer?.cancel()
        audioClip?.stop()
        audioClip?.close()
    }

    override fun dispose() {
        cleanupResources()
        super.dispose()
    }
}
