package com.arttttt.rotationcontrolv3.utils.adapterdelegates

import android.view.ViewGroup
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arttttt.rotationcontrolv3.utils.extensions.unsafeCastTo
import java.util.concurrent.Executor

open class AsyncListDifferDelegationAdapter(
	delegates: Set<AdapterDelegate<out ListItem>>,
	diffCallback: DiffUtil.ItemCallback<ListItem>,
	mainThreadExecutor: Executor? = null,
	backgroundThreadExecutor: Executor? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	
	private val delegatesManager: AdapterDelegatesManager<ListItem> = AdapterDelegatesManager(delegates.unsafeCastTo())
	private val differ: AsyncListDiffer<ListItem> = AsyncListDiffer(
		AdapterListUpdateCallback(this),
		AsyncDifferConfig.Builder(diffCallback)
			.setMainThreadExecutor(mainThreadExecutor)
			.setBackgroundThreadExecutor(backgroundThreadExecutor)
			.build()
	)
	
	var items: List<ListItem>?
		get() = differ.currentList
		set(items) {
			differ.submitList(items)
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
			item = differ.currentList.elementAt(position),
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
			item = differ.currentList.elementAt(position),
			position = position,
			holder = holder,
			payloads = payloads,
		)
	}
	
	override fun getItemViewType(position: Int): Int {
		return delegatesManager.getItemViewType(
			item = differ.currentList.elementAt(position),
			position = position,
		)
	}
	
	override fun getItemCount(): Int {
		return differ.currentList.size
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