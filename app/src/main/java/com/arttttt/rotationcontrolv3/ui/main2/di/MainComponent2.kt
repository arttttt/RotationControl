package com.arttttt.rotationcontrolv3.ui.main2.di

import androidx.appcompat.app.AppCompatActivity
import com.arttttt.rotationcontrolv3.di.modules.FragmentFactoryModuleJava
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.about.di.AboutComponentDependencies
import com.arttttt.rotationcontrolv3.ui.main2.MainFragment2
import com.arttttt.rotationcontrolv3.ui.settings.di.SettingsDependencies
import dagger.BindsInstance
import dagger.Component

@PerScreen
@Component(
    dependencies = [
        MainComponentDependencies2::class,
    ],
    modules = [
        MainModule2::class,
        FragmentFactoryModuleJava::class,
    ]
)
interface MainComponent2 : AboutComponentDependencies, SettingsDependencies {

    @Component.Factory
    interface Factory {

        fun create(
            dependencies: MainComponentDependencies2,
            @BindsInstance activity: AppCompatActivity,
        ): MainComponent2
    }

    fun inject(fragment: MainFragment2)
}