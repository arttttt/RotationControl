package com.arttttt.rotationcontrolv3.ui.main2.model

import com.arttttt.rotationcontrolv3.R

sealed interface MenuItem {

    companion object;

    val id: Int
    val title: Int
    val icon: Int

    data object Settings : MenuItem {

        override val id: Int = R.id.item_settings
        override val title: Int = R.string.settings
        override val icon: Int = R.drawable.ic_settings_24
    }

    data object About : MenuItem {

        override val id: Int = R.id.item_about
        override val title: Int = R.string.about
        override val icon: Int = R.drawable.ic_info_24
    }

    data object Apps : MenuItem {

        override val id: Int = R.id.item_apps
        override val title: Int = R.string.apps
        override val icon: Int = R.drawable.ic_apps_24
    }
}