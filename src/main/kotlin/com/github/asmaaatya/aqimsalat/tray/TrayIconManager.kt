package com.github.asmaaatya.aqimsalat.tray


import com.intellij.openapi.application.ApplicationManager
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import javax.imageio.ImageIO
import javax.swing.JOptionPane


object PrayerTray {
    private var trayIcon: TrayIcon? = null

    fun init() {
        if (!SystemTray.isSupported()) return

        val popup = PopupMenu()

        val settingsItem = MenuItem("Settings")
        settingsItem.addActionListener {
            JOptionPane.showMessageDialog(null, "Open Settings from IDE Settings > Tools > Aqim Alsalat")
        }
        popup.add(settingsItem)

        val exitItem = MenuItem("Exit Aqim Alsalat")
        exitItem.addActionListener {
            SystemTray.getSystemTray().remove(trayIcon)
        }
        popup.add(exitItem)

        val image = Toolkit.getDefaultToolkit().getImage(
            PrayerTray::class.java.getResource("/icons/tray_icon.png")
        )
        trayIcon = TrayIcon(image, "Aqim Alsalat", popup)
        trayIcon!!.isImageAutoSize = true

        SystemTray.getSystemTray().add(trayIcon)
    }

    fun showMessage(title: String, message: String) {
        trayIcon?.displayMessage(title, message, TrayIcon.MessageType.INFO)
    }
}

