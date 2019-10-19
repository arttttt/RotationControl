package com.arttttt.rotationcontrolv3

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.arttttt.rotationcontrolv3.presentation.base.FlowAppScreen
import com.arttttt.rotationcontrolv3.presentation.feature.about.view.AboutFragment
import com.arttttt.rotationcontrolv3.presentation.feature.mainflow.view.MainFlowFragment
import com.arttttt.rotationcontrolv3.presentation.feature.settings.view.SettingsFragment
import com.arttttt.rotationcontrolv3.utils.extensions.android.intentOf
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {
    object MainFlowScreen: FlowAppScreen() {
        override fun getFragment(): Fragment {
            return MainFlowFragment.newInstance()
        }
    }

    object SettingsScreen: SupportAppScreen() {
        override fun getFragment(): Fragment {
            return SettingsFragment()
        }
    }

    object AboutScreen: SupportAppScreen() {
        override fun getFragment(): Fragment {
            return AboutFragment()
        }
    }

    class ViewUriScreen(
        private val uri: Uri
    ): SupportAppScreen() {
        override fun getActivityIntent(context: Context?): Intent {
            return intentOf(Intent.ACTION_VIEW, uri)
        }
    }
}