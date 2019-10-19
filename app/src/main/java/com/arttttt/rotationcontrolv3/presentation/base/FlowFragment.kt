package com.arttttt.rotationcontrolv3.presentation.base

import com.arttttt.rotationcontrolv3.presentation.delegate.IBackPressedDelegate
import com.arttttt.rotationcontrolv3.utils.FLOW_HOLDER
import com.arttttt.rotationcontrolv3.utils.extensions.rxpm.accept
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder

abstract class FlowFragment<PM: BaseFlowPresentationModel> : BaseFragment<PM>(), IBackPressedDelegate {

    protected open val navigationHolder: NavigatorHolder by inject(named(FLOW_HOLDER))
    protected abstract val navigator: Navigator

    override fun onPause() {
        super.onPause()

        navigationHolder.removeNavigator()
    }

    override fun onResume() {
        navigationHolder.setNavigator(navigator)
        super.onResume()
    }

    override fun backPressed() {
        presentationModel.backPressed.accept()
    }
}