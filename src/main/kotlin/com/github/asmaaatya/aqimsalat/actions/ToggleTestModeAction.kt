package com.github.asmaaatya.aqimsalat.actions

import com.github.asmaaatya.aqimsalat.services.MyProjectService
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

class TestModeAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val service = project.service<MyProjectService>()

        if (service.testMode) {
            service.disableTestMode()
            showNotification(project, "Test mode disabled", "Using real prayer times")
        } else {
            service.enableTestMode()
            showNotification(project, "Test mode enabled", "Using simulated prayer times")
        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val service = project?.service<MyProjectService>()
        e.presentation.isEnabledAndVisible = project != null

        if (service != null) {
            e.presentation.text = if (service.testMode)
                "Disable Test Mode" else "Enable Test Mode"
        }
    }

    private fun showNotification(project: Project, title: String, message: String) {
        Notifications.Bus.notify(
            Notification(
                "PrayerTimeTest",
                title,
                message,
                NotificationType.INFORMATION
            ),
            project
        )
    }
}