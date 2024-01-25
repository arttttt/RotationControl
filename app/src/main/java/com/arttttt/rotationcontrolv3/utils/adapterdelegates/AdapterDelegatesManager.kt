package com.arttttt.rotationcontrolv3.utils.adapterdelegates

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

open class AdapterDelegatesManager<T : ListItem>(delegates: Set<AdapterDelegate<T>>) {
	
	companion object {
		protected const val FALLBACK_DELEGATE_VIEW_TYPE = Int.MAX_VALUE - 1

		private const val NO_DELEGATE_ERROR = "No delegate found for %s for item at position = %s for viewType = %s"
		private const val VIEW_TYPE_RESERVED_ERROR  = "The view type = $FALLBACK_DELEGATE_VIEW_TYPE is reserved for fallback adapter delegate (see setFallbackDelegate()). Please use another view type."
		private const val DELEGATE_IS_ALREADY_REGISTERED_ERROR = "An AdapterDelegate is already registered for the viewType = %s. Already registered AdapterDelegate is %s"
		private const val NO_MORE_VIEW_TYPES_LEFT_ERROR = "Oops, we are very close to Integer.MAX_VALUE. It seems that there are no more free and unused view type integers left to add another AdapterDelegate."
		private const val NO_ADAPTER_DELEGATE_ERROR = "No AdapterDelegate added for ViewType %s"
		private const val NO_ADAPTER_DELEGATE_THAT_MATCHES_ITEM_ERROR = "No AdapterDelegate added that matches item=%s at position=%s in data source"
		private const val NO_ADAPTER_DELEGATE_FOR_POSITION = "No AdapterDelegate added for item at position=%s. items=%s"

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
			VIEW_TYPE_RESERVED_ERROR
		}
		require(!(!allowReplacingDelegate && delegates[viewType] != null)) {
			DELEGATE_IS_ALREADY_REGISTERED_ERROR.format(viewType, delegates[viewType])
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
	
	fun removeDelegate(viewType: Int): AdapterDelegatesManager<T> {
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
			NO_ADAPTER_DELEGATE_THAT_MATCHES_ITEM_ERROR.format(itemString, position)
		} else {
			NO_ADAPTER_DELEGATE_FOR_POSITION.format(position, item)
		}
		throw NullPointerException(errorMessage)
	}
	
	fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int,
	): RecyclerView.ViewHolder {
		val delegate = requireNotNull(getDelegateForViewType(viewType)) {
			NO_ADAPTER_DELEGATE_ERROR.format(viewType)
		}
		return delegate.onCreateViewHolder(parent)
	}
	
	@JvmOverloads
	fun onBindViewHolder(
		item: T,
		position: Int,
		holder: RecyclerView.ViewHolder,
		payloads: List<Any> = PAYLOADS_EMPTY_LIST,
	) {
		val delegate = requireNotNull(getDelegateForViewType(holder.itemViewType)) {
			noDelegateMessage(holder)
		}
		delegate.onBindViewHolder(item, position, holder, payloads)
	}
	
	fun onViewRecycled(holder: RecyclerView.ViewHolder) {
		val delegate = requireNotNull(getDelegateForViewType(holder.itemViewType)) {
			noDelegateMessage(holder)
		}
		delegate.onViewRecycled(holder)
	}
	
	fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
		val delegate = requireNotNull(getDelegateForViewType(holder.itemViewType)) {
			noDelegateMessage(holder)
		}
		return delegate.onFailedToRecycleView(holder)
	}
	
	fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
		val delegate = requireNotNull(getDelegateForViewType(holder.itemViewType)) {
			noDelegateMessage(holder)
		}
		delegate.onViewAttachedToWindow(holder)
	}
	
	fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
		val delegate = requireNotNull(getDelegateForViewType(holder.itemViewType)) {
			noDelegateMessage(holder)
		}
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

	private fun noDelegateMessage(holder: RecyclerView.ViewHolder): String {
		return NO_DELEGATE_ERROR.format(
			holder,
			holder.absoluteAdapterPosition,
			holder.itemViewType,
		)
	}
	
	private fun getViewType(): Int {
		var viewType = delegates.size()
		while (delegates[viewType] != null) {
			viewType++
			require(viewType != FALLBACK_DELEGATE_VIEW_TYPE) {
				NO_MORE_VIEW_TYPES_LEFT_ERROR
			}
		}
		
		return viewType
	}
}