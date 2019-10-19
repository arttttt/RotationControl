package com.arttttt.rotationcontrolv3.utils.extensions.koin

import org.koin.core.Koin

inline fun<reified T> Koin.isDefinitionDeclared(): Boolean {
    return rootScope.beanRegistry.getDefinition(T::class) != null
}