package com.rostyslav.consolenotification.filter.consoletext

import com.intellij.execution.filters.Filter
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.rostyslav.consolenotification.service.BindingStorageService
import com.rostyslav.consolenotification.service.ConsoleTextService
import org.jetbrains.annotations.NotNull

@Service(Service.Level.PROJECT)
class ConsoleTextFilter(private val project: Project) : Filter {

    private val consoleTextService: ConsoleTextService = project.service()

    private val bindingsStorageService: BindingStorageService = project.service()

    override fun applyFilter(@NotNull line: String, entireLength: Int): Filter.Result? {
        if (line.isEmpty() || !bindingsStorageService.hasBindings()) {
            return null
        }
        consoleTextService.processTextLine(line)
        return null
    }
}
