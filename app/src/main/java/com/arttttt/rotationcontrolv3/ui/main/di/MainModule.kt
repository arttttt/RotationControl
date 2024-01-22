package com.arttttt.rotationcontrolv3.ui.main.di

import com.arttttt.navigation.FlowMenuRouter
import com.arttttt.rotationcontrolv3.di.qualifiers.RootRouterQualifier
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import dagger.Module
import dagger.Provides

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
}