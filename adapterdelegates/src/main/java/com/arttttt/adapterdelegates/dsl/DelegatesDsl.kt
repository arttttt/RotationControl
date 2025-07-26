package com.arttttt.adapterdelegates.dsl

import com.arttttt.adapterdelegates.AdapterDelegate
import com.arttttt.adapterdelegates.ListItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

@Suppress("NAME_SHADOWING")
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