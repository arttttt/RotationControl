package com.arttttt.rotationcontrolv3.ui.container.di

import com.arttttt.navigation.FlowMenuRouter
import com.arttttt.navigation.factory.FragmentProvider
import com.arttttt.rotationcontrolv3.di.keys.FragmentClassKey
import com.arttttt.rotationcontrolv3.di.qualifiers.RootRouterQualifier
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.main2.platform.MainFragment2
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
    @FragmentClassKey(MainFragment2::class)
    fun provideMainFragment2Provider(component: ContainerComponent): FragmentProvider {
        return MainFragment2.provider(component)
    }
}