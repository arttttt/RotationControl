package com.arttttt.rotationcontrolv3.di

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.reflect.KClass

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.CLASS
)
@Retention(
    AnnotationRetention.RUNTIME
)
@MapKey
annotation class FragmentClassKey(val value: KClass<out Fragment>)