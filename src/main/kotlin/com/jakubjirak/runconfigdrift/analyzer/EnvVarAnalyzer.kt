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
class EnvVarAnalyzer(
    private val environment: () -> Map<String, String> = System::getenv,
) : DriftAnalyzer {

    override fun analyze(project: Project, requirements: RunRequirements): List<DriftIssue> {
        if (requirements.envVars.isEmpty()) return emptyList()
        return try {
            check(requirements.envVars, environment())
        } catch (e: SecurityException) {
            LOG.warn("Failed to read environment variables", e)
            emptyList()
        }
    }

    companion object {
        private val LOG = Logger.getInstance(EnvVarAnalyzer::class.java)

        /** One WARNING per required variable that is absent from [env] (duplicates reported once). */
        fun check(required: List<String>, env: Map<String, String>): List<DriftIssue> =
            required.distinct().filterNot { env.containsKey(it) }.map { varName ->
                DriftIssue(
                    type = DriftType.MISSING_ENV_VAR,
                    severity = DriftSeverity.WARNING,
                    description = "Missing environment variable: $varName",
                    fixSuggestion = "Set '$varName' in your run configuration or system environment",
                )
            }
    }
}
