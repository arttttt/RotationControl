package com.arttttt.rotationcontrolv3.ui.settings.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arkivanov.mvikotlin.core.utils.diff
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.settings.adapter.delegates.SettingsAdapterDelegate
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.AsyncListDifferDelegationAdapter
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.EqualsDiffCallback
import com.arttttt.rotationcontrolv3.utils.extensions.clearAdapterOnDestroyView

class SettingsViewImpl(
    root: View
) : BaseMviView<SettingsView.Model, SettingsView.UiEvent>(), SettingsView {

    private val adapter = AsyncListDifferDelegationAdapter(
        delegates = setOf(
            SettingsAdapterDelegate(
                onCheckedChanged = { settings, isChecked ->
                    dispatch(
                        SettingsView.UiEvent.SettingsChanged(
                            settings = settings,
                            isChecked = isChecked,
                        )
                    )
                }
            )
        ),
        diffCallback = EqualsDiffCallback(),
    )

    override val renderer: ViewRenderer<SettingsView.Model> = diff {
        diff(
            get = SettingsView.Model::items,
            set = adapter::items::set,
        )
    }

    init {
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.clearAdapterOnDestroyView()
    }
}