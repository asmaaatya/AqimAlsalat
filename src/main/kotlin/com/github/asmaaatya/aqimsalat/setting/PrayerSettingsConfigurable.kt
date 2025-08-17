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
    private val soundCheckbox = JBCheckBox("Play Adhan sound")

    override fun getDisplayName(): String = "Aqim Alsalat"

    override fun createComponent(): JComponent {
        val panel = JPanel(GridLayout(5, 2, 8, 8))
        panel.add(JLabel("City:")); panel.add(cityField)
        panel.add(JLabel("Country:")); panel.add(countryField)
        panel.add(JLabel("Method:")); panel.add(methodField)
        panel.add(shutdownCheckbox); panel.add(soundCheckbox)
        return panel
    }

    override fun isModified(): Boolean {
        val s = SettingsState.getInstance()
        return cityField.text != s.city ||
                countryField.text != s.country ||
                methodField.text.toIntOrNull() != s.method ||
                shutdownCheckbox.isSelected != s.shutdownOnPrayer ||
                soundCheckbox.isSelected != s.playSound
    }

    override fun apply() {
        val s = SettingsState.getInstance()
        s.city = cityField.text
        s.country = countryField.text
        s.method = methodField.text.toIntOrNull() ?: 5
        s.shutdownOnPrayer = shutdownCheckbox.isSelected
        s.playSound = soundCheckbox.isSelected
    }

    override fun reset() {
        val s = SettingsState.getInstance()
        cityField.text = s.city
        countryField.text = s.country
        methodField.text = s.method.toString()
        shutdownCheckbox.isSelected = s.shutdownOnPrayer
        soundCheckbox.isSelected = s.playSound
    }
}



