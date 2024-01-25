package com.arttttt.rotationcontrolv3.ui.container.di

import com.arttttt.navigation.FlowMenuRouter
import com.arttttt.navigation.factory.FragmentProvider
import com.arttttt.rotationcontrolv3.di.keys.FragmentClassKey
import com.arttttt.rotationcontrolv3.di.qualifiers.RootRouterQualifier
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.main.MainFragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
object ContainerModule {

    @Provides
    @PerScreen
    fun provideCicerone(): Cicerone<FlowMenuRouter> {
        return Cicerone.create(FlowMenuRouter(null))
    }

    @Provides
    @PerScreen
    @RootRouterQualifier
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
    @FragmentClassKey(MainFragment::class)
    fun provideMainFragmentProvider(component: ContainerComponent): FragmentProvider {
        return MainFragment.provider(component)
    }
}