package com.arttttt.rotationcontrolv3.ui.main.di

import com.arttttt.navigation.FlowMenuRouter
import com.arttttt.navigation.factory.FragmentProvider
import com.arttttt.rotationcontrolv3.di.keys.FragmentClassKey
import com.arttttt.rotationcontrolv3.di.qualifiers.RootRouterQualifier
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.about.AboutFragment
import com.arttttt.rotationcontrolv3.ui.settings.platform.SettingsFragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
object MainModule {

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
    @IntoMap
    @FragmentClassKey(AboutFragment::class)
    fun provideAboutFragmentProvider(component: MainComponent): FragmentProvider {
        return AboutFragment.provider(component)
    }

    @Provides
    @IntoMap
    @FragmentClassKey(SettingsFragment::class)
    fun provideSettingsFragmentProvider(component: MainComponent): FragmentProvider {
        return SettingsFragment.provider(component)
    }
}