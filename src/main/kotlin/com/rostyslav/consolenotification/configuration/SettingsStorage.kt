package com.rostyslav.consolenotification.configuration

import com.intellij.openapi.components.*

@Service
@State(name = "SettingsStorage", storages = [Storage("plugin_settings.xml")])
class SettingsStorage : PersistentStateComponent<SettingsStorage.State> {

    data class State(var initialized: Boolean = false)

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        fun getInstance(): SettingsStorage = service()
    }
}
