package com.github.asmaaatya.aqimsalat.setting

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBCheckBox
import java.awt.GridLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel


class SettingsConfigurable : Configurable {
    private val cityComboBox = ComboBox( arrayOf("Cairo", "Makkah", "Madinah", "Riyadh", "Jeddah","Dubai", "Abu Dhabi", "Sharjah"))
    private val countryComboBox = ComboBox(arrayOf("EG", "SA", "AE", "JO", "KW", "QA", "OM", "DZ", "MA", "TN"))

    private val methodComboBox = ComboBox(  arrayOf(
        "Egyptian (5)",
        "Umm Al-Qura (4)",
        "Gulf Region (8)",
        "Dubai (11)",
        "Kuwait (9)",
        "Qatar (10)",
        "Morocco (18)",
        "Tunisia (19)",
        "Algeria (18-alg)",
        "Shia Ithna-Ashari (12)"
    ))
    private val shutdownCheckbox = JBCheckBox("Shutdown on prayer time")
    private val soundCheckbox = JBCheckBox("Play Adhan and notifications sound")

    override fun getDisplayName(): String = "Aqim Alsalat"

    override fun createComponent(): JComponent {
        val panel = JPanel(GridLayout(5, 2, 8, 8))
        panel.add(JLabel("City:")); panel.add(cityComboBox)
        panel.add(JLabel("Country:")); panel.add(countryComboBox)
        panel.add(JLabel("Method:")); panel.add(methodComboBox)
        panel.add(shutdownCheckbox); panel.add(soundCheckbox)
        return panel
    }

    override fun isModified(): Boolean {
        val s = SettingsState.getInstance()
        return (cityComboBox.selectedItem as? String ?: "") != s.city ||
                (countryComboBox.selectedItem as? String ?: "") != s.country ||
                methodComboBox.selectedIndex != s.method ||
                shutdownCheckbox.isSelected != s.shutdownOnPrayer ||
                soundCheckbox.isSelected != s.playSound
    }

    override fun apply() {
        val s = SettingsState.getInstance()
        s.city = cityComboBox.selectedItem as? String ?: ""
        s.country = countryComboBox.selectedItem as? String ?: ""
        s.method = methodComboBox.selectedIndex
        s.shutdownOnPrayer = shutdownCheckbox.isSelected
        s.playSound = soundCheckbox.isSelected
    }

    override fun reset() {
        val s = SettingsState.getInstance()
        cityComboBox.selectedItem = s.city
        countryComboBox.selectedItem = s.country
        methodComboBox.selectedIndex = s.method
        shutdownCheckbox.isSelected = s.shutdownOnPrayer
        soundCheckbox.isSelected = s.playSound
    }
}




