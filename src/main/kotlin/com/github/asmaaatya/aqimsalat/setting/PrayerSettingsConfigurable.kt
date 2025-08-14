package com.github.asmaaatya.aqimsalat.setting

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import java.awt.GridLayout
import javax.swing.*

class SettingsConfigurable : Configurable {
    private val cityField = JBTextField()
    private val countryField = JBTextField()
    private val methodField = JBTextField()
    private val shutdownCheckbox = JBCheckBox("Shutdown on prayer time")

    override fun getDisplayName(): String = "Aqim Alsalat Settings"

    override fun createComponent(): JComponent {
        val panel = JPanel(GridLayout(4, 2))
        panel.add(JLabel("City:"))
        panel.add(cityField)
        panel.add(JLabel("Country:"))
        panel.add(countryField)
        panel.add(JLabel("Method:"))
        panel.add(methodField)
        panel.add(shutdownCheckbox)
        return panel
    }

    override fun isModified(): Boolean {
        val settings = SettingsState.getInstance()
        return cityField.text != settings.city ||
                countryField.text != settings.country ||
                methodField.text.toIntOrNull() != settings.method ||
                shutdownCheckbox.isSelected != settings.shutdownOnPrayer
    }

    override fun apply() {
        val settings = SettingsState.getInstance()
        settings.city = cityField.text
        settings.country = countryField.text
        settings.method = methodField.text.toIntOrNull() ?: 5
        settings.shutdownOnPrayer = shutdownCheckbox.isSelected
    }

    override fun reset() {
        val settings = SettingsState.getInstance()
        cityField.text = settings.city
        countryField.text = settings.country
        methodField.text = settings.method.toString()
        shutdownCheckbox.isSelected = settings.shutdownOnPrayer
    }
}

