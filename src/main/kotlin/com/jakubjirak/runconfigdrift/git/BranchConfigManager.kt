package com.jakubjirak.runconfigdrift.git

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project

/**
 * Associates run configurations with specific branch patterns using glob matching.
 */
data class BranchConfig(
    val branchPattern: String,
    val configNames: List<String>,
    val autoApply: Boolean = true,
)

/**
 * Manages branch-specific run configuration mappings.
 * When the active Git branch changes, it resolves which configs should be active
 * and optionally auto-applies them.
 */
@Service(Service.Level.PROJECT)
class BranchConfigManager(
    @Suppress("UNUSED_PARAMETER") private val project: Project,
) {

    private val branchMappings = mutableListOf<BranchConfig>()
    private var defaultConfigs: List<String> = emptyList()

    fun addMapping(branchConfig: BranchConfig) {
        branchMappings.add(branchConfig)
        LOG.info("Added branch mapping: ${branchConfig.branchPattern} -> ${branchConfig.configNames}")
    }

    fun removeMapping(branchPattern: String) {
        branchMappings.removeAll { it.branchPattern == branchPattern }
        LOG.info("Removed branch mapping for pattern: $branchPattern")
    }

    fun setDefaultConfigs(configs: List<String>) {
        defaultConfigs = configs.toList()
    }

    fun getDefaultConfigs(): List<String> = defaultConfigs

    fun resolveConfigsForBranch(branchName: String): List<String> {
        val matching = branchMappings.find { matchesBranchPattern(branchName, it.branchPattern) }
        return matching?.configNames ?: defaultConfigs
    }

    fun shouldAutoApply(branchName: String): Boolean {
        val matching = branchMappings.find { matchesBranchPattern(branchName, it.branchPattern) }
        return matching?.autoApply ?: false
    }

    fun getAllMappings(): List<BranchConfig> = branchMappings.toList()

    fun onBranchChanged(branchName: String): List<String> {
        val configs = resolveConfigsForBranch(branchName)
        LOG.info("Branch changed to '$branchName', resolved configs: $configs")
        return configs
    }

    companion object {
        private val LOG = Logger.getInstance(BranchConfigManager::class.java)

        fun matchesBranchPattern(branchName: String, pattern: String): Boolean {
            val regexPattern = pattern
                .replace(".", "\\.")
                .replace("*", ".*")
                .replace("?", ".")
            return branchName.matches(Regex(regexPattern))
        }
    }
}
