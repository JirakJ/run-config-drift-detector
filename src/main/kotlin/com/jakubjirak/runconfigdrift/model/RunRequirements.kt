package com.jakubjirak.runconfigdrift.model

/**
 * Parsed project requirements from .run-requirements.yaml.
 */
data class RunRequirements(
    val jdk: Int? = null,
    val envVars: List<String> = emptyList(),
    val ports: List<Int> = emptyList(),
    val workingDir: String? = null,
)
