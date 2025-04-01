package com.rostyslav.consolenotification.ui

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.util.NlsSafe
import com.intellij.ui.components.JBTextField
import com.rostyslav.consolenotification.service.BindingStorageService
import com.rostyslav.consolenotification.service.FileSystemService
import com.rostyslav.consolenotification.ui.UIService.Companion.addRows
import java.awt.GridLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.nio.file.Path
import javax.swing.JComponent
import javax.swing.JPanel

class BindingSettingsDialog(private val project: Project, selectedText: @NlsSafe String) :
    DialogWrapper(true) {

    private val textField = JBTextField(selectedText, 20)

    private val filePathField = JBTextField("press to select file...", 20)

    init {
        filePathField.isEditable = false
        title = "Bind Sound To Console Output Text"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(GridLayout(0, 2))
        addRows(textField, filePathField, panel)
        filePathField.addMouseListener(
            object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    val descriptor = FileChooserDescriptor(true, false, false, false, false, false)
                    FileChooser.chooseFile(descriptor, null, null) { virtualFile ->
                        filePathField.text = virtualFile.path
                        project.getService(FileSystemService::class.java)
                            .copyMediaToPluginsDir(Path.of(virtualFile.path))
                    }
                }
            })
        return panel
    }

    override fun doOKAction() {
        val text = textField.text.trim()
        val filePath = filePathField.text.trim()
        if (text.isNotEmpty() && filePath.isNotEmpty()) {
            BindingStorageService.getInstance().addMapping(text, filePath)
            ConsoleSoundNotifierToolWindowFactory.updateBindings()
        }
        super.doOKAction()
    }

}
