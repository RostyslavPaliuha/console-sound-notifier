package com.rostyslav.consolenotification.filter.consoletext

import com.intellij.execution.filters.Filter
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.rostyslav.consolenotification.service.BindingStorageService
import com.rostyslav.consolenotification.service.SoundService
import org.jetbrains.annotations.NotNull

@Service(Service.Level.PROJECT)
class ConsoleTextFilter(private val project: Project) : Filter {

    private var soundService: SoundService

    private var bindingsStorageService: BindingStorageService;

    init {
        this.soundService = SoundService.getInstance()
        this.bindingsStorageService = BindingStorageService.getInstance()
    }

    override fun applyFilter(@NotNull line: String, entireLength: Int): Filter.Result? {
        val soundFilePath = bindingsStorageService.getMediaFileForRespectiveText(line)
        if (soundFilePath != null) {
            soundService.addTask(soundFilePath)
        }
        return null
    }
}