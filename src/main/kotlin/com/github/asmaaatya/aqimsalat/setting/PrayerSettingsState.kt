package com.github.asmaaatya.aqimsalat.setting


import com.intellij.openapi.components.*

@State(
    name = "PrayerSettings",
    storages = [Storage("PrayerSettings.xml")]
)
@Service
class PrayerSettingsState : PersistentStateComponent<PrayerSettingsState.State> {

    data class State(
        var city: String = "Cairo",
        var country: String = "Egypt",
        var language: String = "en",
        var autoShutdownEnabled: Boolean = true,
        var method:Int = 5,
        var playSound: Boolean = true
    )

    private var state = State()

    override fun getState(): State = state
    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        fun getInstance(): PrayerSettingsState =
            ServiceManager.getService(PrayerSettingsState::class.java)
    }
}
