package com.arttttt.rotationcontrolv3.utils.adapterdelegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class AdapterDelegate<T : ListItem> {
	
	internal abstract fun isForViewType(
		item: T,
		position: Int,
	): Boolean
	
	internal abstract fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
	
	internal abstract fun onBindViewHolder(
		item: T,
		position: Int,
		holder: RecyclerView.ViewHolder,
		payloads: List<Any>,
	)
	
	internal open fun onViewRecycled(holder: RecyclerView.ViewHolder) {}
	
	internal open fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
		return false
	}
	
	internal open fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {}
	
	internal open fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {}
}