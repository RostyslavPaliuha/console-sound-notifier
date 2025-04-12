package com.rostyslav.consolenotification.messages

import com.intellij.util.messages.Topic

interface RefreshBindingsTopicInitializer {

    fun onRefreshOnChange(data: String)

    companion object {
        val TOPIC = Topic.create(
            "refresh bindings",
            RefreshBindingsTopicInitializer::class.java
        )
    }
}
