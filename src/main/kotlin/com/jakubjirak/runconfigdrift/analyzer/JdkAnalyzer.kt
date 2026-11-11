package com.jakubjirak.runconfigdrift.analyzer

import com.jakubjirak.runconfigdrift.model.DriftIssue
import com.jakubjirak.runconfigdrift.model.DriftSeverity
import com.jakubjirak.runconfigdrift.model.DriftType
import com.jakubjirak.runconfigdrift.model.RunRequirements
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager

/**
 * Checks the project SDK version against the JDK requirement.
 */
class JdkAnalyzer : DriftAnalyzer {

    override fun analyze(project: Project, requirements: RunRequirements): List<DriftIssue> {
        val requiredJdk = requirements.jdk ?: return emptyList()
        val projectSdk = ProjectRootManager.getInstance(project).projectSdk
        return check(requiredJdk, sdkConfigured = projectSdk != null, versionString = projectSdk?.versionString)
    }

    companion object {
        private val VERSION_PATTERN = Regex("""(\d+)(?:\.\d+)*""")

        /**
         * Compares the project SDK against [requiredJdk]. An unparseable version string is not reported
         * (it may be a non-Java SDK name), a missing SDK is.
         */
        fun check(requiredJdk: Int, sdkConfigured: Boolean, versionString: String?): List<DriftIssue> {
            if (!sdkConfigured) {
                return listOf(
                    DriftIssue(
                        type = DriftType.JDK_MISMATCH,
                        severity = DriftSeverity.ERROR,
                        description = "No project SDK configured. Required: JDK $requiredJdk",
                        fixSuggestion = "Configure a JDK $requiredJdk in Project Structure > SDKs",
                    ),
                )
            }
            val detected = extractMajorVersion(versionString.orEmpty())
            if (detected == null || detected == requiredJdk) return emptyList()
            return listOf(
                DriftIssue(
                    type = DriftType.JDK_MISMATCH,
                    severity = DriftSeverity.ERROR,
                    description = "JDK version mismatch: required $requiredJdk, found $detected ($versionString)",
                    fixSuggestion = "Change project SDK to JDK $requiredJdk in Project Structure > SDKs",
                ),
            )
        }

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
