package com.rostyslav.consolenotification.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.ToolWindowManager

class OpenToolWindowAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val toolWindow =
            ToolWindowManager.getInstance(project).getToolWindow("Console Sound Notifier")
        toolWindow?.show(null)
    }
}