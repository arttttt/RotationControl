package com.arttttt.rotationcontrolv3.utils.extensions.koin

import org.koin.core.context.GlobalContext
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

inline fun<reified T> Module.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    return GlobalContext.get().koin.get(qualifier, parameters)
}