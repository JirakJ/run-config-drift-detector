package com.jakubjirak.runconfigdrift.git

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import java.time.Instant

/**
 * Tracks run configuration changes and creates versioned snapshots.
 * Each change is recorded with a timestamp and optional commit hash for traceability.
 */
data class ConfigSnapshot(
    val configName: String,
    val content: String,
    val timestamp: Instant,
    val commitHash: String?,
)

/**
 * Represents a versioned configuration entry with a description of what changed.
 */
data class ConfigVersion(
    val version: Int,
    val snapshot: ConfigSnapshot,
    val changeDescription: String,
)

@Service(Service.Level.PROJECT)
class ConfigVersioningService(
    @Suppress("UNUSED_PARAMETER") private val project: Project,
) {

    private val snapshots = mutableMapOf<String, MutableList<ConfigVersion>>()
    private val versionCounters = mutableMapOf<String, Int>()

    fun createSnapshot(configName: String, content: String, commitHash: String? = null): ConfigSnapshot {
        val snapshot = ConfigSnapshot(
            configName = configName,
            content = content,
            timestamp = Instant.now(),
            commitHash = commitHash,
        )
        LOG.info("Created snapshot for config: $configName")
        return snapshot
    }

    fun addVersion(configName: String, content: String, changeDescription: String, commitHash: String? = null) {
        val snapshot = createSnapshot(configName, content, commitHash)
        val nextVersion = versionCounters.getOrDefault(configName, 0) + 1
        versionCounters[configName] = nextVersion

        val version = ConfigVersion(
            version = nextVersion,
            snapshot = snapshot,
            changeDescription = changeDescription,
        )

        snapshots.getOrPut(configName) { mutableListOf() }.add(version)
        LOG.info("Added version $nextVersion for config: $configName")
    }

    fun getVersionHistory(configName: String): List<ConfigVersion> =
        snapshots[configName]?.toList() ?: emptyList()

    fun getLatestVersion(configName: String): ConfigVersion? =
        snapshots[configName]?.lastOrNull()

    fun getVersionCount(configName: String): Int =
        versionCounters.getOrDefault(configName, 0)

    fun getAllTrackedConfigs(): Set<String> = snapshots.keys.toSet()

    companion object {
        private val LOG = Logger.getInstance(ConfigVersioningService::class.java)
    }
}
