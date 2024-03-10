package com.arttttt.rotationcontrolv3.ui.main2.di

import androidx.appcompat.app.AppCompatActivity
import com.arttttt.navigation.FlowMenuRouter
import com.arttttt.navigation.factory.FragmentProvider
import com.arttttt.permissions.data.framework.PermissionsRequesterImpl
import com.arttttt.permissions.domain.entity.StandardPermission
import com.arttttt.permissions.domain.repository.PermissionsRequester
import com.arttttt.permissions.presentation.handlers.StandardPermissionHandler
import com.arttttt.permissions.presentation.handlers.StartForResultPermissionHandler
import com.arttttt.rotationcontrolv3.framework.model.DrawOverlayPermission
import com.arttttt.rotationcontrolv3.framework.model.WriteSettingsPermission
import com.arttttt.rotationcontrolv3.di.keys.FragmentClassKey
import com.arttttt.rotationcontrolv3.di.qualifiers.RootRouterQualifier
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.about.AboutFragment
import com.arttttt.rotationcontrolv3.ui.apps.platform.AppsFragment
import com.arttttt.rotationcontrolv3.ui.settings.platform.SettingsFragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
object MainModule2 {

    @Provides
    @PerScreen
    fun provideCicerone(
        @RootRouterQualifier rootRouter: FlowMenuRouter
    ): Cicerone<FlowMenuRouter> {
        return Cicerone.create(FlowMenuRouter(rootRouter))
    }

    @Provides
    @PerScreen
    fun provideRouter(cicerone: Cicerone<FlowMenuRouter>): FlowMenuRouter {
        return cicerone.router
    }

    @Provides
    @PerScreen
    fun providerNavigatorHolder(cicerone: Cicerone<FlowMenuRouter>): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }

    @Provides
    @PerScreen
    fun providePermissionsRequester(
        activity: AppCompatActivity,
    ): PermissionsRequester {
        return PermissionsRequesterImpl(
            activity = activity,
            handlers = mapOf(
                StandardPermission::class to StandardPermissionHandler(),
                DrawOverlayPermission::class to StartForResultPermissionHandler<DrawOverlayPermission>(),
                WriteSettingsPermission::class to StartForResultPermissionHandler<WriteSettingsPermission>(),
            ),
        )
    }

    @Provides
    @IntoMap
    @FragmentClassKey(AboutFragment::class)
    fun provideAboutFragmentProvider(component: MainComponent2): FragmentProvider {
        return AboutFragment.provider(component)
    }

    @Provides
    @IntoMap
    @FragmentClassKey(SettingsFragment::class)
    fun provideSettingsFragmentProvider(component: MainComponent2): FragmentProvider {
        return SettingsFragment.provider(component)
    }

    @Provides
    @IntoMap
    @FragmentClassKey(AppsFragment::class)
    fun provideAppsFragmentProvider(component: MainComponent2): FragmentProvider {
        return AppsFragment.provider(component)
    }
}