package com.arttttt.rotationcontrolv3.data.repository

import android.content.Context
import android.provider.Settings
import androidx.core.hardware.display.DisplayManagerCompat
import com.arttttt.rotationcontrolv3.domain.entity.rotation.OrientationMode
import com.arttttt.rotationcontrolv3.domain.repository.OrientationRepository
import com.arttttt.utils.toBoolean
import javax.inject.Inject

class OrientationRepositoryImpl @Inject constructor(
    private val context: Context
) : OrientationRepository {

    override fun setOrientation(mode: OrientationMode) {
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.USER_ROTATION,
            mode.value,
        )
    }

    override fun getSystemOrientation(): OrientationMode {
        val isAccelerometerEnabled = Settings.System
            .getInt(
                context.contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
            )
            .toBoolean()

        return when {
            isAccelerometerEnabled -> OrientationMode.Auto
            else -> DisplayManagerCompat
                .getInstance(context)
                .getDisplay(0)
                ?.rotation
                ?.let(OrientationMode::of)
                ?: OrientationMode.Auto
        }
    }
}