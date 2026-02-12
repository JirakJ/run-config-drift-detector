package com.jakubjirak.runconfigdrift.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Persistent settings for Run Config Drift Detector.
 */
@Service(Service.Level.APP)
@State(
    name = "DriftDetectorSettings",
    storages = [Storage("DriftDetectorSettings.xml")],
)
class DriftDetectorSettings : PersistentStateComponent<DriftDetectorSettings.State> {

    data class State(
        var enabled: Boolean = true,
        var specFilePath: String = ".run-requirements.yaml",
        var autoScanOnStartup: Boolean = true,
    )

    private var myState = State()

    var enabled: Boolean
        get() = myState.enabled
        set(value) { myState.enabled = value }

    var specFilePath: String
        get() = myState.specFilePath
        set(value) { myState.specFilePath = value }

    var autoScanOnStartup: Boolean
        get() = myState.autoScanOnStartup
        set(value) { myState.autoScanOnStartup = value }

    override fun getState(): State = myState

    override fun loadState(state: State) {
        myState = state
    }

    companion object {
        fun getInstance(): DriftDetectorSettings =
            ApplicationManager.getApplication().getService(DriftDetectorSettings::class.java)
    }
}
