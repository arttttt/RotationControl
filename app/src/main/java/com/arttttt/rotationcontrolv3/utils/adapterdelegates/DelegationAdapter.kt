package com.arttttt.rotationcontrolv3.utils.adapterdelegates

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arttttt.utils.unsafeCastTo

@SuppressLint("NotifyDataSetChanged")
open class DelegationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
	
	private val delegatesManager: AdapterDelegatesManager<ListItem>
	
	var items: List<ListItem> = emptyList()
		set(value) {
			field = value
			notifyDataSetChanged()
		}
	
	constructor(delegates: Set<AdapterDelegate<out ListItem>>) : super() {
		
		delegatesManager = AdapterDelegatesManager(delegates.unsafeCastTo())
	}
	
	constructor() : this(emptySet())
	
	@JvmOverloads
	fun addDelegate(
		delegate: AdapterDelegate<out ListItem>,
		viewType: Int = Int.MIN_VALUE,
		allowReplacingDelegate: Boolean = false,
	) {
		if (viewType == Int.MIN_VALUE) {
			delegatesManager.addDelegate(
				delegate = delegate.unsafeCastTo(),
				allowReplacingDelegate = allowReplacingDelegate,
			)
		} else {
			delegatesManager.addDelegate(
				delegate = delegate.unsafeCastTo(),
				viewType = viewType,
				allowReplacingDelegate = allowReplacingDelegate,
			)
		}
	}
	
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int,
	): RecyclerView.ViewHolder {
		return delegatesManager.onCreateViewHolder(parent, viewType)
	}
	
	override fun onBindViewHolder(
		holder: RecyclerView.ViewHolder,
		position: Int,
	) {
		delegatesManager.onBindViewHolder(
			item = items[position],
			position = position,
			holder = holder,
		)
	}
	
	override fun onBindViewHolder(
		holder: RecyclerView.ViewHolder,
		position: Int,
		payloads: List<Any>,
	) {
		delegatesManager.onBindViewHolder(
			item = items[position],
			position = position,
			holder = holder,
			payloads = payloads,
		)
	}
	
	override fun getItemViewType(position: Int): Int {
		return delegatesManager.getItemViewType(
			item = items[position],
			position = position,
		)
	}
	
	override fun getItemCount(): Int {
		return items.size
	}
	
	override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
		delegatesManager.onViewRecycled(holder)
	}
	
	override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
		return delegatesManager.onFailedToRecycleView(holder)
	}
	
	override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
		delegatesManager.onViewAttachedToWindow(holder)
	}
	
	override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
		delegatesManager.onViewDetachedFromWindow(holder)
	}
}