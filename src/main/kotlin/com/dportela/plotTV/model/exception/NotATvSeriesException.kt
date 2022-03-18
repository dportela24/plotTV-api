package com.dportela.plotTV.model.exception

class NotATvSeriesException(
    override val message: String
) : RuntimeException(message) {
}