package com.rostyslav.consolenotification.service

class ConsoleOutputSessionTracker {

    private val bindingCounter = linkedMapOf<String, Int>()

    fun recordOccurrence(binding: String): Boolean {
        val currentCount = bindingCounter[binding] ?: 0
        bindingCounter[binding] = currentCount + 1
        return currentCount == 0
    }

    fun occurrencesSnapshot(): Map<String, Int> = bindingCounter.toMap()

    fun repeatedOccurrences(): Map<String, Int> {
        return bindingCounter.filterValues { count -> count > 1 }
    }

    fun reset() {
        bindingCounter.clear()
    }
}