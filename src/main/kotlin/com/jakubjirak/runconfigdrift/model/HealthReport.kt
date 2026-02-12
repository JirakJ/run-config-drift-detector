package com.jakubjirak.runconfigdrift.model

import java.time.LocalDateTime

/**
 * Aggregated health report from drift detection analysis.
 */
data class HealthReport(
    val issues: List<DriftIssue>,
    val score: Int,
    val timestamp: LocalDateTime,
) {
    val hasErrors: Boolean
        get() = issues.any { it.severity == DriftSeverity.ERROR }

    val hasWarnings: Boolean
        get() = issues.any { it.severity == DriftSeverity.WARNING }

    val isHealthy: Boolean
        get() = issues.isEmpty()

    val statusIcon: String
        get() = when {
            hasErrors -> "❌"
            hasWarnings -> "⚠️"
            else -> "✅"
        }
}
