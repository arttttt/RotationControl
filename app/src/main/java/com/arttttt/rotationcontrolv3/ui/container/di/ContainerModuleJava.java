package com.arttttt.rotationcontrolv3.ui.container.di;

import androidx.fragment.app.Fragment;

import com.arttttt.navigation.factory.CustomFragmentFactory;
import com.arttttt.navigation.factory.FragmentProvider;
import com.arttttt.rotationcontrolv3.di.FragmentClassKey;
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen;
import com.arttttt.rotationcontrolv3.ui.main.MainFragment;

import java.util.HashMap;
import java.util.Map;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ContainerModuleJava {

    @Provides
    @PerScreen
    static CustomFragmentFactory provideFragmentFactory(
            Map<Class<? extends Fragment>, FragmentProvider> providers
    ) {
        return new CustomFragmentFactory(providers);
    }
}
