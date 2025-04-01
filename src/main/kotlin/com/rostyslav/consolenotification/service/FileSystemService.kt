package com.rostyslav.consolenotification.service

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.components.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

@Service(Service.Level.PROJECT)
class FileSystemService {

    fun copyMediaToPluginsDir(selectedFile: Path) {
        val mediaDirectory = getMediaDirectoryPath()
        makeMediaDirectory(mediaDirectory)
        copyMediaFileTo(selectedFile, mediaDirectory)
    }

    private fun copyMediaFileTo(selectedFile: Path, mediaDirectory: Path) {
        Files.copy(
            selectedFile,
            Files.newOutputStream(mediaDirectory.resolve(selectedFile.fileName))
        )
    }

    private fun makeMediaDirectory(mediaDirectory: Path) {
        if (!Files.exists(mediaDirectory)) Files.createDirectory(mediaDirectory)
    }

    private fun getMediaDirectoryPath(): Path = PathManager.getPluginsDir()
        .resolve("console-sound-notifier")
        .resolve("media")
}