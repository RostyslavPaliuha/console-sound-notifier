package com.rostyslav.consolenotification.filter.consoletext

import com.intellij.execution.filters.Filter
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.rostyslav.consolenotification.service.BindingStorageService
import com.rostyslav.consolenotification.service.SoundService
import org.jetbrains.annotations.NotNull

@Service(Service.Level.PROJECT)
class ConsoleTextFilter(private val project: Project) : Filter {

    private val soundService: SoundService = project.service()

    private val bindingsStorageService: BindingStorageService = project.service()

    override fun applyFilter(@NotNull line: String, entireLength: Int): Filter.Result? {
        if (line.isEmpty() || !bindingsStorageService.hasBindings()) {
            return null
        }

        val soundFilePath = bindingsStorageService.getMediaFileForRespectiveText(line)
        if (soundFilePath != null) {
            soundService.addTask(soundFilePath)
        }
        return null
    }
}
