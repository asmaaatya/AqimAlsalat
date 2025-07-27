package com.github.asmaaatya.aqimsalat.setting


import com.intellij.openapi.options.Configurable
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.*


class PrayerSettingsConfigurable : Configurable, Configurable.NoScroll {
    private val settings = PrayerSettingsState.getInstance().state
    private var initialized = false

    // UI Components
    private val cityDropdown = JComboBox<String>()
    private val countryDropdown = JComboBox<String>(DEFAULT_COUNTRIES.toTypedArray())
    private val languageDropdown = JComboBox<String>(LANGUAGES.keys.toTypedArray())
    private val methodBox = JComboBox<String>(CALCULATION_METHODS.keys.toTypedArray())
    private val autoShutdownCheckbox = JCheckBox("Enable Auto Shutdown")
    private val playSoundCheckbox = JCheckBox("Play Adhan Sound")
    private val minutesBeforeSpinner = JSpinner(SpinnerNumberModel(5, 0, 60, 5))

    override fun createComponent(): JComponent {
        // Initialize components
        countryDropdown.selectedItem = settings.country
        languageDropdown.selectedItem = LANGUAGES.entries.find { it.value == settings.language }?.key ?: "English"
        methodBox.selectedItem = CALCULATION_METHODS.entries.find { it.value == settings.method }?.key ?: "Egyptian (General Authority of Survey)"
        autoShutdownCheckbox.isSelected = settings.autoShutdownEnabled
        playSoundCheckbox.isSelected = settings.playSound
        minutesBeforeSpinner.value = settings.notificationMinutesBefore

        // Set up city dropdown
        updateCities(settings.country)

        // Add listeners
        countryDropdown.addActionListener { updateCities(countryDropdown.selectedItem as String) }

        // Build panel
        val panel = JPanel(GridLayout(0, 2, 10, 10)).apply {
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

            add(JLabel("Country:"))
            add(countryDropdown)

            add(JLabel("City:"))
            add(cityDropdown)

            add(JLabel("Language:"))
            add(languageDropdown)

            add(JLabel("Calculation Method:"))
            add(methodBox)

            add(JLabel("Auto Shutdown:"))
            add(autoShutdownCheckbox)

            add(JLabel("Notification Sound:"))
            add(playSoundCheckbox)

            add(JLabel("Notify Before Prayer (minutes):"))
            add(minutesBeforeSpinner)
        }

        initialized = true
        return JPanel(BorderLayout()).apply {
            add(panel, BorderLayout.NORTH)
        }
    }

    override fun isModified(): Boolean {
        return settings.city != cityDropdown.selectedItem ||
                settings.country != countryDropdown.selectedItem ||
                settings.language != getSelectedLanguageCode() ||
                settings.method != getSelectedMethodValue() ||
                settings.autoShutdownEnabled != autoShutdownCheckbox.isSelected ||
                settings.playSound != playSoundCheckbox.isSelected ||
                settings.notificationMinutesBefore != (minutesBeforeSpinner.value as? Int ?: 5)
    }

    override fun apply() {
        settings.city = cityDropdown.selectedItem as? String ?: "Cairo"
        settings.country = countryDropdown.selectedItem as? String ?: "Egypt"
        settings.language = getSelectedLanguageCode()
        settings.method = getSelectedMethodValue()
        settings.autoShutdownEnabled = autoShutdownCheckbox.isSelected
        settings.playSound = playSoundCheckbox.isSelected
        settings.notificationMinutesBefore = (minutesBeforeSpinner.value as? Int) ?: 5
    }

    override fun getDisplayName(): String = "Aqim As-Salat Settings"

    override fun reset() {
        countryDropdown.selectedItem = settings.country
        cityDropdown.selectedItem = settings.city
        languageDropdown.selectedItem = LANGUAGES.entries.find { it.value == settings.language }?.key ?: "English"
        methodBox.selectedItem = CALCULATION_METHODS.entries.find { it.value == settings.method }?.key ?: "Egyptian (General Authority of Survey)"
        autoShutdownCheckbox.isSelected = settings.autoShutdownEnabled
        playSoundCheckbox.isSelected = settings.playSound
        minutesBeforeSpinner.value = settings.notificationMinutesBefore
    }

    private fun updateCities(country: String) {
        if (!initialized) return

        val cities = when (country) {
            "Egypt" -> arrayOf("Cairo", "Alexandria", "Giza", "Luxor")
            "Saudi Arabia" -> arrayOf("Makkah", "Madinah", "Riyadh", "Jeddah")
            "United Arab Emirates" -> arrayOf("Dubai", "Abu Dhabi", "Sharjah")
            "Morocco" -> arrayOf("Casablanca", "Rabat", "Marrakesh")
            "Turkey" -> arrayOf("Istanbul", "Ankara", "Izmir")
            else -> arrayOf("Cairo")
        }

        cityDropdown.model = DefaultComboBoxModel(cities)
        if (cities.contains(settings.city)) {
            cityDropdown.selectedItem = settings.city
        } else {
            cityDropdown.selectedIndex = 0
        }
    }

    private fun getSelectedLanguageCode(): String {
        return LANGUAGES[languageDropdown.selectedItem as? String] ?: "en"
    }

    private fun getSelectedMethodValue(): Int {
        return CALCULATION_METHODS[methodBox.selectedItem as? String] ?: 5
    }

    companion object {
        private val DEFAULT_COUNTRIES = listOf("Egypt", "Saudi Arabia", "United Arab Emirates", "Morocco", "Turkey")
        private val LANGUAGES = mapOf("English" to "en", "العربية" to "ar")
        private val CALCULATION_METHODS = mapOf(
            "ISNA (North America)" to 2,
            "MWL (Muslim World League)" to 3,
            "Umm Al-Qura (Saudi Arabia)" to 4,
            "Egyptian (General Authority of Survey)" to 5
        )
    }
}
