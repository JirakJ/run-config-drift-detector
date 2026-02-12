package com.jakubjirak.runconfigdrift.model

/**
 * Types of drift that can be detected between project requirements and actual configuration.
 */
enum class DriftType(val displayName: String) {
    MISSING_ENV_VAR("Missing Environment Variable"),
    JDK_MISMATCH("JDK Version Mismatch"),
    WORKING_DIR_DRIFT("Working Directory Drift"),
    MISSING_DEPENDENCY("Missing Dependency"),
    PORT_CONFLICT("Port Conflict"),
}
