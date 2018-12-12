package com.arttttt.rotationcontrolv3.view.fragments.about

import android.content.Intent
import android.net.Uri
import com.arttttt.rotationcontrolv3.BuildConfig
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.presenter.about.AboutContract
import com.arttttt.rotationcontrolv3.view.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_about.*
import org.koin.android.ext.android.inject

class AboutFragment: BaseFragment<AboutContract.View, AboutContract.Presenter>(), AboutContract.View {
    override fun getLayoutResource() = R.layout.fragment_about
    override fun getMvpView() = this

    override val presenter: AboutContract.Presenter by inject()

    override fun initializeUI() {
        presenter.onInitialization()
        donateButton.setOnClickListener { presenter.onDonateClicked() }
        sourcesButton.setOnClickListener { presenter.onSourcesClicked() }
    }

    override fun setVersion() {
        applicationVersion.text = getString(R.string.rotation_control_version, BuildConfig.VERSION_NAME)
    }

    override fun showDonationPage() = startActivity(createViewIntent(R.string.paypal_link))

    override fun showSourceCode() = startActivity(createViewIntent(R.string.github_link))

    private fun createViewIntent(stringRes: Int) = Intent(Intent.ACTION_VIEW, Uri.parse(getString(stringRes)))
}