package com.jakubjirak.runconfigdrift.analyzer

import com.jakubjirak.runconfigdrift.model.DriftIssue
import com.jakubjirak.runconfigdrift.model.DriftSeverity
import com.jakubjirak.runconfigdrift.model.DriftType
import com.jakubjirak.runconfigdrift.model.RunRequirements
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import java.io.File

/**
 * Validates that the expected working directory exists and is correctly configured.
 */
class WorkingDirAnalyzer : DriftAnalyzer {

    override fun analyze(project: Project, requirements: RunRequirements): List<DriftIssue> {
        val expectedDir = requirements.workingDir ?: return emptyList()
        val basePath = project.basePath ?: return emptyList()

        return try {
            val resolvedDir = if (expectedDir == ".") {
                File(basePath)
            } else {
                val candidate = File(expectedDir)
                if (candidate.isAbsolute) candidate else File(basePath, expectedDir)
            }

            if (!resolvedDir.exists()) {
                listOf(
                    DriftIssue(
                        type = DriftType.WORKING_DIR_DRIFT,
                        severity = DriftSeverity.ERROR,
                        description = "Working directory does not exist: ${resolvedDir.absolutePath}",
                        fixSuggestion = "Create the directory or update working_dir in .run-requirements.yaml",
                    )
                )
            } else if (!resolvedDir.isDirectory) {
                listOf(
                    DriftIssue(
                        type = DriftType.WORKING_DIR_DRIFT,
                        severity = DriftSeverity.ERROR,
                        description = "Working directory path is not a directory: ${resolvedDir.absolutePath}",
                        fixSuggestion = "Update working_dir in .run-requirements.yaml to point to a valid directory",
                    )
                )
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            LOG.warn("Failed to analyze working directory", e)
            emptyList()
        }
    }

    companion object {
        private val LOG = Logger.getInstance(WorkingDirAnalyzer::class.java)
    }
}
