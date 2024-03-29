package com.arttttt.rotationcontrolv3.ui.apps.transformer

import android.content.Context
import com.arttttt.rotationcontrolv3.framework.services.AppOrientationAccessibilityService
import com.arttttt.rotationcontrolv3.domain.entity.apps.AppOrientation
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStore
import com.arttttt.rotationcontrolv3.ui.apps.adapter.models.AccessibilityListItem
import com.arttttt.rotationcontrolv3.ui.apps.adapter.models.AppAdapterItem
import com.arttttt.rotationcontrolv3.ui.apps.view.AppsView
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.ListItem
import com.arttttt.rotationcontrolv3.utils.extensions.isAccessibilityServiceEnabled
import com.arttttt.rotationcontrolv3.utils.mvi.Transformer
import javax.inject.Inject

class AppsTransformer @Inject constructor(
    private val context: Context
) : Transformer<AppsStore.State, AppsView.Model> {

    override fun invoke(state: AppsStore.State): AppsView.Model {
        return AppsView.Model(
            items = createItems(state),
        )
    }

    private fun createItems(state: AppsStore.State): List<ListItem> {
        return if (context.isAccessibilityServiceEnabled<AppOrientationAccessibilityService>()) {
            state.apps.map { (_, info) ->
                AppAdapterItem(
                    title = info.title,
                    appPackage = info.pkg,
                    orientation = info.orientation.toTitle(),
                    icon = context.packageManager.getApplicationIcon(info.pkg),
                )
            }
        } else {
            listOf(
                AccessibilityListItem
            )
        }
    }

    private fun AppOrientation.toTitle(): String {
        return when (this) {
            AppOrientation.GLOBAL -> "Global"
            AppOrientation.PORTRAIT -> "Portrait"
            AppOrientation.PORTRAIT_REVERSE -> "Portrait reverse"
            AppOrientation.LANDSCAPE -> "Landscape"
            AppOrientation.LANDSCAPE_REVERSE -> "Landscape reverse"
            AppOrientation.AUTO -> "Auto"
        }
    }
}