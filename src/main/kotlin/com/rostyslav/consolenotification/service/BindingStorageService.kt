package com.rostyslav.consolenotification.service

import com.intellij.openapi.components.*

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

    fun updateBinding(key: String, newValue: String) {
        val bindings = getAllBindings()
        bindings[key] = newValue
    }

    fun deleteBinding(key: String) {
        val bindings = getAllBindings()
        bindings.remove(key)
    }

    companion object {
        fun getInstance(): BindingStorageService = service()
    }
}
