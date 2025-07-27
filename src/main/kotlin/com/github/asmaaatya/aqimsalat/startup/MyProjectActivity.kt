package com.github.asmaaatya.aqimsalat.startup

import com.github.asmaaatya.aqimsalat.core.PrayerTimeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.startup.StartupActivity

class MyProjectActivity : StartupActivity {
    override fun runActivity(project: Project) {
        PrayerTimeManager()
    }
}