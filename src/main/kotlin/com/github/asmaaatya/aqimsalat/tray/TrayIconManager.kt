package com.github.asmaaatya.aqimsalat.tray


import com.github.asmaaatya.aqimsalat.services.MyProjectService
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.thisLogger
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.ActionListener
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO
import javax.swing.JOptionPane


object TrayIconManager {
    private val logger = thisLogger()
    private var trayIcon: TrayIcon? = null
    private var prayerService: MyProjectService? = null

    fun setupTrayIcon(prayerService: MyProjectService) {
        if (!SystemTray.isSupported()) {
            logger.warn("System tray is not supported")
            return
        }

        if (trayIcon != null) {
            logger.info("Tray icon already initialized")
            return
        }

        this.prayerService = prayerService

        try {
            val image = ImageIO.read(javaClass.getResourceAsStream("/icons/tray_icon.png"))
                ?: throw IllegalStateException("Tray icon image not found")

            val popup = createPopupMenu()
            val tray = SystemTray.getSystemTray()

            trayIcon = TrayIcon(image, "Aqim As-Salat", popup).apply {
                isImageAutoSize = true
                toolTip = "Aqim As-Salat - Prayer Time Manager"
                addActionListener(createTrayClickListener())
            }

            tray.add(trayIcon)
            logger.info("Tray icon successfully initialized")
        } catch (e: Exception) {
            logger.error("Failed to initialize tray icon", e)
        }
    }
//./gradlew runIde
    private fun createPopupMenu(): PopupMenu {
        return PopupMenu().apply {
            add(createNextPrayerItem())
            add(createSettingsItem())
            addSeparator()
            add(createExitItem())
        }
    }

    private fun createNextPrayerItem(): MenuItem {
        return MenuItem("Next Prayer").apply {
            addActionListener {
                prayerService?.getNextPrayerTime()?.let { nextPrayer ->
//                    val prayerName = prayerService?.getPrayerName(nextPrayer) ?: "Prayer"
                    val time = nextPrayer.format(DateTimeFormatter.ofPattern("hh:mm a"))
                    showBalloon("Next Prayer", "ppat $time")
                } ?: showBalloon("Prayer Times", "No prayer times available")
            }
        }
    }

    private fun createSettingsItem(): MenuItem {
        return MenuItem("Settings").apply {
            addActionListener {
                // TODO: Open settings dialog
                JOptionPane.showMessageDialog(
                    null,
                    "Plugin settings will open here",
                    "Aqim As-Salat Settings",
                    JOptionPane.INFORMATION_MESSAGE
                )
            }
        }
    }

    private fun createExitItem(): MenuItem {
        return MenuItem("Exit").apply {
            addActionListener {
                ApplicationManager.getApplication().exit()
            }
        }
    }

    private fun createTrayClickListener(): ActionListener {
        return ActionListener {
            prayerService?.getNextPrayerTime()?.let { nextPrayer ->
//                val prayerName = prayerService?.getPrayerName(nextPrayer) ?: "Prayer"
                val time = nextPrayer.format(DateTimeFormatter.ofPattern("hh:mm a"))
                showBalloon("Next Prayer", "[pp at $time")
            }
        }
    }

    fun showBalloon(title: String, message: String) {
        if (trayIcon == null) {
            logger.warn("Tray icon not initialized when showing balloon")
            return
        }

        try {
            trayIcon?.displayMessage(
                title,
                message,
                TrayIcon.MessageType.INFO
            )
            logger.info("Showed tray notification: $title - $message")
        } catch (e: Exception) {
            logger.error("Failed to show tray notification", e)
        }
    }

    fun dispose() {
        try {
            SystemTray.getSystemTray().remove(trayIcon)
            trayIcon = null
            logger.info("Tray icon disposed successfully")
        } catch (e: Exception) {
            logger.error("Failed to dispose tray icon", e)
        }
    }
}
