package com.github.asmaaatya.aqimsalat

import com.github.asmaaatya.aqimsalat.services.MyProjectService
import com.intellij.openapi.components.service
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.time.LocalTime

class MyPluginTest : BasePlatformTestCase() {
    private lateinit var service: MyProjectService

    override fun setUp() {
        super.setUp()
        service = project.service<MyProjectService>()
    }

    fun testPrayerTimeFetching() {
        // Test that prayer times are loaded
        assertTrue(service.getAllPrayerTimes().isNotEmpty())
    }

    fun testNextPrayerTimeCalculation() {
        val nextPrayer = service.getNextPrayerTime()
        assertNotNull(nextPrayer)
        assertTrue(nextPrayer is LocalTime)
    }

    fun testTestModeToggle() {
        // Verify test mode works
        assertFalse(service.testMode)
        service.enableTestMode()
        assertTrue(service.testMode)

        val testTimes = service.getPrayersForDisplay()
        assertEquals(5, testTimes.size)
    }
}