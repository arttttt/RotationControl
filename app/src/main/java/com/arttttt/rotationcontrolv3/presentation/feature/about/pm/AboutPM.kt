package com.arttttt.rotationcontrolv3.presentation.feature.about.pm

import com.arttttt.rotationcontrolv3.BuildConfig
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.Screens
import com.arttttt.rotationcontrolv3.presentation.base.BaseFlowPresentationModel
import com.arttttt.rotationcontrolv3.utils.delegates.resources.IResourcesDelegate
import com.arttttt.rotationcontrolv3.utils.extensions.android.toUri
import me.dmdev.rxpm.action
import me.dmdev.rxpm.state

class AboutPM(
    private val resourcesDelegate: IResourcesDelegate
): BaseFlowPresentationModel() {

    val donateClicked = action<Unit>()
    val sourcesClicked = action<Unit>()

    val appVersion = state(resourcesDelegate.getString(R.string.rotation_control_version, BuildConfig.VERSION_NAME))

    override fun onCreate() {
        super.onCreate()

        donateClicked
            .observable
            .subscribeUntilDestroy { openDonationsPage() }

        sourcesClicked
            .observable
            .subscribeUntilDestroy { openSourcesPage() }
    }

    override fun backPressed() {
        router.exit()
    }

    private fun openDonationsPage() {
        router.navigateTo(
            Screens.ViewUriScreen(
                uri = resourcesDelegate.getString(R.string.paypal_link).toUri()
            )
        )
    }

    private fun openSourcesPage() {
        router.navigateTo(
            Screens.ViewUriScreen(
                uri = resourcesDelegate.getString(R.string.github_link).toUri()
            )
        )
    }
}