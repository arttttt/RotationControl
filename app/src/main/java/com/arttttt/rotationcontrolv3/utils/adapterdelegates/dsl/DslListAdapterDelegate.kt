package com.arttttt.rotationcontrolv3.utils.adapterdelegates.dsl

import com.arttttt.rotationcontrolv3.utils.adapterdelegates.AdapterDelegate
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.ListItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

@PublishedApi
internal class DslListAdapterDelegate<T : ListItem>(
    @LayoutRes private val layout: Int,
    private val on: (item: T, position: Int) -> Boolean,
    private val initializerBlock: AdapterDelegateViewHolder<T>.() -> Unit,
    private val layoutInflater: (parent: ViewGroup, layout: Int) -> View
) : AdapterDelegate<T>() {
	
	override fun isForViewType(item: T, position: Int): Boolean {
		return on(
			item,
			position,
		)
	}
	
	override fun onCreateViewHolder(parent: ViewGroup): AdapterDelegateViewHolder<T> = AdapterDelegateViewHolder<T>(
		layoutInflater(parent, layout)
	).also {
		initializerBlock(it)
	}
	
	override fun onBindViewHolder(
		item: T,
		position: Int,
		holder: RecyclerView.ViewHolder,
		payloads: List<Any>,
	) {
		@Suppress("UNCHECKED_CAST") val vh = (holder as AdapterDelegateViewHolder<T>)
		
		vh._item = item
		vh._bind?.invoke(payloads)
	}
	
	override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
		@Suppress("UNCHECKED_CAST") val vh = (holder as AdapterDelegateViewHolder<T>)
		
		vh._onViewRecycled?.invoke()
	}
	
	override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
		@Suppress("UNCHECKED_CAST") val vh = (holder as AdapterDelegateViewHolder<T>)
		val block = vh._onFailedToRecycleView
		return if (block == null) {
			super.onFailedToRecycleView(holder)
		} else {
			block()
		}
	}
	
	override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
		@Suppress("UNCHECKED_CAST") val vh = (holder as AdapterDelegateViewHolder<T>)
		vh._onViewAttachedToWindow?.invoke()
	}
	
	override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
		@Suppress("UNCHECKED_CAST") val vh = (holder as AdapterDelegateViewHolder<T>)
		vh._onViewDetachedFromWindow?.invoke()
	}
}