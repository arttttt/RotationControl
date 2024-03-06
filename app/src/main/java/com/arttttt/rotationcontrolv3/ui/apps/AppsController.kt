package com.arttttt.rotationcontrolv3.ui.apps

import android.content.Context
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStore
import com.arttttt.rotationcontrolv3.ui.apps.adapter.models.AppAdapterItem
import com.arttttt.rotationcontrolv3.ui.apps.view.AppsView
import com.arttttt.rotationcontrolv3.utils.mvi.Controller
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppsController @Inject constructor(
    private val appsStore: AppsStore,
    /**
     * todo: remove later
     */
    private val context: Context,
) : Controller<AppsView> {

    override fun onViewCreated(
        view: AppsView,
        lifecycle: Lifecycle,
    ) {
        bind(
            lifecycle = lifecycle,
            mode = BinderLifecycleMode.CREATE_DESTROY,
        ) {
            appsStore
                .states
                .map { state ->
                    AppsView.Model(
                        items = state.apps.map { info ->
                            AppAdapterItem(
                                title = info.title,
                                appPackage = info.pkg,
                                icon = context.packageManager.getApplicationIcon(info.pkg),
                            )
                        },
                    )
                }
                .bindTo(view)
        }
    }
}