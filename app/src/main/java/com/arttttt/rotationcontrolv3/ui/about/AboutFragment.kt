package com.arttttt.rotationcontrolv3.ui.about

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.arttttt.navigation.FlowMenuRouter
import com.arttttt.navigation.factory.FragmentProvider
import com.arttttt.rotationcontrolv3.BuildConfig
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.Screens
import com.arttttt.rotationcontrolv3.ui.about.di.AboutComponentDependencies
import com.arttttt.rotationcontrolv3.ui.about.di.DaggerAboutComponent
import com.google.android.material.button.MaterialButton
import javax.inject.Inject

class AboutFragment(
    private val dependencies: AboutComponentDependencies
) : Fragment(R.layout.fragment_about) {

    companion object {

        fun provider(dependencies: AboutComponentDependencies) = FragmentProvider {
            AboutFragment(dependencies)
        }
    }

    @Inject
    lateinit var router: FlowMenuRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAboutComponent
            .factory()
            .create(
                dependencies = dependencies
            )
            .inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.sourcesButton).setOnClickListener {
            router.navigateTo(
                Screens.UriScreen(
                    uri = requireContext().getString(R.string.github_link).toUri()
                )
            )
        }

        view.findViewById<TextView>(R.id.versionTextView).text = getString(
            R.string.rotation_control_version,
            BuildConfig.VERSION_NAME,
        )
    }
}