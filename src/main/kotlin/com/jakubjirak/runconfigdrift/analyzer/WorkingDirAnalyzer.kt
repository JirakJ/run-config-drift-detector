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
            check(basePath, expectedDir)
        } catch (e: SecurityException) {
            LOG.warn("Failed to analyze working directory", e)
            emptyList()
        }
    }

    companion object {
        private val LOG = Logger.getInstance(WorkingDirAnalyzer::class.java)

        /** Resolves [expectedDir] against [basePath] (unless absolute) and reports if it is missing or a file. */
        fun check(basePath: String, expectedDir: String): List<DriftIssue> {
            val candidate = File(expectedDir.trim().ifEmpty { "." })
            val resolvedDir = if (candidate.isAbsolute) candidate else File(basePath, candidate.path)
            val problem = when {
                !resolvedDir.exists() -> "Working directory does not exist: ${resolvedDir.absolutePath}" to
                    "Create the directory or update working_dir in .run-requirements.yaml"
                !resolvedDir.isDirectory -> "Working directory path is not a directory: ${resolvedDir.absolutePath}" to
                    "Update working_dir in .run-requirements.yaml to point to a valid directory"
                else -> return emptyList()
            }
            return listOf(
                DriftIssue(
                    type = DriftType.WORKING_DIR_DRIFT,
                    severity = DriftSeverity.ERROR,
                    description = problem.first,
                    fixSuggestion = problem.second,
                ),
            )
        }
    }
}
