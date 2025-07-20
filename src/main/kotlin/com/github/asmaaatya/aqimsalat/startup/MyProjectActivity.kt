package com.github.asmaaatya.aqimsalat.startup

import com.github.asmaaatya.aqimsalat.core.PrayerTimeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class MyProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        PrayerTimeManager()
    }
}