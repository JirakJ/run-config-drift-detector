package com.jakubjirak.runconfigdrift.git

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import java.io.File
import java.time.Instant

/**
 * Represents a shared run configuration that can be synced via Git.
 */
data class SharedConfig(
    val name: String,
    val type: String,
    val content: String,
    val author: String,
    val lastSync: Instant,
)

/**
 * Represents the result of a sync conflict detection.
 */
data class SyncConflict(
    val configName: String,
    val localContent: String,
    val remoteContent: String,
    val localAuthor: String,
    val remoteAuthor: String,
)

/**
 * Exports and imports run configurations to/from a `.shared-configs/` directory
 * that is tracked by Git, enabling team-wide sharing and conflict detection.
 */
@Service(Service.Level.PROJECT)
class TeamConfigSync(
    @Suppress("UNUSED_PARAMETER") private val project: Project,
) {

    private val sharedConfigs = mutableMapOf<String, SharedConfig>()

    fun exportConfig(
        name: String,
        type: String,
        content: String,
        author: String,
        targetDir: File,
    ): SharedConfig {
        val sharedDir = File(targetDir, SHARED_CONFIGS_DIR)
        if (!sharedDir.exists()) {
            sharedDir.mkdirs()
        }

        val configFile = File(sharedDir, "$name.xml")
        configFile.writeText(content)

        val config = SharedConfig(
            name = name,
            type = type,
            content = content,
            author = author,
            lastSync = Instant.now(),
        )
        sharedConfigs[name] = config
        LOG.info("Exported config '$name' to ${configFile.absolutePath}")
        return config
    }

    fun importConfig(name: String, sourceDir: File): SharedConfig? {
        val configFile = File(sourceDir, "$SHARED_CONFIGS_DIR/$name.xml")
        if (!configFile.exists()) {
            LOG.warn("Config file not found: ${configFile.absolutePath}")
            return null
        }

        val content = configFile.readText()
        val config = SharedConfig(
            name = name,
            type = "imported",
            content = content,
            author = "unknown",
            lastSync = Instant.now(),
        )
        sharedConfigs[name] = config
        LOG.info("Imported config '$name' from ${configFile.absolutePath}")
        return config
    }

    fun detectConflicts(localConfigs: Map<String, SharedConfig>, remoteDir: File): List<SyncConflict> {
        val conflicts = mutableListOf<SyncConflict>()
        val sharedDir = File(remoteDir, SHARED_CONFIGS_DIR)
        if (!sharedDir.exists()) return conflicts

        for ((name, local) in localConfigs) {
            val remoteFile = File(sharedDir, "$name.xml")
            if (remoteFile.exists()) {
                val remoteContent = remoteFile.readText()
                if (remoteContent != local.content) {
                    conflicts.add(
                        SyncConflict(
                            configName = name,
                            localContent = local.content,
                            remoteContent = remoteContent,
                            localAuthor = local.author,
                            remoteAuthor = "remote",
                        ),
                    )
                }
            }
        }
        LOG.info("Detected ${conflicts.size} conflicts")
        return conflicts
    }

    fun getSharedConfigs(): Map<String, SharedConfig> = sharedConfigs.toMap()

    companion object {
        private val LOG = Logger.getInstance(TeamConfigSync::class.java)
        const val SHARED_CONFIGS_DIR = ".shared-configs"
    }
}
