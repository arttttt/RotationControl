package com.arttttt.rotationcontrolv3.utils.extensions.android

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog

class DialogOnClickImpl: DialogInterface.OnClickListener {
    var onClick: (() -> Unit)? = null

    override fun onClick(dialog: DialogInterface?, which: Int) {
        onClick?.invoke()
    }
}

fun bottomSheetDialogOf(context: Context, styleRes: Int = 0, block: BottomSheetDialog.() -> Unit = {}): BottomSheetDialog {
    return BottomSheetDialog(context, styleRes).apply(block)
}

fun alertDialogOf(context: Context, styleRes: Int = 0, block: AlertDialog.Builder.() -> Unit = {}): AlertDialog {
    return AlertDialog.Builder(context, styleRes).apply(block).create()
}

inline fun AlertDialog.Builder.setPositiveButtonExt(stringRes: Int, callback: DialogOnClickImpl.() -> Unit = {}) {
    setPositiveButton(stringRes, DialogOnClickImpl().apply(callback))
}

inline fun AlertDialog.Builder.setNegativeButtonExt(stringRes: Int, callback: DialogOnClickImpl.() -> Unit = {}) {
    setNegativeButton(stringRes, DialogOnClickImpl().apply(callback))
}