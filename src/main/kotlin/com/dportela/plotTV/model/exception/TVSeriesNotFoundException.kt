package com.dportela.plotTV.model.exception

class TVSeriesNotFoundException (
    override val message: String
) : RuntimeException(message)