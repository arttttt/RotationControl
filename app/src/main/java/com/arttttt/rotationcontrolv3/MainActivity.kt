package com.arttttt.rotationcontrolv3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.arttttt.rotationcontrolv3.ui.container.ContainerFragment
import com.arttttt.rotationcontrolv3.utils.NavigationContainerDelegate

class MainActivity : AppCompatActivity() {

    private val containerDelegate by lazy {
        NavigationContainerDelegate(
            context = this,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        containerDelegate.initialize(savedInstanceState)

        super.onCreate(savedInstanceState)

        setContentView(containerDelegate.createContainerView())

        if (savedInstanceState != null) return

        supportFragmentManager.commit {
            replace<ContainerFragment>(
                containerDelegate.containerId,
                null,
                null,
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        containerDelegate.saveState(outState)
    }
}