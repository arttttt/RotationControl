package com.arttttt.rotationcontrolv3.presentation.base

import com.arttttt.rotationcontrolv3.utils.FLOW_ROUTER
import com.arttttt.rotationcontrolv3.utils.navigation.FlowRouter
import org.koin.core.inject
import org.koin.core.qualifier.named

abstract class BaseFlowPresentationModel: BasePresentationModel() {
    override val router: FlowRouter by inject(named(FLOW_ROUTER))
}