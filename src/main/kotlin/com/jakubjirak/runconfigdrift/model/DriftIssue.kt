package com.jakubjirak.runconfigdrift.model

/**
 * Represents a single drift issue detected between project requirements and actual configuration.
 */
data class DriftIssue(
    val type: DriftType,
    val severity: DriftSeverity,
    val description: String,
    val fixSuggestion: String,
)
