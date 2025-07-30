package com.github.asmaaatya.aqimsalat.toolWindow

import com.github.asmaaatya.aqimsalat.core.dialog.FocusModeDialog
import com.github.asmaaatya.aqimsalat.services.MyProjectService
import com.intellij.openapi.CompositeDisposable
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.sun.java.accessibility.util.AWTEventMonitor.addActionListener
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.GridLayout
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JToggleButton
import javax.swing.SwingConstants


class PrayerTimeToolWindowFactory : ToolWindowFactory {
    private val disposables = CompositeDisposable()


    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = JPanel(BorderLayout())
        val service = project.service<MyProjectService>()

        // Main content
        panel.add(PrayerTimeToolWindow(toolWindow).getContent(), BorderLayout.CENTER)


        // Display prayer times with names
//      service.getPrayersForDisplay().forEach { (name, time) ->
//            panel.add(JLabel("$name: ${time.format(DateTimeFormatter.ofPattern("hh:mm a"))}"))
//        }
        // Test controls (only show in development)

            panel.add(createTestControls(service), BorderLayout.SOUTH)


        toolWindow.contentManager.addContent(
            ContentFactory.getInstance().createContent(panel, null, false)
        )
    }



    private fun createTestControls(service: MyProjectService): JPanel {
        return JPanel(GridLayout(0, 1)).apply {
            border = BorderFactory.createTitledBorder("Test Controls")

            add(JButton("Enable Test Mode").apply {
                addActionListener { service.enableTestMode() }
            })

            listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha").forEach { prayer ->
                add(JButton("Test $prayer").apply {
                    addActionListener { service.triggerTestNotification(prayer) }
                })
            }
            }
        }

//    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
//        val mainPanel = JPanel(BorderLayout())
//
//        // 1. Add the main prayer time content
//        val prayerTimeWindow = PrayerTimeToolWindow(toolWindow)
//        val contentPanel = prayerTimeWindow.getContent()
//        mainPanel.add(contentPanel, BorderLayout.CENTER)
//
//        // 2. Add test controls at the bottom
//        addTestControls(mainPanel, project)
//
//        // 3. Create and add the content
//        val content = ContentFactory.getInstance().createContent(
//            mainPanel,  // Use the combined panel
//            null,
//            false
//        )
//        toolWindow.contentManager.addContent(content)
//    }
//
//    private fun addTestControls(mainPanel: JPanel, project: Project) {
//        val testPanel = JPanel(GridLayout(0, 1, 5, 5)).apply {
//            border = BorderFactory.createCompoundBorder(
//                BorderFactory.createTitledBorder("Developer Testing"),
//                BorderFactory.createEmptyBorder(5, 5, 5, 5)
//            )
//        }
//
//        val service = project.service<MyProjectService>()
//
//        // Test Mode Toggle
//        val testModeButton = JToggleButton("Enable Test Mode").apply {
//            toolTipText = "Toggle between real and test prayer times"
//            addActionListener {
//                if (isSelected) {
//                    service.enableTestMode()
//                    JOptionPane.showMessageDialog(
//                        mainPanel,
//                        "Test mode enabled. Using simulated prayer times.",
//                        "Test Mode",
//                        JOptionPane.INFORMATION_MESSAGE
//                    )
//                } else {
//                    service.disableTestMode()
//                }
//            }
//        }
//
//        // Individual Prayer Test Buttons
//        val buttonPanel = JPanel(GridLayout(1, 0, 5, 0))
//        listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha").forEach { prayer ->
//            buttonPanel.add(JButton(prayer).apply {
//                addActionListener {
//                    FocusModeDialog(
//                        prayerName = prayer,
//                        focusMinutes = 1
//                    ).show()
//                }
//            })
//        }
//
//        testPanel.add(JLabel("Test Notifications:"))
//        testPanel.add(buttonPanel)
//        testPanel.add(testModeButton)
//
//        mainPanel.add(testPanel, BorderLayout.SOUTH)
//    }
//}
//
//    private fun addTestControls(panel: JPanel, project: Project) {
//        val testPanel = JPanel(GridLayout(0, 1)).apply {
//            border = BorderFactory.createTitledBorder("Testing Controls")
//        }
//
//        val service = project.service<MyProjectService>()
//
//        // Test Mode Toggle
//        val testModeButton = JToggleButton("Enable Test Mode").apply {
//            addActionListener {
//                if (isSelected) service.enableTestMode()
//                else service.disableTestMode()
//            }
//        }
//
//        // Individual Prayer Test Buttons
//        val prayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
//        prayers.forEach { prayer ->
//            testPanel.add(JButton("Test $prayer Notification").apply {
//                addActionListener {
//                    FocusModeDialog(
//                        prayerName = prayer,
//                        focusMinutes = 1 // Short duration for testing
//                    ).show()
//                }
//            })
//        }
//
//        testPanel.add(testModeButton)
//        panel.add(testPanel, BorderLayout.SOUTH)
//    }

//    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
//        val prayerTimeWindow = PrayerTimeToolWindow(toolWindow)
//        val content = ContentFactory.getInstance().createContent(
//            prayerTimeWindow.getContent(),
//            null,
//            false
//        )
//        toolWindow.contentManager.addContent(content)
//    }

//    override fun shouldBeAvailable(project: Project) = true

    class PrayerTimeToolWindow(toolWindow: ToolWindow) {
        private val service = toolWindow.project.service<MyProjectService>()
        private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

        fun getContent(): JPanel {
            return JBPanel<JBPanel<*>>(BorderLayout()).apply {
                // Main prayer times panel
                val prayerTimesPanel = JBPanel<JBPanel<*>>(FlowLayout(FlowLayout.CENTER, 20, 10)).apply {
                    service.getAllPrayerTimes()?.let { prayerTimes ->
                        add(createPrayerTimeLabel("Fajr", prayerTimes[0]))
                        add(createPrayerTimeLabel("Dhuhr", prayerTimes[1]))
                        add(createPrayerTimeLabel("Asr", prayerTimes[2]))
                        add(createPrayerTimeLabel("Maghrib", prayerTimes[3]))
                        add(createPrayerTimeLabel("Isha", prayerTimes[4]))
                    }
                }

                // Next prayer panel
                val nextPrayerPanel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
                    val nextPrayer = service.getNextPrayerTime()
                    val nextLabel = JBLabel(
                        if (nextPrayer != null) {
                            "Next: ${getPrayerName(nextPrayer)} at ${timeFormatter.format(nextPrayer)}"
                        } else {
                            "Loading prayer times..."
                        },
                        SwingConstants.CENTER
                    )
                    add(nextLabel, BorderLayout.CENTER)
                }

                // Refresh button
                val refreshButton = JButton("Refresh Times").apply {
                    addActionListener {
                        service.refreshPrayerTimes()
                        // Update UI here if needed
                    }
                }

                // Add components to main panel
                add(prayerTimesPanel, BorderLayout.CENTER)
                add(nextPrayerPanel, BorderLayout.NORTH)
                add(refreshButton, BorderLayout.SOUTH)
            }
        }

        private fun createPrayerTimeLabel(prayerName: String, time: LocalTime): JPanel {
            return JBPanel<JBPanel<*>>(BorderLayout()).apply {
                add(JBLabel("<html><b>$prayerName</b></html>", SwingConstants.CENTER), BorderLayout.NORTH)
                add(JBLabel(timeFormatter.format(time), SwingConstants.CENTER), BorderLayout.CENTER)
            }
        }

        private fun getPrayerName(time: LocalTime): String {
            return service.getAllPrayerTimes()?.let { prayerTimes ->
                when (time) {
                    prayerTimes[0] -> "Fajr"
                    prayerTimes[1] -> "Dhuhr"
                    prayerTimes[2] -> "Asr"
                    prayerTimes[3] -> "Maghrib"
                    prayerTimes[4] -> "Isha"
                    else -> "Prayer"
                }
            } ?: "Prayer"
        }
    }
}

//./gradlew runIde