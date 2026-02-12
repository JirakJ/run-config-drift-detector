package com.jakubjirak.runconfigdrift

import com.jakubjirak.runconfigdrift.engine.DriftDetectionEngine
import com.jakubjirak.runconfigdrift.loader.RequirementsLoader
import com.jakubjirak.runconfigdrift.settings.DriftDetectorSettings
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

/**
 * Runs drift detection when the project opens if auto-scan is enabled.
 */
class DriftDetectorStartupActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        try {
            val settings = DriftDetectorSettings.getInstance()
            if (!settings.enabled || !settings.autoScanOnStartup) {
                LOG.info("Run Config Drift Detector: auto-scan disabled")
                return
            }

            LOG.info("Run Config Drift Detector: running startup scan")

            val requirements = RequirementsLoader.loadFromProject(project) ?: return
            val report = DriftDetectionEngine.analyze(project, requirements)

            if (!report.isHealthy) {
                notifyDrift(project, report)
            }

            LOG.info("Run Config Drift Detector: startup scan complete — score: ${report.score}%")
        } catch (e: Exception) {
            LOG.warn("Failed to run startup drift detection", e)
        }
    }

    private fun notifyDrift(project: Project, report: com.jakubjirak.runconfigdrift.model.HealthReport) {
        try {
            val type = if (report.hasErrors) NotificationType.WARNING else NotificationType.INFORMATION
            NotificationGroupManager.getInstance()
                .getNotificationGroup("Run Config Drift Detector")
                .createNotification(
                    "Drift Detected",
                    "${report.issues.size} drift issue(s) found. Health score: ${report.score}%. " +
                        "Check the Drift Report tool window for details.",
                    type,
                )
                .notify(project)
        } catch (e: Exception) {
            LOG.warn("Failed to show drift notification", e)
        }
    }

    companion object {
        private val LOG = Logger.getInstance(DriftDetectorStartupActivity::class.java)
    }
}
