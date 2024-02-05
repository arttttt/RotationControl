package com.arttttt.rotationcontrolv3.ui.main.di

import androidx.appcompat.app.AppCompatActivity
import com.arttttt.rotationcontrolv3.di.modules.FragmentFactoryModuleJava
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.about.di.AboutComponentDependencies
import com.arttttt.rotationcontrolv3.ui.main.MainFragment
import com.arttttt.rotationcontrolv3.ui.settings.di.SettingsDependencies
import dagger.BindsInstance
import dagger.Component

@PerScreen
@Component(
    dependencies = [
        MainComponentDependencies::class,
    ],
    modules = [
        MainModule::class,
        FragmentFactoryModuleJava::class,
    ]
)
interface MainComponent : AboutComponentDependencies, SettingsDependencies {

    @Component.Factory
    interface Factory {

        fun create(
            dependencies: MainComponentDependencies,
            @BindsInstance activity: AppCompatActivity,
        ): MainComponent
    }

    fun inject(fragment: MainFragment)
}