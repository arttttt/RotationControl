package com.arttttt.rotationcontrolv3.ui.main2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.arttttt.navigation.factory.CustomFragmentFactory
import com.arttttt.navigation.factory.FragmentProvider
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.permissions.domain.repository.PermissionsRequester
import com.arttttt.permissions.utils.extensions.toBoolean
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.data.model.DrawOverlayPermission
import com.arttttt.rotationcontrolv3.data.model.NotificationsPermission
import com.arttttt.rotationcontrolv3.data.model.WriteSettingsPermission
import com.arttttt.rotationcontrolv3.domain.entity.Setting
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.ui.about.AboutFragment
import com.arttttt.rotationcontrolv3.ui.main2.di.DaggerMainComponent2
import com.arttttt.rotationcontrolv3.ui.main2.di.MainComponentDependencies2
import com.arttttt.rotationcontrolv3.ui.rotation.RotationService
import com.arttttt.rotationcontrolv3.ui.settings.platform.SettingsFragment
import com.arttttt.utils.unsafeCastTo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class MainFragment2(
    private val dependencies: MainComponentDependencies2,
) : Fragment(R.layout.fragment_main2) {

    sealed interface MenuItem {

        val id: Int
        val title: Int
        val icon: Int

        data object Settings : MenuItem {

            override val id: Int = R.id.item_settings
            override val title: Int = R.string.menu_item_settings
            override val icon: Int = R.drawable.ic_settings_24
        }

        data object About : MenuItem {

            override val id: Int = R.id.item_about
            override val title: Int = R.string.menu_item_about
            override val icon: Int = R.drawable.ic_info_24
        }
    }

    companion object {

        fun provider(dependencies: MainComponentDependencies2): FragmentProvider = FragmentProvider {
            MainFragment2(dependencies)
        }
    }

    @Inject
    lateinit var fragmentFactory: CustomFragmentFactory

    @Inject
    lateinit var permissionsRepository: PermissionsRepository

    @Inject
    lateinit var permissionsRequester: PermissionsRequester

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val rotationServiceIntent by lazy {
        Intent(
            requireContext(),
            RotationService::class.java,
        )
    }

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerMainComponent2
            .factory()
            .create(
                dependencies = dependencies,
                activity = requireActivity().unsafeCastTo(),
            )
            .inject(this)

        childFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val launchServiceButton = view.findViewById<FloatingActionButton>(R.id.fab)

        launchServiceButton.setOnClickListener {
            job?.cancel()

            job = lifecycleScope.launch {
                val isServiceRunning = RotationService.status.value == RotationService.Status.RUNNING

                ensureActive()

                if (isServiceRunning) {
                    stopRotationService()
                } else {
                    val isAllPermissionsGranted = kotlin
                        .runCatching {
                            checkAndGetPermissions()
                        }
                        .onFailure {
                            showDialog(
                                title = R.string.cant_request_permission,
                                message = R.string.cant_request_permission_message,
                            )
                        }
                        .getOrDefault(false)

                    if (!isAllPermissionsGranted) return@launch

                    ensureActive()

                    startRotationService()
                }
            }
        }

        view.unsafeCastTo<ViewGroup>().bringChildToFront(launchServiceButton)

        val bottomNavigation = view.findViewById<BottomNavigationView>(R.id.bottomNavigation)

        bottomNavigation.addMenuItem(MenuItem.Settings)
        bottomNavigation.addMenuItem(MenuItem.About)

        bottomNavigation.setOnItemSelectedListener { item ->
            setFragment(item.itemId)
            launchServiceButton.setVisibilityByMenuItem(item.itemId)

            true
        }

        bottomNavigation.selectedItemId = MenuItem.Settings.id

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            RotationService
                .status
                .map { status -> status.toFabIconRes() }
                .onEach(launchServiceButton::setImageResource)
                .launchIn(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        job?.cancel()
        job = null
    }

    private fun BottomNavigationView.addMenuItem(item: MenuItem) {
        menu.add(0, item.id, 0, item.title).apply {
            setIcon(item.icon)
        }
    }

    private fun setFragment(id: Int) {
        when (id) {
            MenuItem.Settings.id -> {
                childFragmentManager.commit {
                    replace<SettingsFragment>(R.id.container)
                }
            }
            MenuItem.About.id -> {
                childFragmentManager.commit {
                    replace<AboutFragment>(R.id.container)
                }
            }
        }
    }

    private suspend fun checkAndGetPermissions(): Boolean {
        var isPermissionGranted = checkAndRequestPermissions(NotificationsPermission)

        if (!isPermissionGranted) return false

        isPermissionGranted = checkAndRequestPermissions(WriteSettingsPermission)

        if (!isPermissionGranted) return false

        val isForcedModeEnabled = settingsRepository
            .getSetting(Setting.ForcedMode::class)
            .value

        isPermissionGranted = if (isForcedModeEnabled) {
            checkAndRequestPermissions(DrawOverlayPermission)
        } else {
            true
        }

        if (!isPermissionGranted) return false

        return true
    }

    private suspend fun checkAndRequestPermissions(
        permission: Permission,
    ): Boolean {
        val isPermissionGranted = permissionsRepository.checkPermission(permission).toBoolean()

        if (isPermissionGranted) return true

        showDialog(
            message = permission.messageRes
        )

        permissionsRequester.requestPermission(permission)

        return permissionsRepository.checkPermission(permission).toBoolean()
    }

    private suspend fun showDialog(
        title: Int = R.string.notice_text,
        message: Int,
    ) {
        return suspendCancellableCoroutine { continuation ->
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    continuation.resume(Unit)
                }
                .setOnCancelListener {
                    continuation.resume(Unit)
                }
                .create()

            continuation.invokeOnCancellation { dialog.dismiss() }

            dialog.show()
        }
    }

    private fun startRotationService() {
        ContextCompat.startForegroundService(
            requireContext(),
            rotationServiceIntent,
        )
    }

    private fun stopRotationService() {
        requireContext().stopService(rotationServiceIntent)
    }

    private fun RotationService.Status.toFabIconRes(): Int {
        return when (this) {
            RotationService.Status.HALTED -> R.drawable.ic_start
            RotationService.Status.RUNNING -> R.drawable.ic_stop
        }
    }

    private val Permission.messageRes: Int
        get() {
            return when (this) {
                is NotificationsPermission -> R.string.show_notifications_permission_text
                is WriteSettingsPermission -> R.string.can_write_settings_permission_text
                is DrawOverlayPermission -> R.string.draw_overlay_permission_text
                else -> throw IllegalStateException("unsupported permission: $this")
            }
        }

    private fun FloatingActionButton.setVisibilityByMenuItem(id: Int) {
        when (id) {
            MenuItem.Settings.id -> show()
            MenuItem.About.id -> hide()
        }
    }
}