package com.arttttt.rotationcontrolv3.di.modules;

import androidx.fragment.app.Fragment;

import com.arttttt.navigation.factory.CustomFragmentFactory;
import com.arttttt.navigation.factory.FragmentProvider;
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen;

import java.util.Map;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentFactoryModuleJava {

    @Provides
    @PerScreen
    static CustomFragmentFactory provideFragmentFactory(
            Map<Class<? extends Fragment>, FragmentProvider> providers
    ) {
        return new CustomFragmentFactory(providers);
    }
}
