package com.arttttt.rotationcontrolv3.domain.repository

import com.arttttt.rotationcontrolv3.domain.entity.OrientationMode

interface OrientationRepository {

    fun setOrientation(mode: OrientationMode)
}