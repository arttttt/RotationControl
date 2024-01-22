package com.arttttt.rotationcontrolv3.utils.navigationdialog

import android.content.Context
import androidx.annotation.LayoutRes
import com.arttttt.rotationcontrolv3.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView

class NavigationDialog private constructor(
    context: Context,
    items: Set<Item>,
    itemClickListener: NavigationDialogItemClickListener?,
) : BottomSheetDialog(context) {

    interface Item {

        val id: Int
        val title: CharSequence
    }

    companion object {

        fun show(
            context: Context,
            items: Set<Item>,
            itemClickListener: NavigationDialogItemClickListener?,
        ) {
            val dialog = NavigationDialog(
                context = context,
                items = items,
                itemClickListener = itemClickListener,
            )

            dialog.show()
        }
    }

    private val itemsMap: Map<Int, Item>

    init {
        setContentView(R.layout.dialog_navigation_menu)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)!!

        itemsMap = items.associateBy(Item::id)

        itemsMap.values.forEachIndexed { index, item ->
            navigationView
                .menu
                .add(
                    0,
                    item.id,
                    index,
                    item.title,
                )
        }

        if (itemClickListener != null) {
            navigationView.setNavigationItemSelectedListener { item ->
                itemClickListener.onClick(itemsMap.getValue(item.itemId))
                dismiss()
                true
            }
        }
    }
}