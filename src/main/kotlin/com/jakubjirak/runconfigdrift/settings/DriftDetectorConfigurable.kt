package com.jakubjirak.runconfigdrift.settings

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Settings UI for Run Config Drift Detector under Tools menu.
 */
class DriftDetectorConfigurable : Configurable {

    private var enabledCheckBox: JBCheckBox? = null
    private var specFilePathField: JBTextField? = null
    private var autoScanCheckBox: JBCheckBox? = null
    private var mainPanel: JPanel? = null

    override fun getDisplayName(): String = "Run Config Drift Detector"

    override fun createComponent(): JComponent {
        val settings = DriftDetectorSettings.getInstance()

        enabledCheckBox = JBCheckBox("Enable drift detection", settings.enabled)
        specFilePathField = JBTextField(settings.specFilePath)
        autoScanCheckBox = JBCheckBox("Auto-scan on project open", settings.autoScanOnStartup)

        mainPanel = FormBuilder.createFormBuilder()
            .addComponent(enabledCheckBox!!)
            .addVerticalGap(10)
            .addLabeledComponent(JBLabel("Requirements file path:"), specFilePathField!!)
            .addVerticalGap(10)
            .addComponent(autoScanCheckBox!!)
            .addComponentFillVertically(JPanel(), 0)
            .panel

        return mainPanel!!
    }

    override fun isModified(): Boolean {
        val settings = DriftDetectorSettings.getInstance()
        return enabledCheckBox?.isSelected != settings.enabled ||
            specFilePathField?.text != settings.specFilePath ||
            autoScanCheckBox?.isSelected != settings.autoScanOnStartup
    }

    override fun apply() {
        val settings = DriftDetectorSettings.getInstance()
        settings.enabled = enabledCheckBox?.isSelected ?: true
        settings.specFilePath = specFilePathField?.text ?: ".run-requirements.yaml"
        settings.autoScanOnStartup = autoScanCheckBox?.isSelected ?: true
    }

    override fun reset() {
        val settings = DriftDetectorSettings.getInstance()
        enabledCheckBox?.isSelected = settings.enabled
        specFilePathField?.text = settings.specFilePath
        autoScanCheckBox?.isSelected = settings.autoScanOnStartup
    }

    override fun disposeUIResources() {
        enabledCheckBox = null
        specFilePathField = null
        autoScanCheckBox = null
        mainPanel = null
    }
}
