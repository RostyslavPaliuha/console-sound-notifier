package com.rostyslav.consolenotification.ui

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.content.ContentFactory
import com.rostyslav.consolenotification.messages.RefreshBindingsTopicInitializer
import com.rostyslav.consolenotification.service.BindingStorageService
import java.awt.*
import javax.swing.*

class ConsoleSoundNotifierToolWindowFactory : ToolWindowFactory,
    DumbAware {
    var bindingsStorage: BindingStorageService

    var panel = JPanel(BorderLayout()).apply {
        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        background = JBColor.PanelBackground
    }

    var bindingsList = JBList<String>()

    var bindings: MutableMap<String, String>

    lateinit var project: Project;
    init {
        bindingsStorage = BindingStorageService.getInstance()
        bindings = bindingsStorage.getAllBindings()
    }

    override fun shouldBeAvailable(project: Project): Boolean = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        this.project = project
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
        val listPanel = JPanel()
        listPanel.layout = BoxLayout(listPanel, BoxLayout.Y_AXIS)
        listPanel.alignmentY = Component.TOP_ALIGNMENT
        listPanel.background = JBColor.PanelBackground

        bindings.forEach { (key, value) ->
            val rowPanel = JPanel()
            rowPanel.layout = BoxLayout(rowPanel, BoxLayout.X_AXIS)
            rowPanel.border = BorderFactory.createEmptyBorder(5, 10, 5, 10)
            rowPanel.alignmentX = Component.LEFT_ALIGNMENT
            rowPanel.background = JBColor.PanelBackground
            val label = JLabel("$key -> " +  value.split("/").last())
            label.font = Font("JetBrains Mono", Font.PLAIN, 12)
            label.maximumSize = Dimension(Int.MAX_VALUE, label.preferredSize.height)
            rowPanel.add(label)

            rowPanel.add(Box.createHorizontalGlue()) // Push buttons to the right

            val updateButton = JButton("Update").apply {
                font = Font("JetBrains Mono", Font.PLAIN, 11)
                preferredSize = Dimension(80, 24)
                maximumSize = preferredSize
                addActionListener {  SwingUtilities.invokeLater {
                    BindingDialog(project, key).show()
                } }
            }

            val deleteButton = JButton("Delete").apply {
                font = Font("JetBrains Mono", Font.PLAIN, 11)
                preferredSize = Dimension(80, 24)
                maximumSize = preferredSize
                addActionListener { deleteBinding(key) }
            }

            val buttonPanel = JPanel()
            buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.X_AXIS)
            buttonPanel.background = JBColor.PanelBackground
            buttonPanel.add(updateButton)
            buttonPanel.add(Box.createRigidArea(Dimension(8, 0)))
            buttonPanel.add(deleteButton)

            rowPanel.add(buttonPanel)

            listPanel.add(rowPanel)
            listPanel.add(Box.createVerticalStrut(5))
        }

        // 🔝 Push all rows to the top
        listPanel.add(Box.createVerticalGlue())

        val scrollPane = JScrollPane(listPanel).apply {
            border = BorderFactory.createEmptyBorder()
            verticalScrollBar.unitIncrement = 12
        }

        if (panel.componentCount > 1) {
            panel.remove(1)
        }

        panel.add(scrollPane, BorderLayout.CENTER)
        panel.revalidate()
        panel.repaint()
    }

        fun updateBinding(key: String, oldValue: String) {
        val newValue = JOptionPane.showInputDialog(panel, "Enter new value for $key:", oldValue)
        if (newValue != null) {
            bindingsStorage.updateBinding(key, newValue)
            updateBindings()
        }
    }

    fun deleteBinding(key: String) {
        bindingsStorage.deleteBinding(key)
        updateBindings()
    }
}

