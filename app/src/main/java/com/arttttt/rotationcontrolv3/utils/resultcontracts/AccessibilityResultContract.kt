package com.arttttt.rotationcontrolv3.utils.resultcontracts

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import com.arttttt.rotationcontrolv3.RotationControlAccessibilityService
import com.arttttt.rotationcontrolv3.utils.extensions.isAccessibilityServiceEnabled

class AccessibilityResultContract(
    private val contextProvider: () -> Context,
) : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return contextProvider
            .invoke()
            .isAccessibilityServiceEnabled<RotationControlAccessibilityService>()
    }
}