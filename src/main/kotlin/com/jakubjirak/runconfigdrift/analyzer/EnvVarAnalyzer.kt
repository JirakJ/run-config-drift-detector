package com.jakubjirak.runconfigdrift.analyzer

import com.jakubjirak.runconfigdrift.model.DriftIssue
import com.jakubjirak.runconfigdrift.model.DriftSeverity
import com.jakubjirak.runconfigdrift.model.DriftType
import com.jakubjirak.runconfigdrift.model.RunRequirements
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project

/**
 * Checks if required environment variables are set in the system environment.
 */
class EnvVarAnalyzer : DriftAnalyzer {

    override fun analyze(project: Project, requirements: RunRequirements): List<DriftIssue> {
        if (requirements.envVars.isEmpty()) return emptyList()

        return try {
            val systemEnv = System.getenv()
            requirements.envVars.mapNotNull { varName ->
                if (!systemEnv.containsKey(varName)) {
                    DriftIssue(
                        type = DriftType.MISSING_ENV_VAR,
                        severity = DriftSeverity.WARNING,
                        description = "Missing environment variable: $varName",
                        fixSuggestion = "Set '$varName' in your run configuration or system environment",
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            LOG.warn("Failed to analyze environment variables", e)
            emptyList()
        }
    }

    companion object {
        private val LOG = Logger.getInstance(EnvVarAnalyzer::class.java)
    }
}
