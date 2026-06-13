package com.rostyslav.consolenotification.service

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.Alarm

@Service(Service.Level.PROJECT)
class ConsoleTextService(private val project: Project) {

    private val soundService: SoundService = project.service()

    private val bindingStorageService: BindingStorageService = project.service()

    private val sessionTracker = ConsoleOutputSessionTracker()

    private val sessionLock = Any()

    private val idleAlarm = Alarm(Alarm.ThreadToUse.POOLED_THREAD, project)

    fun startOutput() {
        synchronized(sessionLock) {
            sessionTracker.reset()
        }
        idleAlarm.cancelAllRequests()
    }

    fun processTextLine(textLine: String) {
        val binding = bindingStorageService.getBinding(textLine)
        val soundFilePath = bindingStorageService.getMediaFileForRespectiveText(textLine)
        if (binding != null && soundFilePath != null) {
            val shouldPlaySound = synchronized(sessionLock) {
                sessionTracker.recordOccurrence(binding)
            }
            if (shouldPlaySound) {
                soundService.addTask(soundFilePath)
            }
        }
        scheduleSessionCompletion()
    }

    private fun scheduleSessionCompletion() {
        idleAlarm.cancelAllRequests()
        idleAlarm.addRequest(::completeSession, OUTPUT_IDLE_TIMEOUT_MILLIS)
    }

    private fun completeSession() {
        val repeatedOccurrences = synchronized(sessionLock) {
            val repeatedOccurrences = sessionTracker.repeatedOccurrences()
            sessionTracker.reset()
            repeatedOccurrences
        }

        if (repeatedOccurrences.isNotEmpty()) {
            showSummaryNotification(repeatedOccurrences)
        }
    }

    private fun showSummaryNotification(repeatedOccurrences: Map<String, Int>) {
        val content = repeatedOccurrences.entries.joinToString("<br/>") { (binding, count) ->
            "\"${StringUtil.escapeXmlEntities(binding)}\" occurred $count times"
        }
        NotificationGroupManager.getInstance()
            .getNotificationGroup(NOTIFICATION_GROUP_ID)
            .createNotification("Console Sound Notifier", content, NotificationType.INFORMATION)
            .notify(project)
    }

    companion object {
        private const val OUTPUT_IDLE_TIMEOUT_MILLIS = 1_000
        private const val NOTIFICATION_GROUP_ID = "Console Sound Notifier"
    }
}

