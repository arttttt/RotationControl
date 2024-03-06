package com.arttttt.rotationcontrolv3.ui.apps.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arkivanov.mvikotlin.core.utils.diff
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.apps.adapter.AppsDiffCallback
import com.arttttt.rotationcontrolv3.ui.apps.adapter.delegates.AppAdapterDelegate
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.AsyncListDifferDelegationAdapter

class AppsViewImpl(
    root: View
) : BaseMviView<AppsView.Model, AppsView.UiEvent>(), AppsView {

    private val adapter by lazy {
        AsyncListDifferDelegationAdapter(
            delegates = setOf(
                AppAdapterDelegate()
            ),
            diffCallback = AppsDiffCallback(),
        )
    }

    override val renderer: ViewRenderer<AppsView.Model> = diff {
        diff(
            get = AppsView.Model::items,
            set = adapter::items::set,
        )
    }

    init {
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
    }
}