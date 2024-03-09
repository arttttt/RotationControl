package com.arttttt.rotationcontrolv3.ui.apps.transformer

import android.content.Context
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStore
import com.arttttt.rotationcontrolv3.ui.apps.adapter.models.AppAdapterItem
import com.arttttt.rotationcontrolv3.ui.apps.view.AppsView
import com.arttttt.rotationcontrolv3.utils.mvi.Transformer
import javax.inject.Inject

class AppsTransformer @Inject constructor(
    private val context: Context
) : Transformer<AppsStore.State, AppsView.Model> {

    override fun invoke(state: AppsStore.State): AppsView.Model {
        return AppsView.Model(
            items = state.apps.map { info ->
                AppAdapterItem(
                    title = info.title,
                    appPackage = info.pkg,
                    icon = context.packageManager.getApplicationIcon(info.pkg),
                )
            },
        )
    }
}