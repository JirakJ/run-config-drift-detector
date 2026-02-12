package com.jakubjirak.runconfigdrift.analyzer

import com.jakubjirak.runconfigdrift.model.DriftIssue
import com.jakubjirak.runconfigdrift.model.RunRequirements
import com.intellij.openapi.project.Project

/**
 * Common interface for all drift analyzers.
 * Each analyzer checks a specific aspect of the project configuration.
 */
interface DriftAnalyzer {

    /**
     * Analyze the project against the given requirements and return any drift issues found.
     */
    fun analyze(project: Project, requirements: RunRequirements): List<DriftIssue>
}
