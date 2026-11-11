package com.jakubjirak.runconfigdrift.analyzer

import com.jakubjirak.runconfigdrift.model.DriftSeverity
import com.jakubjirak.runconfigdrift.model.DriftType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class AnalyzersTest {

    // ---- environment variables ----

    @Test
    fun `missing env vars are reported once each as warnings`() {
        val issues = EnvVarAnalyzer.check(listOf("A", "B", "A", "C"), mapOf("B" to "1"))
        assertEquals(listOf("Missing environment variable: A", "Missing environment variable: C"), issues.map { it.description })
        assertTrue(issues.all { it.type == DriftType.MISSING_ENV_VAR && it.severity == DriftSeverity.WARNING })
    }

    @Test
    fun `present env vars including empty values are fine`() {
        assertTrue(EnvVarAnalyzer.check(listOf("A", "B"), mapOf("A" to "", "B" to "x")).isEmpty())
        assertTrue(EnvVarAnalyzer.check(emptyList(), emptyMap()).isEmpty())
    }

    // ---- working directory ----

    @Test
    fun `existing relative and dot working dirs pass`(@TempDir base: File) {
        File(base, "sub").mkdirs()
        assertTrue(WorkingDirAnalyzer.check(base.path, "sub").isEmpty())
        assertTrue(WorkingDirAnalyzer.check(base.path, ".").isEmpty())
        assertTrue(WorkingDirAnalyzer.check(base.path, " ").isEmpty())
    }

    @Test
    fun `absolute working dir is not resolved against the project`(@TempDir base: File, @TempDir other: File) {
        assertTrue(WorkingDirAnalyzer.check(base.path, other.absolutePath).isEmpty())
    }

    @Test
    fun `missing working dir is an error`(@TempDir base: File) {
        val issue = WorkingDirAnalyzer.check(base.path, "nope").single()
        assertEquals(DriftType.WORKING_DIR_DRIFT, issue.type)
        assertEquals(DriftSeverity.ERROR, issue.severity)
        assertTrue(issue.description.startsWith("Working directory does not exist"))
    }

    @Test
    fun `file instead of directory is an error`(@TempDir base: File) {
        File(base, "file.txt").writeText("x")
        val issue = WorkingDirAnalyzer.check(base.path, "file.txt").single()
        assertTrue(issue.description.startsWith("Working directory path is not a directory"))
    }

    // ---- JDK ----

    @Test
    fun `missing sdk is an error`() {
        val issue = JdkAnalyzer.check(17, sdkConfigured = false, versionString = null).single()
        assertEquals(DriftType.JDK_MISMATCH, issue.type)
        assertTrue(issue.description.contains("No project SDK configured"))
    }

    @Test
    fun `matching and mismatching jdk versions`() {
        assertTrue(JdkAnalyzer.check(17, true, "17.0.9").isEmpty())
        assertTrue(JdkAnalyzer.check(8, true, "1.8.0_392").isEmpty())
        val issue = JdkAnalyzer.check(21, true, "17.0.9").single()
        assertEquals("JDK version mismatch: required 21, found 17 (17.0.9)", issue.description)
    }

    @Test
    fun `unparseable sdk version is not reported`() {
        assertTrue(JdkAnalyzer.check(17, true, "custom-sdk").isEmpty())
        assertTrue(JdkAnalyzer.check(17, true, null).isEmpty())
    }

    @Test
    fun `extractMajorVersion handles legacy and modern schemes`() {
        assertEquals(8, JdkAnalyzer.extractMajorVersion("1.8.0_292"))
        assertEquals(11, JdkAnalyzer.extractMajorVersion("11.0.2"))
        assertEquals(21, JdkAnalyzer.extractMajorVersion("JetBrains Runtime 21"))
        assertEquals(1, JdkAnalyzer.extractMajorVersion("1"))
        assertNull(JdkAnalyzer.extractMajorVersion("no digits"))
    }
}
