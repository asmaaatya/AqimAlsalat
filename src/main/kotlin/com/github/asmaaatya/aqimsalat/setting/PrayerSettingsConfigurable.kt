package com.github.asmaaatya.aqimsalat.setting


import com.intellij.openapi.options.Configurable
import javax.swing.*
class SettingsConfigurable : Configurable {
        private val cityField = JTextField()
        private val countryField = JTextField()
        private val methodSpinner = JSpinner(SpinnerNumberModel(5, 0, 15, 1))
        private val shutdownCheck = JCheckBox("Shutdown IDE at prayer time")

        override fun getDisplayName(): String = "Aqim Alsalat"

        override fun createComponent(): JPanel {
            val panel = JPanel()
            panel.layout = java.awt.GridLayout(0, 2)
            panel.add(JLabel("City:"))
            panel.add(cityField)
            panel.add(JLabel("Country:"))
            panel.add(countryField)
            panel.add(JLabel("Method:"))
            panel.add(methodSpinner)
            panel.add(shutdownCheck)
            return panel
        }

        override fun isModified(): Boolean {
            val settings = SettingsState.getInstance()
            return cityField.text != settings.city ||
                    countryField.text != settings.country ||
                    methodSpinner.value != settings.method ||
                    shutdownCheck.isSelected != settings.shutdownOnPrayer
        }

        override fun apply() {
            val settings = SettingsState.getInstance()
            settings.city = cityField.text
            settings.country = countryField.text
            settings.method = methodSpinner.value as Int
            settings.shutdownOnPrayer = shutdownCheck.isSelected
        }

        override fun reset() {
            val settings = SettingsState.getInstance()
            cityField.text = settings.city
            countryField.text = settings.country
            methodSpinner.value = settings.method
            shutdownCheck.isSelected = settings.shutdownOnPrayer
        }
    }


