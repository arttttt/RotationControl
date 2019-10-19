package com.arttttt.rotationcontrolv3.presentation.delegate.screenswitcher

import androidx.fragment.app.FragmentManager
import com.arttttt.rotationcontrolv3.utils.extensions.android.beginTransaction
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ScreenSwitcherDelegate(
    private val containerId: Int
): IScreenSwitcherDelegate {

    companion object: IScreenSwitcherFragmentManagerHolder, KoinComponent {
        private val pendingSwitchesDisposable: CompositeDisposable by inject()
        private val pendingSwitchesSubject = PublishSubject.create<Unit>()

        private val pendingSwitches = mutableListOf<SupportAppScreen>()
        private var fragmentManager: FragmentManager? = null

        override fun attachFragmentManager(fragmentManager: FragmentManager) {
            this.fragmentManager = fragmentManager

            if (pendingSwitches.isNotEmpty()) {
                pendingSwitchesSubject.onNext(Unit)
            }
        }

        override fun detachFragmentManager() {
            fragmentManager = null
            pendingSwitchesDisposable.clear()
        }
    }

    private var currentFragmentTag: String? = null

    init {
        pendingSwitchesSubject
            .subscribe {
                pendingSwitches.forEach { screen -> switchScreen(screen) }
                pendingSwitches.clear()
            }.let(pendingSwitchesDisposable::add)
    }

    override fun switchScreen(screenToShow: SupportAppScreen) {
        val fm = fragmentManager
        fm ?: let {
            pendingSwitches += screenToShow

            return
        }

        val currentFragment = fm.findFragmentByTag(currentFragmentTag)
        val savedFragment = fm.findFragmentByTag(screenToShow.screenKey)

        val newFragment = savedFragment ?: screenToShow.fragment

        if (currentFragment === newFragment) {
            return
        }

        fm.beginTransaction {
            currentFragment?.let { fragment -> hide(fragment) }
            savedFragment?.let { fragment -> show(fragment) } ?: add(containerId, newFragment, screenToShow.screenKey)

            commit()
        }

        currentFragmentTag = screenToShow.screenKey
    }
}