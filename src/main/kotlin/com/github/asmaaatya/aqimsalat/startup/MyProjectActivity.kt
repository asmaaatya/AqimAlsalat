package com.github.asmaaatya.aqimsalat.startup

import com.github.asmaaatya.aqimsalat.core.PrayerTimeManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.ui.Messages

class MyProjectActivity : StartupActivity {
    private val logger = Logger.getInstance(MyProjectActivity::class.java)
    override fun runActivity(project: Project) {
        println("🚀 DebugStartup ran")
        Messages.showMessageDialog(
            project,
            "Plugin Loaded",
            "Hello",
            Messages.getInformationIcon()
        )
        logger.info("✅ Aqim As-Salat plugin started for: ${project.name}")
    }
}
