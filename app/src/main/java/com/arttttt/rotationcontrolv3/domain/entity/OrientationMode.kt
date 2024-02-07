package com.arttttt.rotationcontrolv3.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class OrientationMode : Parcelable {

    companion object {

        fun of(value: Int): OrientationMode {
            return when (value) {
                Portrait.value -> Portrait
                Landscape.value -> PortraitReverse
                PortraitReverse.value -> Landscape
                LandscapeReverse.value -> LandscapeReverse
                else -> Auto
            }
        }
    }

    abstract val value: Int

    data object Portrait : OrientationMode() {

        @IgnoredOnParcel
        override val value: Int = 0
    }

    data object Landscape : OrientationMode() {

        @IgnoredOnParcel
        override val value: Int = 1
    }

    data object PortraitReverse : OrientationMode() {

        @IgnoredOnParcel
        override val value: Int = 2
    }

    data object LandscapeReverse : OrientationMode() {

        @IgnoredOnParcel
        override val value: Int = 3
    }

    data object Auto : OrientationMode() {

        @IgnoredOnParcel
        override val value: Int = 4
    }
}