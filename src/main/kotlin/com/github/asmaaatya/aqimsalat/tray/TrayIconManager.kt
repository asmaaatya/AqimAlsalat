package com.github.asmaaatya.aqimsalat.tray


import com.intellij.openapi.application.ApplicationManager
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon
import javax.imageio.ImageIO
import javax.swing.JOptionPane

object TrayIconManager {

    private var trayIcon: TrayIcon? = null

    fun setupTrayIcon() {
        if (!SystemTray.isSupported()) return

        if (trayIcon != null) return

        val tray = SystemTray.getSystemTray()
        val image = ImageIO.read(TrayIconManager::class.java.getResource("/icons/icon.png"))

        val popup = PopupMenu()

        val exitItem = MenuItem("Exit Android Studio").apply {
            addActionListener {
                ApplicationManager.getApplication().exit()
            }
        }

        val infoItem = MenuItem("Next Prayer Info").apply {
            addActionListener {
                JOptionPane.showMessageDialog(null, "Prayer info will appear here.")
            }
        }

        popup.add(infoItem)
        popup.add(exitItem)

        trayIcon = TrayIcon(image, "Aqim As-Salat", popup).apply {
            isImageAutoSize = true
            toolTip = "Aqim As-Salat - Prayer Time Manager"
        }

        try {
            tray.add(trayIcon)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showBalloon(title: String, message: String) {
        trayIcon?.displayMessage(title, message, TrayIcon.MessageType.INFO)
    }
}
