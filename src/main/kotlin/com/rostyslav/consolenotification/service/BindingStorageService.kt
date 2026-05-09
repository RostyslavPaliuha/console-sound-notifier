package com.rostyslav.consolenotification.service

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
@State(name = "SoundMappings", storages = [Storage("sound_mappings.xml")])
class BindingStorageService : PersistentStateComponent<BindingStorageService.State> {

    data class State(var mappings: MutableMap<String, String> = mutableMapOf())

    data class Binding(val text: String, val soundFilePath: String)

    private var state = State()

    @Volatile
    private var bindingsSnapshot: List<Binding> = emptyList()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
        rebuildSnapshot()
    }

    fun addMapping(text: String, filePath: String) {
        state.mappings[text] = filePath
        rebuildSnapshot()
    }

    fun getMediaFileForRespectiveText(text: String): String? {
        return bindingsSnapshot.firstOrNull { binding -> text.contains(binding.text) }?.soundFilePath
    }

    fun getAllBindings(): Map<String, String> {
        return state.mappings.toMap()
    }

    fun updateBinding(key: String, newValue: String) {
        state.mappings[key] = newValue
        rebuildSnapshot()
    }

    fun deleteBinding(key: String) {
        state.mappings.remove(key)
        rebuildSnapshot()
    }

    fun hasBindings(): Boolean {
        return bindingsSnapshot.isNotEmpty()
    }

    private fun rebuildSnapshot() {
        bindingsSnapshot = state.mappings
            .filterKeys { it.isNotBlank() }
            .map { (text, soundFilePath) -> Binding(text, soundFilePath) }
    }

    companion object {
        fun getInstance(project: Project): BindingStorageService = project.service()
    }
}
