package com.arttttt.rotationcontrolv3.utils.adapterdelegates.dsl

import com.arttttt.rotationcontrolv3.utils.adapterdelegates.AdapterDelegate
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.ListItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

inline fun <reified T : ListItem> adapterDelegate(
	@LayoutRes layout: Int,
	noinline on: (item: ListItem, position: Int) -> Boolean = { item, _ -> item is T },
	noinline layoutInflater: (parent: ViewGroup, layoutRes: Int) -> View = { parent, layout ->
		LayoutInflater.from(parent.context)
			.inflate(
				layout,
				parent,
				false,
			)
	},
	noinline block: AdapterDelegateViewHolder<T>.() -> Unit
): AdapterDelegate<T> {
	
	return DslListAdapterDelegate(
		layout = layout,
		on = on,
		initializerBlock = block,
		layoutInflater = layoutInflater,
	)
}