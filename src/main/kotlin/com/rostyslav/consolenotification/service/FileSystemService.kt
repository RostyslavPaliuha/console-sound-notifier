package com.rostyslav.consolenotification.service

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.components.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Service(Service.Level.PROJECT)
class FileSystemService {

    companion object {
        val DEFAULT_SOUND_FILE_NAMES = listOf(
            "airhorn.wav",
            "fahhhh.wav",
            "losing-horn.wav",
            "ohYes.wav"
        )

        fun getMediaDirectoryPath(): Path = PathManager.getPluginsDir()
            .resolve("console-sound-notifier")
            .resolve("media")

        fun isInMediaDirectory(filePath: Path): Boolean {
            return filePath.toAbsolutePath().normalize()
                .startsWith(getMediaDirectoryPath().toAbsolutePath().normalize())
        }
    }

    fun copyMediaToPluginsDir(selectedFile: Path) {
        val mediaDirectory = getMediaDirectoryPath()
        makeMediaDirectory(mediaDirectory)
        copyMediaFileTo(selectedFile, mediaDirectory)
    }

    fun installDefaultSounds() {
        val mediaDirectory = getMediaDirectoryPath()
        makeMediaDirectory(mediaDirectory)
        DEFAULT_SOUND_FILE_NAMES.forEach { fileName ->
            copyBundledSoundIfMissing(fileName, mediaDirectory)
        }
    }

    private fun copyMediaFileTo(selectedFile: Path, mediaDirectory: Path) {
        Files.copy(
            selectedFile,
            mediaDirectory.resolve(selectedFile.fileName),
            StandardCopyOption.REPLACE_EXISTING
        )
    }

    private fun makeMediaDirectory(mediaDirectory: Path) {
        Files.createDirectories(mediaDirectory)
    }

    private fun copyBundledSoundIfMissing(fileName: String, mediaDirectory: Path) {
        val targetPath = mediaDirectory.resolve(fileName)
        if (Files.exists(targetPath)) return

        val resourcePath = "sound/$fileName"
        val inputStream = javaClass.classLoader.getResourceAsStream(resourcePath)
            ?: throw IllegalStateException("Bundled sound resource not found: $resourcePath")
        inputStream.use { input ->
            Files.copy(input, targetPath)
        }
    }

    fun createDirectory(directoryPath: Path) {
        Files.createDirectories(directoryPath)
    }

}
