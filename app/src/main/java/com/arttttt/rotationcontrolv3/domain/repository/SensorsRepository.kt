package com.arttttt.rotationcontrolv3.domain.repository

import com.arttttt.rotationcontrolv3.domain.entity.RotationStatus
import kotlinx.coroutines.flow.Flow

interface SensorsRepository {

    fun enableRotation()
    fun disableRotation()

    fun getRotationStatuses(): Flow<RotationStatus>
}