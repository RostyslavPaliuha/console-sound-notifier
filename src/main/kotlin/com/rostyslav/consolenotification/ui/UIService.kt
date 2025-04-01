package com.rostyslav.consolenotification.ui

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import javax.swing.JPanel

class UIService {
    companion object {
        fun addRows(text: JBTextField, pathToMedia: JBTextField, panel: JPanel) {
            panel.add(JBLabel("Text:"))
            panel.add(text)
            panel.add(JBLabel("Sound file:"))
            panel.add(pathToMedia)
        }
    }
}