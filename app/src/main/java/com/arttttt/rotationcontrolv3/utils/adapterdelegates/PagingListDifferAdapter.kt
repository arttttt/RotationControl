package com.arttttt.rotationcontrolv3.utils.adapterdelegates

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class PagingListDifferAdapter(
	private val remainingItemsThreshold: Int = 10,
	diffCallback: DiffUtil.ItemCallback<ListItem>,
	delegates: Set<AdapterDelegate<out ListItem>>
) : AsyncListDifferDelegationAdapter(delegates, diffCallback) {

	fun interface PaginationCallback {
		fun onNewPageRequired()
	}

	var paginationCallback: PaginationCallback? = null
	
	init {
		items = mutableListOf()
	}
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
		super.onBindViewHolder(holder, position, payloads)
		
		if (position >= items!!.size - remainingItemsThreshold) {
			paginationCallback?.onNewPageRequired()
		}
	}
}