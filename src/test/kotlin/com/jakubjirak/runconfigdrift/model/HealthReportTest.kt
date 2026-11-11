package com.jakubjirak.runconfigdrift.model

import com.jakubjirak.runconfigdrift.settings.DriftDetectorSettings
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class HealthReportTest {

    private fun issue(severity: DriftSeverity) = DriftIssue(DriftType.MISSING_ENV_VAR, severity, "d", "f")

    @Test
    fun `healthy report`() {
        val report = HealthReport(emptyList(), 100, LocalDateTime.MIN)
        assertTrue(report.isHealthy)
        assertFalse(report.hasErrors)
        assertFalse(report.hasWarnings)
        assertEquals("✅", report.statusIcon)
    }

    @Test
    fun `errors dominate the status icon`() {
        val report = HealthReport(listOf(issue(DriftSeverity.WARNING), issue(DriftSeverity.ERROR)), 65, LocalDateTime.MIN)
        assertTrue(report.hasErrors)
        assertTrue(report.hasWarnings)
        assertEquals("❌", report.statusIcon)
    }

    @Test
    fun `warnings only and info only`() {
        assertEquals("⚠️", HealthReport(listOf(issue(DriftSeverity.WARNING)), 90, LocalDateTime.MIN).statusIcon)
        val infoOnly = HealthReport(listOf(issue(DriftSeverity.INFO)), 95, LocalDateTime.MIN)
        assertFalse(infoOnly.isHealthy)
        assertEquals("✅", infoOnly.statusIcon)
    }

    @Test
    fun `settings defaults and loadState`() {
        val settings = DriftDetectorSettings()
        assertTrue(settings.enabled)
        assertTrue(settings.autoScanOnStartup)
        assertEquals(".run-requirements.yaml", settings.specFilePath)
        settings.loadState(DriftDetectorSettings.State(enabled = false, specFilePath = "cfg/req.yaml", autoScanOnStartup = false))
        assertFalse(settings.enabled)
        assertEquals("cfg/req.yaml", settings.specFilePath)
    }
}
