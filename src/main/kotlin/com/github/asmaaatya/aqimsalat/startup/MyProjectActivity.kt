package com.github.asmaaatya.aqimsalat.startup

import com.github.asmaaatya.aqimsalat.core.PrayerTimeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.ui.Messages

class MyProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        Messages.showInfoMessage(
            project,
            "Aqim As-Salat plugin started!",
            "Startup Confirmation"
        )

        println("✅ Aqim As-Salat ProjectActivity executed for project: ${project.name}")
    }
    }
