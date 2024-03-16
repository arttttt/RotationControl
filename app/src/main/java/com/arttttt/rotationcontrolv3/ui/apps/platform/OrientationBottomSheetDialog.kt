package com.arttttt.rotationcontrolv3.ui.apps.platform

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.domain.entity.apps.AppOrientation
import com.arttttt.rotationcontrolv3.utils.extensions.dp
import com.arttttt.rotationcontrolv3.utils.extensions.toPx
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * todo: make it generic
 */
class OrientationBottomSheetDialog(
    context: Context,
    onClick: (AppOrientation) -> Unit,
) : BottomSheetDialog(context) {

    init {
        val inflater = LayoutInflater.from(context)

        val root = LinearLayout(context)
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        root.orientation = LinearLayout.VERTICAL
        ViewCompat.setPaddingRelative(
            root,
            0,
            24.dp.toPx(),
            0,
            8.dp.toPx(),
        )

        AppOrientation.entries.forEachIndexed { index, appOrientation ->
            val itemView = inflater.inflate(R.layout.dialog_simple_item, root, false)

            ViewCompat.setPaddingRelative(
                itemView,
                8.dp.toPx(),
                0,
                8.dp.toPx(),
                if (index < AppOrientation.entries.size - 1) {
                    8.dp.toPx()
                } else {
                    0
                },
            )

            itemView
                .findViewById<ImageView>(R.id.iconImageView)
                .setImageResource(R.drawable.ic_portrait)

            itemView
                .findViewById<TextView>(R.id.titleTextView)
                .text = appOrientation.toTitle()

            itemView.setOnClickListener {
                onClick.invoke(appOrientation)
                dismiss()
            }

            root.addView(itemView)
        }

        setContentView(root)
    }

    private fun AppOrientation.toTitle(): String {
        return when (this) {
            AppOrientation.GLOBAL -> "Global"
            AppOrientation.PORTRAIT -> "Portrait"
            AppOrientation.PORTRAIT_REVERSE -> "Portrait reverse"
            AppOrientation.LANDSCAPE -> "Landscape"
            AppOrientation.LANDSCAPE_REVERSE -> "Landscape reverse"
            AppOrientation.AUTO -> "Auto"
        }
    }
}