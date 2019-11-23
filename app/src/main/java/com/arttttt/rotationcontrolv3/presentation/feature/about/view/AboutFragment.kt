package com.arttttt.rotationcontrolv3.presentation.feature.about.view

import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.presentation.base.BaseFragment
import com.arttttt.rotationcontrolv3.presentation.delegate.IMenuIdProvider
import com.arttttt.rotationcontrolv3.presentation.feature.about.pm.AboutPM
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.fragment_about.*
import me.dmdev.rxpm.bindTo
import org.koin.android.scope.currentScope

class AboutFragment: BaseFragment<AboutPM>(R.layout.fragment_about), IMenuIdProvider {
    override val menuId: Int = R.id.about_fragment_item

    override fun providePresentationModel(): AboutPM {
        return currentScope.get()
    }

    override fun bindActions(pm: AboutPM) {
        btnDonate
            .clicks()
            .bindTo(pm.donateClicked)

        btnSources
            .clicks()
            .bindTo(pm.sourcesClicked)
    }

    override fun bindStates(pm: AboutPM) {
        pm.appVersion
            .bindTo(tvVersion::setText)
    }
}
