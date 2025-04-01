package com.rostyslav.consolenotification.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.rostyslav.consolenotification.ui.BindingSettingsDialog
import javax.swing.SwingUtilities

class OpenBindingPanelAction : AnAction("Bind sound to selected text") {

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val project = e.project ?: return
        val selectedText = editor.selectionModel.selectedText ?: return
        SwingUtilities.invokeLater {
            BindingSettingsDialog(project, selectedText).show()
        }
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabled = editor?.selectionModel?.hasSelection() == true
    }
}
