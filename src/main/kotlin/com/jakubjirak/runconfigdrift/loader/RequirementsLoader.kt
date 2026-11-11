package com.jakubjirak.runconfigdrift.loader

import com.jakubjirak.runconfigdrift.model.RunRequirements
import com.jakubjirak.runconfigdrift.settings.DriftDetectorSettings
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.yaml.snakeyaml.Yaml
import java.io.File

/**
 * Parses .run-requirements.yaml and produces a RunRequirements model.
 */
object RequirementsLoader {

    private val LOG = Logger.getInstance(RequirementsLoader::class.java)
    const val DEFAULT_FILENAME = ".run-requirements.yaml"

    fun loadFromProject(project: Project): RunRequirements? {
        val specFile = specFileFor(project) ?: return null
        if (!specFile.exists()) {
            LOG.info("No requirements file found at: ${specFile.absolutePath}")
            return null
        }
        return parseFile(specFile)
    }

    fun findSpecFile(project: Project): VirtualFile? {
        val specFile = specFileFor(project) ?: return null
        if (!specFile.exists()) return null
        return com.intellij.openapi.vfs.LocalFileSystem.getInstance().findFileByIoFile(specFile)
    }

    private fun specFileFor(project: Project): File? {
        val baseDir = project.basePath ?: return null
        return resolveSpecFile(baseDir, DriftDetectorSettings.getInstance().specFilePath)
    }

    /**
     * Resolves the configured requirements file: relative paths are resolved against the project root,
     * absolute paths are used as-is and a blank setting falls back to [DEFAULT_FILENAME].
     */
    fun resolveSpecFile(projectBasePath: String, configuredPath: String): File {
        val configured = File(configuredPath.trim().ifEmpty { DEFAULT_FILENAME })
        return if (configured.isAbsolute) configured else File(projectBasePath, configured.path)
    }

    fun parseFile(file: File): RunRequirements? {
        if (!file.exists()) {
            LOG.info("Requirements file not found: ${file.absolutePath}")
            return null
        }
        return try {
            parseYaml(file.readText())
        } catch (e: Exception) {
            LOG.warn("Failed to parse requirements file: ${file.absolutePath}", e)
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun parseYaml(yamlContent: String): RunRequirements? {
        return try {
            val yaml = Yaml()
            val data = yaml.load<Map<String, Any?>>(yamlContent) ?: return null
            mapToRequirements(data)
        } catch (e: Exception) {
            LOG.warn("Failed to parse requirements YAML content", e)
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun mapToRequirements(data: Map<String, Any?>): RunRequirements {
        val jdk = when (val jdkValue = data["jdk"]) {
            is Int -> jdkValue
            is String -> jdkValue.toIntOrNull()
            else -> null
        }

        val envVars = (data["env_vars"] as? List<Any?>)
            ?.filterIsInstance<String>()
            ?: emptyList()

        val ports = (data["ports"] as? List<Any?>)
            ?.mapNotNull { port ->
                when (port) {
                    is Int -> port
                    is String -> port.toIntOrNull()
                    else -> null
                }
            }
            ?: emptyList()

        val workingDir = data["working_dir"] as? String

        return RunRequirements(
            jdk = jdk,
            envVars = envVars,
            ports = ports,
            workingDir = workingDir,
        )
    }
}
