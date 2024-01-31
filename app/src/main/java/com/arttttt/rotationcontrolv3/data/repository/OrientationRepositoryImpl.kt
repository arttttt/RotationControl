package com.arttttt.rotationcontrolv3.data.repository

import android.content.Context
import android.provider.Settings
import com.arttttt.rotationcontrolv3.domain.entity.OrientationMode
import com.arttttt.rotationcontrolv3.domain.repository.OrientationRepository
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
}