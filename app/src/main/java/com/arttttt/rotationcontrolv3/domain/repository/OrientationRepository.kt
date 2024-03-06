package com.arttttt.rotationcontrolv3.domain.repository

import com.arttttt.rotationcontrolv3.domain.entity.rotation.OrientationMode

interface OrientationRepository {

    fun setOrientation(mode: OrientationMode)

    fun getSystemOrientation(): OrientationMode
}