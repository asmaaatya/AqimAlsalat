package com.github.asmaaatya.aqimsalat.setting


import com.intellij.openapi.options.Configurable
import java.awt.GridLayout
import javax.swing.*

class PrayerSettingsConfigurable : Configurable {
    private val settings = PrayerSettingsState.getInstance().state

    private lateinit var cityDropdown: JComboBox<String>
    private lateinit var countryDropdown: JComboBox<String>
    private lateinit var languageDropdown: JComboBox<String>
    private lateinit var autoShutdownCheckbox: JCheckBox
    private lateinit var methodBox: JComboBox<String>

    override fun createComponent(): JComponent {
        val panel = JPanel(GridLayout(0, 2, 10, 10))

        // Country dropdown
        val countries = listOf("Egypt", "Saudi Arabia", "United Arab Emirates", "Morocco", "Turkey")
        countryDropdown = JComboBox(countries.toTypedArray())
        countryDropdown.selectedItem = settings.country

        // City dropdown
        val cities = listOf("Cairo", "Alexandria", "Makkah", "Dubai", "Riyadh", "Casablanca", "Istanbul")
        cityDropdown = JComboBox(cities.toTypedArray())
        cityDropdown.selectedItem = settings.city

        // Language dropdown
        val languages = mapOf("English" to "en", "العربية" to "ar")
        languageDropdown = JComboBox(languages.keys.toTypedArray())
        languageDropdown.selectedItem = languages.entries.firstOrNull { it.value == settings.language }?.key

        // Calculation Method dropdown
        val methods = mapOf("ISNA (2)" to 2, "Egyptian (5)" to 5, "MWL (3)" to 3)
        methodBox = JComboBox(methods.keys.toTypedArray())
        methodBox.selectedItem = methods.entries.firstOrNull { it.value == settings.method }?.key

        // Auto-shutdown toggle
        autoShutdownCheckbox = JCheckBox("Enable Auto Shutdown", settings.autoShutdownEnabled)

        panel.add(JLabel("Country:"))
        panel.add(countryDropdown)

        panel.add(JLabel("City:"))
        panel.add(cityDropdown)

        panel.add(JLabel("Language:"))
        panel.add(languageDropdown)

        panel.add(JLabel("Calculation Method:"))
        panel.add(methodBox)

        panel.add(JLabel("Auto Shutdown:"))
        panel.add(autoShutdownCheckbox)

        return panel
    }

    override fun isModified(): Boolean {
        return settings.city != cityDropdown.selectedItem ||
                settings.country != countryDropdown.selectedItem ||
                settings.language != getSelectedLanguageCode() ||
                settings.method != getSelectedMethodValue() ||
                settings.autoShutdownEnabled != autoShutdownCheckbox.isSelected
    }

    override fun apply() {
        settings.city = cityDropdown.selectedItem as String
        settings.country = countryDropdown.selectedItem as String
        settings.language = getSelectedLanguageCode()
        settings.method = getSelectedMethodValue()
        settings.autoShutdownEnabled = autoShutdownCheckbox.isSelected
    }

    private fun getSelectedLanguageCode(): String {
        val selected = languageDropdown.selectedItem as String
        return if (selected == "العربية") "ar" else "en"
    }

    private fun getSelectedMethodValue(): Int {
        return when (methodBox.selectedItem as String) {
            "ISNA (2)" -> 2
            "Egyptian (5)" -> 5
            "MWL (3)" -> 3
            else -> 5
        }
    }

    override fun getDisplayName(): String = "Aqim As-Salat"
}
