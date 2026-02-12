package com.jakubjirak.runconfigdrift.licensing

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service

/**
 * Central gate for freemium feature access.
 * Free tier: basic drift detection with up to 10 requirements.
 * Paid tier: auto-fix, team requirements sync, CI export, unlimited requirements.
 */
@Service(Service.Level.APP)
class FreemiumGate {

    enum class LicenseStatus {
        FREE, TRIAL, PAID, EXPIRED
    }

    /**
     * Check if a paid feature is available for the current license.
     */
    @Suppress("UNUSED_PARAMETER")
    fun isPaidFeatureAvailable(featureId: String): Boolean {
        val status = getLicenseStatus()
        return status == LicenseStatus.PAID || status == LicenseStatus.TRIAL
    }

    fun getLicenseStatus(): LicenseStatus {
        // TODO: Integrate with JetBrains Marketplace Licensing API
        return LicenseStatus.FREE
    }

    fun getMaxRequirements(): Int {
        return when (getLicenseStatus()) {
            LicenseStatus.FREE -> MAX_FREE_REQUIREMENTS
            LicenseStatus.EXPIRED -> MAX_FREE_REQUIREMENTS
            LicenseStatus.TRIAL, LicenseStatus.PAID -> Int.MAX_VALUE
        }
    }

    companion object {
        const val MAX_FREE_REQUIREMENTS = 10

        // Feature IDs
        const val FEATURE_AUTO_FIX = "auto_fix"
        const val FEATURE_TEAM_REQUIREMENTS = "team_requirements"
        const val FEATURE_CI_EXPORT = "ci_export"
        const val FEATURE_UNLIMITED_REQUIREMENTS = "unlimited_requirements"

        fun getInstance(): FreemiumGate =
            ApplicationManager.getApplication().getService(FreemiumGate::class.java)
    }
}
