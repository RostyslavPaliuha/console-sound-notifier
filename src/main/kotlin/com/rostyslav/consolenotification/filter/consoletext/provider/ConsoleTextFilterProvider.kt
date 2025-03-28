package com.rostyslav.consolenotification.filter.consoletext.provider

import com.intellij.execution.filters.ConsoleFilterProvider
import com.intellij.execution.filters.Filter
import com.intellij.openapi.project.Project
import com.rostyslav.consolenotification.filter.consoletext.ConsoleTextFilter
import com.rostyslav.consolenotification.service.SoundService

class ConsoleTextFilterProvider : ConsoleFilterProvider {

    override fun getDefaultFilters(project: Project): Array<Filter> {
        return arrayOf(ConsoleTextFilter(SoundService.getInstance()))
    }
}