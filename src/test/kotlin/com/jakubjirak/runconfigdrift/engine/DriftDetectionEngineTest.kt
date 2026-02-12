package com.jakubjirak.runconfigdrift.engine

import com.jakubjirak.runconfigdrift.model.DriftIssue
import com.jakubjirak.runconfigdrift.model.DriftSeverity
import com.jakubjirak.runconfigdrift.model.DriftType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DriftDetectionEngineTest {

    @Test
    fun `calculate score with no issues returns 100`() {
        val score = DriftDetectionEngine.calculateScore(emptyList())

        assertEquals(100, score)
    }

    @Test
    fun `calculate score with one error deducts 25`() {
        val issues = listOf(
            DriftIssue(DriftType.JDK_MISMATCH, DriftSeverity.ERROR, "test", "fix"),
        )

        val score = DriftDetectionEngine.calculateScore(issues)

        assertEquals(75, score)
    }

    @Test
    fun `calculate score with one warning deducts 10`() {
        val issues = listOf(
            DriftIssue(DriftType.MISSING_ENV_VAR, DriftSeverity.WARNING, "test", "fix"),
        )

        val score = DriftDetectionEngine.calculateScore(issues)

        assertEquals(90, score)
    }

    @Test
    fun `calculate score with one info deducts 5`() {
        val issues = listOf(
            DriftIssue(DriftType.MISSING_DEPENDENCY, DriftSeverity.INFO, "test", "fix"),
        )

        val score = DriftDetectionEngine.calculateScore(issues)

        assertEquals(95, score)
    }

    @Test
    fun `calculate score with mixed severities`() {
        val issues = listOf(
            DriftIssue(DriftType.JDK_MISMATCH, DriftSeverity.ERROR, "jdk", "fix jdk"),
            DriftIssue(DriftType.MISSING_ENV_VAR, DriftSeverity.WARNING, "env", "fix env"),
            DriftIssue(DriftType.MISSING_DEPENDENCY, DriftSeverity.INFO, "dep", "fix dep"),
        )

        // 100 - 25 - 10 - 5 = 60
        val score = DriftDetectionEngine.calculateScore(issues)

        assertEquals(60, score)
    }

    @Test
    fun `calculate score never goes below zero`() {
        val issues = (1..10).map {
            DriftIssue(DriftType.JDK_MISMATCH, DriftSeverity.ERROR, "error $it", "fix $it")
        }

        // 100 - (10 * 25) = -150, clamped to 0
        val score = DriftDetectionEngine.calculateScore(issues)

        assertEquals(0, score)
    }

    @Test
    fun `multiple warnings accumulate deductions`() {
        val issues = listOf(
            DriftIssue(DriftType.MISSING_ENV_VAR, DriftSeverity.WARNING, "var1", "fix"),
            DriftIssue(DriftType.MISSING_ENV_VAR, DriftSeverity.WARNING, "var2", "fix"),
            DriftIssue(DriftType.MISSING_ENV_VAR, DriftSeverity.WARNING, "var3", "fix"),
        )

        // 100 - (3 * 10) = 70
        val score = DriftDetectionEngine.calculateScore(issues)

        assertEquals(70, score)
    }

    @Test
    fun `score with all error types`() {
        val issues = listOf(
            DriftIssue(DriftType.JDK_MISMATCH, DriftSeverity.ERROR, "jdk", "fix"),
            DriftIssue(DriftType.WORKING_DIR_DRIFT, DriftSeverity.ERROR, "dir", "fix"),
        )

        // 100 - (2 * 25) = 50
        val score = DriftDetectionEngine.calculateScore(issues)

        assertEquals(50, score)
    }

    @Test
    fun `drift issue model properties`() {
        val issue = DriftIssue(
            type = DriftType.PORT_CONFLICT,
            severity = DriftSeverity.WARNING,
            description = "Port 8080 in use",
            fixSuggestion = "Kill the process using port 8080",
        )

        assertEquals(DriftType.PORT_CONFLICT, issue.type)
        assertEquals(DriftSeverity.WARNING, issue.severity)
        assertEquals("Port 8080 in use", issue.description)
        assertEquals("Kill the process using port 8080", issue.fixSuggestion)
    }
}
