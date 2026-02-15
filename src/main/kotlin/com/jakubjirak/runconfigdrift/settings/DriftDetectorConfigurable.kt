package com.jakubjirak.runconfigdrift.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.dsl.builder.*

/**
 * Settings UI for Run Config Drift Detector under Tools menu.
 */
class DriftDetectorConfigurable : BoundConfigurable("Run Config Drift Detector") {

    override fun createPanel() = panel {
        val settings = DriftDetectorSettings.getInstance()

        group("General") {
            row {
                checkBox("Enable drift detection")
                    .bindSelected(settings::enabled)
                    .comment("When enabled, the plugin will detect drift between run configurations and the spec file.")
            }
            row {
                checkBox("Auto-scan on project open")
                    .bindSelected(settings::autoScanOnStartup)
                    .comment("Automatically scan for configuration drift when a project is opened.")
            }
        }

        group("Configuration") {
            row("Requirements file path:") {
                cell(TextFieldWithBrowseButton().apply {
                    addBrowseFolderListener("Select Requirements File", null, null,
                        FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor())
                })
                    .bindText(settings::specFilePath)
                    .align(Align.FILL)
                    .comment("Path to the YAML file that defines expected run configuration requirements (e.g. .run-requirements.yaml).")
            }
        }
    }
}
