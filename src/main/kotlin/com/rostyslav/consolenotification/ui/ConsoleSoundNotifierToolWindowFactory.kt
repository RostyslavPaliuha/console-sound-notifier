package com.rostyslav.consolenotification.ui

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.content.ContentFactory
import com.rostyslav.consolenotification.messages.RefreshBindingsTopicInitializer
import com.rostyslav.consolenotification.service.BindingStorageService
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.SwingUtilities

class ConsoleSoundNotifierToolWindowFactory : ToolWindowFactory,
    DumbAware {
    var bindingsStorage: BindingStorageService

    var panel = JPanel(BorderLayout())

    var bindingsList = JBList<Any>()

    var bindings: MutableMap<String, String>

    init {
        bindingsStorage = BindingStorageService.getInstance()
        bindings = bindingsStorage.getAllBindings()
    }

    override fun shouldBeAvailable(project: Project): Boolean = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        subscribeOnRefreshBindingsTopic(project)
        panel.add(JBLabel("Bindings list"), BorderLayout.PAGE_START)
        createBindingsScrolableList()
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun subscribeOnRefreshBindingsTopic(project: Project) {
        project.messageBus.connect().subscribe(
            RefreshBindingsTopicInitializer.TOPIC,
            object : RefreshBindingsTopicInitializer {
                override fun onRefreshOnChange(data: String) {
                    updateBindings()
                }
            })
    }

    fun updateBindings() {
        bindings = bindingsStorage.getAllBindings()
        SwingUtilities.invokeLater {
            try {
                panel.remove(1)
            } catch (e: Exception) {

            }
            createBindingsScrolableList()
        }
    }

    fun createBindingsScrolableList() {
        if (bindings.isNotEmpty()) {
            bindingsList =
                JBList(bindings.map { entry -> "${entry.component1()} : ${entry.component2()}" })
            panel.add(
                JScrollPane(bindingsList), BorderLayout.AFTER_LAST_LINE,
            )
        }
    }

}