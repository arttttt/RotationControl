package com.arttttt.rotationcontrolv3.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.settings.adapter.delegates.SettingsAdapterDelegate
import com.arttttt.rotationcontrolv3.ui.settings.adapter.models.SettingsAdapterItem
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.AsyncListDifferDelegationAdapter
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.EqualsDiffCallback
import com.arttttt.rotationcontrolv3.utils.extensions.clearAdapterOnDestroyView

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val adapter = AsyncListDifferDelegationAdapter(
        delegates = setOf(
            SettingsAdapterDelegate(
                onCheckedChanged = { isChecked ->}
            )
        ),
        diffCallback = EqualsDiffCallback(),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.clearAdapterOnDestroyView()

        adapter.items = listOf(
            SettingsAdapterItem(
                "Start Rotation Control at system boot",
                false,
            ),
        )
    }
}