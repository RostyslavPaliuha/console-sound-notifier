package com.rostyslav.consolenotification.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.UnsupportedAudioFileException

class SoundService {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val playableMediaQueue: Channel<String> = Channel(Channel.UNLIMITED)

    fun addTask(soundFilePath: String) {
        playableMediaQueue.trySend(soundFilePath)
    }

    init {
        startPlaying()
    }

    fun startPlaying() {
        coroutineScope.launch {
            for (mediaFilePath in playableMediaQueue)
                playSound(mediaFilePath)
        }
    }

    suspend fun playSound(pathToMedia: String) {
        try {
            val audioInputStream = AudioSystem.getAudioInputStream(File(pathToMedia))
            val clip: Clip = AudioSystem.getClip()
            clip.open(audioInputStream)
            clip.start()
            delay(5)
            clip.drain()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: UnsupportedAudioFileException) {
            e.printStackTrace()
        } catch (e: LineUnavailableException) {
            e.printStackTrace()
        }
    }

    companion object {
        var serviceInstance: SoundService = SoundService()
        fun getInstance(): SoundService {
            return if (serviceInstance != null) serviceInstance
            else {
                serviceInstance = SoundService();
                return serviceInstance;
            }
        }
    }
}