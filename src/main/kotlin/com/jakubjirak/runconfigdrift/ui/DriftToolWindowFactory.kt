package com.jakubjirak.runconfigdrift.ui

import com.jakubjirak.runconfigdrift.engine.DriftDetectionEngine
import com.jakubjirak.runconfigdrift.loader.RequirementsLoader
import com.jakubjirak.runconfigdrift.model.HealthReport
import com.jakubjirak.runconfigdrift.settings.DriftDetectorSettings
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Font
import java.time.format.DateTimeFormatter
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.SwingConstants

/**
 * Tool window factory for the drift health report dashboard.
 */
class DriftToolWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = DriftReportPanel(project)
        val content = ContentFactory.getInstance().createContent(panel, "Health Report", false)
        toolWindow.contentManager.addContent(content)
    }
}

/**
 * Panel displaying the drift health report with issues list and score.
 */
class DriftReportPanel(private val project: Project) : JPanel(BorderLayout()) {

    private val issuesPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
    }
    private val headerLabel = JBLabel("Drift Health Report").apply {
        font = font.deriveFont(Font.BOLD, 16f)
        border = JBUI.Borders.empty(8, 10)
    }
    private val scoreLabel = JBLabel("").apply {
        border = JBUI.Borders.empty(4, 10)
    }
    private val timestampLabel = JBLabel("").apply {
        border = JBUI.Borders.empty(2, 10, 8, 10)
    }

    init {
        val topPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(headerLabel)
            add(scoreLabel)
            add(timestampLabel)
        }

        val rescanButton = JButton("Re-scan").apply {
            addActionListener { runScan() }
        }

        val buttonPanel = JPanel(BorderLayout()).apply {
            border = JBUI.Borders.empty(4, 10)
            add(rescanButton, BorderLayout.WEST)
        }

        val topContainer = JPanel(BorderLayout()).apply {
            add(topPanel, BorderLayout.CENTER)
            add(buttonPanel, BorderLayout.SOUTH)
        }

        add(topContainer, BorderLayout.NORTH)
        add(JBScrollPane(issuesPanel), BorderLayout.CENTER)

        runScan()
    }

    private fun runScan() {
        if (!DriftDetectorSettings.getInstance().enabled) {
            showNoIssues("Drift detection is disabled")
            return
        }

        val requirements = RequirementsLoader.loadFromProject(project)
        if (requirements == null) {
            showNoIssues("No .run-requirements.yaml found. Create one to start drift detection.")
            return
        }

        val report = DriftDetectionEngine.analyze(project, requirements)
        displayReport(report)
    }

    private fun displayReport(report: HealthReport) {
        scoreLabel.text = "${report.statusIcon} Health Score: ${report.score}%"
        timestampLabel.text = "Last scan: ${report.timestamp.format(TIMESTAMP_FORMAT)}"

        issuesPanel.removeAll()

        if (report.isHealthy) {
            issuesPanel.add(createIssueRow("✅", "No drift issues detected", "Your configuration matches all requirements."))
        } else {
            for (issue in report.issues) {
                val icon = issue.severity.icon
                issuesPanel.add(createIssueRow(icon, issue.description, "Fix: ${issue.fixSuggestion}"))
            }
        }

        issuesPanel.revalidate()
        issuesPanel.repaint()
    }

    private fun showNoIssues(message: String) {
        scoreLabel.text = ""
        timestampLabel.text = ""
        issuesPanel.removeAll()
        issuesPanel.add(createIssueRow("ℹ️", message, ""))
        issuesPanel.revalidate()
        issuesPanel.repaint()
    }

    private fun createIssueRow(icon: String, title: String, detail: String): JPanel {
        return JPanel(BorderLayout()).apply {
            border = BorderFactory.createCompoundBorder(
                JBUI.Borders.customLine(JBUI.CurrentTheme.CustomFrameDecorations.separatorForeground(), 0, 0, 1, 0),
                JBUI.Borders.empty(8, 10),
            )
            maximumSize = java.awt.Dimension(Int.MAX_VALUE, 60)
            alignmentX = Component.LEFT_ALIGNMENT

            add(JBLabel("<html><b>$icon $title</b></html>").apply {
                horizontalAlignment = SwingConstants.LEFT
            }, BorderLayout.NORTH)

            if (detail.isNotEmpty()) {
                add(JBLabel("<html><i>$detail</i></html>").apply {
                    horizontalAlignment = SwingConstants.LEFT
                    border = JBUI.Borders.emptyTop(2)
                }, BorderLayout.CENTER)
            }
        }
    }

    companion object {
        private val TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }
}
