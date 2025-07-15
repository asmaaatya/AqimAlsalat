package com.github.asmaaatya.salatlock.startup

import com.github.asmaaatya.salatlock.core.PrayerTimeManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class MyProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        PrayerTimeManager()
    }
}