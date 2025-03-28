package com.rostyslav.consolenotification.filter.consoletext

import com.intellij.execution.filters.Filter
import com.rostyslav.consolenotification.service.SoundService
import com.rostyslav.consolenotification.service.SoundToTextBindingStorageService
import org.jetbrains.annotations.NotNull

class ConsoleTextFilter : Filter {

    private var soundService: SoundService;

    constructor(service: SoundService) {
        this.soundService = service;
    }

    override fun applyFilter(@NotNull line: String, entireLength: Int): Filter.Result? {
        val soundFilePath =
            SoundToTextBindingStorageService.getInstance().getMediaFileForRespectiveText(line)
        if (soundFilePath != null) {
            soundService. addTask(soundFilePath)
        }
        return null;
    }
}