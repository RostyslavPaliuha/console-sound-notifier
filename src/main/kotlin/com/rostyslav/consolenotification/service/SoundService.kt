package com.rostyslav.consolenotification.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.io.File
import java.io.IOException
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.UnsupportedAudioFileException

@Service(Service.Level.PROJECT)
class SoundService : Disposable {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val playableMediaQueue: Channel<String> = Channel(Channel.UNLIMITED)

    init {
        startPlaying()
    }

    fun addTask(soundFilePath: String) {
        playableMediaQueue.trySend(soundFilePath)
    }

    private fun startPlaying() {
        coroutineScope.launch {
            for (mediaFilePath in playableMediaQueue) {
                    playSound(mediaFilePath)
            }
        }
    }

    private suspend fun playSound(pathToMedia: String) {
        var clip: Clip? = null
        try {
            AudioSystem.getAudioInputStream(File(pathToMedia)).use { audioInputStream ->
                val openedClip = AudioSystem.getClip()
                clip = openedClip
                openedClip.open(audioInputStream)
                openedClip.start()
                delay(5)
                openedClip.drain()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: UnsupportedAudioFileException) {
            e.printStackTrace()
        } catch (e: LineUnavailableException) {
            e.printStackTrace()
        } finally {
            clip?.close()
        }
    }

    override fun dispose() {
        playableMediaQueue.close()
        coroutineScope.cancel()
    }

}
