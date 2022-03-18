package com.dportela.plotTV.model.exception

class ScrappingUnavailableException(
    override val message: String
) : RuntimeException(message)