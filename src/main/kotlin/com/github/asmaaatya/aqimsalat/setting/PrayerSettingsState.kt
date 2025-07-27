package com.github.asmaaatya.aqimsalat.setting


import com.intellij.openapi.components.*

@State(
    name = "PrayerSettings",
    storages = [Storage("PrayerSettings.xml")]
)
@Service(Service.Level.APP)
class PrayerSettingsState : PersistentStateComponent<PrayerSettingsState.State> {

    data class State(
        var city: String = DEFAULT_CITY,
        var country: String = DEFAULT_COUNTRY,
        var language: String = DEFAULT_LANGUAGE,
        var autoShutdownEnabled: Boolean = true,
        var method: Int = DEFAULT_METHOD,
        var playSound: Boolean = true,
        var notificationMinutesBefore: Int = DEFAULT_NOTIFICATION_MINUTES,
        var lastUpdated: Long = System.currentTimeMillis()
    ) {
        companion object {
            const val DEFAULT_CITY = "Cairo"
            const val DEFAULT_COUNTRY = "Egypt"
            const val DEFAULT_LANGUAGE = "en"
            const val DEFAULT_METHOD = 5
            const val DEFAULT_NOTIFICATION_MINUTES = 5
        }
    }

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        // Validate loaded state
        this.state = state.copy(
            notificationMinutesBefore = state.notificationMinutesBefore.coerceIn(0, 60),
            method = if (state.method in 1..12) state.method else State.DEFAULT_METHOD
        )
    }

    fun updateSettings(block: State.() -> Unit) {
        state.block()
    }

    companion object {
        fun getInstance(): PrayerSettingsState = service()

        // Calculation methods constants
        const val METHOD_ISNA = 2
        const val METHOD_MWL = 3
        const val METHOD_UMM_AL_QURA = 4
        const val METHOD_EGYPTIAN = 5
    }
}
