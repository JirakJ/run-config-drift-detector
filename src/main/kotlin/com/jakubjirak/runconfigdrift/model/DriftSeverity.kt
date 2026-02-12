package com.jakubjirak.runconfigdrift.model

/**
 * Severity level of a drift issue.
 */
enum class DriftSeverity(val displayName: String, val icon: String) {
    ERROR("Error", "❌"),
    WARNING("Warning", "⚠️"),
    INFO("Info", "ℹ️"),
}
