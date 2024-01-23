package com.arttttt.permissions.domain.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.permissions.domain.entity.Permission2
import kotlin.reflect.KClass

interface PermissionsStore : Store<PermissionsStore.Intent, PermissionsStore.State, PermissionsStore.Label> {

    data class State(
        val isInProgress: Boolean,
        val permissions: Map<Permission2.Status, Map<KClass<out Permission2>, Permission2>>,
    ) {

        val grantedPermissions: List<Permission2>
            get() {
                return permissions
                    .getOrElse(Permission2.Status.Granted) { emptyMap() }
                    .values
                    .toList()
            }

        val deniedPermissions: List<Permission2>
            get() {
                return permissions
                    .getOrElse(Permission2.Status.Denied) { emptyMap() }
                    .values
                    .toList()
            }
    }

    sealed class Action {

        data object GetRequestedPermissions : Action()
    }

    sealed class Intent {

        data class RequestPermission(
            val permission: KClass<out Permission2>,
        ) : Intent()

        data object CheckPermissions : Intent()
    }

    sealed class Message {

        data object ProgressStarted : Message()
        data object ProgressFinished : Message()
        data class PermissionsReceived(
            val permissions: Map<Permission2.Status, Map<KClass<out Permission2>, Permission2>>,
        ) : Message()
    }

    sealed class Label {

        data object AllPermissionsGranted : Label()
    }
}