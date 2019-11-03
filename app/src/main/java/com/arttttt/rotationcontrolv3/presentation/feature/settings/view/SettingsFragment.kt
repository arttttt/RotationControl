package com.arttttt.rotationcontrolv3.presentation.feature.settings.view

import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.presentation.base.BaseFragment
import com.arttttt.rotationcontrolv3.presentation.delegate.IMenuIdProvider
import com.arttttt.rotationcontrolv3.presentation.feature.settings.pm.SettingsPM
import com.arttttt.rotationcontrolv3.presentation.model.DialogResult
import com.arttttt.rotationcontrolv3.utils.extensions.android.alertDialogOf
import com.arttttt.rotationcontrolv3.utils.extensions.android.setNegativeButtonExt
import com.arttttt.rotationcontrolv3.utils.extensions.android.setPositiveButtonExt
import kotlinx.android.synthetic.main.fragment_settings.*
import me.dmdev.rxpm.widget.bindTo
import org.koin.android.scope.currentScope

class SettingsFragment: BaseFragment<SettingsPM>(), IMenuIdProvider {
    override val menuId: Int = R.id.settings_fragment_item
    override val layoutRes: Int = R.layout.fragment_settings

    override fun providePresentationModel(): SettingsPM {
        return currentScope.get()
    }

    override fun bindRestActions(pm: SettingsPM) {
        pm.startOnBootControl
            .bindTo(startOnBootSwitch)

        pm.forceModelControl
            .bindTo(forceModeSwitch)

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
}
