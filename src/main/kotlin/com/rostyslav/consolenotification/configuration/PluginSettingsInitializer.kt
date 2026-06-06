package com.rostyslav.consolenotification.configuration

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.rostyslav.consolenotification.service.FileSystemService

@Service(Service.Level.PROJECT)
class PluginSettingsInitializer : ProjectManagerListener {

    private val log = Logger.getInstance(PluginSettingsInitializer::class.java)

    override fun projectOpened(project: Project) {
        log.info("Default sounds added to the filesystem.")
        val settings = SettingsStorage.getInstance()
        if (!settings.state.initialized) {
            log.info("First startup detected. Initializing media files...")
            val fileSystemService = project.getService(FileSystemService::class.java)
            fileSystemService.installDefaultSounds()
            settings.state.initialized = true
            ApplicationManager.getApplication().saveSettings()
        } else {
            log.info("Plugin has been initialized before. Skip initializing media files...")
        }
    }
}
