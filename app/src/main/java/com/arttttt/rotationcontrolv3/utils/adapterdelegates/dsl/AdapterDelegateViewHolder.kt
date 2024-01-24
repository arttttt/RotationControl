package com.arttttt.rotationcontrolv3.utils.adapterdelegates.dsl

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

class AdapterDelegateViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
	
	internal var _item: T? = null
	
	val item: T
		get() {
			return if (_item == null) {
				throw IllegalArgumentException(
					"Item has not been set yet"
				)
			} else {
				_item!!
			}
		}
	
	val context: Context = view.context
	
	internal var _bind: ((payloads: List<Any>) -> Unit)? = null
		private set
	
	internal var _onViewRecycled: (() -> Unit)? = null
		private set
	
	internal var _onFailedToRecycleView: (() -> Boolean)? = null
		private set
	
	internal var _onViewAttachedToWindow: (() -> Unit)? = null
		private set
	
	internal var _onViewDetachedFromWindow: (() -> Unit)? = null
		private set
	
	fun bind(bindingBlock: (payloads: List<Any>) -> Unit) {
		if (_bind != null) {
			throw IllegalStateException("bind { ... } is already defined. Only one bind { ... } is allowed.")
		}
		this._bind = bindingBlock
	}
	
	fun onViewRecycled(block: () -> Unit) {
		if (_onViewRecycled != null) {
			throw IllegalStateException(
				"onViewRecycled { ... } is already defined. " + "Only one onViewRecycled { ... } is allowed."
			)
		}
		_onViewRecycled = block
	}
	
	fun onFailedToRecycleView(block: () -> Boolean) {
		if (_onFailedToRecycleView != null) {
			throw IllegalStateException(
				"onFailedToRecycleView { ... } is already defined. " + "Only one onFailedToRecycleView { ... } is allowed."
			)
		}
		_onFailedToRecycleView = block
	}
	
	fun onViewAttachedToWindow(block: () -> Unit) {
		if (_onViewAttachedToWindow != null) {
			throw IllegalStateException(
				"onViewAttachedToWindow { ... } is already defined. " + "Only one onViewAttachedToWindow { ... } is allowed."
			)
		}
		_onViewAttachedToWindow = block
	}
	
	fun onViewDetachedFromWindow(block: () -> Unit) {
		if (_onViewDetachedFromWindow != null) {
			throw IllegalStateException(
				"onViewDetachedFromWindow { ... } is already defined. " + "Only one onViewDetachedFromWindow { ... } is allowed."
			)
		}
		_onViewDetachedFromWindow = block
	}
	
	fun <V : View> findViewById(@IdRes id: Int): V = itemView.findViewById(id) as V
}