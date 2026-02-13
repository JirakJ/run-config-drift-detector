package com.jakubjirak.runconfigdrift.git

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BranchConfigManagerTest {

    @Test
    fun `exact branch name matches exact pattern`() {
        assertTrue(BranchConfigManager.matchesBranchPattern("main", "main"))
    }

    @Test
    fun `feature branch matches wildcard pattern`() {
        assertTrue(BranchConfigManager.matchesBranchPattern("feature/login", "feature/*"))
    }

    @Test
    fun `release branch matches wildcard pattern`() {
        assertTrue(BranchConfigManager.matchesBranchPattern("release/1.0.0", "release/*"))
    }

    @Test
    fun `main branch does not match feature pattern`() {
        assertFalse(BranchConfigManager.matchesBranchPattern("main", "feature/*"))
    }

    @Test
    fun `hotfix branch matches hotfix pattern`() {
        assertTrue(BranchConfigManager.matchesBranchPattern("hotfix/urgent-fix", "hotfix/*"))
    }

    @Test
    fun `develop matches exact pattern`() {
        assertTrue(BranchConfigManager.matchesBranchPattern("develop", "develop"))
    }

    @Test
    fun `empty branch name does not match non-empty pattern`() {
        assertFalse(BranchConfigManager.matchesBranchPattern("", "main"))
    }

    @Test
    fun `wildcard matches any branch`() {
        assertTrue(BranchConfigManager.matchesBranchPattern("anything", "*"))
    }

    @Test
    fun `nested feature branch matches nested pattern`() {
        assertTrue(BranchConfigManager.matchesBranchPattern("feature/team/login", "feature/*/*"))
    }

    @Test
    fun `pattern with dot is escaped correctly`() {
        assertTrue(BranchConfigManager.matchesBranchPattern("release/v1.0", "release/v1.0"))
        assertFalse(BranchConfigManager.matchesBranchPattern("release/v1X0", "release/v1.0"))
    }

    @Test
    fun `BranchConfig model properties`() {
        val config = BranchConfig(
            branchPattern = "feature/*",
            configNames = listOf("debug", "test"),
            autoApply = true,
        )

        assertEquals("feature/*", config.branchPattern)
        assertEquals(listOf("debug", "test"), config.configNames)
        assertTrue(config.autoApply)
    }

    @Test
    fun `BranchConfig defaults autoApply to true`() {
        val config = BranchConfig(
            branchPattern = "main",
            configNames = listOf("prod"),
        )

        assertTrue(config.autoApply)
    }
}
