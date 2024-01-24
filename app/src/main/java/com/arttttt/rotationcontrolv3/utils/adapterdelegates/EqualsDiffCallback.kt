package com.arttttt.rotationcontrolv3.utils.adapterdelegates

import androidx.recyclerview.widget.DiffUtil

open class EqualsDiffCallback : DiffUtil.ItemCallback<ListItem>() {
	override fun areItemsTheSame(
		oldItem: ListItem,
		newItem: ListItem,
	): Boolean {
		return oldItem == newItem
	}
	
	override fun areContentsTheSame(
		oldItem: ListItem,
		newItem: ListItem,
	): Boolean {
		return oldItem == newItem
	}
	
	override fun getChangePayload(
		oldItem: ListItem,
		newItem: ListItem,
	): Any? {
		return newItem
	}
}