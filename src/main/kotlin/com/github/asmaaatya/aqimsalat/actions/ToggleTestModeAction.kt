package com.github.asmaaatya.aqimsalat.actions

import com.github.asmaaatya.aqimsalat.services.MyProjectService
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class TestModeAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val service = project.service<MyProjectService>()
        val times = service.getPrayersForDisplay().joinToString("\n") {
            "${it.first}: ${it.second}"
        }
        Messages.showInfoMessage(project, times, "Prayer Times")
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val service = project?.service<MyProjectService>()
        e.presentation.isEnabledAndVisible = project != null

        if (service != null) {
            e.presentation.text = if (service.testMode)
                "Disable Test Mode" else "Enable Test Mode"
            e.presentation.description = if (service.testMode)
                "Switch back to real prayer times" else "Enable test prayer times"
        }
    }

    private fun showNotification(project: Project, title: String, message: String) {
        Notifications.Bus.notify(
            Notification(
                "PrayerTimeTest",  // Must match the ID in plugin.xml
                title,
                message,
                NotificationType.INFORMATION
            ),
            project
        )
    }


}