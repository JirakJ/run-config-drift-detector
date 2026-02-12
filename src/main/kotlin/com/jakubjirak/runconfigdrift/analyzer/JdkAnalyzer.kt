package com.jakubjirak.runconfigdrift.analyzer

import com.jakubjirak.runconfigdrift.model.DriftIssue
import com.jakubjirak.runconfigdrift.model.DriftSeverity
import com.jakubjirak.runconfigdrift.model.DriftType
import com.jakubjirak.runconfigdrift.model.RunRequirements
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager

/**
 * Checks the project SDK version against the JDK requirement.
 */
class JdkAnalyzer : DriftAnalyzer {

    override fun analyze(project: Project, requirements: RunRequirements): List<DriftIssue> {
        val requiredJdk = requirements.jdk ?: return emptyList()

        return try {
            val projectSdk = ProjectRootManager.getInstance(project).projectSdk
            if (projectSdk == null) {
                return listOf(
                    DriftIssue(
                        type = DriftType.JDK_MISMATCH,
                        severity = DriftSeverity.ERROR,
                        description = "No project SDK configured. Required: JDK $requiredJdk",
                        fixSuggestion = "Configure a JDK $requiredJdk in Project Structure > SDKs",
                    )
                )
            }

            val versionString = projectSdk.versionString ?: ""
            val detectedVersion = extractMajorVersion(versionString)

            if (detectedVersion != null && detectedVersion != requiredJdk) {
                listOf(
                    DriftIssue(
                        type = DriftType.JDK_MISMATCH,
                        severity = DriftSeverity.ERROR,
                        description = "JDK version mismatch: required $requiredJdk, found $detectedVersion ($versionString)",
                        fixSuggestion = "Change project SDK to JDK $requiredJdk in Project Structure > SDKs",
                    )
                )
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            LOG.warn("Failed to analyze JDK version", e)
            emptyList()
        }
    }

    companion object {
        private val LOG = Logger.getInstance(JdkAnalyzer::class.java)
        private val VERSION_PATTERN = Regex("""(\d+)(?:\.\d+)*""")

        fun extractMajorVersion(versionString: String): Int? {
            val match = VERSION_PATTERN.find(versionString) ?: return null
            val major = match.groupValues[1].toIntOrNull() ?: return null
            // JDK 1.x versions: major version is the minor (e.g., 1.8 -> 8)
            return if (major == 1) {
                val fullMatch = match.value
                val parts = fullMatch.split(".")
                if (parts.size >= 2) parts[1].toIntOrNull() ?: major else major
            } else {
                major
            }
        }
    }
}
