package com.jakubjirak.runconfigdrift.engine

import com.jakubjirak.runconfigdrift.analyzer.DriftAnalyzer
import com.jakubjirak.runconfigdrift.analyzer.EnvVarAnalyzer
import com.jakubjirak.runconfigdrift.analyzer.JdkAnalyzer
import com.jakubjirak.runconfigdrift.analyzer.WorkingDirAnalyzer
import com.jakubjirak.runconfigdrift.model.DriftIssue
import com.jakubjirak.runconfigdrift.model.DriftSeverity
import com.jakubjirak.runconfigdrift.model.HealthReport
import com.jakubjirak.runconfigdrift.model.RunRequirements
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import java.time.LocalDateTime

/**
 * Orchestrates all drift analyzers and produces a HealthReport.
 */
object DriftDetectionEngine {

    private val LOG = Logger.getInstance(DriftDetectionEngine::class.java)

    private val analyzers: List<DriftAnalyzer> = listOf(
        JdkAnalyzer(),
        EnvVarAnalyzer(),
        WorkingDirAnalyzer(),
    )

    fun analyze(project: Project, requirements: RunRequirements): HealthReport {
        val issues = mutableListOf<DriftIssue>()

        for (analyzer in analyzers) {
            try {
                issues.addAll(analyzer.analyze(project, requirements))
            } catch (e: Exception) {
                LOG.warn("Analyzer ${analyzer.javaClass.simpleName} failed", e)
            }
        }

        val score = calculateScore(issues)
        return HealthReport(
            issues = issues.toList(),
            score = score,
            timestamp = LocalDateTime.now(),
        )
    }

    fun analyzeWithCustomAnalyzers(
        project: Project,
        requirements: RunRequirements,
        customAnalyzers: List<DriftAnalyzer>,
    ): HealthReport {
        val issues = mutableListOf<DriftIssue>()

        for (analyzer in customAnalyzers) {
            try {
                issues.addAll(analyzer.analyze(project, requirements))
            } catch (e: Exception) {
                LOG.warn("Analyzer ${analyzer.javaClass.simpleName} failed", e)
            }
        }

        val score = calculateScore(issues)
        return HealthReport(
            issues = issues.toList(),
            score = score,
            timestamp = LocalDateTime.now(),
        )
    }

    fun calculateScore(issues: List<DriftIssue>): Int {
        if (issues.isEmpty()) return 100

        var deductions = 0
        for (issue in issues) {
            deductions += when (issue.severity) {
                DriftSeverity.ERROR -> ERROR_DEDUCTION
                DriftSeverity.WARNING -> WARNING_DEDUCTION
                DriftSeverity.INFO -> INFO_DEDUCTION
            }
        }

        return maxOf(0, 100 - deductions)
    }

    private const val ERROR_DEDUCTION = 25
    private const val WARNING_DEDUCTION = 10
    private const val INFO_DEDUCTION = 5
}
