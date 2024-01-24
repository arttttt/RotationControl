package com.arttttt.rotationcontrolv3.utils.adapterdelegates

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

open class AdapterDelegatesManager<T : ListItem>(delegates: Set<AdapterDelegate<T>>) {
	
	companion object {
		protected const val FALLBACK_DELEGATE_VIEW_TYPE = Int.MAX_VALUE - 1
		
		private val PAYLOADS_EMPTY_LIST: List<Any> = emptyList()
	}
	
	protected val delegates: SparseArrayCompat<AdapterDelegate<T>> = SparseArrayCompat()
	
	@JvmField
	protected var fallbackDelegate: AdapterDelegate<T>? = null
	
	init {
		for (element in delegates) {
			addDelegate(element)
		}
	}
	
	@JvmOverloads
	fun addDelegate(
        delegate: AdapterDelegate<T>,
        viewType: Int = getViewType(),
        allowReplacingDelegate: Boolean = false,
	): AdapterDelegatesManager<T> {
		
		require(viewType != FALLBACK_DELEGATE_VIEW_TYPE) {
			("The view type = " + FALLBACK_DELEGATE_VIEW_TYPE + " is reserved for fallback adapter delegate (see setFallbackDelegate() ). Please use another view type.")
		}
		require(!(!allowReplacingDelegate && delegates[viewType] != null)) {
			("An AdapterDelegate is already registered for the viewType = " + viewType + ". Already registered AdapterDelegate is " + delegates[viewType])
		}
		delegates.put(viewType, delegate)
		return this
	}
	
	fun removeDelegate(delegate: AdapterDelegate<T>): AdapterDelegatesManager<T> {
		val indexToRemove = delegates.indexOfValue(delegate)
		if (indexToRemove >= 0) {
			delegates.removeAt(indexToRemove)
		}
		return this
	}
	
	fun removeDelegate(viewType: Int): AdapterDelegatesManager<T>? {
		delegates.remove(viewType)
		return this
	}
	
	fun getItemViewType(
		item: T,
		position: Int,
	): Int {
		val delegatesCount = delegates.size()
		for (i in 0 until delegatesCount) {
			val delegate = delegates.valueAt(i)
			if (delegate.isForViewType(item, position)) {
				return delegates.keyAt(i)
			}
		}
		if (fallbackDelegate != null) {
			return FALLBACK_DELEGATE_VIEW_TYPE
		}
		val errorMessage: String = if (item is List<*>) {
			val itemString = item[position].toString()
			"No AdapterDelegate added that matches item=$itemString at position=$position in data source"
		} else {
			"No AdapterDelegate added for item at position=$position. items=$item"
		}
		throw NullPointerException(errorMessage)
	}
	
	fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int,
	): RecyclerView.ViewHolder {
		val delegate = getDelegateForViewType(viewType)
			?: throw NullPointerException("No AdapterDelegate added for ViewType $viewType")
		return delegate.onCreateViewHolder(parent)
	}
	
	@JvmOverloads
	fun onBindViewHolder(
		item: T,
		position: Int,
		holder: RecyclerView.ViewHolder,
		payloads: List<Any> = PAYLOADS_EMPTY_LIST,
	) {
		val delegate = getDelegateForViewType(holder.itemViewType)
			?: throw NullPointerException("No delegate found for item at position = " + position + " for viewType = " + holder.itemViewType)
		delegate.onBindViewHolder(item, position, holder, payloads)
	}
	
	fun onViewRecycled(holder: RecyclerView.ViewHolder) {
		val delegate = getDelegateForViewType(holder.itemViewType)
			?: throw NullPointerException("No delegate found for " + holder + " for item at position = " + holder.absoluteAdapterPosition + " for viewType = " + holder.itemViewType)
		delegate.onViewRecycled(holder)
	}
	
	fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
		val delegate = getDelegateForViewType(holder.itemViewType)
			?: throw NullPointerException("No delegate found for " + holder + " for item at position = " + holder.absoluteAdapterPosition + " for viewType = " + holder.itemViewType)
		return delegate.onFailedToRecycleView(holder)
	}
	
	fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
		val delegate = getDelegateForViewType(holder.itemViewType)
			?: throw NullPointerException("No delegate found for " + holder + " for item at position = " + holder.absoluteAdapterPosition + " for viewType = " + holder.itemViewType)
		delegate.onViewAttachedToWindow(holder)
	}
	
	fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
		val delegate = getDelegateForViewType(holder.itemViewType)
			?: throw NullPointerException("No delegate found for " + holder + " for item at position = " + holder.absoluteAdapterPosition + " for viewType = " + holder.itemViewType)
		delegate.onViewDetachedFromWindow(holder)
	}
	
	fun setFallbackDelegate(fallbackDelegate: AdapterDelegate<T>?): AdapterDelegatesManager<T> {
		this.fallbackDelegate = fallbackDelegate
		return this
	}
	
	fun getViewType(delegate: AdapterDelegate<T>): Int {
		val index = delegates.indexOfValue(delegate)
		return if (index == -1) {
			-1
		} else {
			delegates.keyAt(index)
		}
	}
	
	fun getDelegateForViewType(viewType: Int): AdapterDelegate<T>? {
		return delegates[viewType, fallbackDelegate]
	}
	
	fun getFallbackDelegate(): AdapterDelegate<T>? {
		return fallbackDelegate
	}
	
	private fun getViewType(): Int {
		var viewType = delegates.size()
		while (delegates[viewType] != null) {
			viewType++
			require(viewType != FALLBACK_DELEGATE_VIEW_TYPE) { "Oops, we are very close to Integer.MAX_VALUE. It seems that there are no more free and unused view type integers left to add another AdapterDelegate." }
		}
		
		return viewType
	}
}