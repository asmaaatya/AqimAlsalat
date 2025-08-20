package com.github.asmaaatya.aqimsalat.setting
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage



@Service(Service.Level.APP)
@State(name = "AqimAlsalatSettings", storages = [Storage("AqimAlsalatSettings.xml")])
class SettingsState : PersistentStateComponent<SettingsState> {

    var city: String = "Cairo"
    var country: String = "EG"
    var method: Int = 0
    var shutdownOnPrayer: Boolean = false
    var playSound: Boolean = true

    override fun getState(): SettingsState = this

    override fun loadState(state: SettingsState) {
        this.city = state.city
        this.country = state.country
        this.method = state.method
        this.shutdownOnPrayer = state.shutdownOnPrayer
        this.playSound = state.playSound
    }

    companion object {
        fun getInstance(): SettingsState =
            ApplicationManager.getApplication().getService(SettingsState::class.java)
    }

}





