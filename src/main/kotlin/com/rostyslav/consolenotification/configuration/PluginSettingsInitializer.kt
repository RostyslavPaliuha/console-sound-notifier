package com.rostyslav.consolenotification.configuration

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.rostyslav.consolenotification.service.FileSystemService
import com.rostyslav.consolenotification.service.FileSystemService.Companion.getMediaDirectoryPath

@Service(Service.Level.PROJECT)
class PluginSettingsInitializer : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        val fileSystemService = project.getService(FileSystemService::class.java)
        val settings = SettingsStorage.getInstance()
        if (!settings.state.initialized) {
            println("First startup detected. Initializing media files...")
            fileSystemService.createDirectory(getMediaDirectoryPath())
            settings.state.initialized = true
            ApplicationManager.getApplication().saveSettings()
        } else {
            println("Plugin has been initialized before. Skipping setup.")
        }
    }
}