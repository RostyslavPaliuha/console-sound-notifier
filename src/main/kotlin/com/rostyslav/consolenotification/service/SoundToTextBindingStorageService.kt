package com.rostyslav.consolenotification.service

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "SoundMappings", storages = [Storage("sound_mappings.xml")])
class SoundToTextBindingStorageService :
    PersistentStateComponent<SoundToTextBindingStorageService.State> {

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
        var serviceInstance: SoundToTextBindingStorageService = SoundToTextBindingStorageService()
        fun getInstance(): SoundToTextBindingStorageService {
            return if (serviceInstance != null) serviceInstance
            else {
                serviceInstance = SoundToTextBindingStorageService();
                return serviceInstance;
            }
        }
    }

}
