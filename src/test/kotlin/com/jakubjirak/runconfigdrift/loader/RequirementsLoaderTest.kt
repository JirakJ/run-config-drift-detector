package com.jakubjirak.runconfigdrift.loader

import com.jakubjirak.runconfigdrift.model.RunRequirements
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class RequirementsLoaderTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `parse valid requirements with all fields`() {
        val yaml = """
            jdk: 17
            env_vars:
              - DATABASE_URL
              - API_KEY
              - SECRET_TOKEN
            ports:
              - 8080
              - 5432
            working_dir: "."
        """.trimIndent()

        val requirements = RequirementsLoader.parseYaml(yaml)

        assertNotNull(requirements)
        assertEquals(17, requirements!!.jdk)
        assertEquals(3, requirements.envVars.size)
        assertTrue(requirements.envVars.contains("DATABASE_URL"))
        assertTrue(requirements.envVars.contains("API_KEY"))
        assertTrue(requirements.envVars.contains("SECRET_TOKEN"))
        assertEquals(2, requirements.ports.size)
        assertTrue(requirements.ports.contains(8080))
        assertTrue(requirements.ports.contains(5432))
        assertEquals(".", requirements.workingDir)
    }

    @Test
    fun `parse requirements with only jdk`() {
        val yaml = """
            jdk: 21
        """.trimIndent()

        val requirements = RequirementsLoader.parseYaml(yaml)

        assertNotNull(requirements)
        assertEquals(21, requirements!!.jdk)
        assertTrue(requirements.envVars.isEmpty())
        assertTrue(requirements.ports.isEmpty())
        assertNull(requirements.workingDir)
    }

    @Test
    fun `parse requirements with only env vars`() {
        val yaml = """
            env_vars:
              - MY_VAR
        """.trimIndent()

        val requirements = RequirementsLoader.parseYaml(yaml)

        assertNotNull(requirements)
        assertNull(requirements!!.jdk)
        assertEquals(1, requirements.envVars.size)
        assertEquals("MY_VAR", requirements.envVars[0])
    }

    @Test
    fun `parse requirements with string jdk version`() {
        val yaml = """
            jdk: "17"
        """.trimIndent()

        val requirements = RequirementsLoader.parseYaml(yaml)

        assertNotNull(requirements)
        assertEquals(17, requirements!!.jdk)
    }

    @Test
    fun `parse empty yaml returns empty requirements`() {
        val yaml = "{}"

        val requirements = RequirementsLoader.parseYaml(yaml)

        assertNotNull(requirements)
        assertNull(requirements!!.jdk)
        assertTrue(requirements.envVars.isEmpty())
        assertTrue(requirements.ports.isEmpty())
        assertNull(requirements.workingDir)
    }

    @Test
    fun `parse malformed yaml returns null`() {
        val yaml = "not: valid: yaml: {{{{"

        val requirements = RequirementsLoader.parseYaml(yaml)

        assertNull(requirements)
    }

    @Test
    fun `parse file from disk`() {
        val specFile = tempDir.resolve(".run-requirements.yaml").apply {
            writeText("""
                jdk: 11
                env_vars:
                  - HOME
                ports:
                  - 3000
                working_dir: "src"
            """.trimIndent())
        }

        val requirements = RequirementsLoader.parseFile(specFile)

        assertNotNull(requirements)
        assertEquals(11, requirements!!.jdk)
        assertEquals(listOf("HOME"), requirements.envVars)
        assertEquals(listOf(3000), requirements.ports)
        assertEquals("src", requirements.workingDir)
    }

    @Test
    fun `parse non-existent file returns null`() {
        val result = RequirementsLoader.parseFile(tempDir.resolve("does-not-exist.yaml"))

        assertNull(result)
    }

    @Test
    fun `parse requirements with empty env vars list`() {
        val yaml = """
            jdk: 17
            env_vars: []
            ports: []
        """.trimIndent()

        val requirements = RequirementsLoader.parseYaml(yaml)

        assertNotNull(requirements)
        assertEquals(17, requirements!!.jdk)
        assertTrue(requirements.envVars.isEmpty())
        assertTrue(requirements.ports.isEmpty())
    }

    @Test
    fun `parse requirements with working dir subdirectory`() {
        val yaml = """
            working_dir: "backend/app"
        """.trimIndent()

        val requirements = RequirementsLoader.parseYaml(yaml)

        assertNotNull(requirements)
        assertEquals("backend/app", requirements!!.workingDir)
    }
}
