package com.rostyslav.consolenotification.service

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.rostyslav.consolenotification.configuration.SettingsStorage

@Service(Service.Level.PROJECT)
@State(name = "SoundMappings", storages = [Storage("sound_mappings.xml")])
class BindingStorageService : PersistentStateComponent<BindingStorageService.State> {

    data class State(var mappings: MutableMap<String, String> = mutableMapOf())

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    fun addMapping(text: String, filePath: String) {
        state.mappings[text] = filePath
    }

    fun getMediaFileForRespectiveText(text: String): String? {
        return state.mappings.asIterable().find { entry -> text.contains(entry.key) }?.value
    }

    fun getAllBindings(): MutableMap<String, String> {
        return state.mappings
    }


    companion object {
        fun getInstance(): BindingStorageService = service()
    }
}
