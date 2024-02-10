package com.arttttt.rotationcontrolv3.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.arttttt.navigation.MenuAppNavigator
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
import com.arttttt.rotationcontrolv3.ui.main.di.DaggerMainComponent
import com.arttttt.rotationcontrolv3.ui.main.di.MainComponentDependencies
import com.arttttt.rotationcontrolv3.ui.rotation.RotationService
import com.arttttt.rotationcontrolv3.utils.behavior.BottomAppBarBehavior
import com.arttttt.rotationcontrolv3.utils.navigation.NavigationContainerDelegate
import com.arttttt.rotationcontrolv3.utils.navigationdialog.NavigationDialog
import com.arttttt.utils.unsafeCastTo
import com.github.terrakok.cicerone.NavigatorHolder
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume


class MainFragment(
    private val dependencies: MainComponentDependencies,
) : Fragment(R.layout.fragment_main) {

    sealed class MenuItem : NavigationDialog.Item {

        data object Settings : MenuItem() {

            override val id: Int = R.id.item_settings
            override val title: CharSequence = "Settings"
        }

        data object About : MenuItem() {

            override val id: Int = R.id.item_about
            override val title: CharSequence = "About"
        }
    }

    companion object {

        fun provider(dependencies: MainComponentDependencies): FragmentProvider = FragmentProvider {
            MainFragment(dependencies)
        }
    }

    private val containerDelegate by lazy {
        NavigationContainerDelegate(
            context = requireContext(),
        )
    }

    private val navigator by lazy {
        MenuAppNavigator(
            activity = requireActivity(),
            containerId = containerDelegate.containerId,
            fragmentManager = childFragmentManager,
        )
    }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var coordinator: MainCoordinator

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

    /**
     * todo: do it in a proper way
     */
    private var selectedMenuItem: NavigationDialog.Item = MenuItem.Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        containerDelegate.initialize(savedInstanceState)

        DaggerMainComponent
            .factory()
            .create(
                dependencies = dependencies,
                activity = requireActivity().unsafeCastTo()
            )
            .inject(this)

        childFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) return

        coordinator.start()
    }

    @Suppress("NAME_SHADOWING")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState) ?: return null

        val container = containerDelegate.createContainerView()
        view.unsafeCastTo<ViewGroup>().addView(container)
        container.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            behavior = BottomAppBarBehavior()
        }

        return view
    }

    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val launchServiceButton = view.findViewById<FloatingActionButton>(R.id.launchServiceButton)

        view.unsafeCastTo<ViewGroup>().bringChildToFront(launchServiceButton)

        launchServiceButton.setOnClickListener {
            job?.cancel()

            job = lifecycleScope.launch {
                val isAllPermissionsGranted = checkAndGetPermissions()

                if (!isAllPermissionsGranted) return@launch

                val isServiceRunning = RotationService.status.value == RotationService.Status.RUNNING

                if (isServiceRunning) {
                    stopRotationService()
                } else {
                    startRotationService()
                }
            }
        }

        val bottomAppBar = view.findViewById<BottomAppBar>(R.id.bottomAppBar)
        bottomAppBar.setNavigationOnClickListener {
            NavigationDialog.show(
                context = requireContext(),
                items = setOf(
                    MenuItem.Settings,
                    MenuItem.About,
                ),
                itemClickListener = { item ->
                    coordinator.handleMenuClick(item)
                    selectedMenuItem = item

                    when (item) {
                        is MenuItem.Settings -> launchServiceButton.show()
                        is MenuItem.About -> launchServiceButton.hide()
                    }
                },
                selectedItem = selectedMenuItem,
            )
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            RotationService
                .status
                .map { status -> status.toFabIconRes() }
                .onEach(launchServiceButton::setImageResource)
                .launchIn(this)
        }
    }

    override fun onResume() {
        super.onResume()

        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()

        navigatorHolder.removeNavigator()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        containerDelegate.saveState(outState)
    }

    private fun MainCoordinator.handleMenuClick(item: NavigationDialog.Item) {
        when (item) {
            is MenuItem.About -> showAbout()
            is MenuItem.Settings -> showSettings()
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
        message: Int,
    ) {
        return suspendCancellableCoroutine { continuation ->
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.notice_text)
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
}