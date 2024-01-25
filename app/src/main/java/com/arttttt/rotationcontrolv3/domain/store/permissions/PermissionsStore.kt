package com.arttttt.rotationcontrolv3.domain.store.permissions

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.permissions.domain.entity.Permission
import kotlin.reflect.KClass

interface PermissionsStore : Store<PermissionsStore.Intent, PermissionsStore.State, PermissionsStore.Label> {

    data class State(
        val isInProgress: Boolean,
        val permissions: Map<Permission.Status, Map<KClass<out Permission>, Permission>>,
    ) {

        val grantedPermissions: List<Permission>
            get() {
                return permissions
                    .getOrElse(Permission.Status.Granted) { emptyMap() }
                    .values
                    .toList()
            }

        val deniedPermissions: List<Permission>
            get() {
                return permissions
                    .getOrElse(Permission.Status.Denied) { emptyMap() }
                    .values
                    .toList()
            }
    }

    sealed class Action {

        data object GetRequestedPermissions : Action()
    }

    sealed class Intent {

        data class RequestPermission(
            val permission: KClass<out Permission>,
        ) : Intent()

        data object CheckPermissions : Intent()
    }

    sealed class Message {

        data object ProgressStarted : Message()
        data object ProgressFinished : Message()
        data class PermissionsReceived(
            val permissions: Map<Permission.Status, Map<KClass<out Permission>, Permission>>,
        ) : Message()
    }

    sealed class Label {

        data object AllPermissionsGranted : Label()
    }
}