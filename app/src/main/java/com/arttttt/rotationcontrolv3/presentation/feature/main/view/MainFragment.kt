package com.arttttt.rotationcontrolv3.presentation.feature.main.view

import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.presentation.base.FlowFragment
import com.arttttt.rotationcontrolv3.presentation.delegate.IMenuIdProvider
import com.arttttt.rotationcontrolv3.presentation.feature.main.pm.MainPM
import com.arttttt.rotationcontrolv3.presentation.model.DialogResult
import com.arttttt.rotationcontrolv3.utils.extensions.android.*
import com.arttttt.rotationcontrolv3.utils.extensions.koilin.castTo
import com.arttttt.rotationcontrolv3.utils.extensions.rxpm.accept
import com.google.android.material.navigation.NavigationView
import com.jakewharton.rxbinding3.material.itemSelections
import com.jakewharton.rxbinding3.material.visibility
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.fragment_main.*
import me.dmdev.rxpm.bindTo
import me.dmdev.rxpm.widget.bindTo
import org.koin.android.scope.currentScope
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainFragment: FlowFragment<MainPM>(R.layout.fragment_main) {

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override val navigator: Navigator by lazy { SupportAppNavigator(activity, childFragmentManager, R.id.container) }

    private var currentFragmentTag: String? = null

    override fun providePresentationModel(): MainPM {
        return currentScope.get()
    }

    override fun bindActions(pm: MainPM) {
        bottomToolbar.setNavigationOnClickListener { pm.hamburgerClicked.accept() }

        fab.clicks().bindTo(pm.fabClicked)
    }

    override fun bindCommands(pm: MainPM) {
        pm.currentScreen.bindTo(::showScreen)
    }

    override fun bindStates(pm: MainPM) {
        pm.fabVisibility
            .bindTo(fab.visibility())

        pm.fabIconRes
            .bindTo { imageRes ->
                fab.setImageResource(imageRes)

                // This hack fixes the disappearance of an icon
                if (fab.isVisible) {
                    fab.hide()
                    fab.show()
                }
            }
    }

    override fun bindRestActions(pm: MainPM) {
        pm.navigationDialog
            .bindTo { _, dc ->
                bottomSheetDialogOf(requireContext()) {
                    layoutInflater.inflate(R.layout.navigation_menu) {
                        with(findViewById<NavigationView>(R.id.navigation)) {
                            itemSelections()
                                .doOnNext { dc.dismiss() }
                                .bindTo(pm.navigationMenuClicked)

                            // todo: dispatch via delegate
                            childFragmentManager
                                .fragments
                                .firstOrNull { fragment -> fragment.isVisible }
                                .castTo<IMenuIdProvider>()
                                ?.let { provider -> menu.findItem(provider.menuId).isChecked = true }
                        }

                        setContentView(this)
                    }
                }
            }

        pm.writeSettingsDialog
            .bindTo { _, dc ->
                alertDialogOf(requireContext()) {
                    setTitle(R.string.notice_text)
                    setMessage(R.string.can_write_settings_permission_text)
                    setPositiveButtonExt(android.R.string.ok) {
                        onClick = { dc.sendResult(DialogResult.OK) }
                    }
                    setNegativeButtonExt(android.R.string.cancel) {
                        onClick = { dc.sendResult(DialogResult.CANCEL) }
                    }
                }
            }

        pm.drawOverlayDialog
            .bindTo { _, dc ->
                alertDialogOf(requireContext()) {
                    setTitle(R.string.notice_text)
                    setMessage(R.string.draw_overlay_permission_text)
                    setPositiveButtonExt(android.R.string.ok) {
                        onClick = { dc.sendResult(DialogResult.OK) }
                    }
                    setNegativeButtonExt(android.R.string.cancel) {
                        onClick = { dc.sendResult(DialogResult.CANCEL) }
                    }
                }
            }
    }

    private fun showScreen(screen: SupportAppScreen) {
        val currentFragment = childFragmentManager.findFragmentByTag(currentFragmentTag)
        val savedFragment = childFragmentManager.findFragmentByTag(screen.screenKey)

        val newFragment = savedFragment ?: screen.fragment

        if (currentFragment === newFragment) {
            return
        }

        childFragmentManager.beginTransaction {
            currentFragment?.let { fragment -> hide(fragment) }
            savedFragment?.let { fragment -> show(fragment) } ?: add(R.id.container, newFragment, screen.screenKey)

            commit()
        }

        currentFragmentTag = screen.screenKey
    }
}