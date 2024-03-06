package com.arttttt.rotationcontrolv3.data.repository

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.provider.Settings
import com.arttttt.rotationcontrolv3.domain.entity.rotation.RotationStatus
import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class SensorsRepositoryImpl @Inject constructor(
    private val context: Context,
) : SensorsRepository {

    @OptIn(DelicateCoroutinesApi::class)
    private val accelerometerFlow = callbackFlow {
        val observer = object: ContentObserver(null) {

            override fun onChange(selfChange: Boolean, uri: Uri?) {
                uri ?: return

                val value = Settings.System.getInt(
                    context.contentResolver,
                    uri.schemeSpecificPart.substringAfterLast('/'),
                )

                trySend(
                    if (value == 0) {
                        RotationStatus.Disabled
                    } else {
                        RotationStatus.Enabled
                    }
                )
            }
        }

        context.contentResolver.registerContentObserver(
            Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION),
            false,
            observer
        )
        awaitClose {
            context.contentResolver.unregisterContentObserver(observer)
        }
    }
        .shareIn(GlobalScope, SharingStarted.WhileSubscribed())

    override fun enableRotation() {
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            1,
        )
    }

    override fun disableRotation() {
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            0,
        )
    }

    override fun getRotationStatuses(): Flow<RotationStatus> {
        return accelerometerFlow
    }
}