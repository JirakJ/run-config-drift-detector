package com.jakubjirak.runconfigdrift.ui

import com.jakubjirak.runconfigdrift.engine.DriftDetectionEngine
import com.jakubjirak.runconfigdrift.loader.RequirementsLoader
import com.jakubjirak.runconfigdrift.settings.DriftDetectorSettings
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger

/**
 * Action to manually trigger a re-scan for drift issues.
 */
class RescanDriftAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        if (!DriftDetectorSettings.getInstance().enabled) {
            return
        }

        val requirements = RequirementsLoader.loadFromProject(project)
        if (requirements == null) {
            try {
                NotificationGroupManager.getInstance()
                    .getNotificationGroup("Run Config Drift Detector")
                    .createNotification(
                        "No Requirements File",
                        "Create a .run-requirements.yaml to define project requirements",
                        NotificationType.INFORMATION,
                    )
                    .notify(project)
            } catch (ex: Exception) {
                LOG.warn("Failed to show notification", ex)
            }
            return
        }

        val report = DriftDetectionEngine.analyze(project, requirements)

        try {
            val type = if (report.hasErrors) NotificationType.ERROR
            else if (report.hasWarnings) NotificationType.WARNING
            else NotificationType.INFORMATION

            val message = if (report.isHealthy) {
                "No drift issues detected. Health score: ${report.score}%"
            } else {
                "${report.issues.size} drift issue(s) found. Health score: ${report.score}%"
            }

            NotificationGroupManager.getInstance()
                .getNotificationGroup("Run Config Drift Detector")
                .createNotification("Drift Detection Report", message, type)
                .notify(project)
        } catch (ex: Exception) {
            LOG.warn("Failed to show notification", ex)
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = DriftDetectorSettings.getInstance().enabled
    }

    companion object {
        private val LOG = Logger.getInstance(RescanDriftAction::class.java)
    }
}
