package com.rostyslav.consolenotification.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.io.File
import java.util.concurrent.CountDownLatch
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent.Type.STOP

@Service(Service.Level.PROJECT)
class SoundService : Disposable {

    private val log = Logger.getInstance(SoundService::class.java)

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

    private fun playSound(pathToMedia: String) {
        var clip: Clip? = null
        try {
            AudioSystem.getAudioInputStream(File(pathToMedia)).use { audioInputStream ->
                val countDownLatch = CountDownLatch(1)
                clip = AudioSystem.getClip()
                clip.addLineListener { lineUnavailable ->
                    if (lineUnavailable.type == STOP) {
                        countDownLatch.countDown()
                    }
                }
                clip.open(audioInputStream)
                clip.start()
                countDownLatch.await()
                clip.drain()
            }
        } catch (e: Exception) {
            log.error(e)
        } finally {
            clip?.close()
        }
    }

    override fun dispose() {
        playableMediaQueue.close()
        coroutineScope.cancel()
    }

}
