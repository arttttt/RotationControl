package com.arttttt.rotationcontrolv3.device.services.delegate

import com.arttttt.rotationcontrolv3.device.services.rotation.RotationService

interface IWindowDelegate {
    fun createOrUpdateWindow(orientation: RotationService.Orientation)
    fun removeWindow()
}