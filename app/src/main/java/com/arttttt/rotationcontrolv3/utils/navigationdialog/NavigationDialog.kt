package com.arttttt.rotationcontrolv3.utils.navigationdialog

import android.content.Context
import androidx.annotation.LayoutRes
import com.arttttt.rotationcontrolv3.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView

class NavigationDialog private constructor(
    context: Context,
    itemClickListener: NavigationDialogItemClickListener?,
) : BottomSheetDialog(context) {

    companion object {

        fun show(
            context: Context,
            itemClickListener: NavigationDialogItemClickListener?,
        ) {
            val dialog = NavigationDialog(
                context = context,
                itemClickListener = itemClickListener,
            )

            dialog.show()
        }
    }

    init {
        setContentView(R.layout.dialog_navigation_menu)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)!!

        if (itemClickListener != null) {
            navigationView.setNavigationItemSelectedListener { item ->
                itemClickListener.onClick(item.itemId)
                dismiss()
                true
            }
        }
    }
}