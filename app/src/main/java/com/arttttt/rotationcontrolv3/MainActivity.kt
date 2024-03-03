package com.arttttt.rotationcontrolv3

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import androidx.core.content.IntentSanitizer
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.arttttt.rotationcontrolv3.ui.container.ContainerFragment
import com.arttttt.rotationcontrolv3.ui.rotation.RotationService
import com.arttttt.rotationcontrolv3.ui.rotation.view.RotationServiceViewImpl
import com.arttttt.rotationcontrolv3.utils.extensions.appComponent
import com.arttttt.rotationcontrolv3.utils.navigation.NavigationContainerDelegate

class MainActivity : AppCompatActivity() {

    companion object {

        const val LAUNCH_PAYLOAD = "launch_payload"
    }

    private val containerDelegate by lazy {
        NavigationContainerDelegate(
            context = this,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        containerDelegate.initialize(savedInstanceState)

        super.onCreate(savedInstanceState)

        setContentView(containerDelegate.createContainerView())

        intent?.let(::handleLaunchIntent)

        if (savedInstanceState != null) return

        supportFragmentManager.commit {
            replace<ContainerFragment>(
                containerDelegate.containerId,
                null,
                null,
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        containerDelegate.saveState(outState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let(::handleLaunchIntent)
    }

    private fun handleLaunchIntent(intent: Intent) {
        val payload = IntentCompat
            .getParcelableExtra(
                intent,
                LAUNCH_PAYLOAD,
                Intent::class.java,
            )
            ?.let { payload ->
                try {
                    IntentSanitizer.Builder()
                        .allowComponent(ComponentName(applicationContext, RotationService::class.java))
                        .allowAction(RotationServiceViewImpl.STOP_SERVICE_ACTION)
                        .build()
                        .sanitizeByThrowing(payload)
                } catch (e: Exception) {
                    null
                }
            }

        payload ?: return

        startService(payload)
    }
}